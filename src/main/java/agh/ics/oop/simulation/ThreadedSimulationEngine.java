package agh.ics.oop.simulation;

public class ThreadedSimulationEngine extends SimulationEngine {
    private final int moveDelay;

    public ThreadedSimulationEngine(AbstractWorldMap map,
                                    int initialNumOfAnimals, int initialNumOfGrass,
                                    int startEnergy, int moveEnergy, int plantEnergy,
                                    int moveDelay) {
        super(map, initialNumOfAnimals, initialNumOfGrass,
                startEnergy, moveEnergy, plantEnergy);
        this.moveDelay = moveDelay;
    }


    public void run() {
        for (int i = 0; i < 10000; i++) {
            try {
                deleteAnimals(this.map.removeDeadAnimals());
                for (Animal animal : animals) {
                    animal.move(this.moveEnergy);
                }
                this.map.feedAnimals(this.plantEnergy);
                addAnimals(this.map.reproduceAnimals());
                this.map.addGrass();
                if (this.animals.size() == 0) {
                    break;
                }
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
