package agh.ics.oop.application.gui;

import agh.ics.oop.simulation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {
    private int moveDelay = 33;
    private BoundedMap leftMap;
    private UnboundedMap rightMap;
    private ThreadedSimulationEngine leftEngine;
    private ThreadedSimulationEngine rightEngine;
    private int mapWidth = 500;
    private int mapHeight = 500;

    @Override
    public void init() throws IllegalArgumentException {
        int width = 20;
        int height = 20;
        float jungleRatio = 0.3f;
        int initialNumOfAnimals = 100;
        int initialNumOfGrass = 0;
        int startEnergy = 150;
        int moveEnergy = 1;
        int plantEnergy = 20;

        int cellSize = Math.min(mapWidth / width, mapHeight / height);

        this.leftMap = new BoundedMap(width, height, jungleRatio, cellSize);
        this.rightMap = new UnboundedMap(width, height, jungleRatio, cellSize);
        this.leftEngine = new ThreadedSimulationEngine(this.leftMap,
                initialNumOfAnimals, initialNumOfGrass,
                startEnergy, moveEnergy, plantEnergy,
                this.moveDelay);
        this.rightEngine = new ThreadedSimulationEngine(this.rightMap,
                initialNumOfAnimals, initialNumOfGrass,
                startEnergy, moveEnergy, plantEnergy,
                this.moveDelay);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox leftVBox = new VBox(this.leftMap.getMapGrid().getGrid());
        VBox rightVBox = new VBox(this.rightMap.getMapGrid().getGrid());
        HBox mainHBox = new HBox(leftVBox, rightVBox);
        Scene scene = new Scene(mainHBox, 2 * mapWidth, mapHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.leftMap.drawMap();
        this.rightMap.drawMap();
        Thread leftThread = new Thread(this.leftEngine);
        Thread rightThread = new Thread(this.rightEngine);
        leftThread.start();
        rightThread.start();
    }

//    @Override
//    public void stop() throws Exception {
//        super.stop();
//        System.exit(0);
//    }
}
