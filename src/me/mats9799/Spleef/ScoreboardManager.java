package me.matsync.Spleef;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.sql.SQLManager;

public class ScoreboardManager {

	public ScoreboardManager() {
		sb = new HashMap<UUID, Scoreboard>();
	}
	
	private Map<UUID, Scoreboard> sb;
	
	public Map<UUID, Scoreboard> getPlayerScoreboards() {
		return sb;
	}
	
	public Scoreboard getScoreboard(Player player) {
		return sb.get(player.getUniqueId());
	}
	
	private String getDate() {
		Calendar cal = Calendar.getInstance();
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		return cal.get(Calendar.DAY_OF_MONTH) + " " + months[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
	}
	
	public Scoreboard getArenaScoreboard(Arena arena) {
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = s.registerNewObjective(ChatColor.AQUA + "Round " + arena.getRound(), "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (UUID uuid : arena.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			
			obj.getScore(ChatColor.WHITE + player.getName()).setScore(arena.getRoundsWon(player));
		}
		
		return s;
	}
	
	private Scoreboard getNewScoreboard(Player player) {
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = s.registerNewObjective(ChatColor.WHITE + ChatColor.BOLD.toString() + "Great" + ChatColor.GOLD + ChatColor.BOLD.toString() + "Fox", "dummy");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.getScore("").setScore(8);
		obj.getScore(ChatColor.AQUA + player.getName() + "'s stats").setScore(7);
		obj.getScore("Games: " + ChatColor.GRAY + SQLManager.getInstance().getGames(player.getUniqueId())).setScore(6);
		obj.getScore("Wins: " + ChatColor.GRAY + SQLManager.getInstance().getWins(player.getUniqueId())).setScore(5);
		obj.getScore("WGR: " + ChatColor.GRAY + SQLManager.getInstance().getWGR(player) + "%").setScore(4);
		obj.getScore(" ").setScore(3);
		obj.getScore("ELO: " + ChatColor.GRAY + SQLManager.getInstance().getELO(player.getUniqueId())).setScore(2);
		obj.getScore("  ").setScore(1);
		obj.getScore(ChatColor.AQUA + getDate()).setScore(0);
		
		return s;
	}
	
	public void registerNewScoreboard(Player player) {
		Scoreboard s = getNewScoreboard(player);
		
		sb.put(player.getUniqueId(), s);
	}
	
	public void removeScoreboard(Player player) {
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
	
	public void update(Player player) {
		sb.replace(player.getUniqueId(), getNewScoreboard(player));
	}
}