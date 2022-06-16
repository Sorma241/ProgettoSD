package com.example.demo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Account {

	private String name;
	private String surname;
	private String accountId;
	private double balance;
	private List<Transaction> transactions;
	

	
	
	public Account(String name, String surname) {
		this.name = name;
		this.surname = surname;
		this.balance = 0;
		this.transactions = new ArrayList<>();
		getAccountTransaction();
		createId();
	}
	
	public Account(String name, String surname, String id, double saldo) {
		this.name = name;
		this.surname = surname;
		this.accountId = id;
		this.balance = saldo;
		getAccountTransaction();
	}
	
	private void createId() {
		
		int maxLength = 20;
		
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		
		while(sb.length() < maxLength) {
			sb.append(Integer.toHexString(r.nextInt()));
		}
		
		accountId = sb.toString().substring(0, maxLength);
		
	}
	
	private void getAccountTransaction() {
		
		try {
			transactions = new Database().returnTransferAccount(accountId);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	//returnId
	public String getAccountID(){
		return accountId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transaction> t) {
		this.transactions = t;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
	
}
