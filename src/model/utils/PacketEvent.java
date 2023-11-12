package model.utils;

/**
 * All packet events that can be traced in a simulation
 * 
 * @author Bastien Tauran
 * @version 1.0
 * 
 * @see PacketTracer
 */
public enum PacketEvent {
    ENQUE("ENQ"),
    SEND("SND"),
    RECEIVE("RCV"),
    DROP("DRP");

    /**
     * String representation of the event
     */
    private String name;

    /**
     * Construct a PacketEvent item
     * 
     * @param name String representation of the event
     */
    private PacketEvent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
