package com.foo.myapp;

import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);	
	
	@RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
	public String home(Locale locale, Model model, @AuthenticationPrincipal User user) {
		
		logger.info("Welcome home! The client locale is {}.", locale);		
		logger.info("{} logged in.", user.getUsername());
		
		
		Date date = Date.from(Instant.now());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String d = df.format(date);

		model.addAttribute("serverTime", d);
		
		return "home";
	}

	
	@RequestMapping(value="/main.do")
    public String mainPage(Model model) {
		
		model.addAttribute("name", "아소카");
		return "main";
    }

}
