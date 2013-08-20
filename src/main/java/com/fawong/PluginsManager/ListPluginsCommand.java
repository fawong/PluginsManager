package com.fawong.PluginsManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

/**
 * Handle events for all lp and listplugins in-game commands
 * @author fawong
 */
public class ListPluginsCommand implements CommandExecutor {
  private final PluginsManager plugin;
  private final ListPlugins lp;

  public ListPluginsCommand(PluginsManager instance) {
    plugin = instance;
    lp = new ListPlugins(instance);
  }

  // Command related code here

  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (args.length != 0) {
      boolean col = args[0].equalsIgnoreCase("column");
      boolean row = args[0].equalsIgnoreCase("row");
      if (col || row) {
        String[] fpn = lp.getFullPluginNames();
        if (plugin.getConfig().getBoolean("alphabetize_plugin") == true) {
          Arrays.sort(fpn, String.CASE_INSENSITIVE_ORDER);
        }
        String sendstring = "";
        for (int i = 0; i < fpn.length; i++) {
          if (i < fpn.length - 1) {
            if (col) {
              sender.sendMessage(fpn[i]);
            }
            else if (row) {
              sendstring += fpn[i] + ", ";
            } else {
              plugin.getLogger().log(Level.SEVERE, "This should not happen.");
            }
          } else {
            sendstring += fpn[i];
            sender.sendMessage(sendstring);
          }
        }
        return true;
      } else {
        sender.sendMessage("You must specify either \"column\" or \"row\"");
        return true;
      }
    } else {
      sender.sendMessage("You must specify either \"column\" or \"row\"");
      return true;
    }
  }
}
