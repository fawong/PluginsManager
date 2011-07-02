package com.fawong.PluginsManager;

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
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

/**
* PluginsManager for Bukkit Minecraft Server
*
* @author fawong
*/
public class PluginsManager extends JavaPlugin implements PluginsManagerSettings {
	private String toggle_value = "";
	private Logger mcl = Logger.getLogger("Minecraft");
	private PluginManager pm;
	private PluginDescriptionFile pdFile = getDescription();
	private Plugin[] listofplugins;
	private String[] nameofplugins;
	private Properties prop = new Properties();

	public PluginsManager() {
		// Custom initialisation code here
		// NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
	}

	public void onEnable() {
		// Custom enable code here including the registration of any events

		// Register events
		pm = getServer().getPluginManager();

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		mcl.log(Level.INFO, "[PluginsManager]: " + pdFile.getName() + " version " + pdFile.getVersion() + " enabled");

		// Set Executor file to use
		getCommand("pmgr").setExecutor(new PMgrCommand(this));
		getCommand("lp").setExecutor(new ListPluginsCommand(this));
		getCommand("listplugins").setExecutor(new ListPluginsCommand(this));

		// Call method to list plugins to a file.
		if (loadSettings()) {
			mcl.log(Level.INFO, "[PluginsManager]: settings have been loaded");
			if (toggle_value.equals("off")) {
				mcl.log(Level.INFO, "[PluginsManager]: Toggle value off");
				mcl.log(Level.INFO, "[PluginsManager]: Plugin will be disabled");
				pm.disablePlugin(this);
			}
		} else {
			mcl.log(Level.SEVERE, "[PluginsManager]: Please configure the " + config_file_name + " file and reload the plugin");
		}
	}

	public void onDisable() {
		// Custom disable code here

		// NOTE: All registered events are automatically unregistered when a plugin is disabled

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		mcl.log(Level.INFO, "[PluginsManager]: " + pdFile.getName() + " version " + pdFile.getVersion() + " disabled");
	}

	public void setDefaultSettings() {
		prop.setProperty(toggle, toggle_default_value);
		prop.setProperty(output_folder_name, output_folder_name_default_value);
		prop.setProperty(output_file_name, output_file_name_default_value);
		prop.setProperty(column_view, column_view_default_value);
		prop.setProperty(last_updated, last_updated_default_value);
		prop.setProperty(plugin_name_branding, plugin_name_branding_default_value);
		prop.setProperty(server_pretext, server_pretext_default_value);
		prop.setProperty(plugins_pretext, plugins_pretext_default_value);
		prop.setProperty(css_file_name, css_file_name_default_value);
		File config_folder = new File(config_folder_name);
		try {
			if (!config_folder.exists()) {
				config_folder.mkdirs();
			}
			OutputStream os = new FileOutputStream(config_file);
			prop.store(os, config_file_name_comment);
			mcl.log(Level.INFO, "[PluginsManager]: default configuration file successfully created");
		} catch (IOException ioe) {
			mcl.log(Level.SEVERE, "[PluginsManager]: default configuration file could not be written to");
		}
	}

	public boolean loadSettings() {
		try {
			prop.load(new FileInputStream(config_file));
			toggle_value = prop.getProperty(toggle);
			output_folder_name_value = prop.getProperty(output_folder_name);
			output_file_name_value = prop.getProperty(output_file_name);
			column_view_value = prop.getProperty(column_view);
			last_updated_value = prop.getProperty(last_updated);
			plugin_name_branding_value = prop.getProperty(plugin_name_branding);
			server_pretext_value = prop.getProperty(server_pretext);
			plugins_pretext_value = prop.getProperty(plugins_pretext);
			css_file_name_value = prop.getProperty(css_file_name);
			if (toggle_value == null || output_folder_name_value == null || output_file_name_value == null ||
			column_view_value == null || last_updated_value == null || plugin_name_branding_value == null ||
			server_pretext_value == null || plugins_pretext_value == null || css_file_name_value == null) {
				mcl.log(Level.SEVERE, "[PluginsManager]: " + config_file_name + " file is not in the proper format");
				return false;
			} else {
				mcl.log(Level.INFO, "[PluginsManager]: " + config_file_name + " file successfully loaded");
				toggle_value = toggle_value.trim().toLowerCase();
				column_view_value = column_view_value.trim().toLowerCase();
				last_updated_value = last_updated_value.trim().toLowerCase();
				plugin_name_branding_value = plugin_name_branding_value.trim().toLowerCase();
				return true;
			}
		} catch (IOException ioe) {
			mcl.log(Level.SEVERE, "[PluginsManager]: " + config_file_name + " file could not be loaded");
			prop.clear();
			setDefaultSettings();
			return false;
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
