import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Perceptron extends XmlBase implements IPerceptron {
    //region Fields
    private List<Matrix> _weights = new ArrayList<>();
    private List<Matrix> _biases = new ArrayList<>();
    private int[] _structure;

    // XML
    private static final String HEAD_ELEMENT_NAME = "Perceptron";
    private static final String STRUCTURE_ATT_NAME = "Structure";
    private static final String WEIGHT_SET_NAME = "WEIGHTS";
    private static final String WEIGHT_ITEM_NAME = "WEIGHT";
    private static final String BIAS_SET_NAME = "BIASES";
    private static final String BIAS_ITEM_NAME = "BIAS";
    //endregion

    //region Properties
    
    //endregion

    //region Constructor
    /** Create a Perceptron from xml element */
    public Perceptron(Element ele) {
        ProcessElement(ele);
    }

    /** Create a Perceptron from xml text */
    public Perceptron(String xml) {
        ReadXml(xml);
    }

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

    //region IMatrixFunction
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

    //region XmlBase
    @Override
    protected void ProcessElement(Element ele) {
        if (ele == null)
            return;

        try {
            switch (ele.getNodeName()) {
                // Head Perceptron Element
                case (HEAD_ELEMENT_NAME):
                    NodeList headElementChildNodes = ele.getChildNodes();
                    String[] content = ele.getAttribute(STRUCTURE_ATT_NAME).split(" ");
                    _structure = new int[content.length];
                    for (int curS = 0; curS < _structure.length; curS++) {
                        _structure[curS] = Integer.parseInt(content[curS]);
                    }

                    for (int i = 0; i < headElementChildNodes.getLength(); i++) {
                        ProcessElement((Element)headElementChildNodes.item(i));
                    }
                    break;
                
                // Set of weights
                case (WEIGHT_SET_NAME):
                    NodeList weightSetElementNodes = ele.getChildNodes();
                    for (int curWeightMatrix = 0; curWeightMatrix < weightSetElementNodes.getLength(); curWeightMatrix++) {
                        Matrix newMat = new Matrix((Element)weightSetElementNodes.item(curWeightMatrix));
                        _weights.add(newMat);
                    }
                    break;
                // Read each bias matrix
                case (BIAS_SET_NAME):
                    NodeList biasSetElementNodes = ele.getChildNodes();
                    for (int curBiasMatrix = 0; curBiasMatrix < biasSetElementNodes.getLength(); curBiasMatrix++) {
                        Matrix newMat = new Matrix((Element)biasSetElementNodes.item(curBiasMatrix));
                        _biases.add(newMat);
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
        String netStructure = "";
        for (int i = 0; i < _structure.length; i++) {
            netStructure = String.format("%s %d", netStructure, _structure[i]);
        }
        rootEle.setAttribute(STRUCTURE_ATT_NAME, netStructure.trim());

        try {
            // Weight set element
            Element weightSet = parentDoc.createElement(WEIGHT_SET_NAME);
            for (int curWeightSet = 0; curWeightSet < _weights.size(); curWeightSet++) {
                weightSet.appendChild(_weights.get(curWeightSet).BuildElement(parentDoc));
            }

            // Bias set element
            Element biasSet = parentDoc.createElement(BIAS_SET_NAME);
            for (int curBiasSet = 0; curBiasSet < _biases.size(); curBiasSet++) {
                biasSet.appendChild(_biases.get(curBiasSet).BuildElement(parentDoc));
            }

            // Append child nodes
            rootEle.appendChild(weightSet);
            rootEle.appendChild(biasSet);

        } catch (Exception exc) {
            Logger.Error("Matrix BuildElement Exc: " + exc.getMessage());
        }

        return rootEle;
    }
    //endregion

    //region Object
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (!(obj instanceof Perceptron))
            return false;

        Perceptron other = (Perceptron)obj;

        if (_structure.length != other._structure.length)
            return false;

        for (int i = 0; i < _structure.length; i++) {
            if (_structure[i] != other._structure[i])
                return false;
        }

        if (_weights.size() != other._weights.size())
            return false;

        if (_biases.size() != other._biases.size()) 
            return false;

        // The Weight set count and bias set counts
        //  should be equal in a perceptron
        //  so this is safe
        for (int curWeightSet = 0; curWeightSet < _weights.size(); curWeightSet++) {
            if (!_weights.get(curWeightSet).equals(other._weights.get(curWeightSet)))
                return false;

            if (!_biases.get(curWeightSet).equals(other._biases.get(curWeightSet)))
                return false;
        }

        return true;
    }
    //endregion
}