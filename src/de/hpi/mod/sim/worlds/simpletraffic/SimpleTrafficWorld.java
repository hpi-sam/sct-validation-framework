package de.hpi.mod.sim.worlds.simpletraffic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotRenderer;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;
import de.hpi.mod.sim.worlds.flasher.config.FlasherConfiguration;
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
	public void render(Graphics graphics) {
		super.render(graphics);
		if(SimpleTrafficWorldConfiguration.showStatistics() && getScenarioManager().isRunningScenario())
			this.drawStatistics(graphics);
	}
	
	private void drawStatistics(Graphics graphics) {

		float timeInSeconds = getScenarioManager().getCurrentSimulationTime();
		int departedRobots = getStreetNetworkManager().getNumberOfDepartedRobots();
		int arrivedRobots = getStreetNetworkManager().getNumberOfArrivedRobots();
		

		String robotsPerMinuteString = "---";
		if(timeInSeconds >= 60) {
			float robotsPerMinute = (arrivedRobots / timeInSeconds) *60;
			robotsPerMinuteString = new DecimalFormat("#.00").format(robotsPerMinute);
		}
		

		// TopRightPoint
		Point2D topRightPoint = getSimulationBlockView().toDrawPosition(
				SimpleTrafficWorldConfiguration.getFieldWidth(),
				SimpleTrafficWorldConfiguration.getFieldHeight());
						
		Font bigFont = new Font("Monospaced", Font.BOLD, 18);
		Font smallFont = new Font("Arial", Font.PLAIN, 10);
		
		// Get fontmetrics to calulate spacings
	    FontMetrics bigFontMetrics = graphics.getFontMetrics(bigFont);
	    FontMetrics smallFontMetrics = graphics.getFontMetrics(smallFont);
	    
	    // Set color for all text rendering
		graphics.setColor(Color.WHITE);
		
		graphics.setFont(bigFont);
		
		String text1 = " Statistics ";
		int text1X = (int) (topRightPoint.getX() - bigFontMetrics.stringWidth(text1));
		int text1Y =(int) (topRightPoint.getY() + 2*getSimulationBlockView().getBlockSize());

		graphics.drawString(text1, text1X, text1Y);
		
		String text2 = robotsPerMinuteString;
		int text2X = (int) (text1X + (bigFontMetrics.stringWidth(text1) - bigFontMetrics.stringWidth(text2))/2);
		int text2Y = text1Y + (int) (1.2*bigFontMetrics.getHeight());
		graphics.drawString(text2, text2X, text2Y);

		graphics.setFont(smallFont);
		
		String text3 = "robots/min";
		int text3X = (int) (text1X + (bigFontMetrics.stringWidth(text1) - smallFontMetrics.stringWidth(text3))/2);
		int text3Y = text2Y +(int) +smallFontMetrics.getHeight();
		graphics.drawString(text3, text3X, text3Y);
		
		
		String text4 = "Departed: "+departedRobots+ " robots";
		int text4X = (int) (text1X + (bigFontMetrics.stringWidth(text1) - smallFontMetrics.stringWidth(text4))/2);
		int text4Y = text3Y +(int) (1.5*smallFontMetrics.getHeight());
		graphics.drawString(text4, text4X, text4Y);
		
		String text5 = "Arrived: "+arrivedRobots+ " robots";
		int text5X = (int) (text1X + (bigFontMetrics.stringWidth(text1) - smallFontMetrics.stringWidth(text5))/2);
		int text5Y = text4Y +(int) smallFontMetrics.getHeight();
		graphics.drawString(text5, text5X, text5Y);
		
		String text6 = "Now driving: "+(departedRobots-arrivedRobots)+ " robots";
		int text6X = (int) (text1X + (bigFontMetrics.stringWidth(text1) - smallFontMetrics.stringWidth(text6))/2);
		int text6Y = text5Y +(int) smallFontMetrics.getHeight();
		graphics.drawString(text6, text6X, text6Y);
			
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