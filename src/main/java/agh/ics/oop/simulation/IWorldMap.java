package agh.ics.oop.simulation;

public interface IWorldMap {

    void placeAnimal(Animal animal);

    boolean placeGrass(Grass grass);

    boolean canMoveTo(Vector2d position);

    boolean isOccupied(Vector2d position);

    Object objectAt(Vector2d position);

    Animal animalAt(Vector2d position);

    Grass grassAt(Vector2d position);

    boolean isOccupiedByAnimal(Vector2d position);

    boolean isOccupiedByGrass(Vector2d position);

    Vector2d transformPosition(Vector2d position, Vector2d nextPosition);
}
