package agh.ics.oop.simulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class AbstractWorldMap implements IWorldMap {
    protected final int width, height;
    protected final Vector2d lowerLeftBound;
    protected final Vector2d upperRightBound;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final float jungleRatio;
    protected HashMap<Vector2d, AnimalSet> animalSets;
    protected HashMap<Vector2d, Grass> grassMap;

    public AbstractWorldMap(int width, int height, int moveEnergy,
                            int plantEnergy, float jungleRatio) {
        this.width = width;
        this.height = height;
        this.lowerLeftBound = new Vector2d(0, 0);
        this.upperRightBound = new Vector2d(width-1, height-1);
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
    }

    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        if (isOccupiedByAnimal(position)) {
            AnimalSet animalSet = this.animalSets.get(position);
            animalSet.add(animal);
        } else {
            AnimalSet animalSet = new AnimalSet();
            animalSet.add(animal);
            this.animalSets.put(position, animalSet);
        }
    }

    public boolean placeGrass(Grass grass) {
        Vector2d position = grass.getPosition();
        if (!(isOccupiedByAnimal(position)) && !(isOccupiedByGrass(position))) {
            this.grassMap.put(position, grass);
            return true;
        }
        return false;
    }

    public Animal animalAt(Vector2d position) {
        if (isOccupiedByAnimal(position)) {
            AnimalSet animalSet = animalSets.get(position);
            return animalSet.last();
        }
        return null;
    }

    public Grass grassAt(Vector2d position) {
        if (isOccupiedByGrass(position)) {
            return grassMap.get(position);
        }
        return null;
    }

    public boolean isOccupied(Vector2d position) {
        return isOccupiedByAnimal(position) || isOccupiedByAnimal(position);
    }

    public boolean isOccupiedByAnimal(Vector2d position) {
        return animalSets.containsKey(position);
    }

    public boolean isOccupiedByGrass(Vector2d position) {
        return grassMap.containsKey(position);
    }

    public Vector2d getRandomPosition() {
        Vector2d position;
        Random random = new Random();
        int x, y;
        int i = 0;

        do {
            x = random.nextInt(this.width);
            y = random.nextInt(this.height);
            position = new Vector2d(x, y);
            i++;
        } while (isOccupied(position) && i < this.width * this.height);

        if (isOccupied(position)) {
            return null;
        }
        return position;
    }

    public void removeDeadAnimals() {
        List<AnimalSet> animalSetsList = new LinkedList<>();
        animalSetsList.addAll(this.animalSets.values());
        for (AnimalSet animalSet : animalSetsList) {
            while (!(animalSet.isEmpty()) && animalSet.last().getEnergy() == 0) {
                animalSet.remove(animalSet.last());
            }
            if (animalSet.isEmpty()) {
                this.animalSets.remove(animalSet);
            }
        }
    }

    public void feedAnimals() {
        List<Animal> strongestAnimals;
        for (Vector2d position : this.animalSets.keySet()) {
            if (isOccupiedByGrass(position)) {
                AnimalSet animalSet = this.animalSets.get(position);
                strongestAnimals = animalSet.firstWithTies();

                int energyForEach = this.plantEnergy / strongestAnimals.size();

                for (Animal animal : strongestAnimals) {
                    animal.increaseEnergy(energyForEach);
                }

                this.grassMap.remove(position);
            }
        }
    }
}
