package com.example.demo;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class Database {

	
	
	private Connection connect() {

		Connection conn = null;

		String url = "jdbc:sqlite:prova.db";

		try {
			
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(url);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return conn;

		
	}
	
	private void endConnection(Connection c) {
		
		try {
			if(!c.isClosed()) {
				
				c.close();
			}
		} catch (SQLException e) {
			
			
			e.printStackTrace();
		}
		
	}

	public void addAccount(String name, String surname, String accountId) throws SQLException {

		String sql = "INSERT INTO Account(accountId, name, surname, balance) VALUES(?,?,?,0)";

		Connection conn = this.connect();
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, accountId);
		pstmt.setString(2, name);
		pstmt.setString(3, surname);
		pstmt.executeUpdate();
		
		endConnection(conn);
	}
	
	public List<Account> returnAllAccounts() throws SQLException {

		String sql = "SELECT * FROM Account";

		Connection conn = this.connect();

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		
		List<Account> accountList = new ArrayList<>();
		while(rs.next()) {
			
			accountList.add(new Account(rs.getString("name"), rs.getString("surname"), rs.getString("accountId"), rs.getDouble("balance")));
			
		}
		
		endConnection(conn);
		return accountList;
		
		
	}
	
	public void deleteAccount(String id) throws SQLException {
		
		String sql = "DELETE FROM Account WHERE accountId = '" + id + "'";
		
		Connection conn = this.connect();
		
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
		
		endConnection(conn);
		
	}
	
	public Account returnAccount(String id) throws SQLException {
		String sql = "SELECT * FROM Account WHERE accountId = '" + id + "'";
		
		Connection conn = this.connect();
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		Account resuls = new Account(rs.getString("name"), rs.getString("surname"), "", rs.getDouble("balance"));
		
		endConnection(conn);
		return resuls;
	}
	
	public List<Transaction> returnTransferAccount(String id) throws SQLException {
		String sql = "SELECT t.id_transfer, t.from_account, t.to_account, t.transfer_date, t.amount FROM Account AS a, Transfer AS t WHERE a.accountId = t.from_account AND accountId = '" + id + "'";
		
		Connection conn = this.connect();
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		List<Transaction> allTransfer = new ArrayList<>();
		while(rs.next()) {
			
			Transaction trans = new Transaction(rs.getString("from_account"), rs.getString("to_account"), rs.getString("id_transfer"), rs.getDate("transfer_date"), rs.getDouble("amount"));
			
			allTransfer.add(trans);
			
		}
		
		endConnection(conn);
		return allTransfer;
	}
	
	public void changeBalance(String accountId, double amount) throws SQLException {
		
		String sql = "UPDATE Account SET balance = ((SELECT balance FROM Account AS a WHERE a.accountId = '"+ accountId + "') + "+ amount +")"
				+ " WHERE accountId = '"+ accountId +"';";
		
		Connection conn = this.connect();
		
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
		
		endConnection(conn);
	}
	
	public void changeValue(String accountId, String name, String parameter) throws SQLException {
		String sql = "UPDATE Account SET '" + parameter + "' = " + "'" + name + "' WHERE accountId = '"+ accountId + "'";
		
		Connection conn = this.connect();
		
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
		
		endConnection(conn);
	}
	
	

}
