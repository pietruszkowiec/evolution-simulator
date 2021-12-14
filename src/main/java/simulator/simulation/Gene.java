package simulator.simulation;

import java.util.*;

public enum Gene {
    G0,
    G1,
    G2,
    G3,
    G4,
    G5,
    G6,
    G7;

    public static List<Gene> generateRandomGenome(int length) {
        Random random = new Random();
        List<Gene> genomeList = new ArrayList<>();
        Gene chosenGene;
        for(int i = 0; i < length; i++) {
            chosenGene = values()[random.nextInt(values().length)];
            genomeList.add(chosenGene);
        }
        Collections.sort(genomeList);
        return genomeList;
    }

    public static Gene getRandomGeneFromGenome(List<Gene> genome) {
        Random random = new Random();
        int randomIndex = random.nextInt(genome.size());
        return genome.get(randomIndex);
    }

    public static Rotation geneToRotation(Gene gene) {
        return switch(gene) {
            case G0 -> Rotation.D0;
            case G1 -> Rotation.D45;
            case G2 -> Rotation.D90;
            case G3 -> Rotation.D135;
            case G4 -> Rotation.D180;
            case G5 -> Rotation.D225;
            case G6 -> Rotation.D270;
            case G7 -> Rotation.D315;
        };
    }


}
