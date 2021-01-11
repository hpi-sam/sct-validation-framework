package de.hpi.mod.sim.worlds.flasher.entities;

import java.util.List;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;
import de.hpi.mod.sim.worlds.flasher.config.FlasherConfiguration;

public abstract class TaskProvider implements Entity {

	private FlashWorld world;

	private FlashTask currentTask;
	private TaskProviderState currentState = TaskProviderState.WAITING_FOR_NEXT_TASK;
	private double countdownTimer = 0;

	public TaskProvider(FlashWorld world) {
		this.world = world;
	}

	public void update(float delta) {

		// Decrement timer
		if (this.countdownTimer > 0) {
			this.countdownTimer -= delta;
			System.out.println(this.countdownTimer);
		}

		// Update depending on current task and timer...

		// Case 1: If currently in pause before task start AND Timer has tun out....
		if (this.currentState == TaskProviderState.PAUSE_BEFORE_TASK && this.countdownTimer <= 0
				&& this.currentTask != null) {

			// ...send task to lightbulb and start timer
			System.out.println("End Pause, Send Task");
			this.currentState = TaskProviderState.TASK_IS_RUNNING;
			this.world.startBulb(this.currentTask.getNumberOfFlashes());
			this.countdownTimer = FlasherConfiguration.getWaitingTimeBeforeTask()
					/ Configuration.getEntitySpeedFactor();
		}

		// Case 2: Waiting for task OR ( if a task if running AND Timer has run out )...
		if (this.currentState == TaskProviderState.WAITING_FOR_NEXT_TASK
				|| (this.currentState == TaskProviderState.TASK_IS_RUNNING && this.countdownTimer <= 0)) {

			// ...then get next task and...
			this.currentTask = getNextTask();

			if (this.currentTask != null) {
				// ...start mandatory pause before task execution if next task is available...
				System.out.println("Get New Task, Start Pause");
				this.currentState = TaskProviderState.PAUSE_BEFORE_TASK;
				this.countdownTimer = FlasherConfiguration.getWaitingTimeBeforeTask()
						/ Configuration.getEntitySpeedFactor();

			} else {
				// ...or move to final state if no new task was found.
				System.out.println("End Queue");
				this.currentState = TaskProviderState.NO_TASK_LEFT;
			}

		}
	}

	protected abstract FlashTask getNextTask();

	@Override
	public abstract boolean hasPassedAllTestCriteria();

	public FlashTask getCurrentTask() {
		return currentTask;
	}

	private enum TaskProviderState {
		WAITING_FOR_NEXT_TASK, PAUSE_BEFORE_TASK, TASK_IS_RUNNING, NO_TASK_LEFT
	};

}