package de.hpi.mod.sim.env.simulation;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.setting.Setting;
import de.hpi.mod.sim.env.setting.infinitestations.Robot;
import de.hpi.mod.sim.env.setting.infinitestations.Robot.RobotState;
import de.hpi.mod.sim.env.testing.Scenario;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

public class World { 
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

	public World(Setting setting) {
		this.setting = setting;
	}

	public void initialize(SimulationWorld simulationWorld) {
		this.simulationWorld = simulationWorld;
	}

	public void reset() {
		simulationWorld.resetZoom();
		simulationWorld.resetOffset();
		simulationWorld.resetHighlightedRobots();
	}


	public void addTimeListener(ITimeListener timeListener) {
		timeListeners.add(timeListener);
	}

	private void refreshTimeListeners() {
		timeListeners.forEach(ITimeListener::refresh);
	}

	public List<Robot> getRobots() {
		return setting.getRobots();
	}

	/**
	 * Clears all Robots, stops the simulation, loads the scenario and plays it
	 * 
	 * @param scenario The Scenario to play
	 */
	public void playScenario(Scenario scenario) {
		while (isRefreshing || isUpdating) {
			// Do nothing while refreshing or updating
		}

		if (isRunning())
			toggleRunning();
		getRobots().clear();

		scenario.loadScenario(this);
	}

	/**
	 * Refreshes the Simulation and sends Sensor-Refreshes to all Robots. Locks the
	 * List of robots
	 */
	public synchronized void refresh() {
		if (running && !runForbidden) {
			isRefreshing = true;
			setting.getRoboterDispatcher().refresh();
			isRefreshing = false;
			notifyAll();
		}
	}

	/**
	 * Updates the Robots each frame. Locks the List of Robots
	 * 
	 * @param delta The time since last frame in milliseconds
	 */
	public synchronized void update(float delta) {
		if (running && !runForbidden) {
			try {
				isUpdating = true;
				setting.updateRobots(delta);
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

	
	public synchronized Robot addRobot() {		
		return addRobotRunner(() -> setting.getRoboterDispatcher().addRobot());
	}

	public Robot addRobotAtPosition(Position pos, RobotState initialState, Orientation facing, List<Position> targets,	
			int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedBattery,
			boolean hardArrivedConstraint) {
				
		return addRobotRunner(() -> setting.getRoboterDispatcher().addRobotAtPosition(pos, initialState, facing, targets, delay,
				initialDelay, fuzzyEnd, unloadingTest, hasReservedBattery, hardArrivedConstraint));
	}

	public Robot addRobotInScenario(Position pos, Orientation facing, int delay) {
		return addRobotRunner(() -> setting.getRoboterDispatcher().addRobotInScenario(pos, facing, delay));
	}

	private Robot addRobotRunner(AddRobotRunner runner) { // TODO: What is this doing? At least partially relocate to
															// SimulationWorld, I guess
		while (isRefreshing || isUpdating) {
			// Do nothing while refreshing or updating
		}

		Robot r = runner.run();
		if (simulationWorld.getHighlightedRobot1() == null)
			setHighlightedRobot1(r);
		else
			setHighlightedRobot2(r);
		return r;
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

	public void dispose() {
		setting.getRoboterDispatcher().close();
	}

	private interface AddRobotRunner {
		Robot run(); 
	}

	public void setRunForbidden(boolean isForbidden) {
		runForbidden = isForbidden;
	}

	public void releaseAllLocks() {
		setting.getRoboterDispatcher().releaseAllLocks();
	}

	public void updateSimulator(int chargingStationsInUse) {
		setting.getRoboterDispatcher().createNewStationManager(chargingStationsInUse);
	}

	// TODO generalize
	public void setHighlightedRobot2(Robot r) {
		simulationWorld.setHighlightedRobot2(r);
	}

	// TODO generalize
	public void setHighlightedRobot1(Robot r) {
		simulationWorld.setHighlightedRobot2(r);
	}
}