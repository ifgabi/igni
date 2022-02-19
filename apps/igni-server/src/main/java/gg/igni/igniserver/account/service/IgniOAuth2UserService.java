package gg.igni.igniserver.account.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.account.model.User;
import gg.igni.igniserver.account.repositories.UserRepository;

@Service
public class IgniOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  @Autowired
  UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
    OAuth2User oau = defaultOAuth2UserService.loadUser(userRequest);

    String username = oau.getAttribute("name");
    String email = oau.getAttribute("email");

    User user = userRepository.findByUsernameAndEmail(username, email)
    .orElseGet(() -> {
      User newUser = new User();
      newUser.setUsername(username);
      newUser.setEmail(email);
      return userRepository.save(newUser);
    });

    return user;
  }

}
