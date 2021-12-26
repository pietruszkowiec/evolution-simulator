package agh.ics.oop.simulation;

import javafx.application.Platform;

public class ThreadedSimulationEngine extends SimulationEngine implements Runnable {
    private final int moveDelay;
    public boolean isPaused;

    public ThreadedSimulationEngine(AbstractWorldMap map,
                                    int initialNumOfAnimals, int initialNumOfGrass,
                                    int startEnergy, int moveDelay) {
        super(map, initialNumOfAnimals, initialNumOfGrass, startEnergy);
        this.moveDelay = moveDelay;
    }


    public void pause() {
        synchronized (this) {
            this.isPaused = true;
        }
    }

    public void resume() {
        synchronized (this) {
            this.isPaused = false;
            this.notifyAll();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (this.isPaused) {
                    synchronized (this) {
                        this.wait();
                    }
                }

                this.map.nextDayCycle();

                Platform.runLater(() -> this.map.drawMap());

                Thread.sleep(this.moveDelay);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
