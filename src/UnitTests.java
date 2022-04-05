public class UnitTests {
    public static void MatrixMultiplicationTest() {
        Matrix m1 = new Matrix(3, 3);
        Matrix m2 = new Matrix(3, 3);

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

        // Multiply the two matricies
        Matrix res = Matrix.Multiply(x1, x2);

        
    }
}