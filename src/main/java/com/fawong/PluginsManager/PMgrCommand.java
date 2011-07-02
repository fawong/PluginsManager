package com.fawong.PluginsManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

/**
* Handle events for all pmgr commands
* @author fawong
*/
public class PMgrCommand implements CommandExecutor, PluginsManagerSettings{
	private final PluginsManager plugin;

	public PMgrCommand(PluginsManager instance) {
		plugin = instance;
	}

	// Command related code here
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage("poke");
			System.out.println("poke");
			return true;
		} else {
			sender.sendMessage(plugin.pluginMessageString("You need to be a player to run this command"));
			return true;
		}
	}
}
