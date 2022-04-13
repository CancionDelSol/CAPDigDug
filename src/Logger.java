import java.util.Date;

public class Logger {
    //region Fields
    private LogLevel _logLevel = LogLevel.DEBUG;
    //endregion

    //region Logger
    public static void Debug(String msg) {
        Write(LogLevel.DEBUG, msg);
    }
    public static void Verbose(String msg) {
        Write(LogLevel.VERBOSE, msg);
    }
    public static void Warn(String msg) {
        Write(LogLevel.WARN, msg);
    }
    public static void Error(String msg) {
        Write(LogLevel.ERROR, msg);
    }
    public static void Gui(String msg) {
        Write(LogLevel.GUI, msg);
    }
    //endregion

    //region Private
    private static String GetNow() {
        return new Date().toString();
    }
    private static void Write(LogLevel level, String msg) {
        System.out.println(
            String.format(
                "%s | [%8s] : %s",
                GetNow(),
                level.toString(),
                msg
            )
        );
    }
    //endregion

    //region Level
    public enum LogLevel {
        DEBUG(1),
        VERBOSE(2),
        WARN(4),
        ERROR(8),
        INFO(16),
        GUI(32);

        int _flags = 1;
        int _val;
        LogLevel(int val) {
            _val = val;
            do {
                _flags &= val;
                val /= 2;
                if (val == 1)
                    break;
            } while (val > 1);
        }
        void Remove(LogLevel level) {
            if ((_val & level._val) == level._val)
                _val &= level._val;
        }
        void Add(LogLevel level) {
            if ((_val & level._val) != level._val)
                _val &= level._val;
        }
    }
    //endregion
}