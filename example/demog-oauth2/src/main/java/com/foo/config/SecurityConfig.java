package com.foo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


@Configuration
@EnableWebSecurity(debug = true)
@PropertySource(value = {"classpath:oauth2.properties"})
public class SecurityConfig {
	
//	@Autowired  
//	private DataSource dataSource;
	
	@Autowired
    private Environment env;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(new String[]{"/resources/**"}).permitAll()
				.anyRequest().authenticated()
			 )		
		    .oauth2Login(oauth2 -> oauth2.clientRegistrationRepository(clientRegistrationRepository())
		    	.authorizedClientService(authorizedClientService(clientRegistrationRepository()))		    	
		    	.redirectionEndpoint(redirect -> redirect.baseUri("/oauth2/callback"))
		    	.userInfoEndpoint(user -> user.userService(oauth2UserService()))
		    	.successHandler(authenticationSuccessHandler())
		     )
		    .logout(logout -> logout.logoutUrl("/signout")
		    		.logoutSuccessHandler(logoutSuccessHandler()));
		return http.build();
	}
	
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		
		ClientRegistration google = ClientRegistration.withRegistrationId("google")
		    .clientId(env.getProperty("google.clientId"))
		    .clientSecret(env.getProperty("google.clientSecret"))
		    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		    .authorizationUri(env.getProperty("google.authorizationUri"))		    
		    .tokenUri(env.getProperty("google.accessTokenUri"))
		    .redirectUri(env.getProperty("google.redirectUri"))
		    .scope(env.getProperty("google.scope").split(","))
		    .userInfoUri(env.getProperty("google.userInfoUri"))
		    .userNameAttributeName("name")
		    .build();
		
		InMemoryClientRegistrationRepository clientRegisterationRepository = new InMemoryClientRegistrationRepository(new ClientRegistration[] {google});
		return clientRegisterationRepository;
	}
	
	
	@Bean
    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
		InMemoryOAuth2AuthorizedClientService authorizedClientService = new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
		return authorizedClientService;
    }
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {		 
		 return new GoogleSuccessLogoutHandler();
	}	 
	
	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {		 
		 return new MyOAuth2UserService();		 
	}
	 
	private AuthenticationSuccessHandler authenticationSuccessHandler() {		 
		 return new MyAuthenticationSuccessHandler();
	}	 
	 
	
	
}
