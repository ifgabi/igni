package gg.igni.igniserver.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import gg.igni.igniserver.account.model.User;
import gg.igni.igniserver.account.repositories.UserRepository;

@Component
public class AuthProvider implements AuthenticationProvider {

	@Autowired
	UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication)
    throws AuthenticationException {

      String name = authentication.getName();
      String password = authentication.getCredentials().toString();

      User user = userRepository.findByUsername(name).orElse(null);

      if (user == null)
        return null;

      if(passwordEncoder.matches(password, user.getPasswordHash()))
      {
        //return a new UserPasswordAuthenticationToken
          return new UsernamePasswordAuthenticationToken(
              user.getUsername(), user.getPasswordHash(), user.getPrivilegesAsAuthorities()
            );
      }

      return null;
  }

  @Override
  public boolean supports(Class<?> authenticationClass) {
      return authenticationClass.equals(UsernamePasswordAuthenticationToken.class);
  }
}
