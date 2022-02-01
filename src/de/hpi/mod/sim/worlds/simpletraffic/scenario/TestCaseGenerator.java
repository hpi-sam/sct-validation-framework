package de.hpi.mod.sim.worlds.simpletraffic.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorld;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration.GridMode;
import de.hpi.mod.sim.worlds.simpletraffic.entities.RelativePosition;
import de.hpi.mod.sim.worlds.simpletraffic.entities.TrafficLightWithStaticColors;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.ArrivalPointSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.DeparturePointSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.SimpleRobotSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.SimpleTestRobotSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.TestTrafficLightSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.TrafficLightSpecification;

public class TestCaseGenerator {

	private SimpleTrafficWorld world;

	public TestCaseGenerator(SimpleTrafficWorld world) {
		this.world = world;
	}

	public Map<String, List<TestScenario>> getAllTestCases() {
		Map<String, List<TestScenario>> testGroups = new LinkedHashMap<>();

		testGroups.put("I. Simple Driving on Streets", generateSimpleDrivingTests(this.world.getStreetNetworkManager()));
		testGroups.put("II. Driving with Obstacles", generateDrivingWithObstaclesTests(this.world.getStreetNetworkManager())); 
		testGroups.put("III. Driving with Traffic Lights", generateDrivingAtTrafficLightTests(this.world.getStreetNetworkManager()));
		testGroups.put("IV. Complete Route Driving", generateCompleteRouteTests(this.world.getStreetNetworkManager()));
		return testGroups;
	}



