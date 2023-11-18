package model.logger;

/**
 * Enumeration of all log severity levels, used by logger
 * 
 * @author Bastien Tauran
 * @version 1.0
 * 
 * @see Logger
 */
public enum LogSeverity {
    DEBUG("Debug"),
    INFO("Info"),
    WARNING("Warning"),
    ERROR("Error"),
    CRITICAL("Critical");

    /**
     * String representation of the severity level
     */
    private String name;

    /**
     * Construct a new instance of log severity
     * 
     * @param name String representation of this severity level
     */
    private LogSeverity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
