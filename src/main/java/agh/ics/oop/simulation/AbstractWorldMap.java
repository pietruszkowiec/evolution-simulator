package agh.ics.oop.simulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class AbstractWorldMap implements IWorldMap, IAnimalObserver {
    protected final int width, height;
    protected final Vector2d lowerLeftBound;
    protected final Vector2d upperRightBound;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final float jungleRatio;
    protected final int jungleWidth, jungleHeight;
    protected final Vector2d jungleLowerLeft;
    protected final Vector2d jungleUpperRight;
    protected final Random random;
    protected HashMap<Vector2d, AnimalSet> animalSets;
    protected HashMap<Vector2d, Grass> grassMap;

    public AbstractWorldMap(int width, int height, int moveEnergy,
                            int plantEnergy, float jungleRatio) {
        this.width = width;
        this.height = height;
        this.lowerLeftBound = new Vector2d(0, 0);
        this.upperRightBound = new Vector2d(width-1, height-1);
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;

        this.jungleWidth = (int) (width * jungleRatio);
        this.jungleHeight = (int) (height * jungleRatio);
        int jungleLowerLeftX = (this.width - this.jungleWidth) / 2;
        int jungleLowerLeftY = (this.height - this.jungleHeight) / 2;
        this.jungleLowerLeft = new Vector2d(jungleLowerLeftX, jungleLowerLeftY);
        this.jungleUpperRight = new Vector2d(jungleLowerLeftX+this.jungleWidth,
                jungleLowerLeftY+this.jungleHeight);

        this.random = new Random();
    }

    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        AnimalSet animalSet;
        if (isOccupiedByAnimal(position)) {
            animalSet = this.animalSets.get(position);
            animalSet.add(animal);
        } else {
            animalSet = new AnimalSet();
            animalSet.add(animal);
            this.animalSets.put(position, animalSet);
        }
        animal.addObserver(this);
    }

    public boolean placeGrass(Grass grass) {
        Vector2d position = grass.getPosition();
        if (!(isOccupiedByAnimal(position)) && !(isOccupiedByGrass(position))) {
            this.grassMap.put(position, grass);
            return true;
        }
        return false;
    }

    public Animal animalAt(Vector2d position) {
        if (isOccupiedByAnimal(position)) {
            AnimalSet animalSet = animalSets.get(position);
            return animalSet.last();
        }
        return null;
    }

    public Grass grassAt(Vector2d position) {
        if (isOccupiedByGrass(position)) {
            return grassMap.get(position);
        }
        return null;
    }

    public boolean isOccupied(Vector2d position) {
        return isOccupiedByAnimal(position) || isOccupiedByAnimal(position);
    }

    public boolean isOccupiedByAnimal(Vector2d position) {
        return animalSets.containsKey(position);
    }

    public boolean isOccupiedByGrass(Vector2d position) {
        return grassMap.containsKey(position);
    }

    public boolean isInJungle(Vector2d position) {
        return position.follows(this.jungleLowerLeft)
                && position.precedes(this.jungleUpperRight);
    }

    public Vector2d getRandomPosition() {
        Vector2d position;
        int x, y;
        int i = 0;
        int mapArea = this.width * this.height;

        do {
            x = this.random.nextInt(this.width);
            y = this.random.nextInt(this.height);
            position = new Vector2d(x, y);
            i++;
        } while (isOccupied(position) && i < mapArea);

        if (isOccupied(position)) {
            return null;
        }

        return position;
    }

    public Vector2d getRandomPositionInJungle() {
        Vector2d position;
        int x, y;
        int i = 0;
        int jungleArea = this.jungleWidth * this.jungleHeight;

        do {
            x = this.random.nextInt(this.jungleWidth);
            y = this.random.nextInt(this.jungleHeight);
            position = this.jungleLowerLeft.add(new Vector2d(x, y));
            i++;
        } while (isOccupied(position) && i < jungleArea);

        if (isOccupied(position)) {
            return null;
        }

        return position;
    }

    public Vector2d getRandomPositionOutsideJungle() {
        Vector2d position;
        int x, y;
        int i = 0;
        int notJungleArea = this.width * this.height
                - this.jungleWidth * this.jungleHeight;

        do {
            x = this.random.nextInt(this.width);
            y = this.random.nextInt(this.height);
            position = new Vector2d(x, y);
            i++;
        } while (isOccupied(position) && isInJungle(position) && i < notJungleArea);

        if (isOccupied(position)) {
            return null;
        }

        return position;
    }

    public void feedAnimals() {
        List<Animal> strongestAnimals;
        AnimalSet animalSet;

        for (Vector2d position : this.animalSets.keySet()) {
            if (isOccupiedByGrass(position)) {
                animalSet = this.animalSets.get(position);
                strongestAnimals = animalSet.firstWithTies();
                int energyForEach = this.plantEnergy / strongestAnimals.size();
                for (Animal animal : strongestAnimals) {
                    animal.increaseEnergy(energyForEach);
                    animalSet.remove(animal);
                    animalSet.add(animal);
                }
                this.grassMap.remove(position);
            }
        }
    }

    public List<Animal> reproduceAnimals() {
        List<Animal> firstTwoAnimals;
        List<Animal> children = new LinkedList<>();
        Animal firstParent;
        Animal secondParent;
        Animal child;

        for (AnimalSet animalSet : this.animalSets.values()) {
            firstTwoAnimals = animalSet.firstTwo();
            if (firstTwoAnimals.size() == 2) {
                firstParent = firstTwoAnimals.get(0);
                secondParent = firstTwoAnimals.get(1);
                child = firstParent.reproduce(secondParent);
                children.add(child);
                animalSet.add(child);
                child.addObserver(this);
            }
        }

        return children;
    }

    public void addGrass() {
        Vector2d inJunglePosition = getRandomPositionInJungle();
        Vector2d outsideJunglePosition = getRandomPositionOutsideJungle();

        if (inJunglePosition != null) {
            placeGrass(new Grass(inJunglePosition));
        }
        if (outsideJunglePosition != null) {
            placeGrass(new Grass(outsideJunglePosition));
        }
    }

    public void positionChanged(Vector2d oldPosition, Animal animal) {
        AnimalSet animalSet = this.animalSets.get(oldPosition);
        animalSet.remove(animal);
        placeAnimal(animal);
    }

    public void animalDied(Animal animal) {
        Vector2d position = animal.getPosition();
        AnimalSet animalSet = this.animalSets.get(position);
        animalSet.remove(animal);
        if (animalSet.isEmpty()) {
            this.animalSets.remove(position);
        }
    }
}
