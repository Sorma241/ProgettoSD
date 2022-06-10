package com.example.demo;

import java.io.File;
import java.sql.*;


public class Database {
	
	
	
	private Connection connect() {
		
		Connection c = null;
		
		File dbFile = new File(".");
		String url="jdbc:sqlite:prova.db";
		
		System.out.println(url);
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(url);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
		System.out.println("Connection successfully");
        return c;
	}
	
	public void addAccount(String name, String surname, String accountId) {
		
		String sql = "INSERT INTO Account(accountId, name, surname, saldo) VALUES(?,?,?,0)";  
		
		Connection conn = this.connect();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountId);
			pstmt.setString(2, name);
			pstmt.setString(3, surname);
			pstmt.executeUpdate(); 
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}  
		
		
		
	}
	
	
	
	
	
}
