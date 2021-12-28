package agh.ics.oop.simulation;

public enum Gene {
    G_0,
    G_1,
    G_2,
    G_3,
    G_4,
    G_5,
    G_6,
    G_7;


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
