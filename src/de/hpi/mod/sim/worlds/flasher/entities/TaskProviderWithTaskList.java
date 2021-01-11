package de.hpi.mod.sim.worlds.flasher.entities;

import java.util.List;

import de.hpi.mod.sim.worlds.flasher.FlashWorld;

public class TaskProviderWithTaskList extends TaskProvider {

	private List<FlashTask> taskList;
	private boolean repeatTaskList;
	private int currentListIndex = 0;

	public TaskProviderWithTaskList(List<FlashTask> list, boolean repeat, FlashWorld world) {
		super(world);
		this.taskList = list;
		this.repeatTaskList = repeat;
	}

	protected FlashTask getNextTask() {

		// Resert list index if repetition is turned on AND if list has overflowed
		if (this.currentListIndex >= this.taskList.size() && this.repeatTaskList)
			this.currentListIndex = 0;

		// Return current list if list index is valid
		if (this.currentListIndex < this.taskList.size())
			return this.taskList.get(this.currentListIndex++);

		return null;

	}

	@Override
	public boolean hasPassedAllTestCriteria() {
		// If the list is set to be repeaded, always accept as passed
		// If the list is not repeated, it nly counts as passed if list is finished
		return this.repeatTaskList || this.currentListIndex >= this.taskList.size();
	}

}