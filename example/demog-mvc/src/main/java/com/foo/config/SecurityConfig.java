package com.foo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	
	@Autowired  
	private DataSource dataSource;

			
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(new String[]{"/sayhello", "/resources/**"}).permitAll()				
				.anyRequest().authenticated())
		    .httpBasic(Customizer.withDefaults())
			.formLogin(Customizer.withDefaults())
			.exceptionHandling(t -> t.accessDeniedPage("/resources/error.html"));		 
		    		
		return http.build();
	}	
		
	@Bean
	public UserDetailsService jdbcUserDetailsService() {		
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		userDetailsManager.setUsersByUsernameQuery("SELECT USER_ID,USER_PASS,TRUE FROM MEMBERS WHERE USER_ID = ?");
		userDetailsManager.setAuthoritiesByUsernameQuery("SELECT USER_ID, ROLE_ID FROM MEMBERS_ROLES WHERE USER_ID = ?");
		
		return userDetailsManager;
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();		
//		
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		UserDetails userDetails = User.withUsername("scott")
//				                      .passwordEncoder((p)-> encoder.encode(p))
//		                              //.password(encoder.encode("5555"))
//				                      .password("5555")
//		                              .roles("USER")
//		                              .build();
//		
//		manager.createUser(userDetails);		
//		return manager;
//	}
	
}
