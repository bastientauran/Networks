package model.nodes;

import model.network.IpAddress;
import model.network.IpHeader;
import model.network.Packet;
import model.simulator.SchedulableMethod;
import model.utils.Pair;

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
        IpHeader ipHeader = (IpHeader) packet.peekHeader();
        Pair<Interface, IpAddress> routingEntry = this.routingTable.getEntry(ipHeader.getDestination());

        routingEntry.first.enque(packet, routingEntry.second);
    }

    @Override
    public void receive(Packet packet) {
        System.out.println("Received packet " + packet);
    }

    @Override
    public void run(SchedulableMethod method, Object[] arguments) {
        switch (method) {
            case END_DEVICE_SEND: {
                send((Packet) arguments[0]);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknow method for class EndDevice: " + method);
            }
        }
    }
}
