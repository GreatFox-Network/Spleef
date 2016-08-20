package me.matsync.Spleef.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import me.matsync.Spleef.Spleef;

public class Floor {

	private final Location min, max;
	private final World world;
	private final Arena arena;
	
	public Floor(Location min, Location max, World world, Arena arena) {
		this.min = min;
		this.max = max;
		this.world = world;
		this.arena = arena;
	}
	
	public Location getMin() {
		return min;
	}
	
	public Location getMax() {
		return max;
	}
	
	public World getWorld() {
		return world;
	}
	
	public boolean contains(Block block) {
		Location location = block.getLocation();
		
		if (location.getX() >= min.getX() && location.getX() <= max.getX() && location.getY() >= min.getY()
				&& location.getY() <= max.getY() && location.getZ() >= min.getZ() && location.getZ() <= max.getZ()) return true;
		
		return false;
	}
	
	public void refresh() {
		for (int x = min.getBlockX(); x <= max.getBlockX(); x ++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y ++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z ++) {
					Block block = world.getBlockAt(new Location(world, x, y, z));
					
					if (block.getType() == Material.AIR) {
						block.setType(Material.SNOW_BLOCK);
					}
				}
			}
		}
	}
	
	public void refresh(boolean effect, long speed) {
		List<BlockState> blocks = new ArrayList<BlockState>();
		
		for (int x = min.getBlockX(); x <= max.getBlockX(); x ++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y ++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z ++) {
					Block block = world.getBlockAt(new Location(world, x, y, z));
					
					if (block.getType() == Material.AIR) {
						blocks.add(block.getState());
					}
				}
			}
		}
		
		new BukkitRunnable() {
            int i = -1;
            public void run() {
                if (i != blocks.size() - 1) {
                    i ++;
                    BlockState state = blocks.get(i);
                    state.setType(Material.SNOW_BLOCK);
                    state.update(true, false);
                    
                    if (effect) {
                    	state.getBlock().getWorld().playEffect(state.getLocation(), Effect.STEP_SOUND, state.getBlock().getType());
                    }
                } else {
                    blocks.clear();
                    arena.setState(ArenaState.JOINABLE);
                    cancel();
                }
            }
        }.runTaskTimer(Spleef.getPlugin(), speed, speed);
	}
}