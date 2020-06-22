package de.hpi.mod.sim.core.testing;

import java.util.List;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.setting.robot.Robot;

public abstract class Detector {

    protected Setting setting;

    protected Detector(Setting setting) {
        this.setting = setting;
    }

    public void report(String reason, Robot involved1, Robot involved2) {
        if (involved1 != null)
            setting.getSimulation().getSimulationWorld().setHighlighted1(involved1);
        if (involved2 != null)
            setting.getSimulation().getSimulationWorld().setHighlighted2(involved2);

        if (setting.getSimulation().isRunning())
            setting.getSimulation().toggleRunning();
        if (setting.getScenarioManager().isRunningTest()) {
            setting.getScenarioManager().failCurrentTest(reason);
        }
    }

    public void report(String reason, Robot involved) {
        report(reason, involved, null);
    }

    public abstract void update(List<? extends Entity> entities);

    public abstract void reset();
}