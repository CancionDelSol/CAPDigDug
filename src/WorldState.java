/**
 * World state class. 
 *  The DigDug Gameboard
 * @author Roger Johnson
 *
 * @date 4/24/2016
 *
 * @info Course COP4601
 */


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
    private static final int FIRE_OUTPUT = 4;
    private String _message= "";
    private long _score = 0L;
    private boolean _isComplete = false;
    private int _x, _y;
    private int player_X, player_Y;
    private TileType[] _tiles;
    private List<TileType> _destroyed = new ArrayList<>();
    private int[] _hasMoved;
    private int _curTileCount = 0;

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

    /**
     * Return the tile piece at a given coordinate
     */
    private TileType GetTileType(int x, int y) throws Exception {
        int index = _getIndex(x, y, false);
        return index < 0 ? TileType.VOID : _tiles[index];
    }

    /**
     * Get the tile at the coordinate
     * @param x X coordinate
     * @param y Y coordinate
     */
    private TileType _getAtCoord(int x, int y) throws Exception {
        return _tiles[_getIndex(x, y, true)];
    }

    /**
     * Set the tile type at a given coordinate
     * @param x X coordinate
     * @param y Y coordinate
     * @param tile The tile to put in the array
     */
    private void _setAtCoord(int x, int y, TileType tile) throws Exception {
        _tiles[_getIndex(x, y, true)] = tile;

        if (tile == TileType.EMPTY)
            _curTileCount--;
        else
            _curTileCount++;

        if (tile == TileType.PLAYER) {
            player_X = x;
            player_Y = y;
        }

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
        ClearBoard();

        player_X = initializer.player_X;
        player_Y = initializer.player_Y;

        for (int x = 0; x < _x; x++) {
            for (int y = 0; y < _y; y++) {
                _setAtCoord(x, y, initializer._getAtCoord(x, y));
            }
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

        // Delta coordinates
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
                case 0: // Move left
                    dX = -1;
                    break;
                case 1: // Move up
                    dY = -1;
                    break;
                case 2: // Move right
                    dX = 1;
                    break;
                case 3: // Move down
                    dY = 1;
                    break;
            }
        }
        
        boolean isFiring = networkOutput[FIRE_OUTPUT] >= threshold;

        if (isFiring)
            _score--;
        
        if (_score < 0)
            _score = 1;

        MovePlayer(dX, dY, isFiring);

        // Keep track of what has already been moved
        _hasMoved = new int[_tiles.length];

        // Move enemies and rocks
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

        // Using harmonic values as inputs 
        //  will prevent the agent from being
        //  "locked" in place due to no changes
        //  in the environment within the Agents'
        //  FOV
        double a, b, c, d;
        a = Math.sin(_curTime * Values.PI/16.0);
        b = Math.sin(_curTime * Values.PI/8.0);
        c = Math.sin(_curTime * Values.PI/4.0);
        d = Math.sin(_curTime * Values.PI/2.0);
        _encodedState[curIndex] = a * a;
        _encodedState[curIndex + 1] = b * b;
        _encodedState[curIndex + 2] = c * c;
        _encodedState[curIndex + 3] = d * d;

    }

    /**
     * Randomize the game board 
     */
    private void RandomizeTiles() throws Exception {

        List<TileType> queue = new ArrayList<>();
        
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

        // Clear the board  
        ClearBoard();

        // Randomly pull from list and place into _tiles array
        for (TileType tile : queue) {
            PlaceInRandomPosition(tile);
        }

        // Put the player in the center
        int n = Settings.MAP_SIZE/2;
        _setAtCoord(n + Util.RandInt(-1, 1), n + Util.RandInt(-1, 1), TileType.PLAYER);
    }

    /**
     * Place the EMPTY tile in every position
     */
    private void ClearBoard() {
        for (int i = 0; i < _tiles.length; i++) {
            _tiles[i] = TileType.EMPTY;
        }
    }

    /**
     * Place the tile into a random EMPTY position in
     *  the _tiles array
     */
    private void PlaceInRandomPosition(TileType tileType) throws Exception {
        if (_curTileCount >= _tiles.length)
            Logger.Throw("Shouldn't be able to add tiles");

        int x = -1;
        int y = -1;

        TileType atCoord = TileType.VOID;
        while (atCoord != TileType.EMPTY) {
            x = Util.RandInt(0, Settings.MAP_SIZE);
            y = Util.RandInt(0, Settings.MAP_SIZE);

            atCoord = GetTileType(x, y);
        }

        _setAtCoord(x, y, tileType);
    }

    /**
     * Simply add the tile to the list
     * @param list The list to add the tile too
     * @param tile The tile to add to the list
     * @param count The number of times to add it to the list
     */
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

    /**
     * Move the player piece by x and y
     */
    private void MovePlayer(int dx, int dy, boolean isFiring) throws Exception {
        
        int xPrime = player_X + dx;
        int yPrime = player_Y + dy;
        int xInit = player_X;
        int yInit = player_Y;

        if (xPrime < 0 || xPrime >= _x)
            xPrime = xInit;
        
        if (yPrime < 0 || yPrime >= _y)
            yPrime = yInit;

        TileType atNewSpot = _getAtCoord(xPrime, yPrime);

        if (isFiring) {
            switch(atNewSpot) {
                case ENEMY:
                case COIN:
                case DIRT:
                case EMPTY:
                    _score += atNewSpot.getPointTotal();
                    _setAtCoord(xInit, yInit, TileType.EMPTY);
                    _setAtCoord(xPrime, yPrime, TileType.PLAYER);
                    break;
                default:
                    break;
            }
        } else {
            switch (atNewSpot) {
                case ENEMY:
                    EndGame("Player hit Enemy without firing at: " + Util.DisplayCoord(xPrime, yPrime));
                    _setAtCoord(xInit, yInit, TileType.EMPTY);
                    break;
                case COIN:
                case DIRT:
                case EMPTY:
                    _score += atNewSpot.getPointTotal();
                    _setAtCoord(xPrime, yPrime, TileType.PLAYER);
                    _setAtCoord(xInit, yInit, TileType.EMPTY);
                    break;
                
                default:
                    break;
            }
        }
        
    }

    /**
     * Move an enemy into a random spot
     *  Will not move if not empty or player
     */
    private void MoveEnemy(int x, int y) throws Exception {
        if (_hasMoved[_getIndex(x, y, true)] == 1)
            return;

        // Generate a random change
        //  in position. The rng does
        //  not include the last number
        //  so make the range -1 - 2
        int dX = Util.RandInt(-1, 2);
        int dY = Util.RandInt(-1, 2);

        // The new prime position
        int xPrime = x + dX;
        int yPrime = y + dY;

        // Get what is in that position already
        TileType atNewPos = GetTileType(xPrime, yPrime);

        switch (atNewPos) {
            case EMPTY:
                _setAtCoord(xPrime, yPrime, TileType.ENEMY);
                _setAtCoord(x, y, TileType.EMPTY);
                _hasMoved[_getIndex(xPrime, yPrime, true)] = 1;
                break;
            case PLAYER:
                _setAtCoord(xPrime,yPrime, TileType.ENEMY);
                _setAtCoord(x, y, TileType.EMPTY);
                EndGame("Enemy attacked Player at: " + Util.DisplayCoord(xPrime, yPrime));
                break;
        }
    }

    /**
     * Move the rock if empty space is beneath it
     */
    private void MoveRock(int x, int y) throws Exception {
        if (_hasMoved[_getIndex(x, y, true)] == 1)
            return;

        // The new prime position
        int xPrime = x;
        int yPrime = y + 1;

        // Get what is in that position already
        TileType atNewPos = GetTileType(xPrime, yPrime);

        switch (atNewPos) {
            case EMPTY:
                _setAtCoord(xPrime, yPrime, TileType.ROCK);
                _setAtCoord(x, y, TileType.EMPTY);
                _hasMoved[_getIndex(xPrime, yPrime, true)] = 1;
                break;
            case ENEMY:
                _setAtCoord(xPrime, yPrime, TileType.ROCK);
                _setAtCoord(x, y, TileType.EMPTY);
                _hasMoved[_getIndex(xPrime, yPrime, true)] = 1;
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