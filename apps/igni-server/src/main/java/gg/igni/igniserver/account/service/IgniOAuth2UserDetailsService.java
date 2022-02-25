package gg.igni.igniserver.account.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.account.model.User;
import gg.igni.igniserver.account.repositories.RoleRepository;
import gg.igni.igniserver.account.repositories.UserRepository;

@Service
public class IgniOAuth2UserDetailsService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
    OAuth2User oau = defaultOAuth2UserService.loadUser(userRequest);

    String username = ((String)oau.getAttribute("email")).split("@")[0];
    String email = oau.getAttribute("email");

    //decision: unique email, unique username
    //both at the same time independently unique

    User user = userRepository.findByEmail(email)
    .orElseGet(() -> {
      String finalUsername = username;
      while(userRepository.findByUsername(finalUsername).isPresent())
      {
        finalUsername = finalUsername + "1";
      };

      User newUser = new User();
      newUser.setUsername(finalUsername);
      newUser.setEmail(email);
      newUser.setEnabled(true);
      newUser.setRoles(Set.of(roleRepository.findByName("ROLE_USER").get()));
      return userRepository.save(newUser);
    });

    return user;
  }

}
