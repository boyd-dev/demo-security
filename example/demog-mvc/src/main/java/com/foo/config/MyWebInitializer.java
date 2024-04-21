package com.foo.config;

import javax.servlet.Filter;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
//		return new Class<?>[] {
//			AppConfig.class		
//		};
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {
			WebConfig.class,
			SecurityConfig.class,
			AppConfig.class
			
		};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	@Override
	protected Filter[] getServletFilters() {
		
		CharacterEncodingFilter cef = new CharacterEncodingFilter();
		cef.setEncoding("UTF-8");
		cef.setForceEncoding(true);
		
		return new Filter[] {new HiddenHttpMethodFilter(), cef};
		
	}
	
	
//	public static class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
//
//		@Override
//		protected String getDispatcherWebApplicationContextSuffix() {
//			return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
//		}
//	}
}
