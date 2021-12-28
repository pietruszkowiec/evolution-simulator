package agh.ics.oop.simulation;

import agh.ics.oop.application.gui.StatisticsPanel;
import javafx.application.Platform;

public class ThreadedSimulationEngine extends SimulationEngine implements Runnable {
    private final int moveDelay;
    private boolean paused;
    private int day = 0;
    private StatisticsPanel statisticsPanel;

    public ThreadedSimulationEngine(AbstractWorldMap map,
                                    int initialNumOfAnimals, int initialNumOfGrass,
                                    int startEnergy, int moveDelay) {
        super(map, initialNumOfAnimals, initialNumOfGrass, startEnergy);
        this.moveDelay = moveDelay;
    }

    public void setStatisticsPanel(StatisticsPanel statisticsPanel) {
        this.statisticsPanel = statisticsPanel;
    }

    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }

    public int getDay() {
        return day;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        synchronized (this) {
            this.paused = true;
        }
    }

    public void resume() {
        synchronized (this) {
            this.paused = false;
            this.notifyAll();
        }
    }

    @Override
    public void run() {
        while (this.map.getAnimalsSize() > 0) {
            try {
                if (this.paused) {
                    synchronized (this) {
                        this.wait();
                    }
                }

                this.map.nextDayCycle();

                if (this.day % 20 == 0) {
                    Platform.runLater(() -> statisticsPanel.updateSeries());
                }

                if (this.map.isMagicEvolution()) {
                    Platform.runLater(() -> statisticsPanel.updateMagicEvolutionCnt());
                }

                Platform.runLater(this.map::drawMap);

                this.day++;

                Thread.sleep(this.moveDelay);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
