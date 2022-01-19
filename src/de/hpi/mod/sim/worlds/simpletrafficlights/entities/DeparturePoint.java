package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;

public class DeparturePoint extends TransitPoint {

	private enum DeparturePointState {
		NOT_OCCUPIED, ROBOT_WAITING_BEFORE_START, ROBOT_STARTED, ROBOT_LEFT
	}
	DeparturePointState currentState = DeparturePointState.NOT_OCCUPIED;
	float countdownTimer = 0;
	
	SimpleRobot startingRobot;
	ArrivalPoint targetforStartingRobot;

	public DeparturePoint(int i, Position p, Orientation o) {
		super(i, p, o);
	}

	public void addStartingRobot(SimpleRobot robot, ArrivalPoint arrival) {
		// Store References
		this.startingRobot = robot;
		this.targetforStartingRobot = arrival;
		
		// Put robot in correct spot
		this.startingRobot.setRobotTo(getPosition());
		this.startingRobot.setFacing(getOrientation());

		// Start timer to send target to robot on time.
		this.currentState = DeparturePointState.ROBOT_WAITING_BEFORE_START;
		this.countdownTimer = 1000; // TODO: Randomize value
	}

	public void update(float delta) {

		// Decrement timer
		if (this.countdownTimer > 0) {
			this.countdownTimer -= delta;
		}

		// Update depending on current task and timer...

		// Case 1: If currently in pause before task start AND timer has tun out....
		if (this.currentState == DeparturePointState.ROBOT_WAITING_BEFORE_START && this.countdownTimer <= 0) {
			System.out.println("START");

			// ...send start signal to robot and update starte.
			this.startingRobot.setTargetAndNotify(this.targetforStartingRobot.getPosition());
			this.currentState = DeparturePointState.ROBOT_STARTED;
		}

		// Case 2: Task was send AND robot left field....
		else if (this.currentState == DeparturePointState.ROBOT_STARTED && !this.startingRobot.pos().fuzzyEquals(this.getPosition())) {

			// ...update state and start timer.
			this.currentState = DeparturePointState.ROBOT_LEFT;
			this.countdownTimer = 1000; // TODO: Randomize value
		}

		// Case 3: If currently in pause before task start AND timer has tun out....
		else if (this.currentState == DeparturePointState.ROBOT_LEFT && this.countdownTimer <= 0) {

			// ...update state and start timer.
			this.currentState = DeparturePointState.ROBOT_WAITING_BEFORE_START;
		}
	}

}
