import java.util.Date;

public class Logger {
    //region Fields
    private static LogLevel _logLevel = LogLevel.DEBUG;
    //endregion

    //region Logger
    public static void SetLevel(LogLevel level) {
        _logLevel = level;
    }
    public static LogLevel GetLevel() { return _logLevel; }
    public static void Throw(String msg) throws Exception {
        Write(LogLevel.ERROR, msg);
        throw new Exception(msg);
    }
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
    public static void Info(String msg) {
        Write(LogLevel.INFO, msg);
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
        if (!_logLevel.HasLevel(level))
            return;

        PrintToScreen(level, msg);
    }
    private static void PrintToScreen(LogLevel level, String msg) {
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

        int _value;
        LogLevel(int val) {
            _value = val;
        }
        public boolean HasLevel(LogLevel level) {
            return _value <= level._value;
        }
    }
    //endregion
}