package me.matsync.Spleef.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.Spleef;

public class Version implements SubCommand {

	public void execute(Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_AQUA + "This server runs " + ChatColor.AQUA + "Spleef v"
			+ Spleef.getPlugin().getDescription().getVersion() + ChatColor.DARK_AQUA + " created by " + ChatColor.AQUA + "GreatFox");
		
		player.setScoreboard(Spleef.getScoreboardManager().getScoreboard(player));
	}

	public String name() {
		return "version";
	}

	public String description() {
		return "gets the current version of the plugin";
	}

	public String[] aliases() {
		return new String[] { "ver" };
	}

	public boolean admin() {
		return false;
	}
}