package me.hawkcore.utils.missions.types.utils;

import org.bukkit.Bukkit;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.boosbar.BossBar;
import me.hawkcore.utils.missions.listeners.plugin.MissionCompleteEvent;
import me.hawkcore.utils.missions.objects.ConfigMission;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;

@Getter
public class MissionObjective implements MissionProgressRequired, Listener{
	
	@Setter
	private Mission mission;
	@Setter
	private MissionCategory category;
	@Setter
	private double maxValue, 
	value;
	
	public MissionObjective(Mission mission, double maxValue) {
		this.mission = mission;
		this.category = mission.getCategory();
	}

	@Override
	public String progress() {
		int percent = (int) (getValue() * 100 / getMaxValue());
		return (percent > 100 ? 100 : percent) + "%";
	}
	
	@Override
	public boolean isCompleted() {
		return getValue() >= getMaxValue();
	}
	
	@Override
	public void complete() {
		MissionPlayer mp = mission.getPlayer();
		if (mp==null) return;
		mission.setCompleted(true);
		mp.setCategorySelected(mission.getCategory());
		mp.setMissionSelected(mission);
		MissionCompleteEvent event = new MissionCompleteEvent(mp, mission, this);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		Player p = mp.getPlayer();
		if (p==null) return;
		String msg = PlaceholderAPI.setPlaceholders(p, ConfigMission.get().getMission_complete());
		p.sendMessage(" ");
		p.sendMessage(msg);
		p.sendMessage(" ");
		BossBar.send(p, msg, 10);
		p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 0.5f, 0.5f);
		if (mission.getMsgFinalize().length() > 0) p.sendMessage(PlaceholderAPI.setPlaceholders(p, mission.getMsgFinalize()));
		Task.run(()->{
			mission.getRewards().forEach(cmd -> {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(p, cmd));
			});
		});
		mp.saveAsync();
	}
	
}
