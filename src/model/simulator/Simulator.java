package model.simulator;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * The simulator is used to scehdule events in the future.
 * It uses Singleton template.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Event
 */
public class Simulator {

    /**
     * Instance of Simulator
     */
    private static Simulator instance;

    /**
     * Current time in simulation
     */
    private Time currentTime;

    /**
     * Time where the simulation will be stopped
     */
    private Time stopTime;

    /**
     * The list of events to schedule
     */
    private TreeSet<Event> events;

    /**
     * Private constructor
     */
    private Simulator() {
        this.currentTime = new Time();
        this.stopTime = new Time();
        this.events = new TreeSet<Event>();
    }

    /**
     * Get the instance of Simulator. Construct it if not already created.
     * 
     * @return The instance of Simulator
     */
    public static Simulator getInstance() {
        if (instance == null) {
            instance = new Simulator();
        }
        return instance;
    }

    /**
     * Set the time where the simulation will be stopped
     * 
     * @param stopTime Time where the simulation will be stopped
     */
    public void setStopTime(Time stopTime) {
        this.stopTime = stopTime;
    }

    /**
     * Get current time in simulation
     * 
     * @return Current time
     */
    public Time getCurrentTime() {
        return new Time(this.currentTime);
    }

    /**
     * Schedule a new event, if date of execution is lower than stopTime
     * 
     * @param time      Time to schedule the event
     * @param instance  The instance to launch
     * @param method    The method of instance to launch
     * @param arguments The arguments of the method
     */
    public void schedule(Time time, Schedulable instance, String method, ArrayList<Object> arguments) {
        if (time.compareTo(this.currentTime) < 0) {
            throw new IllegalArgumentException("Cannot schedule in the past");
        }
        if (time.compareTo(this.stopTime) < 0) {
            Event e = new Event(time, instance, method, arguments);
            this.events.add(e);
        }
    }

    /**
     * Run the simulation.
     * It runs while events are scheduled and stop simulation time is not reached
     */
    public void run() {
        while (!this.events.isEmpty()) {
            Event e = this.events.pollFirst();
            this.currentTime = e.getTime();
            e.runEvent();
        }
    }
}
