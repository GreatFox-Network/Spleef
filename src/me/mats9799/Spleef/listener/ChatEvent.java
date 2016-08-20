package me.matsync.Spleef.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.util.Messages;

public class ChatEvent implements Listener {

	ArenaManager am = ArenaManager.getInstance();
	SettingsManager sm = SettingsManager.getInstance();
	
	@EventHandler
	public void onPlayerCommandSend(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		
		if (!am.isPlaying(player)) return;
		
		if (player.hasPermission("spleef.admin")) return;
		
		String command = event.getMessage().substring(1, event.getMessage().length());
		
		if (sm.getConfigYaml().contains(command)) return;
		
		player.sendMessage(ChatColor.RED + "You are not allowed to use this command while playing Spleef.");
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if (!am.isPlaying(player)) return;
		
		Arena arena = am.getArena(player);
		
		event.setCancelled(true);
		
		for (UUID uuid : arena.getPlayers()) {
			Messages.sendPlayerMessage(Bukkit.getPlayer(uuid), ChatColor.WHITE + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());
		}
	}
}