package me.matsync.Spleef.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.matsync.Spleef.arena.Arena;

public class PlayerJoinArenaEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Arena arena;
	private Player player;
	
	public PlayerJoinArenaEvent(Player player, Arena arena) {
		this.arena = arena;
		this.player = player;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
}