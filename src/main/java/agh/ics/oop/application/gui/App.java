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
    private boolean magicEvolutionOnUnbounded = false;
    private boolean magicEvolutionOnBounded = false;
    private boolean moveStatistics = false;
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

    private void initializeMapsAndEngines() {
        int cellSize = Math.min(sceneWidth / width, sceneHeight / height);

        this.leftMap = new UnboundedMap(width, height, jungleRatio,
                cellSize, moveEnergy, plantEnergy, magicEvolutionOnUnbounded);

        this.rightMap = new BoundedMap(width, height, jungleRatio, cellSize, moveEnergy, plantEnergy,
                magicEvolutionOnBounded);

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

    private int integerOptionsInRange(TextField textBox,
                                      int gotVal, int downVal,
                                      int upVal, int defaultVal) throws IllegalArgumentException {
        if (gotVal < downVal || gotVal > upVal) {
            textBox.setText(defaultVal + "");
            throw new IllegalArgumentException("Value " + gotVal
                    + " not in range (" + downVal + ", " + upVal + ")");
        }
        return gotVal;
    }

    private float floatOptionsInRange(TextField textBox,
                                      float gotVal, float downVal,
                                      float upVal, float defaultVal) throws IllegalArgumentException {
        if (gotVal < downVal || gotVal > upVal) {
            textBox.setText(defaultVal + "");
            throw new IllegalArgumentException("Value " + gotVal
                    + " not in range (" + downVal + ", " + upVal + ")");
        }
        return gotVal;
    }

    private Stage optionsMenu(Stage primaryStage) {
        GridPane optionsGrid = new GridPane();

        optionsGrid.getColumnConstraints().add(new ColumnConstraints(20));
        optionsGrid.getColumnConstraints().add(new ColumnConstraints(210));
        optionsGrid.getColumnConstraints().add(new ColumnConstraints(180));
        optionsGrid.getColumnConstraints().add(new ColumnConstraints(20));
        for (int i = 0; i < 13; i++) {
            optionsGrid.getRowConstraints().add(new RowConstraints(25));
        }
        optionsGrid.getRowConstraints().add(new RowConstraints(15));
        optionsGrid.getRowConstraints().add(new RowConstraints(30));
        optionsGrid.getRowConstraints().add(new RowConstraints(15));

        Label widthLabel = new Label("Map width: ");
        optionsGrid.add(widthLabel, 1, 1);
        TextField widthTextBox = new TextField(this.width + "");
        optionsGrid.add(widthTextBox, 2, 1);

        Label heightLabel = new Label("Map height: ");
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

        Label unboundedMagicLabel = new Label("Magic evolution on unbounded map? ");
        optionsGrid.add(unboundedMagicLabel, 1, 10);
        CheckBox unboundedMagicCheckBox = new CheckBox();
        optionsGrid.add(unboundedMagicCheckBox, 2, 10);

        Label boundedMagicLabel = new Label("Magic evolution on bounded map? ");
        optionsGrid.add(boundedMagicLabel, 1, 11);
        CheckBox boundedMagicCheckBox = new CheckBox();
        optionsGrid.add(boundedMagicCheckBox, 2, 11);

        Label moveStatisticsLabel = new Label("Move statistic charts? ");
        optionsGrid.add(moveStatisticsLabel, 1, 12);
        CheckBox moveStatisticsCheckBox = new CheckBox();
        optionsGrid.add(moveStatisticsCheckBox, 2, 12);

        Button startButton = new Button("Start");
        optionsGrid.add(startButton, 2, 14);

        Scene optionsScene = new Scene(optionsGrid);
        Stage optionsStage = new Stage();
        optionsStage.setScene(optionsScene);

        startButton.setOnAction(event -> {
            try {
                this.height = integerOptionsInRange(heightTextBox,
                        Integer.parseInt(heightTextBox.getText()),
                        2, 100, 20);

                this.width = integerOptionsInRange(widthTextBox,
                        Integer.parseInt(widthTextBox.getText()),
                        2, 100, 20);

                this.jungleRatio = floatOptionsInRange(jungleRatioTextBox,
                        Float.parseFloat(jungleRatioTextBox.getText()),
                        0, 1, 0.3f);

                this.initialNumOfAnimals = integerOptionsInRange(
                        initialNumOfAnimalsTextBox,
                        Integer.parseInt(initialNumOfAnimalsTextBox.getText()),
                        0, height * width,
                        (int) (height * jungleRatio * width));

                this.initialNumOfGrass = integerOptionsInRange(
                        initialNumOfGrassTextBox,
                        Integer.parseInt(initialNumOfGrassTextBox.getText()),
                        0, height * width - initialNumOfAnimals, 0);

                this.startEnergy = integerOptionsInRange(startEnergyTextBox,
                        Integer.parseInt(startEnergyTextBox.getText()),
                        0, 1000000, startEnergy);

                this.moveEnergy = integerOptionsInRange(moveEnergyTextBox,
                        Integer.parseInt(moveEnergyTextBox.getText()),
                        0, 1000000, moveEnergy);

                this.plantEnergy = integerOptionsInRange(plantEnergyTextBox,
                        Integer.parseInt(plantEnergyTextBox.getText()),
                        0, 1000000, plantEnergy);

                this.moveDelay = integerOptionsInRange(moveDelayTextBox,
                        Integer.parseInt(moveDelayTextBox.getText()),
                        1, 300, moveDelay);

                this.moveStatistics = moveStatisticsCheckBox.isSelected();
                this.magicEvolutionOnUnbounded = unboundedMagicCheckBox.isSelected();
                this.magicEvolutionOnBounded = boundedMagicCheckBox.isSelected();

                initializeMapsAndEngines();

                optionsStage.close();
                primaryStage.show();

                this.leftMap.drawMap();
                this.rightMap.drawMap();

                this.leftThread = new Thread(this.leftEngine);
                this.rightThread = new Thread(this.rightEngine);
                this.leftThread.start();
                this.rightThread.start();

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.toString(),
                        ButtonType.OK);

                alert.showAndWait();
            }
        });

        return optionsStage;
    }

    private Button makePauseResumeButton(ThreadedSimulationEngine engine) {
        Button pauseResumeButton = new Button("Pause");

        pauseResumeButton.setOnAction(event -> {
            if (engine.isPaused()) {
                engine.resume();
                pauseResumeButton.setText("Pause");
            } else {
                engine.pause();
                pauseResumeButton.setText("Resume");
            }
        });

        return pauseResumeButton;
    }

    private BorderPane makePane(ThreadedSimulationEngine engine, AbstractWorldMap map) {
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
