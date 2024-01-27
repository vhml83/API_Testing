package logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Log {

    private static Logger Log = LogManager.getLogger(Log.class.getName());

    private enum LogLevel {FATAL, ERROR, WARN, INFO, DEBUG, TRACE}

    static {

        LogLevel level;

        try {
            level = LogLevel.valueOf(System.getProperty("log", "info").toUpperCase());
        } catch (IllegalArgumentException iae) {
            level = LogLevel.INFO;
        }

        switch (level) {
            case FATAL -> Configurator.setRootLevel(Level.FATAL);
            case ERROR -> Configurator.setRootLevel(Level.ERROR);
            case WARN ->  Configurator.setRootLevel(Level.WARN);
            case DEBUG -> Configurator.setRootLevel(Level.DEBUG);
            case TRACE -> Configurator.setRootLevel(Level.TRACE);
            default -> Configurator.setRootLevel(Level.INFO);
        }
    }

    public static void fatal(String message) { Log.fatal(message); }
    public static void error(String message) { Log.error(message); }
    public static void warn(String message) { Log.warn(message); }
    public static void info(String message) { Log.info(message); }
    public static void debug(String message) { Log.debug(message); }
    public static void trace(String message) { Log.trace(message); }

}
