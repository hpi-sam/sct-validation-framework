package de.hpi.mod.sim.env.setting;

import java.util.List;

import de.hpi.mod.sim.env.model.IGrid;
import de.hpi.mod.sim.env.setting.infinitestations.Robot;
import de.hpi.mod.sim.env.setting.infinitestations.RobotDispatcher;
import de.hpi.mod.sim.env.simulation.World;
import de.hpi.mod.sim.env.testing.Detector;
import de.hpi.mod.sim.env.testing.scenarios.ScenarioManager;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public abstract class Setting {

    private DriveSimFrame frame;

    private World world;

    public abstract IGrid getGrid();

    public abstract List<Detector> getDetectors();

    public abstract ScenarioManager getScenarioManager();

    protected Setting(DriveSimFrame frame) {
        this.frame = frame;
        world = new World(this);
    }

    public World getWorld() {
        return world;
    }
    
    public DriveSimFrame getFrame() {
        return frame;
    }

    public abstract void updateRobots(float delta);

    public abstract RobotDispatcher getRoboterDispatcher();
    
    public abstract List<Robot> getRobots();  //TODO Think about where to put
}