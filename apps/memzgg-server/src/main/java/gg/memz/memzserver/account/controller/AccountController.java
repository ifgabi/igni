package gg.memz.memzserver.account.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import gg.memz.memzserver.account.data.AccountConstraints;
import gg.memz.memzserver.account.data.LoginRequestDto;
import gg.memz.memzserver.account.data.LoginResponseDto;
import gg.memz.memzserver.account.data.SessionHeartbeatResponseDto;
import gg.memz.memzserver.account.data.SignUpRequestDto;
import gg.memz.memzserver.account.data.SignUpResponseDto;
import gg.memz.memzserver.account.data.UserDto;
import gg.memz.memzserver.account.model.User;
import gg.memz.memzserver.account.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	private AccountService accountService;

	@PostMapping("/signup")
	@ResponseBody
	public ResponseEntity<SignUpResponseDto> signup(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SignUpRequestDto reqbody) throws NullPointerException {

		SignUpResponseDto respbody = new SignUpResponseDto();

		//no reqbody? spring internal error
		if(reqbody == null)
			throw new NullPointerException("RequestBody is null in signUp endpoint.");

		//empty client data, shall not be handled
		if(reqbody.getUsername() == null)
			return new ResponseEntity<SignUpResponseDto>(respbody, HttpStatus.BAD_REQUEST);

		if(reqbody.getPassword() == null)
			return new ResponseEntity<SignUpResponseDto>(respbody, HttpStatus.BAD_REQUEST);

		if(reqbody.getEmail() == null)
			return new ResponseEntity<SignUpResponseDto>(respbody, HttpStatus.BAD_REQUEST);

		respbody.setUsernameErrorFlags(accountService.checkUsernameConstraints(reqbody.getUsername()));
		respbody.setPasswordErrorFlags(accountService.checkPasswordConstraints(reqbody.getPassword()));
		respbody.setEmailErrorFlags(accountService.checkEmailConstraints(reqbody.getEmail()));

		// if success username password and email
		if(respbody.getUsernameErrorFlags().size() == 0 && respbody.getPasswordErrorFlags().size() == 0 && respbody.getEmailErrorFlags().size() == 0)
		{
			// logout first
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null)
				new SecurityContextLogoutHandler().logout(request, response, auth);

			request.getSession(true);

			// create user and then log him in
			respbody.setUserData(accountService.createAccount(reqbody.getUsername(), reqbody.getPassword(), reqbody.getEmail()).orElse(null));

			LoginRequestDto loginreq = new LoginRequestDto();
			loginreq.setUsername(reqbody.getUsername());
			loginreq.setPassword(reqbody.getPassword());
			login(loginreq, request);

			return new ResponseEntity<SignUpResponseDto>(respbody, HttpStatus.OK);
		}

		//if there is a problem with the data received over the wire it's client's fault: 400
		if(respbody.getUsernameErrorFlags().contains(AccountConstraints.UsernameConstraintErrorFlag.ERROR) || respbody.getPasswordErrorFlags().contains(AccountConstraints.PasswordConstraintErrorFlag.ERROR) || respbody.getEmailErrorFlags().contains(AccountConstraints.EmailConstraintErrorFlag.ERROR))
			return new ResponseEntity<SignUpResponseDto>(respbody, HttpStatus.BAD_REQUEST);

		//non-critical ERRORs
		return new ResponseEntity<SignUpResponseDto>(respbody, HttpStatus.OK);
	}

	// @PreAuthorize("hasAuthority('PRIVILEGE_USERS_READ')")
	// @GetMapping("/user")
	// @ResponseBody
	// public UserDto getUser(@RequestParam(name = "username") String username) {
	// 	Optional<UserDto> instance = userService.findByUsername(username);

	// 	return instance.orElse(null);
	// }

	@GetMapping("/checksession")
	@ResponseBody
	public ResponseEntity<SessionHeartbeatResponseDto> checkSession(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth == null)
			return new ResponseEntity<SessionHeartbeatResponseDto>(new SessionHeartbeatResponseDto(false), HttpStatus.OK);

		if(auth instanceof AnonymousAuthenticationToken)
      return new ResponseEntity<SessionHeartbeatResponseDto>(new SessionHeartbeatResponseDto(false), HttpStatus.OK);

		return new ResponseEntity<SessionHeartbeatResponseDto>(new SessionHeartbeatResponseDto(true), HttpStatus.OK);
	}

	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest, HttpServletRequest request) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		if (username == null || password == null)
			return new ResponseEntity<LoginResponseDto>(HttpStatus.BAD_REQUEST);

		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();

		if(auth != null)
			if(!(auth instanceof AnonymousAuthenticationToken))
				if(auth.isAuthenticated())
					return new ResponseEntity<LoginResponseDto>(HttpStatus.BAD_REQUEST); //already logged in returns false

		//invalidate session to prevent spoofing
		//also serves for client-side complete reset
		HttpSession sess = request.getSession();

		if (sess != null)
			if(!sess.isNew())
				sess.invalidate();

    Optional<Authentication> newAuth = accountService.loginAccount(username, password);

    if(newAuth.orElse(null) == null)
    {
      return new ResponseEntity<LoginResponseDto>(HttpStatus.UNAUTHORIZED);
    }

    //important: creates a session
		sess = request.getSession(true);

		sc.setAuthentication(newAuth.get());

    User user = (User) newAuth.get().getPrincipal();
    UserDto userdto = new UserDto();

    //use mapper
    ModelMapper mm = new ModelMapper();
    userdto = mm.map(user, UserDto.class);

    LoginResponseDto respDto = new LoginResponseDto();
		respDto.setUserData(userdto);
		respDto.setUsername(username);
		return new ResponseEntity<LoginResponseDto>(respDto, HttpStatus.OK);
	}
}
