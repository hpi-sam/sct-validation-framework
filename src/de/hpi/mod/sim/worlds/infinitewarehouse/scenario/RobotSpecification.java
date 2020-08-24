package de.hpi.mod.sim.worlds.infinitewarehouse.scenario;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.WarehouseManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

public abstract class RobotSpecification implements EntitySpecification<WarehouseRobot> {
    
    private WarehouseManager robots;

    public RobotSpecification(WarehouseManager robots) {
        this.robots = robots;
    }

    public abstract WarehouseRobot getRobot(WarehouseManager robots);

    @Override 
    public WarehouseRobot get() {
        return getRobot(robots);
    }
}