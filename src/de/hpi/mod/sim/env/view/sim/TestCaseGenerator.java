package de.hpi.mod.sim.env.view.sim;

import java.util.ArrayList;
import java.util.Arrays;
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
		testGroups.put("I. Driving in Station", generateStationTests());
		testGroups.put("II. Simple Crossroad Driving", generateSimpleCrossroadTests());
		testGroups.put("III. Crossroad Conflicts", generateCrossroadConflicTests());
		testGroups.put("IV. Unloading Correctly", generateUnloadingTests());
		testGroups.put("V. Complete Drive Routine", generateCompleteDriveRoutineTests());
		//testGroups.put("VI. Bonus: Deadlock tests", generateDeadlockTests());
		return testGroups;
	}
	
//	private ArrayList<TestScenario> generateDeadlockTests() {
//		ArrayList<TestScenario> testScenarios = new ArrayList<>();
//		List<NewRobot> newRobots = new ArrayList<>();
//        List<Position> targetsRobotOne = new ArrayList<>();
//        List<Position> targetsRobotTwo = new ArrayList<>();
//        targetsRobotOne.add(new Position(1,0));
//        targetsRobotTwo.add(new Position(3,4));
//        newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne));
//        newRobots.add(new NewTestRobot(new Position(3, 5), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotTwo));
//        testScenarios.add(new ConcreteTestScenario("Opposite Robots", "Opposite Robots", newRobots));
//        
//        testScenarios.add(new ConcreteTestScenario("4 Robots deadlock", "2 robots at crossroad and 2 robots waiting on waypoint creates Deadlock", newRobots));
//        
//        testScenarios.add(new ConcreteTestScenario("8 Robots deadlock", "2 robots at crossroad and 6 robots waiting on waypoint creates Deadlock", newRobots));
//        
//        testScenarios.add(new ConcreteTestScenario("12 Robots deadlock", "2 robots at crossroad and 10 robots waiting on waypoint creates Deadlock", newRobots));
//        
//        //Etc.
//		return testScenarios;
//	}

	private ArrayList<TestScenario> generateUnloadingTests() {
		
		// Start list of test scenarios
		ArrayList<TestScenario> testScenarios  = new ArrayList<>();
        NewTestRobot testRobot;
        
        
        // Unloading test where robots starts at a station
        
        testRobot = new NewTestRobot(p(2, 0), RobotState.TO_LOADING, Orientation.NORTH, p_list(p(0,9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from station 1", "Start at a station, drive to unloding shaft, unload there and report. (Version 1)", r_list(testRobot)));
        
        testRobot = new NewTestRobot(p(2, 0), RobotState.TO_LOADING, Orientation.NORTH, p_list(p(3,9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from station 2", "Start at a station, drive to unloding shaft, unload there and report. (Version 2)", r_list(testRobot)));
        
        testRobot = new NewTestRobot(p(2, 0), RobotState.TO_LOADING, Orientation.NORTH, p_list(p(6,9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from station 3", "Start at a station, drive to unloding shaft, unload there and report. (Version 3)", r_list(testRobot)));


        
        
        // Unloading test where robots starts on any waypoint
        
        testRobot = new NewTestRobot(p(-3, 10), RobotState.TO_LOADING, Orientation.EAST, p_list(p(6,9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 1", "Start at a waypoint, drive to unloding shaft, unload there and report. (Version 3)", r_list(testRobot)));

        testRobot = new NewTestRobot(p(6, 8), RobotState.TO_LOADING, Orientation.WEST, p_list(p(-3,9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 2", "Start at a waypoint, drive to unloding shaft, unload there and report. (Version 3)", r_list(testRobot)));

        testRobot = new NewTestRobot(p(1, 21), RobotState.TO_LOADING, Orientation.SOUTH, p_list(p(3,9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 3", "Start at a waypoint, drive to unloding shaft, unload there and report. (Version 3)", r_list(testRobot)));

        testRobot = new NewTestRobot(p(1, 21), RobotState.TO_LOADING, Orientation.SOUTH, p_list(p(0,9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 4", "Start at a waypoint, drive to unloding shaft and unload there. (Version 4)", r_list(testRobot)));

        
        
		
		return testScenarios;
	}

	private ArrayList<TestScenario> generateCompleteDriveRoutineTests() {
		ArrayList<TestScenario> testScenarios = new ArrayList<>();
		List<NewRobot> newRobots = new ArrayList<>();
        List<Position> targets = new ArrayList<>();
        targets.add(new Position(3,9));
        newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets, true));
        testScenarios.add(new ConcreteTestScenario("Drive to unload", "Drive to unload", newRobots));
		
		newRobots = new ArrayList<>();
        List<Position> targetsRobotOne = new ArrayList<>();
        List<Position> targetsRobotTwo = new ArrayList<>();
        targetsRobotOne.add(new Position(3,12));
        targetsRobotTwo.add(new Position(2,0));
        targetsRobotTwo.add(new Position(3,9));
        newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targetsRobotOne, true));
        newRobots.add(new NewTestRobot(new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH, targetsRobotTwo, true));
		testScenarios.add(new ConcreteTestScenario ("Two robots driving routine", "Two robots driving routine", newRobots));
		
		newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotTwo = new ArrayList<>();
        List<Position> targetsRobotThree = new ArrayList<>();
        targetsRobotOne.add(new Position(3,15));
        targetsRobotTwo.add(new Position(2,0));
        targetsRobotTwo.add(new Position(3,12));
        targetsRobotThree.add(new Position(2,0));
        targetsRobotThree.add(new Position(3,9));
        newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targetsRobotOne, true));
        newRobots.add(new NewTestRobot(new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH, targetsRobotTwo, true));
        newRobots.add(new NewTestRobot(new Position(2, -2), RobotState.TO_QUEUE, Orientation.NORTH, targetsRobotThree, true));
        testScenarios.add(new ConcreteTestScenario ("Three robots driving routine", "Three robots driving routine", newRobots));
        
        newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(3,9));
        targets.add(new Position(1,0));
        targets.add(new Position(2,0));
        targets.add(new Position(6,9));
        newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets, true));
        testScenarios.add(new ConcreteTestScenario("Medium long route", "Medium long route", newRobots));
        
        newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(3,9));
        targets.add(new Position(1,0));
        targets.add(new Position(2,0));
        targets.add(new Position(6,9));
        targets.add(new Position(7,0));
        targets.add(new Position(8,0));
        targets.add(new Position(12,9));
        newRobots.add(new NewTestRobot(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets, true));
        testScenarios.add(new ConcreteTestScenario("Long route", "Long route", newRobots));
        
        return testScenarios;
	}

    private Position p(int x, int y) {
        return new Position(x, y);
    }
    
    private ArrayList<Position> p_list(Position... p){
    	return new ArrayList<Position>(Arrays.asList(p));
    }
    
    private ArrayList<NewRobot> r_list(NewRobot... r){
    	return new ArrayList<NewRobot>(Arrays.asList(r));
    }
	
	private ArrayList<TestScenario> generateStationTests() {
		
		// Start list of test scenarios
		ArrayList<TestScenario> testScenarios  = new ArrayList<>();
        NewTestRobot testRobot1,testRobot2,testRobot3;
        
        // Drive single step
        
        testRobot1 = new NewTestRobot(p(1, -2), RobotState.TO_QUEUE, Orientation.SOUTH, p_list(p(1,-3)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Drive single step 1", "Drive single step forward and report arrival. (Version 1)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(1, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2,-5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Drive single step 2", "Drive single step forward and report arrival. (Version 2)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2,-3)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Drive single step 3", "Drive single step forward and report arrival. (Version 3)", r_list(testRobot1)));

        
        
        // Leave battery
        
        testRobot1 = new NewTestRobot(p(0, -1), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1,-1)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Leave battery 1", "Drive single step forward out of battery position and report arrival. (Version 1)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(0, -2), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1,-2)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Leave battery 2", "Drive single step forward out of battery position and report arrival. (Version 2)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(0, -3), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1,-3)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Leave battery 3", "Drive single step forward out of battery position and report arrival. (Version 3)", r_list(testRobot1)));

        

        // From station entry to queue
        
        testRobot1 = new NewTestRobot(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(2,-5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From entry to queue end", "Drive from station entry to beginning of queue and report arrival.", r_list(testRobot1)));
        
        
        
        // From battery to queue
        
        testRobot1 = new NewTestRobot(p(0, -1), RobotState.TO_STATION, Orientation.EAST, p_list(p(2,-5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to queue end 1", "Drive from battery position to beginning of queue and report arrival. (Version 1)", r_list(testRobot1)));
        
        testRobot1 = new NewTestRobot(p(0, -2), RobotState.TO_STATION, Orientation.EAST, p_list(p(2,-5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to queue end 2", "Drive from battery position to beginning of queue and report arrival. (Version 2)", r_list(testRobot1)));
             
        testRobot1 = new NewTestRobot(p(0, -3), RobotState.TO_STATION, Orientation.EAST, p_list(p(2,-5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to queue end 3", "Drive from battery position to beginning of queue and report arrival. (Version 3)", r_list(testRobot1)));
        
        

        // From station entry to battery
        
        testRobot1 = new NewTestRobot(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0,-1)));
        testRobot1.setBatteryReservation(true);
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Enter battery 1", "Drive from station entry to battery position, enter battery backward, report arrival. (Version 1)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0,-2)));
        testRobot1.setBatteryReservation(true);
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Enter battery 2", "Drive from station entry to battery position, enter battery backward, report arrival. (Version 2)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0,-3)));
        testRobot1.setBatteryReservation(true);
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Enter battery 3", "Drive from station entry to battery position, enter battery backward, report arrival. (Version 3)", r_list(testRobot1)));

        

        // In queue

        testRobot1 = new NewTestRobot(p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2,0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("One robot in queue", "Drive from beginning queue to loading position and report arrival.", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2,-1)));
        testRobot1.setDelayBeforeStart(0);
        testRobot2 = new NewTestRobot(p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2,0)));
        testRobot2.setDelayBeforeStart(2000);
        testScenarios.add(new ConcreteTestScenario("Two robots in queue", "Two robots drive forward in queue without crash.", r_list(testRobot1,testRobot2)));

        testRobot1 = new NewTestRobot(p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2,-2)));
        testRobot1.setDelayBeforeStart(0);
        testRobot2 = new NewTestRobot(p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2,-1)));
        testRobot2.setDelayBeforeStart(1000);
        testRobot3 = new NewTestRobot(p(2, -1), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2,0)));
        testRobot3.setDelayBeforeStart(5000);
        testScenarios.add(new ConcreteTestScenario("Three robots in queue", "Two robots drive forward in queue without crash.", r_list(testRobot1,testRobot2,testRobot3)));

        
        

        // Complete sequence

        testRobot1 = new NewTestRobot(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(2,0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From entry to loading", "Drive from station entry start loading position and report arrival.", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(0, -1), RobotState.TO_STATION, Orientation.EAST, p_list(p(2,0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to loading 1", "Drive from battery to loading position and report arrival. (Version 1)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(0, -2), RobotState.TO_STATION, Orientation.EAST, p_list(p(2,0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to loading 2", "Drive from battery to loading position and report arrival. (Version 2)", r_list(testRobot1)));

        testRobot1 = new NewTestRobot(p(0, -3), RobotState.TO_STATION, Orientation.EAST, p_list(p(2,0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to loading 3", "Drive from battery to loading position and report arrival. (Version 3)", r_list(testRobot1)));
        
        
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
        newRobots.add(new NewTestRobot(new Position(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne));
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
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
        testScenarios.add(new ConcreteTestScenario("West-west", "Driving crossroad from west to west", newRobots));
		
		biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,6+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
        testScenarios.add(new ConcreteTestScenario("West-south", "Driving crossroad from west to south", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,9+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne));
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
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne));
        testScenarios.add(new ConcreteTestScenario("East-east", "Driving crossroad from east to east", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,6+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne));
        testScenarios.add(new ConcreteTestScenario("East-south", "Driving crossroad from east to south", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,9+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne));
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
        newRobots.add(new NewTestRobot(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne));
        testScenarios.add(new ConcreteTestScenario("North-north", "Driving Crossroad from north to north", newRobots));
        
		biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6+biasX*3,7+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne));
        testScenarios.add(new ConcreteTestScenario("North-east ", "Driving Crossroad from north to east", newRobots));
        
		biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-9+biasX*3,8+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne));
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
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 1", "Entering a crossroad from the north. No arrival message should be send.", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,8+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 2", "Entering a crossroad from the south. No arrival message should be send.", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,7+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 3", "Entering a crossroad from the east. No arrival message should be send.", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,8+biasY*3));
        newRobots.add(new NewTestRobot(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 4", "Entering a crossroad from the west. No arrival message should be send.", newRobots));
        
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
