package simulator.simulation;

import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private int energy;
    private MapDirection direction = MapDirection.NORTH;
    private List<Gene> genome;
    private Random randomGenerator = new Random();

    public Animal() {
        this.genome = Gene.generateRandomGenome(32);
    }

//    public void move() {
//
//    }
}
