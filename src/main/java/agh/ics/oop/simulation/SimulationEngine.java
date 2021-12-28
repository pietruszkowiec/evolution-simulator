package agh.ics.oop.simulation;

public class SimulationEngine {
    public final AbstractWorldMap map;
    protected final int initialNumOfAnimals;
    protected final int initialNumOfGrass;
    protected final int startEnergy;

    public SimulationEngine(AbstractWorldMap map,
                            int initialNumOfAnimals, int initialNumOfGrass,
                            int startEnergy) {
        this.map = map;
        this.initialNumOfAnimals = initialNumOfAnimals;
        this.initialNumOfGrass = initialNumOfGrass;
        this.startEnergy = startEnergy;
        createAnimals();
        createGrass();
    }

    public void createAnimals() {
        Animal animal;
        Vector2d position;

        for (int i = 0; i < this.initialNumOfAnimals; i++) {
            position = this.map.getRandomPositionInJungle();
            if (position == null) {
                position = this.map.getRandomPosition();
                if (position == null) {
//                TODO throwing exception, map too small
                    break;
                }
            }
            animal = new Animal(position, this.startEnergy,
                    this.startEnergy, this.map);
            this.map.addNewAnimal(animal);
        }
    }

    public void createGrass() {
        Grass grass;
        Vector2d position;

        for (int i = 0; i < this.initialNumOfGrass; i++) {
            position = this.map.getRandomPosition();
            if (position == null) {
                break;
            }
            grass = new Grass(position);
            this.map.placeGrass(grass);
        }
    }
}
