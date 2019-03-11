package de.hpi.mod.sim.env.robot;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.model.ISensorDataProvider;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;


public class RobotTest {

    private Robot createRobot(int x, int y, int tx, int ty, Orientation facing) {
        ISensorDataProvider grid = new ServerGridManagement(null);
        var r = new Robot(0, 0, grid, null,
                null, null, new Position(x, y), facing);
        r.setTarget(new Position(tx, ty));
        return r;
    }
}