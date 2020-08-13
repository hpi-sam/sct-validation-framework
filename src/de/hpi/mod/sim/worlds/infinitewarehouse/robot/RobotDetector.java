package de.hpi.mod.sim.worlds.infinitewarehouse.robot;

import java.util.List;
import java.util.stream.Collectors;

import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;

public abstract class RobotDetector extends Detector {

    protected InfiniteWarehouse world;

    protected RobotDetector(InfiniteWarehouse world) {
        super(world);
        this.world = world;
    }

    public abstract void robotUpdate(List<Robot> robots);

    @Override
    public void update(List<? extends Entity> entities) {
        List<Robot> list = entities.stream()
                .filter(e -> (e instanceof Robot))
                .map(e -> (Robot) e)
                .collect(Collectors.toList());
        robotUpdate(list);
    }
    
}