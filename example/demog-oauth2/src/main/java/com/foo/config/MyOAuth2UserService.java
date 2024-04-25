package com.foo.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class MyOAuth2UserService extends DefaultOAuth2UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(MyOAuth2UserService.class);
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	
		logger.info(userRequest.getClientRegistration().getRegistrationId());
		
		OAuth2User user = super.loadUser(userRequest);		
		
		user.getAttributes().forEach((k,v)-> System.out.println(k + "=" + v));
		user.getAuthorities().forEach(grantedAuth -> System.out.println(grantedAuth));				
		
		return user;
	}	
	
}
