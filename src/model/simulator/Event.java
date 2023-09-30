package model.simulator;

/**
 * Class representing a event.
 * This is a method of chosen instance to launch at desired date.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Schedulable
 */
public class Event implements Comparable<Event> {
    /**
     * Time to schedule the event
     */
    private Time time;

    /**
     * The instance to launch
     */
    private Schedulable instance;

    /**
     * The method of instance to launch
     */
    private String method;

    /**
     * The arguments of the method
     */
    private Object[] arguments;

    /**
     * ID of the event
     */
    private long id;

    /**
     * Create a new event
     * 
     * @param time      Time to schedule the event
     * @param instance  The instance to launch
     * @param method    The method of instance to launch
     * @param arguments The arguments of the method
     */
    public Event(Time time, Schedulable instance, String method, Object[] arguments) {
        this.time = time;
        this.instance = instance;
        this.method = method;
        this.arguments = arguments;
        this.id = Singleton.getInstance().getId();
    }

    /**
     * Get time of event
     * 
     * @return Time of event
     */
    public Time getTime() {
        return this.time;
    }

    /**
     * Run this event
     */
    public void runEvent() {
        this.instance.run(this.method, this.arguments);
    }

    @Override
    public int compareTo(Event other) {
        if (this.time == other.time) {
            if (this.id > other.id) {
                return 1;
            } else if (this.id < other.id) {
                return -1;
            } else {
                return 0;
            }
        }
        return this.time.compareTo(other.time);
    }
}
