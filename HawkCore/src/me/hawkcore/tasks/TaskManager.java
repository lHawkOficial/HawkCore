package me.hawkcore.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.hawkcore.Core;

@Getter
public class TaskManager extends BukkitRunnable {
	
	private List<Task> tasks = new ArrayList<>();
	
	public static TaskManager get() {
		return Core.getInstance().getTaskmanager();
	}
	
	@Override
	public void run() {
		
		for(Task task : tasks) {
			try {
				if (task.tickRelative < task.getTickRate()) task.tickRelative++;
				else {
					task.tickRelative = 1;
					task.getRunnable().run();
				}
			} catch (Exception e) {
				continue;
			}
		}
		
	}

}
