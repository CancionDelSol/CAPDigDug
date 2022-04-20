import java.util.Arrays;

public class DeltaWorldState implements IDeltaWorldState{
    //region Members
    double[] _deltas;
    //endregion

    //region Constructor
    /**
      * Create a delta world 
      *  state with given deltas
      * @param deltas Deltas
      */
    public DeltaWorldState(double[] deltas) {
        _deltas = Arrays.copyOf(deltas, deltas.length);
    }
    //endregion

    //region IDeltaWorldState
    /**
      * Get length of values array
      */
    public int getLength() {
        return _deltas.length;
    }

    /**
     * DeltaEncoding property
     */
    public double[] getDeltaEncoding() {
        return Arrays.copyOf(_deltas, _deltas.length);
    }
    //endregion

    //region Object
    /**
     * Compare this instance with another 
     *  object 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof DeltaWorldState))
            return false;

        DeltaWorldState other = (DeltaWorldState)obj;

        if (other._deltas.length != _deltas.length)
            return false;

        for (int i = 0; i < _deltas.length; i++) {
            if (_deltas[i] != other._deltas[i])
                return false;
        }
        
        return true;
    }
    //endregion
}