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

        TestCaseExpectation expectation0 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 500, false)));
        LightBulbSpecification bulb0 = new LightBulbSpecification(world, expectation0);
        TaskProviderSpecification provider0 = new TaskProviderSpecification(new ArrayList<>(), false, world);
        testScenarios.add(new ConcreteTestScenario("Energy saving 1", "The bulb is initially off.", bulb0, provider0));

        TestCaseExpectation expectation1 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 500, false)));
        LightBulbSpecification bulb1 = new LightBulbSpecification(world, expectation1);
        TaskProviderSpecification provider1 = new TaskProviderSpecification(Arrays.asList(new FlashTask(0, 10000), new FlashTask(0, 5000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Energy saving 2", "The bulb doesn't light if called with start(0).", bulb1, provider1));

        TestCaseExpectation expectation2 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 9900, 500, false),
        		new TestCaseExpectationGenerator(10100, true)));
        LightBulbSpecification bulb2 = new LightBulbSpecification(world, expectation2);
        TaskProviderSpecification provider2 = new TaskProviderSpecification(Arrays.asList(new FlashTask(1, 10000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Can be turned on", "The bulb turns on if called with start(1)", bulb2, provider2));

        TestCaseExpectation expectation3 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 9900, 500, false),
        		new TestCaseExpectationGenerator(10100, true),
        		new TestCaseExpectationGenerator(0, 9900, 500, false)));
        LightBulbSpecification bulb3 = new LightBulbSpecification(world, expectation3);
        TaskProviderSpecification provider3 = new TaskProviderSpecification(Arrays.asList(new FlashTask(1, 10000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Turns off again", "The bulb turns off after flashing.", bulb3, provider3));

        TestCaseExpectation expectation4 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        LightBulbSpecification bulb4 = new LightBulbSpecification(world, expectation4);
        TaskProviderSpecification provider4 = new TaskProviderSpecification(Arrays.asList(new FlashTask(2, 10000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Two flashes", "The bulb flashes two times after called with start(2).", bulb4, provider4));

        TestCaseExpectation expectation5 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        LightBulbSpecification bulb5 = new LightBulbSpecification(world, expectation5);
        TaskProviderSpecification provider5 = new TaskProviderSpecification(Arrays.asList(new FlashTask(5, 10000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Five flashes", "The bulb flashes five times after called with start(5).", bulb5, provider5));

        TestCaseExpectation expectation6 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        LightBulbSpecification bulb6 = new LightBulbSpecification(world, expectation6);
        TaskProviderSpecification provider6 = new TaskProviderSpecification(Arrays.asList(new FlashTask(2, 10000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Correct flashing time", "The bulb flashes have a duration of 500ms.", bulb6, provider6));

        TestCaseExpectation expectation7 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        LightBulbSpecification bulb7 = new LightBulbSpecification(world, expectation7);
        TaskProviderSpecification provider7 = new TaskProviderSpecification(Arrays.asList(new FlashTask(0, 10000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Correct pausing time", "The pauses between the flashes have a duration of 500ms.", bulb7, provider7));

        TestCaseExpectation expectation8 = TestCaseExpectation.createExpectationFromGenerators(Arrays.asList(
        		new TestCaseExpectationGenerator(0, 20000, 1000, false)));
        LightBulbSpecification bulb8 = new LightBulbSpecification(world, expectation8);
        TaskProviderSpecification provider8 = new TaskProviderSpecification(Arrays.asList(new FlashTask(2, 2000), new FlashTask(4, 4000), new FlashTask(1, 2000)), false, world);
        testScenarios.add(new ConcreteTestScenario("Multiple start calls", "The bulb can react to multiple, sequential start calls (start(2), start(4), start(1))", bulb8, provider8));
        
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
