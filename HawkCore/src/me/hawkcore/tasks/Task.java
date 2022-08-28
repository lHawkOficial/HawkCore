package me.hawkcore.tasks;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;


public class Task implements TaskRequired {
	@Getter
	private Runnable runnable;
	@Getter
	@Setter
	private int tickRate = 1;
	public int tickRelative = 1;
	
	public Task(Runnable runnable) {
		this.runnable = runnable;
		TaskManager.get().getTasks().add(this);
	}
	
	public Task() {
		TaskManager.get().getTasks().add(this);
	}

	@Override
	public Task run(int tickRate) {
		if (runnable == null) return null;
		this.runnable.run();
		this.tickRate = tickRate;
		return this;
	}
	
	@Override
	public void cancel() {
		TaskManager.get().getTasks().remove(this);
	}
	
	public Task setRunnable(Runnable run) {
		this.runnable = run;
		return this;
	}
	
	public static void run(Runnable run) {
		Bukkit.getScheduler().runTask(Core.getInstance(), run);
	}
	
	public static void runAsync(Runnable run) {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), run);
	}
	
}
