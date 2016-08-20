package me.matsync.Spleef.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.matsync.Spleef.Spleef;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.sql.SQLManager;
import me.matsync.Spleef.util.Messages;
import me.matsync.Spleef.util.PlayerUtils;

public class Arena {

	SettingsManager sm = SettingsManager.getInstance();
	private final String name;
	private final Location min, max;
	private boolean move;
	private int round;
	private Location spawn1, spawn2;
	private ArenaState state;
	private Floor floor;
	private JoinSign sign;
	private Map<UUID, Integer> players = new HashMap<UUID, Integer>();
	
	public Arena(String name, Location min, Location max) {
		this.min = min;
		this.max = max;
		this.setMove(false);
		this.name = name;
		this.round = 1;
		this.state = ArenaState.JOINABLE;
	}
	
	public Floor getFloor() {
		return floor;
	}
	
	public void setFloor(Floor floor) {
		this.floor = floor;
	}
	
	public JoinSign getJoinSign() {
		return sign;
	}
	
	public void setJoinSign(JoinSign sign) {
		this.sign = sign;
	}
	
	public Location getMin() {
		return min;
	}

	public Location getMax() {
		return max;
	}
	
	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}
	
	public String getName() {
		return name;
	}
	
	public int getRound() {
		return round;
	}
	
	public void setRound(int round) {
		this.round = round;
	}
	
	public ArenaState getState() {
		return state;
	}

	public void setState(ArenaState state) {
		this.state = state;
		
		if (sign != null) {
			sign.update();
		}
	}
	
	public void add(Player player) {
		if (players.containsKey(player.getUniqueId())) return;
		
		players.put(player.getUniqueId(), 0);
	}
	
	public void remove(Player player) {
		if (!players.containsKey(player.getUniqueId())) return;
		
		players.remove(player.getUniqueId());
	}
	
	public boolean contains(Location location) {
		if (location.getX() >= min.getX() && location.getX() <= max.getX() && location.getY() >= min.getY()
				&& location.getY() <= max.getY() && location.getZ() >= min.getZ() && location.getZ() <= max.getZ()) return true;
		
		return false;
	}
	
	public List<UUID> getPlayers() {
		List<UUID> list = new ArrayList<UUID>();
		
		for (UUID uuid : players.keySet()) {
			list.add(uuid);
		}
		
		return list;
	}
	
	public int getRoundsWon(Player player) {
		return players.get(player.getUniqueId());
	}
	
	public Location getSpawn(int number) {
		switch (number) {
		case 1: return spawn1;
		case 2: return spawn2;
		}
		return null;
	}
	
	public boolean hasPlayer(Player player) {
		return players.containsKey(player.getUniqueId());
	}
	
	//Do this is if all the five rounds aren't played yet
	public void restart(Player winner) {
		int index = 1;
		setWins(winner, getRoundsWon(winner) + 1);
		
		for (UUID uuid : getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			
			Messages.sendPlayerMessage(player, ChatColor.AQUA + winner.getName() + " has won Round " + round + "!");
			player.setScoreboard(Spleef.getScoreboardManager().getArenaScoreboard(Arena.this));
			player.teleport(getSpawn(index));
			
			index ++;
		}
		
		setMove(false);
		setRound(round + 1);
		floor.refresh();
		
		new BukkitRunnable() {

			public void run() {
				setMove(true);
				for (UUID uuid : getPlayers()) {
					Player player = Bukkit.getPlayer(uuid);
					
					Messages.sendTitleMessage(player, ChatColor.AQUA + "GO!", "", 5, 30, 5);
					PlayerUtils.setDefaultGameInventory(player);
				}
			}
			
		}.runTaskLater(Spleef.getPlugin(), 100);
		
		return;
	}
	
	public void setSpawn(int number, Location location) {
		switch (number) {
		case 1: this.spawn1 = location;
		case 2: this.spawn2 = location;
		}
		return;
	}
	
	public void setWins(Player player, int wins) {
		if (!players.containsKey(player.getUniqueId())) return;
		
		players.replace(player.getUniqueId(), wins);
	}
	
	
	//Do this if all five rounds are played
	public void stop() {
		if (state == ArenaState.JOINABLE) {
			for (UUID uuid : getPlayers()) {
				Messages.sendPlayerMessage(Bukkit.getPlayer(uuid), ChatColor.AQUA + "Your opponent has left the arena.");
			}
			return;
		}
		
		Player ow = null;
		int score = 0;

		for (UUID uuid : players.keySet()) {
			Player player = Bukkit.getPlayer(uuid);
				
			if (getRoundsWon(player) > score) {
				score = getRoundsWon(player);
				ow = player;
			}
		}
		
		setState(ArenaState.RESTARTING);
		
		for (UUID uuid : getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			
			player.setAllowFlight(true);
			
			PlayerUtils.shootRandomFirework(player.getLocation());
			PlayerUtils.healPlayer(player);
			PlayerUtils.clearItems(player);
			
			Messages.sendTitleMessage(player, ChatColor.AQUA + ow.getName() + " has won the game!", "", 5, 60, 5);
			
			SQLManager.getInstance().incrementStat(uuid, "games", 1);
			if (ow == player) {
				SQLManager.getInstance().incrementStat(uuid, "wins", 1);
			}
		}
		
		Player loser = null;
		
		for (UUID u : getPlayers()) {
			if (Bukkit.getPlayer(u) != ow) {
				loser = Bukkit.getPlayer(u);
				break;
			}
		}
		
		SQLManager.getInstance().rateElo(ow, loser);
		
		floor.refresh(true, 1);
		
		new BukkitRunnable() {

			public void run() {
				for (UUID uuid : getPlayers()) {
					Player player = Bukkit.getPlayer(uuid);
					
					player.setAllowFlight(false);
					player.setGameMode(GameMode.ADVENTURE);

					Spleef.getScoreboardManager().update(player);
					player.setScoreboard(Spleef.getScoreboardManager().getScoreboard(player));
					
					if (sm.getCacheYaml().getLocation("main.lobby") != null) {
						player.teleport(sm.getCacheYaml().getLocation("main.lobby"));
					} else {
						player.sendMessage(ChatColor.RED + "The main lobby has not been set up yet. Please alert a staff member.");
					}
					
					PlayerUtils.clearItems(player);
					PlayerUtils.healPlayer(player);
				}
				
				players.clear();
				sign.update();
			}
			
		}.runTaskLater(Spleef.getPlugin(), 100);
	}
}