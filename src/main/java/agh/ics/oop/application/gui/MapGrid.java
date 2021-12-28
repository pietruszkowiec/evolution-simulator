package agh.ics.oop.application.gui;

import agh.ics.oop.simulation.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class MapGrid {
    private final AbstractWorldMap map;
    private final GridPane grid;
    private final int cellSize;
    private final int width;
    private final int height;
    private final ButtonField[][] buttonFields;

    public MapGrid(AbstractWorldMap map, int cellSize) {
        this.map = map;
        this.cellSize = cellSize;
        this.grid = new GridPane();
//        this.grid.setGridLinesVisible(true);
        Vector2d lowerLeft = map.getLowerLeft();
        Vector2d upperRight = map.getUpperRight();
        this.width = upperRight.x - lowerLeft.x + 1;
        this.height = upperRight.y - lowerLeft.y + 1;
        this.buttonFields = new ButtonField[this.height][this.width];
        addConstraints();
        addButtonFields();
    }

    public void drawGrid() {
        int x, y;
        ButtonField buttonField;
        Vector2d position;

        for (int i = 0; i < this.height; i++) {
            x = this.height - i - 1;

            for (int j = 0; j < this.width; j++) {
                y = j;

                position = new Vector2d(x, y);
                buttonField = this.buttonFields[x][y];
//                if (this.map.objectAt())
                buttonField.setObject(this.map.objectAt(position));
            }
        }
    }

    public GridPane getGrid() {
        return grid;
    }

    private void addConstraints() {
        for (int i = 0; i < this.height; i++) {
            this.grid.getRowConstraints().add(new RowConstraints(cellSize));
        }

        for (int j = 0; j < this.width; j++) {
            this.grid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }
    }

    private void addButtonFields() {
        int x, y;
        ButtonField buttonField;

        for (int i = 0; i < this.height; i++) {
            x = this.height - i - 1;

            for (int j = 0; j < this.width; j++) {
                y = j;
                Vector2d position = new Vector2d(x, y);

                buttonField = new ButtonField(this.map.isInJungle(position));
                buttonField.getVBox().setPrefSize(cellSize, cellSize);
                this.buttonFields[x][y] = buttonField;
                this.grid.add(buttonField.getVBox(), j, i);
            }
        }
    }
}
