import java.util.*;

public class WorldState implements IWorldState {
    //region PerceptronSupport
    private int MOVE_LEFT = 0;
    private int MOVE_RIGHT = 1;
    private int MOVE_UP = 2;
    private int MOVE_DOWN = 3;
    private int SHOOT = 4;
    //endregion

    //region Gameboard
    private String _message= "";
    private long _score = 0L;
    private boolean _isComplete = false;
    private int _x, _y;
    private int player_X, player_Y;
    private TileType[] _tiles;
    private List<TileType> _destroyed = new ArrayList<>();

    /** Return the player's score */
    public long getScore() {
        return _score;
    }

    private void EndGame(String message) {
        _message = message;
        _isComplete = true;
    }

    public String getMessage() {
        return _message;
    }

    /** Return the tile piece at a given coordinate */
    private TileType GetTileType(int x, int y) throws Exception {
        int index = _getIndex(x, y, false);
        return index < 0 ? TileType.VOID : _tiles[index];
    }

    private TileType _getAtCoord(int x, int y) throws Exception {
        return _tiles[_getIndex(x, y, true)];
    }

    /** Set the tile type at a given coordinate */
    private void _setAtCoord(int x, int y, TileType tile) throws Exception {
        _tiles[_getIndex(x, y, true)] = tile;
    }

    /** Return the index in the _values array */
    private int _getIndex(int x, int y, boolean throwExc) throws Exception {
        
        int index = (y * _x) + x;
        if (x < 0 || x >= _x || y < 0 || y >= _y)
            if (throwExc)
                Logger.Throw("Out of bounds [" + index + "]: (" + x + ", " + y + ") ");
            else
                return -1;
        
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
    /**
     * Length property
     * @return The encoded state length
     */
    public int getLength() throws Exception {
        return getEncoding().length;
    }

    /**
     * Encoding property
     * @return The encoding (i.e input
     *          array for the network)
     */
    public double[] getEncoding() throws Exception {
        if (getIsComplete())
            Logger.Throw("Attempt to get encoding for completed game");

        if (_encodedState == null)
            CacheEncodedState();

        return Arrays.copyOf(_encodedState, _encodedState.length);
    }

    /**
     * Time property
     * @return Current world state time
     */
    public long getTime() throws Exception {
        return _curTime;
    }

    /**
     * IsComplete property
     * @return True if world is complete
     */
    public boolean getIsComplete() throws Exception {
        CheckGameCompletion();

        return _isComplete;
    }

    /** 
     * Apply a delta to this world state
     *  using the neural network outputs
     * @param dState The delta to apply
     */
    public void ApplyDelta(IDeltaWorldState dState) throws Exception {
        _encodedState = null;

        // Increment the time
        _curTime++;

        // Get network output
        double[] networkOutput = dState.getDeltaEncoding();

        // Read player movement output
        //  0 : move left
        //  1 : move up
        //  2 : move right
        //  3 : move down
        double threshold = Settings.OUTPUT_FIRE_THRESHOLD;

        // Move left or right
        int dX = 0, dY = 0;
        double highestVal = Values.VerySmall;
        int highestIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (networkOutput[i] > highestVal) {
                highestIndex = i;
                highestVal = networkOutput[i];
            }
        }

        if (highestVal > threshold) {
            switch(highestIndex) {
                case 0:
                    dX = -1;
                    break;
                case 1:
                    dY = -1;
                    break;
                case 2:
                    dX = 1;
                    break;
                case 3:
                    dY = 1;
                    break;
            }
        }
        
        MovePlayer(dX, dY, networkOutput[4] > threshold);

        // TODO : Enemy Movement
        for (int x = 0; x < Settings.MAP_SIZE; x++) {
            for (int y = 0; y < Settings.MAP_SIZE; y++) {
                TileType curType = GetTileType(x, y);

                switch (curType) {
                    case ENEMY:
                        MoveEnemy(x, y);
                        break;
                    case ROCK:
                        MoveRock(x, y);
                        break;
                }
            }
        }

        // TODO : ROCKS FALL
        
    }

