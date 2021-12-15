package agh.ics.oop.simulation;

import java.util.*;

public class AnimalSet {
    private final SortedSet<Animal> animals;

    public AnimalSet() {
        this.animals = new TreeSet<>(new EnergyComparator());
    }

    public boolean add(Animal animal) {
        return this.animals.add(animal);
    }

    public boolean remove(Animal animal) {
        return this.animals.remove(animal);
    }

    public boolean contains(Animal animal) {
        return this.animals.contains(animal);
    }

    public Animal first() {
        return this.animals.first();
    }

    public Animal last() {
        return this.animals.last();
    }

    public List<Animal> firstWithTies() {
        List<Animal> strongestAnimals = new LinkedList<>();
        Animal firstAnimal = this.animals.first();

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
        return this.isEmpty();
    }

    public int size() {
        return this.animals.size();
    }

}
