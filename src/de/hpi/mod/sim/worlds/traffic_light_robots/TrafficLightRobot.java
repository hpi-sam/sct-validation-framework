package de.hpi.mod.sim.worlds.traffic_light_robots;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;

public class TrafficLightRobot extends Robot {

    public TrafficLightRobot(int robotID, RobotGridManager grid, Position startPosition, Orientation startFacing) {
        super(robotID, grid, startPosition, startFacing);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onRefresh() {
        // TODO Auto-generated method stub

    }
    
}
