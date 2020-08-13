package de.hpi.mod.sim.environment.robot;

import org.junit.Test;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.DriveManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IDriveListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
        Configuration.setEntitySpeedLevel(2);  // Move 1 cell per second

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

    private class DummyDriveHandler implements IDriveListener {

        private int callCount = 0;

        @Override
        public void actionCompleted() {
            callCount++;
        }

        int getCallCount() {
            return callCount;
        }
    }
}