/** Store some useful variables */
public class Settings {

    public static double MUTATION_RATE = .25;
    public static int NETWORK_INPUT_COUNT = 2;
    public static int NETWORK_OUTPUT_COUNT = 2; 
    public static int[] NETWORK_STRUCTURE = { NETWORK_INPUT_COUNT, 2, NETWORK_OUTPUT_COUNT};
    public static int POPULATION_SIZE = 100;
    public static int EPOCHS = 1000;
    public static int SET_SIZE = 100;
    public static int MAP_SIZE = 5;
    public static double PERFORMANCE_THRESHOLD = Values.Epsilon;
    public static int MAX_AGENT_FOV = 5;

}