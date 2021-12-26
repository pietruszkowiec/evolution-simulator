package agh.ics.oop.application;

import agh.ics.oop.application.gui.App;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
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
