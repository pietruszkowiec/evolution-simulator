package agh.ics.oop.simulation;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimalList {
    private final CopyOnWriteArrayList<Animal> animals;
    private static EnergyComparator energyComparator = new EnergyComparator();

    public AnimalList() {
        this.animals = new CopyOnWriteArrayList<>();
    }

    public synchronized CopyOnWriteArrayList<Animal> getAnimals() {
        return animals;
    }

    public synchronized void add(Animal animal) {
        this.animals.add(animal);
        Collections.sort(this.animals, AnimalList.energyComparator);
    }

    public synchronized boolean remove(Animal animal) {
        return this.animals.remove(animal);
    }

    public synchronized Animal first() {
        if (isEmpty()) {
            return null;
        }
        return this.animals.get(0);
    }

    public synchronized Animal last() {
        if (isEmpty()) {
            return null;
        }
        return this.animals.get(this.animals.size() - 1);
    }

    public synchronized List<Animal> firstWithTies() {
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

    public synchronized List<Animal> firstTwo() {
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
}
