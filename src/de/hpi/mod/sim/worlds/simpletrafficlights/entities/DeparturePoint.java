package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;

public class DeparturePoint extends TransitPoint {
	
	// Properties
	private SimpleRobot startingRobot;
	private ArrivalPoint targetforStartingRobot;

	// State
	private enum DeparturePointState {
		NOT_OCCUPIED, WAITING_BEFORE_ROBOT_STARTS, WAITING_FOR_ROBOT_TO_LEAVE, WAITING_AFTER_ROBOT_LEFT
	}
	private DeparturePointState currentState = DeparturePointState.NOT_OCCUPIED;

	public DeparturePoint(int i, Position p, Orientation o, boolean r) {
		super(i, p, o, r);
	}

	public void addStartingRobot(SimpleRobot robot, ArrivalPoint arrival) {
		if(this.currentState != DeparturePointState.NOT_OCCUPIED)
			return;
		
		// Store References
		this.startingRobot = robot;
		this.targetforStartingRobot = arrival;
		
		// Put robot in correct spot
		this.startingRobot.setRobotTo(getPosition());
		this.startingRobot.setTargetFacing(getOrientation());
		this.startingRobot.setFacingTo(getOrientation());
		
		// Temporarily define current position as destination
		this.startingRobot.setTarget(this.getPosition());

		// Start timer to send target to robot on time.
		this.currentState = DeparturePointState.WAITING_BEFORE_ROBOT_STARTS;
		this.startCountdownTimer();
	}

	public boolean isNotOccupied() {
		return this.currentState == DeparturePointState.NOT_OCCUPIED;
	}

	public void update(float delta) {
		
		// Decrement timer
		updateCountdownTimer((int) delta);

		// Update depending on current state and timer...

		// Case 1: If currently in pause before task start AND timer has tun out....
		if (this.currentState == DeparturePointState.WAITING_BEFORE_ROBOT_STARTS && this.countdownTimerFinished()) {
			// ...send start signal to robot and update starte.
			this.currentState = DeparturePointState.WAITING_FOR_ROBOT_TO_LEAVE;
			this.startingRobot.setTargetAndNotify(this.targetforStartingRobot.getPosition());
		}

		// Case 2: Task was send AND robot left field....
		else if (this.currentState == DeparturePointState.WAITING_FOR_ROBOT_TO_LEAVE && !this.startingRobot.pos().fuzzyEquals(this.getPosition())) {
			// ...update state and start timer.
			this.currentState = DeparturePointState.WAITING_AFTER_ROBOT_LEFT;
			this.startCountdownTimer();
		}

		// Case 3: If currently in pause before task start AND timer has tun out....
		else if (this.currentState == DeparturePointState.WAITING_AFTER_ROBOT_LEFT && this.countdownTimerFinished()) {
			// ...update state and start timer.
			this.currentState = DeparturePointState.NOT_OCCUPIED;
		}
	}

}
