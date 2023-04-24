package me.hawkcore.utils.events.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.hawkcore.Core;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.utils.Event;

public interface EventExecutor {
	
	default void addPlayerToEvent(Player p, Event event) {
		if (p.hasMetadata("event")) p.removeMetadata("event", Core.getInstance());
		if (p.hasMetadata("scoreevent")) p.removeMetadata("scoreevent", Core.getInstance());
		p.setMetadata("event", new FixedMetadataValue(Core.getInstance(), event));
		p.setMetadata("scoreevent", new FixedMetadataValue(Core.getInstance(), new Scoreboard(p, event.getTag())));
	}
	default void removePlayerFromEvent(Player p) {
		if (p.hasMetadata("event")) p.removeMetadata("event", Core.getInstance());
		if (p.hasMetadata("scoreevent")) {
			((Scoreboard)p.getMetadata("scoreevent").get(0).value()).destroy();
			p.removeMetadata("scoreevent", Core.getInstance());
		}
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
