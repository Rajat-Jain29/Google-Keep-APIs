package com.example.API.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class connect {
	Connection connection;
	Statement statement;
	String query,Connection;
	ResultSet resultSet;
	void conection() throws SQLException {
		connection = DriverManager.getConnection(Connection, "root", "");
		statement=connection.createStatement();
	}
}
