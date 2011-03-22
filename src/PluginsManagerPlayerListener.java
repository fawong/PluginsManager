package com.fawong.PluginsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.entity.Player;

/**
* Handle events for all Player related events
* @author fawong
*/
public class PluginsManagerPlayerListener extends PlayerListener {
	private final PluginsManager plugin;

	public PluginsManagerPlayerListener(PluginsManager instance) {
		plugin = instance;
	}

	// Player related code here
}
