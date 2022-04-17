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
    IGenetic _source;
    double _mutationRate;
    //endregion

    //region Constructor
    /** Initialize a population and use a genetic algorithm to minimize the error function*/
    public GenAlg(int popSize, IGenetic initializer, double mutationRate, IErrorFunction errorFunction) {
        _errorFunction = errorFunction;
        _mutationRate = mutationRate;
        _source = initializer;
        _popSize = popSize;
    }
    //endregion

    //region GeneticAlgorithm
    /** Run the gentic algorithm
     *   for a certain number of epochs 
     */
    public void Execute(int epochs) throws Exception {
        // Check for bad state
        ValidateSetup();

        // Variables
        IGenetic[] population = new IGenetic[_popSize];

        // Iterate over epochs
        for (int i = 0; i < epochs; i++) {
            
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