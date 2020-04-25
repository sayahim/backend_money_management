package com.himorfosis.moneymanagement;

import com.himorfosis.moneymanagement.service.ImageStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication()
public class MoneymanagementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MoneymanagementApplication.class, args);
	}

}
