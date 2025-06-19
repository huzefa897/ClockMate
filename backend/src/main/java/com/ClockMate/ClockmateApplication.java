package com.ClockMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class ClockmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClockmateApplication.class, args);
	}

}
