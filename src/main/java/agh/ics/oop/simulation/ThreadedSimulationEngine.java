package agh.ics.oop.simulation;

import javafx.application.Platform;

public class ThreadedSimulationEngine extends SimulationEngine implements Runnable {
    private final int moveDelay;

    public ThreadedSimulationEngine(AbstractWorldMap map,
                                    int initialNumOfAnimals, int initialNumOfGrass,
                                    int startEnergy, int moveEnergy, int plantEnergy,
                                    int moveDelay) {
        super(map, initialNumOfAnimals, initialNumOfGrass,
                startEnergy, moveEnergy, plantEnergy);
        this.moveDelay = moveDelay;
    }

    @Override
    public void run() {
        while (true) {
            try {
                deleteAnimals(this.map.removeDeadAnimals());
                for (Animal animal : animals) {
                    animal.move(this.moveEnergy);
                }
                this.map.feedAnimals(this.plantEnergy);
                addAnimals(this.map.reproduceAnimals());
                this.map.addGrass();
//                if (this.animals.size() == 0) {
//                    break;
//                }
                Platform.runLater(() -> this.map.drawMap());
//                this.map.drawMap();
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
