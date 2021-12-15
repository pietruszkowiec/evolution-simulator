package agh.ics.oop.application;

import agh.ics.oop.simulation.AbstractWorldMap;
import agh.ics.oop.simulation.BoundedMap;
import agh.ics.oop.simulation.SimulationEngine;

public class Main {
    public static void main(String[] args) {
        int width = 100;
        int height = 100;
        float jungleRatio = 0.40f;
        int initialNumOfAnimals = 100;
        int initialNumOfGrass = 10000;
        int startEnergy = 10;
        int moveEnergy = 1;
        int plantEnergy = 5;
        AbstractWorldMap map;
        SimulationEngine engine;

        map = new BoundedMap(width, height, jungleRatio);
        engine = new SimulationEngine(map,
                    initialNumOfAnimals, initialNumOfGrass,
                    startEnergy, moveEnergy, plantEnergy);
        engine.run();
    }
}
