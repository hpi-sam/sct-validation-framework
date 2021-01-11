package de.hpi.mod.sim.worlds.flasher.entities;

import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;

public class TaskProviderSpecification implements EntitySpecification<TaskProvider> {

	private List<FlashTask> taskList;
    private boolean repeatTaskList;
    
    private ITaskGenerator taskGenerator;
    
    private FlashWorld world;

    public TaskProviderSpecification(List<FlashTask> list, boolean repeat, FlashWorld world) {
        this.taskList = list;
        this.repeatTaskList = repeat;
        this.world = world;
    }
    
    public TaskProviderSpecification(ITaskGenerator generator, FlashWorld world) {
        this.taskGenerator = generator;
        this.world = world;
    }

    @Override
    public TaskProvider createEntity() {
    	TaskProvider starter;
    	if(taskGenerator == null) {
    		starter = new TaskProviderWithTaskList(this.taskList, this.repeatTaskList, world);

    	}else {
    		starter = new TaskProviderWithGenerator(this.taskGenerator, world);
    	}
        world.setStarter(starter);
        return starter;
    }

}
