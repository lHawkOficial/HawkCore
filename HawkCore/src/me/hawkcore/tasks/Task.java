package me.hawkcore.tasks;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.hawkcore.Core;


public class Task implements TaskRequired {
	@Getter
	private Runnable runnable;
	@Getter
	private int tickRate = 1;
	public int tickRelative = 1;
	
	public Task(Runnable runnable) {
		this.runnable = runnable;
		TaskManager.get().getTasks().add(this);
	}

	@Override
	public Task run(int tickRate) {
		this.runnable.run();
		this.tickRate = tickRate;
		return this;
	}
	
	@Override
	public void cancel() {
		TaskManager.get().getTasks().remove(this);
	}
	
	public static void run(Runnable run) {
		Bukkit.getScheduler().runTask(Core.getInstance(), run);
	}
	
}
