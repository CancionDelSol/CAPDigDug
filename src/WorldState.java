import java.util.Arrays;

public class WorldState implements IWorldState {
    //region Members
    private double[] _encodedState;
    private long _curTime = 0L;
    //endregion

    //region Constructor
    /** Generates a random DigDug world */
    public WorldState() throws Exception {
        throw new Exception("TODO");
    }

    /** Create a copy of an initializer world state */
    private WorldState(WorldState initializer) throws Exception {
        double[] origState = initializer.getEncoding();
        _encodedState = Arrays.copyOf(origState, origState.length);
        _curTime = initializer.getTime();

        // TODO : Copy the rest of the world state
        Logger.Throw("TODO");
    }
    //endregion

    //region IWorldState
    public int getLength() throws Exception {
        return _encodedState.length;
    }
    public double[] getEncoding() throws Exception {
        return Arrays.copyOf(_encodedState, _encodedState.length);
    }
    public long getTime() throws Exception {
        return _curTime;
    }
    public boolean getIsComplete() throws Exception {
        Logger.Throw("TODO");
        return true;
    }
    public void ApplyDelta(IDeltaWorldState dState) throws Exception {
        Logger.Throw("TODO");
    }
    public IWorldState clone() {
        try {
            return new WorldState(this);
        } catch (Exception exc) {
            Logger.Error("Exception during clone: " + exc.getMessage());
        }
        return null;
    }
    //endregion

    //region Object

    //endregion

    public static GenAlg.IErrorFunction digDugEvaluation = (worldState) -> { 
        double rVal = 0.0;
        try {
            Logger.Throw("TODO");
        } catch (Exception exc) {
            Logger.Error("Exception in error function: " + exc.getMessage());
        }
        return rVal;
    };
}