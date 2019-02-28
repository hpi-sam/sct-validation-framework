package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.NewRobot;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;
import de.hpi.mod.sim.env.SimulatorConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ScenarioManager {

    private List<Scenario> scenarios = new ArrayList<>();
    private List<TestScenario> tests = new ArrayList<>();
    private SimulationWorld world;
    private List<ITestListener> listeners = new ArrayList<>();


    public ScenarioManager(SimulationWorld world) {
        this.world = world;
        scenarios.add(new EmptyScenario());
        scenarios.add(new OneRobotScenario());
        scenarios.add(new EasyScenario());
        scenarios.add(new MediumScenario());
        scenarios.add(new HardScenario());
        tests.add(new FourRobotsOnCrossroadScenario());
        tests.add(new OppositeRobotsScenario());
    }

    public void runScenario(Scenario scenario) {
    	world.resetZoom();
		world.resetOffset();
        world.playScenario(scenario);
        if (scenario instanceof TestScenario)
            world.toggleRunning();
    }

    public void addTestListener(ITestListener listener) {
        listeners.add(listener);
    }

    public void refresh() {
        for (TestScenario test : tests) {
            if (test.isPassed()) {
                for (ITestListener listener : listeners) {
                    listener.onTestCompleted(test);
                }
            }
        }
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public List<TestScenario> getTests() {
        return tests;
    }

    private class EmptyScenario extends Scenario {

        public EmptyScenario() {
            name = "Empty";
            resizable = true;
        }

        @Override
        public List<NewRobot> initializeScenario() {
            return new ArrayList<>();
        }
    }
    
    private class OneRobotScenario extends Scenario{
    	public OneRobotScenario() {
    		name = "One Robot";
    	}
    	
    	@Override
    	public List<NewRobot> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<NewRobot> newRobots = new ArrayList<>();
    		
    		do {
    			station_number = ThreadLocalRandom.current().nextInt(maxStations);
    		} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    		
            newRobots.add(new NewScenarioRobotHPI(new Position(SimulatorConfig.getFirstStationTop().getX()
            		+ station_number * SimulatorConfig.getSpaceBetweenChargingStations(), 
            		SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]), Orientation.EAST));
            return newRobots;
    	}
    }
    
    private class EasyScenario extends Scenario{
    	public EasyScenario() {
    		name = "Easy Scenario";
    	}
    	
    	@Override
    	public List<NewRobot> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<NewRobot> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<5; i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new NewScenarioRobotHPI(new Position(SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations(), 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]), Orientation.EAST));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }
    
    private class MediumScenario extends Scenario{
    	public MediumScenario() {
    		name = "Medium Scenario";
    	}
    	
    	@Override
    	public List<NewRobot> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<NewRobot> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<10; i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new NewScenarioRobotHPI(new Position(SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations(), 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]), Orientation.EAST));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }
    
    private class HardScenario extends Scenario{
    	public HardScenario() {
    		name = "Hard Scenario";
    	}
    	
    	@Override
    	public List<NewRobot> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<NewRobot> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<20; i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new NewScenarioRobotHPI(new Position(SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations(), 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]), Orientation.EAST));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }

    private class FourRobotsOnCrossroadScenario extends TestScenario {

        public FourRobotsOnCrossroadScenario() {
            name = "4 Robots on Crossroad";
        }

        @Override
        public List<NewRobot> initializeScenario() {
            List<NewRobot> newRobots = new ArrayList<>();
            newRobots.add(new NewWaypointRobot(new Position(6, 5), Orientation.WEST, new Position(0, 5)));
            newRobots.add(new NewWaypointRobot(new Position(5, 3), Orientation.NORTH, new Position(5, 9)));
            newRobots.add(new NewWaypointRobot(new Position(3, 4), Orientation.EAST, new Position(9, 4)));
            newRobots.add(new NewWaypointRobot(new Position(4, 6), Orientation.SOUTH, new Position(4, 0)));
            return newRobots;
        }
    }

    private class OppositeRobotsScenario extends TestScenario {
        public OppositeRobotsScenario() { name = "Opposite Robots"; }

        @Override
        protected List<NewRobot> initializeScenario() {
            List<NewRobot> newRobots = new ArrayList<>();
            newRobots.add(new NewWaypointRobot(new Position(3, 4), Orientation.EAST, new Position(1, 0)));
            newRobots.add(new NewWaypointRobot(new Position(3, 5), Orientation.WEST, new Position(3, 4)));
            return newRobots;
        }
    }

    private class NewWaypointRobot extends NewRobot {

        private Position pos, target;
        private Orientation facing;

        public NewWaypointRobot(Position pos, Orientation facing, Position target) {
            this.pos = pos;
            this.target = target;
            this.facing = facing;
        }

        @Override
        public Robot register(SimulationWorld sim) {
            return sim.addRobotAtWaypoint(pos, facing, target);
        }
    }
    
    private class NewScenarioRobotHPI extends NewRobot{
    	private Position pos;
    	private Orientation facing;
    	
    	public NewScenarioRobotHPI(Position pos, Orientation facing) {
    		this.pos = pos;
    		this.facing = facing;
    	}
    	
    	@Override
    	public Robot register(SimulationWorld sim) {
    		return sim.addRobotInScenarioHPI2(pos, facing);
    	}
    }
}
