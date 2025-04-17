package com.PoseidonCapitalSolutions.TradingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class TradingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingAppApplication.class, args);
	}

}
