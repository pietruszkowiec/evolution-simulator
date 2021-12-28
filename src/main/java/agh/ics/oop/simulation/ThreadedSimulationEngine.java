package agh.ics.oop.simulation;

import agh.ics.oop.application.gui.StatisticsPanel;
import javafx.application.Platform;

public class ThreadedSimulationEngine extends SimulationEngine implements Runnable {
    private final int moveDelay;
    public boolean isPaused;
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

//                System.out.println(Thread.currentThread().getName()
//                        + " : Day " + day
//                        + "\n\t animals = " + this.map.getAnimalCnt()
//                        + "\n\t grass = " + this.map.getGrassCnt()
//                        + "\n\t avg energy = " + this.map.getAvgEnergy()
//                        + "\n\t avg life length = " + this.map.getAvgLifeLength()
//                        + "\n\t avg num of children = " + this.map.getAvgChildrenCnt()
//                        + "\n\t dominant genotype = " + this.map.getDominantGenotype());

                if (this.day % 20 == 0) {
                    Platform.runLater(() -> statisticsPanel.updateSeries());
                }
                Platform.runLater(() -> this.map.drawMap());

                this.day++;

                Thread.sleep(this.moveDelay);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
