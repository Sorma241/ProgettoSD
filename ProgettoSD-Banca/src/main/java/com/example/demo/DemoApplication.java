package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	static List<Account> accountList = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
		Account a = new Account("provsa", "prova");
		accountList.add(a);
		
		Database d = new Database();
		d.addAccount("prova", "prova", "sdkj3j323k423");
		
	}

}
