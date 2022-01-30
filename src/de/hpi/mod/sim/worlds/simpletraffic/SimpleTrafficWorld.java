package de.hpi.mod.sim.worlds.simpletraffic;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.GridConfiguration;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotRenderer;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;
import de.hpi.mod.sim.worlds.simpletraffic.entities.TrafficLight;
import de.hpi.mod.sim.worlds.simpletraffic.entities.rendering.SimpleTrafficRobotRenderer;
import de.hpi.mod.sim.worlds.simpletraffic.entities.rendering.TrafficLightRenderer;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.ScenarioGenerator;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.TestCaseGenerator;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;

public class SimpleTrafficWorld extends RobotWorld {

	private TrafficLightRenderer trafficLightRenderer;

	public SimpleTrafficWorld() {
		super();
		publicName = "Simple robots with Traffic Light World";
	}

	@Override
	public void initialize() {
		super.initialize();
		trafficLightRenderer = new TrafficLightRenderer(getSimulationBlockView(), getStreetNetworkManager());
	}

	@Override
	protected GridManager createGridManager() {
		return new TrafficGridManager();
	}

	@Override
    protected RobotRenderer createRobotRenderer() {
    	return new SimpleTrafficRobotRenderer(getSimulationBlockView(), getStreetNetworkManager());
    }

	@Override
	public void resetScenario() {
	}

	@Override
	public List<Scenario> getScenarios() {
		return new ScenarioGenerator(this).getScenarios();
	}

	@Override
	public Map<String, List<TestScenario>> getTestGroups() {
		return new TestCaseGenerator(this).getAllTestCases();
		// return new java.util.Hashtable<>();
	}

	@Override
	public void configurationChanged() {
		// Trigger update in GridManager
		getStreetNetworkManager().resetFieldDataStructures();
	}

	@Override
	public void refreshSimulationSize(int currentHeight, int currentWidth) {

		// Transform pixes size to blocks
		SimulationBlockView blockView = getSimulationBlockView();
		float blockSize = blockView.getBlockSize();
		int width = (int) (currentWidth / blockSize);
		int height = (int) (currentHeight / blockSize);

		// Update field size in configuration
		SimpleTrafficWorldConfiguration.setAvailableFieldDimensions(width, height);

		// Trigger a generic refresh to ensure that all properties are synced.
		configurationChanged();
	}

	@Override
	public void runScenario(Scenario scenario) {
		// Start all Scenarios by
		super.runScenario(scenario);
	}

	@Override
	public void updateEntities(float delta) {
		// Update Robots (via super)
		super.updateEntities(delta);

		// Update local entities
		getStreetNetworkManager().updateNonRobotEntities(delta);
	}
	
	@Override
	public void clearEntities() {
		super.clearEntities();
		getStreetNetworkManager().clearNonRobotEntities();
	}

	@Override
	protected void renderEntities(Graphics graphics) {
		this.trafficLightRenderer.render(graphics, getSimulationBlockView().getBlockSize());
		super.renderEntities(graphics);
	}

	@Override
	public List<? extends Entity> getEntitiesForDetectors() {
		// Get Robots
		List<? extends Entity> robots = super.getEntitiesForDetectors();

		// Get other relevant entities
		List<? extends Entity> streetNetworkEntities = getStreetNetworkManager().getNonRobotEntitiesForDetectors();

		// Combine Lists
		return Stream.concat(robots.stream(), streetNetworkEntities.stream()).collect(Collectors.toList());
	}

	public TrafficGridManager getStreetNetworkManager() {
		return (TrafficGridManager) getGridManager();
	}

	@Override
	public IHighlightable getHighlightAtPosition(int x, int y) {

		// Use super method to find if there is a robot at the targeted position
		IHighlightable robotHighlight = super.getHighlightAtPosition(x, y);
		if (robotHighlight != null)
			return robotHighlight;

		// If there is no robot, check if there is a crossroad
        Position pos = getSimulationBlockView().toGridPosition(x, y);
		Optional<TrafficLight> trafficLightHighlight = getStreetNetworkManager().getTrafficLightAt(pos);
		if (trafficLightHighlight.isPresent())
			return trafficLightHighlight.get();
		
		// If nothing matches, return null
		return null;

	}
}