// Example: If ReactAppController is in com.example.laundrocheck.controller
package com.example.laundrocheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.laundrocheck.controller", "com.example.laundrocheck"})
public class LaundrocheckApplication {
	public static void main(String[] args) {
		SpringApplication.run(LaundrocheckApplication.class, args);
	}
}
