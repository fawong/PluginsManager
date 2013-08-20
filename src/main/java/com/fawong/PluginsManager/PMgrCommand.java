package com.fawong.PluginsManager;

import java.util.Arrays;
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
public class PMgrCommand implements CommandExecutor {
  private final PluginsManager plugin;
  private PluginManager pm;

  public PMgrCommand(PluginsManager instance) {
    plugin = instance;
    pm = instance.getServer().getPluginManager();
  }

  // Command related code here
  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (args.length >= 2) {
      String type = args[0];
      String plugin = args[2];
    } else {
    }

    if (sender instanceof Player) {
      sender.sendMessage("HI THERE");
    }
    return true;
  }

  private void enableDisablePlugin(String type, String plugin) {
    if (type.equalsIgnoreCase("disable")) {
      pm.disablePlugin(pm.getPlugin(plugin));
    } else if (type.equalsIgnoreCase("enable")) {
      pm.enablePlugin(pm.getPlugin(plugin));
    }
  }
}
