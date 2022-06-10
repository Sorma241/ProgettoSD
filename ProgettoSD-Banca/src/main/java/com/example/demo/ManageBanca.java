package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.apache.catalina.webresources.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public List<String> returnAllAccount() {
		
		try {
			return db.returnAllAccounts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public String creaAccount(@RequestBody String account) {
		Map <String, String> body = parseBody(account);
		
		Account ac = new Account(body.get("name"), body.get("surname"));
		
		try {
			db.addAccount(ac.getName(), ac.getSurname(), ac.getAccountId());
		}catch (SQLException e) {
			if(e.getMessage().indexOf("SQLITE_CONSTRAINT_PRIMARYKEY") >= 0) {
				return new ResponseEntity<String>("Constraint key", HttpStatus.CONFLICT).toString();
			}else {
				return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR).toString();
			}
		}
		
		
		return ac.getAccountId();
	}
	
	@RequestMapping(value = "/api/account", method = RequestMethod.DELETE)
	public void delateAccount(@PathParam(value = "ID") String IDaccount) {
		System.out.println(IDaccount);
	}
}


