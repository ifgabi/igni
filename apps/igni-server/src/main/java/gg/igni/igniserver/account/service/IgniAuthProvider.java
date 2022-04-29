package gg.igni.igniserver.account.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import gg.igni.igniserver.account.repositories.UserRepository;
import gg.igni.igniserver.model.User;

@Component
public class IgniAuthProvider implements AuthenticationProvider {

  @Resource
  private IgniUserDetailsService igniUserDetailsService;

	// @Autowired
	// private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication)
    throws AuthenticationException {

      String name = authentication.getName();
      String password = authentication.getCredentials().toString();

      // User user = userRepository.findByUsername(name).orElse(null);
      User user = (User) igniUserDetailsService.loadUserByUsername(name);

      if (user == null)
        return null;

      if(passwordEncoder.matches(password, user.getPasswordHash()))
      {
        //return a new UserPasswordAuthenticationToken
        UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(name, password);
        newToken.setDetails(user);
        return newToken;
      }

      return null;
  }

  @Override
  public boolean supports(Class<?> authenticationClass) {
      return authenticationClass.equals(UsernamePasswordAuthenticationToken.class);
  }
}
