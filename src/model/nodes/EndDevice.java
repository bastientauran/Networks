package model.nodes;

import model.network.Packet;

/**
 * Class representing a End Device
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Node
 */
public class EndDevice extends Node {

    /**
     * End Device constructor
     * 
     * @param name Name of this node
     */
    public EndDevice(String name) {
        super(name);
    }

    @Override
    public void send(Packet packet) {
        // TODO send to correct interface
    }

    @Override
    public void receive(Packet packet) {
        System.out.println("Received packet " + packet);
    }
}
