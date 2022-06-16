package com.example.demo;

import java.sql.SQLException;
import java.util.Date;

public class Transaction {
	
	private String id_transfer;
	private Account from;
	private Account to;
	private Date date;
	private double amount;
	
	public Transaction(String from, String to, String id, Date date, double amount) {
		this.id_transfer = id;
		
		try {
			this.from = new Database().returnAccount(from);
			this.to = new Database().returnAccount(to);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		this.date = date;
		this.amount = amount;
		
	}
	
}
