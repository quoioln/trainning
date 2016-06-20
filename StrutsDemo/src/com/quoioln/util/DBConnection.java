package com.quoioln.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author vpquoi
 *
 */
public class DBConnection {
	private static Connection connection = null;
	public static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://192.168.17.41:5432/TestUserDB","postgres", "postgres");
			System.out.println("Connect success database");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			System.out.println("Cann't get connection");
		} 
		return connection;
	}
}
