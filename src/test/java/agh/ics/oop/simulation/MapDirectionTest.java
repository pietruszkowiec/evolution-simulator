package agh.ics.oop.simulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {
    @Test
    public void nextTest() {
        MapDirection mapDirection = MapDirection.NORTH;

        MapDirection[] directions = {
                MapDirection.NORTH,
                MapDirection.NORTH_EAST,
                MapDirection.EAST,
                MapDirection.SOUTH_EAST,
                MapDirection.SOUTH,
                MapDirection.SOUTH_WEST,
                MapDirection.WEST,
                MapDirection.NORTH_WEST};

        for (int i = 0; i < 8; i++) {
            assertEquals(mapDirection, directions[i]);
            mapDirection = mapDirection.next();
        }
    }

    @Test
    public void prevTest() {
        MapDirection mapDirection = MapDirection.NORTH;

        MapDirection[] directions = {
                MapDirection.NORTH,
                MapDirection.NORTH_WEST,
                MapDirection.WEST,
                MapDirection.SOUTH_WEST,
                MapDirection.SOUTH,
                MapDirection.SOUTH_EAST,
                MapDirection.EAST,
                MapDirection.NORTH_EAST};

        for (int i = 0; i < 8; i++) {
            assertEquals(mapDirection, directions[i]);
            mapDirection = mapDirection.prev();
        }
    }
}
