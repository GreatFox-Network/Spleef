package me.matsync.Spleef.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.mats9799.Lobby.api.Lobby;
import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.Spleef;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.arena.Start;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.event.PlayerJoinArenaEvent;
import me.matsync.Spleef.event.PlayerLeaveArenaEvent;
import me.matsync.Spleef.sql.SQLManager;
import me.matsync.Spleef.util.Messages;
import me.matsync.Spleef.util.PlayerUtils;

public class ArenaEvent implements Listener {
	
	ArenaManager am = ArenaManager.getInstance();
	SettingsManager sm = SettingsManager.getInstance();
	
	@EventHandler
	public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
		Arena arena = event.getArena();
		Player player = event.getPlayer();
		
		if (arena.hasPlayer(player)) return;
		
		if (arena.getSpawn(arena.getPlayers().size() + 1) == null) {
			player.sendMessage(ChatColor.RED + "Arena " + arena.getName() + " is missing a spawn! Please alert a staff member.");
		} else {
			player.teleport(arena.getSpawn(arena.getPlayers().size() + 1));
		}
		
		player.setAllowFlight(false);
		player.setGameMode(GameMode.SURVIVAL);
		
		Spleef.getScoreboardManager().removeScoreboard(player);
		
		arena.add(player);
		arena.getJoinSign().update();
		
		PlayerUtils.healPlayer(player);
		PlayerUtils.setDefaultGameInventory(player);
		
		Messages.sendPlayerMessage(player, ChatColor.AQUA + "You have joined the arena " + arena.getName() + ". Type "
				+ ChatColor.WHITE + "/leave " + ChatColor.AQUA + "to return to the main lobby.");
		
		if (arena.getPlayers().size() == 2) {
			new Start(arena, 5).runTaskTimer(Spleef.getPlugin(), 0, 20);
			
			for (UUID uuid : arena.getPlayers()) {
				Player p = Bukkit.getPlayer(uuid);
				
				Messages.sendTitleMessage(p, ChatColor.AQUA + "Get ready...", "", 5, 40, 5);
				p.setScoreboard(Spleef.getScoreboardManager().getArenaScoreboard(arena));
			}
		}
		
		Lobby.removePlayer(player);
	}
	
	@EventHandler
	public void onPlayerLeaveArena(PlayerLeaveArenaEvent event) {
		Arena arena = event.getArena();
		Player player = event.getPlayer();
		
		if (!arena.hasPlayer(player)) return;
		
		if (sm.getCacheYaml().getConfigurationSection("main.lobby") == null) {
			player.sendMessage(ChatColor.RED + "The main lobby has not been set up yet. Please alert a staff member.");
		} else {
			player.teleport(sm.getCacheYaml().getLocation("main.lobby"));
		}
		
		arena.remove(player);
		arena.getJoinSign().update();
		
		player.setGameMode(GameMode.ADVENTURE);
		player.setScoreboard(Spleef.getScoreboardManager().getScoreboard(player));
		
		PlayerUtils.healPlayer(player);
		PlayerUtils.clearItems(player);
		
		if (arena.getPlayers().size() == 1) {
			arena.setWins(player, 0);
			arena.stop();
			
			SQLManager.getInstance().incrementStat(player.getUniqueId(), "games", 1);
		}
		
		Lobby.addPlayer(player);
	}
}