	private List<TestScenario> generateSimpleDrivingTests(TrafficGridManager manager) {

		// Start list of test scenarios for this group
		List<TestScenario> testScenarios = new ArrayList<>();


		testScenarios.add(new SimpleTrafficTestScenario(
				"Drive on empty Street 1", "Robot starts on a street and drives two fields ahead in east direction.",
				e_list(new SimpleTestRobotSpecification(manager, p(10,7), Orientation.EAST, p(12,7))), 
				GridMode.TWO_CROSSROADS, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Drive on empty Street 2", "Robot starts on a street and drives four fields ahead in south direction, starting at an departure point.",
				e_list(new SimpleTestRobotSpecification(manager, p(14,21), Orientation.SOUTH, p(14,17))), 
				GridMode.TWO_CROSSROADS, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Drive on empty Street 3", "Robot starts on a street and drives five fields ahead in west direction, ending at an arrival point.",
				e_list(new SimpleTestRobotSpecification(manager, p(6,15), Orientation.WEST, p(1,15))), 
				GridMode.TWO_CROSSROADS, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Drive on empty Street 4", "Robot starts on a street and drives five fields ahead in north direction, starting on a crossroad.",
				e_list(new SimpleTestRobotSpecification(manager, p(8,7), Orientation.NORTH, p(8,12))), 
				GridMode.TWO_CROSSROADS, false, false, false));

		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Drive long distance 1", "Robot starts on a street and drives three fields ahead in west direction, accross diabeled traffic light.",
				e_list(new SimpleTestRobotSpecification(manager, p(21,8), Orientation.WEST, p(1,8))), 
				GridMode.TWO_CROSSROADS, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Drive long distance 2", "Robot starts on a street and drives three fields ahead in north direction, starting at an departure point.",
				e_list(new SimpleTestRobotSpecification(manager, p(15,1), Orientation.NORTH, p(15,21))), 
				GridMode.TWO_CROSSROADS, false, false, false));

		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Report arrived 1", "Robot dives a shout route ahead in east direction and reports arrive() when finished.",
				e_list(new SimpleTestRobotSpecification(manager, p(10,14), Orientation.EAST, p(12,14), true)), 
				GridMode.TWO_CROSSROADS, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Report arrived 2", "Robot dives a long route ahead in south direction and reports arrive() when finished.",
				e_list(new SimpleTestRobotSpecification(manager, p(7,21), Orientation.SOUTH, p(7,1), true)), 
				GridMode.TWO_CROSSROADS, false, false, false));

		return testScenarios;
	}




	private List<TestScenario> generateDrivingWithObstaclesTests(TrafficGridManager manager) {

		// Start list of test scenarios for this group
		List<TestScenario> testScenarios = new ArrayList<>();

		testScenarios.add(new SimpleTrafficTestScenario(
				"Don't crash into Wall 1", "Robot does not drive ahead for at least five seconds if starting directly in front of a wall, even if target is ahead.",
				e_list(new SimpleTestRobotSpecification(manager, p(14,11), Orientation.WEST, p(12,11), false, p(14,11), 5000, 7000 )), 
				GridMode.TWO_CROSSROADS, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Don't crash into Wall 2", "If after driving ahead, Robot encounters a wall ahead, it does not advance further for at least five seconds, even if target is ahead.",
				e_list(new SimpleTestRobotSpecification(manager, p(7,4), Orientation.SOUTH, p(7,-1), false, p(7,1), 5000, 7000)), 
				GridMode.TWO_CROSSROADS, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Don't crash into Wall 3", "If after driving ahead, Robot encounters a wall ahead, it does not advance further for at least five seconds, even if target is ahead.",
				e_list(new SimpleTestRobotSpecification(manager, p(5,7), Orientation.NORTH, p(5,9), false, p(5,8), 5000, 7000)), 
				GridMode.TWO_CROSSROADS, false, false, false));


		testScenarios.add(new SimpleTrafficTestScenario(
				"Don't crash into Robot 1", "Robot does not drive ahead for at least five seconds if another robot is blocking the path ahead of it.",
				e_list( new SimpleTestRobotSpecification(manager, p(5,14), Orientation.EAST, p(8,14), false, p(5,14), 5000, 7000 ),
						new SimpleTestRobotSpecification(manager, p(6,14), Orientation.NORTH )), 
				GridMode.TWO_CROSSROADS, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Don't crash into Robot 2", "Robot does not drive ahead for at least five seconds if another robot is blocking the path ahead of it.",
				e_list( new SimpleTestRobotSpecification(manager, p(21,8), Orientation.WEST, p(8,8), false, p(17,8), 5000, 7000 ),
						new SimpleTestRobotSpecification(manager, p(16,8), Orientation.EAST )), 
				GridMode.TWO_CROSSROADS, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Don't crash into Robot 3", "Robot does not drive ahead for at least five seconds if another robot is blocking the path ahead of it.",
				e_list( new SimpleTestRobotSpecification(manager, p(8,10), Orientation.NORTH, p(8,20), false, p(8,13), 5000, 7000 ),
						new SimpleTestRobotSpecification(manager, p(8,14), Orientation.NORTH ),
						new SimpleTestRobotSpecification(manager, p(8,15), Orientation.WEST ),
						new SimpleTestRobotSpecification(manager, p(7,14), Orientation.EAST ),
						new SimpleTestRobotSpecification(manager, p(7,15), Orientation.SOUTH )), 
				GridMode.TWO_CROSSROADS, false, false, false));
		
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving behind each other 1", "Robots can drive behind each other without crashing.",
				e_list( new SimpleTestRobotSpecification(manager, p(15,21), Orientation.SOUTH, p(15,4) ),
						new SimpleTestRobotSpecification(manager, p(15,20), Orientation.SOUTH, p(15,3) ),
						new SimpleTestRobotSpecification(manager, p(15,19), Orientation.SOUTH, p(15,2) ),
						new SimpleTestRobotSpecification(manager, p(15,18), Orientation.SOUTH, p(15,1) )), 
				GridMode.TWO_CROSSROADS, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving behind each other 2", "Robots can drive behind each other without crashing.",
				e_list( new SimpleTestRobotSpecification(manager, p(2,14), Orientation.EAST, p(22,14), false, p(19,14), 8000, 16000 ),
						new SimpleTestRobotSpecification(manager, p(4,14), Orientation.EAST, p(20,14) ),
						new SimpleTestRobotSpecification(manager, p(18,15), Orientation.WEST, p(2,15), false, p(3,15), 8000, 16000 ),
						new SimpleTestRobotSpecification(manager, p(17,15), Orientation.WEST, p(2,15) )), 
				GridMode.TWO_CROSSROADS, false, false, false));
		
		return testScenarios;
	}


	
	private List<TestScenario> generateDrivingAtTrafficLightTests(TrafficGridManager manager) {

		// Start list of test scenarios for this group
		List<TestScenario> testScenarios = new ArrayList<>();

		testScenarios.add(new SimpleTrafficTestScenario(
				"Wait in front of red Traffic Light 1", "Robot coming from south direction stop in front of a red traffic light and waits there.",
				e_list( new SimpleTestRobotSpecification(manager, p(8,1), Orientation.NORTH, p(8,14), false, p(8,6), 7000, 10000),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Wait in front of red Traffic Light 2", "Robot coming from west direction stop in front of a red traffic light and waits there.",
				e_list( new SimpleTestRobotSpecification(manager, p(1,7), Orientation.EAST, p(14,7), false, p(6,7), 7000, 10000),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Wait in front of red Traffic Light 3", "Two Robots coming from north direction stop in front of a red traffic light and wait there.",
				e_list( new SimpleTestRobotSpecification(manager, p(7,14), Orientation.SOUTH, p(7,1), false, p(7,10), 7000, 10000),
						new SimpleTestRobotSpecification(manager, p(7,13), Orientation.SOUTH, p(7,1), false, p(7,9), 7000, 10000),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));

		testScenarios.add(new SimpleTrafficTestScenario(
				"Wait in front of red Traffic Light 4", "Two Robots coming from east direction stop in front of a red traffic light and wait there.",
				e_list( new SimpleTestRobotSpecification(manager, p(14,8), Orientation.WEST, p(1,8), false, p(10,8), 7000, 10000),
						new SimpleTestRobotSpecification(manager, p(13,8), Orientation.WEST, p(1,8), false, p(9,8), 7000, 10000),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));

		
		
		
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving ahead on Crossroad 1", "Robot coming from south drives ahead while traffic light is green.",
				e_list( new SimpleTestRobotSpecification(manager, p(8,3), Orientation.NORTH, p(8,12), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), true)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving ahead on Crossroad 2", "Robot coming from west drives ahead while traffic light is green.",
				e_list( new SimpleTestRobotSpecification(manager, p(3,7), Orientation.EAST, p(12,7), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), true)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving ahead on Crossroad 3", "Robot coming from north drives ahead while traffic light is green.",
				e_list( new SimpleTestRobotSpecification(manager, p(7,12), Orientation.SOUTH, p(7,3), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), true)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));

		
		
		
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving right on Crossroad 1", "Robot coming from east drives to crossroad, passes green traffic light, turns right and drives on ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(12,8), Orientation.WEST, p(8,12), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false, true, false, false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving right on Crossroad 2", "Robot coming from north drives to crossroad, passes green traffic light, turns right and drives on ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(7,12), Orientation.SOUTH, p(3,8), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), true, false, false, false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving right on Crossroad 3", "Robot coming from west drives to crossroad, passes green traffic light, turns right and drives on ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(3,7), Orientation.EAST, p(7,3), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false, false, false, true)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));

		
		
		
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving left on Crossroad 1", "Robot coming from south drives to crossroad, passes green traffic light, turns right and drives on ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(8,3), Orientation.NORTH, p(3,8), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false, false, true, false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving left on Crossroad 2", "Robot coming from west drives to crossroad, passes green traffic light, turns right and drives on ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(3,7), Orientation.EAST, p(8,12), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false, false, false, true)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Driving left on Crossroad 3", "Robot coming from north drives to crossroad, passes green traffic light, turns right and drives on ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(7,12), Orientation.SOUTH, p(12,7), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), true, false, false, false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		


		
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Multiple robots on Crossroad 1", "Three Robots coming from east drive to crossroad, pass green traffic light and turn or drive ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(14,8), Orientation.WEST, p(8,12), false),
						new SimpleTestRobotSpecification(manager, p(13,8), Orientation.WEST, p(3,8), false),
						new SimpleTestRobotSpecification(manager, p(12,8), Orientation.WEST, p(7,3), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false, true, false, false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		
		testScenarios.add(new SimpleTrafficTestScenario(
				"Multiple robots on Crossroad 2 ", "Three Robots coming from south drive to crossroad, pass green traffic light and turn or drive ahead.",
				e_list( new SimpleTestRobotSpecification(manager, p(8,1), Orientation.NORTH, p(3,8), false),
						new SimpleTestRobotSpecification(manager, p(8,2), Orientation.NORTH, p(12,7), false),
						new SimpleTestRobotSpecification(manager, p(8,3), Orientation.NORTH, p(8,12), false),
						new TestTrafficLightSpecification(new RelativePosition(0,0), world.getStreetNetworkManager(), false, false, true, false)), 
				GridMode.SINGLE_CROSSROAD, false, false, false));
		
		
		return testScenarios;
	}
	
	

	private List<TestScenario> generateCompleteRouteTests(TrafficGridManager manager) {

		// Start list of test scenarios for this group
		List<TestScenario> testScenarios = new ArrayList<>();

		// Define Variables
		SimpleRobotSpecification testRobot;
		ArrivalPointSpecification testArrivalPoint;
		DeparturePointSpecification testDeparturePoint;


		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(1, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(4, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Straigt Ahead 1", "Robot starts at top position of left side and has to drive straight to top position of right side.",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(7, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(2, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Straigt Ahead 2", "Robot starts at left position of bottom side and has to drive straight to left position of top side.",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(5, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(0, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Straigt Ahead 3", "Robot starts at bottom position of right side and has to drive straight to bottom position of left side.",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));




		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(0, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(7, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Right 1", "Robot starts at bottom position of left side and has to drive to left position of bottom side (i.e. turn right once).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(2, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(5, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Right 2", "Robot starts at left position of top side and has to drive to bottom position of left side (i.e. drive straigt once, then turn right).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(7, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(5, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Right 3", "Robot starts at left position of bottom side and has to drive to bottom position of right side (i.e. turn right once, then drive straigt).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(5, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(2, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Right 4", "Robot starts at bottom position of right side and has to drive to left position of top side (i.e. drive straigt once, turn right once, then drive straigt again or alternatively drive left, right, left).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(4, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(2, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Right 4 EXTRA", "Robot starts at bottom position of right side and has to drive to left position of top side (i.e. drive straigt once, turn right once, then drive straigt again or alternatively drive left, right, left).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 





		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(1, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(2, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Left 1", "Robot starts at top position of left side and has to drive to left position of top side (i.e. turn left once).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(3, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(5, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Left 2", "Robot starts at right position of top side and has to drive to bottom position of right side (i.e. drive straigt once, then turn left).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(5, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(7, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Left 3", "Robot starts at bottom position of right side and has to drive to left position of bottom side (i.e. turn left once, then drive straigt).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(6, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(1, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Left 4", "Robot starts at right position of bottom side and has to drive to top position of left side (i.e. drive straigt once, turn left once, then drive straigt again or alternatively drive left, right, left).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false)); 




		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(5, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(1, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Opposite Ahead 1", "Robot starts at bottom position of right side and has to drive top position of left side (ie. turn right once and then left at some point to switch lanes).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(3, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(7, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Opposite Ahead 2", "Robot starts at right position of top side and has to drive left position of bottom side (ie. turn left once and then right at some point to switch lanes).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));



		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(0, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(1, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Behind 1", "Robot starts at bottom position of left side and has to drive to top position of left side (i.e. turn left twice).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(3, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(4, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Behind 2", "Robot starts at right position of top side and has to drive to left position of top side (i.e. turn right twice).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));

		testRobot = new SimpleRobotSpecification(manager);
		testDeparturePoint = new DeparturePointSpecification(4, manager, false, true);
		testArrivalPoint = new ArrivalPointSpecification(5, manager, false, true);
		testScenarios.add(new SimpleTrafficTestScenario(
				"Target is Behind 3", "Robot starts at top position of right side and has to drive to bottom position of right side (i.e. turn left twice).",
				e_list(testRobot, testArrivalPoint, testDeparturePoint), 
				GridMode.TWO_CROSSROADS, true, false, false));

		return testScenarios;
	}




	private static List<EntitySpecification<?>> e_list(EntitySpecification<?>... e) {
		return new ArrayList<>(Arrays.asList(e));
	}

	private static Position p(int x, int y) {
		return new Position(x,y);
	}

	private class SimpleTrafficTestScenario extends TestScenario {

		List<EntitySpecification<?>> customEntities = new ArrayList<>();
		SimpleTrafficWorldConfiguration.GridMode gridMode;
		boolean autoamticTrafficLights;
		boolean autoamticArrivalPoints;
		boolean autoamticDeparturePoints;

		public SimpleTrafficTestScenario(String name, String description, List<EntitySpecification<?>> customEntities, 
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
					list.add(new DeparturePointSpecification(i, world.getStreetNetworkManager(), false, true));
				}
			}

			// Add  Departure Point specifications to list
			if(autoamticDeparturePoints) {
				for (int i=0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints() ; i++) {
					list.add(new ArrivalPointSpecification(i, world.getStreetNetworkManager(), false, true));
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
			SimpleTrafficWorldConfiguration.setShowStatistics(false);
			super.loadScenario(world);
		}
	}

}
