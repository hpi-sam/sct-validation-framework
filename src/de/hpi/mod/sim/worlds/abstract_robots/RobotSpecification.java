package de.hpi.mod.sim.worlds.abstract_robots;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public abstract class RobotSpecification <R extends Robot, M extends RobotGridManager> implements EntitySpecification<R> {
    
    private M robots;

    public RobotSpecification(M robots) {
        this.robots = robots;
    }

    public abstract R createRobot(M robots);

    @Override 
    public R createEntity() {
        R robot = createRobot(robots);
        robots.addRobot(robot);
        return robot;
    }
}