package de.hpi.mod.sim.worlds.simpletraffic.scenario.specification;

import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.entities.RelativePosition;
import de.hpi.mod.sim.worlds.simpletraffic.entities.TrafficLight;
import de.hpi.mod.sim.worlds.simpletraffic.entities.TrafficLightWithStaticColors;

public class TestTrafficLightSpecification extends TrafficLightSpecification {
	
	boolean northGreen;
	boolean eastGreen;
	boolean southGreen;
	boolean westGreen;

	public TestTrafficLightSpecification(RelativePosition p, TrafficGridManager g, boolean n, boolean e, boolean s, boolean w) {
		super(p, g);
		this.northGreen = n;
		this.eastGreen = e;
		this.southGreen = s;
		this.westGreen = w;
	}
	public TestTrafficLightSpecification(RelativePosition p, TrafficGridManager g, boolean allGreen) {
		this(p, g, allGreen, allGreen, allGreen, allGreen);
	}

    @Override
    public TrafficLight createEntity() {
    	TrafficLight entity = new TrafficLightWithStaticColors(getRelativePosition(), getCoordinates(), northGreen, eastGreen, southGreen, westGreen);
        this.getGridManager().addTrafficLight(entity);
		return entity;
    }
	

}
