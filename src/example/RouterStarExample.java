package example;

import helper.PointToPointHelper;
import model.logger.LogSeverity;
import model.logger.Logger;
import model.network.IpAddress;
import model.network.IpHeader;
import model.network.MacHeader;
import model.network.Packet;
import model.node.EndDevice;
import model.node.Interface;
import model.node.Router;
import model.simulator.SchedulableMethod;
import model.simulator.Simulator;
import model.simulator.Time;
import model.utils.Pair;

/**
 * Example of a star topology with three end devices and a router between them.
 * Some packets are manually sent.
 * 
 * The topology is as follows
 * 
 * <pre>
 * 192.168.0.2/24                   192.168.0.1/24    192.168.1.1/24                   192.168.1.2/24
 * +----------+                              +----------+                              +----------+
 * |  Node 1  |------------------------------|  Router  |------------------------------|  Node 2  |
 * +----------+          1kB/s, 30s          +----------+          1kB/s, 30s          +----------+
 *                                                 |
 *                                                 | 192.168.2.1/24
 *                                                 |                                   192.168.2.2/24
 *                                                 |                                   +----------+
 *                                                 +-----------------------------------|  Node 3  |
 *                                                                 1kB/s, 30s          +----------+
 * </pre>
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class RouterStarExample {
    public static void main(String[] args) {
        Logger.getInstance().enableLogger();
        Logger.getInstance().setOutputPath("out/simulation/RouterStarExample", "log.txt");
        Logger.getInstance().setMinSeveritylevel(LogSeverity.INFO);

        Logger.getInstance().log(LogSeverity.INFO, "Launch RouterStarExample");

        EndDevice node1 = new EndDevice("node 1");
        EndDevice node2 = new EndDevice("node 2");
        EndDevice node3 = new EndDevice("node 3");
        Router router = new Router("router");

        PointToPointHelper p2pHelper = new PointToPointHelper(1000, new Time(30, 0));
        Pair<Interface, Interface> interfaces;
        
        interfaces = p2pHelper.install(router, node1, new IpAddress("192.168.0.0/24"));
        Interface interfaceRouter1 = interfaces.first;
        Interface interfaceNode1 = interfaces.second;

        interfaces = p2pHelper.install(router, node2, new IpAddress("192.168.1.0/24"));
        Interface interfaceRouter2 = interfaces.first;
        Interface interfaceNode2 = interfaces.second;

        interfaces = p2pHelper.install(router, node3, new IpAddress("192.168.2.0/24"));
        Interface interfaceRouter3 = interfaces.first;
        Interface interfaceNode3 = interfaces.second;

        // Set routing
        node1.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceNode1, new IpAddress("192.168.0.1"));
        node1.getRoutingTable().addEntry(new IpAddress("192.168.1.0/24"), interfaceNode1, new IpAddress("192.168.0.1"));
        node1.getRoutingTable().addEntry(new IpAddress("192.168.2.0/24"), interfaceNode1, new IpAddress("192.168.0.1"));

        node2.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceNode2, new IpAddress("192.168.1.1"));
        node2.getRoutingTable().addEntry(new IpAddress("192.168.1.0/24"), interfaceNode2, new IpAddress("192.168.1.1"));
        node2.getRoutingTable().addEntry(new IpAddress("192.168.2.0/24"), interfaceNode2, new IpAddress("192.168.1.1"));

        node3.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceNode3, new IpAddress("192.168.2.1"));
        node3.getRoutingTable().addEntry(new IpAddress("192.168.1.0/24"), interfaceNode3, new IpAddress("192.168.2.1"));
        node3.getRoutingTable().addEntry(new IpAddress("192.168.2.0/24"), interfaceNode3, new IpAddress("192.168.2.1"));
        
        router.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceRouter1, new IpAddress("192.168.0.2"));
        router.getRoutingTable().addEntry(new IpAddress("192.168.1.0/24"), interfaceRouter2, new IpAddress("192.168.1.2"));
        router.getRoutingTable().addEntry(new IpAddress("192.168.2.0/24"), interfaceRouter3, new IpAddress("192.168.2.2"));

        // Set ARP
        node1.getArpTable().addEntry(new IpAddress("192.168.0.1"), interfaceRouter1.getMacAddress());
        node2.getArpTable().addEntry(new IpAddress("192.168.1.1"), interfaceRouter2.getMacAddress());
        node3.getArpTable().addEntry(new IpAddress("192.168.2.1"), interfaceRouter3.getMacAddress());

        router.getArpTable().addEntry(new IpAddress("192.168.0.2"), interfaceNode1.getMacAddress());
        router.getArpTable().addEntry(new IpAddress("192.168.1.2"), interfaceNode2.getMacAddress());
        router.getArpTable().addEntry(new IpAddress("192.168.2.2"), interfaceNode3.getMacAddress());

        // Create scenario
        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(100, 0));
        Simulator.getInstance().setScenarioName("RouterStarExample");
        Simulator.getInstance().enableProgressBar();

        // Create traffic
        Packet packet;
        for (int i = 0; i < 5; i++) {
            packet = new Packet("Packet " + (i + 1), 1000 - new MacHeader().getSize() - new IpHeader().getSize());
            Simulator.getInstance().schedule(new Time(), node1, SchedulableMethod.END_DEVICE__SEND, packet,
                    interfaceNode2.getIpAddress());
        }
        
        for (int i = 0; i < 5; i++) {
            packet = new Packet("Packet " + (i + 1), 1000 - new MacHeader().getSize() - new IpHeader().getSize());
            Simulator.getInstance().schedule(new Time(), node3, SchedulableMethod.END_DEVICE__SEND, packet,
                    interfaceNode1.getIpAddress());
        }

        Simulator.getInstance().run();
    }
}
