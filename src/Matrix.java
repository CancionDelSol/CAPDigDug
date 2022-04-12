public class Matrix {
    //region Fields
    private double[] _values;
    //endregion

    //region Properties
    public final int Nx;
    public final int Ny;
    //endregion

    //region Constructor
    public Matrix(int nx, int ny, double[] initialValues) throws Exception {
        this(nx, ny);
        for (int i = 0; i < initialValues.length; i++) {
            _values[i] = initialValues[i];
        }
    }

    /**
     * Create an empty matrix with nx by ny
     *  dimensions
     */
    public Matrix(int nx, int ny) throws Exception{

        // Save dimensions
        Nx = nx;
        Ny = ny;

        // Initialize values array
        _values = new double[Nx * Ny];
    }
    //endregion

    //region Operators
    /**
     * Multiply two matricies
     * Return null on incompatible matricies
     */
    public static Matrix Multiply(Matrix a, Matrix b) throws Exception {
        
        // Dimension mismatch
        if (a.Nx != b.Ny)
            throw new Exception("Mismatched Dimensions in Multiply");

        // Result matrix
        Matrix res = new Matrix(a.Nx, b.Ny);

        // Iterate over x of new matrix
        for (int x = 0; x < a.Nx; x++) {

            // Iterate over y of new matrix
            for (int y = 0; y < b.Ny; y++) {
                double sum = 0.0;

                // Iterate over overlapping indicies
                for (int cur = 0; cur < a.Nx; cur++) {
                    sum += a.get(cur, y) * b.get(x, cur);
                }

            }

        }

        return res;

    }

    /**
     * Add two matricies
     * Return null for incompatible matricies
     */
    public static Matrix Add(Matrix a, Matrix b) throws Exception {

        // Dimension mismatch
        if (a.Nx != b.Nx || a.Ny != b.Ny)
            throw new Exception("Mismatched Dimensions in Add");

        Matrix res = new Matrix(a.Nx, b.Nx);

        // Iterate over x 
        for (int x = 0; x < a.Nx; x++) {

            // Iterate over y 
            for (int y = 0; y < b.Ny; y++) {
                
                res.put(x, y, a.get(x, y) + b.get(x, y));

            }

        }

        return res;
    }

    /**
     * Subtrace two matricies
     * Return null for incompatible matricies
     */
    public static Matrix Subtract(Matrix a, Matrix b) throws Exception {

        // Dimension mismatch
        if (a.Nx != b.Nx || a.Ny != b.Ny)
            throw new Exception("Mismatched dimensions in Subtract");

        Matrix res = new Matrix(a.Nx, b.Nx);

        // Iterate over x 
        for (int x = 0; x < a.Nx; x++) {

            // Iterate over y 
            for (int y = 0; y < b.Ny; y++) {
                
                res.put(x, y, a.get(x, y) - b.get(x, y));

            }

        }

        return res;
    }
    //endregion

    //region Public
    /**
     * Retrieve value at index
     */
    public double get(int x, int y) throws Exception {
        if (!_inBounds(x, y))
            throw new Exception("Out of bounds");
        
        return _values[y * Nx + x];
    }

    /**
     * Put value at index
     */
     public void put(int x, int y, double value) throws Exception {
         if (!_inBounds(x, y))
            throw new Exception("Out of bounds");

        _values[y * Nx + x] = value;
     }
    //endregion

    //region Object
    @Override
    public object clone() {
        
    }
    //endregion

    //region Private
    private boolean _inBounds(int x, int y) {
        if (x < 0 || x > Nx || y < 0 || y > Ny)
            return false;
        
        return true;
    }
    //endregion
}