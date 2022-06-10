package com.example.demo;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private Connection connect() {

		Connection c = null;

		String url = "jdbc:sqlite:prova.db";

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

	public void addAccount(String name, String surname, String accountId) throws SQLException {

		String sql = "INSERT INTO Account(accountId, name, surname, saldo) VALUES(?,?,?,0)";

		Connection conn = this.connect();

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, accountId);
		pstmt.setString(2, name);
		pstmt.setString(3, surname);
		pstmt.executeUpdate();
	}
	
	public List<String> returnAllAccounts() throws SQLException {

		String sql = "SELECT * FROM Account";

		Connection conn = this.connect();

		Statement pstmt = conn.createStatement();
		ResultSet rs = pstmt.executeQuery(sql);
		
		String a;
		List<String> acc = new ArrayList<String>();
		while(rs.next()) {
			a = rs.getString("name") + ":" + rs.getString("surname") + ":" + rs.getString("accountId");
			
			System.out.println(a);
			acc.add(a);
		}
		
		return acc;
	}

}
