package de.hpi.mod.sim.setting.infinitewarehouses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hpi.mod.sim.core.scenario.EntityDescription;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.setting.Position;
import de.hpi.mod.sim.setting.robot.Robot.RobotState;

public class TestCaseGenerator {
	
	private static Random rand = new Random();

	
    public static Map<String, List<TestScenario>> getAllTestCases() {
        Map<String, List<TestScenario>> testGroups = new LinkedHashMap<>();
		testGroups.put("I. Driving in Station", generateStationTests());
        testGroups.put("II. Driving at Crossroads", generateSimpleCrossroadTests());
        testGroups.put("III. Crossroad Conflicts", generateCrossroadConflicTests());
        testGroups.put("IV. Unloading Correctly", generateUnloadingTests());
        testGroups.put("V. Combined Drive Routine", generateCompleteDriveRoutineTests());
        // testGroups.put("VI. Bonus: Deadlock tests", generateDeadlockTests());
        return testGroups;
    }

    // private List<TestScenario> generateDeadlockTests() {
    // List<TestScenario> testScenarios = new ArrayList<>();
    // List<NewRobot> newRobots = new ArrayList<>();
    // List<Position> targetsRobotOne = new ArrayList<>();
    // List<Position> targetsRobotTwo = new ArrayList<>();
    // targetsRobotOne.add(new Position(1,0));
    // targetsRobotTwo.add(new Position(3,4));
    // newRobots.add(new NewTestRobot(new Position(3, 4), RobotState.TO_UNLOADING,
    // Orientation.EAST, targetsRobotOne));
    // newRobots.add(new NewTestRobot(new Position(3, 5), RobotState.TO_UNLOADING,
    // Orientation.WEST, targetsRobotTwo));
    // testScenarios.add(new ConcreteTestScenario("Opposite Robots", "Opposite
    // Robots", newRobots));
    //
    // testScenarios.add(new ConcreteTestScenario("4 Robots deadlock", "2 robots at
    // crossroad and 2 robots waiting on waypoint creates Deadlock", newRobots));
    //
    // testScenarios.add(new ConcreteTestScenario("8 Robots deadlock", "2 robots at
    // crossroad and 6 robots waiting on waypoint creates Deadlock", newRobots));
    //
    // testScenarios.add(new ConcreteTestScenario("12 Robots deadlock", "2 robots at
    // crossroad and 10 robots waiting on waypoint creates Deadlock", newRobots));
    //
    // //Etc.
    // return testScenarios;
    // }

