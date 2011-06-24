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
* PluginsManager Settings Interface for Bukkit Minecraft Server
*
* @author fawong
*/
public interface PluginsManagerSettings {
	final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	String config_folder_name = "plugins/PluginsManager";
	String config_file_name = "pluginsmanager.settings";
	String config_file_name_comment = "Plugins Manager Configuration File";
	String toggle = "toggle";
	String toggle_default_value = "on";
	String output_file_name = "output_file_name";
	String output_file_name_default_value = "CHANGE THIS VALUE";
	String output_folder_name = "output_folder_name";
	String output_folder_name_default_value = "CHANGE THIS VALUE";
	String column_view = "column_view";
	String column_view_default_value = "off";
	String last_updated = "last_updated";
	String last_updated_default_value = "on";
	String plugin_name_branding = "plugin_name_branding";
	String plugin_name_branding_default_value = "off";
	String server_pretext = "server_pretext";
	String server_pretext_default_value = "This server runs:";
	String plugins_pretext = "plugins_pretext";
	String plugins_pretext_default_value = "This server uses the following plugins:";
	String css_file_name = "css_file_name";
	String css_file_name_default_value = "CHANGE THIS VALUE";
	File config_file = new File(config_folder_name, config_file_name);
}
