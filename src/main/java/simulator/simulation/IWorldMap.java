package simulator.simulation;

public interface IWorldMap {
    boolean place(Animal animal);

    boolean canMoveTo(Vector2d position);

    boolean isOccupied(Vector2d position);

    Object objectAt(Vector2d position);

    Vector2d transformPosition(Vector2d position, Vector2d nextPosition);
}
