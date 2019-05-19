package de.hpi.mod.sim.env.view.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot.RobotState;
import de.hpi.mod.sim.env.view.model.NewRobot;
import de.hpi.mod.sim.env.view.model.TestScenario;

public class TestCaseGenerator {
	
	private Random rand;
	private Map<String, ArrayList<TestScenario>> testGroups;


	public TestCaseGenerator(Map<String, ArrayList<TestScenario>> testGroups) {
		rand = new Random();
		this.testGroups = testGroups;
	}
	
	public Map<String, ArrayList<TestScenario>> getAllTestCases(){
		testGroups.put("Simple crossroad", generateSimpleCrossroadTests());
		return testGroups;
	}
	
	private ArrayList<TestScenario>  generateSimpleCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		testScenarios.addAll(generateEnteringCrossroadTests());
		testScenarios.addAll(generateDrivingFromNorthCrossroadTests());
		testScenarios.addAll(generateDrivingFromEastCrossroadTests());
		testScenarios.addAll(generateDrivingFromWestCrossroadTests());
		testScenarios.addAll(generateDrivingFromSouthCrossroadTests());
		return testScenarios;
	}

	private ArrayList<TestScenario> generateDrivingFromSouthCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
		List<Position> targetsRobotOne = new ArrayList<>();
		targetsRobotOne.add(new Position(-8+biasX*3,6+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("South-south", "Driving crossroad from south to south", newRobots));
		return testScenarios;
	}

	private ArrayList<TestScenario> generateDrivingFromWestCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
		List<Position> targetsRobotOne = new ArrayList<>();
		targetsRobotOne.add(new Position(-9+biasX*3,8+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("West-west", "Driving crossroad from west to west", newRobots));
		
		biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,6+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("West-south", "Driving crossroad from west to south", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,9+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("West-north", "Driving crossroad from west to north", newRobots));
		return testScenarios;
	}

	private ArrayList<TestScenario> generateDrivingFromEastCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
		List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+biasX*3,7+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("East-east", "Driving crossroad from east to east", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,6+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("East-south", "Driving crossroad from east to south", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,9+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("East-north", "Driving crossroad from east to north", newRobots));
		return testScenarios;
	}

	private ArrayList<TestScenario> generateDrivingFromNorthCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
		List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,9+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("North-north", "Driving Crossroad from north to north", newRobots));
        
		biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+biasX*3,7+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("North-east ", "Driving Crossroad from north to east", newRobots));
        
		biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-9+biasX*3,8+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne, 0, 1500));
        testScenarios.add(new ConcreteTestScenario("North-west", "Entering crossroad north to west", newRobots));
        
		return testScenarios;
	}

	private ArrayList<TestScenario> generateEnteringCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
		List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,7+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering north", "Entering crossroad north", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,8+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering south", "Entering crossroad south", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,8+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering east", "Entering crossroad east", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,7+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering west", "Entering crossroad west", newRobots));
        
        return testScenarios;
	}
	
	
	private class ConcreteTestScenario extends TestScenario {
		List<NewRobot> newRobots = new ArrayList<>();

        public ConcreteTestScenario(String name, String description, List<NewRobot> robots) {
            this.name = name;
            this.description = description;
            this.newRobots = robots;
        }

        @Override
        public List<NewRobot> initializeScenario() {
            /*List<NewRobot> newRobots = new ArrayList<>();
            List<Position> targetsRobotOne = new ArrayList<>();
            targetsRobotOne.add(new Position(0,5));
            List<Position> targetsRobotThree = new ArrayList<>();
            targetsRobotThree.add(new Position(9,4));
            newRobots.add(new NewTestRobot(new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
            newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree));*/
            return newRobots;
        }
    }

}
