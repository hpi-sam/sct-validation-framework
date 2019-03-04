package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.robot.Robot.RobotState;
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
        tests.add(new DriveToQueueEnd());
        tests.add(new DriveToLoadingPosition());
        tests.add(new DriveToUnloadingPosition());
        tests.add(new MiddleRoute());
        tests.add(new HandleThreeRobotsInStation());
        tests.add(new OppositeRobotsScenario());
    }

    public void runScenario(Scenario scenario) {
    	world.resetZoom();
		world.resetOffset();
        world.playScenario(scenario);
        if (scenario instanceof TestScenario) {
        	((TestScenario)scenario).setActive(true);
        	world.toggleRunning();
        }
    }

    public void addTestListener(ITestListener listener) {
        listeners.add(listener);
    }

    public void refresh() {
        for (TestScenario test : tests) {
            if (test.isPassed() && test.isActive()) {
            	test.setActive(false);
                for (ITestListener listener : listeners) {
                    listener.onTestCompleted(test);
                }
            }
        }
    }
    
    public SimulationWorld getWorld() {
    	return world;
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
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse(); i++) {
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
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse()*Math.ceil
    				(((float)SimulatorConfig.getBatteriesPerStation())/2); i++) {
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
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse()*SimulatorConfig.getBatteriesPerStation(); i++) {
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
            List<Position> targetsRobotOne = new ArrayList<>();
            targetsRobotOne.add(new Position(0,5));
            List<Position> targetsRobotTwo = new ArrayList<>();
            targetsRobotTwo.add(new Position(5,9));
            List<Position> targetsRobotThree = new ArrayList<>();
            targetsRobotThree.add(new Position(9,4));
            List<Position> targetsRobotFour = new ArrayList<>();
            targetsRobotFour.add(new Position(4,0));
            newRobots.add(new NewTestRobot(new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
            newRobots.add(new NewTestRobot(new Position(5, 3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo));
            newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree));
            newRobots.add(new NewTestRobot(new Position(4, 6), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotFour));
            return newRobots;
        }
    }
    
    private class DriveToQueueEnd extends TestScenario {
    	
    	public DriveToQueueEnd() {
    		name = "Drive to end of the queue";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targets = new ArrayList<>();
             targets.add(new Position(2,-5));
             newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_BATTERY, Orientation.EAST, targets));
             return newRobots;
         }
    }
    
    private class DriveToLoadingPosition extends TestScenario {
    	
    	public DriveToLoadingPosition() {
    		name = "Drive to loading position";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targets = new ArrayList<>();
             targets.add(new Position(2,0));
             newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_BATTERY, Orientation.EAST, targets));
             return newRobots;
         }
    }
    
    
    private class DriveToUnloadingPosition extends TestScenario {
    	
    	public DriveToUnloadingPosition() {
    		name = "Drive to unloading position";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targets = new ArrayList<>();
             targets.add(new Position(2,0));
             targets.add(new Position(3,10));
             newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_BATTERY, Orientation.EAST, targets));
             return newRobots;
         }
    }
    
    private class MiddleRoute extends TestScenario {
    	
    	public MiddleRoute() {
    		name = "Can handle medium long route";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targets = new ArrayList<>();
             targets.add(new Position(2,0));
             targets.add(new Position(3,10));
             targets.add(new Position(1,0));
             targets.add(new Position(2,0));
             targets.add(new Position(6,10));
             newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_BATTERY, Orientation.EAST, targets));
             return newRobots;
         }
    }
    
    private class HandleThreeRobotsInStation extends TestScenario {
    	
    	public HandleThreeRobotsInStation() {
    		name = "Handle three robots in station";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targetsRobotOne = new ArrayList<>();
             List<Position> targetsRobotTwo = new ArrayList<>();
             List<Position> targetsRobotThree = new ArrayList<>();
             targetsRobotOne.add(new Position(2,0));
             targetsRobotOne.add(new Position(3,10));
             targetsRobotTwo.add(new Position(2,0));
             targetsRobotTwo.add(new Position(3,13));
             targetsRobotThree.add(new Position(2,0));
             targetsRobotThree.add(new Position(3,16));
             newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_BATTERY, Orientation.EAST, targetsRobotOne));
             newRobots.add(new NewTestRobot(new Position(0, -3), RobotState.TO_BATTERY, Orientation.EAST, targetsRobotTwo));
             newRobots.add(new NewTestRobot(new Position(0, -4), RobotState.TO_BATTERY, Orientation.EAST, targetsRobotThree));
             return newRobots;
         }
    }

    private class OppositeRobotsScenario extends TestScenario {
        public OppositeRobotsScenario() { name = "Opposite Robots"; }

        @Override
        protected List<NewRobot> initializeScenario() {
            List<NewRobot> newRobots = new ArrayList<>();
            List<Position> targetsRobotOne = new ArrayList<>();
            List<Position> targetsRobotTwo = new ArrayList<>();
            targetsRobotOne.add(new Position(1,0));
            targetsRobotTwo.add(new Position(3,4));
            newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne));
            newRobots.add(new NewTestRobot(new Position(3, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotTwo));
            return newRobots;
        }
    }

    private class NewTestRobot extends NewRobot {

        private Position pos;
        private RobotState state;
        private List<Position> targets = new ArrayList<Position>();
        private Orientation facing;

        public NewTestRobot(Position pos, RobotState startingState, Orientation facing, List<Position> targets) {
            this.pos = pos;
            this.state = startingState;
            this.targets = targets;
            this.facing = facing;
        }

        @Override
        public Robot register(SimulationWorld sim) {
            return sim.addRobotAtPosition(pos, state, facing, targets);
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
