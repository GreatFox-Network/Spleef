package me.matsync.Spleef.listener;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.arena.JoinSign;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.util.Messages;

public class SignEvent implements Listener {
	
	ArenaManager am = ArenaManager.getInstance();
	SettingsManager sm = SettingsManager.getInstance();
	
	@EventHandler
	public void onSignCreate(SignChangeEvent event) {
		Player player = event.getPlayer();
		Sign sign = (Sign) event.getBlock().getState();
		
		if (!event.getLine(0).equalsIgnoreCase("spleef") || !player.hasPermission("spleef.admin")) return;
		
		if (event.getLine(1).equalsIgnoreCase("lb")) {
			int rank;
			
			try {
				rank = Integer.parseInt(event.getLine(2));
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Invalid rank number.");
				return;
			}
			
			sm.getCacheYaml().setLocation("main.leaderboard." + rank, sign.getLocation());
			Messages.sendPlayerMessage(player, ChatColor.AQUA + "You have created the #" + rank + " leaderboard.");
			return;
		}
		
		if (!am.arenaExists(event.getLine(1))) {
			player.sendMessage(ChatColor.RED + "An arena by the name " + event.getLine(1) + " doesn't exist.");
			return;
		}
		
		Arena arena = am.getArena(event.getLine(1));
		
		sm.getCacheYaml().setLocation("arena." + arena.getName() + ".sign", sign.getLocation());
		
		JoinSign js = new JoinSign(arena, sign);
		
		arena.setJoinSign(js);
		js.update();
		
		Messages.sendPlayerMessage(player, ChatColor.AQUA + "You have created the join sign for arena " + arena.getName() + ".");
	}

	@EventHandler
	public void onSignInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		if (!(event.getClickedBlock().getState() instanceof Sign)) return;
		
		Sign sign = (Sign) event.getClickedBlock().getState();
		
		if (!am.arenaExists(sign.getLine(2))) return;
		
		Arena arena = am.getArena(sign.getLine(2));
		
		event.getPlayer().performCommand("s j " + arena.getName());
	}
}