package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.*;

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
}
