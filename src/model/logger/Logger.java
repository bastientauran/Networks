package model.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import model.simulator.Simulator;

/**
 * Class used to trace all packets transmissions
 * It uses Singleton design pattern.
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class Logger {

    /**
     * Instance of Logger
     */
    private static Logger instance;

    /**
     * Boolean indicating if the logger is enabled or not
     */
    private boolean enableLog;

    /**
     * Instance writing logs to file
     */
    private PrintWriter writer;

    /**
     * Minimum severity level to record
     */
    private LogSeverity minSeverityLevel;

    /**
     * Create the instance of Logger an initializes attributes to default values
     */
    private Logger() {
        this.enableLog = false;
        this.writer = null;
        this.minSeverityLevel = LogSeverity.WARNING;
    }

    /**
     * Get the instance of Logger. Construct it if not already created.
     * 
     * @return The instance of Logger
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Destroy the instance of Logger
     */
    public static void destroy() {
        if (instance.writer != null) {
            instance.writer.close();
        }
        instance = null;
    }

    /**
     * Enable the Logger
     */
    public void enableLogger() {
        this.enableLog = true;
    }

    /**
     * Disable the Logger
     */
    public void disableLogger() {
        this.enableLog = false;
        if (this.writer != null) {
            this.writer.close();
        }
    }

    /**
     * Set the path to where the trace will be printed
     * 
     * @param path Output path. If empty, print to the console
     */
    public void setOutputPath(String path) {
        try {
            this.writer = new PrintWriter(new FileWriter(path));
        } catch (IOException e) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Cannot open " + path + " for log");
        }
    }

    /**
     * Print this trace in the console instead of a file
     */
    public void printToConsole() {
        this.writer = null;
    }

    /**
     * Set the minimum severity level to display
     * 
     * @param minSeverityLevel Minimum severity level
     */
    public void setMinSeveritylevel(LogSeverity minSeverityLevel) {
        this.minSeverityLevel = minSeverityLevel;
    }

    /**
     * Log a new message. It is logged only if severity is equal or higher than
     * minimum severity
     * 
     * @param logSeverity Log severity level
     * @param message     Optional message to add
     */
    public void log(LogSeverity logSeverity, String message) {
        if (!this.enableLog || logSeverity.compareTo(minSeverityLevel) < 0) {
            return;
        }

        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        if (stes.length < 3) {
            return;
        }
        StackTraceElement ste = stes[2];
        String output = "[" + Simulator.getInstance().getCurrentTime().toStringWithPrecision(6) + "]";
        output += "[" + logSeverity + "]";
        output += "[" + ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber() + "] ";
        output += message;

        if (this.writer != null) {
            this.writer.write(output + "\n");
        } else {
            System.out.println(output);
        }

        if (logSeverity == LogSeverity.CRITICAL) {
            System.out.println("Critical error encoutered, stopping simulation with stack trace:");
            throw new NetworksCriticalException();
        }
    }
}
