package com.dino.usersecuritythymeleaf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf().disable()
				.authorizeHttpRequests()
					.requestMatchers("/authenticate").permitAll()
					.requestMatchers("/register").permitAll()
					.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
					.requestMatchers("/employee/**").hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_ADMIN")
					.requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE")
					.anyRequest().authenticated()
				.and()
					.formLogin()
						.loginPage("/authenticate")
						.defaultSuccessUrl("/")
						.permitAll()
				.and()
					.logout()
						.logoutUrl("/logout")
						.permitAll()
				.and()
					.authenticationProvider(authenticationProvider)
				.build();
	}
}
