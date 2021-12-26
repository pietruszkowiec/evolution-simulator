package agh.ics.oop.application.gui;

import agh.ics.oop.simulation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {
    private int width = 20;
    private int height = 20;
    private float jungleRatio = 0.3f;
    private int initialNumOfAnimals = 100;
    private int initialNumOfGrass = 0;
    private int startEnergy = 150;
    private int moveEnergy = 1;
    private int plantEnergy = 20;
    private int moveDelay = 100;
    private UnboundedMap leftMap;
    private BoundedMap rightMap;
    private ThreadedSimulationEngine leftEngine;
    private ThreadedSimulationEngine rightEngine;
    private Thread leftThread;
    private Thread rightThread;
    private int mapWidth = 500;
    private int mapHeight = 500;

    public void initializeMapsAndEngines() {
        int cellSize = Math.min(mapWidth / width, mapHeight / height);

        this.leftMap = new UnboundedMap(width, height, jungleRatio, cellSize, moveEnergy, plantEnergy);
        this.rightMap = new BoundedMap(width, height, jungleRatio, cellSize, moveEnergy, plantEnergy);
        this.leftEngine = new ThreadedSimulationEngine(this.leftMap,
                initialNumOfAnimals, initialNumOfGrass,
                startEnergy, this.moveDelay);
        this.rightEngine = new ThreadedSimulationEngine(this.rightMap,
                initialNumOfAnimals, initialNumOfGrass,
                startEnergy, this.moveDelay);
    }

    public Stage optionsMenu(Stage primaryStage) {
        GridPane optionsGrid = new GridPane();

        optionsGrid.getColumnConstraints().add(new ColumnConstraints(10));
        optionsGrid.getColumnConstraints().add(new ColumnConstraints(200));
        optionsGrid.getRowConstraints().add(new RowConstraints(10));

        Label widthLabel = new Label("Map width ");
        optionsGrid.add(widthLabel, 1, 1);

        TextField widthTextBox = new TextField(this.width + "");
        optionsGrid.add(widthTextBox, 2, 1);

        Label heightLabel = new Label("Map height ");
        optionsGrid.add(heightLabel, 1, 2);

        TextField heightTextBox = new TextField(this.height + "");
        optionsGrid.add(heightTextBox, 2, 2);

        Label jungleRatioLabel = new Label("Jungle / Map ratio (0, 1): ");
        optionsGrid.add(jungleRatioLabel, 1, 3);

        TextField jungleRatioTextBox = new TextField(this.jungleRatio + "");
        optionsGrid.add(jungleRatioTextBox, 2, 3);

        Label initialNumOfAnimalsLabel = new Label("Initial number of animals on map: ");
        optionsGrid.add(initialNumOfAnimalsLabel, 1, 4);

        TextField initialNumOfAnimalsTextBox = new TextField(this.initialNumOfAnimals + "");
        optionsGrid.add(initialNumOfAnimalsTextBox, 2, 4);

        Label initialNumOfGrassLabel = new Label("Initial number of grass on map: ");
        optionsGrid.add(initialNumOfGrassLabel, 1, 5);

        TextField initialNumOfGrassTextBox = new TextField(this.initialNumOfGrass + "");
        optionsGrid.add(initialNumOfGrassTextBox, 2, 5);

        Label startEnergyLabel = new Label("Start energy level: ");
        optionsGrid.add(startEnergyLabel, 1, 6);

        TextField startEnergyTextBox = new TextField(this.startEnergy + "");
        optionsGrid.add(startEnergyTextBox, 2, 6);

        Label moveEnergyLabel = new Label("Move energy cost: ");
        optionsGrid.add(moveEnergyLabel, 1, 7);

        TextField moveEnergyTextBox = new TextField(this.moveEnergy + "");
        optionsGrid.add(moveEnergyTextBox, 2, 7);

        Label plantEnergyLabel = new Label("Plant energy gain: ");
        optionsGrid.add(plantEnergyLabel, 1, 8);

        TextField plantEnergyTextBox = new TextField(this.plantEnergy + "");
        optionsGrid.add(plantEnergyTextBox, 2, 8);

        Button startButton = new Button("Start");
        optionsGrid.add(startButton, 2, 9);

        Scene optionsScene = new Scene(optionsGrid, 450, 300);
        Stage optionsStage = new Stage();
        optionsStage.setScene(optionsScene);

        startButton.setOnAction(event -> {
            optionsStage.close();

            this.height = Integer.parseInt(heightTextBox.getText());
            this.width = Integer.parseInt(widthTextBox.getText());
            this.jungleRatio = Float.parseFloat(jungleRatioTextBox.getText());
            this.initialNumOfAnimals = Integer.parseInt(initialNumOfAnimalsTextBox.getText());
            this.initialNumOfGrass = Integer.parseInt(initialNumOfGrassTextBox.getText());
            this.startEnergy = Integer.parseInt(startEnergyTextBox.getText());
            this.moveEnergy = Integer.parseInt(moveEnergyTextBox.getText());
            this.plantEnergy = Integer.parseInt(plantEnergyTextBox.getText());

            initializeMapsAndEngines();

            primaryStage.show();

            this.leftThread = new Thread(this.leftEngine);
            this.rightThread = new Thread(this.rightEngine);
            this.leftThread.start();
            this.rightThread.start();
        });

        return optionsStage;
    }

    public Button makePauseResumeButton(ThreadedSimulationEngine engine) {
        Button pauseResumeButton = new Button("Pause");

        pauseResumeButton.setOnAction(event -> {
            if (engine.isPaused) {
                engine.resume();
                pauseResumeButton.setText("Pause");
            } else {
                engine.pause();
                pauseResumeButton.setText("Resume");
            }
        });

        return pauseResumeButton;
    }

    public BorderPane makePane(ThreadedSimulationEngine engine, AbstractWorldMap map) {
        BorderPane pane = new BorderPane();

        Button pauseResumeButton = makePauseResumeButton(engine);

        pane.setCenter(map.getMapGrid().getGrid());
        pane.setBottom(pauseResumeButton);
        pane.setAlignment(pauseResumeButton, Pos.CENTER);

        return pane;
    }

    @Override
    public void start(Stage primaryStage) {
        Stage optionsStage = optionsMenu(primaryStage);

        optionsStage.showAndWait();

        BorderPane leftPane = makePane(this.leftEngine, this.leftMap);
        BorderPane rightPane = makePane(this.rightEngine, this.rightMap);

        HBox mainHBox = new HBox(leftPane, rightPane);
        Scene scene = new Scene(mainHBox);
        primaryStage.setScene(scene);
    }
}
