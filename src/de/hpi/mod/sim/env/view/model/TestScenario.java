package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.util.ArrayList;
import java.util.List;

public abstract class TestScenario extends Scenario {

    private List<Robot> robots = new ArrayList<>();
    private boolean hasRun = false;


    @Override
    public void loadScenario(SimulationWorld sim) {
        robots.clear();
        List<NewRobot> newRobots = initializeScenario();
        for (NewRobot r : newRobots) {
            robots.add(r.register(sim));
        }
        hasRun = true;
    }

    public boolean isPassed() {
        return hasRun && robots.stream().allMatch(Robot::isTargetReached);
    }
}
