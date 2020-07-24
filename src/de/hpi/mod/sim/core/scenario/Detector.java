package de.hpi.mod.sim.core.scenario;

import java.util.List;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.core.model.Setting;

public abstract class Detector {

    protected Setting setting;

    private boolean activated = false;

    protected Detector(Setting setting) {
        this.setting = setting;
    }

    public void report(String reason, IHighlightable involved1, IHighlightable involved2) {
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

    public void report(String reason, IHighlightable involved) {
        report(reason, involved, null);
    }

    public void report(String reason) {
        report(reason, null, null);
    }

    public abstract void update(List<? extends Entity> entities);

    public abstract void reset();

    public boolean isActivated() {
        return activated;
    }

    public void activate() {
        activated = true;
    }

    public void deactivate() {
        activated = false;
    }
}