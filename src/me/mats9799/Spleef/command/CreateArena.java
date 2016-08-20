package me.matsync.Spleef.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.matsync.Spleef.ArenaManager;
import me.matsync.Spleef.Spleef;
import me.matsync.Spleef.arena.Arena;
import me.matsync.Spleef.config.SettingsManager;
import me.matsync.Spleef.util.Messages;

public class CreateArena implements SubCommand {

	public void execute(Player player, String[] args) {
		ArenaManager am = ArenaManager.getInstance();
		SettingsManager sm = SettingsManager.getInstance();
		
		Selection selection = Spleef.getWorldEditPlugin().getSelection(player);
		
		if (selection == null) {
			player.sendMessage(ChatColor.RED + "Make a WorldEdit selection before creating an arena.");
			return;
		}
		
		if (args.length <= 1) {
			player.sendMessage(ChatColor.RED + "Define an arena name.");
			return;
		}
		
		if (am.arenaExists(args[1])) {
			player.sendMessage(ChatColor.RED + "An arena by that name already exists.");
			return;
		}
		
		Arena arena = new Arena(args[1], selection.getMinimumPoint(), selection.getMaximumPoint());
		
		am.getArenas().add(arena);
		sm.getCacheYaml().setBounds("arena." + args[1], selection.getMinimumPoint(), selection.getMaximumPoint());
		
		Messages.sendPlayerMessage(player, ChatColor.AQUA + "You have created the arena " + arena.getName() + ".");
	}

	public String name() {
		return "createarena";
	}
	
	public String description() {
		return "creates a new arena";
	}

	public String[] aliases() {
		return new String[] { "ca" };
	}

	public boolean admin() {
		return true;
	}
}