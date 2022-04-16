/**
 * During a run-through, this will be called
 *  to evaluate the utility of a particular world
 *  state
 */
public interface UtilityFunction {
    // Lambda
    double CalculateUtility(IWorldState state);
}