package agh.ics.oop.simulation;

public class BoundedMap extends AbstractWorldMap {
    public BoundedMap(int width, int height, float jungleRatio) {
        super(width, height, jungleRatio);
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
