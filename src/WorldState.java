import java.util.Arrays;

public class WorldState implements IWorldState {
    //region Gameboard
    private final int _x;
    private final int _y;
    private final TileType[] _tiles;
    
    /** Return the tile piece at a given coordinate */
    private TileType _getAtCoord(int x, int y) throws Exception {
        return _tiles[_getIndex(x, y)];
    }

    /** Set the tile type at a given coordinate */
    private void _setAtCoord(int x, int y, TileType tile) throws Exception {
        _tiles[_getIndex(x, y)] = tile;
    }

    /** Return the index in the _values array */
    private int _getIndex(int x, int y) throws Exception {
        int index = (y * _x) + x;
        if (index < 0 || index >= _tiles.length)
            Logger.Throw("Out of bounds [" + index + "]: (" + x + ", " + y + ") ");

        return index;
    }
    //endregion

    //region Members
    private double[] _encodedState = null;
    private long _curTime = 0L;
    //endregion

    //region Constructor
    /** Generates a random DigDug world */
    public WorldState(int x, int y) throws Exception {
        _x = x;
        _y = y;
        _tiles = new TileType[x * y];

        // Build a random map
        RandomizeTiles();

        // TODO : WorldState.WorldState
    }

    /** Create a copy of an initializer world state */
    private WorldState(WorldState initializer) throws Exception {
        _x = initializer._x;
        _y = initializer._y;
        _tiles = new TileType[_x * _y];

        for (int i = 0; i < _tiles.length; i++) {
            _tiles[i] = initializer._tiles[i];
        }

        _curTime = initializer._curTime;
    }
    //endregion

    //region IWorldState
    public int getLength() throws Exception {
        return getEncoding().length;
    }
    public double[] getEncoding() throws Exception {
        if (_encodedState == null)
            CacheEncodedState();

        return Arrays.copyOf(_encodedState, _encodedState.length);
    }
    public long getTime() throws Exception {
        return _curTime;
    }
    public boolean getIsComplete() throws Exception {
        return CheckGameCompletion();
    }
    public void ApplyDelta(IDeltaWorldState dState) throws Exception {
        _curTime++;

        // TODO : WorldState.ApplyDelta
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

    //region IErrorFunction
    public static GenAlg.IErrorFunction digDugEvaluation = (worldState) -> { 
        double rVal = 0.0;
        try {
            if (!(worldState instanceof WorldState))
                Logger.Throw("Incorrect worldState object type");

            return ((WorldState)worldState).GetDigAgentScore();

        } catch (Exception exc) {
            Logger.Error("Exception in error function: " + exc.getMessage());
        }
        return rVal;
    };

    private double GetDigAgentScore() {
        // TODO : WorldState.GetDigAgentScore
        return Util.Uniform(0.0, 1.0);
    }
    //endregion

    //region Private
    /** Cache the encoded state for use with the neural network */
    private void CacheEncodedState() throws Exception {
        _encodedState = new double[Settings.NETWORK_INPUT_COUNT];

        // TODO : Cache the encoded state
    }

    /** Randomize the game board */
    private void RandomizeTiles() throws Exception {
        // Place the player in a random spot
        // Place a few random enemies
        // Place a few rocks on the map
        // Place a few coins on the map
    }

    private boolean CheckGameCompletion() throws Exception {
        // TODO : Check if game is complete
        // Only allow 10 frames for now
        if (_curTime > 10L) 
            return true;

        return false;
    }
    //endregion
}