    private static List<TestScenario> generateUnloadingTests() {

        // Start list of test scenarios
        List<TestScenario> testScenarios = new ArrayList<>();
        TestRobotDescription testRobot;

        // Unloading test where robots starts at a station

        testRobot = new TestRobotDescription(p(2, 0), RobotState.TO_LOADING, Orientation.NORTH, p_list(p(0, 9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from station 1",
                "Start at a station, drive to unloding shaft, unload there and report. (Version 1)",
                r_list(testRobot)));

        testRobot = new TestRobotDescription(p(2, 0), RobotState.TO_LOADING, Orientation.NORTH, p_list(p(3, 9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from station 2",
                "Start at a station, drive to unloding shaft, unload there and report. (Version 2)",
                r_list(testRobot)));

        testRobot = new TestRobotDescription(p(2, 0), RobotState.TO_LOADING, Orientation.NORTH, p_list(p(6, 9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from station 3",
                "Start at a station, drive to unloding shaft, unload there and report. (Version 3)",
                r_list(testRobot)));

        // Unloading test where robots starts on any waypoint

        testRobot = new TestRobotDescription(p(-3, 10), RobotState.TO_LOADING, Orientation.EAST, p_list(p(6, 9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 1",
                "Start at a waypoint, drive to unloding shaft, unload there and report. (Version 3)",
                r_list(testRobot)));

        testRobot = new TestRobotDescription(p(6, 8), RobotState.TO_LOADING, Orientation.WEST, p_list(p(-3, 9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 2",
                "Start at a waypoint, drive to unloding shaft, unload there and report. (Version 3)",
                r_list(testRobot)));

        testRobot = new TestRobotDescription(p(1, 21), RobotState.TO_LOADING, Orientation.SOUTH, p_list(p(3, 9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 3",
                "Start at a waypoint, drive to unloding shaft, unload there and report. (Version 3)",
                r_list(testRobot)));

        testRobot = new TestRobotDescription(p(1, 21), RobotState.TO_LOADING, Orientation.SOUTH, p_list(p(0, 9)));
        testRobot.setRequireArrived(true);
        testRobot.setFuzzyTargetCheck(true);
        testRobot.setUnloadingRequired(true);
        testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 4",
                "Start at a waypoint, drive to unloding shaft and unload there. (Version 4)", r_list(testRobot)));

        return testScenarios;
    }

    private static List<TestScenario> generateCompleteDriveRoutineTests() {
        List<TestScenario> testScenarios = new ArrayList<>();
        List<EntityDescription<?>> newRobots = new ArrayList<>();
        List<Position> targets;
        
        List<Position> targetsRobotOne = new ArrayList<>();
        List<Position> targetsRobotTwo = new ArrayList<>();
        targetsRobotOne.add(new Position(3, 12));
        targetsRobotTwo.add(new Position(2, 0));
        targetsRobotTwo.add(new Position(3, 9));
        newRobots.add(new TestRobotDescription(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH,
                targetsRobotOne, true));
        newRobots.add(new TestRobotDescription(new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH,
                targetsRobotTwo, true));
        testScenarios.add(new ConcreteTestScenario("Two robots unload",
                "Two robots driving close to each other to similar unloading positions.", newRobots));

        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotTwo = new ArrayList<>();
        List<Position> targetsRobotThree = new ArrayList<>();
        targetsRobotOne.add(new Position(3, 15));
        targetsRobotTwo.add(new Position(2, 0));
        targetsRobotTwo.add(new Position(3, 12));
        targetsRobotThree.add(new Position(2, 0));
        targetsRobotThree.add(new Position(3, 9));
        newRobots.add(new TestRobotDescription(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH,
                targetsRobotOne, true));
        newRobots.add(new TestRobotDescription(new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH,
                targetsRobotTwo, true));
        newRobots.add(new TestRobotDescription(new Position(2, -2), RobotState.TO_QUEUE, Orientation.NORTH,
                targetsRobotThree, true));
        testScenarios.add(new ConcreteTestScenario("Three robots unload",
                "Three robots driving close to each other to similar unloading positions.", newRobots));

        newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(3, 9));
        targets.add(new Position(1, 0));
        targets.add(new Position(2, 0));
        targets.add(new Position(6, 9));
        newRobots.add(
                new TestRobotDescription(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets, true));
        testScenarios.add(new ConcreteTestScenario("Medium long route",
                "A single robot has a sequence of targets to cover.", newRobots));

        newRobots = new ArrayList<>();
        targets = new ArrayList<>();
        targets.add(new Position(3, 9));
        targets.add(new Position(1, 0));
        targets.add(new Position(2, 0));
        targets.add(new Position(6, 9));
        targets.add(new Position(7, 0));
        targets.add(new Position(8, 0));
        targets.add(new Position(12, 9));
        newRobots.add(
                new TestRobotDescription(new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets, true));
        testScenarios.add(new ConcreteTestScenario("Long route",
                "A single robot has a long sequence of targets to cover.", newRobots));

        return testScenarios;
    }

    private static Position p(int x, int y) {
        return new Position(x, y);
    }

    private static List<Position> p_list(Position... p) {
        return new ArrayList<Position>(Arrays.asList(p));
    }

    private static List<EntityDescription<?>> r_list(TestRobotDescription... r) {
        return new ArrayList<>(Arrays.asList(r));
    }

    private static List<TestScenario> generateStationTests() {

        // Start list of test scenarios
        List<TestScenario> testScenarios = new ArrayList<>();
        TestRobotDescription testRobot1, testRobot2, testRobot3;

        // Drive single step

        testRobot1 = new TestRobotDescription(p(1, -2), RobotState.TO_QUEUE, Orientation.SOUTH, p_list(p(1, -3)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Drive single step 1",
                "Drive single step forward and report arrival. (Version 1)", r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(1, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, -5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Drive single step 2",
                "Drive single step forward and report arrival. (Version 2)", r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, -3)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Drive single step 3",
                "Drive single step forward and report arrival. (Version 3)", r_list(testRobot1)));

        // Leave battery

        testRobot1 = new TestRobotDescription(p(0, -1), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1, -1)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Leave battery 1",
                "Drive single step forward out of battery position and report arrival. (Version 1)",
                r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(0, -2), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1, -2)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Leave battery 2",
                "Drive single step forward out of battery position and report arrival. (Version 2)",
                r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(0, -3), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1, -3)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Leave battery 3",
                "Drive single step forward out of battery position and report arrival. (Version 3)",
                r_list(testRobot1)));

