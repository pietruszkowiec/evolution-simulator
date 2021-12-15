package agh.ics.oop.simulation;

import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private final int startEnergy;
    private int energy;
    private MapDirection direction = MapDirection.getRandomDirection();
    private final List<Gene> genome;
    public static final int genomeLength = 32;
    private final AbstractWorldMap map;

    public Animal(Vector2d initialPosition,
                  int startEnergy, int initialEnergy,
                  List<Gene> genome, AbstractWorldMap map) {
        this.position = initialPosition;
        this.startEnergy = startEnergy;
        this.energy = initialEnergy;
        this.genome = genome;
        this.map = map;
    }

    public Animal(Vector2d initialPosition,
                  int startEnergy, int initialEnergy,
                  AbstractWorldMap map) {
        this.position = initialPosition;
        this.startEnergy = startEnergy;
        this.energy = initialEnergy;
        this.genome = Gene.generateRandomGenome();
        this.map = map;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public void move() {
        Gene gene = Gene.getRandomGeneFromGenome(genome);
        MapBehaviour behaviour = gene.geneToMapBehaviour();

        this.direction = this.direction.changeDirectionWithMapBehaviour(behaviour);

        if (behaviour == MapBehaviour.FORWARD) {
            Vector2d nextPosition = this.position.add(this.direction.toUnitVector());
            this.position = this.map.transformPosition(this.position, nextPosition);
        } else if (behaviour == MapBehaviour.BACKWARD) {
            Vector2d nextPosition = this.position.subtract(this.direction.toUnitVector());
            this.position = this.map.transformPosition(this.position, nextPosition);
        }

        decreaseEnergy(this.map.moveEnergy);
    }

    public void increaseEnergy(int energyGained) {
        this.energy += energyGained;
    }

    public void decreaseEnergy(int energyLost) {
        this.energy -= energyLost;
    }

    public Animal reproduce(Animal other) {
        int thisParentEnergyLoss = this.energy / 4;
        int otherParentEnergyLoss = other.energy / 4;

        int childEnergy = thisParentEnergyLoss + otherParentEnergyLoss;

        Random random = new Random();
        int leftOrRight = random.nextInt(2);

        int thisParentGenomeShare = (Animal.genomeLength * this.energy)
                / (this.energy + other.energy);
        int otherParentGenomeShare = Animal.genomeLength - thisParentGenomeShare;

        List<Gene> leftPartGenome;
        List<Gene> rightPartGenome;
        if (leftOrRight == 0) {
            leftPartGenome = this.genome.subList(0, thisParentGenomeShare);
            rightPartGenome = other.genome.subList(thisParentGenomeShare, Animal.genomeLength);
        }  else {
            leftPartGenome = other.genome.subList(0, otherParentGenomeShare);
            rightPartGenome = this.genome.subList(otherParentGenomeShare, Animal.genomeLength);
        }

        List<Gene> childGenome = leftPartGenome;
        childGenome.addAll(rightPartGenome);

        this.decreaseEnergy(thisParentEnergyLoss);
        other.decreaseEnergy(otherParentEnergyLoss);

        return new Animal(this.position, this.startEnergy,
                childEnergy, childGenome, this.map);
    }
}
