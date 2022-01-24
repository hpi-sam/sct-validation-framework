package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public class ArrivalPoint extends TransitPoint {
	
	List<SimpleRobot> awaitedRobots = new ArrayList<>();
	SimpleRobot arrivedRobot;
	int totalArrivedRobots = 0;
	
	public ArrivalPoint(int i, Position p, Orientation o, boolean r) {
		super(i, p, o, r);
	}
	
	public void addExpectedRobot(SimpleRobot robot) {
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
				// ...move robot to idle location....
				arrivedRobot.moveToIdlePosition();
				arrivedRobot.getCrossRoadsManager().makeRobotIdle(arrivedRobot);
				
				// ... and make departure point free for new arrivals.
				this.arrivedRobot = null;
			}
			return;
		}
		
		// Case 2: One of the awaited robots has called "arrive" and is actually here...
		Optional<SimpleRobot> potentiallyArrivedRobot = awaitedRobots.stream().filter(robot -> robot.hasReportedArrive() && robot.isOn(getPosition())).findAny();
		if(potentiallyArrivedRobot.isPresent()) {
			// ...update data structure and start timer.
			this.arrivedRobot = potentiallyArrivedRobot.get();
			this.arrivedRobot.resetArrived();
			this.awaitedRobots.remove(arrivedRobot);
			this.startCountdownTimer();
			this.totalArrivedRobots++;		
		}
	}

}
