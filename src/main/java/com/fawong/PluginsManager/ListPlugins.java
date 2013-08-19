package com.fawong.PluginsManager;

import freemarker.template.Template;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
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
    String printtofile = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
    printtofile += "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n";
    printtofile += "<head>\n";
    printtofile += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n";
    String css = plugin.getConfig().getString("css-file-name");
    if (!css.equals("CHANGE THIS VALUE")) {
      printtofile += "<link rel=\"stylesheet\" href=\"" + css + "\" type=\"text/css\" />\n";
    }
    printtofile += "<title>" + plugin.getServer().getName() + "</title>\n";
    printtofile += "</head>\n";
    String bkg = plugin.getConfig().getString("background-image-file-name");
    if (!bkg.equals("CHANGE THIS VALUE")) {
      printtofile += "<body background=\"" + bkg + "\">\n";
    } else {
      printtofile += "<body>\n";
    }
    printtofile += "<strong>" + plugin.getConfig().getString("server-pretext") + "</strong><br />\n" + plugin.getServer().getVersion() + "\n<br /><br />\n";
    printtofile += "<strong>" + plugin.getConfig().getString("plugins-pretext") + "</strong><br />\n";
    printtofile += listFullPluginNames(plugin.getConfig().getBoolean("column-view"), plugin.getConfig().getBoolean("alphabetize_plugin"));
    printtofile += lastUpdatedDate();
    printtofile += pluginNameBranding();
    printtofile += "</body>\n";
    printtofile += "</html>\n";
    File file_to_output = null;
    try {
      file_to_output = new File(plugin.getConfig().getString("output-folder-name"), plugin.getConfig().getString("output-file-name"));
      FileWriter fw = new FileWriter(file_to_output);
      BufferedWriter out = new BufferedWriter(fw);
      out.write(printtofile);
      out.close();
      plugin.getLogger().log(Level.INFO, plugin.pluginMessageString(plugin.getConfig().getString("output-file-name") + " successfully created"));
    } catch (IOException ioe) {
      plugin.getLogger().log(Level.SEVERE, plugin.pluginMessageString(plugin.getConfig().getString("output-file-name") + " could not be created"));
    }

    Template templatefile = null;
    try {
      templatefile = PluginsManager.cfg.getTemplate("test.z");
    } catch (IOException ioe) {
      plugin.getLogger().log(Level.SEVERE, plugin.pluginMessageString("Could not open template file " + templatefile.getName()));
    }

    HashMap<String, String> root = new HashMap<String, String>();
    root.put("a", "gaewo");
    root.put("b", "g4334");
    root.put("c", "44351");

    FileWriter out = null;
    try {
      out = new FileWriter(file_to_output);
      templatefile.process(root, out);
    } catch (IOException ioe) {
      plugin.getLogger().log(Level.SEVERE, plugin.pluginMessageString("Could not open file " + file_to_output.getAbsolutePath()));
    } catch (freemarker.template.TemplateException te) {
      plugin.getLogger().log(Level.SEVERE, plugin.pluginMessageString("Could not parse template file " + templatefile.getName()));
    }
  }
}
