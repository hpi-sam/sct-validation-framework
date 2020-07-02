package de.hpi.mod.sim.setting.robot;

import java.util.List;
import java.util.stream.Collectors;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehousesSetting;

public abstract class RobotDetector extends Detector {

    protected InfiniteWarehousesSetting setting;

    protected RobotDetector(InfiniteWarehousesSetting setting) {
        super(setting);
        this.setting = setting;
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