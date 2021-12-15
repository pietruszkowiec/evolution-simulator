package agh.ics.oop.simulation;

public interface IAnimalObserver {
    void positionChanged(Vector2d oldPosition, Animal animal);

    void energyChanged(Animal animal);
}
