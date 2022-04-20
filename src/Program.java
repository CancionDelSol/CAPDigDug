/**
 * 
 * @author Roger Johnson
 *
 * @date 4/24/2016
 *
 * @info Course COP4601
 */
 
import java.util.*;
import java.nio.charset.*;

public class Program {
    //region Members
    private static ProgramType _type;
    private static IPerceptron _cachedNetwork = null;
    //endregion

    //region ProgramType
    /**
     * Enum describing what steps to run
     */
    private enum ProgramType {
        UNITTEST, // Run Unit test suite
        GENALG,   // Run the genetic algorithm
        LIVERUN,  // Run a live performance of the network on disc
        PLAYER    // Allow the user to freeplay the game
    }
    //endregion
    /**
     * 
     */
    public static void main(String[] args) {
        Logger.Gui("Start Up");

        try {

            // Read command line args
            ProcessCLArgs(args);

            // Setup global settings
            SetDefaults();

            // Use the _type member to control flow
            if (_type == ProgramType.UNITTEST) {
                RunUnitTests();
            }
            if (_type == ProgramType.GENALG) {
                RunGeneticAlgorithm();
            }
            if (_type == ProgramType.LIVERUN) {
                RunLive();
            }
            if (_type == ProgramType.PLAYER) {
                RunPlayerSession();
            }   

        } catch (Exception exc) {
            exc.printStackTrace();
            Logger.Error("Exception in main: " + exc.getMessage());
        }
    }
    
    /** 
     * Processes the command line arguments
     */
    private static void ProcessCLArgs(String[] args) throws Exception {

        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toUpperCase();

            switch (arg) {
                // Set Unit test
                case "-U":
                case "-UNITTEST":
                    Logger.Debug("Setting program type to UNITTEST");
                    _type = ProgramType.UNITTEST;
                    break;

                // Set Player Playthrough
                case "-P":
                case "-PLAYER":
                    Logger.Debug("Program type set to PLAYER");
                    _type = ProgramType.PLAYER;
                    break;

                // Set Genetic algorithm
                case "-G":
                case "-GENETICALG":
                    Logger.Info("Program type set to GENALG");
                    _type = ProgramType.GENALG;
                    break;

                case "-LIVE":
                case "-LIVERUN":
                    Logger.Info("Program type set to LIVERUN");
                    _type = ProgramType.LIVERUN;
                    break;
                // Read the mutation rate
                case "-RATE":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for mutation rate");
                    
                    String mArg = args[i + 1];
                    double mRate = Settings.MUTATION_RATE;
                    try {
                        mRate = Double.parseDouble(mArg);
                    } catch (Exception exc) {
                        Logger.Throw("Incorrect mutation rate argument: " + mArg);
                    }
                    Settings.MUTATION_RATE = mRate;
                    Logger.Info("Mutation rate set to: " + Settings.MUTATION_RATE);
                    break;

                // Read Logger level
                case "-L":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for log level");

                    String loggingArg = args[i + 1].toUpperCase();
                    Logger.LogLevel lvl = Logger.LogLevel.GUI;
                    try {
                        lvl = Logger.LogLevel.valueOf(loggingArg);
                    } catch (Exception exc) {
                        Logger.Throw("Incorrect logging level argument: " + loggingArg);
                    }
                    Logger.setLevel(lvl);
                    Logger.Info("Log level set to: " + Logger.getLevel());
                    break;

                // Read the Epochs
                case "-E":
                case "-EPOCHS":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for epochs");

                    String epochsArg = args[i + 1];
                    Logger.Debug("Epochs arg: " + epochsArg);
                    int epochsVal = Settings.EPOCHS;
                    try {
                        epochsVal = Integer.parseInt(epochsArg);
                    } catch (Exception exc) {
                        Logger.Throw("Incorrect epochs argument: " + epochsArg);
                    }

                    Settings.EPOCHS = epochsVal;
                    Logger.Info("Epochs set to: " + Settings.EPOCHS);
                    break;

                // Read the Population size
                case "-POP":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for population size");

                    String popArg = args[i + 1];
                    int popVal = Settings.POPULATION_SIZE;
                    try {
                        popVal = Integer.parseInt(popArg);
                    } catch (Exception exc) {
                        Logger.Throw("Incorrect population argument: " + popArg);
                    }

                    Settings.POPULATION_SIZE = popVal;

                    if (Settings.POPULATION_SIZE < 1)
                        Logger.Throw("Cannot have population size less than 1");

                    Logger.Info("Population size set to: " + Settings.POPULATION_SIZE);
                    break;

                // Read map size
                case "-M":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for map size");

                    String mapArg = args[i + 1];
                    int mapVal = Settings.MAP_SIZE;
                    try {
                        mapVal = Integer.parseInt(mapArg);
                    } catch (Exception exc) {
                        Logger.Throw("Incorrect map size argument: " + mapArg);
                    }
                    Settings.MAP_SIZE = mapVal;

                    if (Settings.MAP_SIZE < 2)
                        Logger.Throw("Cannot have map size less than 2");

                    Logger.Debug("Setting map size to: " + Settings.MAP_SIZE);
                    break;
                
                // Read the network structure
                //  The user cannot specificy input and output length
                //  so all the text refers to the hidden layers
                case "-N":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for network structure");

                    // Get the text entered by the user
                    String structure = args[i + 1];

                    // Remove brackets if included
                    structure = structure.replace("(", "");
                    structure = structure.replace(")", "");

                    // Split by commas
                    String[] parts = structure.split(":");
                    Settings.NETWORK_STRUCTURE = new int[parts.length + 2];

                    // Set input count
                    Settings.NETWORK_STRUCTURE[0] = Settings.NETWORK_INPUT_COUNT;

                    // Set hidden layer
                    for (int p = 0; p < parts.length; p++) {
                        Settings.NETWORK_STRUCTURE[p + 1] = Integer.parseInt(parts[p]);
                    }

                    // Set output count
                    int fullLength = Settings.NETWORK_STRUCTURE.length;
                    Settings.NETWORK_STRUCTURE[fullLength- 1] = Settings.NETWORK_OUTPUT_COUNT;

                    break;
                
                // Throw exception for unrecognized argument
                default:
                    break;
            }
        }

