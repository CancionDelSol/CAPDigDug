public class UnitTests {
    //region Run all
    public static void RunAll() throws Exception{
        MatrixMultiplicationTestOne();
        MatrixMultiplicationTestTwo();
        MatrixAdditionSubtractionTest();
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
    }

    public static void MatrixAdditionSubtractionTest() throws Exception {

    }
    //endregion

    //region Perceptron

    //endregion

    //region Private
    private static void LogResult(String testName, boolean testPassed, String message) {
        if (testPassed) {
            Logger.Debug(testName + " PASSED : " + message);
        } else {
            Logger.Debug(testName + " FAILED : " + message);
        }
    }
    //endregion
}