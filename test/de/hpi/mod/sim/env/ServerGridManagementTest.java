package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.model.Direction;
import de.hpi.mod.sim.env.model.IRobotController;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ServerGridManagementTest {

    private ServerGridManagement getGridWithRobotsAtPositions(Position[] pos) {
        IRobotController control = new DummyRobotController(pos);
        return new ServerGridManagement(control);
    }

    private ServerGridManagement getGrid() {
        return new ServerGridManagement(new DummyRobotController());
    }

    @Test
    public void cellType() {
        ServerGridManagement grid = getGrid();
        assertEquals(CellType.BLOCK, grid.cellType(new Position(0, 0)));
        assertEquals(CellType.BLOCK, grid.cellType(new Position(0, 3)));
        assertEquals(CellType.BLOCK, grid.cellType(new Position(-3, 3)));

        assertEquals(CellType.LOADING, grid.cellType(new Position(2, 0)));
        assertEquals(CellType.LOADING, grid.cellType(new Position(5, 0)));
        assertEquals(CellType.LOADING, grid.cellType(new Position(-1, 0)));

        assertEquals(CellType.BATTERY, grid.cellType(new Position(0, -2)));
        assertEquals(CellType.BATTERY, grid.cellType(new Position(0, -3)));
        assertEquals(CellType.BATTERY, grid.cellType(new Position(0, -4)));
        assertEquals(CellType.BATTERY, grid.cellType(new Position(3, -4)));
        assertEquals(CellType.BATTERY, grid.cellType(new Position(-3, -4)));

        assertEquals(CellType.WAYPOINT, grid.cellType(new Position(0, 1)));
        assertEquals(CellType.WAYPOINT, grid.cellType(new Position(0, 2)));
        assertEquals(CellType.WAYPOINT, grid.cellType(new Position(-3, 2)));
        assertEquals(CellType.WAYPOINT, grid.cellType(new Position(3, 2)));
        assertEquals(CellType.WAYPOINT, grid.cellType(new Position(2, 3)));
        assertEquals(CellType.WAYPOINT, grid.cellType(new Position(1, 3)));
        assertEquals(CellType.WAYPOINT, grid.cellType(new Position(-1, 3)));

        assertEquals(CellType.CROSSROAD, grid.cellType(new Position(1, 1)));
        assertEquals(CellType.CROSSROAD, grid.cellType(new Position(1, 2)));
        assertEquals(CellType.CROSSROAD, grid.cellType(new Position(2, 1)));
        assertEquals(CellType.CROSSROAD, grid.cellType(new Position(2, 2)));
        assertEquals(CellType.CROSSROAD, grid.cellType(new Position(-2, 2)));
        assertEquals(CellType.CROSSROAD, grid.cellType(new Position(-2, 5)));
        assertEquals(CellType.CROSSROAD, grid.cellType(new Position(4, 5)));

    }

    @Test
    public void isBlockedByRobot() {
        var grid = getGridWithRobotsAtPositions(new Position[] {
                new Position(1, 1)
        });
        assertTrue("", grid.isBlockedByRobot(new Position(1, 1)));
        assertFalse("", grid.isBlockedByRobot(new Position(1, 2)));
    }

    @Test
    public void isBlockedByMap() {
        var grid = getGrid();
        assertTrue("", grid.isBlockedByMap(new Position(0, 0)));
        assertTrue("", grid.isBlockedByMap(new Position(0, -1)));
        assertFalse("", grid.isBlockedByMap(new Position(0, -2)));
        assertFalse("", grid.isBlockedByMap(new Position(0, -3)));
        assertFalse("", grid.isBlockedByMap(new Position(0, -4)));
        assertTrue("", grid.isBlockedByMap(new Position(0, -5)));
        assertTrue("", grid.isBlockedByMap(new Position(0, -6)));
        assertTrue("", grid.isBlockedByMap(new Position(0, 3)));
        assertTrue("", grid.isBlockedByMap(new Position(3, 3)));
        assertTrue("", grid.isBlockedByMap(new Position(-3, 3)));
    }

    @Test
    public void blocked() {
        var grid = getGridWithRobotsAtPositions(new Position[] {
                new Position(2, 1)
        });
        assertArrayEquals(new boolean[] { false, false, false, true
        }, grid.blocked(Orientation.NORTH, new Position(0, 1)));
        assertArrayEquals(new boolean[] { false, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(1, 1)));
        assertArrayEquals(new boolean[] { true, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(1, 0)));
        assertArrayEquals(new boolean[] { true, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(1, -1)));
        assertArrayEquals(new boolean[] { false, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(1, -2)));
        assertArrayEquals(new boolean[] { false, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(1, -3)));
        assertArrayEquals(new boolean[] { false, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(1, -4)));
        assertArrayEquals(new boolean[] { true, false, false, true
        }, grid.blocked(Orientation.NORTH, new Position(1, -5)));
        assertArrayEquals(new boolean[] { false, false, true, true
        }, grid.blocked(Orientation.NORTH, new Position(2, -5)));
        assertArrayEquals(new boolean[] { true, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(2, -4)));
        assertArrayEquals(new boolean[] { true, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(2, -3)));
        assertArrayEquals(new boolean[] { true, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(2, -2)));
        assertArrayEquals(new boolean[] { true, false, true, false
        }, grid.blocked(Orientation.NORTH, new Position(2, -1)));
        assertArrayEquals(new boolean[] { true, true, true, false
        }, grid.blocked(Orientation.NORTH, new Position(2, 0)));
        assertArrayEquals(new boolean[] { false, true, true, true
        }, grid.blocked(Orientation.WEST, new Position(2, 0)));

        assertArrayEquals(new boolean[] { false, false, false, true
        }, grid.blocked(Orientation.NORTH, new Position(3, 4)));
    }

    @Test
    public void blockedWaypoint() {
        var grid = getGridWithRobotsAtPositions(new Position[] {
                new Position(3, 4),
                new Position(4, 6),
                new Position(6, 4)
        });
        // #     # b   #
        //
        //       a   2 c
        // #     #   1 #
        //
        //
        // #     #     #
        // ^(0, 0)
        assertArrayEquals(new boolean[] { true, true, false
        }, grid.blockedWaypoint(Orientation.NORTH, new Position(5, 3)));
        assertArrayEquals(new boolean[] { false, false, true
        }, grid.blockedWaypoint(Orientation.NORTH, new Position(5, 4)));
    }

    @Test
    public void blockedCrossroadAhead() {
        var grid = getGridWithRobotsAtPositions(new Position[] {
                new Position(-2, 5),  // Left Top
                new Position(1, 4),   // Left Bottom
                new Position(2, 8),   // Right Top
        });
        assertTrue("", grid.blockedCrossroadAhead(Orientation.NORTH, new Position(-1, 3)));
        assertTrue("", grid.blockedCrossroadAhead(Orientation.NORTH, new Position(-1, 4)));
        assertTrue("", grid.blockedCrossroadAhead(Orientation.NORTH, new Position(2, 6)));
    }

    @Test
    public void blockedCrossroadRight() {
        var grid = getGridWithRobotsAtPositions(new Position[] {
                new Position(-2, 5)  // Left Top
        });
        // #     #
        //   O
        // 2
        // #   1 # <- (0, 3)
        assertTrue("1", grid.blockedCrossroadRight(Orientation.WEST, new Position(-1, 3)));
        assertTrue("2", grid.blockedCrossroadRight(Orientation.NORTH, new Position(-3, 4)));
    }

    @Test
    public void getWaypointOfCrossroad() {
        var grid = getGrid();
        // # b   #
        //       c
        // a 1
        // #   d #
        // ^(0, 3)
        assertEquals(new Position(0, 4), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.WEST, false));
        assertEquals(new Position(1, 6), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.NORTH, false));
        assertEquals(new Position(3, 5), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.EAST, false));
        assertEquals(new Position(2, 3), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.SOUTH, false));

        assertEquals(new Position(0, 5), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.WEST, true));
        assertEquals(new Position(2, 6), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.NORTH, true));
        assertEquals(new Position(3, 4), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.EAST, true));
        assertEquals(new Position(1, 3), grid.getWaypointOfCrossroad(new Position(1, 4), Orientation.SOUTH, true));
    }

    @Test
    public void getSouthwestCornerOfUpcomingCrossroad() {
        var grid = getGrid();
        // #     #     # 4   #
        //
        // 3 c           d
        // #     #     #     #
        //       2
        //   b     a 
        // #     #   1 #     #
        //       ^(0, 0)
        assertEquals(new Position(1, 1), grid.getSouthwestCornerOfUpcomingCrossroad(Orientation.NORTH, new Position(2, 0)));
        assertEquals(new Position(-2, 1), grid.getSouthwestCornerOfUpcomingCrossroad(Orientation.WEST, new Position(0, 2)));
        assertEquals(new Position(-2, 4), grid.getSouthwestCornerOfUpcomingCrossroad(Orientation.EAST, new Position(-3, 4)));
        assertEquals(new Position(4, 4), grid.getSouthwestCornerOfUpcomingCrossroad(Orientation.SOUTH, new Position(4, 6)));
    }

    @Test
    public void getSouthwestCornerOfCrossroad() {
        var grid = getGrid();
        // #     #     #     #
        //               4
        //  3c           d
        // #     #     #     #
        //     2
        //   b     a 1
        // #     #     #     #
        //       ^(0, 0)
        assertEquals(new Position(1, 1), grid.getSouthwestCornerOfCrossroad(new Position(2, 1)));
        assertEquals(new Position(-2, 1), grid.getSouthwestCornerOfCrossroad(new Position(-1, 2)));
        assertEquals(new Position(-2, 4), grid.getSouthwestCornerOfCrossroad(new Position(-2, 4)));
        assertEquals(new Position(4, 4), grid.getSouthwestCornerOfCrossroad(new Position(4, 5)));
        assertEquals(new Position(-5, 1), grid.getSouthwestCornerOfCrossroad(new Position(-4, 2)));
    }

    
    @Test
    public void getSouthwestCornerOfWaypoint() {
        var grid = getGrid();
        // ##     ##     ##     ##
        //               e
        // 3c            5
        // ##     ## 4 d ##     ##
        // b   
        // 2      
        // ##     ## 1a  ##     ##
        //         ^(0, 0)
        assertEquals(new Position(1, 0), grid.getSouthwestCornerOfWaypoint(new Position(1, 0)));
        assertEquals(new Position(-3, 1), grid.getSouthwestCornerOfWaypoint(new Position(-3, 2)));
        assertEquals(new Position(-3, 4), grid.getSouthwestCornerOfWaypoint(new Position(-3, 4)));
        assertEquals(new Position(1, 3), grid.getSouthwestCornerOfWaypoint(new Position(2, 3)));
        assertEquals(new Position(3, 4), grid.getSouthwestCornerOfWaypoint(new Position(3, 5)));
    }

    
    @Test
    public void getCellsOfCrossroad() {
    }

    @Test
    public void getArrivalPositionAtStation() {
        var grid = getGrid();
        assertEquals(new Position(1, 0), grid.getArrivalPositionAtStation(0));
        assertEquals(new Position(4, 0), grid.getArrivalPositionAtStation(1));
        assertEquals(new Position(7, 0), grid.getArrivalPositionAtStation(2));
    }

    @Test
    public void getChargerPositionAtStation() {
        var grid = getGrid();
        assertEquals(new Position(0, -2), grid.getChargerPositionAtStation(0, 0));
        assertEquals(new Position(3, -3), grid.getChargerPositionAtStation(1, 1));
        assertEquals(new Position(6, -4), grid.getChargerPositionAtStation(2, 2));
    }

    @Test
    public void getLoadingPositionAtStation() {
        var grid = getGrid();
        assertEquals(new Position(2, 0), grid.getLoadingPositionAtStation(0));
        assertEquals(new Position(5, 0), grid.getLoadingPositionAtStation(1));
    }

    @Test
    public void getUnloadingPositionFromID() {

    }

    @Test
    public void targetDirectionInStationFacingNorth() {
    	// Robot at (-8,0), facing north:

        // Expected results:    	
        // A A A A A
        // A A A A A
        // L L ^ R R
        // B B B B B
        // B B B B B
    	
        var grid = getGrid();
        
        // 24 direct neightbours (5x5 grid, except self)
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-10,2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-10,1)));
        assertEquals(Direction.LEFT, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-10,0)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-10,-1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-10,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-9,2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-9,1)));
        assertEquals(Direction.LEFT, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-9,0)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-9,-1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-9,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-8,2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-8,1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-8,-1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-8,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-7,2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-7,1)));
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-7,0)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-7,-1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-7,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-6,2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-6,1)));
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-6,0)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-6,-1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-6,-2)));
                
        // far off positions
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-99,999)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.NORTH, p(-8,0), p(99,999)));
        assertEquals(Direction.LEFT, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-99999,0)));
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.NORTH, p(-8,0), p(5555,0)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(-99,-99)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.NORTH, p(-8,0), p(99,-99)));
        
        // interesting positions
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.NORTH, p(-8,0), p(0,0)));
    	
    }

    @Test
    public void targetDirectionInStationFacingEast() {
    	// Robot at (-8,0), facing north:

        // Expected results:    	
        // B B L A A
        // B B L A A
        // B B > A A
        // B B R A A
        // B B R A A
    	
        var grid = getGrid();
        
        // 24 direct neightbours (5x5 grid, except self)
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-2,-2)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-2,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-2,-4)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-2,-5)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-2,-6)));

        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-1,-2)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-1,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-1,-4)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-1,-5)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-1,-6)));

        assertEquals(Direction.LEFT,  grid.targetDirection(Orientation.EAST, p(0,-4), p(0,-2)));
        assertEquals(Direction.LEFT,  grid.targetDirection(Orientation.EAST, p(0,-4), p(0,-3)));
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.EAST, p(0,-4), p(0,-5)));
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.EAST, p(0,-4), p(0,-6)));

        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(1,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(1,-3)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(1,-4)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(1,-5)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(1,-6)));

        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(2,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(2,-3)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(2,-4)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(2,-5)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.EAST, p(0,-4), p(2,-6)));
                
        // far off positions
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-999,999)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.EAST, p(0,-4), p(999,999)));
        assertEquals(Direction.LEFT,   grid.targetDirection(Orientation.EAST, p(0,-4), p(0,88888888)));
        assertEquals(Direction.RIGHT,  grid.targetDirection(Orientation.EAST, p(0,-4), p(0,-88888888)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.EAST, p(0,-4), p(-999,-999)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.EAST, p(0,-4), p(999,-999)));
        
        // interesting positions
        assertEquals(Direction.LEFT, grid.targetDirection(Orientation.EAST, p(0,-4), p(0,0)));
    	
    }

    @Test
    public void targetDirectionInStationFacingSouth() {
        // Robot at (5,-5), facing west
        
        // Expected results:
        // B B B B B
        // B B B B B
        // R R v L L
        // A A A A A
        // A A A A A
    	
        var grid = getGrid();

        // 24 direct neightbours (5x5 grid, except self)
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(3,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(3,-4)));
        assertEquals(Direction.RIGHT,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(3,-5)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(3,-6)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(3,-7)));

        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(4,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(4,-4)));
        assertEquals(Direction.RIGHT,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(4,-5)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(4,-6)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(4,-7)));

        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(5,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(5,-4)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(5,-6)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(5,-7)));

        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(6,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(6,-4)));
        assertEquals(Direction.LEFT,   grid.targetDirection(Orientation.SOUTH, p(5,-5),p(6,-5)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(6,-6)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(6,-7)));

        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(7,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5),p(7,-4)));
        assertEquals(Direction.LEFT,   grid.targetDirection(Orientation.SOUTH, p(5,-5),p(7,-5)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(7,-6)));
        assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5),p(7,-7)));
         
        
		// far off positions
		assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5), p(-9999,9999)));
		assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5), p(9999,9999)));
		assertEquals(Direction.RIGHT,  grid.targetDirection(Orientation.SOUTH, p(5,-5), p(-11111,-5)));
		assertEquals(Direction.LEFT,   grid.targetDirection(Orientation.SOUTH, p(5,-5), p(11111,-5)));
		assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5), p(-9999,-9999)));
		assertEquals(Direction.AHEAD,  grid.targetDirection(Orientation.SOUTH, p(5,-5), p(9999,-9999)));
		
		// interesting positions
		assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.SOUTH, p(5,-5), p(0,0)));

    }

    @Test
    public void targetDirectionInStationFacingWest() {
        // Robot at (1,-3), facing west
        
        // Expected results:
        // A A R B B
        // A A R B B
        // A A < B B
        // A A L B B
        // A A L B B
    	
        var grid = getGrid();

        // 24 direct neightbours (5x5 grid, except self)
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3),p(-1,-1)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(-1,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(-1,-3)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(-1,-4)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(-1,-5)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(0,-1)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(0,-2)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(0,-3)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(0,-4)));
        assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(0,-5)));
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.WEST, p(1,-3), p(1,-1)));
        assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.WEST, p(1,-3), p(1,-2)));
        assertEquals(Direction.LEFT, grid.targetDirection(Orientation.WEST, p(1,-3), p(1,-4)));
        assertEquals(Direction.LEFT, grid.targetDirection(Orientation.WEST, p(1,-3), p(1,-5)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(2,-1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(2,-2)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(2,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(2,-4)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(2,-5)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(3,-1)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(3,-2)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(3,-3)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(3,-4)));
        assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(3,-5)));
        
		// far off positions
		assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(-99,999)));
		assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(99,999)));
		assertEquals(Direction.RIGHT, grid.targetDirection(Orientation.WEST, p(1,-3), p(1,55555)));
		assertEquals(Direction.LEFT, grid.targetDirection(Orientation.WEST, p(1,-3), p(1,-55555)));
		assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(-99,-99)));
		assertEquals(Direction.BEHIND, grid.targetDirection(Orientation.WEST, p(1,-3), p(99,-99)));
		
		// interesting positions
		assertEquals(Direction.AHEAD, grid.targetDirection(Orientation.WEST, p(1,-3), p(0,0)));

    }
    

    @Test
    public void targetOrientation() {
        var grid = getGrid();

        // m >= 0
        // Loading = (3n+2, 0)
        // Unloading = (3n, 3m+1)
        // Station Entry = (3n+1, 0)
        // Battery = (3n, -2), (3n, -3), (3n, -4)
        // Station Depth = -5

        // Drive out of Station from Loading to Unloading
        // Always North because Robot has to exit Station before driving to WP
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, 0), p(3, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, 0), p(0, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, 0), p(-3, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, 0), p(3, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, 0), p(0, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, 0), p(-3, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 0), p(3, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 0), p(0, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 0), p(-3, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 0), p(3, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 0), p(0, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 0), p(-3, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, 0), p(3, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, 0), p(0, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, 0), p(-3, 1)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, 0), p(3, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, 0), p(0, 4)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, 0), p(-3, 4)));

        // Drive in Station to Loading
        // While driving down should be South because Robot has to drive to end of station first
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(1, -0), p(2, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(1, -1), p(2, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(1, -2), p(2, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(1, -3), p(2, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(1, -4), p(2, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(4, -0), p(5, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(4, -1), p(5, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(4, -2), p(5, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(4, -3), p(5, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(4, -4), p(5, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(-2, -0), p(-1, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(-2, -1), p(-1, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(-2, -2), p(-1, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(-2, -3), p(-1, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(-2, -4), p(-1, 0)));
        // At Bottom should be East
        assertEquals(Orientation.EAST, grid.targetOrientation(p(1, -5), p(2, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(4, -5), p(5, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(-2, -5), p(-1, 0)));
        // Driving up -> North
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, -1), p(2, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, -2), p(2, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, -3), p(2, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, -4), p(2, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(2, -5), p(2, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, -1), p(5, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, -2), p(5, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, -3), p(5, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, -4), p(5, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, -5), p(5, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, -1), p(-1, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, -2), p(-1, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, -3), p(-1, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, -4), p(-1, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(-1, -5), p(-1, 0)));

        // Drive out of Battery to Loading -> East
        assertEquals(Orientation.EAST, grid.targetOrientation(p(0, -2), p(2, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(0, -3), p(2, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(0, -4), p(2, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(3, -2), p(5, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(3, -3), p(5, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(3, -4), p(5, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(-3, -2), p(-1, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(-3, -3), p(-1, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(-3, -4), p(-1, 0)));

        // Drive from Loading to Unloading
        // From West
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 1), p(9, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 2), p(9, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 3), p(9, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 4), p(9, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 5), p(9, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(5, 6), p(9, 7)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(5, 7), p(9, 7)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(6, 7), p(9, 7)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(7, 7), p(9, 7)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(8, 7), p(9, 7)));
        // From East
        // Drive North to targetY+1, so Robot is still on right side of street
        // Drive West, do U-Turn on Crossroad after target
        // TODO: No U-Turn
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 1), p(3, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 2), p(3, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 3), p(3, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 4), p(3, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 5), p(3, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 6), p(3, 7)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 7), p(3, 7)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(8, 8), p(3, 7)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(7, 8), p(3, 7)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(6, 8), p(3, 7)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(5, 8), p(3, 7)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(4, 8), p(3, 7)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 8), p(3, 7)));

        // Drive from Unloading to Loading
        // From East
        assertEquals(Orientation.WEST, grid.targetOrientation(p(3, 7), p(7, 0)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(4, 7), p(7, 0)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(5, 7), p(7, 0)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(6, 7), p(7, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(7, 7), p(7, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(7, 6), p(7, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(7, 5), p(7, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(7, 4), p(7, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(7, 3), p(7, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(7, 2), p(7, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(7, 1), p(7, 0)));
        // From West
        assertEquals(Orientation.WEST, grid.targetOrientation(p(6, 7), p(3, 0)));
        assertEquals(Orientation.WEST, grid.targetOrientation(p(7, 7), p(3, 0)));
        assertEquals(Orientation.NORTH, grid.targetOrientation(p(8, 7), p(3, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(8, 8), p(3, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(7, 8), p(3, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(6, 8), p(3, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(5, 8), p(3, 0)));
        assertEquals(Orientation.EAST, grid.targetOrientation(p(4, 8), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 8), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 7), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 6), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 5), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 4), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 3), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 2), p(3, 0)));
        assertEquals(Orientation.SOUTH, grid.targetOrientation(p(3, 1), p(3, 0)));
    }

    private Position p(int x, int y) {
        return new Position(x, y);
    }

    private class DummyRobotController implements IRobotController {

        Position[] blocked;


        private DummyRobotController() {
            blocked = new Position[] {};
        }

        private DummyRobotController(Position[] blocked) {
            this.blocked = blocked;
        }

        @Override
        public boolean isBlockedByRobot(Position pos) {
            return Arrays.asList(blocked).contains(pos);
        }
    }
}