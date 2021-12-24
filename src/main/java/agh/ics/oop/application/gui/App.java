package agh.ics.oop.application.gui;

import agh.ics.oop.simulation.*;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class App extends Application implements IAnimalObserver {
    private final int CELL_SIZE = 40;
    private final int moveDelay = 300;
    private AbstractWorldMap map;
    private SimulationEngine engine;
    private GridPane grid;

    @Override
    public void init() {
        int width = 100;
        int height = 100;
        float jungleRatio = 0.9f;
        int initialNumOfAnimals = 1000;
        int initialNumOfGrass = 10000;
        int startEnergy = 10;
        int moveEnergy = 1;
        int plantEnergy = 8;

        this.map = new BoundedMap(width, height, jungleRatio);
        this.engine = new ThreadedSimulationEngine(this.map,
                initialNumOfAnimals, initialNumOfGrass,
                startEnergy, moveEnergy, plantEnergy,
                this.moveDelay);
    }

    public void drawGrid() {
        Vector2d lowerLeft = this.map.getLowerLeft();
        Vector2d upperRight = this.map.getUpperRight();
        int width = upperRight.x - lowerLeft.x;
        int height = upperRight.y - lowerLeft.y;
        Node gridLines = this.grid.getChildren().get(0);
        this.grid.getChildren().clear();
        this.grid.getColumnConstraints().clear();
        this.grid.getRowConstraints().clear();
        this.grid.getChildren().add(0, gridLines);
        int x, y;
        for(int i = 0; i < height; i++) {
            y = height - 1 - i + lowerLeft.y;
            this.grid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
//            this.grid.add(makeLabel("" + y), 0, i + 1);
        }
        for(int j = 0; j < width; j++) {
            x = j + lowerLeft.x;
            this.grid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
//            this.grid.add(makeLabel("" + x), j + 1, 0);
        }
        for(int i = 0; i < height; i++) {
            y = height - 1 - i + lowerLeft.y;
            for(int j = 0; j < width; j++) {
                x = j + lowerLeft.x;
                Object element = this.map.objectAt(new Vector2d(x, y));
                if(element != null) {
//                    GuiElementBox guiBox = new GuiElementBox((IMapElement) element);
//                    this.grid.add(guiBox.getvBox(), j + 1, i + 1);
                }
            }
        }
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Animal animal) {
//        Platform.runLater(System.out.println("runLater"););
    }

    @Override
    public void energyChanged(Animal animal) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
