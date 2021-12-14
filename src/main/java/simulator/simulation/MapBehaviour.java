package simulator.simulation;

public enum MapBehaviour {
    FORWARD,
    R_45,
    R_90,
    R_135,
    BACKWARD,
    R_225,
    R_270,
    R_315;

    @Override
    public String toString() {
        return switch (this) {
            case FORWARD -> "FWD";
            case R_45 -> "45";
            case R_90 -> "90";
            case R_135 -> "135";
            case BACKWARD -> "BWD";
            case R_225 -> "225";
            case R_270 -> "270";
            case R_315 -> "315";
        };
    }
}
