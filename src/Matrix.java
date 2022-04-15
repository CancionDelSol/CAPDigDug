import java.util.Arrays;
import interfaces.IGenetic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Matrix extends XmlBase implements IGenetic {
    //region Fields
    private double[] _values;

    // XMLs
    private static final String HEAD_ELEMENT_NAME = "MATRIX";
    private static final String DIMENSIONS_ATT_NAME = "DIM";
    private static final String VALUES_ELEMENT_NAME = "VALUES";
    //endregion

    //region Properties
    public int Nx;
    public int Ny;
    //endregion

    //region Constructor
    /**
     * Initialize a matrix from xml element
     */
    public Matrix(Element ele) {
        ProcessElement(ele);
    }

    /**
     * Initialize a matrix from xml text
     */
    public Matrix(String xml) {
        ReadXml(xml);
    }

    /**
     * Initialize a matrix with values
     */
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
        Matrix res = new Matrix(b.Nx, a.Ny);

        // Iterate over x of new matrix
        for (int x = 0; x < res.Nx; x++) {

            // Iterate over y of new matrix
            for (int y = 0; y < res.Ny; y++) {
                double sum = 0.0;

                // Iterate over overlapping indicies
                for (int cur = 0; cur < a.Nx; cur++) {
                    sum += a.get(cur, y) * b.get(x, cur);
                }

                res.put(x, y, sum);
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

        Matrix res = new Matrix(a.Nx, a.Ny);

        // Iterate over x 
        for (int x = 0; x < a.Nx; x++) {

            // Iterate over y 
            for (int y = 0; y < b.Ny; y++) {
                
                res.put(x, y, a.get(x, y) + b.get(x, y));

            }

        }

        return res;
    }

    /** Subtract two matricies */
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

    /** Apply some function to each value in the matrix */
    public void ApplyFunction(IMatrixFunction function) {
        for (int i = 0; i < _values.length; i++) {
            _values[i] = function.Activate(_values[i]);
        }
    }
    //endregion

    //region IGenetic
    @Override
    public IGenetic PerfectCopy() throws Exception {
        return (IGenetic)clone();
    }

    @Override
    public IGenetic MutatedCopy(double rate) throws Exception {
        Matrix newM = new Matrix(Nx, Ny, _values);
        for (int i = 0; i < _values.length; i++) {
            newM._values[i] += Util.Uniform(-rate, rate);
        }
        return newM;
    }
    //endregion

    //region Xml
    @Override
    protected void ProcessElement(Element ele) {
        if (ele == null)
            return;

        try {
            switch (ele.getNodeName()) {
                case (HEAD_ELEMENT_NAME):
                    NodeList headElementChildNodes = ele.getChildNodes();
                    String[] content = ele.getAttribute(DIMENSIONS_ATT_NAME).split(" ");
                    Nx = Integer.parseInt(content[0]);
                    Ny = Integer.parseInt(content[1]);
                    _values = new double[Nx * Ny];

                    for (int i = 0; i < headElementChildNodes.getLength(); i++) {
                        ProcessElement((Element)headElementChildNodes.item(i));
                    }
                    break;
                // Read each weight matrix
                case (VALUES_ELEMENT_NAME):
                    String[] values = ele.getTextContent().split(" ");
                    for (int v = 0; v < values.length; v++) {
                        _values[v] = Double.parseDouble(values[v]);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    protected Element BuildElement(Document parentDoc) {
        Element rootEle = parentDoc.createElement(HEAD_ELEMENT_NAME);
        String dims = String.format("%d %d", Nx, Ny);
        rootEle.setAttribute(DIMENSIONS_ATT_NAME, dims);

        // Values
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < _values.length; i++) {
            bldr.append(" ");
            bldr.append(_values[i]);
        }

        Element valuesElement = parentDoc.createElement(VALUES_ELEMENT_NAME);
        valuesElement.setTextContent(bldr.toString().trim());
        rootEle.appendChild(valuesElement);

        return rootEle;
    }
    //endregion

    //region Public
    /** Retrieve value at index */
    public double get(int x, int y) throws Exception {
        _inBounds(x, y);
        
        return _values[y * Nx + x];
    }

    /** Get a row from the Matrix */
    public double[] getRow(int index) {
        return Arrays.copyOfRange(_values, index * Ny, (index * Ny) + Nx);
    }

    /** Put value at index */
     public void put(int x, int y, double value) throws Exception {
        _inBounds(x, y);
        
        _values[_getIndex(x, y)] = value;
     }
    //endregion

    //region Object
    @Override
    public Object clone() {
        try {
            return new Matrix(Nx, Ny, _values);
        } catch (Exception exc) {
            Logger.Error("Exception Matrix clone " + exc.getMessage());
        }
        return null;
    }
    //endregion

    //region Private
    private boolean _inBounds(int x, int y) throws Exception {
        if (x < 0 || x >= Nx || y < 0 || y >= Ny)
            throw new Exception("Out of bounds (" + x + ", " + y + ")");
        
        return true;
    }

    /** Return the index in the _values array */
    private int _getIndex(int x, int y) throws Exception {
        int index = (y * Nx) + x;
        if (index < 0 || index >= _values.length)
            throw new Exception("Out of bounds [" + index + "]: (" + x + ", " + y + ") ");

        return index;
    }
    //endregion
}