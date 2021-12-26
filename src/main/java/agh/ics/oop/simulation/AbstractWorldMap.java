package agh.ics.oop.simulation;

import agh.ics.oop.application.gui.MapGrid;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWorldMap implements IWorldMap, IAnimalObserver {
    protected final int width, height;
    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;
    protected final float jungleRatio;
    protected final int jungleWidth, jungleHeight;
    protected final Vector2d jungleLowerLeft;
    protected final Vector2d jungleUpperRight;
    protected final int cellSize;
    protected final Random random;
    protected final MapGrid mapGrid;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected ConcurrentHashMap<Vector2d, AnimalList> animalLists;
    protected HashMap<Vector2d, Grass> grassMap;

    public AbstractWorldMap(int width, int height, float jungleRatio, int cellSize,
                            int moveEnergy, int plantEnergy) {
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);
        this.jungleRatio = jungleRatio;
        this.cellSize = cellSize;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;

        this.jungleWidth = (int) (width * jungleRatio);
        this.jungleHeight = (int) (height * jungleRatio);
        int jungleLowerLeftX = (this.width - this.jungleWidth) / 2;
        int jungleLowerLeftY = (this.height - this.jungleHeight) / 2;
        this.jungleLowerLeft = new Vector2d(jungleLowerLeftX, jungleLowerLeftY);
        this.jungleUpperRight = new Vector2d(jungleLowerLeftX+this.jungleWidth,
                jungleLowerLeftY+this.jungleHeight);

        this.random = new Random();
        this.mapGrid = new MapGrid(this, cellSize);

        this.animalLists = new ConcurrentHashMap<>(0);
        this.grassMap = new LinkedHashMap<>(0);
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public MapGrid getMapGrid() {
        return mapGrid;
    }

    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        AnimalList animalList;
        if (isOccupiedByAnimal(position)) {
            animalList = this.animalLists.get(position);
            animalList.add(animal);
        } else {
            animalList = new AnimalList();
            animalList.add(animal);
            this.animalLists.put(position, animalList);
        }
    }

    public void placeGrass(Grass grass) {
        Vector2d position = grass.getPosition();
        if (!(isOccupiedByAnimal(position)) && !(isOccupiedByGrass(position))) {
            this.grassMap.put(position, grass);
        }
    }

    public Object objectAt(Vector2d position) {
        Animal animal = animalAt(position);
        if (animal != null) {
            return animal;
        }
        return grassAt(position);
    }

    public Animal animalAt(Vector2d position) {
        if (isOccupiedByAnimal(position)) {
            AnimalList animalList = animalLists.get(position);
            return animalList.first();
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
        return isOccupiedByAnimal(position) || isOccupiedByGrass(position);
    }

    public boolean isOccupiedByAnimal(Vector2d position) {
        return animalLists.containsKey(position) && !(animalLists.get(position).isEmpty());
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

    public void removeDeadAnimals() {
        this.animalLists.forEach(
                (position, animalList) -> {
                    while (!(animalList.isEmpty()) && animalList.last().isDead()) {
                        animalList.remove(animalList.last());
                    }
                });
    }

    public void moveAnimals() {
        this.animalLists.forEach(
                (position, animalList) -> {
                    if (!(animalList.isEmpty())) {
                        for (Animal animal : animalList.getAnimals()) {
                            animal.move(this.moveEnergy);
                        }
                    }
                });
    }

    public void feedAnimals() {
        this.animalLists.forEach(
                (position, animalList) -> {
                    if (isOccupiedByGrass(position)) {
                        List<Animal> strongestAnimals;
                        animalList = this.animalLists.get(position);
                        strongestAnimals = animalList.firstWithTies();
                        if (strongestAnimals.size() > 0) {
                            int energyForEach = this.plantEnergy / strongestAnimals.size();
                            for (Animal animal : strongestAnimals) {
                                animal.increaseEnergy(energyForEach);
                                animalList.remove(animal);
                                animalList.add(animal);
                            }
                            this.grassMap.remove(position);
                        }
                    }
        });
    }

    public void reproduceAnimals() {
        this.animalLists.forEach(
                (position, animalList) -> {
                    List<Animal> firstTwoAnimals;
                    List<Animal> children = new LinkedList<>();
                    Animal firstParent;
                    Animal secondParent;
                    Animal child;
                    firstTwoAnimals = animalList.firstTwo();
                    if (firstTwoAnimals.size() == 2) {
                        firstParent = firstTwoAnimals.get(0);
                        secondParent = firstTwoAnimals.get(1);
                        if (firstParent.canReproduce() && secondParent.canReproduce()) {
                            child = firstParent.reproduce(secondParent);
                            children.add(child);
                            animalList.add(child);
                            child.addObserver(this);
                        }
                    }
        });
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
        AnimalList animalList = this.animalLists.get(oldPosition);
        animalList.remove(animal);
        placeAnimal(animal);
    }

    public void energyChanged(Animal animal) {
        Vector2d position = animal.getPosition();
        AnimalList animalList = this.animalLists.get(position);
        animalList.remove(animal);
        animalList.add(animal);
    }

    public void nextDayCycle() {
        removeDeadAnimals();
        moveAnimals();
        feedAnimals();
        reproduceAnimals();
        addGrass();
    }

    public void drawMap() {
        this.mapGrid.drawGrid();
    }
}
