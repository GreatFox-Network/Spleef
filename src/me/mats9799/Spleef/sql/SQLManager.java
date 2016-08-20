package me.matsync.Spleef.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matsync.Spleef.Spleef;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.util.Messages;

public class SQLManager {

	private SQLManager() {
		SettingsManager sm = SettingsManager.getInstance();
		
		String ip = sm.getSQLYaml().getDatabaseIP();
		String database = sm.getSQLYaml().getDatabaseName();
		String username = sm.getSQLYaml().getUsername();
		String password = sm.getSQLYaml().getPassword();
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
		} catch (Exception e) {
			Spleef.getPluginLogger().error("Could not connect to the SQL database! Have you configured everything correctly?");
		}
		
		new TableCreator();
	}
	
	private static SQLManager instance;
	
	public static SQLManager getInstance() {
        if (instance == null) {
            instance = new SQLManager();
        }
        
        return instance;
    }
	
	private Connection connection;
	
	public synchronized void checkNameChange(Player player) {
    	try {
    		PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?;");
    		ps.setString(1, player.getUniqueId().toString());
    		
    		ResultSet rs = ps.executeQuery();
    		rs.next();
    		
    		String name = rs.getString("name");
    		
    		if (!name.equalsIgnoreCase(player.getName())) {
    			PreparedStatement update = connection.prepareStatement("UPDATE `spleef` SET name = ? WHERE uuid = ?;");
    			update.setString(1, player.getName());
    			update.setString(2, player.getUniqueId().toString());
    			update.executeUpdate();
    			update.close();
    		}
    		ps.close();
    		rs.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
	public synchronized boolean contains(Player player) {
    	try {
    		PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?;");
    		ps.setString(1, String.valueOf(player.getUniqueId()));
    		
    		ResultSet rs = ps.executeQuery();
    		boolean contains = rs.next();
    		
    		ps.close();
    		rs.close();
    		
    		return contains;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
	
	public synchronized int getELO(UUID uuid) {
    	try {
    		PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?;");
    		ps.setString(1, uuid.toString());
    		
    		ResultSet rs = ps.executeQuery();
    		rs.next();
    		
    		return rs.getInt("elo");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return 0;
    }
	
	public synchronized int getGames(UUID uuid) {
    	try {
    		PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?;");
    		ps.setString(1, uuid.toString());
    		
    		ResultSet rs = ps.executeQuery();
    		rs.next();
    		
    		return rs.getInt("games");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return 0;
    }
	
	public synchronized Map<UUID, Integer> getLeaderboardScores() {
		try {
			Map<UUID, Integer> map = new HashMap<UUID, Integer>();
			
			PreparedStatement ps = connection.prepareStatement("SELECT uuid, elo FROM `spleef`");
			
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				map.put(UUID.fromString(rs.getString("uuid")), rs.getInt("elo"));
			}
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized String getPlayerName(UUID uuid) {
    	try {
    		PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?");
    		ps.setString(1, uuid.toString());
    		
    		ResultSet rs = ps.executeQuery();
    		rs.next();
    		
    		return rs.getString("name");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
	
	public synchronized int getWins(UUID uuid) {
    	try {
    		PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?;");
    		ps.setString(1, uuid.toString());
    		
    		ResultSet rs = ps.executeQuery();
    		rs.next();
    		
    		return rs.getInt("wins");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return 0;
    }
	
	public synchronized double getWGR(Player player) {
    	try {
    		if (!contains(player)) return 0;
    		
    		PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?;");
    		ps.setString(1, player.getUniqueId().toString());
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			double games = rs.getDouble("games");
			double wins = rs.getDouble("wins");
			
			if (games == 0.0) {
				return 0;
			}
			if (games > 0.0 && wins == 0.0) {
				return 0;
			}
			if (games == wins) {
				return 100;
			}
			
			DecimalFormat df = new DecimalFormat("#.##");
			
			ps.close();
			rs.close();
			
			return Double.valueOf(df.format((wins / games) * 100).replace(',', '.'));
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
    }
	
	public synchronized void incrementStat(UUID uuid, String stat, int amount) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM `spleef` WHERE uuid = ?;");
    		ps.setString(1, String.valueOf(uuid));
    		
    		ResultSet rs = ps.executeQuery();
    		rs.next();
    		
    		int num = rs.getInt(stat);
			
			PreparedStatement update = connection.prepareStatement("UPDATE `spleef` SET " + stat + " = ? WHERE uuid = ?;");
			update.setInt(1, num + amount);
			update.setString(2, String.valueOf(uuid));
			update.executeUpdate();
			
			ps.close();
			update.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized boolean isConnected() throws SQLException {
		return connection != null || !connection.isClosed();
	}
	
	public synchronized void rateElo(Player winner, Player loser) {
		double r1 = 10^(getELO(winner.getUniqueId()) / 400);
		double r2 = 10^(getELO(loser.getUniqueId()) / 400);
		
		double e1 = r1 / (r1 + r2);
		double e2 = r2 / (r1 + r2);
		
		double x1 = getELO(winner.getUniqueId()) + 24 * (1 - e1);
		double x2 = getELO(loser.getUniqueId()) + 24 * (0 - e2);
		
		double gain = x1 - Math.round(getELO(winner.getUniqueId()));
		double loss = x2 - Math.round(getELO(loser.getUniqueId()));
		
		incrementStat(winner.getUniqueId(), "elo", (int) gain);
		incrementStat(loser.getUniqueId(), "elo", (int) loss);
		
		Messages.sendPlayerMessage(winner, ChatColor.AQUA + "You have gained +" + (int) gain + " ELO from " + loser.getName() + "!");
		Messages.sendPlayerMessage(loser, ChatColor.AQUA + "You have lost -" + (int) -loss + " ELO to " + winner.getName() + ".");
	}
	
	public synchronized void registerNewPlayer(Player player) {
    	try {
    		PreparedStatement ps = connection.prepareStatement("INSERT INTO `spleef` values(?, ?, 0, 0, 1600);");
    		ps.setString(1, player.getUniqueId().toString());
    		ps.setString(2, player.getName());
    		
    		ps.execute();
    		ps.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}