package me.matsync.Spleef.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.arena.ArenaState;
import me.matsync.Spleef.event.PlayerJoinArenaEvent;

public class Join implements SubCommand {

	ArenaManager am = ArenaManager.getInstance();
	
	public void execute(Player player, String[] args) {
		if (am.isPlaying(player)) {
			player.sendMessage(ChatColor.RED + "You are already playing.");
			return;
		}
		
		if (args.length <= 1) {
			player.sendMessage(ChatColor.RED + "Define the arena name.");
			return;
		}
		
		if (!am.arenaExists(args[1])) {
			player.sendMessage(ChatColor.RED + "An arena by that name doesn't exist.");
			return;
		}
		
		Arena arena = am.getArena(args[1]);
		
		if (arena.getState() == ArenaState.DISABLED) {
			player.sendMessage(ChatColor.RED + "That arena is currently disabled.");
			return;
		}
		
		if (arena.getState() != ArenaState.JOINABLE) {
			player.sendMessage(ChatColor.RED + "You currently cannot join this arena.");
			return;
		}
		
		if (arena.getPlayers().size() >= 2) {
			player.sendMessage(ChatColor.RED + "This arena is full.");
			return;
		}
		
		Bukkit.getPluginManager().callEvent(new PlayerJoinArenaEvent(player, arena));
	}

	public String name() {
		return "join";
	}

	public String description() {
		return "joins the selected arena";
	}

	public String[] aliases() {
		return new String[] { "j" };
	}

	public boolean admin() {
		return false;
	}
}