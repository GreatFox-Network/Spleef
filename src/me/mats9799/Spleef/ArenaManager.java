package me.matsync.Spleef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.arena.Floor;
import me.matsync.Spleef.arena.JoinSign;
import me.matsync.Spleef.config.SettingsManager;

public class ArenaManager {

	private ArenaManager() {
		arenas = new ArrayList<Arena>();
		sm = SettingsManager.getInstance();
	}
	
	private static ArenaManager instance;
	
	public static ArenaManager getInstance() {
		if (instance == null) {
			instance = new ArenaManager();
		}
		return instance;
	}
	
	SettingsManager sm;
	private List<Arena> arenas;
	
	public boolean arenaExists(String name) {
		return getArena(name) != null;
	}
	
	public Arena getArena(String name) {
		for (Arena arena : arenas) {
			if (arena.getName().equalsIgnoreCase(name)) {
				return arena;
			}
		}
		return null;
	}
	
	public Arena getArena(Player player) {
		for (Arena arena : arenas) {
			if (arena.hasPlayer(player)) {
				return arena;
			}
		}
		return null;
	}
	
	public List<Arena> getArenas() {
		return arenas;
	}
	
	public List<UUID> getPlayers() {
		List<UUID> list = new ArrayList<UUID>();
		
		for (Arena arena : arenas) {
			for (UUID uuid : arena.getPlayers()) {
				list.add(uuid);
			}
		}
		
		return list;
	}
	
	public boolean isPlaying(Player player) {
		return getArena(player) != null;
	}
	
	public void loadArenas() {
		if (sm.getCacheYaml().getConfigurationSection("arena") == null) {
			Spleef.getPluginLogger().warn("Note no Spleef arenas were found and none will appear ingame");
			return;
		}
		
		for (String name : sm.getCacheYaml().getConfigurationSection("arena").getKeys(false)) {
			try {
				Location min = sm.getCacheYaml().getLocation("arena." + name + ".bounds.min");
				Location max = sm.getCacheYaml().getLocation("arena." + name + ".bounds.max");
				
				Location floorMin = sm.getCacheYaml().getLocation("arena." + name + ".floor.bounds.min");
				Location floorMax = sm.getCacheYaml().getLocation("arena." + name + ".floor.bounds.max");
				
				Location spawn1 = sm.getCacheYaml().getLocation("arena." + name + ".spawn.1");
				Location spawn2 = sm.getCacheYaml().getLocation("arena." + name + ".spawn.2");
				
				Arena arena = new Arena(name, min, max);
				
				arenas.add(arena);
				
				Floor floor = new Floor(floorMin, floorMax, floorMin.getWorld(), arena);
				
				arena.setFloor(floor);
				arena.setSpawn(1, spawn1);
				arena.setSpawn(2, spawn2);
				
				if (sm.getCacheYaml().getConfigurationSection("arena." + name + ".sign") != null) {
					if (sm.getCacheYaml().getLocation("arena." + name + ".sign").getBlock().getState() instanceof Sign) {
						JoinSign sign = new JoinSign(arena, (Sign) sm.getCacheYaml().getLocation("arena." + name + ".sign").getBlock().getState());
						
						arena.setJoinSign(sign);
						
						sign.update();
					}
				}
			} catch (NullPointerException e) {
				Spleef.getPluginLogger().warn("Could not load arena " + name + ": Data is missing");
				e.printStackTrace();
			}
		}
	}
}