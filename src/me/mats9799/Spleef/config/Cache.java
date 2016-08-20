package me.matsync.Spleef.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.matsync.Spleef.Spleef;

public class Cache extends Yaml {

	public Cache() {
		super(Spleef.getPlugin().getDataFolder().getPath(), "cache.yml", true);
	}
	
	public Location getLocation(String path) {
		if (contains(path)) {
			int x = getInt(path + ".x");
			int y = getInt(path + ".y");
			int z = getInt(path + ".z");
			int yaw = getInt(path + ".yaw");
			
			Location location = new Location(Bukkit.getWorld(getString(path + ".world")), x, y, z, yaw, 0);
			return location;
		}
		return null;
	}
	
	public void setLocation(String path, Location location) {
		set(path + ".world", location.getWorld().getName());
		set(path + ".x", (int) location.getX());
		set(path + ".y", (int) location.getY());
		set(path + ".z", (int) location.getZ());
		set(path + ".yaw", (int) location.getYaw());
		save();
	}
	
	public void setBounds(String path, Location min, Location max) {
		if (min.getWorld() != max.getWorld()) return;
		
		set(path + ".bounds.min.x", min.getX());
		set(path + ".bounds.min.y", min.getY());
		set(path + ".bounds.min.z", min.getZ());
		set(path + ".bounds.min.world", min.getWorld().getName());
		set(path + ".bounds.max.x", max.getX());
		set(path + ".bounds.max.y", max.getY());
		set(path + ".bounds.max.z", max.getZ());
		set(path + ".bounds.max.world", max.getWorld().getName());
		save();
	}
}