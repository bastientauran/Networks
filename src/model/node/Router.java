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
import model.utils.Pair;

/**
 * Class representing a Router
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Node
 */
public class Router extends Node {

    /**
     * Router constructor
     * 
     * @param name Name of this node
     */
    public Router(String name) {
        super(name);
    }

    @Override
    public void send(Packet packet, IpAddress addressDst) {
        Logger.getInstance().log(LogSeverity.DEBUG, "Send packet " + packet.getPacketId() + " to " + addressDst);

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
        Header currentHeader = packet.peekHeader();
        if (currentHeader != null) {
            if (currentHeader.getType() != HeaderType.IP_HEADER) {
                Logger.getInstance().log(LogSeverity.CRITICAL, "Got incorrect header type");
            }
        }

        IpHeader header = (IpHeader) currentHeader;
        Pair<Interface, IpAddress> routingEntry = this.routingTable.getEntry(header.getDestination());
        if (routingEntry != null) {
            Logger.getInstance().log(LogSeverity.DEBUG, "Forward packet " + packet.getPacketId() + " to " + header.getDestination());
            routingEntry.first.enque(packet, routingEntry.second);
        } else {
            System.out.println("No route to destination, dropping packet");
        }
    }
}
