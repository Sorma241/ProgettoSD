package com.example.demo;

import java.util.List;
import java.util.Random;

public class Account {

	private String name;
	private String surname;
	private String accountId;
	private double balance;
	//private List<String> transizioni;
	

	
	
	public Account(String name, String surname) {
		this.name = name;
		this.surname = surname;
		this.balance = 0;
		createId();
	}
	
	public Account(String name, String surname, String id, double saldo) {
		this.name = name;
		this.surname = surname;
		this.accountId = id;
		this.balance = saldo;
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
	
	//public void setTransfert() {
	
	
}
