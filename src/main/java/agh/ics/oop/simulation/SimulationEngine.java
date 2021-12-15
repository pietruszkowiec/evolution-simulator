package agh.ics.oop.simulation;

import java.util.LinkedList;
import java.util.List;

public class SimulationEngine implements IAnimalObserver {
    protected final AbstractWorldMap map;
    protected final int initialNumOfAnimals;
    protected final int initialNumOfGrass;
    protected final int startEnergy;
    protected final List<Animal> animals;

    public SimulationEngine(AbstractWorldMap map, int initialNumOfAnimals,
                            int initialNumOfGrass, int startEnergy) {
        this.map = map;
        this.initialNumOfAnimals = initialNumOfAnimals;
        this.initialNumOfGrass = initialNumOfGrass;
        this.startEnergy = startEnergy;
        this.animals = new LinkedList<>();
        createAnimals();
    }

    public void createAnimals() {
        Animal animal;
        Vector2d position;

        for (int i = 0; i < this.initialNumOfAnimals; i++) {
            position = this.map.getRandomPosition();
            if (position == null) {
//                too small map, animals cannot fit!
//                TODO throwing exception
                break;
            }
            animal = new Animal(position, this.startEnergy,
                    this.startEnergy, this.map);
            this.map.placeAnimal(animal);
            animals.add(animal);
            animal.addObserver(this);
        }
    }

    public void addAnimals(List<Animal> children) {
        this.animals.addAll(children);
        for (Animal child : children) {
            child.addObserver(this);
        }
    }

    public void run() {
        while (true) {
            for (Animal animal : animals) {
                animal.move();
            }
            this.map.feedAnimals();
            addAnimals(this.map.reproduceAnimals());
            this.map.addGrass();
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Animal animal) {
        return;
    }

    @Override
    public void animalDied(Animal animal) {
        this.animals.remove(animal);
    }
}
