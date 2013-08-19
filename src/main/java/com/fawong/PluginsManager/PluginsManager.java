package com.fawong.PluginsManager;

import freemarker.template.TemplateExceptionHandler;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Version;
import freemarker.template.Template;
import freemarker.template.Configuration;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.mcstats.Metrics;

/**
 * PluginsManager for Bukkit Minecraft Server
 *
 * @author fawong
 */
public class PluginsManager extends JavaPlugin {
  private Logger mcl = Logger.getLogger("Minecraft");
  private PluginManager pm;
  private PluginDescriptionFile pdFile;
  private ListPlugins lp = new ListPlugins(this);
  private HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
  public static Configuration cfg = new Configuration();

  public PluginsManager() {
    // Custom initialisation code here
    // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
    try {
      cfg.setDirectoryForTemplateLoading(new File("/tmp/"));
      cfg.setObjectWrapper(new DefaultObjectWrapper());
      cfg.setDefaultEncoding("UTF-8");
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
      //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      cfg.setIncompatibleImprovements(new Version(2, 3, 20));
    } catch (IOException ioe) {
      mcl.log(Level.SEVERE, pluginMessageString("THIS IS NOT GOOD"));
    }
  }

  public void onEnable() {
    // Start metrics
    try {
      Metrics metrics = new Metrics(this);
      metrics.start();
    } catch (IOException e) {
        // Failed to submit the stats :-(
    }

    // Custom enable code here including the registration of any events
    // Register events
    pm = getServer().getPluginManager();

    // EXAMPLE: Custom code, here we just output some info so we can check all is well
    pdFile = getDescription();
    mcl.log(Level.INFO, pluginMessageString(pdFile.getName() + " version " + pdFile.getVersion() + " enabled"));

    // Set Executor file to use
    getCommand("pluginsmanager").setExecutor(new PMgrCommand(this));
    getCommand("pmgr").setExecutor(new PMgrCommand(this));
    getCommand("plm").setExecutor(new PMgrCommand(this));
    getCommand("listplugins").setExecutor(new ListPluginsCommand(this));
    getCommand("lp").setExecutor(new ListPluginsCommand(this));

    saveDefaultConfig();
    if (getConfig().getBoolean("toggle") == false) {
      mcl.log(Level.INFO, pluginMessageString("Toggle value off"));
      mcl.log(Level.INFO, pluginMessageString("Plugin will be disabled"));
      pm.disablePlugin(this);
      return;
    } else {
      if (getConfig().getBoolean("toggle") == true) {
        mcl.log(Level.INFO, pluginMessageString("Settings have been successfully loaded"));
        lp.listPluginsToFile();
      } else {
        mcl.log(Level.WARNING, pluginMessageString("Please configure the config.yml file and reload the plugin"));
      }
    }
  }

  public void onDisable() {
    // NOTE: All registered events are automatically unregistered when a plugin is disabled
    // Custom disable code here
    // EXAMPLE: Custom code, here we just output some info so we can check all is well
    pdFile = getDescription();
    mcl.log(Level.INFO, pluginMessageString(pdFile.getName() + " version " + pdFile.getVersion() + " disabled"));
    pm.disablePlugin(this);
  }

  protected String pluginMessageString(String s) {
    return "[PluginsManager] " + s;
  }

  public boolean isDebugging(final Player player) {
    if (debugees.containsKey(player)) {
      return debugees.get(player);
    } else {
      return false;
    }
  }

  public void setDebugging(final Player player, final boolean value) {
    debugees.put(player, value);
  }
}
