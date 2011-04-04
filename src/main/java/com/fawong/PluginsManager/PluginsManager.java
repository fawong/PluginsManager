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
import java.util.Date;
import java.text.SimpleDateFormat;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

/**
* PluginsManager for Bukkit Minecraft Server
*
* @author fawong
*/
public class PluginsManager extends JavaPlugin {
	//private final PluginsManagerPlayerListener playerListener = new PluginsManagerPlayerListener(this);
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	private static String config_folder_name = "plugins/PluginsManager";
	private static String config_file_name = "config.yml";
	private static String config_file_name_comment = "Plugins Manager Configuration File";
	private static String output_toggle = "toggle";
	private static String output_toogle_default_value = "on";
	private static String output_file_name = "output_file_name";
	private static String output_file_name_default_value = "CHANGE THIS VALUE";
	private static String output_folder_name = "output_folder_name";
	private static String output_folder_name_default_value = "CHANGE THIS VALUE";
	private static String column_view = "column_view";
	private static String column_view_default_value = "off";
	private static String last_updated = "last_updated";
	private static String last_updated_default_value = "on";
	private static String plugin_name_branding = "plugin_name_branding";
	private static String plugin_name_branding_default_value = "off";
	private static String server_pretext = "server_pretext";
	private static String server_pretext_default_value = "This server runs:";
	private static String plugins_pretext = "plugins_pretext";
	private static String plugins_pretext_default_value = "This server uses the following plugins:";
	private static String css_file_name = "css_file_name";
	private static String css_file_name_default_value = "CHANGE THIS VALUE";
	private static File config_file = new File(config_folder_name, config_file_name);
	private String output_toggle_value = "";
	private String output_file_name_value = "";
	private String output_folder_name_value = "";
	private String column_view_value = "";
	private String last_updated_value = "";
	private String plugin_name_branding_value = "";
	private String server_pretext_value = "";
	private String plugins_pretext_value = "";
	private String css_file_name_value = "";
	private Logger mcl = Logger.getLogger("Minecraft");
	private PluginManager pm; 
	private PluginDescriptionFile pdFile;
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
		pdFile = getDescription();
		mcl.log(Level.INFO, "[PluginsManager]: " + pdFile.getName() + " version " + pdFile.getVersion() + " enabled");

