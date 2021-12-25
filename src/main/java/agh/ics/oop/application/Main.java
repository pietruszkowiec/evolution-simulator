package agh.ics.oop.application;

import agh.ics.oop.application.gui.App;
import agh.ics.oop.simulation.AbstractWorldMap;
import agh.ics.oop.simulation.BoundedMap;
import agh.ics.oop.simulation.SimulationEngine;
import agh.ics.oop.simulation.UnboundedMap;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
//        int width = 100;
//        int height = 100;
//        float jungleRatio = 0.5f;
//        int initialNumOfAnimals = 1000;
//        int initialNumOfGrass = 10000;
//        int startEnergy = 100;
//        int moveEnergy = 1;
//        int plantEnergy = 40;
//        AbstractWorldMap map;
//        SimulationEngine engine;
//
//        map = new BoundedMap(width, height, jungleRatio);
//        engine = new SimulationEngine(map,
//                    initialNumOfAnimals, initialNumOfGrass,
//                    startEnergy, moveEnergy, plantEnergy);
//        engine.run();

        try {
            Application.launch(App.class, args);
        } catch(IllegalArgumentException ex) {
            System.out.println("Something went wrong: " + ex);
        } finally {
            System.out.println("Exiting program");
            System.exit(0);
        }
    }
}
