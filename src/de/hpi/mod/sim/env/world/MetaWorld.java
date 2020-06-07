package de.hpi.mod.sim.env.world;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.simulation.Simulator;
import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.simulation.robot.Robot.RobotState;
import de.hpi.mod.sim.env.testing.Scenario;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

public class MetaWorld { // TODO make abstract
	private SimulationWorld simulationWorld;

	private Simulator simulator;

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

	public MetaWorld() {
		simulator = new Simulator();
	}

	public void initialize(SimulationWorld simulationWorld) {
		this.simulationWorld = simulationWorld;
		registerDetectors();
	}

	 void registerDetectors() {} //TODO make abstract

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
		simulator.getRobots().clear();

		scenario.loadScenario(this);
	}

	/**
	 * Refreshes the Simulation and sends Sensor-Refreshes to all Robots. Locks the
	 * List of robots
	 */
	public synchronized void refresh() {
		if (running && !runForbidden) {
			isRefreshing = true;
			simulator.refresh();
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
				for (Robot robot : simulator.getRobots()) {
					if (robot.getRobotSpecificDelay() == 0 || !robot.isInTest()) {
						robot.getDriveManager().update(delta);
					} else {
						robot.getDriveManager().update(delta, robot.getRobotSpecificDelay());
					}

				}
				isUpdating = false;
				notifyAll();
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
		}
	}

	public Simulator getSimulator() {
		return simulator;
	}

	public SimulationWorld getSimulationWorld() {
		return simulationWorld;
	}

	/**
	 * Returns the Robots of the Simulation. Be careful when using this, since other
	 * threads are modifying the List, which can lead to
	 * {@link ConcurrentModificationException}
	 * 
	 * @return List of Robots in Simulation
	 */
	public List<Robot> getRobots() {
		return simulator.getRobots();
	}

	public synchronized Robot addRobot() {
		return addRobotRunner(() -> simulator.addRobot());
	}

	public Robot addRobotAtPosition(Position pos, RobotState initialState, Orientation facing, List<Position> targets,
			int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedBattery,
			boolean hardArrivedConstraint) {
		return addRobotRunner(() -> simulator.addRobotAtPosition(pos, initialState, facing, targets, delay,
				initialDelay, fuzzyEnd, unloadingTest, hasReservedBattery, hardArrivedConstraint));
	}

	public Robot addRobotInScenario(Position pos, Orientation facing, int delay) {
		return addRobotRunner(() -> simulator.addRobotInScenario(pos, facing, delay));
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
		simulator.close();
	}

	private interface AddRobotRunner {
		Robot run();
	}

	public void setRunForbidden(boolean isForbidden) {
		runForbidden = isForbidden;
	}

	public void releaseAllLocks() {
		simulator.releaseAllLocks();
	}

	public void updateSimulator(int chargingStationsInUse) {
		simulator.createNewStationManager(chargingStationsInUse);
	}

	public void setHighlightedRobot2(Robot r) {
		simulationWorld.setHighlightedRobot2(r);
	}

	public void setHighlightedRobot1(Robot r) {
		simulationWorld.setHighlightedRobot2(r);
	}
}