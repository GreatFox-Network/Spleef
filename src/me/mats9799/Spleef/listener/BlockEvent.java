package me.matsync.Spleef.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.arena.ArenaState;

public class BlockEvent implements Listener {

	ArenaManager am = ArenaManager.getInstance();
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		if (!am.isPlaying(player)) return;
		
		Arena arena = am.getArena(player);
		
		if (!arena.isMove()) {
			event.setCancelled(true);
			return;
		}
		
		if (arena.getState() == ArenaState.INGAME) {
			if (event.getBlock().getType() == Material.SNOW_BLOCK) {
				event.getBlock().setType(Material.AIR);
				
				if (player.getItemInHand().getType() == Material.IRON_SPADE) {
					player.getItemInHand().setDurability((short) 0);
				}
				
				if (player.getItemInHand().getType() == Material.DIAMOND_SPADE) {
					player.getItemInHand().setDurability((short) 0);
				}
				return;
			}
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		if (!am.isPlaying(player)) return;
		
		if (am.getArena(player).getFloor().contains(event.getBlock())) return;
		
		event.setCancelled(true);
	}
}