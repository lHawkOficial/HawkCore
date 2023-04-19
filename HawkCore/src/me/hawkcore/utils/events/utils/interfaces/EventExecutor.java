package me.hawkcore.utils.events.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.hawkcore.Core;
import me.hawkcore.utils.events.utils.Event;

public interface EventExecutor {
	
	default void addPlayerToEvent(Player p, Event event) {
		if (p.hasMetadata("event")) p.removeMetadata("event", Core.getInstance());
		p.setMetadata("event", new FixedMetadataValue(Core.getInstance(), event));
	}
	default void removePlayerFromEvent(Player p) {
		if (p.hasMetadata("event")) p.removeMetadata("event", Core.getInstance());
	}
	void setupConfig();
	void updateScore();
	boolean containsPlayerOnEvent(Player p);
	void addPlayerToEspectator(Player p);
	void removePlayerFromEspectator(Player p);
	void start();
	void closed();
	void forceStart();
	void warning();
	void stop();
	void finish();
	
}
