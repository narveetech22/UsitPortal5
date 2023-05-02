package com.narvee.usit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.web.client.RestTemplate;

import com.narvee.usit.listener.LoginLogoutListener;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableScheduling
public class UsitPortalApplication {
	private static final Logger logger = LoggerFactory.getLogger(UsitPortalApplication.class);
	public static void main(String[] args) {
	
		logger.info(" Application started");
		logger.info("inside class !!!, Method !!!: main Method");
		logger.debug("loading starter page..");
		logger.warn("WARN level message!!");
		logger.error("ERROR level message !!");
		SpringApplication.run(UsitPortalApplication.class, args);
	}
	      @Bean
	    public ApplicationListener<AbstractAuthenticationEvent> loginLogoutListener() { 
	        return new LoginLogoutListener();
	    }
}
