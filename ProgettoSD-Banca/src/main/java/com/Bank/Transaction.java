package com.Bank;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Transaction {
	
	private String id_transfer;
	private String from;
	private String to;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/YYYY hh:mm:ss", timezone="UTC")
	private Date date;
	private double amount;
	
	public Transaction(String id, String from, String to, Date date, double amount) {
		this.id_transfer = id;
		this.from = from;
		this.to = to;
		
		this.date = date;
		this.amount = amount;
	}
	
	public Transaction(String from, String to, Date date, double amount) {
		this.from = from;
		this.to = to;
		this.date = date;
		this.amount = amount;
		this.id_transfer = createUUID();
	}
	
	public static String createUUID() {
		UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        return uuidAsString;
	}

	public String getId_transfer() {
		return id_transfer;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
}
