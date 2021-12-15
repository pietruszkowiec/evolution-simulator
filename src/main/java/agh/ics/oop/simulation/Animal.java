package agh.ics.oop.simulation;

import java.util.*;

public class Animal {
    private Vector2d position;
    private int energy;
    private final int startEnergy;
    private MapDirection direction = MapDirection.getRandomDirection();
    public final List<Gene> genome;
    public static final int genomeLength = 32;
    private final AbstractWorldMap map;
    private final List<IAnimalObserver> observers;

    public Animal(Vector2d initialPosition,
                  int startEnergy, int initialEnergy,
                  List<Gene> genome, AbstractWorldMap map) {
        this.position = initialPosition;
        this.startEnergy = startEnergy;
        this.energy = initialEnergy;
        this.genome = genome;
        this.map = map;
        this.observers = new LinkedList<>();
    }

    public Animal(Vector2d initialPosition,
                  int startEnergy, int initialEnergy,
                  AbstractWorldMap map) {
        this.position = initialPosition;
        this.startEnergy = startEnergy;
        this.energy = initialEnergy;
        this.genome = Gene.generateRandomGenome();
        this.map = map;
        this.observers = new LinkedList<>();
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public void move(int moveEnergy) {
        Gene gene = Gene.getRandomGeneFromGenome(genome);
        MapBehaviour behaviour = gene.geneToMapBehaviour();
        Vector2d oldPosition = this.position;

        this.direction = this.direction.changeDirectionWithMapBehaviour(behaviour);

        if (behaviour == MapBehaviour.FORWARD) {
            Vector2d nextPosition = this.position.add(this.direction.toUnitVector());
            this.position = this.map.transformPosition(this.position, nextPosition);
        } else if (behaviour == MapBehaviour.BACKWARD) {
            Vector2d nextPosition = this.position.subtract(this.direction.toUnitVector());
            this.position = this.map.transformPosition(this.position, nextPosition);
        }

        positionChanged(oldPosition);
        decreaseEnergy(moveEnergy);
    }

    public void increaseEnergy(int energyGained) {
        this.energy += energyGained;
    }

    public void decreaseEnergy(int energyLost) {
        this.energy -= energyLost;
        if (this.energy < 0) {
            this.energy = 0;
        }
        energyChanged();
    }

    public boolean isDead() {
        return this.energy == 0;
    }

    public boolean canReproduce() {
        return this.energy >= (this.startEnergy / 2);
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

        List<Gene> childGenome = new ArrayList<>(leftPartGenome);
        childGenome.addAll(rightPartGenome);
        Collections.sort(childGenome);

        this.decreaseEnergy(thisParentEnergyLoss);
        other.decreaseEnergy(otherParentEnergyLoss);

        return new Animal(this.position, this.startEnergy,
                childEnergy, childGenome, this.map);
    }

    public void addObserver(IAnimalObserver observer) {
        this.observers.add(observer);
    }

    public void positionChanged(Vector2d oldPosition) {
        for (IAnimalObserver observer : this.observers) {
            observer.positionChanged(oldPosition, this);
        }
    }

    public void energyChanged() {
        for (IAnimalObserver observer : this.observers) {
            observer.energyChanged(this);
        }
    }

    public String toString() {
        String res = this.energy + "";
        if (this.energy < 10) {
            res += " ";
        }
        res += this.direction;
        return res;
    }
}
