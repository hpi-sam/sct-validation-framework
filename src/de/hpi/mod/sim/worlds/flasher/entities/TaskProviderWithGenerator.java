package de.hpi.mod.sim.worlds.flasher.entities;

import de.hpi.mod.sim.worlds.flasher.FlashWorld;

public class TaskProviderWithGenerator extends TaskProvider{

	private ITaskGenerator taskGenerator;

	public TaskProviderWithGenerator(ITaskGenerator generator, FlashWorld world) {
		super(world);
		this.taskGenerator = generator;
	}

	protected FlashTask getNextTask() {
		
		// Return next generator event
		return this.taskGenerator.next();
			
	}

	@Override
	public boolean hasPassedAllTestCriteria() {
		return true;
	}
}
