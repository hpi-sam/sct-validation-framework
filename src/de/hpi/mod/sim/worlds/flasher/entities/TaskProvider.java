package de.hpi.mod.sim.worlds.flasher.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

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

	private enum TaskProviderState {
		WAITING_FOR_NEXT_TASK, PAUSE_BEFORE_TASK, TASK_IS_RUNNING, NO_TASK_LEFT
	};
	
	
	
	public void render(Graphics graphics, int panelWidth, int panelHeight) {
		
		// Get position and size of Task display box		
		int taskDisplayTopLeftX = FlasherConfiguration.getTaskDisplaySpacing();
		int taskDisplayTopLeftY = panelHeight - FlasherConfiguration.getTaskDisplayHeight() - FlasherConfiguration.getTaskDisplaySpacing();
		int taskDisplayWidth = panelWidth - (2* FlasherConfiguration.getTaskDisplaySpacing());
		int taskDisplayHeight = FlasherConfiguration.getTaskDisplayHeight();
		
		// Get fonts
		Font taskHeaderFont = new Font("Candara", Font.BOLD, FlasherConfiguration.getTaskDisplayHeight()/5);
		FontMetrics taskHeaderFontMetrics = graphics.getFontMetrics(taskHeaderFont);
		Font taskDescriptionFont = new Font("Arial", Font.PLAIN, FlasherConfiguration.getTaskDisplayHeight()/6);
		FontMetrics taskDescriptionFontMetrics = graphics.getFontMetrics(taskDescriptionFont);

		// Case 1: TASK_IS_RUNNING => Show Task + executed flashes + countup since task start
		if(this.currentState == TaskProviderState.TASK_IS_RUNNING) {
			
			// Draw box with color depending on current status.
			if(world.getCurrentBlinkCounter() < this.getCurrentTask().getNumberOfFlashes()) {
				// // Not byet blinked enough => Yellow box
				graphics.setColor(Color.yellow); 
			} else if(world.getCurrentBlinkCounter() == this.getCurrentTask().getNumberOfFlashes()) {
				// Blinked often enough => Green box
				graphics.setColor(new Color(0xdcf3d0)); 
			} else {
				// Blinked too often => Red box
				graphics.setColor(new Color(0xffe1d0)); 
			}
			graphics.fillRect(taskDisplayTopLeftX, taskDisplayTopLeftY, taskDisplayWidth, taskDisplayHeight);
			
			// Draw Task Header
			String taskHeaderText = "Current Task: Blink " + this.getCurrentTask().getNumberOfFlashes() + " times!";
			int taskHeaderTextX = taskDisplayTopLeftX + (taskDisplayWidth - taskHeaderFontMetrics.stringWidth(taskHeaderText)) / 2;
			int taskHeaderTextY = taskDisplayTopLeftY + taskHeaderFontMetrics.getHeight();
			graphics.setColor(Color.black);
			graphics.setFont(taskHeaderFont);
			graphics.drawString(taskHeaderText, taskHeaderTextX, taskHeaderTextY);

		
		// Case 2: PAUSE_BEFORE_TASK => Alraedy show Task + additional show countdown until task start
		} else if(this.currentState == TaskProviderState.PAUSE_BEFORE_TASK) {

			// Draw box with color depending on task success.
			graphics.setColor(Color.lightGray); // Case 1c: Blinked too often => Red box
			graphics.fillRect(taskDisplayTopLeftX, taskDisplayTopLeftY, taskDisplayWidth, taskDisplayHeight);
			
			// Draw Task Header
			String taskHeaderText = "Upcoming Task: Blink " + this.getCurrentTask().getNumberOfFlashes() + " times!";
			int taskHeaderTextX = taskDisplayTopLeftX + (taskDisplayWidth - taskHeaderFontMetrics.stringWidth(taskHeaderText)) / 2;
			int taskHeaderTextY = taskDisplayTopLeftY + taskHeaderFontMetrics.getHeight();
			graphics.setColor(Color.darkGray);
			graphics.setFont(taskHeaderFont);
			graphics.drawString(taskHeaderText, taskHeaderTextX, taskHeaderTextY);

		
		
		// Case 3: WAITING_FOR_NEXT_TASK / NO_TASK_LEFT:
		} else {
			
			String noTaskText = "No Task!";
			int noTaskTextX = taskDisplayTopLeftX + (taskDisplayWidth - taskHeaderFontMetrics.stringWidth(noTaskText)) / 2;
			int noTaskTextY = taskDisplayTopLeftY + (taskDisplayHeight - taskHeaderFontMetrics.getHeight()) / 2;
			graphics.setColor(Color.black);
			graphics.setFont(taskHeaderFont);
			graphics.drawString(noTaskText, noTaskTextX, noTaskTextY);
			
		}
			      
		
	}

}