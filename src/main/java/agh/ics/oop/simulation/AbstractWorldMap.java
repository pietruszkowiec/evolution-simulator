package agh.ics.oop.simulation;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractWorldMap implements IWorldMap {
    protected final int width, height;
    protected final float jungleRatio;
    protected HashMap<Vector2d, SortedSet<Animal>> AnimalSets;
    protected HashMap<Vector2d, Grass> GrassMap;

    public AbstractWorldMap(int width, int height, float jungleRatio) {
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
    }

    @Override
    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        if (isOccupiedByAnimal(position)) {
            SortedSet<Animal> positionSet = this.AnimalSets.get(position);
            positionSet.add(animal);
        } else {
            SortedSet<Animal> positionSet = new TreeSet<>(new EnergyComparator());
            positionSet.add(animal);
            this.AnimalSets.put(position, positionSet);
        }
    }

    @Override
    public boolean placeGrass(Grass grass) {
        Vector2d position = grass.getPosition();
        if (!(isOccupiedByAnimal(position)) && !(isOccupiedByGrass(position))) {
            this.GrassMap.put(position, grass);
            return true;
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        return null;
    }

    @Override
    public Animal animalAt(Vector2d position) {
        if (isOccupiedByAnimal(position)) {
            SortedSet<Animal> positionSet = AnimalSets.get(position);
            return positionSet.last();
        }
        return null;
    }

    @Override
    public Grass grassAt(Vector2d position) {
        if (isOccupiedByGrass(position)) {
            return GrassMap.get(position);
        }
        return null;
    }

    @Override
    public boolean isOccupiedByAnimal(Vector2d position) {
        return AnimalSets.containsKey(position);
    }

    @Override
    public boolean isOccupiedByGrass(Vector2d position) {
        return GrassMap.containsKey(position);
    }
}
