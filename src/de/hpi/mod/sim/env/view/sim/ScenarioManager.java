package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.robot.Robot.RobotState;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.NewRobot;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;
import de.hpi.mod.sim.env.SimulatorConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class ScenarioManager {

    private List<Scenario> scenarios = new ArrayList<>();
    private Scenario clear = new EmptyScenario();
    private Scenario currentScenario;
    private Map<String,ArrayList<TestScenario>> testGroups = new LinkedHashMap<>();
    private SimulationWorld world;
    private List<ITestListener> listeners = new ArrayList<>();
    
    private Queue<TestScenario> testsToRun = new LinkedList<>();
    private boolean runningAllTests = false;
    
	private boolean currentTestFailed = false;
	private boolean isRunningTest = false;
	
	private TestScenario activeTest = null;
    CollisionDetector collisionDetector;
    DeadlockDetector deadlockDetector;
    DriveSimFrame frame;
	private List<TestScenario> tests = new ArrayList<>();


    public ScenarioManager(SimulationWorld world, CollisionDetector collisionDetector, DriveSimFrame frame) {
        this.world = world;
        this.collisionDetector = collisionDetector;
        this.frame = frame;
        initializeScenarioList();
        initializeTestGroupsMap();
        initializeTestList();
    }

	private void initializeScenarioList() {
        scenarios.add(new OneRobotScenario());
        scenarios.add(new EasyScenario());
        scenarios.add(new MediumScenario());
        scenarios.add(new HardScenario());
        scenarios.add(new HardcoreScenario());
	}

	private void initializeTestList() {
		for(String key : testGroups.keySet()) {
			for(TestScenario test : testGroups.get(key)) {
				tests.add(test);
			}
		}
	}

	private void initializeTestGroupsMap() {
		ArrayList<TestScenario> drivingToCrossroadTests = new ArrayList<>();
        drivingToCrossroadTests.add(new OppositeRobotsOnCrossroadScenario());
        drivingToCrossroadTests.add(new TwoRobotsOnCrossroadScenario());
        drivingToCrossroadTests.add(new ThreeRobotsOnCrossroadScenario());
        drivingToCrossroadTests.add(new FourRobotsOnCrossroadScenario());
        testGroups.put("Driving at Crossroad", drivingToCrossroadTests);
        ArrayList<TestScenario> drivingInStationTests = new ArrayList<>();
        drivingInStationTests.add(new DriveToQueueEnd());
        drivingInStationTests.add(new DriveToLoadingPosition());
        testGroups.put("Driving in Station", drivingInStationTests);
        ArrayList<TestScenario> simpleDrivingTests = new ArrayList<>();
        simpleDrivingTests.add(new DriveToUnloadingPosition());
        simpleDrivingTests.add(new HandleTwoRobotsInStation());
        simpleDrivingTests.add(new HandleThreeRobotsInStation());
        simpleDrivingTests.add(new MiddleRoute());
        simpleDrivingTests.add(new LongRoute());
        simpleDrivingTests.add(new OppositeRobots());
        // simpleDrivingTests.add(new ExplosionTest()); Test to test explosion functionality
        testGroups.put("Simple drive logic", simpleDrivingTests);
        ArrayList<TestScenario> complexDrivingTests = new ArrayList<>();
        complexDrivingTests.add(new MiddleRouteTwoRobots());
        complexDrivingTests.add(new MiddleRouteTwoRobots2());
        complexDrivingTests.add(new MiddleRouteThreeRobots());
        complexDrivingTests.add(new MiddleRouteTwoRobots3());
        testGroups.put("Complex drive logic", complexDrivingTests);
	}

    private void runScenario(Scenario scenario, boolean isTest) {
    	frame.allowRunning();
    	frame.setResizable(scenario.isResizable());
    	world.resetZoom();
		world.resetOffset();
		world.resetHighlightedRobots();
        world.playScenario(scenario);
        deadlockDetector.reactivate();
        collisionDetector.reset();
        if(!world.isRunning()) 
        	world.toggleRunning();
        
        frame.resetSimulationView();
        
        if (isTest) {
        	activeTest = (TestScenario) scenario;
        	isRunningTest = true;
        } else {
        	activeTest = null;
        	isRunningTest = false;
        }
    }
    
    public void runTest(TestScenario test) {
    	currentScenario = test;
    	testsToRun.clear();
    	runningAllTests = false;
    	test.resetTest();
    	frame.getTestListPanel().select(test);
    	frame.getTimerPanel().startNewClock();
    	frame.displayMessage("Starting test \"" + test.getName() + "\"");
    	runScenario(test, true);
    }
    
    public void runScenario(Scenario scenario) {
    	currentScenario = scenario;
    	testsToRun.clear();
    	runningAllTests = false;
    	frame.getScenarioPanel().select(scenario);
    	frame.getTimerPanel().startNewClock();
    	frame.displayMessage("Starting scenario: \"" + scenario.getName() + "\"");
    	runScenario(scenario, false);
    }
    
    public void runAllTests() {
    	testsToRun.clear();
    	testsToRun.addAll(tests);
    	runningAllTests = true;
    	frame.displayMessage("Running all tests.");
    	runNextTest();
    }

    private void runNextTest() {
    	if(testsToRun.isEmpty()) {
    		runningAllTests = false;
    	} else {
    		TestScenario nextTest = testsToRun.poll();
    		nextTest.resetTest();
    		frame.getTestListPanel().select(nextTest);
    		currentScenario = nextTest;
    		frame.getTimerPanel().startNewClock();
    		runScenario(nextTest, true);
    	}
	}

	public void addTestListener(ITestListener listener) {
        listeners.add(listener);
    }
	
	public void clearScenario() {
		currentScenario = null;
		testsToRun.clear();
    	runningAllTests = false;
    	frame.clearSelections();
    	frame.getTimerPanel().clearTimer();
    	runScenario(clear, false);
	}
	
	public void restartScenario() {
		if(currentScenario != null) {
			frame.getTimerPanel().startNewClock();
	    	frame.displayMessage("Restarting scenario: \"" + currentScenario.getName() + "\"");
			runScenario(currentScenario, isRunningTest);
			
		}
	}

    public void refresh() {
    	if (isRunningTest) {
    		if(currentTestFailed) {
    			deadlockDetector.deactivate();
    			for (ITestListener listener : listeners) {
                    listener.failTest(activeTest);
                }
    			activeTest.notifyFailToUser(frame);
    			currentTestFailed = false;
    			isRunningTest = false;
    			activeTest = null;
    			if (runningAllTests)
    				runNextTest();
    		} else if (activeTest.isPassed()) {
    			deadlockDetector.deactivate();
    			for (ITestListener listener : listeners) {
                    listener.onTestCompleted(activeTest);
                }
    			activeTest.notifySuccessToUser(frame);
    			isRunningTest = false;
    			activeTest = null;
    			if (runningAllTests)
    				runNextTest();
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
    
    public void setDeadlockDetector(DeadlockDetector deadlockDetector){
    	this.deadlockDetector = deadlockDetector;
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
    		
            newRobots.add(new NewScenarioRobot(new Position((SimulatorConfig.getFirstStationTop().getX()
            		+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
            		SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]+2), Orientation.NORTH));
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
    	
    			newRobots.add(new NewScenarioRobot(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2), Orientation.NORTH));
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
    	
    			newRobots.add(new NewScenarioRobot(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2), Orientation.NORTH));
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
    	
    			newRobots.add(new NewScenarioRobot(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]+2), Orientation.NORTH));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }
    
    private class HardcoreScenario extends Scenario{
    	public HardcoreScenario() {
    		name = "Hardcore mode";
    	}
    	
    	@Override
    	public List<NewRobot> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<NewRobot> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse()*SimulatorConfig.getBatteriesPerStation(); i++) {
    		//for(int i=0; i<3; i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new NewScenarioRobot(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]+2), Orientation.NORTH, true));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }
    
    private class OppositeRobotsOnCrossroadScenario extends TestScenario {

        public OppositeRobotsOnCrossroadScenario() {
            name = "Opposite Robots on Crossroad";
            description = "Opposite Robots on Crossroad";
        }

        @Override
        public List<NewRobot> initializeScenario() {
            List<NewRobot> newRobots = new ArrayList<>();
            List<Position> targetsRobotOne = new ArrayList<>();
            targetsRobotOne.add(new Position(0,5));
            List<Position> targetsRobotThree = new ArrayList<>();
            targetsRobotThree.add(new Position(9,4));
            newRobots.add(new NewTestRobot(new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
            newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree));
            return newRobots;
        }
    }
    
    private class TwoRobotsOnCrossroadScenario extends TestScenario {

        public TwoRobotsOnCrossroadScenario() {
            name = "2 Robots on Crossroad";
            description = "2 Robots on Crossroad";
        }

        @Override
        public List<NewRobot> initializeScenario() {
            List<NewRobot> newRobots = new ArrayList<>();
            List<Position> targetsRobotOne = new ArrayList<>();
            targetsRobotOne.add(new Position(0,5));
            List<Position> targetsRobotTwo = new ArrayList<>();
            targetsRobotTwo.add(new Position(5,9));
            newRobots.add(new NewTestRobot(new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
            newRobots.add(new NewTestRobot(new Position(5, 3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo));
            return newRobots;
        }
    }
    
    private class ThreeRobotsOnCrossroadScenario extends TestScenario {

        public ThreeRobotsOnCrossroadScenario() {
            name = "3 Robots on Crossroad";
            description = "3 Robots on Crossroad";
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
            newRobots.add(new NewTestRobot(new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
            newRobots.add(new NewTestRobot(new Position(5, 3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo));
            newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree));
            return newRobots;
        }
    }

    private class FourRobotsOnCrossroadScenario extends TestScenario {

        public FourRobotsOnCrossroadScenario() {
            name = "4 Robots on Crossroad";
            description = "4 Robots on Crossroad";
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
    		description = "Drive to end of the queue";
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
    		description = "Drive to loading position";
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
    		description = "Drive to unloading position";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targets = new ArrayList<>();
             targets.add(new Position(3,10));
             newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets));
             return newRobots;
         }
    }
    
    private class MiddleRoute extends TestScenario {
    	
    	public MiddleRoute() {
    		name = "Drive medium long route";
    		description = "Drive medium long route";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targets = new ArrayList<>();
             //targets.add(new Position(2,0));
             targets.add(new Position(3,10));
             targets.add(new Position(1,0));
             targets.add(new Position(2,0));
             targets.add(new Position(6,10));
             newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets));
             return newRobots;
         }
    }
    
	private class MiddleRouteTwoRobots extends TestScenario {
	    	
	    	public MiddleRouteTwoRobots() {
	    		name = "2 Robots same to same";
	    		description = "2 Robots from same stations to same destinations";
	    	}
	    	
	    	 @Override
	         public List<NewRobot> initializeScenario() {
	             List<NewRobot> newRobots = new ArrayList<>();
	             List<Position> targets = new ArrayList<>();
	             List<Position> targets2 = new ArrayList<>();
	             targets.add(new Position(3,10));
	             targets.add(new Position(1,0));
	             targets.add(new Position(2,0));
	             targets.add(new Position(6,10));
	             targets.add(new Position(4,0));
	             targets.add(new Position(5,0));
	
	             targets2.add(new Position(2,0));
	             targets2.add(new Position(3,10));
	             targets2.add(new Position(1,0));
	             targets2.add(new Position(2,0));
	             targets2.add(new Position(6,10));
	             targets2.add(new Position(7,0));
	             targets2.add(new Position(8,0));
	             newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets));
	             newRobots.add(new NewTestRobot(new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH, targets2));
	             return newRobots;
	         }
	    }

	private class MiddleRouteTwoRobots2 extends TestScenario {
		
		public MiddleRouteTwoRobots2() {
			name = "2 Robots different to same";
			description = "2 Robots from different stations to same destinations";
		}
		
		 @Override
	     public List<NewRobot> initializeScenario() {
	         List<NewRobot> newRobots = new ArrayList<>();
	         List<Position> targets = new ArrayList<>();
	         List<Position> targets2 = new ArrayList<>();
	         targets.add(new Position(3,10));
	         targets.add(new Position(1,0));
	         targets.add(new Position(2,0));
	         targets.add(new Position(6,10));
	         targets.add(new Position(4,0));
	         targets.add(new Position(5,0));
	
	         targets2.add(new Position(3,10));
	         targets2.add(new Position(4,0));
	         targets2.add(new Position(5,0));
	         targets2.add(new Position(6,10));
	         targets2.add(new Position(7,0));
	         targets2.add(new Position(8,0));
	         newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets));
	         newRobots.add(new NewTestRobot(new Position(5, 0), RobotState.TO_LOADING, Orientation.NORTH, targets2));
	         return newRobots;
	     }
	}
	
