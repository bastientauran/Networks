package model.nodes;

import java.util.LinkedList;

import model.network.IpAddress;
import model.network.MacAddress;
import model.network.MacAddressContainer;
import model.network.Packet;

/**
 * Class representing an interface.
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class Interface {

    /**
     * Name of this interface
     */
    private String name;

    /**
     * MAC address of this interface
     */
    private MacAddress macAddress;

    /**
     * IP address of this interface
     */
    private IpAddress ipAddress;

    /**
     * Queue to store packets being sent
     */
    private LinkedList<Packet> queue;

    /**
     * Max size of the queue
     */
    private int queueSizeMaxPackets;

    /**
     * Create a new interface
     * 
     * @param name Name of the interface
     */
    public Interface(String name) {
        this.name = name;
        this.macAddress = MacAddressContainer.getInstance().getNewMacAddress();
        this.ipAddress = new IpAddress();
        this.queue = new LinkedList<Packet>();
        this.queueSizeMaxPackets = 1000;
    }

    /**
     * Add a packet to the queue, if possible
     * 
     * @param p The packet to add
     * @return True if the packet has been added, False otherwise
     */
    public boolean enque(Packet p) {
        if (this.queue.size() == this.queueSizeMaxPackets) {
            return false;
        }
        this.queue.add(p);

        return true;
    }

    /**
     * Set the IP address of the interface
     * 
     * @param ipAddress The IP address
     */
    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Set the maximum size of the queue
     * 
     * @param queueSizeMaxPackets The maximum size of queue to set
     */
    public void setQueueSizeMaxPackets(int queueSizeMaxPackets) {
        if (queueSizeMaxPackets < 1) {
            throw new IllegalArgumentException("Queue size must be positive");
        }
        this.queueSizeMaxPackets = queueSizeMaxPackets;
    }

    @Override
    public String toString() {
        return "[ Interface " + this.name + ", macAddress='" + this.macAddress + "', ipAddress='" + this.ipAddress
                + "']";
    }

}
