package de.hpi.mod.sim.worlds.simpletraffic.scenario;

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
 import de.hpi.mod.sim.core.scenario.TestScenario;
 import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
 import de.hpi.mod.sim.worlds.abstract_grid.Position;
 import de.hpi.mod.sim.worlds.infinitewarehouse.environment.WarehouseManager;
 import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot.RobotState;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.TestRobotSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorld;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration.GridMode;
import de.hpi.mod.sim.worlds.simpletraffic.entities.DeparturePoint;
import de.hpi.mod.sim.worlds.simpletraffic.entities.RelativePosition;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.ArrivalPointSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.DeparturePointSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.SimpleRobotSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.TrafficLightSpecification;

 public class TestCaseGenerator {

    private SimpleTrafficWorld world;

	public TestCaseGenerator(SimpleTrafficWorld world) {
        this.world = world;
    }
	
     public Map<String, List<TestScenario>> getAllTestCases() {
         Map<String, List<TestScenario>> testGroups = new LinkedHashMap<>();
         
         testGroups.put("I. Simple Driving", new CopyOnWriteArrayList<TestScenario>());
         testGroups.put("II. Complete Route Driving", generateCompleteRouteTests(this.world.getStreetNetworkManager()));
         testGroups.put("III. Crossroad Conflicts", new CopyOnWriteArrayList<TestScenario>());
         testGroups.put("IV. Complex Traffic", new CopyOnWriteArrayList<TestScenario>());
         return testGroups;
     }

      private List<TestScenario> generateCompleteRouteTests(TrafficGridManager manager) {

         // Start list of test scenarios for this group
         List<TestScenario> testScenarios = new ArrayList<>();
         
         // Define Variables
         SimpleRobotSpecification testRobot;
         ArrivalPointSpecification testArrivalPoint;
         DeparturePointSpecification testDeparturePoint;

//         // Unloading test where robots starts at a station

//         testRobot = new TestRobotSpecification(robots, p(1, 21), RobotState.TO_LOADING, Orientation.SOUTH, p_list(p(3, 9)));
//         testRobot.setRequireArrived(true);
//         testRobot.setFuzzyTargetCheck(true);
//         testRobot.setUnloadingRequired(true);
//         testScenarios.add(new ConcreteTestScenario("Unloading from anywhere 3",
//                 "Start at a waypoint, drive to unloding shaft, unload there and report. (Version 3)",
//                 r_list(testRobot)));
         

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(1, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(4, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Straigt Ahead 1", "Robot starts at top position of left side and has to drive straight to top position of right side.",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(7, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(2, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Straigt Ahead 2", "Robot starts at left position of bottom side and has to drive straight to left position of top side.",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(5, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(0, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Straigt Ahead 3", "Robot starts at bottom position of right side and has to drive straight to bottom position of left side.",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));

         
         
         
         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(0, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(7, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Right 1", "Robot starts at bottom position of left side and has to drive to left position of bottom side (i.e. turn right once).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(2, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(5, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Right 2", "Robot starts at left position of top side and has to drive to bottom position of left side (i.e. drive straigt once, then turn right).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(7, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(5, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Right 3", "Robot starts at left position of bottom side and has to drive to bottom position of right side (i.e. turn right once, then drive straigt).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(5, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(2, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Right 4", "Robot starts at bottom position of right side and has to drive to left position of top side (i.e. drive straigt once, turn right once, then drive straigt again or alternatively drive left, right, left).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(4, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(2, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Right 4 EXTRA", "Robot starts at bottom position of right side and has to drive to left position of top side (i.e. drive straigt once, turn right once, then drive straigt again or alternatively drive left, right, left).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 


         
         
         
         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(1, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(2, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Left 1", "Robot starts at top position of left side and has to drive to left position of top side (i.e. turn left once).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(3, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(5, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Left 2", "Robot starts at right position of top side and has to drive to bottom position of right side (i.e. drive straigt once, then turn left).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(5, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(7, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Left 3", "Robot starts at bottom position of right side and has to drive to left position of bottom side (i.e. turn left once, then drive straigt).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(6, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(1, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Left 4", "Robot starts at right position of bottom side and has to drive to top position of left side (i.e. drive straigt once, turn left once, then drive straigt again or alternatively drive left, right, left).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false)); 

         

         
         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(5, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(1, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Opposite Ahead 1", "Robot starts at bottom position of right side and has to drive top position of left side (ie. turn right once and then left at some point to switch lanes).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(3, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(7, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Opposite Ahead 2", "Robot starts at right position of top side and has to drive left position of bottom side (ie. turn left once and then right at some point to switch lanes).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));
         
         
         
         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(0, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(1, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Behind 1", "Robot starts at bottom position of left side and has to drive to top position of left side (i.e. turn left twice).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));
         
         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(3, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(4, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Behind 2", "Robot starts at right position of top side and has to drive to left position of top side (i.e. turn right twice).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));

         testRobot = new SimpleRobotSpecification(manager);
         testDeparturePoint = new DeparturePointSpecification(4, manager, false);
         testArrivalPoint = new ArrivalPointSpecification(5, manager, false);
         testScenarios.add(new SimpleTrafficLightTestScenario(
        		 "Target is Behind 3", "Robot starts at top position of right side and has to drive to bottom position of right side (i.e. turn left twice).",
        		 e_list(testRobot, testArrivalPoint, testDeparturePoint), 
        		 GridMode.TWO_CROSSROADS, true, false, false));
         
         return testScenarios;
     }

//     private static List<TestScenario> generateCompleteDriveRoutineTests(WarehouseManager robots) {
//         List<TestScenario> testScenarios = new ArrayList<>();
//         List<EntitySpecification<?>> newRobots = new ArrayList<>();
//         List<Position> targets;
        
//         List<Position> targetsRobotOne = new ArrayList<>();
//         List<Position> targetsRobotTwo = new ArrayList<>();
//         targetsRobotOne.add(new Position(3, 12));
//         targetsRobotTwo.add(new Position(2, 0));
//         targetsRobotTwo.add(new Position(3, 9));
//         newRobots.add(new TestRobotSpecification(robots, new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH,
//                 targetsRobotOne, true));
//         newRobots.add(new TestRobotSpecification(robots, new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH,
//                 targetsRobotTwo, true));
//         testScenarios.add(new ConcreteTestScenario("Two robots unload",
//                 "Two robots driving close to each other to similar unloading positions.", newRobots));

//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotTwo = new ArrayList<>();
//         List<Position> targetsRobotThree = new ArrayList<>();
//         targetsRobotOne.add(new Position(3, 15));
//         targetsRobotTwo.add(new Position(2, 0));
//         targetsRobotTwo.add(new Position(3, 12));
//         targetsRobotThree.add(new Position(2, 0));
//         targetsRobotThree.add(new Position(3, 9));
//         newRobots.add(new TestRobotSpecification(robots, new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH,
//                 targetsRobotOne, true));
//         newRobots.add(new TestRobotSpecification(robots, new Position(2, -1), RobotState.TO_QUEUE, Orientation.NORTH,
//                 targetsRobotTwo, true));
//         newRobots.add(new TestRobotSpecification(robots, new Position(2, -2), RobotState.TO_QUEUE, Orientation.NORTH,
//                 targetsRobotThree, true));
//         testScenarios.add(new ConcreteTestScenario("Three robots unload",
//                 "Three robots driving close to each other to similar unloading positions.", newRobots));

//         newRobots = new ArrayList<>();
//         targets = new ArrayList<>();
//         targets.add(new Position(3, 9));
//         targets.add(new Position(1, 0));
//         targets.add(new Position(2, 0));
//         targets.add(new Position(6, 9));
//         newRobots.add(
//                 new TestRobotSpecification(robots, new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets, true));
//         testScenarios.add(new ConcreteTestScenario("Medium long route",
//                 "A single robot has a sequence of targets to cover.", newRobots));

//         newRobots = new ArrayList<>();
//         targets = new ArrayList<>();
//         targets.add(new Position(3, 9));
//         targets.add(new Position(1, 0));
//         targets.add(new Position(2, 0));
//         targets.add(new Position(6, 9));
//         targets.add(new Position(7, 0));
//         targets.add(new Position(8, 0));
//         targets.add(new Position(12, 9));
//         newRobots.add(
//                 new TestRobotSpecification(robots, new Position(2, 0), RobotState.TO_LOADING, Orientation.NORTH, targets, true));
//         testScenarios.add(new ConcreteTestScenario("Long route",
//                 "A single robot has a long sequence of targets to cover.", newRobots));

//         return testScenarios;
//     }

//     private static Position p(int x, int y) {
//         return new Position(x, y);
//     }

//     private static List<Position> p_list(Position... p) {
//         return new ArrayList<Position>(Arrays.asList(p));
//     }

//     private static List<EntitySpecification<?>> r_list(TestRobotSpecification... r) {
//         return new ArrayList<>(Arrays.asList(r));
//     }

//     private static List<TestScenario> generateStationTests(WarehouseManager robots) {

//         // Start list of test scenarios
//         List<TestScenario> testScenarios = new ArrayList<>();
//         TestRobotSpecification testRobot1, testRobot2, testRobot3;

//         // Drive single step

//         testRobot1 = new TestRobotSpecification(robots, p(1, -2), RobotState.TO_QUEUE, Orientation.SOUTH, p_list(p(1, -3)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Drive single step 1",
//                 "Drive single step forward and report arrival. (Version 1)", r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(1, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, -5)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Drive single step 2",
//                 "Drive single step forward and report arrival. (Version 2)", r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, -3)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Drive single step 3",
//                 "Drive single step forward and report arrival. (Version 3)", r_list(testRobot1)));

//         // Leave Charger

//         testRobot1 = new TestRobotSpecification(robots, p(0, -1), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1, -1)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Leave charger 1",
//                 "Drive single step forward out of charger position and report arrival. (Version 1)",
//                 r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(0, -2), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1, -2)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Leave charger 2",
//                 "Drive single step forward out of charger position and report arrival. (Version 2)",
//                 r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(0, -3), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(1, -3)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Leave charger 3",
//                 "Drive single step forward out of charger position and report arrival. (Version 3)",
//                 r_list(testRobot1)));

//         // From station entry to queue

//         testRobot1 = new TestRobotSpecification(robots, p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(2, -5)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From entry to queue end",
//                 "Drive from station entry to beginning of queue and report arrival.", r_list(testRobot1)));

//         // From charger to queue

//         testRobot1 = new TestRobotSpecification(robots, p(0, -1), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, -5)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From charger to queue end 1",
//                 "Drive from charger position to beginning of queue and report arrival. (Version 1)",
//                 r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(0, -2), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, -5)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From charger to queue end 2",
//                 "Drive from charger position to beginning of queue and report arrival. (Version 2)",
//                 r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(0, -3), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, -5)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From charger to queue end 3",
//                 "Drive from charger position to beginning of queue and report arrival. (Version 3)",
//                 r_list(testRobot1)));

//         // From station entry to charger

//         testRobot1 = new TestRobotSpecification(robots, p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0, -1)));
//         testRobot1.setChargerReservation(true);
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Enter charger 1",
//                 "Drive from station entry to charger position, enter charger backward, report arrival. (Version 1)",
//                 r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0, -2)));
//         testRobot1.setChargerReservation(true);
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Enter charger 2",
//                 "Drive from station entry to charger position, enter charger backward, report arrival. (Version 2)",
//                 r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(0, -3)));
//         testRobot1.setChargerReservation(true);
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Enter charger 3",
//                 "Drive from station entry to charger position, enter charger backward, report arrival. (Version 3)",
//                 r_list(testRobot1)));

//         // In queue

//         testRobot1 = new TestRobotSpecification(robots, p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, 0)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("One robot in queue",
//                 "Drive from beginning queue to loading position and report arrival.", r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, -1)));
//         testRobot1.setDelayBeforeStart(0);
//         testRobot2 = new TestRobotSpecification(robots, p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, 0)));
//         testRobot2.setDelayBeforeStart(2000);
//         testScenarios.add(new ConcreteTestScenario("Two robots in queue",
//                 "Two robots drive forward in queue without crash.", r_list(testRobot1, testRobot2)));

//         testRobot1 = new TestRobotSpecification(robots, p(2, -5), RobotState.TO_QUEUE, Orientation.EAST, p_list(p(2, -2)));
//         testRobot1.setDelayBeforeStart(0);
//         testRobot2 = new TestRobotSpecification(robots, p(2, -4), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, -1)));
//         testRobot2.setDelayBeforeStart(1000);
//         testRobot3 = new TestRobotSpecification(robots, p(2, -1), RobotState.TO_QUEUE, Orientation.NORTH, p_list(p(2, 0)));
//         testRobot3.setDelayBeforeStart(5000);
//         testScenarios.add(new ConcreteTestScenario("Three robots in queue",
//                 "Two robots drive forward in queue without crash.", r_list(testRobot1, testRobot2, testRobot3)));

//         // Complete sequence

//         testRobot1 = new TestRobotSpecification(robots, p(1, 0), RobotState.TO_STATION, Orientation.SOUTH, p_list(p(2, 0)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From entry to loading",
//                 "Drive from station entry start loading position and report arrival.", r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(0, -1), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, 0)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From charger to loading 1",
//                 "Drive from charger to loading position and report arrival. (Version 1)", r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(0, -2), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, 0)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From charger to loading 2",
//                 "Drive from charger to loading position and report arrival. (Version 2)", r_list(testRobot1)));

//         testRobot1 = new TestRobotSpecification(robots, p(0, -3), RobotState.TO_STATION, Orientation.EAST, p_list(p(2, 0)));
//         testRobot1.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("From charger to loading 3",
//                 "Drive from charger to loading position and report arrival. (Version 3)", r_list(testRobot1)));

//         return testScenarios;
//     }

//     private static List<TestScenario> generateCrossroadConflicTests(WarehouseManager robots) {
//         List<TestScenario> testScenarios = new ArrayList<>();
//         testScenarios.addAll(generateTwoRobotsOnCrossroadTests(robots));
//         testScenarios.addAll(generateThreeRobotsOnCrossroadTests(robots));
//         testScenarios.addAll(generateFourRobotsOnCrossroadTest(robots));
//         return testScenarios;
//     }

//     private static List<TestScenario> generateTwoRobotsOnCrossroadTests(WarehouseManager robots) {
//         List<TestScenario> testScenarios = new ArrayList<>();
//         List<EntitySpecification<?>> newRobots = new ArrayList<>();
//         List<Position> targetsRobotOne, targetsRobotTwo, targetsRobotThree;

//         int biasX = rand.nextInt(5);
//         int biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-6 + biasX * 3, 8 + biasY * 3));
//         targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-3 + biasX * 3, 7 + biasY * 3));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-3 + biasX * 3, 8 + biasY * 3), RobotState.TO_UNLOADING,
//                 Orientation.WEST, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-6 + biasX * 3, 7 + biasY * 3), RobotState.TO_UNLOADING,
//                 Orientation.EAST, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Two Robots 1",
//                 "Two Robots must pass a croassroad without collision. (Version 1)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-5 + biasX * 3, 6 + biasY * 3));
//         targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-4 + biasX * 3, 9 + biasY * 3));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-5 + biasX * 3, 9 + biasY * 3), RobotState.TO_UNLOADING,
//                 Orientation.SOUTH, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-4 + biasX * 3, 6 + biasY * 3), RobotState.TO_UNLOADING,
//                 Orientation.NORTH, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Two Robots 2",
//                 "Two Robots must pass a croassroad without collision. (Version 2)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
//         targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.NORTH, targetsRobotTwo, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.EAST, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Two Robots 3",
//                 "Two Robots must pass a croassroad without collision. (Version 3)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
//         targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.SOUTH, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.EAST, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Two Robots 4",
//                 "Two Robots must pass a croassroad without collision. (Version 4)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
//         targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.WEST, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.SOUTH, targetsRobotTwo, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Two Robots 5",
//                 "Two Robots must pass a croassroad without collision. (Version 5)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
//         targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.WEST, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.NORTH, targetsRobotTwo, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Two Robots 6",
//                 "Two Robots must pass a croassroad without collision. (Version 6)", newRobots));

//         return testScenarios;
//     }

//     private static List<TestScenario> generateThreeRobotsOnCrossroadTests(WarehouseManager robots) {
//         List<TestScenario> testScenarios = new ArrayList<>();
//         int biasX = rand.nextInt(5);
//         int biasY = rand.nextInt(5);
//         List<EntitySpecification<?>> newRobots = new ArrayList<>();
//         List<Position> targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
//         List<Position> targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
//         List<Position> targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.WEST, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.NORTH, targetsRobotTwo, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.EAST, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Three Robots 1",
//                 "Three Robots must pass a croassroad without collision. (Version 1)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
//         targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
//         targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.SOUTH, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.NORTH, targetsRobotTwo, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.EAST, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Three Robots 2",
//                 "Three Robots must pass a croassroad without collision. (Version 2)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
//         targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
//         targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-3 + 3 * biasX, 7 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.WEST, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.SOUTH, targetsRobotTwo, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-6 + 3 * biasX, 7 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.EAST, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Three Robots 3",
//                 "Three Robots must pass a croassroad without collision. (Version 3)", newRobots));

//         biasX = rand.nextInt(5);
//         biasY = rand.nextInt(5);
//         newRobots = new ArrayList<>();
//         targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-6 + 3 * biasX, 8 + 3 * biasY));
//         targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(-4 + 3 * biasX, 9 + 3 * biasY));
//         targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(-5 + 3 * biasX, 6 + 3 * biasY));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-3 + 3 * biasX, 8 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.WEST, targetsRobotOne, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-4 + 3 * biasX, 6 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.NORTH, targetsRobotTwo, 0, 1000));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-5 + 3 * biasX, 9 + 3 * biasY), RobotState.TO_UNLOADING,
//                 Orientation.SOUTH, targetsRobotThree, 0, 1000));
//         testScenarios.add(new ConcreteTestScenario("Three Robots 4",
//                 "Three Robots must pass a croassroad without collision. (Version 4)", newRobots));

//         return testScenarios;
//     }

//     private static List<TestScenario> generateFourRobotsOnCrossroadTest(WarehouseManager robots) {
//         List<TestScenario> testScenarios = new ArrayList<>();

//         List<EntitySpecification<?>> newRobots = new ArrayList<>();
//         List<Position> targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(0, 5));
//         List<Position> targetsRobotTwo = new ArrayList<>();
//         targetsRobotTwo.add(new Position(5, 9));
//         List<Position> targetsRobotThree = new ArrayList<>();
//         targetsRobotThree.add(new Position(9, 4));
//         List<Position> targetsRobotFour = new ArrayList<>();
//         targetsRobotFour.add(new Position(4, 0));
//         newRobots.add(new TestRobotSpecification(robots, new Position(6, 5), RobotState.TO_UNLOADING, Orientation.WEST,
//                 targetsRobotOne));
//         newRobots.add(new TestRobotSpecification(robots, new Position(5, 3), RobotState.TO_UNLOADING, Orientation.NORTH,
//                 targetsRobotTwo));
//         newRobots.add(new TestRobotSpecification(robots, new Position(3, 4), RobotState.TO_UNLOADING, Orientation.EAST,
//                 targetsRobotThree));
//         newRobots.add(new TestRobotSpecification(robots, new Position(4, 6), RobotState.TO_UNLOADING, Orientation.SOUTH,
//                 targetsRobotFour));
//         testScenarios.add(new ConcreteTestScenario("Four Robots",
//                 "Four Robots must pass a croassroad without collision.", newRobots));

//         return testScenarios;

//     }

//     private static List<TestScenario> generateSimpleCrossroadTests(WarehouseManager robots) {
// 		List<TestScenario> testScenarios = new ArrayList<>();
// 		testScenarios.addAll(generateEnteringCrossroadTests(robots));
// 		testScenarios.addAll(generatePassingCrossroadAheadTests(robots));
// 		testScenarios.addAll(generateTurningRightOnCrossroadTests(robots));
// 		testScenarios.addAll(generateTurningLeftOnCrossroadTests(robots));
// 		return testScenarios;
// 	}

// 	private static List<TestScenario> generatePassingCrossroadAheadTests(WarehouseManager robots) {
// 		List<TestScenario> testScenarios = new ArrayList<>();
// 		TestRobotSpecification testRobot;
// 		int biasX, biasY;
		
// 		biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, p_list(p(-8+biasX*3,6+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 1", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 1)", r_list(testRobot)));

//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, p_list(p(-9+biasX*3,8+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 2", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 2)", r_list(testRobot)));

//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, p_list(p(-6+biasX*3,7+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 3", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 3)", r_list(testRobot)));

//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, p_list(p(-7+biasX*3,9+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Going Ahead on crossroad 4", "Start at a waypoint, pass upcoming crossroad straight ahead, report arrival. (Version 4)", r_list(testRobot)));

// 		return testScenarios;
// 	}

// 	private static List<TestScenario> generateTurningLeftOnCrossroadTests(WarehouseManager robots) {
// 		List<TestScenario> testScenarios = new ArrayList<>();
// 		TestRobotSpecification testRobot;
// 		int biasX, biasY;
		
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, p_list(p(-8+biasX*3,6+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Turning right on crossroad 1", "Start at a waypoint, go right on upcoming crossroad, report arrival. (Version 1)", r_list(testRobot)));
        
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, p_list(p(-7+biasX*3,9+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Turning right on crossroad 2", "Start at a waypoint, go right on upcoming crossroad, report arrival. (Version 2)", r_list(testRobot)));
        
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, p_list(p(-9+biasX*3,8+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Turning right on crossroad 3", "Start at a waypoint, go right on upcoming crossroad, report arrival. (Version 3)", r_list(testRobot)));
        
// 		return testScenarios;
        
// 	}

// 	private static List<TestScenario> generateTurningRightOnCrossroadTests(WarehouseManager robots) {
// 		List<TestScenario> testScenarios = new ArrayList<>();
// 		TestRobotSpecification testRobot;
// 		int biasX, biasY;
		
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, p_list(p(-7+biasX*3,9+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Turning left on crossroad 1", "Start at a waypoint, go left on upcoming crossroad, report arrival. (Version 1)", r_list(testRobot)));
       
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, p_list(p(-8+biasX*3,6+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Turning left on crossroad 2", "Start at a waypoint, go left on upcoming crossroad, report arrival. (Version 2)", r_list(testRobot)));

//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
//         testRobot = new TestRobotSpecification(robots, p(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, p_list(p(-6+biasX*3,7+biasY*3)));
//         testRobot.setRequireArrived(true);
//         testScenarios.add(new ConcreteTestScenario("Turning left on crossroad 3", "Start at a waypoint, go left on upcoming crossroad, report arrival. (Version 3)", r_list(testRobot)));
      
// 		return testScenarios;
// 	}

// 	private static List<TestScenario> generateEnteringCrossroadTests(WarehouseManager robots) {
// 		List<TestScenario> testScenarios = new ArrayList<>();
		
// 		int biasX = rand.nextInt(5);
// 		int biasY = rand.nextInt(5);
// 		List<EntitySpecification<?>> newRobots = new ArrayList<>();
// 		List<Position> targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-7+biasX*3,7+biasY*3));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-7+biasX*3, 6+biasY*3), RobotState.TO_UNLOADING, Orientation.NORTH, targetsRobotOne, 1000, 3000));
//         testScenarios.add(new ConcreteTestScenario("Entering crossroad 1", "Entering a crossroad from the north. No arrival message should be send.", newRobots));
        
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
// 		newRobots = new ArrayList<>();
// 		targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-8+biasX*3,8+biasY*3));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-8+biasX*3, 9+biasY*3), RobotState.TO_UNLOADING, Orientation.SOUTH, targetsRobotOne, 1000, 3000));
//         testScenarios.add(new ConcreteTestScenario("Entering crossroad 2", "Entering a crossroad from the south. No arrival message should be send.", newRobots));
        
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
// 		newRobots = new ArrayList<>();
// 		targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-8+biasX*3,7+biasY*3));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-9+biasX*3, 7+biasY*3), RobotState.TO_UNLOADING, Orientation.EAST, targetsRobotOne, 1000, 3000));
//         testScenarios.add(new ConcreteTestScenario("Entering crossroad 3", "Entering a crossroad from the east. No arrival message should be send.", newRobots));
        
//         biasX = rand.nextInt(5);
// 		biasY = rand.nextInt(5);
// 		newRobots = new ArrayList<>();
// 		targetsRobotOne = new ArrayList<>();
//         targetsRobotOne.add(new Position(-7+biasX*3,8+biasY*3));
//         newRobots.add(new TestRobotSpecification(robots, new Position(-6+biasX*3, 8+biasY*3), RobotState.TO_UNLOADING, Orientation.WEST, targetsRobotOne, 1000, 3000));
//         testScenarios.add(new ConcreteTestScenario("Entering crossroad 4", "Entering a crossroad from the west. No arrival message should be send.", newRobots));
        
//         return testScenarios;
// 	}
	

      private static List<EntitySpecification<?>> e_list(EntitySpecification<?>... e) {
          return new ArrayList<>(Arrays.asList(e));
      }
      
      private class SimpleTrafficLightTestScenario extends TestScenario {
    		
    		List<EntitySpecification<?>> customEntities = new ArrayList<>();
    	 	SimpleTrafficWorldConfiguration.GridMode gridMode;
    	 	boolean autoamticTrafficLights;
    	 	boolean autoamticArrivalPoints;
    	 	boolean autoamticDeparturePoints;

    	     public SimpleTrafficLightTestScenario(String name, String description, List<EntitySpecification<?>> customEntities, 
    	    		 GridMode gridMode, boolean autoamticTrafficLights, boolean autoamticArrivalPoints, boolean autoamticDeparturePoints) {
    	         this.name = name;
    	         this.description = description;
    	         this.customEntities = customEntities;
    	         this.gridMode = gridMode;
    	         this.autoamticTrafficLights = autoamticTrafficLights;
    	         this.autoamticArrivalPoints = autoamticArrivalPoints;
    	         this.autoamticDeparturePoints = autoamticDeparturePoints;
    	     }

    	     @Override
    	     public List<EntitySpecification<?>> getScenarioEntities() {
    	         List<EntitySpecification<?>> entites = createAutomaticEntities();
    	         entites.addAll(customEntities);
    	         return entites;
    	     }     

    		 private List<EntitySpecification<?>> createAutomaticEntities() {
    			// Initialize list
    			List<EntitySpecification<?>> list = new ArrayList<>();
    			
    			// Add Traffic Light Specifications to list
    			if(autoamticTrafficLights) {
    				for(int x = 0 ; x<SimpleTrafficWorldConfiguration.getVerticalStreets() ; x++) {
    					for(int y = 0 ; y<SimpleTrafficWorldConfiguration.getHorizontalStreets() ; y++) {
    						list.add(new TrafficLightSpecification(new RelativePosition(x, y), world.getStreetNetworkManager()));
    					}
    				}
    			}
    			
    			// Add Arrival  Point specifications to list
    			if(autoamticArrivalPoints) {
    				for (int i=0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints() ; i++) {
    					list.add(new DeparturePointSpecification(i, world.getStreetNetworkManager()));
    				}
    			}
    			
    			// Add  Departure Point specifications to list
    			if(autoamticDeparturePoints) {
    				for (int i=0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints() ; i++) {
    					list.add(new ArrivalPointSpecification(i, world.getStreetNetworkManager()));
    				}
    			}
    			
    			// Return List
    			return list; 
    		}
    		
    		
    	     @Override
    	     public void loadScenario(World world) {
    	     	if(gridMode != null) {
    	     		SimpleTrafficWorldConfiguration.setCrossroadsMode(gridMode);
    	     		world.configurationChanged(); 
    	     	}
    	     	super.loadScenario(world);
    	     }
    	 }

 }
