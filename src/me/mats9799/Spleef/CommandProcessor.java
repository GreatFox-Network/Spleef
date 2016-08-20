package me.matsync.Spleef;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.matsync.Spleef.command.CreateArena;
import me.matsync.Spleef.command.Help;
import me.matsync.Spleef.command.Join;
import me.matsync.Spleef.command.Leave;
import me.matsync.Spleef.command.Reload;
import me.matsync.Spleef.command.SetFloor;
import me.matsync.Spleef.command.SetMainLobby;
import me.matsync.Spleef.command.SetSpawn;
import me.matsync.Spleef.command.SubCommand;
import me.matsync.Spleef.command.Version;

public class CommandProcessor implements CommandExecutor {

	private static CommandProcessor instance;
	private List<SubCommand> commands;
	
	private CommandProcessor() {
		commands = new ArrayList<SubCommand>();
		setup();
	}

	public static CommandProcessor getInstance() {
		if (instance == null) {
			instance = new CommandProcessor();
		}
		return instance;
	}
	
	private void setup() {
		commands.add(new CreateArena());
		commands.add(new Help());
		commands.add(new Join());
		commands.add(new Leave());
		commands.add(new Reload());
		commands.add(new SetFloor());
		commands.add(new SetMainLobby());
		commands.add(new SetSpawn());
		commands.add(new Version());
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players may use this command.");
			return true;
		}

		Player player = (Player) sender;
		
		if (command.getName().equalsIgnoreCase("leave")) {
			player.performCommand("s l");
			return true;
		}

		if (args.length == 0) {
			getCommand("help").execute(player, args);
			return true;
		}

		SubCommand cmd = getCommand(args[0]);

		if (cmd == null) {
			player.sendMessage(ChatColor.RED + "Invalid argument. Type /spleef help for a list of commands.");
			return true;
		}

		if (cmd.admin() && !player.hasPermission("spleef.admin")) {
			player.sendMessage(ChatColor.RED + "You don't have permission to do this.");
			return true;
		}

		try {
			cmd.execute(player, args);
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "Something went wrong. Check the console for more info.");
			e.printStackTrace();
		}
		return true;
	}
	
	public SubCommand getCommand(String command) {
		for (SubCommand cmd : commands) {
			if (cmd.name().equalsIgnoreCase(command)) {
				return cmd;
			}
			for (String alias : cmd.aliases()) {
				if (alias.equalsIgnoreCase(command)) {
					return cmd;
				}
			}
		}
		return null;
	}
}