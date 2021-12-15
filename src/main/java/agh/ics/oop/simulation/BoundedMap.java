package agh.ics.oop.simulation;

public class BoundedMap extends AbstractWorldMap {

    public BoundedMap(int width, int height, int moveEnergy,
                      int plantEnergy, float jungleRatio) {
        super(width, height, moveEnergy, plantEnergy, jungleRatio);
    }

    @Override
    public Vector2d transformPosition(Vector2d position, Vector2d nextPosition) {
        if (nextPosition.follows(this.lowerLeftBound)
                && nextPosition.precedes(this.upperRightBound)) {
            return nextPosition;
        }
        return position;
    }
}
