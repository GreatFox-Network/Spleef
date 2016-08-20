package me.matsync.Spleef.config;

import java.util.ArrayList;
import java.util.List;

public class SettingsManager {

	private Cache cache;
	private Config config;
	private SQLConfig sql;
	private List<Yaml> yamls;
	
	private SettingsManager() { }
	
	private static SettingsManager instance;
	
	public static SettingsManager getInstance() {
		if (instance == null) {
			instance = new SettingsManager();
		}
		return instance;
	}
	
	public Cache getCacheYaml() {
		return cache;
	}
	
	public Config getConfigYaml() {
		return config;
	}
	
	public SQLConfig getSQLYaml() {
		return sql;
	}
	
	public void addYaml(Yaml yaml) {
		if (yamls.contains(yaml)) return;
		
		yamls.add(yaml);
	}
	
	public Yaml getYaml(String resource) {
		for (Yaml yaml : yamls) {
			if (yaml.getResource().toString().equalsIgnoreCase(resource)) {
				return yaml;
			}
		}
		return null;
	}
	
	public void loadConfigs() {
		for (Yaml yaml : yamls) {
			yaml.reloadConfig();
		}
	}
	
	public void saveConfigs() {
		for (Yaml yaml : yamls) {
			if (yaml.isSaveable()) {
				yaml.save();
			}
		}
	}
	
	public void setup() {
		this.yamls = new ArrayList<Yaml>();
		
		this.cache = new Cache();
		this.config = new Config();
		this.sql = new SQLConfig();
	}
}