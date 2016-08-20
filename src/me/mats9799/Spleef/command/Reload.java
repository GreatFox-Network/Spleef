package me.matsync.Spleef.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.config.SettingsManager;

public class Reload implements SubCommand {

	public void execute(Player player, String[] args) {
		SettingsManager.getInstance().loadConfigs();
		player.sendMessage(ChatColor.AQUA + "All configurations have been refreshed.");
	}

	public String name() {
		return "reload";
	}

	public String description() {
		return "refreshes data from the configuration";
	}

	public String[] aliases() {
		return new String[] { "rel", "load" };
	}

	public boolean admin() {
		return true;
	}
}