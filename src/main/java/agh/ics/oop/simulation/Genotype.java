package agh.ics.oop.simulation;

import java.util.*;

public class Genotype {
    public static final int genotypeLength = 32;
    private final List<Gene> genes;

    public Genotype() {
        this.genes = new ArrayList<>(0);
    }

    public Genotype(Genotype genotype) {
        this.genes = new ArrayList<>(genotype.genes);
    }

    public Genotype(Genotype left, Genotype right, int cutPoint) {
        this.genes = new ArrayList<>(left.genes.subList(0, cutPoint));
        List<Gene> rightPart = new ArrayList<>(right.genes.subList(cutPoint, genotypeLength));
        this.genes.addAll(rightPart);
        Collections.sort(this.genes);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Genotype)) {
            return false;
        }
        Genotype that = (Genotype) other;
        for (int i = 0; i < genotypeLength; i++) {
            if (this.genes.get(i) != that.genes.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.genes.toArray(new Gene[0]));
    }

    public static Genotype generateRandomGenotype() {
        Random random = new Random();
        Genotype genotype = new Genotype();

        Gene chosenGene;
        for (int i = 0; i < genotypeLength; i++) {
            chosenGene = Gene.values()[random.nextInt(Gene.values().length)];
            genotype.genes.add(chosenGene);
        }

        Collections.sort(genotype.genes);
        return genotype;
    }

    public Gene getRandomGene() {
        Random random = new Random();
        int randomIndex = random.nextInt(Genotype.genotypeLength);
        return this.genes.get(randomIndex);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Gene gene : this.genes) {
            if (i < 10) {
                builder.append(" ");
            }
            builder.append(i);
            builder.append(" : ").append(gene).append(" \n");
            i++;
        }
        return builder.toString();
    }
}
