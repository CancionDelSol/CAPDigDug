public class DigAgent implements IAgent {
    //region Members
        IPerceptron _perceptron;
        //endregion

        //region Constructor
        /** Create an initial, random agent */
        public DigAgent(int[] structure) throws Exception {
            _perceptron = new Perceptron(structure);
        }

        /** Create a copy of an initializer perceptron */
        private DigAgent(IPerceptron perceptron) throws Exception {
            _perceptron = perceptron;
        }
        //endregion

        //region IAgent
        public IDeltaWorldState GetAction(IWorldState currentState) throws Exception {
            Logger.Throw("TODO");
            return null;
        }
        //endregion

        //region IGenetic
        /** Create a perfect copy of this agent */
        public IGenetic PerfectCopy() throws Exception {
            Logger.Throw("TODO");
            return null;
        }

        /** Create a mutated copy of this agent */
        public IGenetic MutatedCopy(double rate) throws Exception {
            Logger.Throw("TODO");
            return null;
        }
        //endregion
}