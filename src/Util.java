import java.text.DecimalFormat;
import java.lang.Math;
import java.util.Random;
import java.util.Calendar;

/**
 * Just holds some useful functions
 */
public class Util {
    private static final Random _rand = new Random(Calendar.getInstance().getTimeInMillis());

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

    public static double Uniform(double min, double max) {
        return ((max - min) * _rand.nextDouble()) + min;
    }

    public static double Mag(double[] vec) {
        double sum = 0.0;
        for (int i = 0; i < vec.length; i++) {
            sum += Math.pow(vec[i], 2.0);
        }
        return Math.sqrt(sum);
    }

    public static String DisplayCoord(int x, int y) {
        return "(" + x + ", " + y + ")";
    }

    public static String DisplayArray(int[] array) {
        StringBuilder bldr = new StringBuilder();
        bldr.append("[");
        for (int i = 0; i < array.length - 1; i++) {
            bldr.append(String.format("%3d ", array[i]));
        }
        bldr.append(String.format("%3d]", array[array.length - 1]));
        return bldr.toString();
    }

    public static String DisplayArray(double[] array) {
        StringBuilder bldr = new StringBuilder();
        bldr.append("[");
        for (int i = 0; i < array.length - 1; i++) {
            bldr.append(String.format("%.4f ", array[i]));
        }
        bldr.append(String.format("%.4f]", array[array.length - 1]));
        return bldr.toString();
    }
}