private class MiddleRouteThreeRobots extends TestScenario {
		
		public MiddleRouteThreeRobots() {
			name = "3 Robots different to same";
			description = "3 Robots from different stations to same destinations";
		}
		
		 @Override
	     public List<NewRobot> initializeScenario() {
	         List<NewRobot> newRobots = new ArrayList<>();
	         List<Position> targets = new ArrayList<>();
	         List<Position> targets2 = new ArrayList<>();
	         List<Position> targets3 = new ArrayList<>();
	         targets.add(new Position(3,10));
	         targets.add(new Position(1,0));
	         targets.add(new Position(2,0));
	         targets.add(new Position(6,10));
	         targets.add(new Position(4,0));
	         targets.add(new Position(5,0));
	
	         targets2.add(new Position(3,10));
	         targets2.add(new Position(4,0));
	         targets2.add(new Position(5,0));
	         targets2.add(new Position(6,10));
	         targets2.add(new Position(7,0));
	         targets2.add(new Position(8,0));
	         
	         targets3.add(new Position(3,10));
	         targets3.add(new Position(7,0));
	         targets3.add(new Position(8,0));
	         targets3.add(new Position(6,10));
	         targets3.add(new Position(10,0));
	         targets3.add(new Position(11,0));
	         newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets));
	         newRobots.add(new NewTestRobot(new Position(5, 0), RobotState.TO_LOADING, Orientation.NORTH, targets2));
	         newRobots.add(new NewTestRobot(new Position(8, 0), RobotState.TO_LOADING, Orientation.NORTH, targets3));
	         return newRobots;
	     }
	}
	