		// Call method to list plugins to a file.
		if (loadSettings()) {
			mcl.log(Level.INFO, "[PluginsManager]: settings have been loaded");
			if (output_toggle_value.equals("on")) {
				listPluginsToFile();
			} else {
				mcl.log(Level.INFO, "[PluginsManager]: Output toggle value off");
				mcl.log(Level.INFO, "[PluginsManager]: Not outputting to file");
			}
		} else {
			mcl.log(Level.SEVERE, "[PluginsManager]: Please configure the " + config_file_name + " file and reload the plugin");
		}
	}

	public void onDisable() {
		// Custom disable code here

		// NOTE: All registered events are automatically unregistered when a plugin is disabled

		// EXAMPLE: Custom code, here we just output some info so we can check all is well
		pdFile = getDescription();
		mcl.log(Level.INFO, "[PluginsManager]: " + pdFile.getName() + " version " + pdFile.getVersion() + " disabled");
		output_toggle_value = null;
		output_file_name_value = null;
		output_folder_name_value = null;
		column_view_value = null;
		last_updated_value = null;
		plugin_name_branding_value = null;
		server_pretext_value = null;
		plugins_pretext_value = null;
		css_file_name_value = null;
		mcl = null;
		pm = null;
		pdFile = null;
		listofplugins = null;
		nameofplugins = null;
		prop = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (isEnabled()) {
			String commandName = command.getName().toLowerCase();
			if ((commandName.equalsIgnoreCase("listplugins")) || (commandName.equalsIgnoreCase("lp"))) {
				if (args.length != 0) {
					boolean col = args[0].equalsIgnoreCase("column");
					boolean row = args[0].equalsIgnoreCase("row");
					if (col || row) {
						String[] fpn = getFullPluginNames();
						String sendstring = "";
						for (int i = 0; i < fpn.length; i++) {
							if (i < fpn.length - 1) {
								if (col) {
									sendstring = sendstring + fpn[i] + "\n";
								}
								else if (row) {
									sendstring = sendstring + fpn[i] + ", ";
								} else {
									mcl.log(Level.SEVERE, "This should not happen.");
								}
							} else {
								sendstring += fpn[i];
							}
						}
						sender.sendMessage(sendstring);
						return true;
					} else {
						sender.sendMessage("[PluginsManager]: You must specify either \"column\" or \"row\"");
						return true;
					}
				} else {
					sender.sendMessage("[PluginsManager]: You must specify an argument");
					return true;
				}
			}
			else if (sender instanceof Player) {
				Player player = (Player) sender;
				if ((commandName.equalsIgnoreCase("pmgr")) || (commandName.equalsIgnoreCase("pluginsmanager"))) { 
					player.sendMessage("poke");
				}
				return true;
			} else {
				sender.sendMessage("[PluginsManager]: You need to be a player to run this command");
				return true;
			}
		} else {
			return false;
		}
	}

	public void setDefaultSettings() {
		prop.setProperty(output_toggle, "" + output_toogle_default_value);
		prop.setProperty(output_folder_name, "" + output_folder_name_default_value);
		prop.setProperty(output_file_name, "" + output_file_name_default_value);
		prop.setProperty(column_view, "" + column_view_default_value);
		prop.setProperty(last_updated, "" + last_updated_default_value);
		prop.setProperty(plugin_name_branding, "" + plugin_name_branding_default_value);
		prop.setProperty(server_pretext, "" + server_pretext_default_value);
		prop.setProperty(plugins_pretext, "" + plugins_pretext_default_value);
		prop.setProperty(css_file_name, "" + css_file_name_default_value);
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
			output_toggle_value = prop.getProperty(output_toggle);
			output_folder_name_value = prop.getProperty(output_folder_name);
			output_file_name_value = prop.getProperty(output_file_name);
			column_view_value = prop.getProperty(column_view);
			last_updated_value = prop.getProperty(last_updated);
			plugin_name_branding_value = prop.getProperty(plugin_name_branding);
			server_pretext_value = prop.getProperty(server_pretext);
			plugins_pretext_value = prop.getProperty(plugins_pretext);
			css_file_name_value = prop.getProperty(css_file_name);
			if (output_toggle_value == null || output_folder_name_value == null || output_file_name_value == null ||
			column_view_value == null || last_updated_value == null || plugin_name_branding_value == null ||
			server_pretext_value == null || plugins_pretext_value == null || css_file_name_value == null) {
				mcl.log(Level.SEVERE, "[PluginsManager]: " + config_file_name + " file is not in the proper format");
				return false;
			} else {
				mcl.log(Level.INFO, "[PluginsManager]: " + config_file_name + " file successfully loaded");
				output_toggle_value = output_toggle_value.trim().toLowerCase();
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

	private String[] getFullPluginNames() {
		listofplugins = pm.getPlugins();
		String[] returnstring = new String[listofplugins.length];
		for (int i = 0; i < listofplugins.length; i++) {
			pdFile = listofplugins[i].getDescription();
			returnstring[i] = pdFile.getFullName();
		}
		return returnstring;
	}

	private String listFullPluginNames(String cvv) {
		String returnstring = "";
		nameofplugins = getFullPluginNames();
		for (int j = 0; j < nameofplugins.length; j++) {
			if (j < nameofplugins.length - 1) {
				if (cvv.equalsIgnoreCase("off")) {
					returnstring += nameofplugins[j] + ", ";
				} else {
					returnstring += nameofplugins[j] + "<br />\n";
				}
			} else {
				returnstring += nameofplugins[j] + "<br /><br /><br /><br />\n";
			}
		}
		return returnstring;
	}

	private String lastUpdatedDate() {
		String returnstring = "";
		if (last_updated_value.equalsIgnoreCase("on")) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd @ HH:mm:ss");		
			returnstring = "This page was generated on: " + sdf.format(date) + "\n";
		}
		return returnstring;
	}

	private String pluginNameBranding() {
		String returnstring = "";
		System.out.println(plugin_name_branding_value);
		if (plugin_name_branding_value.equalsIgnoreCase("on")) {
			pdFile = getDescription();
			returnstring += "Using " + pdFile.getFullName() + "<br />\n";
		}
		return returnstring;
	}

	private void listPluginsToFile() {
		String printtofile = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
		printtofile += "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n";
		printtofile += "<head>\n";
		printtofile += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n";
		if (css_file_name_value.length() != 0) {
			printtofile += "<link rel=\"stylesheet\" href=\"" + css_file_name_value + "\" type=\"text/css\" />\n";
		}
		printtofile += "<title>" + getServer().getName() + "</title>\n";
		printtofile += "</head>\n";
		printtofile += "<body>\n";
		printtofile += "<strong>" + server_pretext_value + "</strong><br />\n" + getServer().getVersion() + "\n<br /><br />\n";
		printtofile += "<strong>" + plugins_pretext_value + "</strong><br />\n";
		printtofile += listFullPluginNames(column_view_value);
		printtofile += lastUpdatedDate();
		printtofile += pluginNameBranding();
		printtofile += "</body>\n";
		printtofile += "</html>\n";
		try {
			File file_to_output = new File(output_folder_name_value, output_file_name_value);
			FileWriter fw = new FileWriter(file_to_output);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(printtofile);
			out.close();
			mcl.log(Level.INFO, "[PluginsManager]: " + output_file_name_value + " successfully created");
		} catch (IOException ioe) {
			mcl.log(Level.SEVERE, "[PluginsManager]: " + output_file_name_value + " could not be created");
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
