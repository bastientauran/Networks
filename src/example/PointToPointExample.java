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
import model.simulator.SchedulableMethod;
import model.simulator.Simulator;
import model.simulator.Time;
import model.utils.Pair;

/**
 * Simple example building two end devices with a point to point link between
 * them.
 * Some packets are manually sent.
 * 
 * The topology is as follows
 * 
 * <pre>
 * 192.168.0.1/24                            192.168.0.2/24
 * +----------+                              +----------+
 * |  Node 1  |------------------------------|  Node 2  |
 * +----------+          1kB/s, 50s          +----------+
 * </pre>
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class PointToPointExample {
    public static void main(String[] args) {
        Logger.getInstance().enableLogger();
        Logger.getInstance().setOutputPath("out/simulation/PointToPointExample/log.txt");
        Logger.getInstance().setMinSeveritylevel(LogSeverity.INFO);

        Logger.getInstance().log(LogSeverity.INFO, "Launch PointToPointExample");

        EndDevice nodeSrc = new EndDevice("src");
        EndDevice nodeDst = new EndDevice("dst");

        PointToPointHelper p2pHelper = new PointToPointHelper(1000, new Time(50, 0));
        Pair<Interface, Interface> interfaces = p2pHelper.install(nodeSrc, nodeDst, new IpAddress("192.168.0.0/24"));

        Interface interfaceSrc = interfaces.first;
        Interface interfaceDst = interfaces.second;

        nodeSrc.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceSrc, new IpAddress("192.168.0.2"));
        nodeDst.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceDst, new IpAddress("192.168.0.1"));

        nodeSrc.getArpTable().addEntry(new IpAddress("192.168.0.2"), interfaceDst.getMacAddress());
        nodeDst.getArpTable().addEntry(new IpAddress("192.168.0.1"), interfaceSrc.getMacAddress());

        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(1000, 0));
        Simulator.getInstance().setScenarioName("PointToPointExample");

        Packet packet;
        for (int i = 0; i < 5; i++) {
            packet = new Packet("Packet " + (i + 1), 1000 - new MacHeader().getSize() - new IpHeader().getSize());
            Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet,
                    interfaceDst.getIpAddress());
        }

        Simulator.getInstance().run();
    }
}
