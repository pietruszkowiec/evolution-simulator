package agh.ics.oop.simulation;

import java.util.Comparator;

public class EnergyComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal a1, Animal a2) {
//        reverse order of comparison
        return a2.getEnergy() - a1.getEnergy();
    }
}
