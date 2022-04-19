public enum TileType {
    DIRT(10),
    PLAYER(0),
    ENEMY(100),
    COIN(1000),
    ROCK(0),
    EMPTY(0),
    VOID(0);

    private int _ptTotal;
    TileType(int points) {
        _ptTotal = points;
    }
    public int getpointTotal() {
        return _ptTotal;
    }
}