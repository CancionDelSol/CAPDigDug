public interface IAgent extends IGenetic {
    IDeltaWorldState GetAction(IWorldState currentState);
}