package me.hawkcore.utils;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hawkcore.Core;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.playersdata.objects.PlayerData;
import me.hawkcore.utils.strings.StringAPI;

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
		PlayerData pd = PlayerData.check(p);
		switch (params.toLowerCase().split("_")[0]) {
		case "missions":
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
			case "missionvalue":
				if (mp.getMissionSelected() != null) return String.valueOf((int)mp.getMissionSelected().getObjective().getValue());
				break;
			case "missionmaxvalue":
				if (mp.getMissionSelected() != null) return String.valueOf((int)mp.getMissionSelected().getObjective().getMaxValue());
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
			break;
		case "thirst":
			switch (params.toLowerCase().split("_")[1]) {
			case "percent":
				return Double.valueOf((pd.getThirst().getValue() * 100 / pd.getThirst().getMaxValue())).intValue() + "%";
			case "bar":
				String[] args = pd.getThirst().getBar().split(",");
				String colorDefault = args[0];
				String colorBackground = args[1];
				String icons = args[2];
				String start = args[3];
				String finalS = args[4];
				return PlaceholderAPI.setPlaceholders(p, start+StringAPI.createBar(icons, colorDefault, colorBackground, pd.getThirst().getValue(), pd.getThirst().getMaxValue())+finalS).replace("&", "§");
			default:
				break;
			}
			break;
		case "heat":
			switch (params.toLowerCase().split("_")[1]) {
			case "percent":
				return Double.valueOf(pd.getHeat().getValue()).intValue() + "º";
			case "bar":
				String[] args = pd.getHeat().getBar().split(",");
				String colorDefault = args[0];
				String colorBackground = args[1];
				String icons = args[2];
				String start = args[3];
				String finalS = args[4];
				return PlaceholderAPI.setPlaceholders(p, start+StringAPI.createBar(icons, colorDefault, colorBackground, pd.getHeat().getValue(), pd.getHeat().getMaxValue())+finalS).replace("&", "§");
			default:
				break;
			}
			break;
		default:
			break;
		}
		return "N.A";
	}

}
