package de.hpi.mod.sim.env.testing.scenarios;

import de.hpi.mod.sim.env.Setting;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.simulation.SimulatorConfig;
import de.hpi.mod.sim.env.testing.Detector;
import de.hpi.mod.sim.env.testing.ITestListener;
import de.hpi.mod.sim.env.testing.RobotDescription;
import de.hpi.mod.sim.env.testing.Scenario;
import de.hpi.mod.sim.env.testing.tests.TestCaseGenerator;
import de.hpi.mod.sim.env.testing.tests.TestScenario;
import de.hpi.mod.sim.env.view.DriveSimFrame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class ScenarioManager {

	private Setting setting;
    private List<Scenario> scenarios = new ArrayList<>();
    private Scenario clear = new EmptyScenario();
    private Scenario currentScenario;
    private Map<String,List<TestScenario>> testGroups = new LinkedHashMap<>();
    private List<ITestListener> listeners = new ArrayList<>();
    
    private Queue<TestScenario> testsToRun = new LinkedList<>();
    private boolean runningAllTests = false;
    
	private boolean currentTestFailed = false;
	private boolean isRunningTest = false;
	
	private String failReason = "";
	
	private TestScenario activeTest = null;
	
    DriveSimFrame frame;
	private List<TestScenario> tests = new ArrayList<>();
	private TestCaseGenerator testCaseGenerator;


    public ScenarioManager(Setting setting) {
        this.setting = setting;
        this.frame = setting.getFrame();
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


	private void updateDetectors() {
		List<Robot> robots = setting.getRobots();
		for (Detector detector : setting.getDetectors())
			detector.update(robots);
	}

    private void runScenario(Scenario scenario, boolean isTest) {
    	setting.runScenario(scenario);
        
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
			if (currentTestFailed) {
				setting.resetDetectors(); //TODO: originally just deadlockDetector.deactivate(); Does it work?
				for (ITestListener listener : listeners) {
					listener.failTest(activeTest);
				}
				activeTest.notifyFailToUser(frame, failReason);
				currentTestFailed = false;
				isRunningTest = false;
				activeTest = null;
				if (runningAllTests)
					runNextTest();
			} else if (activeTest.isPassed()) {
				setting.resetDetectors(); // TODO: originally just deadlockDetector.deactivate(); Does it work?
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
		updateDetectors();
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
        public List<RobotDescription> initializeScenario() {
            return new ArrayList<>();
        }
    }
    
    private class OneRobotScenario extends Scenario{
    	public OneRobotScenario() {
    		name = "One Robot";
    	}
    	
    	@Override
    	public List<RobotDescription> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<RobotDescription> newRobots = new ArrayList<>();
    		
    		do {
    			station_number = ThreadLocalRandom.current().nextInt(maxStations);
    		} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    		ScenarioRobotDescription singleRobot = new ScenarioRobotDescription(new Position((SimulatorConfig.getFirstStationTop().getX()
            		+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
            		SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]+2), Orientation.NORTH);
    		singleRobot.setIsAlone(true);
            newRobots.add(singleRobot);
            return newRobots;
    	}
    }
    
    private class EasyScenario extends Scenario{
    	public EasyScenario() {
    		name = "Few Robots";
    	}
    	
    	@Override
    	public List<RobotDescription> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<RobotDescription> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse(); i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new ScenarioRobotDescription(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2), Orientation.NORTH));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }
    
    private class MediumScenario extends Scenario{
    	public MediumScenario() {
    		name = "Average number of Robots";
    	}
    	
    	@Override
    	public List<RobotDescription> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<RobotDescription> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse()*Math.ceil
    				(((float)SimulatorConfig.getRecommendedRobotsPerStation())/2); i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new ScenarioRobotDescription(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2), Orientation.NORTH));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }
    
    private class HardScenario extends Scenario{
    	public HardScenario() {
    		name = "Many Robots";
    	}
    	
    	@Override
    	public List<RobotDescription> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<RobotDescription> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse()*SimulatorConfig.getRecommendedRobotsPerStation(); i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new ScenarioRobotDescription(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]+2), Orientation.NORTH));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }
    
    private class HardcoreScenario extends Scenario{
    	public HardcoreScenario() {
    		name = "Many Robots + Reaction Delay [Extreme mode]";
    	}
    	
    	@Override
    	public List<RobotDescription> initializeScenario(){
    		int maxStations = SimulatorConfig.getChargingStationsInUse();
    		int station_number;
    		int[] robotsAtStations = new int[maxStations];
    		List<RobotDescription> newRobots = new ArrayList<>();
    		
    		for(int i=0; i<SimulatorConfig.getChargingStationsInUse()*SimulatorConfig.getRecommendedRobotsPerStation(); i++) {
    		//for(int i=0; i<3; i++) {
    			do {
    				station_number = ThreadLocalRandom.current().nextInt(maxStations);
    			} while(robotsAtStations[station_number] >= SimulatorConfig.getMaxRobotsPerStation());
    	
    			newRobots.add(new ScenarioRobotDescription(new Position((SimulatorConfig.getFirstStationTop().getX()
    					+ station_number * SimulatorConfig.getSpaceBetweenChargingStations())+2, 
    					SimulatorConfig.getFirstStationTop().getY() - robotsAtStations[station_number]+2), Orientation.NORTH, 1000));
    			robotsAtStations[station_number] ++;
    		}
            return newRobots;
    	}
    }

	public void failCurrentTest(String reason) {
		failReason = reason;
		currentTestFailed = true;
	}

	public boolean isRunningTest() {
		return isRunningTest;
	}


	public Map<String,List<TestScenario>> getTestGroups() {
		return testGroups;
	}
}
