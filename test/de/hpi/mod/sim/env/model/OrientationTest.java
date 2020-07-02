package de.hpi.mod.sim.env.model;

import org.junit.Test;

import de.hpi.mod.sim.setting.grid.Direction;
import de.hpi.mod.sim.setting.grid.Orientation;

import static org.junit.Assert.assertEquals;

public class OrientationTest {

    @Test
    public void rotate() {
        assertEquals(Orientation.NORTH, Orientation.rotate(Orientation.NORTH, Direction.AHEAD));
        assertEquals(Orientation.NORTH, Orientation.rotate(Orientation.WEST, Direction.RIGHT));
        assertEquals(Orientation.NORTH, Orientation.rotate(Orientation.SOUTH, Direction.BEHIND));
        assertEquals(Orientation.NORTH, Orientation.rotate(Orientation.EAST, Direction.LEFT));

        assertEquals(Orientation.WEST, Orientation.rotate(Orientation.WEST, Direction.AHEAD));
        assertEquals(Orientation.WEST, Orientation.rotate(Orientation.SOUTH, Direction.RIGHT));
        assertEquals(Orientation.WEST, Orientation.rotate(Orientation.EAST, Direction.BEHIND));
        assertEquals(Orientation.WEST, Orientation.rotate(Orientation.NORTH, Direction.LEFT));

        assertEquals(Orientation.SOUTH, Orientation.rotate(Orientation.SOUTH, Direction.AHEAD));
        assertEquals(Orientation.SOUTH, Orientation.rotate(Orientation.EAST, Direction.RIGHT));
        assertEquals(Orientation.SOUTH, Orientation.rotate(Orientation.NORTH, Direction.BEHIND));
        assertEquals(Orientation.SOUTH, Orientation.rotate(Orientation.WEST, Direction.LEFT));

        assertEquals(Orientation.EAST, Orientation.rotate(Orientation.EAST, Direction.AHEAD));
        assertEquals(Orientation.EAST, Orientation.rotate(Orientation.NORTH, Direction.RIGHT));
        assertEquals(Orientation.EAST, Orientation.rotate(Orientation.WEST, Direction.BEHIND));
        assertEquals(Orientation.EAST, Orientation.rotate(Orientation.SOUTH, Direction.LEFT));
    }

    @Test
    public void difference() {
        assertEquals(Direction.AHEAD, Orientation.difference(Orientation.NORTH, Orientation.NORTH));
        assertEquals(Direction.AHEAD, Orientation.difference(Orientation.EAST, Orientation.EAST));
        assertEquals(Direction.AHEAD, Orientation.difference(Orientation.WEST, Orientation.WEST));
        assertEquals(Direction.AHEAD, Orientation.difference(Orientation.SOUTH, Orientation.SOUTH));

        assertEquals(Direction.LEFT, Orientation.difference(Orientation.NORTH, Orientation.WEST));
        assertEquals(Direction.LEFT, Orientation.difference(Orientation.WEST, Orientation.SOUTH));
        assertEquals(Direction.LEFT, Orientation.difference(Orientation.SOUTH, Orientation.EAST));
        assertEquals(Direction.LEFT, Orientation.difference(Orientation.EAST, Orientation.NORTH));

        assertEquals(Direction.BEHIND, Orientation.difference(Orientation.NORTH, Orientation.SOUTH));
        assertEquals(Direction.BEHIND, Orientation.difference(Orientation.WEST, Orientation.EAST));
        assertEquals(Direction.BEHIND, Orientation.difference(Orientation.SOUTH, Orientation.NORTH));
        assertEquals(Direction.BEHIND, Orientation.difference(Orientation.EAST, Orientation.WEST));

        assertEquals(Direction.RIGHT, Orientation.difference(Orientation.NORTH, Orientation.EAST));
        assertEquals(Direction.RIGHT, Orientation.difference(Orientation.EAST, Orientation.SOUTH));
        assertEquals(Direction.RIGHT, Orientation.difference(Orientation.SOUTH, Orientation.WEST));
        assertEquals(Direction.RIGHT, Orientation.difference(Orientation.WEST, Orientation.NORTH));
    }
}