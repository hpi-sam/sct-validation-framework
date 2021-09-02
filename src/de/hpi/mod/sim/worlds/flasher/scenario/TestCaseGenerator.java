package de.hpi.mod.sim.worlds.flasher.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.TestScenario;
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



    private static List<TestScenario> generateTests(FlashWorld world) {

        // Start list of test scenarios
        List<TestScenario> testScenarios = new ArrayList<>();

        LightBulbSpecification bulb1 = new LightBulbSpecification(world);
        TaskProviderSpecification provider1 = new TaskProviderSpecification(Arrays.asList(new FlashTask(0, 10000)), false, world);
        TestCaseExpectation expectation1 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 500, false)));
        testScenarios.add(new ConcreteTestScenario("Energy saving", "The bulb is initially off and doesn't light if called with start(0).", 
                bulb1, provider1, expectation1));

        LightBulbSpecification bulb2 = new LightBulbSpecification(world);
        TaskProviderSpecification provider2 = new TaskProviderSpecification(Arrays.asList(new FlashTask(1, 10000)), false, world);
        TestCaseExpectation expectation2 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        testScenarios.add(new ConcreteTestScenario("Can be turned on", "The bulb turns on if called with start(1)",
                bulb2, provider2, expectation2));

        LightBulbSpecification bulb3 = new LightBulbSpecification(world);
        TaskProviderSpecification provider3 = new TaskProviderSpecification(Arrays.asList(new FlashTask(1, 10000)), false, world);
        TestCaseExpectation expectation3 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        testScenarios.add(new ConcreteTestScenario("Turns off again", "The bulb turns off after flashing.",
                bulb3, provider3, expectation3));

        LightBulbSpecification bulb4 = new LightBulbSpecification(world);
        TaskProviderSpecification provider4 = new TaskProviderSpecification(Arrays.asList(new FlashTask(2, 10000)), false, world);
        TestCaseExpectation expectation4 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        testScenarios.add(new ConcreteTestScenario("Two flashes", "The bulb flashes two times after called with start(2).", 
        		bulb4, provider4, expectation4));

        LightBulbSpecification bulb5 = new LightBulbSpecification(world);
        TaskProviderSpecification provider5 = new TaskProviderSpecification(Arrays.asList(new FlashTask(5, 10000)), false, world);
        TestCaseExpectation expectation5 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        testScenarios.add(new ConcreteTestScenario("Five flashes", "The bulb flashes five times after called with start(5).", 
        		bulb5, provider5, expectation5));
                
        LightBulbSpecification bulb6 = new LightBulbSpecification(world);
        TaskProviderSpecification provider6 = new TaskProviderSpecification(Arrays.asList(new FlashTask(2, 10000)), false, world);
        TestCaseExpectation expectation6 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        testScenarios.add(new ConcreteTestScenario("Correct flashing time", "The bulb flashes have a duration of 500ms.", 
        		bulb6, provider6, expectation6));

        LightBulbSpecification bulb7 = new LightBulbSpecification(world);
        TaskProviderSpecification provider7 = new TaskProviderSpecification(Arrays.asList(new FlashTask(0, 10000)), false, world);
        TestCaseExpectation expectation7 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        testScenarios.add(new ConcreteTestScenario("Correct pausing time", "The pauses between the flashes have a duration of 500ms.", 
        		bulb7, provider7, expectation7));
                
        LightBulbSpecification bulb8 = new LightBulbSpecification(world);
        TaskProviderSpecification provider8 = new TaskProviderSpecification(Arrays.asList(new FlashTask(2, 2000), new FlashTask(4, 4000), new FlashTask(1, 2000)), false, world);
        TestCaseExpectation expectation8 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        testScenarios.add(new ConcreteTestScenario("Multiple start calls", "The bulb can react to multiple, sequential start calls (start(2), start(4), start(1))",
        		bulb8, provider8, expectation8));
        
        // Return list of test scenarios
        return testScenarios;
    }

    private static class ConcreteTestScenario extends TestScenario {
    	
        List<EntitySpecification<?>> newEntities = new ArrayList<>();
        TestCaseExpectation expectedObservation;
        
        public ConcreteTestScenario(String name, String description, LightBulbSpecification bulb, TaskProviderSpecification provider, TestCaseExpectation expectations) {
            this.name = name;
            this.description = description;
            this.newEntities = Arrays.asList(bulb, provider);
            this.expectedObservation = expectations;
        }

        @Override
        public List<EntitySpecification<?>> getScenarioEntities() {
            return newEntities;
        }
    }

}
