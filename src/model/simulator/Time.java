package model.simulator;

import model.logger.LogSeverity;
import model.logger.Logger;

/**
 * Class representing Time
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class Time implements Comparable<Time> {

    /**
     * Number of seconds
     */
    private int seconds;

    /**
     * Number of nanoseconds within the second
     */
    private int nanoSeconds;

    /**
     * Number of nanoseconds in a microsecond
     */
    public static final int NANOSECONDS_IN_MICROSECOND = 1000;

    /**
     * Number of nanoseconds in a millisecond
     */
    public static final int NANOSECONDS_IN_MILLISECOND = 1000000;

    /**
     * Number of nanoseconds in a second
     */
    public static final int NANOSECONDS_IN_SECOND = 1000000000;

    /**
     * Number of microseconds in a second
     */
    public static final int MICROSECONDS_IN_SECOND = 1000000;

    /**
     * Number of milliseconds in a second
     */
    public static final int MILLISECONDS_IN_SECOND = 1000;

    /**
     * Default constructor.
     * Initializes time to zero.
     */
    public Time() {
        this.seconds = 0;
        this.nanoSeconds = 0;
    }

    /**
     * Create a new instance of Time in seconds
     * 
     * @param seconds Number of seconds
     * @return Time created
     */
    public static Time seconds(int seconds) {
        if (seconds < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Number of seconds must be positive or null");
        }

        return new Time(seconds, 0);
    }

    /**
     * Create a new instance of Time in milliseconds
     * 
     * @param milliSeconds Number of milliseconds
     * @return Time created
     */
    public static Time milliSeconds(long milliSeconds) {
        if (milliSeconds < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Number of milliseconds must be positive or null");
        }

        int seconds = (int) (milliSeconds / Time.MILLISECONDS_IN_SECOND);
        int nanoSeconds = (int) (Time.NANOSECONDS_IN_MILLISECOND * (milliSeconds % Time.MILLISECONDS_IN_SECOND));
        return new Time(seconds, nanoSeconds);
    }

    /**
     * Create a new instance of Time in microseconds
     * 
     * @param microSeconds Number of microseconds
     * @return Time created
     */
    public static Time microSeconds(long microSeconds) {
        if (microSeconds < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Number of microseconds must be positive or null");
        }

        int seconds = (int) (microSeconds / Time.MICROSECONDS_IN_SECOND);
        int nanoSeconds = (int) (Time.NANOSECONDS_IN_MICROSECOND * (microSeconds % Time.MICROSECONDS_IN_SECOND));
        return new Time(seconds, nanoSeconds);
    }

    /**
     * Create a new instance of Time in nanoseconds
     * 
     * @param nanoSeconds Number of nanoseconds
     * @return Time created
     */
    public static Time nanoSeconds(long nanoSeconds) {
        if (nanoSeconds < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Number of nanoseconds must be positive or null");
        }

        int seconds = (int) (nanoSeconds / Time.NANOSECONDS_IN_SECOND);
        nanoSeconds = nanoSeconds % Time.NANOSECONDS_IN_SECOND;
        return new Time(seconds, (int) nanoSeconds);
    }

    /**
     * Create an object to desired time
     * 
     * @param seconds     Number of seconds
     * @param nanoSeconds Number of nanoseconds within the second
     */
    public Time(int seconds, int nanoSeconds) {
        if (seconds < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Number of seconds must be positive or null");
        }
        if (nanoSeconds < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Number of nanoseconds must be positive or null");
        }
        if (nanoSeconds >= Time.NANOSECONDS_IN_SECOND) {
            Logger.getInstance().log(LogSeverity.CRITICAL,
                    "Number of nanoseconds must be strictly lower than 1 billion");
        }
        this.seconds = seconds;
        this.nanoSeconds = nanoSeconds;
    }

    /**
     * Copy constructor
     * 
     * @param other The object to duplicate
     */
    public Time(Time other) {
        this.seconds = other.seconds;
        this.nanoSeconds = other.nanoSeconds;
    }

    /**
     * Add argument time with the current time.
     * It does not modify current reference, nor the argument
     * 
     * @param toAdd The time to add to current time
     * @return The time being the sum of current and argument
     */
    public Time add(Time toAdd) {
        Time time = new Time(this);
        time.seconds += toAdd.seconds;
        time.nanoSeconds += toAdd.nanoSeconds;

        if (time.nanoSeconds >= Time.NANOSECONDS_IN_SECOND) {
            time.seconds += 1;
            time.nanoSeconds -= Time.NANOSECONDS_IN_SECOND;
        }

        return time;
    }

    /**
     * Remove argument time from the current time.
     * It does not modify current reference, nor the argument
     * 
     * @param toRemove The time to remove to current time
     * @return The time being the difference between current and argument
     */
    public Time remove(Time toRemove) {
        Time time = new Time(this);
        time.seconds -= toRemove.seconds;
        time.nanoSeconds -= toRemove.nanoSeconds;

        if (time.nanoSeconds < 0) {
            time.seconds -= 1;
            time.nanoSeconds += Time.NANOSECONDS_IN_SECOND;
        }
        ;

        if (time.seconds < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Time cannot be negative");
        }

        return time;
    }

    /**
     * Divide time by a given factor
     * 
     * @param divider The factor to use to divide
     * @return A new instance of Time which is the fraction of current Time
     */
    public Time divide(double divider) {
        if (divider < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Cannot divide Time by negative value");
        }
        long nanoSeconds = 1L * Time.NANOSECONDS_IN_SECOND * this.seconds + this.nanoSeconds;
        nanoSeconds /= divider;

        return new Time((int) (nanoSeconds / Time.NANOSECONDS_IN_SECOND),
                (int) (nanoSeconds % Time.NANOSECONDS_IN_SECOND));
    }

    /**
     * Truncate time with a given precision
     * 
     * @param precision The number of decimal to keep when using second format
     * @return A new instance of Time which is truncated
     */
    public Time truncate(int precision) {
        if (precision < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Precision must be positive or null");
        }
        if (precision > 9) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Precision cannot be higher than a nanosecond");
        }

        precision = 9 - precision;
        int mask = 1;
        for (int i = 0; i < precision; i++) {
            mask *= 10;
        }

        Time time = new Time(this);
        time.nanoSeconds = ((int) (time.nanoSeconds / mask)) * mask;

        return time;
    }

    /**
     * Get number of seconds in this instance. Includes fraction of second
     * 
     * @return The number of seconds
     */
    public double getSeconds() {
        return this.seconds + this.nanoSeconds / (Time.NANOSECONDS_IN_SECOND * 1.0);
    }

    /**
     * Get number of milliseconds in this instance. Includes fraction of millisecond
     * 
     * @return The number of milliseconds
     */
    public double getMilliSeconds() {
        return 1.0 * this.seconds * Time.MILLISECONDS_IN_SECOND
                + 1.0 * this.nanoSeconds / Time.NANOSECONDS_IN_MILLISECOND;
    }

    /**
     * Get number of microseconds in this instance. Includes fraction of microsecond
     * 
     * @return The number of microseconds
     */
    public double getMicroSeconds() {
        return 1.0 * this.seconds * Time.MICROSECONDS_IN_SECOND
                + 1.0 * this.nanoSeconds / Time.NANOSECONDS_IN_MICROSECOND;
    }

    /**
     * Get number of nanoseconds in this instance
     * 
     * @return The number of nanoseconds
     */
    public double getNanoSeconds() {
        return 1.0 * this.seconds * Time.NANOSECONDS_IN_SECOND + this.nanoSeconds;
    }

    @Override
    public int compareTo(Time other) {
        if (this.seconds == other.seconds) {
            return this.nanoSeconds - other.nanoSeconds;
        }
        return this.seconds - other.seconds;
    }

    @Override
    public String toString() {
        return this.seconds + "s" + this.nanoSeconds + "ns";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof Time)) {
            return false;
        }
        Time time = (Time) other;
        return this.seconds == time.seconds && this.nanoSeconds == time.nanoSeconds;
    }
}
