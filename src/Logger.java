import java.util.Date;

public class Logger {
    //region Fields
    private static LogLevel _logLevel = LogLevel.DEBUG;
    //endregion

    //region Logger
    /**
     * Level property
     */
    public static void setLevel(LogLevel level) { _logLevel = level; }
    
    /**
     * Level property
     */
    public static LogLevel getLevel() { return _logLevel; }

    /**
     * Log message and throw exception
     */
    public static void Throw(String msg) throws Exception {
        Write(LogLevel.ERROR, msg);
        throw new Exception(msg);
    }

    /**
     * Debug log
     */
    public static void Debug(String msg) {
        Write(LogLevel.DEBUG, msg);
    }

    /**
     * Verbose log
     */
    public static void Verbose(String msg) {
        Write(LogLevel.VERBOSE, msg);
    }

    /**
     * Warning log
     */
    public static void Warn(String msg) {
        Write(LogLevel.WARN, msg);
    }

    /**
     * Error log
     */
    public static void Error(String msg) {
        Write(LogLevel.ERROR, msg);
    }

    /**
     * Information log
     */
    public static void Info(String msg) {
        Write(LogLevel.INFO, msg);
    }

    /**
     * Graphical user interface log
     */
    public static void Gui(String msg) {
        Write(LogLevel.GUI, msg);
    }
    //endregion

    //region Private
    /**
     * Now property
     */
    private static String getNow() {
        return new Date().toString();
    }

    /**
     * Write message if 
     *  log level permits
     * @param level The message log level
     * @param msg The message
     */
    private static void Write(LogLevel level, String msg) {
        if (!_logLevel.HasLevel(level))
            return;

        PrintToScreen(level, msg);
    }

    /**
     * Print text to screen
     * @param level The message log level
     * @param msg The message
     */
    private static void PrintToScreen(LogLevel level, String msg) {
        System.out.println(
            String.format(
                "%s | [%8s] : %s",
                getNow(),
                level.toString(),
                msg
            )
        );
    }
    //endregion

    //region Level
    /**
     * Log level enum
     */
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