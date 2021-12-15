package agh.ics.oop.simulation;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
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

        this.animals = new ArrayList<>(0);

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
        }
    }

    public void run() {
        while (true) {
            this.map.removeDeadAnimals();
            for (Animal animal : animals) {
                animal.move();
            }
            this.map.feedAnimals();
//          TODO reproduce
//          TODO addGrass
        }
    }

}
