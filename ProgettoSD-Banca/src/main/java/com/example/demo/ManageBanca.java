package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag;

@RestController
public class ManageBanca {
	
	private Database db = new Database();
	
	public Map<String, String> parseBody(String str) {
		Map<String, String> body = new HashMap<>();
		String[] values = str.split("&");
		for (int i = 0; i < values.length; ++i) {
			String[] coppia = values[i].split("=");
			if (coppia.length != 2) {
				continue;
			} else {
				body.put(coppia[0], coppia[1]);
			}
		}
		return body;
	}

	@RequestMapping("/api/account")
	public List<Account> returnAllAccount() {
		
		return null;
	}
	
	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public String creaAccount(@RequestBody String account) {
		Map <String, String> body = parseBody(account);
		
		Account ac = new Account(body.get("name"), body.get("surname"));
		
		db.addAccount(ac.getName(), ac.getSurname(), ac.getAccountId());
		
		return ac.getAccountId();
	}
	
	@RequestMapping(value = "/api/account", method = RequestMethod.DELETE)
	public void delateAccount(@PathParam(value = "ID") String IDaccount) {
		
	}
}


