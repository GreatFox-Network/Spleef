package me.matsync.Spleef.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.util.Messages;

public class SetSpawn implements SubCommand {

	public void execute(Player player, String[] args) {
		ArenaManager am = ArenaManager.getInstance();
		SettingsManager sm = SettingsManager.getInstance();
		
		if (args.length <= 1) {
			player.sendMessage(ChatColor.RED + "Define an arena name.");
			return;
		}
		
		if (!am.arenaExists(args[1])) {
			player.sendMessage(ChatColor.RED + "An arena by that name doesn't exist.");
			return;
		}
		
		if (args.length <= 2) {
			player.sendMessage(ChatColor.RED + "Define the spawn number.");
			return;
		}
		
		int id;
		
		try {
			id = Integer.parseInt(args[2]);
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "That's not a number.");
			return;
		}
		
		if (id >= 3) {
			player.sendMessage(ChatColor.RED + "You can only set spawn 1 and 2.");
			return;
		}
		
		Arena arena = am.getArena(args[1]);
		
		arena.setSpawn(id, player.getLocation());
		
		sm.getCacheYaml().setLocation("arena." + arena.getName() + ".spawn." + id, player.getLocation());
		
		Messages.sendPlayerMessage(player, ChatColor.AQUA + "You have set spawn " + id + " for arena " + arena.getName() + ".");
	}

	public String name() {
		return "setspawn";
	}

	public String description() {
		return "sets a player spawn for an arena";
	}

	public String[] aliases() {
		return new String[] { "ss" };
	}

	public boolean admin() {
		return true;
	}
}