    /**
     * Return a clone of this state
     * @return A copy of this instance
     */
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
    /**
     * Print grid to screen
     */
    public void Display() {
        String colSep = "| ";

        // Iterate through the items
        String buffer = "";
        for (int i = 0; i < _tiles.length; i++) {
            if ((i+1)%_x == 0)
                Logger.Gui(getRowSep());

            buffer = buffer + colSep + _tiles[i].getDisplay() + " ";

            if ((i+1)%_x == 0) {
                Logger.Gui(buffer + "|");
                buffer = "";
            }
        }
        Logger.Gui(getRowSep() + "\n");
    }

    /**
     * The row seperator
     */
    private String _rowSep;
    private String getRowSep() {
        if (_rowSep == null) {
            String block = "+---";
            StringBuilder bldr = new StringBuilder();
            for (int i = 0; i < _x; i++) {
                bldr.append(block);
            }
            bldr.append("+");
            _rowSep = bldr.toString();
        }

        return _rowSep;
    }
    //endregion

    //region IErrorFunction
    /**
     * The lamda for agent error evaluation
     */
    public static GenAlg.IErrorFunction digDugEvaluation = (worldState) -> { 
        double rVal = 0.0;
        try {
            if (!(worldState instanceof WorldState))
                Logger.Throw("Incorrect worldState object type");

            return ((WorldState)worldState).GetDigAgentError();

        } catch (Exception exc) {
            Logger.Error("Exception in error function: " + exc.getMessage());
        }
        return rVal;
    };

    /**
     * Get the dig agent error
     * @return Dig agent error
     */
    private double GetDigAgentError() {
        // Return the current error the ratio
        //  of the current score over the 
        //  total possible score
        //  Start at one to prevent divide by zero
        double totalPossibleScore = 1.0;

        // The total possile score
        //  includes the current score
        totalPossibleScore += _score;

        // Loop through all tiles and
        //  add it's value to the total
        //  possible score
        for (int i = 0; i < _tiles.length; i++) {
            TileType tile = _tiles[i];
            if (tile == TileType.EMPTY)
                continue;

            totalPossibleScore += tile.getPointTotal();
        }
        return (totalPossibleScore - _score)/totalPossibleScore;
    }
    //endregion

    //region Private
    /** Cache the encoded state for use with the neural network */
    private void CacheEncodedState() throws Exception {
        _encodedState = new double[Settings.NETWORK_INPUT_COUNT];

        // Create a circular view of the current map state
        //  by iterating over a square around the player,
        //  and checking to see if it is
        int inArray = 0;
        int perTile = TileType.values().length - 1;
        for (int x = 0; x < Settings.AGENT_FOV; x++) {
            for (int y = 0; y < Settings.AGENT_FOV; y++) {
                int povX = (x - Settings.AGENT_FOV/2) + player_X;
                int povY = (y - Settings.AGENT_FOV/2) + player_Y;
                TileType atIndex = GetTileType(povX, povY);

                if (atIndex != TileType.VOID) {
                    int netIndex = (inArray * perTile) + atIndex.ordinal();
                    _encodedState[netIndex] = 1.0;
                } else {
                }
                inArray++;
            }
        }
        int curIndex = inArray * perTile;
        _encodedState[curIndex] = Math.sin(_curTime * Values.PI/16.0);
        _encodedState[curIndex + 1] = Math.sin(_curTime * Values.PI/8.0);
        _encodedState[curIndex + 2] = Math.sin(_curTime * Values.PI/4.0);
        _encodedState[curIndex + 3] = Math.sin(_curTime * Values.PI/2.0);

    }

    /** Randomize the game board */
    private void RandomizeTiles() throws Exception {
        List<TileType> queue = new ArrayList<>();

        // Add player tile
        queue.add(TileType.PLAYER);
        
        // Add Enemy tiles
        AddTileToList(queue, TileType.ENEMY, Settings.ENEMY_COUNT);

        // Add Coin tiles
        AddTileToList(queue, TileType.COIN, Settings.COIN_COUNT);

        // Add Rock tiles
        AddTileToList(queue, TileType.ROCK, Settings.ROCK_COUNT);

        // Add Dirt tiles
        AddTileToList(queue, TileType.DIRT, Settings.DIRT_COUNT);

        // Fill the rest with Empty
        AddTileToList(queue, TileType.EMPTY, (Settings.MAP_SIZE * Settings.MAP_SIZE) - queue.size());   

        // Randomly pull from list and place into _tiles array
        for (int x = 0; x < Settings.MAP_SIZE; x++) {
            for (int y = 0; y < Settings.MAP_SIZE; y++) {
                int randIndex = Util.RandInt(0, queue.size());
                TileType pulled = queue.remove(randIndex);

                if (pulled == TileType.PLAYER) {
                    player_X = x;
                    player_Y = y;
                }
                _setAtCoord(x, y, pulled);
            }
        }
    }

