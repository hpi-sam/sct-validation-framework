package de.hpi.mod.sim.worlds.flasher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.TestScenario;

public class TestCaseGenerator {

    private static FlashWorld world;

    public static Map<String, List<TestScenario>> getAllTestCases(FlashWorld worldP) {
        Map<String, List<TestScenario>> testGroups = new LinkedHashMap<>();
        testGroups.put("Flash tests", generateTests(worldP));
        world = worldP;
        return testGroups;
    }



    private static List<TestScenario> generateTests(FlashWorld world) {

        // Start list of test scenarios
        List<TestScenario> testScenarios = new ArrayList<>();

        Bulb bulb = new Bulb();
        Starter starter = new Starter(bulb, Arrays.asList(0), Arrays.asList(10000f));
        testScenarios.add(new ConcreteTestScenario("Energy saving", "The bulb is initially off and doesn't light if called with start(0).", 
                bulb, starter));

        Bulb bulb2 = new Bulb();
        Starter starter2 = new Starter(bulb2, Arrays.asList(1), Arrays.asList(10000f));
        testScenarios.add(new ConcreteTestScenario("Can be turned on", "The bulb turns on if called with start(1)",
                bulb2, starter2));

        Bulb bulb3 = new Bulb();
        bulb3.setHasToBeOffForTests(true);
        Starter starter3 = new Starter(bulb3, Arrays.asList(1), Arrays.asList(10000f));
        testScenarios.add(new ConcreteTestScenario("Turns off again", "The bulb turns off after flashing.",
                bulb3, starter3));


        Bulb bulb4 = new Bulb();
        bulb4.setHasToBeOffForTests(true);
        Starter starter4 = new Starter(bulb4, Arrays.asList(2), Arrays.asList(10000f));
        testScenarios.add(
                new ConcreteTestScenario("Two flashes", "The bulb flashes two times after called with start(2).", bulb4, starter4));

        Bulb bulb5 = new Bulb();
        bulb5.setHasToBeOffForTests(true);
        Starter starter5 = new Starter(bulb5, Arrays.asList(5), Arrays.asList(10000f));
        testScenarios.add(new ConcreteTestScenario("Five flashes",
                "The bulb flashes five times after called with start(5).", bulb5, starter5));
                
        Bulb bulb6 = new Bulb();
        bulb6.setHasToBeOffForTests(true);
        bulb6.setCheckOnTimeForTests(true);
        Starter starter6 = new Starter(bulb6, Arrays.asList(2), Arrays.asList(10000f));
        testScenarios.add(new ConcreteTestScenario("Correct flashing time",
                "The bulb flashes have a duration of 500ms.", bulb6, starter6));

        Bulb bulb7 = new Bulb();
        bulb7.setCheckOffTimeForTests(true);
        Starter starter7 = new Starter(bulb6, Arrays.asList(3), Arrays.asList(10000f));
        testScenarios.add(new ConcreteTestScenario("Correct pausing time",
                "The pauses between the flashes have a duration of 500ms.", bulb7, starter7));
                
        Bulb bulb8 = new Bulb();
        Starter starter8 = new Starter(bulb6, Arrays.asList(2, 4, 1), Arrays.asList(5000f, 7000f, 10000f));
        testScenarios.add(new ConcreteTestScenario("Multiple start calls",
                "The bulb can react to multiple, sequential start calls (start(2), start(4), start(1))", bulb8, starter8));
        return testScenarios;
    }

    private static class ConcreteTestScenario extends TestScenario {
        List<EntitySpecification<?>> newEntities = new ArrayList<>();
        Bulb bulb;
        Starter starter;

        public ConcreteTestScenario(String name, String description, Bulb bulb, Starter starter) {
            this.name = name;
            this.description = description;
            this.newEntities = Arrays.asList(bulb, starter);
            this.starter = starter;
            this.bulb = bulb;
        }

        @Override
        public List<EntitySpecification<?>> initializeScenario() {
            world.setBulb(bulb);
            world.setStarter(starter);
            return newEntities;
        }
    }

}
