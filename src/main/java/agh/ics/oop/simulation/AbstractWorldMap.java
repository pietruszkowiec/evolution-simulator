package agh.ics.oop.simulation;

import agh.ics.oop.application.gui.MapGrid;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractWorldMap implements IWorldMap, IAnimalObserver {
    protected final int width, height;
    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;
    private final int jungleWidth, jungleHeight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Random random;
    private final MapGrid mapGrid;
    private final int moveEnergy;
    private final int plantEnergy;
    private final boolean magicEvolution;
    private final ConcurrentHashMap<Vector2d, AnimalList> animalLists;
    private final HashMap<Vector2d, Grass> grassMap;
    private final List<Animal> animals;
    private final HashMap<Genotype, AtomicInteger> genotypeMap;
    private float avgLifeLength = 0;
    private int deathCnt = 0;
    private final AtomicInteger magicEvolutionCnt = new AtomicInteger(0);

    public AbstractWorldMap(int width, int height, float jungleRatio, int cellSize,
                            int moveEnergy, int plantEnergy, boolean magicEvolution) {
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.magicEvolution = magicEvolution;

        this.jungleWidth = (int) (width * jungleRatio);
        this.jungleHeight = (int) (height * jungleRatio);
        int jungleLowerLeftX = (this.width - this.jungleWidth) / 2;
        int jungleLowerLeftY = (this.height - this.jungleHeight) / 2;
        this.jungleLowerLeft = new Vector2d(jungleLowerLeftX, jungleLowerLeftY);
        this.jungleUpperRight = this.upperRight.subtract(jungleLowerLeft);

        this.random = new Random();
        this.mapGrid = new MapGrid(this, cellSize);

        this.animalLists = new ConcurrentHashMap<>(0);
        this.grassMap = new LinkedHashMap<>(0);
        this.animals = new LinkedList<>();
        this.genotypeMap = new LinkedHashMap<>(0);
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public MapGrid getMapGrid() {
        return mapGrid;
    }

    public int getAnimalsSize() {
        return this.animals.size();
    }

    public boolean isMagicEvolution() {
        return magicEvolution;
    }

    public AtomicInteger getMagicEvolutionCnt() {
        return magicEvolutionCnt;
    }

    public void incrementGenotypeCnt(Genotype genotype) {
        if (this.genotypeMap.containsKey(genotype)) {
            this.genotypeMap.get(genotype).incrementAndGet();
        } else {
            this.genotypeMap.put(genotype, new AtomicInteger(1));
        }
    }

    public void decrementGenotypeCnt(Genotype genotype) {
        int cnt;
        if (this.genotypeMap.containsKey(genotype)) {
            cnt = this.genotypeMap.get(genotype).decrementAndGet();
            if (cnt == 0) {
                genotypeMap.remove(genotype);
            }
        }
    }

    public void addNewAnimal(Animal animal) {
        this.animals.add(animal);
        incrementGenotypeCnt(animal.getGenotype());
        animal.addObserver(this);
        placeAnimal(animal);
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
        } while (isOccupied(position) && i < 2*mapArea);

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

        if (jungleArea <= 0) {
            return null;
        }

        do {
            x = this.random.nextInt(this.jungleWidth);
            y = this.random.nextInt(this.jungleHeight);
            position = this.jungleLowerLeft.add(new Vector2d(x, y));
            i++;
        } while (isOccupied(position) && i < 2*jungleArea);

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

        if (notJungleArea <= 0) {
            return null;
        }

        do {
            x = this.random.nextInt(this.width);
            y = this.random.nextInt(this.height);
            position = new Vector2d(x, y);
            i++;
        } while (isOccupied(position) && isInJungle(position) && i < 2*notJungleArea);

        if (isOccupied(position)) {
            return null;
        }

        return position;
    }

    public void removeDeadAnimals() {
        this.animalLists.values().forEach(animalList -> {
                    Animal animal;
                    while (!(animalList.isEmpty()) && animalList.last().isDead()) {
                        animal = animalList.last();
                        animalList.remove(animal);
                        avgLifeLength = avgLifeLength * ((float) deathCnt) / (deathCnt + 1)
                                + ((float) animal.getAge()) / (deathCnt + 1);
                        deathCnt++;
                        animals.remove(animal);
                        decrementGenotypeCnt(animal.getGenotype());
                    }
                });
    }

    public void moveAnimals() {
        List<Animal> animalList = new LinkedList<>(this.animals);
        for (Animal animal : animalList) {
            animal.move(this.moveEnergy);
        }
    }

    public void feedAnimals() {
        this.animalLists.forEach((position, animalList) -> {
                    if (isOccupiedByGrass(position)) {
                        List<Animal> strongestAnimals;
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
        this.animalLists.values().forEach(animalList -> {
                    List<Animal> firstTwoAnimals;
                    Animal firstParent;
                    Animal secondParent;
                    Animal child;
                    firstTwoAnimals = animalList.firstTwo();

                    if (firstTwoAnimals.size() == 2) {
                        firstParent = firstTwoAnimals.get(0);
                        secondParent = firstTwoAnimals.get(1);
                        if (firstParent.canReproduce() && secondParent.canReproduce()) {
                            child = firstParent.reproduce(secondParent);
                            animalList.add(child);
                            child.addObserver(this);
                            animals.add(child);
                            incrementGenotypeCnt(child.getGenotype());
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

    public void magicAddAnimals() {
        if (this.magicEvolutionCnt.get() >= 3) {
            return;
        }
        Animal newAnimal;
        Genotype newGenotype;
        Vector2d position;
        int energy;
        List<Animal> oldAnimalList;
        if (this.animals.size() <= 5) {
            oldAnimalList = new LinkedList<>(this.animals);
            for (Animal animal : oldAnimalList) {
                position = getRandomPosition();
                if (position != null) {
                    newGenotype = new Genotype(animal.getGenotype());
                    energy = animal.getStartEnergy();
                    newAnimal = new Animal(position, energy, energy, newGenotype, this);
                    addNewAnimal(newAnimal);
                }
            }
            this.magicEvolutionCnt.incrementAndGet();
        }
    }

    public void nextDayCycle() {
        removeDeadAnimals();
        moveAnimals();
        feedAnimals();
        reproduceAnimals();
        addGrass();
        if (this.magicEvolution) {
            magicAddAnimals();
        }
    }

    public void drawMap() {
        this.mapGrid.drawGrid();
    }

    public int getAnimalCnt() {
        return this.animals.size();
    }

    public int getGrassCnt() {
        return this.grassMap.size();
    }

    public float getAvgEnergy() {
        int energySum = 0;
        int cnt = 0;
        List<Animal> animalList = new LinkedList<>(this.animals);
        for (Animal animal : animalList) {
            energySum += animal.getEnergy();
            cnt++;
        }
        return ((float) energySum) / cnt;
    }

    public float getAvgLifeLength() {
        return avgLifeLength;
    }

    public float getAvgChildrenCnt() {
        int childrenSum = 0;
        int cnt = 0;
        List<Animal> animalList = new LinkedList<>(this.animals);
        for (Animal animal : animalList) {
            childrenSum += animal.getChildrenCnt();
            cnt++;
        }
        return ((float) childrenSum) / cnt;
    }

    public Genotype getDominantGenotype() {
        Genotype dominant = null;
        int maxCnt = 0;
        int cnt;
        Genotype genotype;

        for (Map.Entry<Genotype, AtomicInteger> entry : this.genotypeMap.entrySet()) {
            genotype = entry.getKey();
            cnt = entry.getValue().get();
            if (cnt > maxCnt) {
                maxCnt = cnt;
                dominant = genotype;
            }
        }

        return dominant;
    }
}
