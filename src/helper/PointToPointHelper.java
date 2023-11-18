package helper;

import model.link.PointToPointLink;
import model.network.IpAddress;
import model.node.Interface;
import model.node.Node;
import model.simulator.Time;
import model.utils.Pair;

/**
 * Helper used to build a new point to Point link
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class PointToPointHelper {

    /**
     * Bandwidth of the link to create
     */
    private int bandwidthBytesPerSecond;

    /**
     * Delay of the link to create
     */
    private Time delay;

    /**
     * Default constructor.
     * Initializes bandwidth to 1kB/s, and delay to 1s
     */
    public PointToPointHelper() {
        this.bandwidthBytesPerSecond = 1000;
        this.delay = new Time(0, 1000000);
    }

    /**
     * Constructor witrh custom attribute values
     * 
     * @param bandwidthBytesPerSecond The bandwith of link in bytes per second
     * @param delay                   The delay of the link
     */
    public PointToPointHelper(int bandwidthBytesPerSecond, Time delay) {
        this.bandwidthBytesPerSecond = bandwidthBytesPerSecond;
        this.delay = delay;
    }

    /**
     * Install a new link between two nodes
     * 
     * @param src     First node to link
     * @param dst     Second node to link
     * @param network IP network created by this link
     * @return The pair of interfaces created
     */
    public Pair<Interface, Interface> install(Node src, Node dst, IpAddress network) {
        if (network.getMask() > 30) {
            throw new IllegalArgumentException("Network mask too high, cannot add two different addresses");
        }

        PointToPointLink pointToPointLink = new PointToPointLink();
        pointToPointLink.setBandwidthBytesPerSecond(this.bandwidthBytesPerSecond);
        pointToPointLink.setDelay(this.delay);

        Interface interfaceSrc = new Interface("ens" + (src.getNumberInterfaces() + 3), src, pointToPointLink);
        Interface interfaceDst = new Interface("ens" + (dst.getNumberInterfaces() + 3), dst, pointToPointLink);

        IpAddress net = network.getNetwork();
        IpAddress ipAddressSrc = net.getNextAddress();
        IpAddress ipAddressDst = ipAddressSrc.getNextAddress();

        interfaceSrc.setIpAddress(ipAddressSrc);
        interfaceDst.setIpAddress(ipAddressDst);

        src.addInterface(interfaceSrc);
        dst.addInterface(interfaceDst);

        return new Pair<Interface, Interface>(interfaceSrc, interfaceDst);
    }

    /**
     * Install a new link between two nodes
     * 
     * @param src          First node to link
     * @param dst          Second node to link
     * @param ipAddressSrc Source IP address
     * @param ipAddressDst Destination IP address
     * @return The pair of interfaces created
     */
    public Pair<Interface, Interface> install(Node src, Node dst, IpAddress ipAddressSrc, IpAddress ipAddressDst) {
        if (!ipAddressSrc.isInNetwork(ipAddressDst)) {
            throw new IllegalArgumentException("Source IP address is not in same network than destination");
        }
        if (!ipAddressDst.isInNetwork(ipAddressSrc)) {
            throw new IllegalArgumentException("Source IP address is not in same network than destination");
        }

        PointToPointLink pointToPointLink = new PointToPointLink();
        pointToPointLink.setBandwidthBytesPerSecond(this.bandwidthBytesPerSecond);
        pointToPointLink.setDelay(this.delay);

        Interface interfaceSrc = new Interface("ens" + (src.getNumberInterfaces() + 3), src, pointToPointLink);
        Interface interfaceDst = new Interface("ens" + (dst.getNumberInterfaces() + 3), dst, pointToPointLink);

        interfaceSrc.setIpAddress(ipAddressSrc);
        interfaceDst.setIpAddress(ipAddressDst);

        src.addInterface(interfaceSrc);
        dst.addInterface(interfaceDst);

        return new Pair<Interface, Interface>(interfaceSrc, interfaceDst);
    }

    /**
     * Set the bandwidth of the link to create
     * 
     * @param bandwidthBytesPerSecond The bandwidth in bytes per sencond
     */
    public void setBandwidthBytesPerSecond(int bandwidthBytesPerSecond) {
        this.bandwidthBytesPerSecond = bandwidthBytesPerSecond;
    }

    /**
     * Set the delay of the link to create
     * 
     * @param delay The delay
     */
    public void setDelay(Time delay) {
        this.delay = delay;
    }

}
