import java.util.List;

public interface IPerceptron {
    //region Properties
    List<Matrix> getWeights();
    List<Matrix> getBiases();
    //endregion

    //region Methods
    double[] FeedForward(double[] inputs);
    //endregion
}