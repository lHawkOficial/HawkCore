package me.hawkcore.utils.events.events.parkour.utils;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import me.hawkcore.utils.events.events.parkour.Parkour;
import me.hawkcore.utils.events.utils.ConfigEvent;
import me.hawkcore.utils.events.utils.Event;

@Getter
public class ConfigParkour extends ConfigEvent {
	
	private Parkour parkour;
	private int timeWarn,
	time;
	private boolean score_active;
	private String score_title,
	mito,
	tag_mito;
	private List<String> score_lines,
	commands;
	private double valueJoin;
	
	public ConfigParkour(Event event) {
		super(event);
		this.parkour = (Parkour) event;
		FileConfiguration config = event.getConfig();
		ConfigurationSection section = config.getConfigurationSection("Config");
		commands = new ArrayList<>(section.getStringList("commands"));
		commands.replaceAll(l -> l.replaceFirst("/", new String()));
		event.setTag(section.getString("tag").replace("&", "§"));
		timeWarn = section.getInt("timeWarn");
		setAutoStart(section.getBoolean("autoStart"));
		setAutoStartTime(section.getInt("autoStartTime"));
		time = section.getInt("time");
		valueJoin = section.getDouble("valueJoin");
		mito = section.getString("mito").replace("&", "§").replace("{tag}", event.getTag());
		tag_mito = section.getString("tag_mito").replace("&", "§").replace("{tag}", event.getTag());
		section = config.getConfigurationSection("ScoreBoard");
		score_active = section.getBoolean("active");
		score_title = section.getString("title").replace("{tag}", event.getTag()).replace("&", "§");
		score_lines = new ArrayList<>(section.getStringList("text"));
		score_lines.replaceAll(l -> l.replace("{tag}", event.getTag()).replace("&", "§"));
	}
	
}
