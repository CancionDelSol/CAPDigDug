import java.util.List;
import java.util.ArrayList;

public class Perceptron implements IPerceptron {
    //region Fields
    private List<Matrix> _weights = new ArrayList<>();
    private List<Matrix> _biases = new ArrayList<>();
    //endregion

    //region Properties
    
    //endregion

    //region Constructor

    //endregion

    //region IPerceptron
    /**
     * Return a copy of the weights
     */
    public List<Matrix> getWeights() {

    }
    /**
     * Return a copy of the biases
     */
    public List<Matrix> getBiases() {

    }

    public double[] FeedForward(double[] inputs) throws Exception {

    }
    //endregion
}