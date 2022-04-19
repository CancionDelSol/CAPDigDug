import interfaces.*;
import java.util.*;

public class UnitTests {
    //region Run all
    public static void RunAll() throws Exception{
        // MatrixMultiplicationTestOne();
        // MatrixMultiplicationTestTwo();
        // MatrixAdditionSubtractionTest();
        // PerceptronXmlSerializationTest();
        // PerceptronGeneticTest();
        // PerceptronViabilityTest();
        WorldStateTest();
    }
    //endregion

    //region Matrix
    public static void MatrixMultiplicationTestOne() throws Exception {
        String testName = "MatrixMultiplicationOne";

        Matrix m1 = new Matrix(3, 3);
        Matrix m2 = new Matrix(3, 3);
        Matrix expected = new Matrix(3, 3);

        // Set values in Matrix one
        m1.put(0, 0, 4.0);
        m1.put(1, 0, 7.0);
        m1.put(2, 0, 9.0);
        m1.put(0, 1, 3.0);
        m1.put(1, 1, 5.0);
        m1.put(2, 1, 2.0);
        m1.put(0, 2, 6.0);
        m1.put(1, 2, 1.0);
        m1.put(2, 2, 8.0);

        // Set values in Matrix two
        m2.put(0, 0, 2.0);
        m2.put(1, 0, 8.0);
        m2.put(2, 0, 5.0);
        m2.put(0, 1, 9.0);
        m2.put(1, 1, 3.0);
        m2.put(2, 1, 6.0);
        m2.put(0, 2, 4.0);
        m2.put(1, 2, 1.0);
        m2.put(2, 2, 7.0);

        // Create the expected
        expected.put(0, 0, 107.0);
        expected.put(1, 0, 62.0);
        expected.put(2, 0, 125.0);
        expected.put(0, 1, 59.0);
        expected.put(1, 1, 41.0);
        expected.put(2, 1, 59.0);
        expected.put(0, 2, 53.0);
        expected.put(1, 2, 59.0);
        expected.put(2, 2, 92.0);

        // Multiply the two matricies
        Matrix actual = Matrix.Multiply(m1, m2);

        // Test res dimensions
        if (actual.Nx != 3 || actual.Ny != 3) {
            LogResult(testName, false, "Incorrect dimension " + String.valueOf(actual.Nx) + " " + String.valueOf(actual.Ny));
            return;
        }

        // Test each value
        for (int x = 0; x < actual.Nx; x++) {
            for (int y = 0; y < actual.Ny; y++) {
                if (expected.get(x, y) != actual.get(x, y)) {
                    LogResult(testName, false, "Values not equal at index (" + x + ", " + y + ") act: " + actual.get(x, y) + " exp: " + expected.get(x, y));
                    return;
                }
            }
        }

        LogResult(testName, true, "");

    }

    public static void MatrixMultiplicationTestTwo() throws Exception {
        String testName = "MatrixMultiplicationTwo";

        Matrix m1 = new Matrix(2, 4);
        Matrix m2 = new Matrix(2, 2);
        Matrix expected = new Matrix(2, 4);

        m1.put(0, 0, 1.0);
        m1.put(1, 1, 2.0);
        m1.put(0, 2, 3.0);
        m1.put(1, 3, 4.0);

        m2.put(0, 0, 1.0);
        m2.put(1, 1, 1.0);

        expected.put(0, 0, 1.0);
        expected.put(1, 1, 2.0);
        expected.put(0, 2, 3.0);
        expected.put(1, 3, 4.0);

        Matrix actual = null;
        try {
            actual = Matrix.Multiply(m1, m2);
        } catch (Exception exc) {
            LogResult(testName, false, exc.getMessage());
            return;
        }
        

        if (actual.Nx != 2 || actual.Ny != 4) {
            LogResult(testName, false, "Incorrect dimensions: (" + actual.Nx + ", " + actual.Ny + ")");
            return;
        }

        // Test each value
        for (int x = 0; x < actual.Nx; x++) {
            for (int y = 0; y < actual.Ny; y++) {
                if (expected.get(x, y) != actual.get(x, y)) {
                    LogResult(testName, false, "Values not equal at index (" + x + ", " + y + ") act: " + actual.get(x, y) + " exp: " + expected.get(x, y));
                    return;
                }
            }
        }

        LogResult(testName, true, "");
    }

    public static void MatrixAdditionSubtractionTest() throws Exception {
        String testName = "MatrixAdditionSubtractionTest";
        LogResult(testName, false, "Not Implemented yet");
    }
    //endregion

    //region Perceptron
    public static void PerceptronXmlSerializationTest() throws Exception {
        String testName = "PerceptronXmlSerializationTest";

        int[] testStructure = new int[] { 2, 3, 4 };
        Perceptron perceptron = new Perceptron(testStructure);

        String xmlOut = perceptron.WriteXml();

        Perceptron fromXml = new Perceptron(xmlOut);

        if (!fromXml.equals(perceptron)) {
            LogResult(testName, false, "Perceptrons not equal");
            return;
        }

        String newXml = fromXml.WriteXml();
        if (!xmlOut.equals(newXml))
        {
            LogResult(testName, false, "Perceptron xml not equal");
            Logger.Verbose(xmlOut);
            Logger.Verbose(newXml);
            return;
        }
        

        LogResult(testName, true, "");
    }

