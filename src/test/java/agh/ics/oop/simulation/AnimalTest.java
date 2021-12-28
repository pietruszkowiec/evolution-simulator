package agh.ics.oop.simulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    @Test
    public void canReproduceTest() {
        AbstractWorldMap map = null;

        int initialFatherEnergy = 60;
        int initialMotherEnergy = 20;

        Vector2d position = new Vector2d(1, 1);
        Animal father = new Animal(position, 100, initialFatherEnergy, map);
        assertTrue(father.canReproduce());

        Animal badFather = new Animal(position, 100, initialMotherEnergy, map);
        assertFalse(badFather.canReproduce());
    }

    @Test
    public void reproduceTest() {
        AbstractWorldMap map = null;

        int initialFatherEnergy = 60;
        int initialMotherEnergy = 20;

        Vector2d position = new Vector2d(1, 1);

        Animal father = new Animal(position, 100, initialFatherEnergy, map);
        Animal mother = new Animal(position, 100, initialMotherEnergy, map);

        Animal child = father.reproduce(mother);

        assertEquals(child.getPosition(), position);

        assertEquals(father.getEnergy(), (initialFatherEnergy * 3 / 4));
        assertEquals(mother.getEnergy(), (initialMotherEnergy * 3 / 4));

        int cutPoint = (Genotype.genotypeLength * father.getEnergy())
                / (father.getEnergy() + mother.getEnergy());

        assertEquals(child.getPosition(), position);

        Genotype fatherGenotype = father.getGenotype();
        Genotype motherGenotype = mother.getGenotype();
        Genotype childGenotype = child.getGenotype();

        Genotype correctLeftChildGenotype = new Genotype(fatherGenotype,
                motherGenotype, cutPoint);

        Genotype correctRightChildGenotype = new Genotype(motherGenotype,
                fatherGenotype, Genotype.genotypeLength - cutPoint);

        assertTrue(childGenotype.equals(correctLeftChildGenotype)
                || childGenotype.equals(correctRightChildGenotype));
    }

}
