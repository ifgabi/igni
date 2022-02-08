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

import gg.igni.igniserver.account.service.IgniUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
	}

  @Autowired
	private IgniUserDetailsService userDetailsService;

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
      .antMatchers(HttpMethod.POST, "/addstream").permitAll();


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

    final CorsConfiguration configurationRest = new CorsConfiguration();
    configurationRest.setExposedHeaders(Arrays.asList("Authorization"));
    configurationRest.addAllowedOrigin("http://localhost:4200");
    configurationRest.addAllowedMethod("*");
    configurationRest.addAllowedHeader("*");
    configurationRest.setAllowCredentials(true);
    configurationRest.applyPermitDefaultValues();
    source.registerCorsConfiguration("/**", configurationRest);

    final CorsConfiguration configurationChat = new CorsConfiguration();
    configurationChat.setExposedHeaders(Arrays.asList("Authorization"));
    configurationChat.addAllowedOrigin("http://localhost:4200");
    configurationChat.addAllowedMethod("*");
    configurationChat.addAllowedHeader("*");
    configurationChat.setAllowCredentials(true);
    configurationChat.applyPermitDefaultValues();
    source.registerCorsConfiguration("/chat-websocket/**", configurationChat);

    return source;
	}
}
