package com.ibm.anz.wmla.sdk.ibmwmla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IbmWmlaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbmWmlaApplication.class, args);
	}

}
