package me.hawkcore.tasks;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.hawkcore.Core;

@Getter
public class TaskManager extends BukkitRunnable {
	
	private List<Task> tasks = new ArrayList<>();
	private int lastTick;
	
	public static TaskManager get() {
		return Core.getInstance().getTaskmanager();
	}
	
	@Override
	public void run() {
		
		if (tasks.isEmpty()) return;
		long time = System.currentTimeMillis();
		for (int i = 0; i < tasks.size(); i++) {
			try {
				Task task = tasks.get(i);
				if (task.tickRelative < task.getTickRate()) task.tickRelative++;
				else {
					task.tickRelative = 1;
					task.getRunnable().run();
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		lastTick = (int) (System.currentTimeMillis()-time);
		
	}

}
