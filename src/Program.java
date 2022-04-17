import java.util.*;

public class Program {
    //region Settings
    private static ProgramType _type = ProgramType.ALL;
    private static int _epochs = 1000;
    private static int[] _networkStructure = new int[] { 2, 2, 2 };
    private static double _mutationRate = 1.0;
    private static int _popSize = 100;
    private static int _setSize = 100;
    private static int _mapSize = 5;
    private static double _performanceThreshold = Values.Epsilon;
    //endregion

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
                case "-rate":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for -rate");
                    
                    _mutationRate = Double.parseDouble(args[i + 1]);
                    Logger.Debug("Setting mutation rate to: " + _mutationRate);
                    break;
                case "-l":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for -rate");

                    Logger.SetLevel(Logger.LogLevel.valueOf(args[i + 1].toUpperCase()));
                    Logger.Debug("Setting mutation rate to: " + Logger.GetLevel());
                    break;
                case "-e":
                    if (i + 1 >= args.length)
                        Logger.Throw("Incomplete arguments for -rate");

                    _epochs = Integer.parseInt(args[i + 1]);
                    Logger.Debug("Setting epochs to: " + _epochs);
                default:
                    break;
            }
        }
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
            for (int i = 0; i < _setSize; i++) {
                // Generate a random world state
                IWorldState newState = new WorldState();
                testSet.add(newState);
            }

            IAgent baseAgent = new DigAgent(_networkStructure);

            GenAlg genAlg = new GenAlg(_popSize,
                                        baseAgent,
                                        _mutationRate,
                                        WorldState.digDugEvaluation);

            double res = genAlg.Execute(_epochs, testSet);

            Logger.Gui(" Algorithm finished with final agent performance error: " + res);

        } catch (Exception exc) {
            Logger.Error("Exception during genetic algorithm: " + exc.getMessage());
        }
    }

    private static void RunPlayerSession() throws Exception {
        Logger.Gui("Running Player Session");
        try {

        } catch (Exception exc) {
            Logger.Error("Exception during player session: " + exc.getMessage());
        }
    }

    private static void RunLive() throws Exception {
        Logger.Throw("TODO : RunLive");
    }
    //endregion
}