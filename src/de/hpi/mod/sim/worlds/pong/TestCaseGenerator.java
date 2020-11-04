package de.hpi.mod.sim.worlds.pong;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.TestScenario;


public class TestCaseGenerator {

    private static PongWorld world;
    

    public static Map<String, List<TestScenario>> getAllTestCases(PongWorld worldP) {
        Map<String, List<TestScenario>> testGroups = new LinkedHashMap<>();
        testGroups.put("Pong tests", generateTests(worldP));
        world = worldP;
        return testGroups;
    }



    private static List<TestScenario> generateTests(PongWorld world) {
    	
        // Start list of test scenarios
        List<TestScenario> testScenarios = new ArrayList<>();
        
        PaddleSpecification paddle = new PaddleSpecification(world);
        BallSpecification ball = new BallSpecification(0, -0.04 , 0, world, true);
        testScenarios.add(new ConcreteTestScenario("No Movement", "The ball starts at the height of the paddle.", 
                paddle, ball));
        
        BallSpecification ball5 = new BallSpecification(0.25, -0.008 , 0, world, true);
        testScenarios.add(new ConcreteTestScenario("Start above", "The ball starts over the initial paddle position.", 
                paddle, ball5));
        
        
        BallSpecification ball6 = new BallSpecification(-0.25, -0.008 , 0, world, true);
        testScenarios.add(new ConcreteTestScenario("Start below", "The ball starts below the initial paddle position.", 
                paddle, ball6));
        
        BallSpecification ball1 = new BallSpecification(0, -0.008, 0.003, world, true);
        testScenarios.add(new ConcreteTestScenario("Move Up", "The ball hits over the initial paddle position.", 
                paddle, ball1));
        
        BallSpecification ball2 = new BallSpecification(0, -0.008, -0.003, world, true);
        testScenarios.add(new ConcreteTestScenario("Move Down", "The ball hits beneath the initial paddle position.", 
                paddle, ball2));
        
        
        
        BallSpecification ball3 = new BallSpecification(0, -0.006, 0.005, world, true);
        testScenarios.add(new ConcreteTestScenario("Ball hits lower boundary", "The ball changes his direction", 
                paddle, ball3));
        
        
        
        BallSpecification ball4 = new BallSpecification(0, -0.006, -0.005, world, true);
        testScenarios.add(new ConcreteTestScenario("Ball hits upper boundary", "The ball changes his direction", 
                paddle, ball4));
        
        BallSpecification ball7 = new BallSpecification(0, -0.001, 0.003, world, true);
        testScenarios.add(new ConcreteTestScenario("Maximal Height", "Don't go above the maximal Height!",
        		paddle, ball7));

        BallSpecification ball8 = new BallSpecification(0, -0.001, -0.003, world, true);
        testScenarios.add(new ConcreteTestScenario("Minimal Height", "Don't go beneath the minimal Height!",
        		paddle, ball8));
        
        return testScenarios;
        
    }

    private static class ConcreteTestScenario extends TestScenario {
        List<EntitySpecification<?>> newEntities = new ArrayList<>();
        PaddleSpecification paddle1;
        BallSpecification ball;
        

        public ConcreteTestScenario(String name, String description, PaddleSpecification paddle1, BallSpecification ball) {
            this.name = name;
            this.description = description;
            this.newEntities = Arrays.asList(paddle1, ball);
            this.paddle1 = paddle1;
            this.ball = ball;
        }

        @Override
        public List<EntitySpecification<?>> getScenarioEntities() {
            return newEntities;
        }
    }

}
