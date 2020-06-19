package de.hpi.mod.sim.core.simulation;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.testing.Scenario;
import de.hpi.mod.sim.core.view.model.ITimeListener;
import de.hpi.mod.sim.core.view.sim.SimulationWorld;

public class Simulation { 
	private SimulationWorld simulationWorld;

	private Setting setting;

	/**
	 * Whether the simulation is running or not
	 */
	private boolean running = false;

	/**
	 * If set to true, the simulation will be considered as not running, even if it
	 * can Run
	 */
	private boolean runForbidden = false;

	/**
	 * Used for locking while the List of Robots gets traversed
	 */
	private boolean isRefreshing = false, isUpdating = false;

	/**
	 * List of {@link ITimeListener}s. Gets called if the time flow changes.
	 */
	private List<ITimeListener> timeListeners = new ArrayList<>();

	public Simulation(Setting setting) {
		this.setting = setting;
	}

	public void initialize(SimulationWorld simulationWorld) {
		this.simulationWorld = simulationWorld;
	}

	public void reset() {
		simulationWorld.resetZoom();
		simulationWorld.resetOffset();
		simulationWorld.resetHighlightedEntities();
	}


	public void addTimeListener(ITimeListener timeListener) {
		timeListeners.add(timeListener);
	}

	private void refreshTimeListeners() {
		timeListeners.forEach(ITimeListener::refresh);
	}

	/**
	 * Clears all Entities, stops the simulation, loads the scenario and plays it
	 * 
	 * @param scenario The Scenario to play
	 */
	public void playScenario(Scenario scenario) {
		while (isRefreshing || isUpdating) {
			// Do nothing while refreshing or updating
		}

		if (isRunning())
			toggleRunning();
		setting.clearEntities();

		scenario.loadScenario(setting);
	}

	/**
	 * Refreshes the Simulation and sends Sensor-Refreshes to all Robots. Locks the
	 * List of robots
	 */
	public synchronized void refresh() {
		if (running && !runForbidden) {
			isRefreshing = true;
			setting.refreshEntities();
			isRefreshing = false;
			notifyAll();
		}
	}

	/**
	 * Updates the Entities each frame. Locks the List of Entities
	 * 
	 * @param delta The time since last frame in milliseconds
	 */
	public synchronized void update(float delta) {
		if (running && !runForbidden) {
			try {
				isUpdating = true;
				setting.updateEntities(delta);
				isUpdating = false;
				notifyAll();
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
		}
	}

	public SimulationWorld getSimulationWorld() {
		return simulationWorld;
	}

	public Entity addEntityRunner(Supplier<Entity> entityGetter) { 
		while (isRefreshing || isUpdating) {
			// Do nothing while refreshing or updating
		}

		Entity e = entityGetter.get();
		if (e.isHighlightable()) {
			if (simulationWorld.getHighlightedEntity1() == null)
				setHighlightedEntity1(e);
			else
				setHighlightedEntity2(e);
		}
		return e;
	}

	public void toggleRunning() {
		running = !running;
		refreshTimeListeners();
	}

	public boolean isRunning() {
		if (runForbidden) {
			return false;
		}
		return running;
	}

	public void setRunForbidden(boolean isForbidden) {
		runForbidden = isForbidden;
	}

	public void onSimulationPropertyRefresh(int chargingStationsInUse) {
		setting.onSimulationPropertyRefresh(); 
	}

	// TODO move.
	public void setHighlightedEntity2(Entity e) {
		simulationWorld.setHighlightedEntity2(e);
	}

	// TODO move
	public void setHighlightedEntity1(Entity e) {
		simulationWorld.setHighlightedEntity1(e);
	}
}