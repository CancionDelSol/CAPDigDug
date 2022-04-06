public class UnitTests {
    public static void RunAll() {
        MatrixMultiplicationTestOne();
        MatrixMultiplicationTestTwo();
        MatrixAdditionSubtractionTest();
    }
    public static void MatrixMultiplicationTestOne() {
        String testName = "MatrixMultiplicationOne";

        Matrix m1 = new Matrix(3, 3);
        Matrix m2 = new Matrix(3, 3);
        Matrix expected = new Matrix(3, 3);

        // Set values in Matrix one
        m1.setValue(0, 0, 1.0)
        m1.setValue(1, 0, 2.0)
        m1.setValue(2, 0, 3.0)
        m1.setValue(0, 1, 4.0)
        m1.setValue(1, 1, 5.0)
        m1.setValue(2, 1, 6.0)
        m1.setValue(0, 2, 7.0)
        m1.setValue(1, 2, 8.0)
        m1.setValue(2, 2, 9.0)

        // Set values in Matrix two
        m2.setValue(0, 0, 1.0)
        m2.setValue(1, 0, 1.0)
        m2.setValue(2, 0, 1.0)
        m2.setValue(0, 1, 1.0)
        m2.setValue(1, 1, 1.0)
        m2.setValue(2, 1, 1.0)
        m2.setValue(0, 2, 1.0)
        m2.setValue(1, 2, 1.0)
        m2.setValue(2, 2, 1.0)

        // Create the expected TODO : FINISH
        expected.setValue(0, 0, 1.0)
        expected.setValue(1, 0, 1.0)
        expected.setValue(2, 0, 1.0)
        expected.setValue(0, 1, 1.0)
        expected.setValue(1, 1, 1.0)
        expected.setValue(2, 1, 1.0)
        expected.setValue(0, 2, 1.0)
        expected.setValue(1, 2, 1.0)
        expected.setValue(2, 2, 1.0)

        // Multiply the two matricies
        Matrix actual = Matrix.Multiply(x1, x2);

        // Test res dimensions
        if (actual.N != 3) {
            LogResult(testName, false, "Incorrect dimension " + String.valueOf(actual.N));
        }


    }

    public static void MatrixMultiplicationTestTwo() {
        String testName = "MatrixMultiplicationTwo";

        Matrix m1 = new Matrix(2, 4);
        Matrix m2 = new Matrix(2, 2);
        Matrix exp = new Matrix(2, 4);

        m1.setValue(0, 0, 1.0);
        m1.setValue(1, 1, 2.0);
        m1.setValue(0, 2, 3.0);
        m1.setValue(1, 3, 4.0);

        m2.setValue(0, 0, 1.0);
        m2.setValue(1, 1, 1.0);

        exp.setValue(0, 0, 1.0);
        // TODO : FINISH

    }

    public static void MatrixAdditionSubtractionTest() {

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