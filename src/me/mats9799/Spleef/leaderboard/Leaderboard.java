package me.matsync.Spleef.leaderboard;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import me.matsync.Spleef.Spleef;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.sql.SQLManager;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Leaderboard {

	public Leaderboard(Map base) {
		this.base = base;
	}
	
	Map base;
	SettingsManager sm = SettingsManager.getInstance();
	
	public Map<UUID, Integer> getLeaderboards() {
		return getSortedMap(base);
	}
	
	public UUID getPlayerFromRank(int rank) {
		int number = 1;
		for (UUID uuid : getLeaderboards().keySet()) {
			if (number == rank) {
				return uuid;
			}
			number ++;
		}
		return null;
	}
	
	public int getRankFromPlayer(Player player) {
		int rank = 1;
		for (UUID uuid : getLeaderboards().keySet()) {
			if (Bukkit.getPlayer(uuid) == player) {
				return rank;
			}
			rank ++;
		}
		return 0;
	}
	
	private Map getSortedMap(Map base) {
		Map unsorted = base;
		
		ScoreComparator compare = new ScoreComparator(unsorted);
		
		TreeMap<UUID, Integer> leaderboard = new TreeMap<UUID, Integer>(compare);
		leaderboard.putAll(unsorted);
		
		return leaderboard;
	}
	
	private String trimPlayerName(String name) {
		if (name.length() <= 12) {
			return name;
		}
		return name.substring(0, 12);
	}
	
	public void updateLeaderboards() {
		try {
			for (String index : sm.getCacheYaml().getConfigurationSection("main.leaderboard").getKeys(false)) {
				if (sm.getCacheYaml().getLocation("main.leaderboard." + index).getBlock().getState() instanceof Sign) {
					try {
						Sign sign = (Sign) sm.getCacheYaml().getLocation("main.leaderboard." + index).getBlock().getState();
						int rank = Integer.parseInt(index);
						UUID uuid = getPlayerFromRank(rank);
						
						if (uuid != null) {
							sign.setLine(0, "");
							sign.setLine(1, ChatColor.BOLD + index + ". " + ChatColor.DARK_BLUE + trimPlayerName(SQLManager.getInstance().getPlayerName(uuid)));
							sign.setLine(2, ChatColor.YELLOW + ChatColor.BOLD.toString() + "ELO: " + ChatColor.BLACK + SQLManager.getInstance().getELO(uuid));
							sign.setLine(3, "");
						}
						else {
							sign.setLine(0, "");
							sign.setLine(1, ChatColor.BOLD + index + ". " + ChatColor.DARK_BLUE + "---");
							sign.setLine(2, ChatColor.YELLOW + ChatColor.BOLD.toString() + "ELO: " + ChatColor.BLACK + "---");
							sign.setLine(3, "");
						}
						sign.update();
					} catch (Exception e) {
						Spleef.getPluginLogger().error("Failed to refresh Leaderboard #" + index + ". Caused by: " + e.getCause());
					}
				}
			}
			
			Spleef.getPluginLogger().info("Leaderboards have been refreshed");
			
		} catch (NullPointerException e) {
			Spleef.getPluginLogger().warn("Leaderboard signs have not been found!");
		}
	}
}