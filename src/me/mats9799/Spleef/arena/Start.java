package me.matsync.Spleef.arena;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.matsync.Spleef.util.Messages;

public class Start extends BukkitRunnable {

	private final Arena arena;
	private int time;
	
	public Start(Arena arena, int time) {
		this.arena = arena;
		this.time = time;
	}
	
	public void run() {
		if (arena.getPlayers().size() <= 1) {
			cancel();
		}
		
		if (time == 0) {
			arena.setState(ArenaState.INGAME);
			arena.setMove(true);
			cancel();
			
			for (UUID uuid : arena.getPlayers()) {
				Player player = Bukkit.getPlayer(uuid);
				
				Messages.sendTitleMessage(player, ChatColor.AQUA + "GO!", "", 5, 20, 5);
			}
		}
		
		time --;
	}
}