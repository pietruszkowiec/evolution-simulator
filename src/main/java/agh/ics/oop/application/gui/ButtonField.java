package agh.ics.oop.application.gui;

import agh.ics.oop.simulation.Animal;
import agh.ics.oop.simulation.Grass;
import agh.ics.oop.simulation.Vector2d;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ButtonField {
    private final VBox vBox;
    private final Button button;
    private final Vector2d position;
    private Object object;
    private boolean isInJungle;

    public ButtonField(Vector2d position, boolean isInJungle) {
        this.position = position;
        this.button = new Button();
        this.vBox = new VBox(this.button);
        this.object = null;
        this.isInJungle = isInJungle;
        setObject(null);
    }

    public VBox getVBox() {
        return this.vBox;
    }

    public void setObject(Object object) {
        this.object = object;
        Color color;

        if (this.object instanceof Animal) {
            Animal animal = (Animal) this.object;
            double energy = (double) animal.getEnergy();
            double startEnergy = (double) animal.startEnergy;
            double fraction = Math.min(energy / startEnergy, 1);
            color = Color.color(fraction, 0, 0);
        } else if (this.object instanceof Grass) {
            color = Color.rgb(0, 255, 0);
        } else if (this.isInJungle) {
            color = Color.rgb(0, 128, 0);
        } else {
            color = Color.rgb(240, 220, 70);
        }
        updateColor(color);
    }

    private static String format(double value) {
        String in = Integer.toHexString((int) Math.round(value * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    private static String toHexString(Color color) {
        return "#" + (format(color.getRed())
                + format(color.getGreen())
                + format(color.getBlue())
        ).toUpperCase();
    }

    private void updateColor(Color color) {
        this.vBox.setStyle("-fx-background-color:" + toHexString(color) + ";");
        this.button.setStyle("-fx-background-color:" + toHexString(color) + ";");
    }
}
