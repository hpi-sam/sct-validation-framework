package de.hpi.mod.sim.environment;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.PositionType;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IProcessor;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IRobotActors;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.ISensor;

public class DummyRobot implements ISensor, IRobotActors, IProcessor {

    @Override
    public void driveForward() {

    }

	@Override
	public void driveBackward() {
		
	}

    @Override
    public void turnLeft() {

    }

    @Override
    public void turnRight() {

    }

    @Override
    public void startUnloading() {

    }

    @Override
    public void arrived() {

    }

    @Override
    public Orientation posOrientation() {
        return null;
    }

    @Override
    public PositionType posType() {
        return null;
    }

    @Override
    public Direction targetDirection() {
        return null;
    }

    @Override
    public boolean isOnTarget() {
        return false;
    }

	@Override
	public boolean canUnloadToTarget() {
		return false;
	}

    @Override
    public boolean blockedLeft() {
        return false;
    }

    @Override
    public boolean blockedFront() {
        return false;
    }

    @Override
    public boolean blockedRight() {
        return false;
    }

    @Override
    public boolean blockedWaypointAhead() {
        return false;
    }

    @Override
    public boolean blockedWaypointLeft() {
        return false;
    }

    @Override
    public boolean blockedWaypointRight() {
        return false;
    }

    @Override
    public boolean blockedCrossroadAhead() {
        return false;
    }

    @Override
    public boolean blockedCrossroadRight() {
        return false;
    }

	@Override
	public int posX() {
		return 0;
	}

	@Override
	public int posY() {
		return 0;
	}

	@Override
	public int targetX() {
		return 0;
	}

	@Override
	public int targetY() {
		return 0;
	}

	@Override
	public boolean canChargeAtTarget() {
		return false;
	}
}
