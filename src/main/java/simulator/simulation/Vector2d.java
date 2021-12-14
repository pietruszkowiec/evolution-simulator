package simulator.simulation;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public boolean precedes(Vector2d other) {
        return (this.x <= other.x) && (this.y <= other.y);
    }

    public boolean follows(Vector2d other) {
        return (other.x <= this.x) && (other.y <= this.y);
    }

    public Vector2d upperRight(Vector2d other) {
        int newX = Math.max(this.x, other.x);
        int newY = Math.max(this.y, other.y);
        return new Vector2d(newX, newY);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int newX = Math.min(this.x, other.x);
        int newY = Math.min(this.y, other.y);
        return new Vector2d(newX, newY);
    }

    public Vector2d add(Vector2d other) {
        int newX = this.x + other.x;
        int newY = this.y + other.y;
        return new Vector2d(newX, newY);
    }

    public Vector2d subtract(Vector2d other) {
        int newX = this.x - other.x;
        int newY = this.y - other.y;
        return new Vector2d(newX, newY);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if(!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if (this.x != that.x || this.y != that.y) {
            return false;
        }
        return true;
    }

    public Vector2d opposite() {
        int newX = this.y;
        int newY = this.x;
        return new Vector2d(newX, newY);
    }
}
