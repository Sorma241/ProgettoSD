package com.Bank;


import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;




public class Database {

	
	
	private Connection connect() {

		Connection conn = null;

		String url = "jdbc:sqlite:BankDB.db";

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
		
		Account resuls = new Account(rs.getString("name"), rs.getString("surname"), rs.getString("accountId"), rs.getDouble("balance"));
		
		endConnection(conn);
		return resuls;
	}
	
	public List<Transaction> returnTransferAccount(String id) throws SQLException {
		String sql = "SELECT t.id_transfer, t.from_account, t.to_account, t.transfer_date, t.amount FROM Account AS a, Transfer AS t WHERE a.accountId = t.from_account AND accountId = '" + id 
				+ "' ORDER BY t.transfer_date";
		
		Connection conn = this.connect();
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		List<Transaction> allTransfer = new ArrayList<>();
		while(rs.next()) {
			
			Transaction trans = new Transaction(rs.getString("id_transfer"), rs.getString("from_account"), rs.getString("to_account"), 
					rs.getTimestamp("transfer_date", new GregorianCalendar()), rs.getDouble("amount"));
			allTransfer.add(trans);
			
		}
		
		endConnection(conn);
		return allTransfer;
	}
	
	public double changeBalance(String accountId, double amount) throws SQLException {
		
		String oldBalanceSQL = "SELECT balance FROM Account a WHERE a.accountId = '"+ accountId + "';";
	
		Connection conn = this.connect();
		Statement st = conn.createStatement();
		
		double oldBalance = st.executeQuery(oldBalanceSQL).getDouble("balance");
		double newBalance = amount + oldBalance;
		String sql = "UPDATE Account SET balance = '" + newBalance  + " 'WHERE accountId = '"+ accountId +"';";
		
		st.executeUpdate(sql);
		
		endConnection(conn);
		
		return newBalance;
	}
	
	public boolean checkBalance(String id, double amount) throws SQLException {
		String sql = "SELECT balance FROM Account a WHERE a.accountId = '"+ id + "';";
		
		Connection conn = this.connect();
		Statement st = conn.createStatement();
		double oldBalance = st.executeQuery(sql).getDouble("balance");
		double newBalance = oldBalance + amount;
		endConnection(conn);
		
		if(newBalance < 0) {
			return false;
		}else {
			return true;
		}	
	}
	
	public Transaction returnTransaction(String id) throws SQLException {
		String sql = "SELECT * FROM Transfer t WHERE id_transfer = '"+ id + "';";
		
		Connection conn = this.connect();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		Transaction resuls = new Transaction(rs.getString("id_transfer"), rs.getString("from_account"), rs.getString("to_account"), 
				rs.getTimestamp("transfer_date", new GregorianCalendar()), rs.getDouble("amount"));
		endConnection(conn);
		
		return resuls;
	}
	
	public String addTransaction (String id_from, String id_to, double amount) throws SQLException {
		String sql = "INSERT INTO Transfer(id_transfer, from_account, to_account, transfer_date, amount) VALUES(?,?,?, datetime('now','localtime'), ?)";
		
		Connection conn = this.connect();
		
		String id_transfer = Transaction.createUUID();
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id_transfer);
		pstmt.setString(2, id_from);
		pstmt.setString(3, id_to);
		pstmt.setDouble(4, amount);
		pstmt.executeUpdate();
		
		endConnection(conn);
		return id_transfer;
	}
	
	public void changeValue(String accountId, String value, String parameter) throws SQLException, NotFoundException {
		String sql = "UPDATE Account SET '" + parameter + "' = " + "'" + value + "' WHERE accountId = '"+ accountId + "'";
		
		Connection conn = this.connect();
		
		Statement st = conn.createStatement();
		int change = st.executeUpdate(sql);
		
		if(change == 0) {
			throw new NotFoundException();
		}
		
		endConnection(conn);
	}
	
	

}
