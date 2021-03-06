package agh.ics.oop.simulation;

import java.util.*;

public class Animal {
    private Vector2d position;
    private int energy;
    private final int startEnergy;
    private MapDirection direction = MapDirection.getRandomDirection();
    private final Genotype genotype;
    private final AbstractWorldMap map;
    private final List<IAnimalObserver> observers;
    private int age = 0;
    private int childrenCnt = 0;

    public Animal(Vector2d initialPosition,
                  int startEnergy, int initialEnergy,
                  Genotype genotype, AbstractWorldMap map) {
        this.position = initialPosition;
        this.startEnergy = startEnergy;
        this.energy = initialEnergy;
        this.genotype = genotype;
        this.map = map;
        this.observers = new LinkedList<>();
        this.age = 0;
    }

    public Animal(Vector2d initialPosition,
                  int startEnergy, int initialEnergy,
                  AbstractWorldMap map) {
        this.position = initialPosition;
        this.startEnergy = startEnergy;
        this.energy = initialEnergy;
        this.genotype = Genotype.generateRandomGenotype();
        this.map = map;
        this.observers = new LinkedList<>();
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenCnt() {
        return childrenCnt;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public void move(int moveEnergy) {
        Gene gene = this.genotype.getRandomGene();
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
        this.age++;
    }

    public void increaseEnergy(int energyGained) {
        this.energy += energyGained;
    }

    private void decreaseEnergy(int energyLost) {
        this.energy -= energyLost;
        if (this.energy < 0) {
            this.energy = 0;
        }
        energyChanged();
    }

    public boolean isDead() {
        return this.energy <= 0;
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

        int thisParentGenotypeShare = (Genotype.genotypeLength * this.energy)
                / (this.energy + other.energy);
        int otherParentGenotypeShare = Genotype.genotypeLength - thisParentGenotypeShare;

        Genotype childGenotype;
        if (leftOrRight == 0) {
            childGenotype = new Genotype(this.genotype, other.genotype, thisParentGenotypeShare);
        }  else {
            childGenotype = new Genotype(other.genotype, this.genotype, otherParentGenotypeShare);
        }

        this.decreaseEnergy(thisParentEnergyLoss);
        other.decreaseEnergy(otherParentEnergyLoss);

        this.childrenCnt++;
        other.childrenCnt++;

        return new Animal(this.position, this.startEnergy,
                childEnergy, childGenotype, this.map);
    }

    public void addObserver(IAnimalObserver observer) {
        this.observers.add(observer);
    }

    private void positionChanged(Vector2d oldPosition) {
        for (IAnimalObserver observer : this.observers) {
            observer.positionChanged(oldPosition, this);
        }
    }

    private void energyChanged() {
        for (IAnimalObserver observer : this.observers) {
            observer.energyChanged(this);
        }
    }
}
