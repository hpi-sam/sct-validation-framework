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
		testGroups.put("I.Driving in station", generateStationTests());
		testGroups.put("II. Simple crossroad", generateSimpleCrossroadTests());
		testGroups.put("III. Crossroad conflicts", generateCrossroadConflicTests());
		//testGroups.put("V. Complete drive routine", generateCompleteDriveRoutineTests());
		return testGroups;
	}
	
	private ArrayList<TestScenario> generateCompleteDriveRoutineTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		List<NewRobot> newRobots = new ArrayList<>();
        List<Position> targetsRobotOne = new ArrayList<>();
        List<Position> targetsRobotTwo = new ArrayList<>();
        targetsRobotOne.add(new Position(2,0));
        targetsRobotOne.add(new Position(3,10));
        targetsRobotTwo.add(new Position(2,0));
        targetsRobotTwo.add(new Position(3,13));
        newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_BATTERY, Orientation.EAST, targetsRobotOne));
        newRobots.add(new NewTestRobot(new Position(0, -3), RobotState.TO_BATTERY, Orientation.EAST, targetsRobotTwo));
		return testScenarios;
	}

	private ArrayList<TestScenario> generateStationTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		List<NewRobot> newRobots = new ArrayList<>();
        List<Position> targets = new ArrayList<>();
        targets.add(new Position(1,-2));
        newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_QUEUE, Orientation.EAST, targets, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Drive one step", "Drive one step", newRobots));
        
		newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(1,-5));
        targets.add(new Position(2,-5));
        newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_UNLOADING, Orientation.EAST, targets, 0, 1000)); //RobotState is arbitrary here
        testScenarios.add(new ConcreteTestScenario("Drive to end of queue", "Drive to end of queue", newRobots));
        
        newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(0,-2));
        newRobots.add(new NewTestRobot(new Position(1, 0), RobotState.TO_UNLOADING, Orientation.SOUTH, targets, 0, 1000)); //RobotState is arbitrary here
        testScenarios.add(new ConcreteTestScenario("Drive to battery", "Drive to battery", newRobots));
        
        newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(1,-5));
        targets.add(new Position(2,0));
        newRobots.add(new NewTestRobot(new Position(1, 0), RobotState.TO_UNLOADING, Orientation.SOUTH, targets, 0, 1000)); //RobotState is arbitrary here
        testScenarios.add(new ConcreteTestScenario("Drive to loading 1", "Drive to loading position from station entry", newRobots));
        
        newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(1,-5));
        targets.add(new Position(2,0));
        newRobots.add(new NewTestRobot(new Position(0, -2), RobotState.TO_UNLOADING, Orientation.EAST, targets, 0, 1000)); //RobotState is arbitrary here
        testScenarios.add(new ConcreteTestScenario("Drive to loading 2", "Drive to loading position from battery", newRobots)); 
        
        return testScenarios;
	}

	private ArrayList<TestScenario> generateCrossroadConflicTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		testScenarios.addAll(generateTwoRobotsOppositOnCrossroadTests());
		testScenarios.addAll(generateTwoRobotsCornerOnCrossroadTests());
		testScenarios.addAll(generateThreeRobotsOnCrossroadTests());
		testScenarios.addAll(generateFourRobotsOnCrossroadTest());
		return testScenarios;
	}


	private ArrayList<TestScenario> generateTwoRobotsOppositOnCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
        List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+biasX*3,8+biasY*3));
        List<Position> targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3+biasX*3,7+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-3+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots vertical", "Driving crossroad with two Robots vertical opposite", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-5+biasX*3,6+biasY*3));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-4+biasX*3,9+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-5+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-4+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots horizontal", "Driving crossroad with two Robots horizontal opposite", newRobots));
		return testScenarios;
	}

	private ArrayList<TestScenario> generateTwoRobotsCornerOnCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
        List<Position> targetsRobotOne = new ArrayList<>();
        
        List<Position> targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4+3*biasX,9+3*biasY));
        List<Position> targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3+3*biasX,7+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-4+3*biasX, 6+3*biasY), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo,0,1000));
        newRobots.add(new NewTestRobot(new Position(-6+3*biasX, 7+3*biasY), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -NW", "Two Robots on crossroad with no Robot in the north and in the west", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-5+3*biasX,6+3*biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3+3*biasX,7+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-5+3*biasX, 9+3*biasY), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-6+3*biasX, 7+3*biasY), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -ES", "Two Robots on crossroad with no Robot in the east and in the south", newRobots));
        
        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+3*biasX,8+3*biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-5+3*biasX,6+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-3+3*biasX, 8+3*biasY), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-5+3*biasX, 9+3*biasY), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotTwo,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -SE", "Two Robots on crossroad with no Robot in the south and the east", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+3*biasX,8+3*biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4+3*biasX,9+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-3+3*biasX, 8+3*biasY), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-4+3*biasX, 6+3*biasY), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -WN", "Two Robots on crossroad with no Robot in the west and the north", newRobots));
        
        return testScenarios;
	}

	private ArrayList<TestScenario> generateThreeRobotsOnCrossroadTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<NewRobot> newRobots = new ArrayList<>();
        List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+3*biasX,8+3*biasY));
        List<Position> targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4+3*biasX,9+3*biasY));
        List<Position> targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3+3*biasX,7+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-3+3*biasX, 8+3*biasY), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-4+3*biasX, 6+3*biasY), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo,0,1000));
        newRobots.add(new NewTestRobot(new Position(-6+3*biasX, 7+3*biasY), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -N", "Three Robots on crossroad with no Robot in the north", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-5+3*biasX,6+3*biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4+3*biasX,9+3*biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3+3*biasX,7+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-5+3*biasX, 9+3*biasY), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-4+3*biasX, 6+3*biasY), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo,0,1000));
        newRobots.add(new NewTestRobot(new Position(-6+3*biasX, 7+3*biasY), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -E", "Three Robots on crossroad with no Robot in the east", newRobots));
        
        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+3*biasX,8+3*biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-5+3*biasX,6+3*biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3+3*biasX,7+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-3+3*biasX, 8+3*biasY), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-5+3*biasX, 9+3*biasY), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotTwo,0,1000));
        newRobots.add(new NewTestRobot(new Position(-6+3*biasX, 7+3*biasY), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -S", "Three Robots on crossroad with no Robot in the south", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+3*biasX,8+3*biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4+3*biasX,9+3*biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-5+3*biasX,6+3*biasY));
        newRobots.add(new NewTestRobot(new Position(-3+3*biasX, 8+3*biasY), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne,0,1000));
        newRobots.add(new NewTestRobot(new Position(-4+3*biasX, 6+3*biasY), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo,0,1000));
        newRobots.add(new NewTestRobot(new Position(-5+3*biasX, 9+3*biasY), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotThree,0,1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots -W", "Three Robots on crossroad with no Robot in the west", newRobots));
		
		return testScenarios;
	}

	private ArrayList<TestScenario> generateFourRobotsOnCrossroadTest() {
		 ArrayList<TestScenario> testScenarios = new ArrayList<>();
		
		 List<NewRobot> newRobots = new ArrayList<>();
         List<Position> targetsRobotOne = new ArrayList<>();
         targetsRobotOne.add(new Position(0,5));
         List<Position> targetsRobotTwo = new ArrayList<>();
         targetsRobotTwo.add(new Position(5,9));
         List<Position> targetsRobotThree = new ArrayList<>();
         targetsRobotThree.add(new Position(9,4));
         List<Position> targetsRobotFour = new ArrayList<>();
         targetsRobotFour.add(new Position(4,0));
         newRobots.add(new NewTestRobot(new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
         newRobots.add(new NewTestRobot(new Position(5, 3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotTwo));
         newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotThree));
         newRobots.add(new NewTestRobot(new Position(4, 6), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotFour));
         testScenarios.add(new ConcreteTestScenario("Four Robots", "Four Robots on crossroad", newRobots));
         
         return testScenarios;
         
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
            for(int i=0; i<newRobots.size(); i++) {
            	newRobots.get(i).refreshRobot();
            }
            return newRobots;
        }
    }

}
