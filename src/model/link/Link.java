package model.link;

import model.network.Packet;
import model.nodes.Interface;

/**
 * Abstract class representing a link
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Interface
 */
public abstract class Link {

    /**
     * Start transmission of a packet
     * 
     * @param packet The packet to send
     * @param src    The source interface
     */
    public abstract void startTx(Packet packet, Interface src);
}
