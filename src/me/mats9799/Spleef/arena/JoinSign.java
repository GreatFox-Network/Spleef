package me.matsync.Spleef.arena;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import me.matsync.Spleef.Spleef;
public class JoinSign {

	private final Arena arena;
	private Sign sign;
	
	public JoinSign(Arena arena, Sign sign) {
		this.arena = arena;
		this.sign = sign;
	}
	
	public Sign getSign() {
		return sign;
	}
	
	public void update() {
		if (sign == null) {
			Spleef.getPluginLogger().warn("Could not update the join sign of arena " + arena.getName());
			return;
		}
		
		switch (arena.getState()) {
		case DISABLED:
			sign.setLine(0, ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "[ DISABLED ]");
			break;
		case INGAME:
			sign.setLine(0, ChatColor.RED + ChatColor.BOLD.toString() + "[ INGAME ]");
			break;
		case JOINABLE:
			sign.setLine(0, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "[ JOINABLE ]");
			break;
		case RESTARTING:
			sign.setLine(0, ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "[ RESTARTING ]");
			break;
		}
		sign.setLine(1, ChatColor.BOLD + "Spleef");
		sign.setLine(2, arena.getName());
		sign.setLine(3, ChatColor.DARK_BLUE + String.valueOf(arena.getPlayers().size()) + "/2");
		
		sign.update();
	}
}