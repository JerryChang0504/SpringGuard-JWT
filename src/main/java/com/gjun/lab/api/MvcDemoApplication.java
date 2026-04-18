package com.gjun.lab.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gjun.lab")
public class MvcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcDemoApplication.class, args);
	}
}