    private void AddTileToList(List<TileType> list, TileType tile, int count) {
        for (int i = 0; i < count; i++) {
            list.add(tile);
        }
    }

    private void CheckGameCompletion() throws Exception {
        // Check for over time limit
        if (_curTime > Settings.MAX_GAME_LENGTH) 
            EndGame("Time limit reached");

    }

    private void MovePlayer(int x, int y, boolean isFiring) throws Exception {
        int xPrime = player_X + x;
        int yPrime = player_Y + y;


        if (xPrime < 0 || xPrime >= Settings.MAP_SIZE)
            xPrime = player_X;
        
        if (yPrime < 0 || yPrime >= Settings.MAP_SIZE)
            yPrime = player_Y;

        TileType atNewSpot = _getAtCoord(xPrime, yPrime);

        switch (atNewSpot) {
            // Player dies, remove him from board
            case ENEMY:
                if (!isFiring) {
                    EndGame("Player hit Enemy without firing at: " + Util.DisplayCoord(xPrime, yPrime));
                    _setAtCoord(player_X, player_Y, TileType.EMPTY);
                    player_X = -1;
                    player_Y = -1;
                    break;
                } else {
                    _score += atNewSpot.getPointTotal();
                    _setAtCoord(xPrime, yPrime, TileType.PLAYER);
                    _setAtCoord(player_X, player_Y, TileType.EMPTY);
                    player_X = xPrime;
                    player_Y = yPrime;
                    break;
                }
            case COIN:
            case DIRT:
                if (!isFiring)
                    _score += atNewSpot.getPointTotal();
            case EMPTY:
                if (!isFiring) {
                    _setAtCoord(xPrime, yPrime, TileType.PLAYER);
                    _setAtCoord(player_X, player_Y, TileType.EMPTY);
                    player_X = xPrime;
                    player_Y = yPrime;
                    break;
                }
                

            default:
                break;
        }
    }

    /**
     * Move an enemy into a random spot
     *  Will not move if not empty or player
     */
    private void MoveEnemy(int x, int y) throws Exception {

        // Generate a random change
        //  in position
        int dX = Util.RandInt(-1, 1);
        int dY = Util.RandInt(-1, 1);

        // The new prime position
        int xPrime = x + dX;
        int yPrime = y + dY;

        // Get what is in that position already
        TileType atNewPos = GetTileType(xPrime, yPrime);

        switch (atNewPos) {
            case EMPTY:
                _setAtCoord(xPrime, yPrime, TileType.ENEMY);
                _setAtCoord(x, y, TileType.EMPTY);
                break;
            case PLAYER:
                _setAtCoord(xPrime,yPrime, TileType.ENEMY);
                _setAtCoord(x, y, TileType.EMPTY);
                EndGame("Enemy attacked Player at: " + Util.DisplayCoord(xPrime, yPrime));
                break;
        }
    }

    private void MoveRock(int x, int y) throws Exception {

        // The new prime position
        int xPrime = x;
        int yPrime = y + 1;

        // Get what is in that position already
        TileType atNewPos = GetTileType(xPrime, yPrime);

        switch (atNewPos) {
            case EMPTY:
                _setAtCoord(xPrime, yPrime, TileType.ROCK);
                _setAtCoord(x, y, TileType.EMPTY);
                break;
            case PLAYER:
                _setAtCoord(xPrime,yPrime, TileType.ROCK);
                _setAtCoord(x, y, TileType.EMPTY);
                EndGame("Rock fell on Player at: " + Util.DisplayCoord(xPrime, yPrime));
                break;
        }
    }
    //endregion
}