package com.example.amelicanflight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AmelicanFlightApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmelicanFlightApplication.class, args);
	}

}
