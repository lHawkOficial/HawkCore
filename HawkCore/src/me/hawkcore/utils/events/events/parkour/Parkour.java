package me.hawkcore.utils.events.events.parkour;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;

public class Parkour extends Event implements EventExecutor {

	public Parkour(String name, File folder, FileConfiguration config, EventType type, boolean enabled) {
		super(name, folder, config, type, enabled);
	}

	@Override
	public void setupConfig() {
		
	}

	@Override
	public void updateScore() {
		
	}

	@Override
	public boolean containsPlayerOnEvent(Player p) {
		return getPlayers().containsKey(p);
	}

	@Override
	public void addPlayerToEspectator(Player p) {
		
	}

	@Override
	public void removePlayerFromEspectator(Player p) {
		
	}

	@Override
	public void start() {
		
	}

	@Override
	public void closed() {
		
	}

	@Override
	public void forceStart() {
		
	}

	@Override
	public void warning() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void finish() {
		
	}

}
