package model.nodes;

import java.util.ArrayList;

import model.network.IpAddress;
import model.network.Packet;

/**
 * Abstract class representing a node
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Interface
 */
public abstract class Node {

    /**
     * Name of the node
     */
    protected String name;

    /**
     * All the interfaces that compose this node
     */
    protected ArrayList<Interface> interfaces;

    /**
     * ARP table of this node
     */
    protected ArpTable arpTable;

    /**
     * Routing table of this node
     */
    protected RoutingTable routingTable;

    /**
     * Node contructor
     * 
     * @param name Name of this node
     */
    public Node(String name) {
        this.name = name;
        this.interfaces = new ArrayList<Interface>();
        this.arpTable = new ArpTable();
        this.routingTable = new RoutingTable();
    }

    /**
     * Send a new packet.
     * This method adds an IP header
     * 
     * @param packet      The packet to send
     * @param destination destination IP address of this packet
     */
    public abstract void send(Packet packet, IpAddress destination);

    /**
     * Receive a new Packet from an interface
     * 
     * @param packet The packet received
     */
    public abstract void receive(Packet packet);

    /**
     * Forward a new Packet to the next interface.
     * This method does not add an IP header
     * 
     * @param packet The packet to forward
     */
    public abstract void forward(Packet packet);

    /**
     * Add a new interface to this node
     * 
     * @param interf The interface to add
     */
    public void addInterface(Interface interf) {
        this.interfaces.add(interf);
    }

    /**
     * Get node name
     * 
     * @return Node name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get list on interfaces connected to this node
     * 
     * @return List of interfaces
     */
    public ArrayList<Interface> getInterfaces() {
        return this.interfaces;
    }

    /**
     * Get the ARP table of this node
     * 
     * @return ARP table
     */
    public ArpTable getArpTable() {
        return this.arpTable;
    }

    /**
     * Get routing table of this node
     * 
     * @return Routing table
     */
    public RoutingTable getRoutingTable() {
        return this.routingTable;
    }

}