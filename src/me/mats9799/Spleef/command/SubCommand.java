package me.matsync.Spleef.command;

import org.bukkit.entity.Player;

public interface SubCommand {

	public void execute(Player player, String[] args);

	public String name();

	public String description();
	
	public String[] aliases();
	
	public boolean admin();
}