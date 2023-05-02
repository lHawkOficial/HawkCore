package me.hawkcore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.configs.ConfigCommands;
import me.hawkcore.utils.configs.ConfigShowMessages;

@Getter
public class ConfigGeral {
	
	private ConfigCommands configcommands;
	private ConfigShowMessages configshowmessages;
	private Boolean thirst,
	heat,
	actionBarActive,
	enable_events,
	active_fragments,
	only_survival_fragments;
	private List<String> regionsDisables,
	events_command,
	warn_text,
	warn_start,
	fragments,
	blocks_fragments;
	private String actionBar,
	channel_events,
	warn_title,
	message_fragments;
	private int time_warn_events;
	
	public ConfigGeral() {
		configcommands = new ConfigCommands();
		configshowmessages = new ConfigShowMessages();
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Config");
		only_survival_fragments = section.getBoolean("only_survival_fragments");
		blocks_fragments = new ArrayList<>(section.getStringList("blocks_fragments"));
		fragments = new ArrayList<>(section.getStringList("fragments"));
		message_fragments = section.getString("message_fragments").replace("{tag}", Core.getInstance().getTag()).replace("&", "§");
		active_fragments = section.getBoolean("active_fragments");
		warn_title = ChatColor.stripColor(section.getString("warn_title").replace("{tag}", Core.getInstance().getTag()));
		warn_text = new ArrayList<>(section.getStringList("warn_text"));
		warn_text.replaceAll(l -> ChatColor.stripColor(l.replace("{tag}", Core.getInstance().getTag()))+"\n");
		warn_start = new ArrayList<>(section.getStringList("warn_start"));
		warn_start.replaceAll(l -> ChatColor.stripColor(l.replace("{tag}", Core.getInstance().getTag()))+"\n");
		time_warn_events = section.getInt("time_warn_events");
		channel_events = section.getString("channel_events");
		events_command = new ArrayList<>(section.getStringList("events_command"));
		events_command.replaceAll(l -> l.replace("{tag}", Core.getInstance().getTag()).replace("&", "§"));
		enable_events = section.getBoolean("enable_events");
		thirst = section.getBoolean("thirst");
		heat = section.getBoolean("heat");
		actionBarActive = section.getBoolean("actionBarActive");
		actionBar = section.getString("actionBar").replace("&", "§");
		regionsDisables = new ArrayList<>(section.getStringList("regionsDisables"));
	}
	
	public static ConfigGeral get() {
		return Core.getInstance().getConfiggeral();
	}
	
}
