package me.matsync.Spleef.config;

import me.matsync.Spleef.Spleef;

public class SQLConfig extends Yaml {

	public SQLConfig() {
		super(Spleef.getPlugin().getDataFolder().getPath(), "sql.yml", false);
	}

	public String getDatabaseIP() {
    	return getString("sql.ip");
    }
    
    public String getDatabaseName() {
    	return getString("sql.database");
    }
    
    public String getPassword() {
    	return getString("sql.password");
    }
    
    public String getUsername() {
    	return getString("sql.username");
    }
}