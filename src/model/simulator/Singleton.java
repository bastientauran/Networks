package model.simulator;

/**
 * Class used to generate unique IDs
 * It uses Singleton template.
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class Singleton {

    /**
     * Instance of Singleton
     */
    private static Singleton instance;

    /**
     * Current value of ID
     */
    private long value;

    /**
     * Private constructor
     */
    private Singleton() {
        this.value =0;
    }

    /**
     * Get the instance of Singleton. Construct it if not already created.
     * 
     * @return The instance of Singleton
     */
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * Get a new unique ID
     * @return The ID
     */
    public long getId() {
        long id = this.value;
        this.value++;
        return id;
    }
}
