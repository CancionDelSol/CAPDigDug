public class Program {
    public static void main(String[] args) {
        
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
            System.out.println("HERE");

                RunUnitTests();
            }
        }
    }

    private static void RunUnitTests() {
        try {
            UnitTests.RunAll();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }
}