        Logger.Debug("Finished reading command line arguments");
    }

    //region Private
    /**
     * Run the unit tests
     */
    private static void RunUnitTests() throws Exception {
        Logger.Gui("Running Unit tests");
        try {
            UnitTests.RunAll();
        } catch (Exception exc) {
            Logger.Error("Exception during unit tests: " + exc.getMessage());
        }

        RunGeneticAlgorithm();
    }
    
    /**
     * Run the genetic algorithm
     */
    private static void RunGeneticAlgorithm() throws Exception {
        Logger.Gui("Running Genetic Algorithm");
        try {
            // Create test training set
            List<IWorldState> testSet = new ArrayList<IWorldState>();

            // Split all epochs into 10 groups
            double res = Values.VeryLarge;
            do {
                testSet.clear();

                // Setup new world states for training
                for (int curWorld = 0; curWorld < Settings.SET_SIZE; curWorld++) {

                    // Generate a random world state
                    int dim = Settings.MAP_SIZE;
                    IWorldState newState = new WorldState(dim, dim);
                    testSet.add(newState);

                }

                // Get base agent used for creating populations
                IAgent baseAgent = new DigAgent((IPerceptron)((IGenetic)getNetwork()).PerfectCopy());

                // Create genetic algorithm handler
                GenAlg genAlg = new GenAlg(Settings.POPULATION_SIZE,
                                        baseAgent,
                                        Settings.MUTATION_RATE,
                                        WorldState.digDugEvaluation);

                // Execute Genetic algorithm
                res = genAlg.Execute(Settings.EPOCHS, testSet);

                // Save the resulting network
                _cachedNetwork = ((DigAgent)genAlg.getBestAgent()).getNetwork();

                Logger.Gui("Saving Network to file");

                // Write network to disk    
                Util.SaveNetwork(_cachedNetwork);

            } while(res > Settings.PERFORMANCE_THRESHOLD);
            
            Logger.Gui(" Algorithm finished with final agent performance error: " + res);

            RunLive();
            
        } catch (Exception exc) {
            Logger.Error("Exception during genetic algorithm: " + exc.getMessage());
        }
    }

    /**
     * Run a player session for the user
     * TODO
     */
    private static void RunPlayerSession() throws Exception {
        Logger.Gui("Running Player Session");
        try {
            Logger.Throw("TODO : RunPlayerSession");
        } catch (Exception exc) {
            Logger.Error("Exception during player session: " + exc.getMessage());
        }
    }

    /**
     * Replay the cached network in a random world
     */
    private static void RunLive() throws Exception {
        Logger.Gui("Running AI playthrough");
        try {

            IPerceptron network = getNetwork();
            IAgent agent = new DigAgent(network);

            IWorldState map = new WorldState(Settings.MAP_SIZE, Settings.MAP_SIZE);

            while (!map.getIsComplete()) {
                ((WorldState)map).Display();
                IDeltaWorldState action = agent.GetAction(map);
                Logger.Gui("Score: " + ((WorldState)map).getScore() + " | Time: " + map.getTime());
                Logger.Debug("Action: " + Util.DisplayArray(action.getDeltaEncoding()));
                map.ApplyDelta(action);
                Thread.sleep(Settings.FRAME_INTERVAL);
            }
            Logger.Gui("Game complete: " + ((WorldState)map).getMessage());
            ((WorldState)map).Display();
        } catch (Exception exc) {
            Logger.Throw("Exception during AI runthrough: " + exc.getMessage());
        }
    }

    /** Set up default values 
         At this point, the user
         has already altered the settings
         using the cli*/
    private static void SetDefaults() throws Exception {

        // Special cases
        int mapSq = Settings.MAP_SIZE * Settings.MAP_SIZE;
        int defMapSq = Settings.DEFAULT_MAP_SIZE * Settings.DEFAULT_MAP_SIZE;
        switch (Settings.MAP_SIZE) {
            case (1) :
            case (2):
                Logger.Throw("Not a very interesting setup");
                break;
            default:
                Settings.AGENT_FOV = Math.min(Settings.MAX_AGENT_FOV, Settings.MAP_SIZE);

                // Scale up the defaults 1x1
                double ratio = mapSq/defMapSq;
                Settings.ROCK_COUNT *= ratio;
                Settings.DIRT_COUNT *= ratio;
                Settings.COIN_COUNT *= ratio;
                Settings.ENEMY_COUNT *= ratio;
                break;
        }

        // Set network input count for default structure
        //  This is equal to the number of possible tiles
        //   minus the void tiles which do not trigger
        //   neuron activity.
        int fovInputs = (TileType.values().length - 1) * Settings.AGENT_FOV * Settings.AGENT_FOV;
        Settings.NETWORK_INPUT_COUNT = fovInputs + 4;
        Settings.NETWORK_STRUCTURE[0] = Settings.NETWORK_INPUT_COUNT;
        Logger.Debug("Network structure set to : " + Util.DisplayArray(Settings.NETWORK_STRUCTURE));

    }
    //endregion

    //region Load/Save
    /**
     * Load the network from disk
     */
    private static void LoadCachedNetwork() throws Exception {
        _cachedNetwork = Util.LoadNetwork();

        if (_cachedNetwork == null) {
            _cachedNetwork = new Perceptron(Settings.NETWORK_STRUCTURE);
            Logger.Info("Random network generated");
        }
        else
            Logger.Info("Network loaded: " + Util.DisplayArray(_cachedNetwork.getStructure()));
    }
    
    /**
     * Network property
     * @return the cached IPerceptron. This will be
     *         loaded from disk. Upon failure to load,
     *         a random network will be created and cached
     */
    private static IPerceptron getNetwork() throws Exception {
        if (_cachedNetwork == null) {
            try {
                LoadCachedNetwork();
            } catch (Exception exc) {
                Logger.Warn(" No network to load: " + exc.getMessage());
                _cachedNetwork = new Perceptron(Settings.NETWORK_STRUCTURE);
            }
        }
        return _cachedNetwork; 
    }
    //endregion
}