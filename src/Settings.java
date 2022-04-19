/** Store some useful variables */
public class Settings {

    public static long MAX_GAME_LENGTH = 100L;
    public static double MUTATION_RATE = .25;
    public static int ENEMY_COUNT = 2;
    public static int COIN_COUNT = 2;
    public static int DIRT_COUNT = 3;
    public static int ROCK_COUNT = 1;
    public static int NETWORK_INPUT_COUNT = 2;
    public static int NETWORK_OUTPUT_COUNT = 2; 
    public static int[] NETWORK_STRUCTURE = { NETWORK_INPUT_COUNT, 2, NETWORK_OUTPUT_COUNT};
    public static int POPULATION_SIZE = 100;
    public static int EPOCHS = 1000;
    public static int SET_SIZE = 100;
    public static int MAP_SIZE = 3;
    public static double PERFORMANCE_THRESHOLD = Values.Epsilon;
    public static int MAX_AGENT_FOV = 5;
    public static int AGENT_FOV = 5;
    public static int DEFAULT_MAP_SIZE = 3;
    public static double OUTPUT_FIRE_THRESHOLD = .5;

}