package com.bridgelabz.fundoonotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FundooAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooAppApplication.class, args);
	}

}
