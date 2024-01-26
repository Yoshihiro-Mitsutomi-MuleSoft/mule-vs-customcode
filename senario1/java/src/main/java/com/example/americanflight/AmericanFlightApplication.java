package com.example.americanflight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AmericanFlightApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmericanFlightApplication.class, args);
	}

}
