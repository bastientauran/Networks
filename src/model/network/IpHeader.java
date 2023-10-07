package model.network;

/**
 * Class representing an IP header
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see IpAddress
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
        super(HeaderType.IP_HEADER);
        this.source = new IpAddress();
        this.destination = new IpAddress();
    }

    /**
     * Constructor of IP header
     * 
     * @param source      Source IP address
     * @param destination Destination IP address
     */
    public IpHeader(IpAddress source, IpAddress destination) {
        super(HeaderType.IP_HEADER);
        this.source = new IpAddress(source, 32);
        this.destination = new IpAddress(destination, 32);
    }

    /**
     * Get source IP address
     * 
     * @return Source IP address
     */
    public IpAddress getSource() {
        return new IpAddress(this.source);
    }

    /**
     * Get destination IP address
     * 
     * @return Destination IP address
     */
    public IpAddress getDestination() {
        return new IpAddress(this.destination);
    }

    @Override
    public int getSize() {
        return 2 * IpAddress.SIZE_NO_MASK_BYTES;
    }

    @Override
    public String toString() {
        return "[IP header: source=" + this.source.toStringNoMask() + ", destination="
                + this.destination.toStringNoMask() + "]";
    }
}
