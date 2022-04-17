public interface IWorldState {
    //region Properties
    int getLength();
    double[] getEncoding();
    long getTime();
    boolean getIsComplete();
    //endregion

    //region Methods
    void ApplyDelta(IDeltaWorldState dState);
    //endregion
}