package agh.ics.oop.simulation;

import java.util.LinkedList;
import java.util.List;

public class SimulationEngine {
    protected final AbstractWorldMap map;
    protected final int initialNumOfAnimals;
    protected final int initialNumOfGrass;
    protected final int startEnergy;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final List<Animal> animals;

    public SimulationEngine(AbstractWorldMap map,
                            int initialNumOfAnimals, int initialNumOfGrass,
                            int startEnergy, int moveEnergy, int plantEnergy) {
        this.map = map;
        this.initialNumOfAnimals = initialNumOfAnimals;
        this.initialNumOfGrass = initialNumOfGrass;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.animals = new LinkedList<>();
        createAnimals();
        createGrass();
    }

    public void createAnimals() {
        Animal animal;
        Vector2d position;

        for (int i = 0; i < this.initialNumOfAnimals; i++) {
            position = this.map.getRandomPositionInJungle();
            if (position == null) {
                position = this.map.getRandomPosition();
                if (position == null) {
//                TODO throwing exception, map too small
                    break;
                }
            }
            animal = new Animal(position, this.startEnergy,
                    this.startEnergy, this.map);
            this.map.placeAnimal(animal);
            animals.add(animal);
            animal.addObserver(this.map);
        }
    }

    public void createGrass() {
        Grass grass;
        Vector2d position;

        for (int i = 0; i < this.initialNumOfGrass; i++) {
            position = this.map.getRandomPosition();
            if (position == null) {
//                too small map, animals cannot fit!
//                TODO throwing exception
                break;
            }
            grass = new Grass(position);
            this.map.placeGrass(grass);
        }
    }

    public void addAnimals(List<Animal> children) {
        this.animals.addAll(children);
    }

    public void deleteAnimals(List<Animal> deadAnimals) {
        this.animals.removeAll(deadAnimals);
    }

    public void run() {
//        System.out.println(this.map);
        Animal lastAnimal = animals.get(0);

        List<Animal> deadAnimals;
        List<Animal> children;

        int maxPopulation = this.animals.size();
        int deadCnt = 0;
        int childrenCnt = 0;

        for (int i = 0; i < 10000; i++) {
            deadAnimals = this.map.removeDeadAnimals();
            deadCnt += deadAnimals.size();
            deleteAnimals(deadAnimals);

            for (Animal animal : animals) {
                animal.move(this.moveEnergy);
            }
            this.map.feedAnimals(this.plantEnergy);

            children = this.map.reproduceAnimals();
            childrenCnt += children.size();
            addAnimals(children);

            this.map.addGrass();

            maxPopulation = Math.max(maxPopulation, this.animals.size());

//            System.out.println(this.map);

            System.out.println("Day " + i + ": " + this.animals.size() + " animals, "
                    + children.size() + " born today, "
                    + childrenCnt + " reproductions so far, "
                    + deadAnimals.size() + " died today, "
                    + deadCnt + " died so far");

            if (this.animals.size() == 0) {
                break;
            }
            lastAnimal = animals.get(0);
        }
        System.out.println(lastAnimal.genome);
        System.out.println("Max population : " + maxPopulation);
    }
}
