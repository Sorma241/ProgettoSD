package com.example.demo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

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
	public MappingJacksonValue returnAllAccount() {

		List<Account> risultato = null;
	    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("accountID", "name", "surname", "balance", "transactions");
	    FilterProvider filters = new SimpleFilterProvider().addFilter("AccountFilter", filter);

		try {

			risultato = db.returnAllAccounts();

		} catch (SQLException e) {

			System.out.println("Errore sistema: " + e.getMessage());
		}
		
		 MappingJacksonValue mapping = new MappingJacksonValue(risultato);
         mapping.setFilters(filters);

		return mapping;
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
	public HttpEntity<MappingJacksonValue> returnAccount(@PathVariable String accountId) {
		
	    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "surname", "balance", "transactions");
	    FilterProvider filters = new SimpleFilterProvider().addFilter("AccountFilter", filter);
	    
		HttpHeaders header = new HttpHeaders();
		Account ac = null;

		try {

			ac = db.returnAccount(accountId);
			ac.setTransactions(db.returnTransferAccount(accountId));
		} catch (SQLException e) {

			System.out.println("Errore sistema: " + e.getCause());
		}
		
		 MappingJacksonValue mapping = new MappingJacksonValue(ac);
         mapping.setFilters(filters);

		header.add("X-Sistema-Bancario", ac.getName() + ";" + ac.getSurname());
		
		return new HttpEntity<MappingJacksonValue>(mapping, header);
	}
	
	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.HEAD)
	public HttpEntity<Account> returnAccountHead(@PathVariable String accountId) {
		
		HttpHeaders header = new HttpHeaders();
		Account ac = null;

		try {

			ac = db.returnAccount(accountId);
		} catch (SQLException e) {
			System.out.println("Errore sistema: " + e.getCause());
		}

		header.add("X-Sistema-Bancario", ac.getName() + ";" + ac.getSurname());
		return new HttpEntity<Account>(header);
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.POST)
	public String deposit(@RequestBody String amount, @PathVariable String accountId) {

		Map<String, String> body = parseBody(amount);
		double operationAmount = Double.parseDouble(body.get("amount"));

		try {
			 
			if(db.checkBalance(accountId, operationAmount)) {
				
				if(db.changeBalance(accountId, operationAmount)) {
					
					if(db.addTransaction(accountId, accountId, operationAmount)) {
						return "Success";
					}else {
						return "Error";
					}
					
				 }else {
					 return "Error";
				 }
			}else {
				return "Error balance";
			}
			 
			 

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
	
	
	@RequestMapping(value = "/api/transfer", method = RequestMethod.POST)
	public String makeTransfer(@RequestBody String transBody) {
		
		Map<String, String> body = parseBody(transBody);
		boolean fromBalanceUpd = false, toBalanceUpd = false,isTransactionAdded = false;
		
		String from = body.get("from");
		String to = body.get("to");
		double amount = Double.parseDouble(body.get("amount"));
		
		try {
			
			if (db.checkBalance(from, -amount)) {
				
				fromBalanceUpd = db.changeBalance(from, -amount);
				System.out.println(fromBalanceUpd);
				toBalanceUpd = db.changeBalance(to, amount);
				System.out.println(toBalanceUpd);
				
				isTransactionAdded = db.addTransaction(from, to, amount);
				System.out.println(isTransactionAdded);
				
				if(isTransactionAdded) {
					return "Success";
				}else {
					return "Error";
				}
			}else {
				return "Error Balance";
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return "Error";
				
		}
	}
	
	@RequestMapping(value = "/api/divert", method = RequestMethod.POST)
	public String makeDivert(@RequestBody String transBody) {
		
		Map<String, String> body = parseBody(transBody);
		boolean fromBalanceUpd = false, toBalanceUpd = false,isTransactionAdded = false;
		
		try {
			Transaction trans = db.returnTransaction(body.get("transfertID"));
			if (db.checkBalance(trans.getTo(), -trans.getAmount())) {
				
				fromBalanceUpd = db.changeBalance(trans.getTo(), -trans.getAmount());
				toBalanceUpd = db.changeBalance(trans.getFrom(), trans.getAmount());
				
				isTransactionAdded = db.addTransaction(trans.getTo(), trans.getFrom(), trans.getAmount());
				
				if(isTransactionAdded) {
					return "Success";
				}else {
					return "Error";
				}
			}else {
				return "Error Balance";
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return "Error";
				
		}
	}
	
}
