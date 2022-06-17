package com.example.demo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

		List<Account> risultato = null;

		try {

			risultato = db.returnAllAccounts();

		} catch (SQLException e) {

			System.out.println("Errore sistema: " + e.getMessage());
		}

		return risultato;
	}

	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public String creaAccount(@RequestBody String account) {

		Map<String, String> body = parseBody(account);
		Account ac = new Account(body.get("name"), body.get("surname"));

		try {

			db.addAccount(ac.getName(), ac.getSurname(), ac.getAccountID());

		} catch (SQLException e) {

			if (e.getMessage().indexOf("SQLITE_CONSTRAINT_PRIMARYKEY") >= 0) {

				return new ResponseEntity<String>("Constraint key", HttpStatus.CONFLICT).toString();
			} else {

				return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR).toString();
			}

		}

		return ac.getAccountID();
	}

	@RequestMapping(value = "/api/account", method = RequestMethod.DELETE)
	public String delateAccount(@RequestParam(value = "ID") String IDaccount) {

		try {
			db.deleteAccount(IDaccount);

		} catch (SQLException e) {

			return "Errore: " + e.getMessage();
		}

		return "Successo";
	}

	@RequestMapping("/api/account/{accountId}")
	public Account returnAccount(@PathVariable String accountId) {
		
		Account ac = null;

		try {

			ac = db.returnAccount(accountId);
			ac.setTransactions(db.returnTransferAccount(accountId));
		} catch (SQLException e) {

			System.out.println("Errore sistema: " + e.getCause());
		}

		return ac;
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.POST)
	public String deposit(@RequestBody String amount, @PathVariable String accountId) {

		Map<String, String> body = parseBody(amount);
		double operationAmount = Double.parseDouble(body.get("amount"));

		try {
			return db.changeBalance(accountId, operationAmount);

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return "Errore";
		}
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.PUT)
	public String changeNameSurname(@RequestBody String valueBody, @PathVariable String accountId) {	
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> body = null;
		
		try {
			
			body = mapper.readValue(valueBody, Map.class);
			
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
		String newName = body.get("name");
		String newSurname = body.get("surname");

		try {
			
			db.changeValue(accountId, newName, "name");
			db.changeValue(accountId, newSurname, "surname");

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return "Errore";
		}

		return "Successo";
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.PATCH)
	public String changeValue(@RequestBody String valueBody, @PathVariable String accountId) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> body = null;
		
		try {
			
			body = mapper.readValue(valueBody, Map.class);
			
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		String value = "";

		try {
			if (body.containsKey("name")) {
				value = body.get("name");
				db.changeValue(accountId, value, "name");
			} else {
				value = body.get("surname");
				db.changeValue(accountId, value, "surname");
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return "Errore";
		}

		return "Successo";
	}
}
