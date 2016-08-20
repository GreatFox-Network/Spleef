package me.matsync.Spleef.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.util.Messages;

public class SetMainLobby implements SubCommand {

	public void execute(Player player, String[] args) {
		SettingsManager.getInstance().getCacheYaml().setLocation("main.lobby", player.getLocation());
		Messages.sendPlayerMessage(player, ChatColor.AQUA + "You have set the main lobby for Spleef.");
	}

	public String name() {
		return "setmainlobby";
	}

	public String description() {
		return "sets the main lobby";
	}

	public String[] aliases() {
		return new String[] { "sml" };
	}

	public boolean admin() {
		return true;
	}
}