/** @file GenAlg.java
 *  @brief Runs genetic Algorithm
 *          using an IGenetic and
 *          world states
 *
 *  @author Roger Johnson
 *  @bug No known bugs.
 */


/* -- Includes -- */

/* Java Utilities */
import java.util.*;

/** @brief Genetic Algorithm Handler
 *
 *  Handles the excecution of a genetic
 *   algorithm.
 *
 */
public class GenAlg {
    //region Members
    private IErrorFunction _errorFunction;
    int _popSize;
    IAgent _source;
    double _mutationRate;
    //endregion

    //region Constructor
    /** Initialize a population and use a genetic algorithm to minimize the error function*/
    public GenAlg(int popSize, IAgent initializer, double mutationRate, IErrorFunction errorFunction) {
        _errorFunction = errorFunction;
        _mutationRate = mutationRate;
        _source = initializer;
        _popSize = popSize;
    }
    //endregion

    //region GeneticAlgorithm
    /** Run the gentic algorithm
     *   for a certain number of epochs 
     *  The networks will be ran on each world state
     *   input. The average performance (error) will
     *   be used as the criteria for the genetic algorithm
     */
    public void Execute(int epochs, List<IWorldState> inputs ) throws Exception {
        // Check for bad state
        ValidateSetup();

        // Variables needed for Genetic Algorithm
        double bestError = Values.VeryLarge;

        // Iterate over epochs
        for (int i = 0; i < epochs; i++) {
            Logger.VERBOSE("Epoch : " + i);

            // Run each genetic (network) for each world state
            //  and take the average performance
            IAgent replacement = _source;

            // Use the source member as the first test 
            //  agent. This ensures we track its performance
            //  and don't lose it in case there isn't a better 
            //  performing agent
            IAgent newMember = _source;
            for (int curGeneticIndex = 0; curGeneticIndex < _popSize; curGeneticIndex++) {

                // Track the average performance
                double avgPerformance = 0.0;
                for (IWorldState initialState : inputs) {
                    IWorldState copy = (IWorldState)initialState.clone();

                    do {
                        IDeltaWorldState delta = newMember.GetAction(copy);
                        copy.ApplyDelta(delta);
                    } while (!copy.getIsComplete());

                    avgPerformance += _errorFunction.GetError(copy);
                }
                avgPerformance = avgPerformance/(double)inputs.size();

                if (avgPerformance < bestError) {
                    bestError = avgPerformance;
                    replacement = newMember;
                }

                // Create new member
                newMember = (IAgent)_source.MutatedCopy(_mutationRate);
            }
            _source = replacement;
        }
    }

    private void ValidateSetup() throws Exception {
        if (_errorFunction == null)
            throw new Exception("No error function");
        
        if (_popSize < 1)
            throw new Exception("Invalid population size: " + String.valueOf(_popSize));

        if (_source == null)
            throw new Exception ("No source genetic");

        if (_mutationRate < Values.Epsilon)
            throw new Exception ("Mutation rate too small");
    }
    //endregion

    public interface IErrorFunction {
        double GetError(IWorldState state);
    }
}