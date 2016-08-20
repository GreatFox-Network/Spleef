package me.matsync.Spleef.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.CommandProcessor;

public class Help implements SubCommand {

	public void execute(Player player, String[] args) {
		CommandProcessor cp = CommandProcessor.getInstance();
		
		player.sendMessage(ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH.toString() + "----------------------" + ChatColor.RESET
				+ ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + " Spleef " + ChatColor.RESET
				+ ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH.toString() + "-----------------------");
		
		player.sendMessage(ChatColor.AQUA + " /spleef help " + ChatColor.GRAY + cp.getCommand("help").description());
		player.sendMessage(ChatColor.AQUA + " /spleef join <arena> " + ChatColor.GRAY + cp.getCommand("join").description());
		player.sendMessage(ChatColor.AQUA + " /spleef leave " + ChatColor.GRAY + cp.getCommand("leave").description());
		player.sendMessage(ChatColor.AQUA + " /spleef version " + ChatColor.GRAY + cp.getCommand("version").description());
		
		if (player.hasPermission("spleef.admin")) {
			player.sendMessage(ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH.toString() + "-----------------------------------------------------");
			
			player.sendMessage(ChatColor.AQUA + " /spleef createarena <name> " + ChatColor.GRAY + cp.getCommand("createarena").description());
			player.sendMessage(ChatColor.AQUA + " /spleef reload " + ChatColor.GRAY + cp.getCommand("reload").description());
			player.sendMessage(ChatColor.AQUA + " /spleef setfloor <arena> " + ChatColor.GRAY + cp.getCommand("setfloor").description());
			player.sendMessage(ChatColor.AQUA + " /spleef setmainlobby " + ChatColor.GRAY + cp.getCommand("setfloor").description());
			player.sendMessage(ChatColor.AQUA + " /spleef setspawn <arena> <#> " + ChatColor.GRAY + cp.getCommand("setspawn").description());
		}
		
		player.sendMessage(ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH.toString() + "-----------------------------------------------------");
	}

	public String name() {
		return "help";
	}

	public String description() {
		return "displays the help menu";
	}

	public String[] aliases() {
		return new String[] { "?" };
	}

	public boolean admin() {
		return false;
	}
}