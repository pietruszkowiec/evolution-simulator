package simulator.simulation;

public enum Rotation {
    D0,
    D45,
    D90,
    D135,
    D180,
    D225,
    D270,
    D315;

    @Override
    public String toString() {
        return switch(this) {
            case D0 -> "0";
            case D45 -> "45";
            case D90 -> "90";
            case D135 -> "135";
            case D180 -> "180";
            case D225 -> "225";
            case D270 -> "270";
            case D315 -> "315";
        };
    }
}
