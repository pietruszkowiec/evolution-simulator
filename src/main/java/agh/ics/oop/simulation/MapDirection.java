package agh.ics.oop.simulation;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    private static final Vector2d northUnitVector = new Vector2d(0, 1);
    private static final Vector2d northEastUnitVector = new Vector2d(1, 1);
    private static final Vector2d eastUnitVector = new Vector2d(1, 0);
    private static final Vector2d southEastUnitVector = new Vector2d(1, -1);
    private static final Vector2d southUnitVector = new Vector2d(0, -1);
    private static final Vector2d southWestUnitVector = new Vector2d(-1, -1);
    private static final Vector2d westUnitVector = new Vector2d(-1, 0);
    private static final Vector2d northWestUnitVector = new Vector2d(-1, 1);

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection prev() {
        return switch (this) {
            case NORTH -> NORTH_WEST;
            case NORTH_EAST -> NORTH;
            case EAST -> NORTH_EAST;
            case SOUTH_EAST -> EAST;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_WEST -> SOUTH;
            case WEST -> SOUTH_WEST;
            case NORTH_WEST -> WEST;
        };
    }

    public MapDirection changeDirectionWithMapBehaviour(MapBehaviour behaviour) {
        return switch (behaviour) {
            case FORWARD -> this;
            case R_45 -> this.next();
            case R_90 -> this.next().next();
            case R_135 -> this.next().next().next();
            case BACKWARD -> this;
            case R_225 -> this.prev().prev().prev();
            case R_270 -> this.prev().prev();
            case R_315 -> this.prev();
        };

    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> northUnitVector;
            case NORTH_EAST -> northEastUnitVector;
            case EAST -> eastUnitVector;
            case SOUTH_EAST -> southEastUnitVector;
            case SOUTH -> southUnitVector;
            case SOUTH_WEST -> southWestUnitVector;
            case WEST -> westUnitVector;
            case NORTH_WEST -> northWestUnitVector;
        };
    }

    public static MapDirection getRandomDirection() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
