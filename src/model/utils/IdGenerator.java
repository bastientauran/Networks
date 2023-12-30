package model.utils;

/**
 * Singleton used to generate unique IDs for some elements of the simulation
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class IdGenerator {

    /**
     * Instance of Simulator
     */
    private static IdGenerator instance;

    /**
     * Counter of node IDs
     */
    private int nodeId;

    /**
     * Counter of packet IDs
     */
    private int packetId;

    /**
     * Private constructor
     */
    private IdGenerator() {
        this.nodeId = 0;
        this.packetId = 0;
    }

    /**
     * Get the instance of Simulator. Construct it if not already created.
     * 
     * @return The instance of Simulator
     */
    public static IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    /**
     * Get a new node ID. This ID is incremented each time this method is called
     * 
     * @return Node ID
     */
    public int getNextNodeId() {
        this.nodeId++;

        return this.nodeId;
    }

    /**
     * Get a new packet ID. This ID is incremented each time this method is called
     * 
     * @return Packet ID
     */
    public int getNextPacketId() {
        this.packetId++;

        return this.packetId;
    }
}
