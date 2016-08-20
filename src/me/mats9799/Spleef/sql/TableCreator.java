package me.matsync.Spleef.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import me.matsync.Spleef.config.SettingsManager;

public class TableCreator {

	public TableCreator() {
		createTable();
	}
	
	private Connection openConnection() {
		Connection connection = null;
		SettingsManager sm = SettingsManager.getInstance();
		
		String ip = sm.getSQLYaml().getDatabaseIP();
		String database = sm.getSQLYaml().getDatabaseName();
		String username = sm.getSQLYaml().getUsername();
		String password = sm.getSQLYaml().getPassword();
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
		} catch (Exception e) { }
		
		return connection;
	}
	
	private void createTable() {
		Connection connection = openConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS spleef "
					+ "(uuid VARCHAR(36) NOT NULL PRIMARY KEY, "
					+ "name VARCHAR(16), "
					+ "games INT, "
					+ "wins INT, "
					+ "elo INT)");
			
			ps.execute();
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}