        // From station entry to queue

        testRobot1 = new TestRobotDescription(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(2, -5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From entry to queue end",
                "Drive from station entry to beginning of queue and report arrival.", r_list(testRobot1)));

        // From battery to queue

        testRobot1 = new TestRobotDescription(p(0, -1), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, -5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to queue end 1",
                "Drive from battery position to beginning of queue and report arrival. (Version 1)",
                r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(0, -2), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, -5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to queue end 2",
                "Drive from battery position to beginning of queue and report arrival. (Version 2)",
                r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(0, -3), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, -5)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to queue end 3",
                "Drive from battery position to beginning of queue and report arrival. (Version 3)",
                r_list(testRobot1)));

        // From station entry to battery

        testRobot1 = new TestRobotDescription(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0, -1)));
        testRobot1.setBatteryReservation(true);
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Enter battery 1",
                "Drive from station entry to battery position, enter battery backward, report arrival. (Version 1)",
                r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0, -2)));
        testRobot1.setBatteryReservation(true);
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Enter battery 2",
                "Drive from station entry to battery position, enter battery backward, report arrival. (Version 2)",
                r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0, -3)));
        testRobot1.setBatteryReservation(true);
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Enter battery 3",
                "Drive from station entry to battery position, enter battery backward, report arrival. (Version 3)",
                r_list(testRobot1)));

        // In queue

        testRobot1 = new TestRobotDescription(p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, 0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("One robot in queue",
                "Drive from beginning queue to loading position and report arrival.", r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, -1)));
        testRobot1.setDelayBeforeStart(0);
        testRobot2 = new TestRobotDescription(p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, 0)));
        testRobot2.setDelayBeforeStart(2000);
        testScenarios.add(new ConcreteTestScenario("Two robots in queue",
                "Two robots drive forward in queue without crash.", r_list(testRobot1, testRobot2)));

        testRobot1 = new TestRobotDescription(p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, -2)));
        testRobot1.setDelayBeforeStart(0);
        testRobot2 = new TestRobotDescription(p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, -1)));
        testRobot2.setDelayBeforeStart(1000);
        testRobot3 = new TestRobotDescription(p(2, -1), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, 0)));
        testRobot3.setDelayBeforeStart(5000);
        testScenarios.add(new ConcreteTestScenario("Three robots in queue",
                "Two robots drive forward in queue without crash.", r_list(testRobot1, testRobot2, testRobot3)));

        // Complete sequence

        testRobot1 = new TestRobotDescription(p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(2, 0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From entry to loading",
                "Drive from station entry start loading position and report arrival.", r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(0, -1), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, 0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to loading 1",
                "Drive from battery to loading position and report arrival. (Version 1)", r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(0, -2), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, 0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to loading 2",
                "Drive from battery to loading position and report arrival. (Version 2)", r_list(testRobot1)));

        testRobot1 = new TestRobotDescription(p(0, -3), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, 0)));
        testRobot1.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("From battery to loading 3",
                "Drive from battery to loading position and report arrival. (Version 3)", r_list(testRobot1)));

        return testScenarios;
    }

    private static List<TestScenario> generateCrossroadConflicTests() {
        List<TestScenario> testScenarios = new ArrayList<>();
        testScenarios.addAll(generateTwoRobotsOnCrossroadTests());
        testScenarios.addAll(generateThreeRobotsOnCrossroadTests());
        testScenarios.addAll(generateFourRobotsOnCrossroadTest());
        return testScenarios;
    }

    private static List<TestScenario> generateTwoRobotsOnCrossroadTests() {
        List<TestScenario> testScenarios = new ArrayList<>();
        List<EntityDescription<?>> newRobots = new ArrayList<>();
        List<Position> targetsRobotOne, targetsRobotTwo, targetsRobotThree;

        int biasX = rand.nextInt(5);
        int biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6 + biasX * 3, 8 + biasY * 3));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3 + biasX * 3, 7 + biasY * 3));
        newRobots.add(new TestRobotDescription(new Position(-3 + biasX * 3, 8 + biasY * 3), RobotState.TO_UNLOADING,
                Orientation.WEST, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-6 + biasX * 3, 7 + biasY * 3), RobotState.TO_UNLOADING,
                Orientation.EAST, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots 1",
                "Two Robots must pass a croassroad without collision. (Version 1)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-5 + biasX * 3, 6 + biasY * 3));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-4 + biasX * 3, 9 + biasY * 3));
        newRobots.add(new TestRobotDescription(new Position(-5 + biasX * 3, 9 + biasY * 3), RobotState.TO_UNLOADING,
                Orientation.SOUTH, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-4 + biasX * 3, 6 + biasY * 3), RobotState.TO_UNLOADING,
                Orientation.NORTH, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots 2",
                "Two Robots must pass a croassroad without collision. (Version 2)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.NORTH, targetsRobotTwo, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.EAST, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots 3",
                "Two Robots must pass a croassroad without collision. (Version 3)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.SOUTH, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.EAST, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots 4",
                "Two Robots must pass a croassroad without collision. (Version 4)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.WEST, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.SOUTH, targetsRobotTwo, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots 5",
                "Two Robots must pass a croassroad without collision. (Version 5)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.WEST, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.NORTH, targetsRobotTwo, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Two Robots 6",
                "Two Robots must pass a croassroad without collision. (Version 6)", newRobots));

        return testScenarios;
    }

    private static List<TestScenario> generateThreeRobotsOnCrossroadTests() {
        List<TestScenario> testScenarios = new ArrayList<>();
        int biasX = rand.nextInt(5);
        int biasY = rand.nextInt(5);
        List<EntityDescription<?>> newRobots = new ArrayList<>();
        List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
        List<Position> targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
        List<Position> targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.WEST, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.NORTH, targetsRobotTwo, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.EAST, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots 1",
                "Three Robots must pass a croassroad without collision. (Version 1)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.SOUTH, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.NORTH, targetsRobotTwo, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.EAST, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots 2",
                "Three Robots must pass a croassroad without collision. (Version 2)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.WEST, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.SOUTH, targetsRobotTwo, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.EAST, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots 3",
                "Three Robots must pass a croassroad without collision. (Version 3)", newRobots));

        biasX = rand.nextInt(5);
        biasY = rand.nextInt(5);
        newRobots = new ArrayList<>();
        targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
        targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
        targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
        newRobots.add(new TestRobotDescription(new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.WEST, targetsRobotOne, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.NORTH, targetsRobotTwo, 0, 1000));
        newRobots.add(new TestRobotDescription(new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
                Orientation.SOUTH, targetsRobotThree, 0, 1000));
        testScenarios.add(new ConcreteTestScenario("Three Robots 4",
                "Three Robots must pass a croassroad without collision. (Version 4)", newRobots));

        return testScenarios;
    }

    private static List<TestScenario> generateFourRobotsOnCrossroadTest() {
        List<TestScenario> testScenarios = new ArrayList<>();

        List<EntityDescription<?>> newRobots = new ArrayList<>();
        List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(0, 5));
        List<Position> targetsRobotTwo = new ArrayList<>();
        targetsRobotTwo.add(new Position(5, 9));
        List<Position> targetsRobotThree = new ArrayList<>();
        targetsRobotThree.add(new Position(9, 4));
        List<Position> targetsRobotFour = new ArrayList<>();
        targetsRobotFour.add(new Position(4, 0));
        newRobots.add(new TestRobotDescription(new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST,
                targetsRobotOne));
        newRobots.add(new TestRobotDescription(new Position(5, 3), RobotState.TO_UNLOADING, Orientation.NORTH,
                targetsRobotTwo));
        newRobots.add(new TestRobotDescription(new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST,
                targetsRobotThree));
        newRobots.add(new TestRobotDescription(new Position(4, 6), RobotState.TO_UNLOADING, Orientation.SOUTH,
                targetsRobotFour));
        testScenarios.add(new ConcreteTestScenario("Four Robots",
                "Four Robots must pass a croassroad without collision.", newRobots));

        return testScenarios;

    }

    private static List<TestScenario> generateSimpleCrossroadTests() {
		List<TestScenario> testScenarios = new ArrayList<>();
		testScenarios.addAll(generateEnteringCrossroadTests());
		testScenarios.addAll(generatePassingCrossroadAheadTests());
		testScenarios.addAll(generateTurningRightOnCrossroadTests());
		testScenarios.addAll(generateTurningLeftOnCrossroadTests());
		return testScenarios;
	}

	private static List<TestScenario> generatePassingCrossroadAheadTests() {
		List<TestScenario> testScenarios = new ArrayList<>();
		TestRobotDescription testRobot;
		int biasX, biasY;
		
		biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, p_list(p(-8+biasX*3,6+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 1", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 1)", r_list(testRobot)));

        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, p_list(p(-9+biasX*3,8+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 2", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 2)", r_list(testRobot)));

        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, p_list(p(-6+biasX*3,7+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 3", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 3)", r_list(testRobot)));

        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, p_list(p(-7+biasX*3,9+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 4", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 4)", r_list(testRobot)));

		return testScenarios;
	}

	private static List<TestScenario> generateTurningLeftOnCrossroadTests() {
		List<TestScenario> testScenarios = new ArrayList<>();
		TestRobotDescription testRobot;
		int biasX, biasY;
		
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, p_list(p(-8+biasX*3,6+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Turning right on crossroad 1", "Start at a waypoint, go right on upcoming crossroad, report arrival. (Version 1)", r_list(testRobot)));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, p_list(p(-7+biasX*3,9+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Turning right on crossroad 2", "Start at a waypoint, go right on upcoming crossroad, report arrival. (Version 2)", r_list(testRobot)));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, p_list(p(-9+biasX*3,8+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Turning right on crossroad 3", "Start at a waypoint, go right on upcoming crossroad, report arrival. (Version 3)", r_list(testRobot)));
        
		return testScenarios;
        
	}

	private static List<TestScenario> generateTurningRightOnCrossroadTests() {
		List<TestScenario> testScenarios = new ArrayList<>();
		TestRobotDescription testRobot;
		int biasX, biasY;
		
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, p_list(p(-7+biasX*3,9+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Turning left on crossroad 1", "Start at a waypoint, go left on upcoming crossroad, report arrival. (Version 1)", r_list(testRobot)));
       
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, p_list(p(-8+biasX*3,6+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Turning left on crossroad 2", "Start at a waypoint, go left on upcoming crossroad, report arrival. (Version 2)", r_list(testRobot)));

        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
        testRobot = new TestRobotDescription(p(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, p_list(p(-6+biasX*3,7+biasY*3)));
        testRobot.setRequireArrived(true);
        testScenarios.add(new ConcreteTestScenario("Turning left on crossroad 3", "Start at a waypoint, go left on upcoming crossroad, report arrival. (Version 3)", r_list(testRobot)));
      
		return testScenarios;
	}

	private static List<TestScenario> generateEnteringCrossroadTests() {
		List<TestScenario> testScenarios = new ArrayList<>();
		
		int biasX = rand.nextInt(5);
		int biasY = rand.nextInt(5);
		List<EntityDescription<?>> newRobots = new ArrayList<>();
		List<Position> targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,7+biasY*3));
        newRobots.add(new TestRobotDescription(new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 1", "Entering a crossroad from the north. No arrival message should be send.", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,8+biasY*3));
        newRobots.add(new TestRobotDescription(new Position(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 2", "Entering a crossroad from the south. No arrival message should be send.", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-8+biasX*3,7+biasY*3));
        newRobots.add(new TestRobotDescription(new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 3", "Entering a crossroad from the east. No arrival message should be send.", newRobots));
        
        biasX = rand.nextInt(5);
		biasY = rand.nextInt(5);
		newRobots = new ArrayList<>();
		targetsRobotOne = new ArrayList<>();
        targetsRobotOne.add(new Position(-7+biasX*3,8+biasY*3));
        newRobots.add(new TestRobotDescription(new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne, 1000, 3000));
        testScenarios.add(new ConcreteTestScenario("Entering crossroad 4", "Entering a crossroad from the west. No arrival message should be send.", newRobots));
        
        return testScenarios;
	}
	
	
	private static class ConcreteTestScenario extends TestScenario {
		List<EntityDescription<?>> newEntities = new ArrayList<>();

        public ConcreteTestScenario(String name, String description, List<EntityDescription<?>> robots) {
            this.name = name;
            this.description = description;
            this.newEntities = robots;
        }

        @Override
        public List<EntityDescription<?>> initializeScenario() {
            for(int i=0; i<newEntities.size(); i++) {
            	newEntities.get(i).refreshEntity();
            }
            return newEntities;
        }
    }

}
