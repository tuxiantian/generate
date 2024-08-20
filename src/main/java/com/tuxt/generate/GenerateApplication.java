package com.tuxt.generate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class GenerateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenerateApplication.class, args);
	}

}
