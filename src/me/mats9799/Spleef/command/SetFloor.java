package me.matsync.Spleef.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.Spleef;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.arena.Floor;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.util.Messages;

public class SetFloor implements SubCommand {

	public void execute(Player player, String[] args) {
		ArenaManager am = ArenaManager.getInstance();
		SettingsManager sm = SettingsManager.getInstance();
		
		Selection selection = Spleef.getWorldEditPlugin().getSelection(player);
		
		if (selection == null) {
			player.sendMessage(ChatColor.RED + "Make a WorldEdit selection before setting the floor.");
			return;
		}
		
		if (args.length <= 1) {
			player.sendMessage(ChatColor.RED + "Define the arena name.");
			return;
		}
		
		if (!am.arenaExists(args[1])) {
			player.sendMessage(ChatColor.RED + "An arena by that name doesn't exist.");
			return;
		}
		
		Arena arena = am.getArena(args[1]);
		Floor floor = new Floor(selection.getMinimumPoint(), selection.getMaximumPoint(), selection.getWorld(), arena);
		
		arena.setFloor(floor);
		
		sm.getCacheYaml().setBounds("arena." + arena.getName() + ".floor", selection.getMinimumPoint(), selection.getMaximumPoint());
		
		Messages.sendPlayerMessage(player, ChatColor.AQUA + "Successfully set the floor for arena " + arena.getName() + ".");
	}

	public String name() {
		return "setfloor";
	}

	public String description() {
		return "sets the snow floor for an arena";
	}

	public String[] aliases() {
		return new String[] { "sf" };
	}

	public boolean admin() {
		return true;
	}
}