    public static void PerceptronGeneticTest() throws Exception {
        String testName = "PerceptronGeneticTest";
        int[] testStructure = new int[] { 2, 3, 4 };
        IGenetic p = new Perceptron(testStructure);

        IGenetic newP = p.PerfectCopy();

        if (!newP.equals(p)) {
            LogResult(testName, false, "Not equal");
            return;
        }

        newP = p.MutatedCopy(.01);

        if (newP.equals(p)) {
            LogResult(testName, false, "Should not be equal");
            return;
        }

        LogResult(testName, true, "");
    }

    private static void PerceptronViabilityTest() throws Exception {
        String testName = "PerceptronViabilityTest";
        int[] testStructure = new int[] { 2, 2, 2 };
        double mutationRate = 1.0;
        int epochs = 1000;
        int popSize = 25;
        int trainingSetSize = 100;
        double mapSize = 10.0;

        // Create test training set
        List<IWorldState> testSet = new ArrayList<IWorldState>();
        for (int i = 0; i < trainingSetSize; i++) {
            IWorldState newState = new LinearWorldState(Util.Uniform(-mapSize, mapSize),
                                                        Util.Uniform(-mapSize, mapSize));
            testSet.add(newState);
        }

        IAgent baseAgent = new LinearAgent(testStructure);

        GenAlg genAlg = new GenAlg(popSize,
                                    baseAgent,
                                    mutationRate,
                                    linearEvaluation);

        double res = genAlg.Execute(epochs, testSet);

        // It should be fairly easy to get a very low
        //  error for a linear classifier 
        // In this case we are training a perceptron
        //  to classify a coordinate as above or below
        //  the line y = x
        if (res <= .01)
            LogResult(testName, true, "Error: " + res);
        else
            LogResult(testName, false, "Error: " + res);
    }
    
    //region Used for the Perceptron Viability Test
    /** Will make classifications about a two-dimensional world state */
    private static class LinearAgent implements IAgent {
        //region Members
        IPerceptron _perceptron;
        //endregion

        //region Constructor
        /** Create an initial, random agent */
        public LinearAgent(int[] structure) throws Exception {
            _perceptron = new Perceptron(structure);
        }
        private LinearAgent(IPerceptron perceptron) throws Exception {
            _perceptron = perceptron;
        }
        //endregion

        //region IAgent
        public IDeltaWorldState GetAction(IWorldState currentState) throws Exception {
            return new LinearDeltaState(_perceptron.FeedForward(currentState.getEncoding()));
        }
        //endregion

        //region IGenetic
        public IGenetic PerfectCopy() throws Exception {
            return new LinearAgent((IPerceptron)((IGenetic)_perceptron).PerfectCopy());
        }
        public IGenetic MutatedCopy(double rate) throws Exception {
            return new LinearAgent((IPerceptron)((IGenetic)_perceptron).MutatedCopy(rate));
        }
        //endregion
    }

    /** Holds the testCoordinate value */
    private static class LinearWorldState implements IWorldState {
        //region Members
        private double[] _coords;
        //endregion

        //region Constructor
        public LinearWorldState(double x, double y) throws Exception {
            _coords = new double[2];
            _coords[0] = x;
            _coords[1] = y;
        }
        //endregion

        //region Properties
        public int getLength() throws Exception { return 2; }
        public double[] getEncoding() throws Exception { return _coords; }
        public long getTime() throws Exception { return 0L; }
        public boolean getIsComplete() throws Exception { return true; }
        //endregion

        //region Methods
        /** The delta contains the network output
          *  Use it to evaluate the "end state" and
          *  set the values
          */
        public void ApplyDelta(IDeltaWorldState dState) throws Exception {
            // The actual classification of the 
            //  test coordinates
            double[] actual = new double[2];
            actual[0] = _coords[1] > _coords[0] ? 1.0 : 0.0;
            actual[1] = _coords[0] > _coords[1] ? 1.0 : 0.0;

            // Place the individual errors in
            //  each network output into the 
            //  _coords array
            _coords[0] = dState.getDeltaEncoding()[0] - actual[0];
            _coords[1] = dState.getDeltaEncoding()[1] - actual[1];
        }
        public IWorldState clone() {
            IWorldState rVal = null;
            try {
                rVal = new LinearWorldState(_coords[0], _coords[1]);
            } catch (Exception exc) {
                Logger.Error("Exception during clone: " + exc.getMessage());
            }

            return rVal;
        }
        //endregion
    }

    /** This class holds the network output */
    private static class LinearDeltaState implements IDeltaWorldState {
        //region Members
        private double[] _delta;
        //endregion

        //region Constructor
        public LinearDeltaState(double[] delta) throws Exception {
            _delta = delta;
        }
        //endregion

        //region IDeltaWorldState
        public double[] getDeltaEncoding() throws Exception {
            return _delta;
        }
        public int getLength() throws Exception { return 2; }
        //endregion
    }

    /** Measures the magnitude of the error array in the world state */
    private static GenAlg.IErrorFunction linearEvaluation = (worldState) -> { 
        double rVal = 0.0;
        try {
            double[] outputs = worldState.getEncoding();
            rVal = Util.Mag(outputs);
        } catch (Exception exc) {
            Logger.Error("Exception in error function: " + exc.getMessage());
        }
        return rVal;
    };
    //endregion

    //region WorldState
    private static void WorldStateTest() throws Exception {
        WorldState testRandomState = new WorldState(Settings.MAP_SIZE, Settings.MAP_SIZE);

        testRandomState.Display();
    }
    //endregion

    //region Private
    private static void LogResult(String testName, boolean testPassed, String message) {
        if (testPassed) {
            System.out.println("TestResult: " + testName + " PASSED : " + message);
        } else {
            System.out.println("TestResult: " + testName + " FAILED : " + message);
        }
    }
    //endregion
}