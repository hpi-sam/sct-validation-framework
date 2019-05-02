package de.hpi.mod.sim.env.robot;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class DriveManagerTest {

    @Test
    public void testConstruction() {
        Position initPosition = new Position(2, 5);
        Orientation initFacing = Orientation.EAST;

        DriveManager drive = new DriveManager(
                new DummyDriveHandler(), initPosition, initFacing);
        assertEquals(initFacing.getAngle(), drive.getAngle(), 1);
        assertEquals(initPosition.getX(), drive.getX(), 1);
        assertEquals(initPosition.getY(), drive.getY(), 1);
        assertEquals(initPosition, drive.currentPosition());
    }

    @Test
    public void testDriveForward() {
        Position initPosition = new Position(0, 0);
        Orientation initFacing = Orientation.NORTH;

        DummyDriveHandler handler = new DummyDriveHandler();
        DriveManager drive = new DriveManager(
                handler, initPosition, initFacing);
        SimulatorConfig.setRobotSpeedLevel(2);  // Move 1 cell per second

        for (int i = 0; i < 10; i++) {
            drive.driveForward();
            drive.update(1000);  // 1 second has elapsed

            Position shouldCurrent = new Position(initPosition.getX(), i+1);
            assertEquals(shouldCurrent.getX(), drive.getX(), .1);
            assertEquals(shouldCurrent.getY(), drive.getY(), .1);
            assertFalse("Should have stopped moving", drive.isMoving());
            assertEquals(shouldCurrent, drive.currentPosition());
            assertEquals(i+1, handler.getCallCount());
        }
    }

    @Test
    public void testTurnLeft() {
        Position initPosition = new Position(0, 0);
        Orientation initFacing = Orientation.SOUTH;

        DummyDriveHandler handler = new DummyDriveHandler();
        DriveManager drive = new DriveManager(
                handler, initPosition, initFacing);

        Orientation shouldFace = initFacing.getTurnedLeft();
        for (int i = 0; i < 10; i++) {
            drive.turnLeft();
            drive.update(1000);  // 1 second has elapsed

            assertEquals("Wrong angle in iteration " + i, shouldFace.getAngle(), drive.getAngle(), .1);
            assertFalse("Should have stopped turning", drive.isTurningLeft());
            assertEquals(shouldFace, drive.currentFacing());
            assertEquals(i+1, handler.getCallCount());

            shouldFace = shouldFace.getTurnedLeft();
        }
    }

    @Test
    public void testTurnRight() {
        Position initPosition = new Position(0, 0);
        Orientation initFacing = Orientation.SOUTH;

        DummyDriveHandler handler = new DummyDriveHandler();
        DriveManager drive = new DriveManager(
                handler, initPosition, initFacing);

        Orientation shouldFace = initFacing.getTurnedRight();
        for (int i = 0; i < 10; i++) {
            drive.turnRight();
            drive.update(1000);  // 1 second has elapsed

            assertEquals("Wrong angle in iteration " + i, shouldFace.getAngle(), drive.getAngle(), .1);
            assertFalse("Should have stopped turning", drive.isTurningRight());
            assertEquals(shouldFace, drive.currentFacing());
            assertEquals(i+1, handler.getCallCount());

            shouldFace = shouldFace.getTurnedRight();
        }
    }

    /* @Test
    public void testStartUnloading() {
        // TODO change DriveManager so a Timer can be injected
        fail("Not yet implemented");
    } */

    private class DummyDriveHandler implements DriveListener {

        private int callCount = 0;

        @Override
        public void actionCompleted() {
            callCount++;
        }

        @Override
        public void unloadingCompleted() {

        }

        int getCallCount() {
            return callCount;
        }
    }
}