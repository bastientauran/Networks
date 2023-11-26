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
     * Number of nanoseconds in a second
     */
    public static final int NANOSECONDS_IN_SECOND = 1000000000;

    /**
     * Default constructor.
     * Initializes time to zero.
     */
    public Time() {
        this.seconds = 0;
        this.nanoSeconds = 0;
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
            Logger.getInstance().log(LogSeverity.CRITICAL, "Number of nanoseconds must be strictly lower than 1 billion");
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
