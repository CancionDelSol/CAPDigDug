/** Store some useful variables */
public class Settings {

    // DigDug inner game mechanics
    public static long MAX_GAME_LENGTH = 25L;
    public static int ENEMY_COUNT = 1;
    public static int COIN_COUNT = 1;
    public static int DIRT_COUNT = 5;
    public static int ROCK_COUNT = 1;
    public static int MAP_SIZE = 3;
    public static int MAX_AGENT_FOV = 3;
    public static int AGENT_FOV = 3;

    // Genetic Algorithm
    public static double MUTATION_RATE = .25;
    public static int POPULATION_SIZE = 100;
    public static int EPOCHS = 1000;
    public static int SET_SIZE = 25;
    public static double PERFORMANCE_THRESHOLD = Values.Epsilon;

    // Perceptron setup
    public static String NETWORK_FILE_NAME = "Network.txt";
    public static int NETWORK_INPUT_COUNT = 2;
    public static int NETWORK_OUTPUT_COUNT = 5; 
    public static int[] NETWORK_STRUCTURE = { NETWORK_INPUT_COUNT, 5, NETWORK_OUTPUT_COUNT};
    public static int DEFAULT_MAP_SIZE = 3;
    public static double OUTPUT_FIRE_THRESHOLD = .25;

}