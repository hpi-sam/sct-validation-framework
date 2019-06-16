package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
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
    InvalidPositionDetector invalidPositionDetector;
    DeadlockDetector deadlockDetector;
    DriveSimFrame frame;
	private List<TestScenario> tests = new ArrayList<>();
	private InvalidUnloadingDetector invalidUnloadingDetector;
	private TestCaseGenerator testCaseGenerator;
	private InvalidTurningDetector invalidTurningDetector;


    public ScenarioManager(SimulationWorld world, CollisionDetector collisionDetector, DriveSimFrame frame) {
        this.world = world;
        this.collisionDetector = collisionDetector;
        this.frame = frame;
        this.testCaseGenerator = new TestCaseGenerator(testGroups);
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
		testGroups = testCaseGenerator.getAllTestCases();
	}

    private void runScenario(Scenario scenario, boolean isTest) {
    	frame.allowRunning();
    	frame.setResizable(scenario.isResizable());
    	world.resetZoom();
		world.resetOffset();
		world.resetHighlightedRobots();
        world.playScenario(scenario);
        world.releaseAllLocks();
        deadlockDetector.reactivate();
        collisionDetector.reset();
        invalidPositionDetector.reset();
        invalidUnloadingDetector.reset();
        invalidTurningDetector.reset();
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
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]+2), Orientation.NORTH, 1000));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
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

	public void setInvalidPositionDetector(InvalidPositionDetector invalidPositionDetector) {
		this.invalidPositionDetector = invalidPositionDetector;
	}

	public void setInvalidUnloadingDetector(InvalidUnloadingDetector invalidUnloadingDetector) {
		this.invalidUnloadingDetector = invalidUnloadingDetector;
		
	}

	public void setInvalidTurningDetector(InvalidTurningDetector invalidTurningDetector) {
		this.invalidTurningDetector = invalidTurningDetector;
		
	}
}
