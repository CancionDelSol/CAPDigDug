import java.util.*;

public class Program {
    //region Members
    private static ProgramType _type;
    //endregion

    //region ProgramType
    /** Enum describing what steps to run */
    private enum ProgramType {
        ALL,      // Run UNITTEST|GENALG|LIVERUN
        UNITTEST, // Run Unit test suite
        GENALG,   // Run the genetic algorithm
        LIVERUN,  // Run a live performance of the network on disc
        PLAYER    // Allow the user to freeplay the game
    }
    //endregion

    public static void main(String[] args) {
        Logger.Gui("Start Up");

        try {
            // Read command line args
            ProcessCLArgs(args);

            // Use the _type member to control flow
            if (_type == ProgramType.ALL || _type == ProgramType.UNITTEST) {
                RunUnitTests();
            }
            if (_type == ProgramType.ALL || _type == ProgramType.GENALG) {
                RunGeneticAlgorithm();
            }
            if (_type == ProgramType.ALL || _type == ProgramType.LIVERUN) {
                RunLive();
            }
            if (_type == ProgramType.PLAYER) {
                RunPlayerSession();
            }   

        } catch (Exception exc) {
            Logger.Error("Exception in main: " + exc.getMessage());
        }
    }
    
    /** Prcesses the command line arguments */
    private static void ProcessCLArgs(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            switch (args[i].toUpperCase()) {
                case "-U":
                case "-UNITTEST":
                    Logger.Debug("Setting program type to UNITTEST");
                    _type = ProgramType.UNITTEST;
                    break;
                case "-P":
                case "-PLAYER":
                    Logger.Debug("Setting program type to PLAYER");
                    _type = ProgramType.PLAYER;
                    break;
                case "-G":
                case "-GENETICALG":
                    Logger.Debug("Setting program type to GENALG");
                    _type = ProgramType.GENALG;
                    break;
                case "-RATE":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for mutation rate");
                    
                    Settings.MUTATION_RATE = Double.parseDouble(args[i + 1]);
                    Logger.Debug("Setting mutation rate to: " + Settings.MUTATION_RATE);
                    break;
                case "-L":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for log level");

                    Logger.SetLevel(Logger.LogLevel.valueOf(args[i + 1].toUpperCase()));
                    Logger.Debug("Setting log level to: " + Logger.GetLevel());
                    break;
                case "-E":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for epochs");

                    Settings.EPOCHS = Integer.parseInt(args[i + 1]);
                    Logger.Debug("Setting epochs to: " + Settings.EPOCHS);
                    break;
                case "-POP":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for population size");

                    Settings.POPULATION_SIZE = Integer.parseInt(args[i + 1]);
                    Logger.Debug("Setting population size to: " + Settings.POPULATION_SIZE);
                    break;
                case "-N":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for population size");

                    String structure = args[i + 1];

                    structure = structure.replace("(", "");
                    structure = structure.replace(")", "");

                    String[] parts = structure.split(",");
                    Settings.NETWORK_STRUCTURE = new int[parts.length + 2];

                    // Set input count
                    Settings.NETWORK_STRUCTURE[0] = Settings.NETWORK_INPUT_COUNT;

                    // Set hidden layer
                    for (int p = 1; p < parts.length - 1; p++) {
                        Settings.NETWORK_STRUCTURE[i] = Integer.parseInt(parts[i]);
                    }

                    // Set output count
                    Settings.NETWORK_STRUCTURE[parts.length - 1] = Settings.NETWORK_OUTPUT_COUNT;

                    Logger.Debug("Setting network structure to: " + Util.DisplayArray(Settings.NETWORK_STRUCTURE));
                    break;
                default:
                    break;
            }
        }

        Logger.Debug("Finished reading command line arguments");
    }

    //region Private
    private static void RunUnitTests() throws Exception {
        Logger.Gui("Running Unit tests");
        try {
            UnitTests.RunAll();
        } catch (Exception exc) {
            Logger.Error("Exception during unit tests: " + exc.getMessage());
        }
    }
    
    private static void RunGeneticAlgorithm() throws Exception {
        Logger.Gui("Running Genetic Algorithm");
        try {
            // Create test training set
            List<IWorldState> testSet = new ArrayList<IWorldState>();
            for (int i = 0; i < Settings.SET_SIZE; i++) {

                // Generate a random world state
                int dim = Settings.MAP_SIZE;
                IWorldState newState = new WorldState(dim, dim);
                testSet.add(newState);
            }

            IAgent baseAgent = new DigAgent(Settings.NETWORK_STRUCTURE);

            GenAlg genAlg = new GenAlg(Settings.POPULATION_SIZE,
                                        baseAgent,
                                        Settings.MUTATION_RATE,
                                        WorldState.digDugEvaluation);

            double res = genAlg.Execute(Settings.EPOCHS, testSet);

            Logger.Gui(" Algorithm finished with final agent performance error: " + res);

        } catch (Exception exc) {
            Logger.Error("Exception during genetic algorithm: " + exc.getMessage());
        }
    }

    private static void RunPlayerSession() throws Exception {
        Logger.Gui("Running Player Session");
        try {
            Logger.Throw("TODO : Program.RunPlayerSession");
        } catch (Exception exc) {
            Logger.Error("Exception during player session: " + exc.getMessage());
        }
    }

    private static void RunLive() throws Exception {
        Logger.Throw("TODO : Program.RunLive");
    }
    //endregion
}