package agh.ics.oop.simulation;

import java.util.*;

public class AnimalList {
    private final List<Animal> animals;
    private static EnergyComparator energyComparator = new EnergyComparator();

    public AnimalList() {
        this.animals = new ArrayList<>(0);
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
        if (this.animals.isEmpty()) {
            return null;
        }
        return this.animals.get(0);
    }

    public Animal last() {
        if (this.animals.isEmpty()) {
            return null;
        }
        return this.animals.get(this.animals.size() - 1);
    }

    public List<Animal> firstWithTies() {
        List<Animal> strongestAnimals = new LinkedList<>();

        if (this.animals.isEmpty()) {
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
        return this.animals.isEmpty();
    }

    public int size() {
        return this.animals.size();
    }

}
