public class Program {
    public static void main(String[] args) {
        Logger.Debug("Start of program");

        // Run test suite
        if (args.length > 0) {
            ProcessCLArgs(args);
        } else {

        }
    }
    
    /**
     * Prcesses the command line arguments
     */
    private static void ProcessCLArgs(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[0].equals("UNITTEST")) {
                Logger.Gui("Running Unit tests");
                RunUnitTests();
            }
        }
    }


    //region Private
    private static void RunUnitTests() {
        try {
            UnitTests.RunAll();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    private static void StartGeneticAlgorithm() {
        
    }
    //endregion
}