package com.fawong.PluginsManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * PluginsManager for Bukkit Minecraft Server
 *
 * @author fawong
 */
public class PluginsManager extends JavaPlugin {
  //private PluginDescriptionFile pdFile;
  private Logger mcl = Logger.getLogger("Minecraft");
  private PluginManager pm;
  private ListPlugins lp = new ListPlugins(this);
  private HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
  private File defaultTemplateFile = null;
  protected static Configuration cfg = new Configuration();

  public PluginsManager() {
    // Custom initialisation code here
    // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
  }

  public void onEnable() {
    // Start metrics
    try {
      Metrics metrics = new Metrics(this);
      metrics.start();
    } catch (IOException e) {
      e.printStackTrace();
      // Failed to submit the stats :(
    }

    // Custom enable code here including the registration of any events
    // Register events
    pm = getServer().getPluginManager();

    // EXAMPLE: Custom code, here we just output some info so we can check all is well
    //pdFile = getDescription();
    //mcl.log(Level.INFO, pdFile.getName() + " version " + pdFile.getVersion() + " enabled");

    // Set Executor file to use
    getCommand("pluginsmanager").setExecutor(new PMgrCommand(this));
    getCommand("pmgr").setExecutor(new PMgrCommand(this));
    getCommand("plm").setExecutor(new PMgrCommand(this));
    getCommand("listplugins").setExecutor(new ListPluginsCommand(this));
    getCommand("lp").setExecutor(new ListPluginsCommand(this));

    final File configFile = new File(getDataFolder().getAbsolutePath(), "config.yml");
    final File templateFile = new File(getDataFolder().getAbsolutePath(), "html.template");
    if (!configFile.exists()) {
      saveDefaultConfig();
      mcl.log(Level.WARNING, "Please configure the " + configFile.getName() + " file and reload the plugin");
      pm.disablePlugin(this);
      return;
    }

    if (!templateFile.exists()) {
      saveDefaultTemplateConfig();
      mcl.log(Level.WARNING, "Please configure the " + templateFile.getName() + " file and reload the plugin");
    }

    if (getConfig().getBoolean("toggle") == false) {
      mcl.log(Level.INFO, "Toggle value off, plugin will be disabled");
      pm.disablePlugin(this);
      return;
    }

    try {
      String template_file_location = getConfig().getString("template-file-location");
      if (template_file_location.equals("default")) {
        template_file_location = getDataFolder().getAbsolutePath();
      }
      cfg.setDirectoryForTemplateLoading(new File(template_file_location));
      cfg.setObjectWrapper(new DefaultObjectWrapper());
      cfg.setDefaultEncoding("UTF-8");
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
      //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      cfg.setIncompatibleImprovements(new Version(2, 3, 20));

      lp.listPluginsToFile();
    } catch (IOException ioe) {
      mcl.log(Level.SEVERE, "Could not set directory for template: " + ioe);
      ioe.printStackTrace();
    }
  }

  public void onDisable() {
    // NOTE: All registered events are automatically unregistered when a plugin is disabled
    // Custom disable code here
    // EXAMPLE: Custom code, here we just output some info so we can check all is well
    //pdFile = getDescription();
    //mcl.log(Level.INFO, pdFile.getName() + " version " + pdFile.getVersion() + " disabled");
    pm.disablePlugin(this);
  }

  public void saveDefaultTemplateConfig() {
    if (defaultTemplateFile == null) {
      defaultTemplateFile = new File(getDataFolder().getAbsolutePath(), "html.template");
    }
    if (!defaultTemplateFile.exists()) {
      saveResource("html.template", false);
    }
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
