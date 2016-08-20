package me.matsync.Spleef.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.event.PlayerLeaveArenaEvent;

public class Leave implements SubCommand {

	ArenaManager am = ArenaManager.getInstance();
	
	public void execute(Player player, String[] args) {
		if (!am.isPlaying(player)) {
			player.sendMessage(ChatColor.RED + "You are not playing.");
			return;
		}
		
		Bukkit.getPluginManager().callEvent(new PlayerLeaveArenaEvent(player, am.getArena(player)));
	}

	public String name() {
		return "leave";
	}

	public String description() {
		return "makes you leave your current arena";
	}

	public String[] aliases() {
		return new String[] { "l" };
	}

	public boolean admin() {
		return false;
	}
}