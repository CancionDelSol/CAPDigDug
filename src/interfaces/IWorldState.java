public interface IWorldState {
    //region Properties
    int getLength() throws Exception;
    double[] getEncoding() throws Exception;
    long getTime() throws Exception;
    boolean getIsComplete() throws Exception;
    //endregion

    //region Methods
    void ApplyDelta(IDeltaWorldState dState) throws Exception;
    IWorldState clone() throws Exception;
    //endregion
}