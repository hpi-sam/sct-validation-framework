package de.hpi.mod.sim.env.testing;

import java.util.List;

import de.hpi.mod.sim.env.setting.Setting;
import de.hpi.mod.sim.env.setting.infinitestations.Robot;

public abstract class Detector {

    protected Setting setting;

    protected Detector(Setting setting) {
        this.setting = setting;
    }

    public void report(String reason, Robot involved1, Robot involved2) {
        if (involved1 != null)
            setting.getWorld().setHighlightedRobot1(involved1);
        if (involved2 != null)
            setting.getWorld().setHighlightedRobot2(involved2);

        if (setting.getWorld().isRunning())
            setting.getWorld().toggleRunning();
        if (setting.getScenarioManager().isRunningTest()) {
            setting.getScenarioManager().failCurrentTest(reason);
        }
    }

    public void report(String reason, Robot involved) {
        report(reason, involved, null);
    }

    public abstract void update(List<Robot> robots);

    public abstract void reset();
}