private class MiddleRouteTwoRobots3 extends TestScenario {
		
		public MiddleRouteTwoRobots3() {
			name = "4 Robots different to same";
			description = "2x2 Robots from different stations to same destinations";
		}
		
		 @Override
	     public List<NewRobot> initializeScenario() {
	         List<NewRobot> newRobots = new ArrayList<>();
	         List<Position> targets = new ArrayList<>();
	         List<Position> targets2 = new ArrayList<>();
	         List<Position> targets3 = new ArrayList<>();
	         List<Position> targets4 = new ArrayList<>();
	         targets.add(new Position(3,10));
	         targets.add(new Position(1,0));
	         targets.add(new Position(2,0));
	         targets.add(new Position(6,10));
	         targets.add(new Position(4,0));
	         targets.add(new Position(5,0));
	         targets2.add(new Position(2,0));
	         targets2.add(new Position(3,10));
	         targets2.add(new Position(1,0));
	         targets2.add(new Position(2,0));
	         targets2.add(new Position(6,10));
	         targets2.add(new Position(7,0));
	         targets2.add(new Position(8,0));
	
	         targets3.add(new Position(3,10));
	         targets3.add(new Position(4,0));
	         targets3.add(new Position(5,0));
	         targets3.add(new Position(6,10));
	         targets3.add(new Position(10,0));
	         targets3.add(new Position(11,0));
	         targets4.add(new Position(5,0));
	         targets4.add(new Position(3,10));
	         targets4.add(new Position(4,0));
	         targets4.add(new Position(5,0));
	         targets4.add(new Position(6,10));
	         targets4.add(new Position(13,0));
	         targets4.add(new Position(14,0));
	         newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets));
	         newRobots.add(new NewTestRobot(new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH, targets2));
	         newRobots.add(new NewTestRobot(new Position(5, 0), RobotState.TO_LOADING, Orientation.NORTH, targets3));
	         newRobots.add(new NewTestRobot(new Position(5, -1), RobotState.TO_QUEUE, Orientation.NORTH, targets4));
	         return newRobots;
	     }
	}
    
    private class LongRoute extends TestScenario {
    	
    	public LongRoute() {
    		name = "Drive long route";
    		description = "Drive long route";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targets = new ArrayList<>();
             targets.add(new Position(3,10));
             targets.add(new Position(1,0));
             targets.add(new Position(2,0));
             targets.add(new Position(6,10));
             targets.add(new Position(7,0));
             targets.add(new Position(8,0));
             targets.add(new Position(12,10));
             newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets));
             return newRobots;
         }
    }
    
    private class HandleTwoRobotsInStation extends TestScenario {
    	
    	public HandleTwoRobotsInStation() {
    		name = "Two robots in station";
    		description = "Handle two robots in the same station";
    	}
    	
    	 @Override
         public List<NewRobot> initializeScenario() {
             List<NewRobot> newRobots = new ArrayList<>();
             List<Position> targetsRobotOne = new ArrayList<>();
             List<Position> targetsRobotTwo = new ArrayList<>();
             targetsRobotOne.add(new Position(2,0));
             targetsRobotOne.add(new Position(3,10));
             targetsRobotTwo.add(new Position(2,0));
             targetsRobotTwo.add(new Position(3,13));
             newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_BATTERY, Orientation.EAST, targetsRobotOne));
             newRobots.add(new NewTestRobot(new Position(0, -3), RobotState.TO_BATTERY, Orientation.EAST, targetsRobotTwo));
             return newRobots;
         }
    }
    
    private class HandleThreeRobotsInStation extends TestScenario {
    	
    	public HandleThreeRobotsInStation() {
    		name = "Three robots in station";
    		description = "Handle three robots in the same station";
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

    private class OppositeRobots extends TestScenario {
        public OppositeRobots() { 
        	name = "Opposite Robots"; 
        	description = "Opposite Robots (typical Deadlock)";
        }

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
    
    private class ExplosionTest extends TestScenario {
        public ExplosionTest() { 
        	name = "Explosion Test"; 
        	description = "Test scenario to test the explosion animation";
        }

        @Override
        protected List<NewRobot> initializeScenario() {
            List<NewRobot> newRobots = new ArrayList<>();
            List<Position> targetsRobotOne = new ArrayList<>();
            List<Position> targetsRobotTwo = new ArrayList<>();
            targetsRobotOne.add(new Position(1,0));
            targetsRobotTwo.add(new Position(3,4));
            newRobots.add(new NewTestRobot(new Position(3, 5), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne));
            newRobots.add(new NewTestRobot(new Position(3, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotTwo));
            return newRobots;
        }
    }
    
    private class NewTestRobot extends NewRobot {

        private Position position;
        private RobotState state;
        private List<Position> targets = new ArrayList<Position>();
        private Orientation facing;

        public NewTestRobot(Position position, RobotState startingState, Orientation facing, List<Position> targets) {
            this.position = position;
            this.state = startingState;
            this.targets = targets;
            this.facing = facing;
        }

        @Override
        public Robot register(SimulationWorld simulationWorld) {
            return simulationWorld.addRobotAtPosition(position, state, facing, targets);
        }
    }
    
    private class NewScenarioRobot extends NewRobot{
    	private Position position;
    	private Orientation facing;
    	private boolean inHardcoreMode = false;
    	
    	public NewScenarioRobot(Position position, Orientation facing) {
    		this.position = position;
    		this.facing = facing;
    	}
    	
    	public NewScenarioRobot(Position position, Orientation facing, boolean inHardcoreMode) {
			this.position = position;
			this.facing = facing;
			this.inHardcoreMode = inHardcoreMode;
		}

		@Override
    	public Robot register(SimulationWorld sim) {
    		return sim.addRobotInScenarioHPI2(position, facing, inHardcoreMode);
    	}
    }

	public void failCurrentTest() {
		currentTestFailed = true;
	}

	public boolean isRunningTest() {
		return isRunningTest;
	}

	public void setCollisionDetector(CollisionDetector collisionDetector) {
		this.collisionDetector = collisionDetector;
	}

	public Map<String,ArrayList<TestScenario>> getTestGroups() {
		return testGroups;
	}
}
