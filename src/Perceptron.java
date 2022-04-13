import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Perceptron implements IPerceptron {
    //region Fields
    private List<Matrix> _weights = new ArrayList<>();
    private List<Matrix> _biases = new ArrayList<>();
    private int[] _structure;
    //endregion

    //region Properties
    
    //endregion

    //region Constructor
    public Perceptron(int[] structure) throws Exception {
        this(structure, null);
    }
    public Perceptron(int[] structure, List<Matrix> weights) throws Exception {
        this(structure, weights, null);
    }
    public Perceptron(int[] structure, List<Matrix> weights, List<Matrix> biases) throws Exception {
        _structure = structure;
        _weights = weights;
        _biases = biases;

        if (_weights == null || _biases == null)
            GenerateRandomWeightsAndBiases();

    }

    /**
     * Create an exact copy of the serializer
     */
    public Perceptron(IPerceptron initializer) throws Exception {

        // Perceptron returns a copy so this is ok
        this(initializer.getStructure(), initializer.getWeights(), initializer.getBiases());

    }
    //endregion

    //region IActivation
    public IMatrixFunction myActivation = (x) -> { return 1.0/(1.0 + Math.exp(-x)); };
    public IMatrixFunction myRandom = (x) -> { return Util.Uniform(-1.0, 1.0); };
    //endregion

    //region IPerceptron
    /** Return a copy of the weights */
    public List<Matrix> getWeights() throws Exception {
        ArrayList<Matrix> weights = new ArrayList<>();

        for (int curLayer = 0; curLayer < _weights.size(); curLayer++) {
            Matrix matrixCopy = (Matrix)_weights.get(curLayer).clone();
            weights.add(matrixCopy);
        }

        return weights;
    }

    /** Return a copy of the biases */
    public List<Matrix> getBiases() throws Exception {
        ArrayList<Matrix> biases = new ArrayList<>();

        for (int curLayer = 0; curLayer < _biases.size(); curLayer++) {
            Matrix matrixCopy = (Matrix)_biases.get(curLayer).clone();
            biases.add(matrixCopy);
        }

        return biases;
    }

    /** Feed the inputs through the Network */
    public double[] FeedForward(double[] inputs) throws Exception {
        
        // This will be what the return array
        //  is pulled from
        Matrix buffer = new Matrix(inputs.length, 1, inputs);

        // Walk through each layer
        for (int curLayer = 0; curLayer < _weights.size(); curLayer++) {

            // Multiply the matricies
            buffer = Matrix.Multiply(buffer, _weights.get(curLayer));

            // Add the biases
            buffer = Matrix.Add(buffer, _biases.get(curLayer));

            // Apply activation function
            buffer.ApplyFunction(myActivation);
        }

        double[] rVal = buffer.getRow(0);

        return rVal;
    }

    public int[] getStructure() {
        int[] structure = new int[_structure.length];
        for (int i = 0; i < structure.length; i++) {
            structure[i] = _structure[i];
        }
        return structure;
    }
    //endregion

    //region Private
    /** Create a random set of weights */
    private void GenerateRandomWeightsAndBiases() throws Exception {

        if (_weights == null) 
            _weights = new ArrayList<>();
        else
            _weights.clear();

        if (_biases == null)
            _biases = new ArrayList<>();
        else
            _weights.clear();

        // Iterate through the layers starting at layer one
        //  In order to create the weight set
        for (int curLayer = 1; curLayer < _structure.length; curLayer++)
        {
            int curLayerCount = _structure[curLayer];
            int prevLayerCount = _structure[curLayer - 1];

            // Weights
            Matrix newWeights = new Matrix(curLayerCount, prevLayerCount);

            // Biases
            Matrix newBiases = new Matrix(_structure[curLayer], 1);

            // Create a random value for the weights
            newWeights.ApplyFunction(myRandom);
            newBiases.ApplyFunction(myRandom);

            _weights.add(newWeights);
            _biases.add(newBiases);
        }
    }
    //endregion
}