package com.eventsApp.eventsApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class EventsAppApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(EventsAppApplication.class, args);
	}

}
