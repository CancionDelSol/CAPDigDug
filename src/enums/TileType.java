public enum TileType {
    DIRT(100, "#"),
    PLAYER(0, "^"),
    ENEMY(75, "&"),
    COIN(250, "*"),
    ROCK(0, "@"),
    EMPTY(0, " "),
    VOID(0, " ");

    private int _ptTotal;
    private String _disp;
    TileType(int points, String disp) {
        _ptTotal = points;
        _disp = disp;
    }

    public int getPointTotal() {
        return _ptTotal;
    }

    public String getDisplay() {
        return _disp;
    }
}