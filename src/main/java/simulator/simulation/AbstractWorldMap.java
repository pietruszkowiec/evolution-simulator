package simulator.simulation;

public abstract class AbstractWorldMap implements IWorldMap {

    @Override
    public boolean place(Animal animal) {
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        return null;
    }
}
