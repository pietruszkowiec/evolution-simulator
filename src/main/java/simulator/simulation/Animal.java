package simulator.simulation;

import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private int energy;
    private MapDirection direction = MapDirection.NORTH;
    private List<Gene> genome;
    private Random randomGenerator = new Random();
    private final static int genomeLength = 32;

    public Animal() {
        this.genome = Gene.generateRandomGenome(genomeLength);
    }

    public void move() {
        Gene gene = Gene.getRandomGeneFromGenome(genome);
        MapBehaviour behaviour = gene.geneToMapBehaviour();

        this.direction = this.direction.changeDirectionWithMapBehaviour(behaviour);

        if (behaviour == MapBehaviour.FORWARD) {
            Vector2d nextPosition = this.position.add(this.direction.toUnitVector());
//            nextPosition = this.map.transformPosition(nextPosition);
            this.position = nextPosition;
        } else if (behaviour == MapBehaviour.BACKWARD) {
            Vector2d nextPosition = this.position.subtract(this.direction.toUnitVector());
//            nextPosition = this.map.transformPosition(nextPosition);
            this.position = nextPosition;
        }
    }
}
