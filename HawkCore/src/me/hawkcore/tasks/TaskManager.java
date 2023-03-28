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
	private int errors;
	
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
				if (!(task.tickRelative < task.getTickRate())) {
					task.tickRelative = 1;
					if (task.getRunnable() == null) continue;
					task.getRunnable().run();
				} else task.tickRelative++;
			} catch (Exception e) {
				e.printStackTrace();
				errors+=1;
			}
		}
		lastTick = (int) (System.currentTimeMillis()-time);
		
	}

}
