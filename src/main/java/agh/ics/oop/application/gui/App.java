package agh.ics.oop.application.gui;

import agh.ics.oop.simulation.*;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
    private final int sceneWidth = 300;
    private final int sceneHeight = 300;
    private StatisticsPanel leftStatistics;
    private StatisticsPanel rightStatistics;
    private boolean moveStatistics = true;

    public void initializeMapsAndEngines() {
        int cellSize = Math.min(sceneWidth / width, sceneHeight / height);

        this.leftMap = new UnboundedMap(width, height, jungleRatio, cellSize, moveEnergy, plantEnergy);
        this.rightMap = new BoundedMap(width, height, jungleRatio, cellSize, moveEnergy, plantEnergy);
        this.leftEngine = new ThreadedSimulationEngine(this.leftMap,
                initialNumOfAnimals, initialNumOfGrass,
                startEnergy, this.moveDelay);
        this.rightEngine = new ThreadedSimulationEngine(this.rightMap,
                initialNumOfAnimals, initialNumOfGrass,
                startEnergy, this.moveDelay);

        this.leftStatistics = new StatisticsPanel(this.leftEngine, this.leftMap, moveStatistics);
        this.rightStatistics = new StatisticsPanel(this.rightEngine, this.rightMap, moveStatistics);

        this.leftEngine.setStatisticsPanel(this.leftStatistics);
        this.rightEngine.setStatisticsPanel(this.rightStatistics);
    }

    public Stage optionsMenu(Stage primaryStage) {
        GridPane optionsGrid = new GridPane();

        optionsGrid.getColumnConstraints().add(new ColumnConstraints(20));
        optionsGrid.getColumnConstraints().add(new ColumnConstraints(180));
        optionsGrid.getColumnConstraints().add(new ColumnConstraints(180));
        optionsGrid.getColumnConstraints().add(new ColumnConstraints(20));
        for (int i = 0; i < 11; i++) {
            optionsGrid.getRowConstraints().add(new RowConstraints(25));
        }
        optionsGrid.getRowConstraints().add(new RowConstraints(15));
        optionsGrid.getRowConstraints().add(new RowConstraints(30));
        optionsGrid.getRowConstraints().add(new RowConstraints(15));

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

        Label initialNumOfAnimalsLabel = new Label("Initial number of animals: ");
        optionsGrid.add(initialNumOfAnimalsLabel, 1, 4);
        TextField initialNumOfAnimalsTextBox = new TextField(this.initialNumOfAnimals + "");
        optionsGrid.add(initialNumOfAnimalsTextBox, 2, 4);

        Label initialNumOfGrassLabel = new Label("Initial number of grass: ");
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

        Label moveDelayLabel = new Label("Move delay (ms): ");
        optionsGrid.add(moveDelayLabel, 1, 9);
        TextField moveDelayTextBox = new TextField(this.moveDelay + "");
        optionsGrid.add(moveDelayTextBox, 2, 9);

        Label moveStatisticsLabel = new Label("Move statistic charts? ");
        optionsGrid.add(moveStatisticsLabel, 1, 10);
        CheckBox moveStatisticsCheckBox = new CheckBox();
        optionsGrid.add(moveStatisticsCheckBox, 2, 10);

        Button startButton = new Button("Start");
        optionsGrid.add(startButton, 2, 12);

        Scene optionsScene = new Scene(optionsGrid);
        Stage optionsStage = new Stage();
        optionsStage.setScene(optionsScene);

        startButton.setOnAction(event -> {

            this.height = Integer.parseInt(heightTextBox.getText());
            this.width = Integer.parseInt(widthTextBox.getText());
            this.jungleRatio = Float.parseFloat(jungleRatioTextBox.getText());
            this.initialNumOfAnimals = Integer.parseInt(initialNumOfAnimalsTextBox.getText());
            this.initialNumOfGrass = Integer.parseInt(initialNumOfGrassTextBox.getText());
            this.startEnergy = Integer.parseInt(startEnergyTextBox.getText());
            this.moveEnergy = Integer.parseInt(moveEnergyTextBox.getText());
            this.plantEnergy = Integer.parseInt(plantEnergyTextBox.getText());
            this.moveDelay = Integer.parseInt(moveDelayTextBox.getText());
            this.moveStatistics = moveStatisticsCheckBox.isSelected();

            initializeMapsAndEngines();

            optionsStage.close();
            primaryStage.show();

            this.leftMap.drawMap();
            this.rightMap.drawMap();

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

        Label mapTitle = new Label(map.toString() + " map\n");
        mapTitle.setFont(new Font(16));
        GridPane grid = map.getMapGrid().getGrid();
        VBox mainVBox = new VBox(mapTitle, new Label("\n"),
                grid, new Label("\n"), pauseResumeButton);

        pauseResumeButton.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
        mainVBox.setAlignment(Pos.CENTER);

        BorderPane.setAlignment(pauseResumeButton, Pos.CENTER);
        pane.setCenter(mainVBox);

        pane.setBottom(engine.getStatisticsPanel().makeAllCharts());
        return pane;
    }

    @Override
    public void start(Stage primaryStage) {
        Stage optionsStage = optionsMenu(primaryStage);

        optionsStage.showAndWait();

        Separator vSeparatorLeft = new Separator();
        vSeparatorLeft.setOrientation(Orientation.VERTICAL);
        Separator vSeparatorRight = new Separator();
        vSeparatorRight.setOrientation(Orientation.VERTICAL);

        BorderPane leftPane = new BorderPane();
        leftPane.setCenter(makePane(this.leftEngine, this.leftMap));
        leftPane.setRight(this.leftStatistics.makeDominantGenotypeVBox());
        leftPane.setLeft(vSeparatorLeft);

        BorderPane rightPane = new BorderPane();
        rightPane.setCenter(makePane(this.rightEngine, this.rightMap));
        rightPane.setLeft(this.rightStatistics.makeDominantGenotypeVBox());
        rightPane.setRight(vSeparatorRight);

        Separator vSeparatorMid = new Separator();
        vSeparatorMid.setOrientation(Orientation.VERTICAL);
        HBox mainHBox = new HBox(leftPane, vSeparatorMid, rightPane);

        Separator hSeparatorUp = new Separator();
        Separator hSeparatorDown = new Separator();
        hSeparatorUp.setOrientation(Orientation.HORIZONTAL);
        hSeparatorDown.setOrientation(Orientation.HORIZONTAL);

        VBox mainVBox = new VBox(new Label(), hSeparatorUp,
                mainHBox, hSeparatorDown);
        Scene scene = new Scene(mainVBox);
        primaryStage.setScene(scene);
    }
}
