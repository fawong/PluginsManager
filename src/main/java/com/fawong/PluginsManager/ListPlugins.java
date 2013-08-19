package com.fawong.PluginsManager;

import freemarker.template.Template;
import java.util.logging.Level;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;

/**
 * Handle events for listing plugins to a file
 * @author fawong
 */
public class ListPlugins {
  private final PluginsManager plugin;
  private Plugin[] listofplugins;
  private String[] nameofplugins;

  public ListPlugins(PluginsManager instance) {
    plugin = instance;
  }

  protected String[] getFullPluginNames() {
    listofplugins = plugin.getServer().getPluginManager().getPlugins();
    String[] returnstring = new String[listofplugins.length];
    PluginDescriptionFile tempPDFile;
    for (int i = 0; i < listofplugins.length; i++) {
      tempPDFile = listofplugins[i].getDescription();
      returnstring[i] = tempPDFile.getFullName();
    }
    return returnstring;
  }

  private String listFullPluginNames(Boolean cvnv, Boolean apnv) {
    String returnstring = "";
    nameofplugins = getFullPluginNames();
    if (apnv == true) {
      Arrays.sort(nameofplugins, String.CASE_INSENSITIVE_ORDER);
    }
    for (int j = 0; j < nameofplugins.length; j++) {
      if (j < nameofplugins.length - 1) {
        if (cvnv == false) {
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

  private String lastUpdatedDate() {
    String returnstring = "";
    if (plugin.getConfig().getBoolean("last-updated") == true) {
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd @ HH:mm:ss");   
      returnstring = "<br /><br /><br /><br />This page was generated on: " + sdf.format(date) + "\n";
    }
    return returnstring;
  }

  private String pluginNameBranding() {
    String returnstring = "";
    if (plugin.getConfig().getBoolean("plugin-name-branding") == true) {
      returnstring += "<br /><br /><br/>Using " + plugin.getDescription().getFullName() + "<br />\n";
    }
    return returnstring;
  }

  protected void listPluginsToFile() {
    Template template_file = null;
    String template_file_name = plugin.getConfig().getString("template-file-name");
    if (template_file_name.equals("default")) {
      template_file_name = "html.template";
    }

    try {
      template_file = PluginsManager.cfg.getTemplate(template_file_name);

      HashMap<String, String> root = new HashMap<String, String>();
      root.put("backgroundimagefilename", "a2w349g89jw349v8423yw");
      root.put("servername", "3232423");
      root.put("cssfilename", "WEIGHWIOEGJWE");
      root.put("backgroundimagefilename", "zzzzzzz");
      root.put("serverpretext", "aaaaaaa");
      root.put("serverversion", "5555555");
      root.put("pluginspretext", "nnnnnnn");
      root.put("plugins", "rrtrrtr");
      root.put("lastupdated", "4676758");
      root.put("pluginnamebranding", "lkagjle");

      String output_file_location = plugin.getConfig().getString("output-file-location");
      if (output_file_location.equals("default")) {
        output_file_location = plugin.getDataFolder().getAbsolutePath();
      }
      String output_file_name = plugin.getConfig().getString("output-file-name");
      if (output_file_name.equals("default")) {
        output_file_name = "index.html";
      }
      File file_to_output = new File(output_file_location, output_file_name);
      FileWriter out = null;
      try {
        out = new FileWriter(file_to_output);
        template_file.process(root, out);
      } catch (IOException ioe) {
        plugin.getLogger().log(Level.SEVERE, plugin.pluginMessageString("Could not open file " + file_to_output.getAbsolutePath() + ": " + ioe));
      } catch (freemarker.template.TemplateException te) {
        plugin.getLogger().log(Level.SEVERE, plugin.pluginMessageString("Could not parse template file " + template_file.getName() + ": " + te));
      }
    } catch (IOException ioe) {
      plugin.getLogger().log(Level.SEVERE, plugin.pluginMessageString("Could not open template file " + template_file_name + ": " + ioe));
    }
  }
}
