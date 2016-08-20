package me.matsync.Spleef;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLogger {

	private final Logger logger;
	private final String name;
	
	public PluginLogger(Logger logger, String name) {
		this.logger = logger;
		this.name = name;
	}
	
	public void error(String arg0) {
		logger.severe("[" + name + "] " + arg0);
	}
	
	public void info(String arg0) {
		logger.info("[" + name + "] " + arg0);
	}
	
	public void log(Level level, String arg0) {
		logger.log(level, "[" + name + "] " + arg0);
	}
	
	public void warn(String arg0) {
		logger.warning("[" + name + "] " + arg0);
	}
}