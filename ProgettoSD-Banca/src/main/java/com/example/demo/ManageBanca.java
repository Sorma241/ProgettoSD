package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManageBanca {

	@RequestMapping("/api/account")
	public List<Account> returnAllAccount() {
		
		return DemoApplication.accountList;
	}
	
	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public void CreaAccount(@RequestBody String account) {
		
		Map<String, String> body = new HashMap<>();
		
	}
}


