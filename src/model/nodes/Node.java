package model.nodes;

import java.util.ArrayList;

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
     * Node contructor
     * 
     * @param name Name of this node
     */
    public Node(String name) {
        this.name = name;
        this.interfaces = new ArrayList<Interface>();
    }

    /**
     * Send a new packet
     * @param packet The packet to send
     */
    public abstract void send(Packet packet);

    /**
     * Receive a new Packet from an interface
     * @param packet The packet received
     */
    public abstract void receive(Packet packet);
}
