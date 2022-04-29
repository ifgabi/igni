package gg.igni.igniserver.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import gg.igni.igniserver.account.service.IgniOAuth2UserService;
import gg.igni.igniserver.account.service.IgniUserDetailsService;
import gg.igni.igniserver.model.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
	}

  @Autowired
	private IgniUserDetailsService userDetailsService;

  @Autowired
  private IgniOAuth2UserService igniOAuth2UserService;

  @Override
  protected void configure(AuthenticationManagerBuilder authb) throws Exception
  {
    authb.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors();

    http
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
      .and()
      .authorizeRequests()
      .antMatchers("/chat-websocket/**").authenticated();

    http
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/login").permitAll()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/signup").permitAll()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.GET, "/checksession").permitAll()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.GET, "/streams").permitAll()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.GET, "/stream").permitAll()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.GET, "/history").authenticated()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/addstream").permitAll()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.GET, "/user").authenticated()

      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/setupProfile").authenticated();

      http.oauth2Login(oauth2Login ->
      oauth2Login
          .loginPage("/login/oauth2")
          .authorizationEndpoint(authorizationEndpoint ->
              authorizationEndpoint
                  .baseUri("/login/oauth2/authorization")
          )
          .defaultSuccessUrl("http://localhost:4200", true)
          .userInfoEndpoint()
            .userService(this.igniOAuth2UserService)
            .customUserType(User.class, "google")
      );

      http.csrf().disable();

    //from stackoverflow
    http.logout(logout -> logout
      .permitAll()
      .logoutSuccessHandler((request, response, authentication) ->
      {
        response.setStatus(HttpServletResponse.SC_OK);
      })
    );
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    final CorsConfiguration configurationApi = new CorsConfiguration();
    configurationApi.setExposedHeaders(Arrays.asList("Authorization"));
    configurationApi.addAllowedOrigin("http://localhost:4200");
    configurationApi.addAllowedMethod("*");
    configurationApi.addAllowedHeader("*");
    configurationApi.setAllowCredentials(true);
    configurationApi.applyPermitDefaultValues();
    source.registerCorsConfiguration("/**", configurationApi);

    final CorsConfiguration configurationWs = new CorsConfiguration();
    configurationWs.setExposedHeaders(Arrays.asList("Authorization"));
    configurationWs.addAllowedOrigin("http://localhost:4200");
    configurationWs.addAllowedMethod("*");
    configurationWs.addAllowedHeader("*");
    configurationWs.setAllowCredentials(true);
    configurationWs.applyPermitDefaultValues();
    source.registerCorsConfiguration("/chat-websocket/**", configurationWs);

    return source;
	}
}
