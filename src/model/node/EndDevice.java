package model.node;

import model.io.Layer;
import model.io.PacketEvent;
import model.io.PacketTracer;
import model.logger.LogSeverity;
import model.logger.Logger;
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
                Logger.getInstance().log(LogSeverity.CRITICAL, "Packet cannot already have IP header");
            }
        }

        Pair<Interface, IpAddress> routingEntry = this.routingTable.getEntry(addressDst);
        if (routingEntry != null) {
            IpAddress addressSrc = routingEntry.first.getIpAddress();

            if (addressSrc == null) {
                Logger.getInstance().log(LogSeverity.ERROR, "Source IP address not set");
                return;
            }

            IpHeader ipHeader = new IpHeader(addressSrc, addressDst);
            packet.addHeader(ipHeader);

            PacketTracer.getInstance().tracePacket(this.getNodeId(), Layer.NETWORK, PacketEvent.SEND, packet);

            routingEntry.first.enque(packet, routingEntry.second);
        } else {
            PacketTracer.getInstance().tracePacket(this.getNodeId(), Layer.NETWORK, PacketEvent.DROP, packet);
            Logger.getInstance().log(LogSeverity.WARNING, "No route to destination, dropping packet");
        }
    }

    @Override
    public void receive(Packet packet) {

        PacketTracer.getInstance().tracePacket(this.getNodeId(), Layer.NETWORK, PacketEvent.RECEIVE, packet);

        Header currentHeader = packet.peekHeader();
        if (currentHeader != null) {
            if (currentHeader.getType() != HeaderType.IP_HEADER) {
                Logger.getInstance().log(LogSeverity.CRITICAL, "Packet does not have an IP header");
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
    }

    // Only for routers
    /*public void forward(Packet packet) {
        Header currentHeader = packet.peekHeader();
        if (currentHeader != null) {
            if (currentHeader.getType() != HeaderType.IP_HEADER) {
                // CRITICAL ERROR
            }
        }

        IpHeader header = (IpHeader) currentHeader;
        Pair<Interface, IpAddress> routingEntry = this.routingTable.getEntry(header.getDestination());
        if (routingEntry != null) {
            routingEntry.first.enque(packet, routingEntry.second);
        } else {
            System.out.println("No route to destination, dropping packet");
        }
    }*/

    @Override
    public void run(SchedulableMethod method, Object[] arguments) {
        switch (method) {
            case END_DEVICE__SEND: {
                this.send((Packet) arguments[0], (IpAddress) arguments[1]);
                break;
            }
            default: {
                Logger.getInstance().log(LogSeverity.CRITICAL, "Unknow method for class EndDevice: " + method);
            }
        }
    }
}
