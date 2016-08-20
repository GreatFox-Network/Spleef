package me.matsync.Spleef.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BlockIterator;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.Spleef;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.arena.ArenaState;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.sql.SQLManager;

public class PlayerEvent implements Listener {

	ArenaManager am = ArenaManager.getInstance();
	SettingsManager sm = SettingsManager.getInstance();
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (!am.isPlaying(player)) return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		
		if (!am.isPlaying(player)) return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (sm.getCacheYaml().getConfigurationSection("main.lobby") != null) {
			player.teleport(sm.getCacheYaml().getLocation("main.lobby"));
		}
		
		if (!SQLManager.getInstance().contains(player)) {
			SQLManager.getInstance().registerNewPlayer(player);
		}
		
		Spleef.getScoreboardManager().registerNewScoreboard(player);
		
		player.setScoreboard(Spleef.getScoreboardManager().getScoreboard(player));
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (!am.isPlaying(player)) return;
		
		Arena arena = am.getArena(player);
		
		if (!arena.contains(player.getLocation())) {
			player.teleport(arena.getSpawn(arena.getPlayers().indexOf(player.getUniqueId()) + 1));
			player.sendMessage(ChatColor.RED + "You don't have permission to leave the arena.");
		}
		
		if (arena.isMove()) return;
		
		if (player.getLocation().distance(arena.getSpawn(arena.getPlayers().indexOf(player.getUniqueId()) + 1)) <= 2) return;
		
		player.teleport(arena.getSpawn(arena.getPlayers().indexOf(player.getUniqueId()) + 1));
		player.sendMessage(ChatColor.RED + "The game hasn't started yet!");
	}
	
	@EventHandler
	public void onPlayerLiquidTouch(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (!am.isPlaying(player)) return;
		
		if (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.LAVA
				&& player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.STATIONARY_LAVA
				&& player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.WATER
				&& player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.STATIONARY_WATER) return;
		
		Arena arena = am.getArena(player);
		
		if (arena.getState() != ArenaState.INGAME) return;
		
		for (UUID uuid : arena.getPlayers()) {
			Player p = Bukkit.getPlayer(uuid);
			
			if (p != player) {
				if (arena.getRound() < 5) {
					arena.restart(p);
				} else {
					arena.stop();
				}
				break;
			}
		}
	}
	
	@EventHandler
	public void onSnowballHit(ProjectileHitEvent event) {
		if (!(event.getEntity().getShooter() instanceof Player)) return;
		
		if (!(event.getEntity() instanceof Snowball)) return;
		
		Player player = (Player) event.getEntity().getShooter();
		
		if (!am.isPlaying(player)) return;
		
		BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(),
				event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0.0D, 4);
		
		Block hit = null;
		
		while (iterator.hasNext()) {
			hit = iterator.next();
			if (hit.getType() != Material.AIR) {
				break;
			}
		}
		
		if (hit.getType() == Material.SNOW_BLOCK) {
			hit.getWorld().playEffect(hit.getLocation(), Effect.STEP_SOUND, hit.getType());
			hit.setType(Material.AIR);
		}
	}
	
	@EventHandler
	public void onSnowballLaunch(ProjectileLaunchEvent event) {
		if (!(event.getEntity().getShooter() instanceof Player)) return;
		
		if (!(event.getEntity() instanceof Snowball)) return;
		
		Player player = (Player) event.getEntity().getShooter();
		
		if (!am.isPlaying(player)) return;
		
		Arena arena = am.getArena(player);
		
		if (!arena.isMove()) {
			event.setCancelled(true);
			return;
		}
		
		if (arena.getState() != ArenaState.INGAME) {
			event.setCancelled(true);
		}
	}
}