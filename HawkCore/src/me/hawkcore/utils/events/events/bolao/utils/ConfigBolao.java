package me.hawkcore.utils.events.events.bolao.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import me.hawkcore.utils.events.events.bolao.Bolao;
import me.hawkcore.utils.events.utils.ConfigEvent;
import me.hawkcore.utils.events.utils.Event;

@Getter
public class ConfigBolao extends ConfigEvent {
	
	private Bolao bolao;
	private int timeWarn,
	time;
	private boolean score_active;
	private String score_title,
	mito,
	tag_mito;
	private List<String> score_lines;
	private double valueJoin;
	
	public ConfigBolao(Event event) {
		super(event);
		this.bolao = (Bolao) event;
		FileConfiguration config = event.getConfig();
		ConfigurationSection section = config.getConfigurationSection("Config");
		event.setTag(section.getString("tag").replace("&", "§"));
		timeWarn = section.getInt("timeWarn");
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
