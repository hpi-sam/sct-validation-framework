package de.hpi.mod.sim.core.simulation;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.view.model.ITimeListener;
import de.hpi.mod.sim.core.view.sim.SimulationView;

public class Simulation { 
	private SimulationView simulationView;

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

	public Simulation(Setting setting, SimulationView simulationView) {
		this.setting = setting;
		this.simulationView = simulationView;
	}

	public void reset() {
		simulationView.reset();
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

	public SimulationView getSimulationWorld() {
		return simulationView;
	}

	public <E extends Entity> E addEntityRunner(Supplier<E> entityGetter) { 
		while (isRefreshing || isUpdating) {
			// Do nothing while refreshing or updating
		}

		E e = entityGetter.get();
		if (e instanceof IHighlightable) {
			if (simulationView.getHighlighted1() == null)
				simulationView.setHighlighted1((IHighlightable) e);
			else
				simulationView.setHighlighted2((IHighlightable) e);
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
}