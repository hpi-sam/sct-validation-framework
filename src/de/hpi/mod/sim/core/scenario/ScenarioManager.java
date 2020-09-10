package de.hpi.mod.sim.core.scenario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.view.SimulatorFrame;

public class ScenarioManager {
	private List<Scenario> scenarios;
	private Scenario clear = new EmptyScenario();
	private Scenario currentScenario;
	private Map<String, List<TestScenario>> testGroups;
	private List<ITestScenarioListener> listeners = new ArrayList<>();

	private Queue<TestScenario> testsToRun = new LinkedList<>();
	private boolean runningAllTests = false;

	private boolean currentTestFailed = false;
	private boolean isRunningTest = false;

	private String failReason = "";

	private TestScenario activeTest = null;

	SimulatorFrame frame;
	private List<TestScenario> tests = new ArrayList<>();
	private World world;

	public ScenarioManager(World world) {
		this.world = world;
		this.frame = world.getFrame();
		scenarios = world.getScenarios();
		testGroups = world.getTestGroups();
		initializeTestList();
	}

	private void initializeTestList() {
		for (String key : testGroups.keySet()) {
			for (TestScenario test : testGroups.get(key)) {
				tests.add(test);
			}
		}
	}

	private void updateDetectors() {
		List<? extends Entity> entities = world.getEntities();
		for (Detector detector : world.getDetectors())
			detector.update(entities);
	}

    private void runScenario(Scenario scenario, boolean isTest) {
    	world.runScenario(scenario);
        
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

	public void addTestScenarioListener(ITestScenarioListener listener) {
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
				world.deactivateDetectors();
				for (ITestScenarioListener listener : listeners) {
					listener.failTest(activeTest);
				}
				activeTest.notifyFailToUser(frame, failReason);
				currentTestFailed = false;
				isRunningTest = false;
				activeTest = null;
				if (runningAllTests)
					runNextTest();
			} else if (activeTest.isPassed()) {
				world.deactivateDetectors();
				for (ITestScenarioListener listener : listeners) {
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
        public List<EntitySpecification<?>> initializeScenario() {
            return new ArrayList<>();
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
