package gg.igni.igniserver.account.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import gg.igni.igniserver.account.data.AccountConstraints;
import gg.igni.igniserver.account.data.LoginReqDto;
import gg.igni.igniserver.account.data.LoginRespDto;
import gg.igni.igniserver.account.data.SessionHeartbeatResponseDto;
import gg.igni.igniserver.account.data.SetupProfileReqDto;
import gg.igni.igniserver.account.data.SetupProfileRespDto;
import gg.igni.igniserver.account.data.SignUpReqDto;
import gg.igni.igniserver.account.data.SignUpRespDto;
import gg.igni.igniserver.account.data.UserDto;
import gg.igni.igniserver.account.data.AccountConstraints.UsernameConstraintErrorFlag;
import gg.igni.igniserver.account.service.IAccountService;
import gg.igni.igniserver.model.User;

@Controller
public class AccountController {

	@Autowired
  @Qualifier("accountService")
	private IAccountService accountService;

	@PostMapping("/signup")
	public ResponseEntity<SignUpRespDto> signup(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SignUpReqDto reqbody) throws NullPointerException {

		SignUpRespDto respbody = new SignUpRespDto();

		//reqbody missing, doesnt happen
		if(reqbody == null)
			throw new NullPointerException("RequestBody is null in signUp endpoint.");

		//empty client data, shall not be handled
		if(reqbody.getUsername() == null)
			return new ResponseEntity<SignUpRespDto>(respbody, HttpStatus.BAD_REQUEST);

		if(reqbody.getPassword() == null)
			return new ResponseEntity<SignUpRespDto>(respbody, HttpStatus.BAD_REQUEST);

		if(reqbody.getEmail() == null)
			return new ResponseEntity<SignUpRespDto>(respbody, HttpStatus.BAD_REQUEST);

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

			LoginReqDto loginreq = new LoginReqDto();
			loginreq.setUsername(reqbody.getUsername());
			loginreq.setPassword(reqbody.getPassword());
			login(loginreq, request);

			return new ResponseEntity<SignUpRespDto>(respbody, HttpStatus.OK);
		}

		//if there is a problem with the data received over the wire it's client's fault: 400
		if(respbody.getUsernameErrorFlags().contains(AccountConstraints.UsernameConstraintErrorFlag.ERROR) || respbody.getPasswordErrorFlags().contains(AccountConstraints.PasswordConstraintErrorFlag.ERROR) || respbody.getEmailErrorFlags().contains(AccountConstraints.EmailConstraintErrorFlag.ERROR))
			return new ResponseEntity<SignUpRespDto>(respbody, HttpStatus.BAD_REQUEST);

		//non-critical ERRORs
		return new ResponseEntity<SignUpRespDto>(respbody, HttpStatus.OK);
	}

	// @PreAuthorize("hasAuthority('PRIVILEGE_USERS_READ')")
	// @GetMapping("/user")
	// @ResponseBody
	// public UserDto getUser(@RequestParam(name = "username") String username) {
	// 	Optional<UserDto> instance = userService.findByUsername(username);

	// 	return instance.orElse(null);
	// }

	@GetMapping("/checksession")
	public ResponseEntity<SessionHeartbeatResponseDto> checkSession(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth == null)
			return new ResponseEntity<SessionHeartbeatResponseDto>(new SessionHeartbeatResponseDto(false), HttpStatus.OK);

		if(auth instanceof AnonymousAuthenticationToken)
      return new ResponseEntity<SessionHeartbeatResponseDto>(new SessionHeartbeatResponseDto(false), HttpStatus.OK);

    User user = (User) auth.getPrincipal();
    SessionHeartbeatResponseDto dto = new SessionHeartbeatResponseDto(true);
    ModelMapper mm = new ModelMapper();
    dto.setUser(mm.map(user, UserDto.class));
		return new ResponseEntity<SessionHeartbeatResponseDto>(dto, HttpStatus.OK);
	}

  //this endpoint is strictly for http login, the oauth2 google auth is done automatically by the authprovider provided
	@PostMapping("/login")
	public ResponseEntity<LoginRespDto> login(@RequestBody LoginReqDto loginRequest, HttpServletRequest request) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		if (username == null || password == null)
			return new ResponseEntity<LoginRespDto>(HttpStatus.BAD_REQUEST);

		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();

		if(auth != null)
			if(!(auth instanceof AnonymousAuthenticationToken))
				if(auth.isAuthenticated())
					return new ResponseEntity<LoginRespDto>(HttpStatus.BAD_REQUEST); //already logged in returns false

		//invalidate session to prevent spoofing
		//also serves for client-side complete reset
		HttpSession sess = request.getSession();

		if (sess != null)
			if(!sess.isNew())
				sess.invalidate();

    Optional<Authentication> newAuth = accountService.loginAccount(username, password);

    if(newAuth.isEmpty())
    {
      return new ResponseEntity<LoginRespDto>(HttpStatus.UNAUTHORIZED);
    }

    //important: creates a session
		sess = request.getSession(true);

		sc.setAuthentication(newAuth.get());

    User user = (User) newAuth.get().getDetails();
    UserDto userdto = new UserDto();

    //use mapper
    ModelMapper mm = new ModelMapper();
    userdto = mm.map(user, UserDto.class);

    LoginRespDto respDto = new LoginRespDto();
		respDto.setUserData(userdto);
		respDto.setUsername(username);
		return new ResponseEntity<LoginRespDto>(respDto, HttpStatus.OK);
	}

  @PostMapping(value = "/setupProfile")
  @ResponseBody
  public ResponseEntity<SetupProfileRespDto> setupProfile(@RequestBody SetupProfileReqDto reqBody)
  {
    SetupProfileRespDto respDto = new SetupProfileRespDto();

    if(reqBody == null)
    throw new NullPointerException("RequestBody is null in setupProfile endpoint.");

    //empty client data, shall not be handled
    if(reqBody.getUsername() == null)
      return new ResponseEntity<SetupProfileRespDto>(respDto, HttpStatus.BAD_REQUEST);

    if(reqBody.getNewUsername() == null)
      return new ResponseEntity<SetupProfileRespDto>(respDto, HttpStatus.BAD_REQUEST);


    List<UsernameConstraintErrorFlag> usernameErrors = accountService.checkUsernameConstraints(reqBody.getNewUsername());
    respDto.setUsernameErrorFlags(usernameErrors);

    if(usernameErrors.size() == 0)
    {
      Optional<UserDto> userDto = accountService.setupProfile(reqBody.getUsername(), reqBody.getNewUsername());
      if(userDto.isPresent())
      {
        respDto.setUser(userDto.get());
        return new ResponseEntity<SetupProfileRespDto>(respDto, HttpStatus.ACCEPTED);
      }
    }

    return new ResponseEntity<SetupProfileRespDto>(respDto, HttpStatus.BAD_REQUEST);
  }
}
