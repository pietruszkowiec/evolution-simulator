package agh.ics.oop.application;

import agh.ics.oop.simulation.AbstractWorldMap;
import agh.ics.oop.simulation.BoundedMap;
import agh.ics.oop.simulation.SimulationEngine;

public class Main {
    public static void main(String[] args) {
        int width = 20;
        int height = 20;
        float jungleRatio = 0.5f;
        int initialNumOfAnimals = 100;
        int initialNumOfGrass = 10000;
        int startEnergy = 100;
        int moveEnergy = 1;
        int plantEnergy = 40;
        AbstractWorldMap map;
        SimulationEngine engine;

        map = new BoundedMap(width, height, jungleRatio);
        engine = new SimulationEngine(map,
                    initialNumOfAnimals, initialNumOfGrass,
                    startEnergy, moveEnergy, plantEnergy);
        engine.run();
    }
}
