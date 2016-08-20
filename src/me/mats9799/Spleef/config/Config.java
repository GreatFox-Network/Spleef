package me.matsync.Spleef.config;

import java.util.List;

import me.matsync.Spleef.Spleef;

public class Config extends Yaml {

	public Config() {
		super(Spleef.getPlugin().getDataFolder().getPath(), "config.yml", false);
	}
	
	public List<String> getAllowedCommands() {
		return getStringList("allowed-commands");
	}
}