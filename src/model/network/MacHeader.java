package model.network;

/**
 * Class representing a MAC header
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see MacAddress
 */
public class MacHeader extends Header {

    /**
     * Source MAC address
     */
    private MacAddress source;

    /**
     * Destination MAC address
     */
    private MacAddress destination;

    /**
     * Default constructor
     */
    public MacHeader() {
        this.source = new MacAddress();
        this.destination = new MacAddress();
    }

    /**
     * Constructor of MAC header
     * @param source Source MAC address
     * @param destination Destination MAC address
     */
    public MacHeader(MacAddress source, MacAddress destination) {
        this.source = new MacAddress(source);
        this.destination = new MacAddress(destination);
    }

    /**
     * Get source MAC address
     * @return Source MAC address
     */
    public MacAddress getSource() {
        return new MacAddress(this.source);
    }

    /**
     * Get destination MAC address
     * @return Destination MAC address
     */
    public MacAddress getDestination() {
        return new MacAddress(this.destination);
    }

    @Override
    public int getSize() {
        return 2*MacAddress.SIZE_BYTES;
    }

    @Override
    public String toString() {
        return "[MAC header: source=" + this.source + ", destination=" + this.destination + "]";
    }
}
