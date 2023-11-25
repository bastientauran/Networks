package model.node;

import java.util.LinkedList;

import model.io.Layer;
import model.io.PacketEvent;
import model.io.PacketTracer;
import model.link.Link;
import model.network.Header;
import model.network.HeaderType;
import model.network.IpAddress;
import model.network.MacAddress;
import model.network.MacAddressContainer;
import model.network.MacHeader;
import model.network.Packet;

/**
 * Class representing an interface.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Node
 * @see Link
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
     * Node connected to this interface
     */
    private Node node;

    /**
     * Link connected to this interface
     */
    private Link link;

    /**
     * Queue to store packets being sent
     */
    private LinkedList<Packet> queue;

    /**
     * Max size of the queue
     */
    private int queueSizeMaxPackets;

    /**
     * Flag used to indicate if this interface is currently sending a packet
     */
    private boolean isSending;

    /**
     * Flag used to indicate if this interface is currently receiving a packet
     */
    private boolean isReceiving;

    /**
     * Create a new interface
     * 
     * @param name Name of the interface
     * @param node The node connected to this interface
     * @param link The link connected to this interface
     */
    public Interface(String name, Node node, Link link) {
        this.name = name;
        this.macAddress = MacAddressContainer.getInstance().getNewMacAddress();
        this.ipAddress = new IpAddress();
        this.node = node;
        this.link = link;
        this.link.attachInterface(this);
        this.queue = new LinkedList<Packet>();
        this.queueSizeMaxPackets = 1000;
        this.isSending = false;
        this.isReceiving = false;
    }

    /**
     * Add a packet to the queue, if possible
     * 
     * @param packet  The packet to add
     * @param nextHop Ip address of next hop
     * @return True if the packet has been added, False otherwise
     */
    public boolean enque(Packet packet, IpAddress nextHop) {
        ArpTable arpTable = this.node.getArpTable();
        MacAddress dstMacAddress = arpTable.getEntry(nextHop);

        if (dstMacAddress == null) {
            System.out.println("Do not know destination MAC address, dropping packet");
            return false;
        }

        Header currentHeader = packet.peekHeader();
        if (currentHeader != null) {
            if (currentHeader.getType() == HeaderType.MAC_HEADER) {
                throw new IllegalStateException("Packet cannot already have MAC header");
            }
        }

        MacHeader macHeader = new MacHeader(this.macAddress, dstMacAddress);
        packet.addHeader(macHeader);

        if (this.queue.isEmpty() && !this.isSending) {
            this.startTx(packet);
            return true;
        }

        if (this.queue.size() == this.queueSizeMaxPackets) {
            PacketTracer.getInstance().tracePacket(this.node.getNodeId(), Layer.MAC, PacketEvent.DROP, packet);
            return false;
        }

        PacketTracer.getInstance().tracePacket(this.node.getNodeId(), Layer.MAC, PacketEvent.ENQUE, packet);
        this.queue.add(packet);

        return true;
    }

    /**
     * Receive a new packet from link.
     * Drop it if destination MAC address is incorrect
     * 
     * @param packet The packet received
     */
    public void receive(Packet packet) {

        PacketTracer.getInstance().tracePacket(this.node.getNodeId(), Layer.MAC, PacketEvent.RECEIVE, packet);

        MacHeader macHeader = (MacHeader) packet.popHeader();

        if (macHeader.getDestination().equals(this.macAddress)) {
            this.node.receive(packet);
        }
    }

    /**
     * Start transmission of a packet
     * 
     * @param packet The packet to send
     */
    public void startTx(Packet packet) {
        if (this.isSending == true) {
            throw new IllegalStateException("Cannot send a packet while another is already being sent");
        }
        this.isSending = true;

        PacketTracer.getInstance().tracePacket(this.node.getNodeId(), Layer.MAC, PacketEvent.SEND, packet);

        this.link.startTx(packet, this);
    }

    /**
     * End of transmission of a packet
     * 
     * @param packet The packet sent
     */
    public void endTx(Packet packet) {
        this.isSending = false;

        if (!this.queue.isEmpty()) {
            Packet newPacket = this.queue.removeFirst();
            this.startTx(newPacket);
        }
    }

    /**
     * Start reception of a packet
     * 
     * @param packet The packet to receive
     */
    public void startRx(Packet packet) {
        if (this.isReceiving == true) {
            throw new IllegalStateException("Cannot receive a packet while another is already being received");
        }
        this.isReceiving = true;
    }

    /**
     * End of reception of a packet
     * 
     * @param packet The packet received
     */
    public void endRx(Packet packet) {
        this.isReceiving = false;

        this.receive(packet);
    }

    /**
     * Get the IP address of the interface
     * 
     * @return The IP address
     */
    public IpAddress getIpAddress() {
        return this.ipAddress;
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
     * Get MAC address of this itnerface
     * 
     * @return MAC address of this interface
     */
    public MacAddress getMacAddress() {
        return this.macAddress;
    }

    /**
     * Get Node associated to this interface
     * 
     * @return The node associated to this interface
     */
    public Node getNode() {
        return this.node;
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
