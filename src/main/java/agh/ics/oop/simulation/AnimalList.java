package agh.ics.oop.simulation;

import java.util.*;

public class AnimalList {
    private final List<Animal> animals;
    private final Vector2d position;
    private static EnergyComparator energyComparator = new EnergyComparator();

    public AnimalList(Vector2d position) {
        this.animals = new ArrayList<>(0);
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void add(Animal animal) {
        this.animals.add(animal);
        Collections.sort(this.animals, AnimalList.energyComparator);
    }

    public boolean remove(Animal animal) {
        return this.animals.remove(animal);
    }

    public boolean contains(Animal animal) {
        return this.animals.contains(animal);
    }

    public Animal first() {
        if (isEmpty()) {
            return null;
        }
        return this.animals.get(0);
    }

    public Animal last() {
        if (isEmpty()) {
            return null;
        }
        return this.animals.get(this.animals.size() - 1);
    }

    public List<Animal> firstWithTies() {
        List<Animal> strongestAnimals = new LinkedList<>();

        if (isEmpty()) {
            return strongestAnimals;
        }

        Animal firstAnimal = this.animals.get(0);

        for (Animal animal : this.animals) {
            if (animal.getEnergy() == firstAnimal.getEnergy()) {
                strongestAnimals.add(animal);
            } else {
                break;
            }
        }
        return strongestAnimals;
    }

    public List<Animal> firstTwo() {
        List<Animal> firstTwoAnimals = new LinkedList<>();
        int i = 0;

        for (Animal animal : this.animals) {
            if (i == 2) {
                break;
            }
            firstTwoAnimals.add(animal);
            i++;
        }
        return firstTwoAnimals;
    }

    public boolean isEmpty() {
        return this.animals.size() == 0;
    }

    public int size() {
        return this.animals.size();
    }

}
