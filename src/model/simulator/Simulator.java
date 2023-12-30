package model.simulator;

import java.util.TreeSet;

import model.io.PacketTracer;
import model.logger.LogSeverity;
import model.logger.Logger;

/**
 * The simulator is used to scehdule events in the future.
 * It uses Singleton template.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Event
 * @see SchedulableMethod
 */
public class Simulator implements Schedulable {

    /**
     * Instance of Simulator
     */
    private static Simulator instance;

    /**
     * Name of the scenario
     */
    private String scenarioName;

    /**
     * Current time in simulation
     */
    private Time currentTime;

    /**
     * Time where the simulation will be stopped
     */
    private Time stopTime;

    /**
     * Print progress bar in console if set to true
     */
    private boolean enableProgressBar;

    /**
     * Interval between to prints on the console of progress
     */
    private Time progressBarStep;

    /**
     * Indicates if the simulation has been launched
     */
    private boolean running;

    /**
     * The list of events to schedule
     */
    private TreeSet<Event> events;

    /**
     * Unique ID given to events to differenciate ones one same date.
     */
    private long id;

    /**
     * Private constructor
     */
    private Simulator() {
        this.scenarioName = "";
        this.currentTime = new Time();
        this.stopTime = new Time();
        this.enableProgressBar = false;
        this.progressBarStep = new Time();
        this.running = false;
        this.events = new TreeSet<Event>();
        this.id = 0;
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
     * Reset the simulation
     */
    public void reset() {
        if (this.running == true) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Cannot reset simulation when running");
        }
        this.scenarioName = "";
        this.currentTime = new Time();
        this.stopTime = new Time();
        this.events = new TreeSet<Event>();
        this.id = 0;
    }

    /**
     * Set the time where the simulation will be stopped
     * 
     * @param stopTime Time where the simulation will be stopped
     */
    public void setStopTime(Time stopTime) {
        if (stopTime.compareTo(new Time()) == 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Stop time must be strictly positive");
        }
        this.stopTime = stopTime;
    }

    /**
     * Enable progress bar in simulation
     */
    public void enableProgressBar() {
        this.enableProgressBar = true;
    }

    /**
     * Disable progress bar in simulation
     */
    public void disableProgressBar() {
        this.enableProgressBar = false;
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
     * Print progress bar
     */
    public void printProgressBar() {
        System.out.println("Progress: " + this.currentTime.getSeconds() + "/" + this.stopTime.getSeconds());

        schedule(this.currentTime.add(this.progressBarStep), this, SchedulableMethod.SIMULATOR__PRINT_PROGRESS_BAR);
    }

    /**
     * Schedule a new event
     * 
     * @param time      Time to schedule the event
     * @param instance  The instance to launch
     * @param method    The method of instance to launch
     * @param arguments The arguments of the method
     */
    public void schedule(Time time, Schedulable instance, SchedulableMethod method, Object... arguments) {
        if (time.compareTo(this.currentTime) < 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Cannot schedule in the past");
        }
        if (checkArguments(method, arguments)) {
            if (this.running == true) {
                if (time.compareTo(this.stopTime) > 0) {
                    Logger.getInstance().log(LogSeverity.INFO, "Trying to schedule event after stop simulation time");
                    return;
                }
            }
            Event e = new Event(time, instance, method, arguments, id);
            this.events.add(e);
            id++;
        }
    }

    /**
     * Run the simulation.
     * It runs while events are scheduled and stop simulation time is not reached
     */
    public void run() {
        if (this.stopTime.compareTo(new Time()) == 0) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Stop time not set");
        }

        if (this.scenarioName != "") {
            PacketTracer.getInstance().initTrace();
        }

        if (this.enableProgressBar) {
            if (this.stopTime.compareTo(new Time(10, 0)) < 0) {
                this.progressBarStep = new Time(0, 100000000);
            } else {
                double seconds = this.stopTime.getSeconds();
                double step = seconds / 100;
                int stepSeconds = (int) step;
                int stepNanoseconds = (int) (1000000000 * (step - stepSeconds));
                this.progressBarStep = new Time(stepSeconds, stepNanoseconds);
            }

            schedule(this.progressBarStep, this, SchedulableMethod.SIMULATOR__PRINT_PROGRESS_BAR);
        }

        this.running = true;
        Logger.getInstance().log(LogSeverity.INFO, "Launch Simulation");
        for (Event e : this.events) {
            if (e.getTime().compareTo(this.stopTime) > 0) {
                this.events.tailSet(e).clear();
                break;
            }
        }

        while (!this.events.isEmpty()) {
            Event e = this.events.pollFirst();
            this.currentTime = e.getTime();
            e.runEvent();
        }
        this.running = false;

        Logger.getInstance().log(LogSeverity.INFO, "Simulation finished");

        if (this.scenarioName != "") {
            PacketTracer.getInstance().closeTrace();
        }

        PacketTracer.destroy();
        Logger.destroy();
    }

    /**
     * Test if the arguments given in schedule method are coherent with types
     * contained in SchedulableMethod instance
     * 
     * @param method    The class and method to schedule
     * @param arguments The arguments of th emethod
     * @return true if this method can be scheduled
     */
    private boolean checkArguments(SchedulableMethod method, Object[] arguments) {
        Class<?>[] argumentTypes = method.getArgumentTypes();
        if (argumentTypes.length != arguments.length) {
            Logger.getInstance().log(LogSeverity.CRITICAL,
                    "Schedule method " + method + " with the wrong number of arguments. Need " + argumentTypes.length
                            + " but got " + arguments.length);
        }
        for (int i = 0; i < argumentTypes.length; i++) {
            try {
                argumentTypes[i].cast(arguments[i]);
            } catch (ClassCastException e) {
                Logger.getInstance().log(LogSeverity.CRITICAL,
                        "Cannot cast argument " + arguments[i] + " to class " + argumentTypes[i]);
            }
        }
        return true;
    }

    /**
     * Get scenario name
     * 
     * @return Scenario name
     */
    public String getScenarioName() {
        return this.scenarioName;
    }

    /**
     * Set scenario name
     * 
     * @param scenarioName The scenario name
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    @Override
    public void run(SchedulableMethod method, Object[] arguments) {
        switch (method) {
            case SIMULATOR__PRINT_PROGRESS_BAR: {
                this.printProgressBar();
                break;
            }
            default: {
                Logger.getInstance().log(LogSeverity.CRITICAL, "Unknow method for class PointToPointLink: " + method);
            }
        }
    }
}
