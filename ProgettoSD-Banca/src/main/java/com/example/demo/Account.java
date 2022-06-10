package com.example.demo;

import java.util.Random;

public class Account {

	private String name;
	private String surname;
	private String accountId;
	private double saldo;
	

	
	
	public Account(String name, String surname) {
		this.name = name;
		this.surname = surname;
		this.saldo = 0;
		createId();
	}
	
	public Account(String name, String surname, String id, double saldo) {
		this.name = name;
		this.surname = surname;
		this.accountId = id;
		this.saldo = saldo;
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
	
	public String getAccountId(){
		return accountId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	
}
