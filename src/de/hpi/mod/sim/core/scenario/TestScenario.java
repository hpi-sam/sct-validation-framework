package de.hpi.mod.sim.core.scenario;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.view.SimulatorFrame;

public abstract class TestScenario extends Scenario {

    private List<Entity> entities = new CopyOnWriteArrayList<>();
    private boolean hasRun = false;
    private boolean alreadyPrinted = false;

    @Override
    public void loadScenario(Setting setting) {
        entities.clear();
        super.loadScenario(setting);
        hasRun = true;
    }

    public boolean isPassed() {
        return hasRun && entities.stream().allMatch(Entity::hasPassedAllTestCriteria);
    }
    
    public void notifySuccessToUser(SimulatorFrame frame) {
    	if(!alreadyPrinted) {
    		frame.displayMessage("Finished test \"" + name + "\" succesfully", SimulatorFrame.MENU_GREEN);
    		alreadyPrinted = true;
    	}
    }
    
	public void resetTest() {
		hasRun = false;
		alreadyPrinted = false;
	}

	public void notifyFailToUser(SimulatorFrame frame, String reason) {
		if(!alreadyPrinted) {
    		frame.displayMessage("Test \"" + name + "\" failed beacuse " + reason, SimulatorFrame.MENU_RED);
    		alreadyPrinted = true;
    	}
	}
}
