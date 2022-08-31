package me.hawkcore.utils;

import org.bukkit.entity.Player;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hawkcore.Core;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class PlaceHolders extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		return Core.getInstance().getDescription().getAuthors().get(0);
	}

	@Override
	public String getIdentifier() {
		return "hawkcore";
	}

	@Override
	public String getVersion() {
		return Core.getInstance().getDescription().getVersion();
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String params) {
		MissionPlayer mp = MissionPlayer.check(p);
		if (params.toLowerCase().startsWith("mission")) {
			switch (params.toLowerCase().split("_")[1]) {
			case "missionname":
				if (mp.getMissionSelected() != null) return mp.getMissionSelected().getName();
				break;
			case "missiondesc":
				if (mp.getMissionSelected() != null) return mp.getMissionSelected().getDescription();
				break;
			case "missionprogress":
				if (mp.getMissionSelected() != null) return mp.getMissionSelected().getObjective().progress();
				break;
			case "categoryname":
				if (mp.getCategorySelected() != null) return mp.getCategorySelected().getName();
				break;
			case "categoryprogress":
				if (mp.getCategorySelected() != null) return mp.getCategorySelected().progress();
				break;
			case "categorymissionscompleted":
				if (mp.getCategorySelected() != null) return String.valueOf(mp.getCategorySelected().getTotalCompleted());
				break;
			case "categorytotalmissions":
				if (mp.getCategorySelected() != null) return String.valueOf(mp.getCategorySelected().getMissions().size());
				break;
			default:
				break;
			}
		}
		return "N.A";
	}

}
