package de.hpi.mod.sim.worlds.flasher.entities;

import java.awt.FontMetrics;
import java.awt.Graphics;
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
		}

		// Update depending on current task and timer...

		// Case 1: If currently in pause before task start AND Timer has tun out....
		if (this.currentState == TaskProviderState.PAUSE_BEFORE_TASK && this.countdownTimer <= 0
				&& this.currentTask != null) {

			// ...send task to lightbulb and start timer
			this.currentState = TaskProviderState.TASK_IS_RUNNING;
			this.world.startBulb(this.currentTask.getNumberOfFlashes());
			this.countdownTimer = this.currentTask.getTaskTime() / Configuration.getEntitySpeedFactor();
		}

		// Case 2: Waiting for task OR ( Task is running AND Timer has run out)...
		if (this.currentState == TaskProviderState.WAITING_FOR_NEXT_TASK
				|| (this.currentState == TaskProviderState.TASK_IS_RUNNING && this.countdownTimer <= 0)) {

			// ...then get next task and...
			this.currentTask = getNextTask();

			if (this.currentTask != null) {
				// ...start mandatory pause before task execution if next task is available...
				this.currentState = TaskProviderState.PAUSE_BEFORE_TASK;
				this.countdownTimer = currentTask.getPreTaskWaitingTime() / Configuration.getEntitySpeedFactor();

			} else {
				// ...or move to final state if no new task was found.
				this.currentState = TaskProviderState.NO_TASK_LEFT;
			}

		}
	}

	// This method must be specified by the concrete implementations.
	protected abstract FlashTask getNextTask();

	public FlashTask getCurrentTask() {
		return currentTask;
	}

	protected int getCurrentTaskNumberOfBlinks() {
		if(this.getCurrentTask() == null)
			return 0;
		return this.getCurrentTask().getNumberOfFlashes();
	}
	
	private enum TaskProviderState {
		WAITING_FOR_NEXT_TASK, PAUSE_BEFORE_TASK, TASK_IS_RUNNING, NO_TASK_LEFT
	};
	
	
	
	public void render(Graphics graphics, int panelWidth, int panelHeight) {
		
		// Get position and size of the task display box to be rendered		
		int taskDisplayX = FlasherConfiguration.getTaskDisplaySpacing();
		int taskDisplayY = panelHeight - FlasherConfiguration.getTaskDisplayHeight() - FlasherConfiguration.getTaskDisplaySpacing();
		int taskDisplayWidth = panelWidth - (2* FlasherConfiguration.getTaskDisplaySpacing());
		int taskDisplayHeight = FlasherConfiguration.getTaskDisplayHeight();
				
		// Case 1: TASK_IS_RUNNING => Show Task + executed flashes + countup since task start
		if(this.currentState == TaskProviderState.TASK_IS_RUNNING) {
			
			// Draw box with background color depending on current status.			
			if(world.getCurrentBlinkCounter() < this.getCurrentTaskNumberOfBlinks()) {
				graphics.setColor(FlasherConfiguration.getTaskDisplayOngoingBackgroundColor());
			} else if(world.getCurrentBlinkCounter() == this.getCurrentTaskNumberOfBlinks()) {
				graphics.setColor(FlasherConfiguration.getTaskDisplaySuccessBackgroundColor());
			} else { 
				graphics.setColor(FlasherConfiguration.getTaskDisplayFailureBackgroundColor());
			}
			graphics.fillRect(taskDisplayX, taskDisplayY, taskDisplayWidth, taskDisplayHeight);

			// Get fontmetrics to calulate spacings
		    FontMetrics taskHeaderFontMetrics = graphics.getFontMetrics(FlasherConfiguration.getTaskDisplayHeaderFont());
		    FontMetrics taskDescriptionFontMetrics = graphics.getFontMetrics(FlasherConfiguration.getTaskDisplayDescripionFont());
		    
		    // Set color for all text rendering
			graphics.setColor(FlasherConfiguration.getTaskDisplayTaskFontColor());
			
			// Draw Task Header
			String taskHeaderText = "Current Task: Blink " + this.getCurrentTask().getNumberOfFlashes() + " times!";
			int taskHeaderTextX = taskDisplayX + (taskDisplayWidth - taskHeaderFontMetrics.stringWidth(taskHeaderText)) / 2;
			int taskHeaderTextY = taskDisplayY + taskHeaderFontMetrics.getHeight();
			graphics.setFont(FlasherConfiguration.getTaskDisplayHeaderFont());
			graphics.drawString(taskHeaderText, taskHeaderTextX, taskHeaderTextY);

			// Draw Task Description, first row
			double timer = (this.currentTask.getTaskTime() - this.countdownTimer)/1000;
			String taskDescriptionText1 = "Task running since " + FlasherConfiguration.getTaskDisplayTimerFormat().format(timer) + "s.";
			int taskDescritptionText1X = taskDisplayX + (taskDisplayWidth - taskDescriptionFontMetrics.stringWidth(taskDescriptionText1)) / 2;
			int taskDescritptionText1Y = taskHeaderTextY + taskDescriptionFontMetrics.getHeight();
			graphics.setFont(FlasherConfiguration.getTaskDisplayDescripionFont());
			graphics.drawString(taskDescriptionText1, taskDescritptionText1X, taskDescritptionText1Y);

			// Draw Task Description, second row
			String taskDescriptionText2 = "Blinked " + world.getCurrentBlinkCounter() + " times.";
			int taskDescritptionText2X = taskDisplayX + (taskDisplayWidth - taskDescriptionFontMetrics.stringWidth(taskDescriptionText2)) / 2;
			int taskDescritptionText2Y = taskDescritptionText1Y + taskDescriptionFontMetrics.getHeight();
			graphics.setFont(FlasherConfiguration.getTaskDisplayDescripionFont());
			graphics.drawString(taskDescriptionText2, taskDescritptionText2X, taskDescritptionText2Y);

			// Draw Task Description, third row (if number of blinks is correct or too high!)
			if(world.getCurrentBlinkCounter() >= this.getCurrentTaskNumberOfBlinks()) {
			    FontMetrics taskDescriptionHighlightFontMetrics = graphics.getFontMetrics(FlasherConfiguration.getTaskDisplayDescripionHighlightFont());
				String taskDescriptionText3 = (world.getCurrentBlinkCounter() == this.getCurrentTaskNumberOfBlinks()) ? "Done!" : "Too often!";
				int taskDescritptionText3X = taskDisplayX + (taskDisplayWidth - taskDescriptionHighlightFontMetrics.stringWidth(taskDescriptionText3)) / 2;
				int taskDescritptionText3Y = taskDescritptionText2Y + taskDescriptionFontMetrics.getHeight();
				graphics.setFont(FlasherConfiguration.getTaskDisplayDescripionHighlightFont());
				graphics.drawString(taskDescriptionText3, taskDescritptionText3X, taskDescritptionText3Y);
			}

			
			
////			.";
//			if(world.getCurrentBlinkCounter() == this.getCurrentTask().getNumberOfFlashes()) taskDescriptionText += "Done!";
//			if(world.getCurrentBlinkCounter() > this.getCurrentTask().getNumberOfFlashes()) taskDescriptionText += "Too often!";
		// Case 2: PAUSE_BEFORE_TASK => Alraedy show Task + additional show countdown until task start
		} else if(this.currentState == TaskProviderState.PAUSE_BEFORE_TASK) {

			// Draw box.
			graphics.setColor(FlasherConfiguration.getTaskDisplayWaitingBackgroundColor()); 
			graphics.fillRect(taskDisplayX, taskDisplayY, taskDisplayWidth, taskDisplayHeight);

			// Get fontmetrics to calulate spacings
		    FontMetrics taskHeaderFontMetrics = graphics.getFontMetrics(FlasherConfiguration.getTaskDisplayHeaderFont());
		    FontMetrics taskDescriptionFontMetrics = graphics.getFontMetrics(FlasherConfiguration.getTaskDisplayDescripionFont());
		    
			// Draw Task Header
			String taskHeaderText = "Upcoming Task: Blink " + this.getCurrentTask().getNumberOfFlashes() + " times!";
			int taskHeaderTextX = taskDisplayX + (taskDisplayWidth - taskHeaderFontMetrics.stringWidth(taskHeaderText)) / 2;
			int taskHeaderTextY = taskDisplayY + taskHeaderFontMetrics.getHeight();
			graphics.setColor(FlasherConfiguration.getTaskDisplayWaitingFontColor());
			graphics.setFont(FlasherConfiguration.getTaskDisplayHeaderFont());
			graphics.drawString(taskHeaderText, taskHeaderTextX, taskHeaderTextY);

			// Draw Task Description
			double timer = this.countdownTimer/1000;
			String taskDescriptionText = "Task starts in " + FlasherConfiguration.getTaskDisplayTimerFormat().format(timer) + "s.";
			int taskDescritptionTextX = taskDisplayX + (taskDisplayWidth - taskDescriptionFontMetrics.stringWidth(taskDescriptionText)) / 2;
			int taskDescritptionTextY = taskHeaderTextY + taskDescriptionFontMetrics.getHeight();
			graphics.setColor(FlasherConfiguration.getTaskDisplayWaitingFontColor());
			graphics.setFont(FlasherConfiguration.getTaskDisplayDescripionFont());
			graphics.drawString(taskDescriptionText, taskDescritptionTextX, taskDescritptionTextY);

		
		
		// Case 3: WAITING_FOR_NEXT_TASK / NO_TASK_LEFT:
		} else {			
			String noTaskText = "No Task!";
		    FontMetrics noTaskTextFontMetrics = graphics.getFontMetrics(FlasherConfiguration.getTaskDisplayNoTaskFont());
			int noTaskTextX = taskDisplayX + (taskDisplayWidth - noTaskTextFontMetrics.stringWidth(noTaskText)) / 2;
			int noTaskTextY = taskDisplayY + (taskDisplayHeight - noTaskTextFontMetrics.getHeight()) / 2;
			graphics.setColor(FlasherConfiguration.getTaskDisplayNoTaskFontColor());
			graphics.setFont(FlasherConfiguration.getTaskDisplayNoTaskFont());
			graphics.drawString(noTaskText, noTaskTextX, noTaskTextY);
		}
			      
		
	}

}