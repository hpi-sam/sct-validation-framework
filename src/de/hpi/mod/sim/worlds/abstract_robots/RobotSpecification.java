package de.hpi.mod.sim.worlds.abstract_robots;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public abstract class RobotSpecification <R extends Robot, M extends RobotGridManager> implements EntitySpecification<R> {
    
    private M robotManager;

    public RobotSpecification(M manager) {
        this.robotManager = manager;
    }

    protected abstract R createRobot(M robots);

    @Override 
    public R createEntity() {
        R robot = createRobot(robotManager);
        robotManager.addRobot(robot);
        return robot;
    }
}