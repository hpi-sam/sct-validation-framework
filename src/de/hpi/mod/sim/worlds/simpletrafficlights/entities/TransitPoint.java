package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;

public abstract class TransitPoint implements Entity {

	// Id of the Transition point, starting at 0, counted on a clockwise circle starting at (1,1) in the bottom left corner
	private int id; 
	
	// Coordinates + Orientation of the transit point
	private Position position;
	private Orientation orientation;

	// Countdown properties
	private boolean randomizeWaitingTimes;
	private int countdownTimer;
	
	
	public TransitPoint(int i, Position p, Orientation o, boolean r) {
		this.id = i;
		this.position = p;
		this.orientation = o;
		this.randomizeWaitingTimes = r;
	}
	
	public int getId() {
		return id;
	}

	public Position getPosition() {
		return position;
	}	

	public boolean isNextTo(TransitPoint other) {
		return this.getId() == other.getId();
	}

	public Orientation getOrientation() {
		return orientation;
	}
	
	protected void startCountdownTimer() {
		// Start Timer...
		if(randomizeWaitingTimes) {
			// ...randomized (if so requested)...
			this.countdownTimer = ThreadLocalRandom.current().nextInt(
					SimpleTrafficLightsConfiguration.getDeparturePointMinimalWaitingTime(), 
					SimpleTrafficLightsConfiguration.getDeparturePointMaximalWaitingTime()
				);
		}else {
			// ...or not randomized (otherwise).
			this.countdownTimer = SimpleTrafficLightsConfiguration.getDeparturePointNormalWaitingTime();
		}
	}

	protected void updateCountdownTimer(int delta) {
		if(this.countdownTimer > 0)
			this.countdownTimer -= delta;
	}
	
	protected boolean countdownTimerFinished() {
		return  this.countdownTimer <= 0;
	}
	
}
