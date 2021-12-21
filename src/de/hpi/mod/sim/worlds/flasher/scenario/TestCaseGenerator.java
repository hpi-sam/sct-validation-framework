package de.hpi.mod.sim.worlds.flasher.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;
import de.hpi.mod.sim.worlds.flasher.entities.FlashTask;
import de.hpi.mod.sim.worlds.flasher.entities.LightBulbSpecification;
import de.hpi.mod.sim.worlds.flasher.entities.TaskProviderSpecification;

public class TestCaseGenerator {

    public static Map<String, List<TestScenario>> getAllTestCases(FlashWorld worldP) {
        Map<String, List<TestScenario>> testGroups = new LinkedHashMap<>();
        testGroups.put("Flash tests", generateTests(worldP));
        return testGroups;
    }

    private static TestCaseExpectation.Expectation x_value(double time, boolean expectedLightBulbOn) {
		return new TestCaseExpectation.SingleValueExpectation(time, expectedLightBulbOn);
	}
	
	private static TestCaseExpectation.Expectation x_interval(double startTime, double endTime, boolean expectedLightBulbOn) {
		return new TestCaseExpectation.IntervalExpectation(startTime, endTime, expectedLightBulbOn);
	}
	
	private static TestCaseExpectation.Expectation x_sequence(double startTime, double endTime, boolean...expectedLightBulbSequence) {
		return new TestCaseExpectation.SequenceExpectation(startTime, endTime, expectedLightBulbSequence);
	}
	
	private static FlashTask t(int timesToBlink, double waitingTime) {
		return new FlashTask(timesToBlink, waitingTime);
	}
	
	private static FlashTask t(int timesToBlink, double preTaskWaitingTime, double taskTime) {
		return new FlashTask(timesToBlink, preTaskWaitingTime, taskTime);
	}
	
	private static List<FlashTask> t_list(FlashTask...flashTasks) {
		return new ArrayList<FlashTask>(Arrays.asList(flashTasks));
	}    
    
    private static List<TestScenario> generateTests(FlashWorld world) {

        // Start list of test scenarios
        List<TestScenario> testScenarios = new ArrayList<>();

        TestCaseExpectation expectation0 = new TestCaseExpectation(x_interval(0, 10000, false));
        LightBulbSpecification bulb0 = new LightBulbSpecification(world, expectation0);
        TaskProviderSpecification provider0 = new TaskProviderSpecification(t_list(), false, world);
        testScenarios.add(new ConcreteTestScenario("Energy saving 1", "The bulb is initially off. Test runs for 10 seconds.", bulb0, provider0));

        TestCaseExpectation expectation1 = new TestCaseExpectation(x_interval(0, 10000, false));
        LightBulbSpecification bulb1 = new LightBulbSpecification(world, expectation1);
        TaskProviderSpecification provider1 = new TaskProviderSpecification(t_list(t(0, 1000), t(0, 5000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Energy saving 2", "The bulb doesn't light if called with start(0). Test runs for 10 seconds.", bulb1, provider1));

        TestCaseExpectation expectation2 = new TestCaseExpectation(x_sequence(0, 1900, false), x_value(2100, true));
        LightBulbSpecification bulb2 = new LightBulbSpecification(world, expectation2);
        TaskProviderSpecification provider2 = new TaskProviderSpecification(t_list(t(1, 2000, 1000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Can be turned on", "The bulb turns on if called with start(1)", bulb2, provider2));

//        TestCaseExpectation expectation3 = new TestCaseExpectation(x(0, 4900, 100, false), 
//        														   x_value(5100, true), 
//        														   x(5900, 10000, 100, false));
//        LightBulbSpecification bulb3 = new LightBulbSpecification(world, expectation3);
//        TaskProviderSpecification provider3 = new TaskProviderSpecification(t_list(t(1, 2000, 10000)), false, world);
//        testScenarios.add(new ConcreteTestScenario("Turns off again", "The bulb turns off after flashing. Test runs for 10 seconds with start(1) in the middle.", bulb3, provider3));
//
//        TestCaseExpectation expectation4 = new TestCaseExpectation(x(0, 1900, 100, false), 
//        														   x(2100, 2400, 100, true), // Flash #1
//        														   x(2600, 2900, 100, false), 
//        														   x(3100, 3400, 100, true), // Flash #2
//        														   x(3600, 000, 100, false));
//        LightBulbSpecification bulb4 = new LightBulbSpecification(world, expectation4);
//        TaskProviderSpecification provider4 = new TaskProviderSpecification(t_list(t(2, 2000, 2000)), false, world);
//        testScenarios.add(new ConcreteTestScenario("Two flashes", "The bulb flashes two times after called with start(2).", bulb4, provider4));
//
//        TestCaseExpectation expectation5 = new TestCaseExpectation(x(0, 1900, 500, false), 
//																   x_value(2250, true), // Flash #1
//																   x_value(2750, false), 
//																   x_value(3750, true), // Flash #2
//																   x_value(3600, false), 
//																   x_value(4750, true), // Flash #3
//																   x_value(4600, false), 
//																   x_value(5750, true), // Flash #4
//																   x_value(5700, false), 
//																   x_value(6750, true), // Flash #5
//																   x(6600, 10000, 100, false));
//        LightBulbSpecification bulb5 = new LightBulbSpecification(world, expectation5);
//        TaskProviderSpecification provider5 = new TaskProviderSpecification(t_list(t(5, 2000, 8000)), false, world);
//        testScenarios.add(new ConcreteTestScenario("Five flashes", "The bulb flashes five times after called with start(5).", bulb5, provider5));
//
//        TestCaseExpectation expectation6 = new TestCaseExpectation(x(0, 1900, 100, false), 
//																   x(2100, 2400, 100, true), // Flash #1.1 (at 2000)
//																   x(2600, 2900, 100, false), 
//																   x(3100, 3400, 100, true), // Flash #1.2 (at 3000)
//																   x(3600, 5900, 100, false), 
//																   x(6100, 6400, 100, true), // Flash #2.1 (at 6000)
//																   x(6600, 6900, 100, false), 
//																   x(7100, 7400, 100, true), // Flash #2.2 (at 7000)
//																   x(7600, 7900, 100, false), 
//																   x(8100, 8400, 100, true), // Flash #2.3 (at 8000)
//																   x(8600, 8900, 100, false), 
//																   x(9100, 9400, 100, true), // Flash #2.4 (at 9000)
//																   x(9600, 11900, 100, false), 
//																   x(12100, 12400, 100, true), // Flash #3.1 (at 12000)
//																   x(12600, 14000, 100, false));
//        LightBulbSpecification bulb6 = new LightBulbSpecification(world, expectation6);
//        TaskProviderSpecification provider6 = new TaskProviderSpecification(t_list(t(2, 2000, 2000), t(4, 2000, 4000), t(1, 2000, 2000)), false, world);
//        testScenarios.add(new ConcreteTestScenario("Multiple start calls", "The bulb can react to multiple, sequential start calls (start(2), start(4), start(1))", bulb6, provider6));
        
        // Return list of test scenarios
        return testScenarios;
    }

    private static class ConcreteTestScenario extends TestScenario {
    	
        List<EntitySpecification<?>> newEntities = new ArrayList<>();
        TestCaseExpectation expectedObservation;
        
        public ConcreteTestScenario(String name, String description, LightBulbSpecification bulb, TaskProviderSpecification provider) {
            this.name = name;
            this.description = description;
            this.newEntities = Arrays.asList(bulb, provider);
        }

        @Override
        public List<EntitySpecification<?>> getScenarioEntities() {
            return newEntities;
        }
    }

}
