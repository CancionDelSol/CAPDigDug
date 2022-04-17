import java.util.Arrays;

public class DeltaWorldState implements IDeltaWorldState{
    //region Members
    double[] _deltas;
    //endregion

    //region Constructor
    /** Create a delta world state with defined deltas */
    public DeltaWorldState(double[] deltas) {
        _deltas = Arrays.copyOf(deltas, deltas.length);
    }
    //endregion

    //region IDeltaWorldState
    /** Get length of values array */
    public int getLength() {
        return _deltas.length;
    }

    public double[] getDelta() {
        return Arrays.copyOf(_deltas, _deltas.length);
    }
    //endregion

    //region Object
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