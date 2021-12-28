package agh.ics.oop.simulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void equalsTest() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(1, 2);
        Vector2d vec4 = new Vector2d(2, 1);
        Vector2d vec5 = new Vector2d(2, 2);

        assertEquals(vec1, vec1);
        assertEquals(vec1, vec2);

        assertNotEquals(vec1, vec3);
        assertNotEquals(vec1, vec4);
        assertNotEquals(vec1, vec5);
    }

    @Test
    public void precedesTest() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(2, 2);
        Vector2d vec4 = new Vector2d(0, 2);
        Vector2d vec5 = new Vector2d(0, 0);

        assertTrue(vec1.precedes(vec2));
        assertTrue(vec1.precedes(vec3));
        assertFalse(vec1.precedes(vec4));
        assertFalse(vec1.precedes(vec5));
    }

    @Test
    public void followsTest() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(2, 2);
        Vector2d vec4 = new Vector2d(0, 2);
        Vector2d vec5 = new Vector2d(0, 0);

        assertTrue(vec1.follows(vec2));
        assertFalse(vec1.follows(vec3));
        assertFalse(vec1.follows(vec4));
        assertTrue(vec1.follows(vec5));
    }

    @Test
    public void addTest() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(2, 2);
        Vector2d vec3 = new Vector2d(-1, -2);
        Vector2d vec4 = new Vector2d(0, 0);

        assertEquals(vec1.add(vec2), new Vector2d(3, 3));
        assertEquals(vec1.add(vec3), new Vector2d(0, -1));
        assertEquals(vec1.add(vec4), new Vector2d(1, 1));
    }

    @Test
    public void subtractTest() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(2, 2);
        Vector2d vec3 = new Vector2d(-1, -2);
        Vector2d vec4 = new Vector2d(0, 0);

        assertEquals(vec1.subtract(vec2), new Vector2d(-1, -1));
        assertEquals(vec1.subtract(vec3), new Vector2d(2, 3));
        assertEquals(vec1.subtract(vec4), new Vector2d(1, 1));
    }
}
