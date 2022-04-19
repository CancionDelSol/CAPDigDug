public class DigAgent implements IAgent {
    //region Members
        IPerceptron _perceptron;
        //endregion

        //region Constructor
        /** Create an initial, random agent */
        public DigAgent(int[] structure) throws Exception {
            _perceptron = new Perceptron(structure);
        }

        /** Create a dig agent with a specified perceptron. 
            Will retain reference to the perceptron*/
        public DigAgent(IPerceptron perceptron) throws Exception {
            _perceptron = perceptron;
        }

        /** Create a copy of an initializer perceptron */
        public DigAgent(DigAgent initializer) throws Exception {
            _perceptron = new Perceptron(initializer._perceptron);
        }
        //endregion

        //region IAgent
        /** Return this agents action for a given world state */
        public IDeltaWorldState GetAction(IWorldState currentState) throws Exception {
            double[] encoding = currentState.getEncoding();

            double[] networkOutput = _perceptron.FeedForward(encoding);

            return new DeltaWorldState(networkOutput);
        }
        //endregion

        //region IGenetic
        /** Create a perfect copy of this agent */
        public IGenetic PerfectCopy() throws Exception {
            return new DigAgent(this);
        }

        /** Create a mutated copy of this agent */
        public IGenetic MutatedCopy(double rate) throws Exception {
            IGenetic pAsGen = (IGenetic)_perceptron;
            IPerceptron newP = (IPerceptron)pAsGen.MutatedCopy(rate);
            return new DigAgent(newP);
        }
        //endregion
}