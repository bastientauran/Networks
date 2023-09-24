package model.network;

/**
 * Class representing an IP header
 */
public class IpHeader extends Header {

    /**
     * Source IP address
     */
    private IpAddress source;

    /**
     * Destination IP address
     */
    private IpAddress destination;

    /**
     * Default constructor
     */
    public IpHeader() {
        this.source = new IpAddress();
        this.destination = new IpAddress();
    }

    /**
     * Constructor of IP header
     * @param source Source IP address
     * @param destination Destination IP address
     */
    public IpHeader(IpAddress source, IpAddress destination) {
        this.source = new IpAddress(source);
        this.destination = new IpAddress(destination);
    }

    /**
     * Get source IP address
     * @return Source IP address
     */
    public IpAddress getSource() {
        return new IpAddress(this.source);
    }

    /**
     * Get destination IP address
     * @return Destination IP address
     */
    public IpAddress getDestination() {
        return new IpAddress(this.destination);
    }

    @Override
    public int getSize() {
        return 2*IpAddress.SIZE_NO_MASK_BYTES;
    }

    @Override
    public String toString() {
        return "[IP header: source=" + this.source.toStringNoMask() + ", destination=" + this.destination.toStringNoMask() + "]";
    }
}