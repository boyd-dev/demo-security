package com.foo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@PropertySource(value = {"classpath:oauth2.properties"})
public class GoogleSuccessLogoutHandler extends SimpleUrlLogoutSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(GoogleSuccessLogoutHandler.class);
	
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService; 
	
	@Value("${google.revokeUri}")
	private String revokeUrl;
	

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		
		OAuth2AccessToken accessToken = authorizedClientService.loadAuthorizedClient("google",authentication.getName()).getAccessToken();
		
		logger.info("user_name={}", authentication.getName());
		logger.info("access token={}", accessToken.getTokenValue());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("token", accessToken.getTokenValue());
		RestOperations restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.exchange(revokeUrl, HttpMethod.POST, new HttpEntity<>(params, headers), String.class);

		if (logger.isDebugEnabled()) {
		    logger.debug(result.getBody());
		    logger.debug(result.getStatusCode().toString());
		}

		setDefaultTargetUrl("/login");
		super.onLogoutSuccess(request, response, authentication);
	}

}

