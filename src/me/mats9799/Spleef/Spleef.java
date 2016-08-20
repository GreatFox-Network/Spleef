package me.matsync.Spleef;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.leaderboard.Leaderboard;
import me.matsync.Spleef.listener.ArenaEvent;
import me.matsync.Spleef.listener.BlockEvent;
import me.matsync.Spleef.listener.ChatEvent;
import me.matsync.Spleef.listener.PlayerEvent;
import me.matsync.Spleef.listener.SignEvent;
import me.matsync.Spleef.sql.SQLManager;

public class Spleef extends JavaPlugin {

	private static Plugin plugin;
	private static PluginLogger logger;
	private static ScoreboardManager sb;
	
	public void onEnable() {
		plugin = this;
		logger = new PluginLogger(getLogger(), getName());
		sb = new ScoreboardManager();
		
		registerCommands();
		registerListeners();
		
		SettingsManager.getInstance().setup();
		
		ArenaManager.getInstance().loadArenas();
		
		updateLeaderboards();
	}
	
	public void onDisable() {
		SettingsManager.getInstance().saveConfigs();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer("Spleef is restarting!");
		}
	}
	
	private void updateLeaderboards() {
		new BukkitRunnable() {
			
			public void run() {
				Leaderboard lb = new Leaderboard(SQLManager.getInstance().getLeaderboardScores());
				
				lb.updateLeaderboards();
			}
			
		}.runTaskTimer(this, 0, 6000);
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static PluginLogger getPluginLogger() {
		return logger;
	}
	
	public static ScoreboardManager getScoreboardManager() {
		return sb;
	}
	
	public static WorldEditPlugin getWorldEditPlugin() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		
		if (plugin instanceof WorldEditPlugin) return (WorldEditPlugin) plugin;
		
		return null;
	}
	
	private void registerCommands() {
		CommandProcessor cp = CommandProcessor.getInstance();
		
		getCommand("leave").setExecutor(cp);
		getCommand("spleef").setExecutor(cp);
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new ArenaEvent(), this);
		pm.registerEvents(new BlockEvent(), this);
		pm.registerEvents(new ChatEvent(), this);
		pm.registerEvents(new PlayerEvent(), this);
		pm.registerEvents(new SignEvent(), this);
	}
}