package com.fawong.PluginsManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

/**
* Handle events for all in-game commands
* @author fawong
*/
public class ListPluginsCommand extends PluginsManager implements CommandExecutor, PluginsManagerSettings{
	private final PluginsManager plugin;

	public ListPluginsCommand(PluginsManager instance) {
		plugin = instance;
	}

	// Command related code here
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
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

	protected String[] getFullPluginNames() {
		listofplugins = pm.getPlugins();
		String[] returnstring = new String[listofplugins.length];
		for (int i = 0; i < listofplugins.length; i++) {
			pdFile = listofplugins[i].getDescription();
			returnstring[i] = pdFile.getFullName();
		}
		return returnstring;
	}

	protected String listFullPluginNames(String cvv) {
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
				returnstring += nameofplugins[j] + "\n";
			}
		}
		return returnstring;
	}

	protected String lastUpdatedDate() {
		String returnstring = "";
		if (last_updated_value.equalsIgnoreCase("on")) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd @ HH:mm:ss");		
			returnstring = "<br /><br /><br /><br />This page was generated on: " + sdf.format(date) + "\n";
		}
		return returnstring;
	}

	protected String pluginNameBranding() {
		String returnstring = "";
		if (plugin_name_branding_value.equalsIgnoreCase("on")) {
			pdFile = getDescription();
			returnstring += "<br /><br /><br/>Using " + pdFile.getFullName() + "<br />\n";
		}
		return returnstring;
	}

	protected void listPluginsToFile() {
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
}
