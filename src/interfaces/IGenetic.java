package interfaces;

public interface IGenetic {
    IGenetic PerfectCopy() throws Exception;
    IGenetic MutatedCopy(double rate) throws Exception;
}