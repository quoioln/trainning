package com.quoioln.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.quoioln.model.Account;
import com.quoioln.util.DBConnection;

public class AccountDAO {
	Connection connection = null;
	private String tableName = "Account";
	
	private String accountIdCol = "AccountId";
	private String userNameCol = "Username";
	private String passwordCol = "password";
	private String fullNameCol = "Fullname";
	private String phoneCol = "Phone";
	private String emailCol = "Email";
			
	public Account getAccount(int accountId) {
		Account account = null;
		try {
			connection =  DBConnection.getConnection();
			String sql = "select " + accountIdCol + ", " + userNameCol + ", " 
						+ passwordCol + ", " + fullNameCol + ", " + emailCol 
						+ ", " + phoneCol + " from " + tableName  
						+ " where " + accountIdCol + "= ?";
			PreparedStatement preStm = connection.prepareStatement(sql);
			preStm.setInt(1, accountId);
			
			ResultSet rs =  preStm.executeQuery();
			if(rs.next()) {
				account = new Account();
				account.setAccountId(rs.getInt(accountIdCol));
				account.setUserName(rs.getString(userNameCol));
				account.setPassword(rs.getString(passwordCol));
				account.setFullName(rs.getString(fullNameCol));
				account.setEmail(rs.getString(emailCol));
				account.setPhone(rs.getString(phoneCol));
				
			}
			return account;
		} catch (SQLException e) {
			e.printStackTrace();
			return account;
		}
	}
	public Account getAccountByUserName(String userName) {
		Account account = null;
		try {
			connection =  DBConnection.getConnection();
			String sql = "select " + accountIdCol + ", " + userNameCol + ", " 
						+ passwordCol + ", " + fullNameCol + ", " + emailCol 
						+ ", " + phoneCol + " from " + tableName  
						+ " where " + userNameCol + "= ?";
			PreparedStatement preStm = connection.prepareStatement(sql);
			preStm.setString(1, userName);
			
			ResultSet rs =  preStm.executeQuery();
			if(rs.next()) {
				account = new Account();
				account.setAccountId(rs.getInt(accountIdCol));
				account.setUserName(rs.getString(userNameCol));
				account.setPassword(rs.getString(passwordCol));
				account.setFullName(rs.getString(fullNameCol));
				account.setEmail(rs.getString(emailCol));
				account.setPhone(rs.getString(phoneCol));
				
			}
			return account;
		} catch (SQLException e) {
			e.printStackTrace();
			return account;
		}
	}
	List<Account> getAllAccount() {
		List<Account> accountList = new ArrayList<Account>();
		try {
			connection =  DBConnection.getConnection();
			String sql = "select " + accountIdCol + ", " + userNameCol + ", " 
						+ passwordCol + ", " + fullNameCol + ", " + emailCol 
						+ ", " + phoneCol + " from " + tableName;
			Statement stm = connection.createStatement();
			
			ResultSet rs = stm.executeQuery(sql);
			Account account = new Account();
			if(rs.next()) {
				account.setAccountId(rs.getInt(accountIdCol));
				account.setUserName(rs.getString(userNameCol));
				account.setPassword(rs.getString(passwordCol));
				account.setFullName(rs.getString(fullNameCol));
				account.setEmail(rs.getString(emailCol));
				account.setPhone(rs.getString(phoneCol));
				accountList.add(account);
				
			}
			return accountList;
		} catch (SQLException e) {
			e.printStackTrace();
			return accountList;
		}
	}
	
	public boolean addAccount(Account account) {
		try {
			connection =  DBConnection.getConnection();
			if (connection != null) {
				String sql = "insert into " + tableName + "(" + userNameCol
							+ ", " + passwordCol + ", " + fullNameCol
							+ ", " + emailCol + ", " + phoneCol + ") "
							+ " values(?, ?, ?, ?, ?)";
				PreparedStatement preStm = connection.prepareStatement(sql)			;
				preStm.setString(1, account.getUserName());
				preStm.setString(2, account.getPassword());
				preStm.setString(3, account.getFullName());
				preStm.setString(4, account.getEmail());
				preStm.setString(5, account.getPhone());
				preStm.executeUpdate();
				return true;
			} else {
				System.out.println("Can not add account");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Can not add account");
			return false;
		}
	}
	
	public boolean updateAccount(Account account) {
		try {
			connection =  DBConnection.getConnection();
			String sql = "update " + tableName + " set " 
						+ passwordCol + " = ?, " 
						+ fullNameCol + " = ?, "
						+ emailCol + " =  ?, " 
						+ phoneCol + " =  ? "
						+ " where " + accountIdCol + " = ?";;
			PreparedStatement preStm = connection.prepareStatement(sql)			;
			preStm.setString(1, account.getPassword());
			preStm.setString(2, account.getFullName());
			preStm.setString(3, account.getEmail());
			preStm.setString(4, account.getPhone());
			preStm.setInt(5, account.getAccountId());
			int row = preStm.executeUpdate();
			if (row > 0) 
				return true;
			else {
				System.out.println("Can not update account");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Can not update account");
			return false;
		}
	}
	
	public boolean deleteAccount(int accountId) {
		try {
			connection =  DBConnection.getConnection();
			String sql = "delete from " + tableName + " where " + accountIdCol + " = ?" ;
			PreparedStatement preStm = connection.prepareStatement(sql)			;
			preStm.setInt(1, accountId);
			int row = preStm.executeUpdate();
			if (row > 0) 
				return true;
			else {
				System.out.println("Can not delete account");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Can not delete account");
			return false;
		}
	}
	
}
