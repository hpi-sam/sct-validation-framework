package de.hpi.mod.sim.worlds.flasher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.worlds.flasher.entities.LightBulbSpecification;
import de.hpi.mod.sim.worlds.flasher.entities.StarterSpecification;

public class TestCaseGenerator {

    public static Map<String, List<TestScenario>> getAllTestCases(FlashWorld worldP) {
        Map<String, List<TestScenario>> testGroups = new LinkedHashMap<>();
        testGroups.put("Flash tests", generateTests(worldP));
        return testGroups;
    }



    private static List<TestScenario> generateTests(FlashWorld world) {

        // Start list of test scenarios
        List<TestScenario> testScenarios = new ArrayList<>();

        LightBulbSpecification bulb = new LightBulbSpecification(true, false, false, world);
        StarterSpecification starter = new StarterSpecification(Arrays.asList(0), Arrays.asList(10000f), false, world);
        testScenarios.add(new ConcreteTestScenario("Energy saving", "The bulb is initially off and doesn't light if called with start(0).", 
                bulb, starter));

        LightBulbSpecification bulb2 = new LightBulbSpecification(false, false, false, world);
        StarterSpecification starter2 = new StarterSpecification(Arrays.asList(1), Arrays.asList(10000f), false, world);
        testScenarios.add(new ConcreteTestScenario("Can be turned on", "The bulb turns on if called with start(1)",
                bulb2, starter2));

        LightBulbSpecification bulb3 = new LightBulbSpecification(true, false, false, world);
        StarterSpecification starter3 = new StarterSpecification(Arrays.asList(1), Arrays.asList(10000f), false, world);
        testScenarios.add(new ConcreteTestScenario("Turns off again", "The bulb turns off after flashing.",
                bulb3, starter3));

        LightBulbSpecification bulb4 = new LightBulbSpecification(true, false, false, world);
        StarterSpecification starter4 = new StarterSpecification(Arrays.asList(2), Arrays.asList(10000f), false, world);
        testScenarios.add(
                new ConcreteTestScenario("Two flashes", "The bulb flashes two times after called with start(2).", bulb4, starter4));

        LightBulbSpecification bulb5 = new LightBulbSpecification(true, false, false, world);
        StarterSpecification starter5 = new StarterSpecification(Arrays.asList(5), Arrays.asList(10000f), false, world);
        testScenarios.add(new ConcreteTestScenario("Five flashes",
                "The bulb flashes five times after called with start(5).", bulb5, starter5));
                
        LightBulbSpecification bulb6 = new LightBulbSpecification(true, true, false, world);
        StarterSpecification starter6 = new StarterSpecification(Arrays.asList(2), Arrays.asList(10000f), false, world);
        testScenarios.add(new ConcreteTestScenario("Correct flashing time",
                "The bulb flashes have a duration of 500ms.", bulb6, starter6));

        LightBulbSpecification bulb7 = new LightBulbSpecification(false, false, true, world);
        StarterSpecification starter7 = new StarterSpecification(Arrays.asList(3), Arrays.asList(10000f), false, world);
        testScenarios.add(new ConcreteTestScenario("Correct pausing time",
                "The pauses between the flashes have a duration of 500ms.", bulb7, starter7));
                
        LightBulbSpecification bulb8 = new LightBulbSpecification(false, false, false, world);
        StarterSpecification starter8 = new StarterSpecification(Arrays.asList(2,4,1), Arrays.asList(5000f, 7000f,
                        10000f), false, world);
        testScenarios.add(new ConcreteTestScenario("Multiple start calls",
                "The bulb can react to multiple, sequential start calls (start(2), start(4), start(1))", bulb8, starter8));
        return testScenarios;
    }

    private static class ConcreteTestScenario extends TestScenario {
        List<EntitySpecification<?>> newEntities = new ArrayList<>();

        public ConcreteTestScenario(String name, String description, LightBulbSpecification bulb, StarterSpecification starter) {
            this.name = name;
            this.description = description;
            this.newEntities = Arrays.asList(bulb, starter);
        }

        @Override
        public List<EntitySpecification<?>> getScenarioEntities() {
            return newEntities;
        }
    }

}
