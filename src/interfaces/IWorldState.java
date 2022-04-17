public interface IWorldState {
    //region Properties
    int getLength();
    double[] getEncoding();
    long getTime();
    //endregion

    //region Methods
    void ApplyDelta(IDeltaWorldState dState);
    //endregion
}