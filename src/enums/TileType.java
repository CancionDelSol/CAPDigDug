public enum TileType {
    DIRT(10, "#"),
    PLAYER(0, "^"),
    ENEMY(100, "&"),
    COIN(1000, "*"),
    ROCK(0, "@"),
    EMPTY(0, " "),
    VOID(0, " ");

    private int _ptTotal;
    private String _disp;
    TileType(int points, String disp) {
        _ptTotal = points;
        _disp = disp;
    }

    public int getpointTotal() {
        return _ptTotal;
    }

    public String getDisplay() {
        return _disp;
    }
}