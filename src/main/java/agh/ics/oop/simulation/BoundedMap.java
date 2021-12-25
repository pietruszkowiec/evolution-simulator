package agh.ics.oop.simulation;

public class BoundedMap extends AbstractWorldMap {
    public BoundedMap(int width, int height, float jungleRatio, int cellSize) {
        super(width, height, jungleRatio, cellSize);
    }

    @Override
    public Vector2d transformPosition(Vector2d position, Vector2d nextPosition) {
        if (nextPosition.follows(this.lowerLeft)
                && nextPosition.precedes(this.upperRight)) {
            return nextPosition;
        }
        return position;
    }
}
