package com.noodle.noodle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class NoodleApplication {

	public static void main(String[] args) {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		System.out.println(bc.encode("12345"));
		SpringApplication.run(NoodleApplication.class, args);
	}

}
