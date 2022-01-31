package de.hpi.mod.sim.worlds.simpletraffic.entities;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public class DeparturePoint extends TransitPoint {
	
	// Properties
	private SimpleTrafficRobot startingRobot;
	private ArrivalPoint targetforStartingRobot;

	// State
	private enum DeparturePointState {
		NOT_OCCUPIED, WAITING_BEFORE_ROBOT_STARTS, WAITING_FOR_ROBOT_TO_LEAVE, WAITING_AFTER_ROBOT_LEFT, FINISHED_AFTER_SINGLE_USE
	}
	private DeparturePointState currentState = DeparturePointState.NOT_OCCUPIED;

	public DeparturePoint(int i, Position p, Orientation o, boolean r) {
		super(i, p, o, r);
	}

	public void addStartingRobot(SimpleTrafficRobot robot, ArrivalPoint arrival) {
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
			this.startingRobot.setTargetAndNotify(this.targetforStartingRobot.getPosition());
			this.currentState = DeparturePointState.WAITING_FOR_ROBOT_TO_LEAVE;
		}

		// Case 2: Task was send AND robot left field....
		else if (this.currentState == DeparturePointState.WAITING_FOR_ROBOT_TO_LEAVE && !this.startingRobot.pos().fuzzyEquals(this.getPosition())) {
			if(isSingleUse()) {
				// ...mark single use as finshed.
				this.currentState = DeparturePointState.FINISHED_AFTER_SINGLE_USE;
				this.singleUseFinished();
			}else {
				// ...OR update state and start timer.
				this.currentState = DeparturePointState.WAITING_AFTER_ROBOT_LEFT;
				this.startCountdownTimer();
			}
		}

		// Case 3: If currently in pause before task start AND timer has tun out....
		else if (this.currentState == DeparturePointState.WAITING_AFTER_ROBOT_LEFT && this.countdownTimerFinished()) {
			// ...update state and start timer.
			this.currentState = DeparturePointState.NOT_OCCUPIED;
		}
	}

}
