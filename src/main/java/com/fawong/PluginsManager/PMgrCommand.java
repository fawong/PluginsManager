package com.fawong.PluginsManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Server;

/**
 * Handle events for all pmgr commands
 * @author fawong
 */
public class PMgrCommand implements CommandExecutor, PluginsManagerSettings {
  private final PluginsManager plugin;
  private PluginManager pm;

  public PMgrCommand(PluginsManager instance) {
    plugin = instance;
    pm = instance.getServer().getPluginManager();
  }

  // Command related code here
  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    Player player = (Player) sender;
    player.sendMessage("poke");
    if (args[0].equalsIgnoreCase("disable")) {
      pm.disablePlugin(pm.getPlugin(args[1]));
      player.sendMessage("Disabled" + args[1]);
    }
    else if (args[0].equalsIgnoreCase("enable")) {
      pm.enablePlugin(pm.getPlugin(args[1]));
      player.sendMessage("Enabled" + args[1]);
    }
    System.out.println("poke");
    return true;
  }
}
