package de.hpi.mod.sim.worlds.simpletraffic.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public class ArrivalPoint extends TransitPoint {
	
	List<SimpleTrafficRobot> awaitedRobots = new ArrayList<>();
	SimpleTrafficRobot arrivedRobot;
	int totalArrivedRobots = 0;
	
	public ArrivalPoint(int i, Position p, Orientation o, boolean r) {
		super(i, p, o, r);
	}
	
	public void addExpectedRobot(SimpleTrafficRobot robot) {
		awaitedRobots.add(robot);
	}

	public int getNumberOfExpectedRobots() {
		return awaitedRobots.size();
	}
	
	public void update(float delta) {
		
		// Decrement timer
		this.updateCountdownTimer((int) delta);
		
		// Case 1: There is an arrived robot... 
		if(this.arrivedRobot != null) {
			// ...AND timer has tun out...
			if(this.countdownTimerFinished()) {
				if(isSingleUse()) {
					// ...mark this point as used...
					singleUseFinished();
				}else {
					// ...OR move robot to idle location (for multiple use)....
					arrivedRobot.moveToIdlePosition();
					arrivedRobot.getCrossRoadsManager().makeRobotIdle(arrivedRobot);
				}
				
				// ... and make departure point free for new arrivals.
				this.arrivedRobot = null;
			}
			return;
		}
		
		// Case 2: One of the awaited robots has called "arrive" and is actually here...
		Optional<SimpleTrafficRobot> potentiallyArrivedRobot = awaitedRobots.stream().filter(robot -> robot.hasReportedArrive() && robot.isOn(getPosition())).findAny();
		if(potentiallyArrivedRobot.isPresent()) {
			// ...update data structure and start timer.
			this.arrivedRobot = potentiallyArrivedRobot.get();
			this.arrivedRobot.resetArrived();
			this.awaitedRobots.remove(arrivedRobot);
			arrivedRobot.getCrossRoadsManager().reportArrival();
			this.startCountdownTimer();
			this.totalArrivedRobots++;		
		}
	}

}
