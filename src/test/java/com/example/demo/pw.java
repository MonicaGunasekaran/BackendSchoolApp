package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class pw {
	@Autowired
	PasswordEncoder passwordEncoder;

	public void generate() {
	    System.out.println(passwordEncoder.encode("admin123"));
	}
}
