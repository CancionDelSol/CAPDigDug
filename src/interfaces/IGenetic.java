package interfaces;

public interface IGenetic {
    IGenetic PerfectCopy();
    IGenetic MutatedCopy(double rate);
}