import java.util.List;

public interface IPerceptron extends IXmlSerializable {
    //region Properties
    List<Matrix> getWeights() throws Exception;
    List<Matrix> getBiases() throws Exception;
    int[] getStructure() throws Exception;
    //endregion

    //region Methods
    double[] FeedForward(double[] inputs) throws Exception;
    //endregion
}