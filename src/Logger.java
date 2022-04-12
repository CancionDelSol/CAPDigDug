public class Logger {
    //region Methods

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