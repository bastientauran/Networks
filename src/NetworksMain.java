import model.link.PointToPointLink;
import model.network.IpAddress;
import model.network.IpHeader;
import model.network.MacHeader;
import model.network.Packet;
import model.nodes.EndDevice;
import model.nodes.Interface;
import model.simulator.SchedulableMethod;
import model.simulator.Simulator;
import model.simulator.Time;

public class NetworksMain {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        EndDevice nodeSrc = new EndDevice("src");
        EndDevice nodeDst = new EndDevice("dst");

        PointToPointLink pointToPointLink = new PointToPointLink();
        pointToPointLink.setBandwidthBytesPerSecond(1000);
        pointToPointLink.setDelay(new Time(50, 0));

        Interface interfaceSrc = new Interface("src", nodeSrc, pointToPointLink);
        Interface interfaceDst = new Interface("dst", nodeDst, pointToPointLink);

        interfaceSrc.setIpAddress(new IpAddress("192.168.0.1/24"));
        interfaceDst.setIpAddress(new IpAddress("192.168.0.2/24"));

        nodeSrc.addInterface(interfaceSrc);
        nodeDst.addInterface(interfaceDst);

        nodeSrc.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceSrc, new IpAddress("192.168.0.2"));
        nodeDst.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceDst, new IpAddress("192.168.0.1"));

        nodeSrc.getArpTable().addEntry(new IpAddress("192.168.0.2"), interfaceDst.getMacAddress());
        nodeDst.getArpTable().addEntry(new IpAddress("192.168.0.1"), interfaceSrc.getMacAddress());

        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(1000, 0));

        Packet packet;
        IpHeader ipHeader;
        for (int i = 0; i < 5; i++) {
            packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());
            ipHeader = new IpHeader(new IpAddress("192.168.0.1"), new IpAddress("192.168.0.2"));
            packet.addHeader(ipHeader);
            Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE_SEND, packet);
        }

        Simulator.getInstance().run();

        System.out.println(Simulator.getInstance().getCurrentTime());
    }
}
