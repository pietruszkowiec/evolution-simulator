package agh.ics.oop.simulation;

import java.util.*;

public enum Gene {
    G_0,
    G_1,
    G_2,
    G_3,
    G_4,
    G_5,
    G_6,
    G_7;

    public static List<Gene> generateRandomGenome() {
        Random random = new Random();
        List<Gene> genomeList = new ArrayList<>();

        Gene chosenGene;
        for (int i = 0; i < Animal.genomeLength; i++) {
            chosenGene = values()[random.nextInt(values().length)];
            genomeList.add(chosenGene);
        }

        Collections.sort(genomeList);
        return genomeList;
    }

    public static Gene getRandomGeneFromGenome(List<Gene> genome) {
        Random random = new Random();
        int randomIndex = random.nextInt(Animal.genomeLength);
        return genome.get(randomIndex);
    }

    public MapBehaviour geneToMapBehaviour() {
        return switch (this) {
            case G_0 -> MapBehaviour.FORWARD;
            case G_1 -> MapBehaviour.R_45;
            case G_2 -> MapBehaviour.R_90;
            case G_3 -> MapBehaviour.R_135;
            case G_4 -> MapBehaviour.BACKWARD;
            case G_5 -> MapBehaviour.R_225;
            case G_6 -> MapBehaviour.R_270;
            case G_7 -> MapBehaviour.R_315;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case G_0 -> "0";
            case G_1 -> "1";
            case G_2 -> "2";
            case G_3 -> "3";
            case G_4 -> "4";
            case G_5 -> "5";
            case G_6 -> "6";
            case G_7 -> "7";
        };
    }

}
