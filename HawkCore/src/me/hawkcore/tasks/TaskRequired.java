package me.hawkcore.tasks;

interface TaskRequired {

	Task run(int tickRate);
	
	void cancel();
	
}
