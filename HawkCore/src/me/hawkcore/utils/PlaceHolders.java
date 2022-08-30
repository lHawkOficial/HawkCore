package me.hawkcore.utils;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hawkcore.Core;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.MissionBreakBlock;
import me.hawkcore.utils.missions.types.MissionBreakItem;
import me.hawkcore.utils.missions.types.MissionChatEvent;
import me.hawkcore.utils.missions.types.MissionCraftItem;
import me.hawkcore.utils.missions.types.MissionDropItem;
import me.hawkcore.utils.missions.types.MissionEatEvent;
import me.hawkcore.utils.missions.types.MissionEnchantItem;
import me.hawkcore.utils.missions.types.MissionKillEntity;
import me.hawkcore.utils.missions.types.MissionMoveDistance;
import me.hawkcore.utils.missions.types.MissionPickItem;
import me.hawkcore.utils.missions.types.MissionPlaceBlock;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

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
				if (mp.getMissionSelected() != null) {
					MissionObjective objective = mp.getMissionSelected().getObjective();
					if (objective instanceof MissionBreakBlock) return ((MissionBreakBlock)objective).progress();
					if (objective instanceof MissionDropItem) return ((MissionDropItem)objective).progress();
					if (objective instanceof MissionMoveDistance) return ((MissionMoveDistance)objective).progress();
					if (objective instanceof MissionPickItem) return ((MissionPickItem)objective).progress();
					if (objective instanceof MissionPlaceBlock) return ((MissionPlaceBlock)objective).progress();
					if (objective instanceof MissionKillEntity) return ((MissionKillEntity)objective).progress();
					if (objective instanceof MissionEnchantItem) return ((MissionEnchantItem)objective).progress();
					if (objective instanceof MissionCraftItem) return ((MissionCraftItem)objective).progress();
					if (objective instanceof MissionEatEvent) return ((MissionEatEvent)objective).progress();
					if (objective instanceof MissionChatEvent) return ((MissionChatEvent)objective).progress();
					if (objective instanceof MissionBreakItem) return ((MissionBreakItem)objective).progress();
				}
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
