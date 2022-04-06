import java.text.DecimalFormat;
import java.lang.Math;

/**
 * Just holds some useful functions
 */
public class Util {
    public static String FormatDouble(double x, int places) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(places);
        return df.format(x);
    }

    public static boolean IsNearZero (double a) {
        return (Math.abs(a) < Values.Epsilon);
    }

    public static double Sig(double x) {
        return 1.0/(1.0 + Math.exp(-x));
    }
}