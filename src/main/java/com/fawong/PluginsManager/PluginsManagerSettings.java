package com.fawong.PluginsManager;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

/**
* PluginsManager Settings Interface
*
* @author fawong
*/
public interface PluginsManagerSettings {
	String config_file_name_comment = "Plugins Manager Configuration File";
	String config_folder_name = "plugins/PluginsManager";
	String config_file_name = "pluginsmanager.settings";
	String toggle_name = "toggle";
	String toggle_name_default_value = "on";
	String output_file_name = "output_file_name";
	String output_file_name_default_value = "CHANGE THIS VALUE";
	String output_folder_name = "output_folder_name";
	String output_folder_name_default_value = "CHANGE THIS VALUE";
	String column_view_name = "column_view";
	String column_view_name_default_value = "on";
	String last_updated_name = "last_updated";
	String last_updated_name_default_value = "on";
	String plugin_name_branding_name = "plugin_name_branding";
	String plugin_name_branding_name_default_value = "off";
	String server_pretext_name = "server_pretext";
	String server_pretext_name_default_value = "This server runs:";
	String plugins_pretext_name = "plugins_pretext";
	String plugins_pretext_name_default_value = "This server uses the following plugins:";
	String css_file_name = "css_file_name";
	String css_file_name_default_value = "CHANGE THIS VALUE";
	String alphabetize_plugin_name = "alphabetize_plugin";
	String alphabetize_plugin_name_default_value = "on";
        String background_image_file_name = "background_image_file_name";
        String background_image_file_name_default_value = "CHANGE THIS VALUE";
	HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	Logger mcl = Logger.getLogger("Minecraft");
	File config_file = new File(config_folder_name, config_file_name);
	Properties prop = new Properties();
}
