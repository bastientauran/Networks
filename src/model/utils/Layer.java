package model.utils;

/**
 * All layers that can be traced in a simulation
 * 
 * @author Bastien Tauran
 * @version 1.0
 * 
 * @see PacketTracer
 */
public enum Layer {
    APPLICATION("APP"),
    NETWORK("NET"),
    MAC("MAC"),
    PHYSICAL("PHY");

    /**
     * String representation of the layer
     */
    private String name;

    /**
     * Construct a Layer item
     * 
     * @param name String representation of the layer
     */
    private Layer(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
