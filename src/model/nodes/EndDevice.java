package model.nodes;

import model.network.Header;
import model.network.HeaderType;
import model.network.IpAddress;
import model.network.IpHeader;
import model.network.Packet;
import model.simulator.Schedulable;
import model.simulator.SchedulableMethod;
import model.utils.Pair;

/**
 * Class representing a End Device
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Node
 */
public class EndDevice extends Node implements Schedulable {

    /**
     * End Device constructor
     * 
     * @param name Name of this node
     */
    public EndDevice(String name) {
        super(name);
    }

    @Override
    public void send(Packet packet, IpAddress addressDst) {
        Header currentHeader = packet.peekHeader();
        if (currentHeader != null) {
            if (currentHeader.getType() == HeaderType.IP_HEADER) {
                throw new IllegalStateException("Packet cannot already have IP header");
            }
        }

        Pair<Interface, IpAddress> routingEntry = this.routingTable.getEntry(addressDst);
        if (routingEntry != null) {
            IpAddress addressSrc = routingEntry.first.getIpAddress();

            if (addressSrc == null) {
                throw new IllegalArgumentException("Source IP address not set");
            }

            IpHeader ipHeader = new IpHeader(addressSrc, addressDst);
            packet.addHeader(ipHeader);

            routingEntry.first.enque(packet, routingEntry.second);
        } else {
            System.out.println("No route to destination, dropping packet");
        }
    }

    @Override
    public void receive(Packet packet) {
        Header currentHeader = packet.peekHeader();
        if (currentHeader != null) {
            if (currentHeader.getType() != HeaderType.IP_HEADER) {
                throw new IllegalStateException("Packet does not have an IP header");
            }
        }
        IpHeader header = (IpHeader) currentHeader;
        IpAddress destination = header.getDestination();

        for (Interface interf : this.interfaces) {
            if (interf.getIpAddress().equals(destination)) {
                System.out.println("Received packet " + packet);
                return;
            }
        }

        forward(packet);
    }

    @Override
    public void forward(Packet packet) {
        Header currentHeader = packet.peekHeader();
        if (currentHeader != null) {
            if (currentHeader.getType() != HeaderType.IP_HEADER) {
                throw new IllegalStateException("Packet does not have an IP header");
            }
        }

        IpHeader header = (IpHeader) currentHeader;
        Pair<Interface, IpAddress> routingEntry = this.routingTable.getEntry(header.getDestination());
        if (routingEntry != null) {
            routingEntry.first.enque(packet, routingEntry.second);
        } else {
            System.out.println("No route to destination, dropping packet");
        }
    }

    @Override
    public void run(SchedulableMethod method, Object[] arguments) {
        switch (method) {
            case END_DEVICE__SEND: {
                this.send((Packet) arguments[0], (IpAddress) arguments[1]);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknow method for class EndDevice: " + method);
            }
        }
    }
}
