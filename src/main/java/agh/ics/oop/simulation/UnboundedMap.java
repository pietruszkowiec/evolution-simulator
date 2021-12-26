package agh.ics.oop.simulation;

public class UnboundedMap extends AbstractWorldMap {
    public UnboundedMap(int width, int height, float jungleRatio, int cellSize, int moveEnergy, int plantEnergy) {
        super(width, height, jungleRatio, cellSize, moveEnergy, plantEnergy);
    }

    @Override
    public Vector2d transformPosition(Vector2d position, Vector2d nextPosition) {
        int x = nextPosition.x;
        int y = nextPosition.y;
        int newX = (x + this.width) % this.width;
        int newY = (y + this.height) % this.height;
        return new Vector2d(newX, newY);
    }
}
