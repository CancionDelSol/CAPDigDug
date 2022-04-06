public class UnitTests {
    public static void RunAll() throws Exception{
        MatrixMultiplicationTestOne();
        MatrixMultiplicationTestTwo();
        MatrixAdditionSubtractionTest();
    }
    public static void MatrixMultiplicationTestOne() throws Exception {
        String testName = "MatrixMultiplicationOne";

        Matrix m1 = new Matrix(3, 3);
        Matrix m2 = new Matrix(3, 3);
        Matrix expected = new Matrix(3, 3);

        // Set values in Matrix one
        m1.put(0, 0, 1.0);
        m1.put(1, 0, 2.0);
        m1.put(2, 0, 3.0);
        m1.put(0, 1, 4.0);
        m1.put(1, 1, 5.0);
        m1.put(2, 1, 6.0);
        m1.put(0, 2, 7.0);
        m1.put(1, 2, 8.0);
        m1.put(2, 2, 9.0);

        // Set values in Matrix two
        m2.put(0, 0, 1.0);
        m2.put(1, 0, 1.0);
        m2.put(2, 0, 1.0);
        m2.put(0, 1, 1.0);
        m2.put(1, 1, 1.0);
        m2.put(2, 1, 1.0);
        m2.put(0, 2, 1.0);
        m2.put(1, 2, 1.0);
        m2.put(2, 2, 1.0);

        // Create the expected TODO : FINISH
        expected.put(0, 0, 1.0);
        expected.put(1, 0, 1.0);
        expected.put(2, 0, 1.0);
        expected.put(0, 1, 1.0);
        expected.put(1, 1, 1.0);
        expected.put(2, 1, 1.0);
        expected.put(0, 2, 1.0);
        expected.put(1, 2, 1.0);
        expected.put(2, 2, 1.0);

        // Multiply the two matricies
        Matrix actual = Matrix.Multiply(m1, m2);

        // Test res dimensions
        if (actual.Nx != 3) {
            LogResult(testName, false, "Incorrect dimension " + String.valueOf(actual.Nx));
        }


    }

    public static void MatrixMultiplicationTestTwo() throws Exception {
        String testName = "MatrixMultiplicationTwo";

        Matrix m1 = new Matrix(2, 4);
        Matrix m2 = new Matrix(2, 2);
        Matrix exp = new Matrix(2, 4);

        m1.put(0, 0, 1.0);
        m1.put(1, 1, 2.0);
        m1.put(0, 2, 3.0);
        m1.put(1, 3, 4.0);

        m2.put(0, 0, 1.0);
        m2.put(1, 1, 1.0);

        exp.put(0, 0, 1.0);
        // TODO : FINISH

    }

    public static void MatrixAdditionSubtractionTest() throws Exception {

    }

    //region Private
    private static void LogResult(String testName, boolean testPassed, String message) {
        if (testPassed) {
            System.out.println(testName + " FAILED : " + message);
        } else {
            System.out.println(testName + " FAILED : " + message);
        }
    }
    //endregion
}