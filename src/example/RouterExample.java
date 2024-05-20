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
 * Simple with two end devices and a router between them.
 * Some packets are manually sent.
 * 
 * The topology is as follows
 * 
 * <pre>
 * 192.168.0.1/24                   192.168.0.2/24    192.168.1.1/24                   192.168.1.2/24
 * +----------+                              +----------+                              +----------+
 * |  Node 1  |------------------------------|  Router  |------------------------------|  Node 2  |
 * +----------+          1kB/s, 50s          +----------+          1kB/s, 50s          +----------+
 * </pre>
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class RouterExample {
    public static void main(String[] args) {
        Logger.getInstance().enableLogger();
        Logger.getInstance().setOutputPath("out/simulation/RouterExample", "log.txt");
        Logger.getInstance().setMinSeveritylevel(LogSeverity.INFO);

        Logger.getInstance().log(LogSeverity.INFO, "Launch RouterExample");

        EndDevice nodeSrc = new EndDevice("src");
        EndDevice nodeDst = new EndDevice("dst");
        Router router = new Router("router");

        PointToPointHelper p2pHelper = new PointToPointHelper(1000, new Time(30, 0));
        Pair<Interface, Interface> interfaces = p2pHelper.install(nodeSrc, router, new IpAddress("192.168.0.0/24"));

        Interface interfaceSrc = interfaces.first;
        Interface interfaceRouterSrc = interfaces.second;

        nodeSrc.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceSrc, new IpAddress("192.168.0.2"));
        nodeSrc.getRoutingTable().addEntry(new IpAddress("192.168.1.0/24"), interfaceSrc, new IpAddress("192.168.0.2"));
        router.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceRouterSrc, new IpAddress("192.168.0.1"));

        nodeSrc.getArpTable().addEntry(new IpAddress("192.168.0.2"), interfaceRouterSrc.getMacAddress());
        router.getArpTable().addEntry(new IpAddress("192.168.0.1"), interfaceSrc.getMacAddress());

        interfaces = p2pHelper.install(router, nodeDst, new IpAddress("192.168.1.0/24"));

        Interface interfaceRoutertDst = interfaces.first;
        Interface interfaceDst = interfaces.second;

        router.getRoutingTable().addEntry(new IpAddress("192.168.1.0/24"), interfaceRoutertDst, new IpAddress("192.168.1.2"));
        nodeDst.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceDst, new IpAddress("192.168.1.1"));
        nodeDst.getRoutingTable().addEntry(new IpAddress("192.168.1.0/24"), interfaceDst, new IpAddress("192.168.1.1"));

        router.getArpTable().addEntry(new IpAddress("192.168.1.2"), interfaceDst.getMacAddress());
        nodeDst.getArpTable().addEntry(new IpAddress("192.168.1.1"), interfaceRoutertDst.getMacAddress());

        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(100, 0));
        Simulator.getInstance().setScenarioName("RouterExample");
        Simulator.getInstance().enableProgressBar();

        Packet packet;
        for (int i = 0; i < 5; i++) {
            packet = new Packet("Packet " + (i + 1), 1000 - new MacHeader().getSize() - new IpHeader().getSize());
            Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet,
                    interfaceDst.getIpAddress());
        }

        Simulator.getInstance().run();
    }
}
