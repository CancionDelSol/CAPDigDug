/** @file IDeltaWorldState.java
 *  @brief Interface for a DeltaWorldState
 *  
 *  @author Roger Johnson
 *  @bug No known bugs.
 */
public interface IDeltaWorldState {
    //region Properties
    int getLength() throws Exception ;
    double[] getDeltaEncoding() throws Exception ;
    //endregion
}