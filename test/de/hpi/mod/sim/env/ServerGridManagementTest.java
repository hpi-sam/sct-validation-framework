package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.model.IRobotController;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

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
    public void getFacingCrossroad() {
        var grid = getGrid();
        // #     #     # 4   #
        //
        // 3 c           d
        // #     #     #     #
        //       2
        //   b     a
        // #     #   1 #     #
        //       ^(0, 0)
        assertEquals(new Position(1, 1), grid.getFacingCrossroad(Orientation.NORTH, new Position(2, 0)));
        assertEquals(new Position(-2, 1), grid.getFacingCrossroad(Orientation.WEST, new Position(0, 2)));
        assertEquals(new Position(-2, 4), grid.getFacingCrossroad(Orientation.EAST, new Position(-3, 4)));
        assertEquals(new Position(4, 4), grid.getFacingCrossroad(Orientation.SOUTH, new Position(4, 6)));
    }

    @Test
    public void getCrossroad() {
        var grid = getGrid();
        // #     #     #     #
        //               4
        //  3c           d
        // #     #     #     #
        //     2
        //   b     a 1
        // #     #     #     #
        //       ^(0, 0)
        assertEquals(new Position(1, 1), grid.getCrossroad(new Position(2, 1)));
        assertEquals(new Position(-2, 1), grid.getCrossroad(new Position(-1, 2)));
        assertEquals(new Position(-2, 4), grid.getCrossroad(new Position(-2, 4)));
        assertEquals(new Position(4, 4), grid.getCrossroad(new Position(4, 5)));
        assertEquals(new Position(-5, 1), grid.getCrossroad(new Position(-4, 2)));

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
    public void getQueuePositionAtStation() {
        var grid = getGrid();
        assertEquals(new Position(2, -1), grid.getQueuePositionAtStation(0, 0));
        assertEquals(new Position(2, -2), grid.getQueuePositionAtStation(0, 1));
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
    public void targetOrientation() {
        var grid = getGrid();

        // m >= 0
        // Loading = (3n+2, 0)
        // Unloading = (3n, 3m+1)
        // Station Entry = (3n+1, 0)
        // Battery = (3n, -2), (3n, -3), (3n, -4)
        // Station Depth = -5

        IntStream nRange = IntStream.range(-2, 3);
        IntStream mRange = IntStream.range(0, 3);
        IntStream depthRange = IntStream.range(0, -5); // Without last Position
        IntStream batteryRange = IntStream.range(-2, -5);

        // Drive out of Station from Loading to Unloading
        // Always North because Robot has to exit Station before driving to WP
        nRange.forEach(n1 ->
                nRange.forEach(n2 ->
                        mRange.forEach(m ->
                                assertEquals(Orientation.NORTH, grid.targetOrientation(loadP(n1), unloadP(n2, m))))));

        // Drive in Station to Loading
        // While driving down should be South because Robot has to drive to end of station first
        nRange.forEach(n ->
                depthRange.forEach(d ->
                        assertEquals(Orientation.SOUTH, grid.targetOrientation(stationWayDownP(n, d), loadP(n)))));
        // While driving up -> North

    }

    private Position loadP(int n) {
        return new Position(3*n+2, 0);
    }

    private Position unloadP(int n, int m) {
        return new Position(3*n, 3*m+1);
    }

    private Position stationWayDownP(int n, int depth) {
        return new Position(3*n+1, depth);
    }

    private Position stationWayUpP(int n, int depth) {
        return new Position(3*n+2, depth);
    }

    private Position batteryP(int n, int b) {
        return new Position(3*n, b);
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