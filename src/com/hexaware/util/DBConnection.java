package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	static Connection con;
	
	public static Connection getConnection() {
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "s9403947192");
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		return con;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getConnection());

	}

}