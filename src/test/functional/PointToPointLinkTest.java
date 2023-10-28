package test.functional;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import helper.PointToPointHelper;
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
import utils.Pair;

public class PointToPointLinkTest {

    @Test
    public void testSendOnePacket() {
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

        Packet packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());

        Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));
        Simulator.getInstance().run();

        assertEquals(new Time(51, 0), Simulator.getInstance().getCurrentTime());
    }

    @Test
    public void testSendOnePacketViaHelper() {
        EndDevice nodeSrc = new EndDevice("src");
        EndDevice nodeDst = new EndDevice("dst");

        PointToPointHelper p2pHelper = new PointToPointHelper(1000, new Time(50, 0));
        Pair<Interface, Interface> interfaces = p2pHelper.install(nodeSrc, nodeDst, new IpAddress("192.168.0.0/24"));

        Interface interfaceSrc = interfaces.first;
        Interface interfaceDst = interfaces.first;

        nodeSrc.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceSrc, new IpAddress("192.168.0.2"));
        nodeDst.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceDst, new IpAddress("192.168.0.1"));

        nodeSrc.getArpTable().addEntry(new IpAddress("192.168.0.2"), interfaceDst.getMacAddress());
        nodeDst.getArpTable().addEntry(new IpAddress("192.168.0.1"), interfaceSrc.getMacAddress());

        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(1000, 0));

        Packet packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());

        Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));
        Simulator.getInstance().run();

        assertEquals(new Time(51, 0), Simulator.getInstance().getCurrentTime());
    }

    @Test
    public void testSendOnePacketViaHelper2() {
        EndDevice nodeSrc = new EndDevice("src");
        EndDevice nodeDst = new EndDevice("dst");

        PointToPointHelper p2pHelper = new PointToPointHelper(1000, new Time(50, 0));
        Pair<Interface, Interface> interfaces = p2pHelper.install(nodeSrc, nodeDst, new IpAddress("192.168.0.2/24"), new IpAddress("192.168.0.4/24"));

        Interface interfaceSrc = interfaces.first;
        Interface interfaceDst = interfaces.first;

        nodeSrc.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceSrc, new IpAddress("192.168.0.4"));
        nodeDst.getRoutingTable().addEntry(new IpAddress("192.168.0.0/24"), interfaceDst, new IpAddress("192.168.0.2"));

        nodeSrc.getArpTable().addEntry(new IpAddress("192.168.0.4"), interfaceDst.getMacAddress());
        nodeDst.getArpTable().addEntry(new IpAddress("192.168.0.2"), interfaceSrc.getMacAddress());

        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(1000, 0));

        Packet packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());

        Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.4"));
        Simulator.getInstance().run();

        assertEquals(new Time(51, 0), Simulator.getInstance().getCurrentTime());
    }

    @Test
    public void testSendFivePackets() {
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
        for (int i = 0; i < 5; i++) {
            packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());
            Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));
        }

        Simulator.getInstance().run();

        assertEquals(new Time(55, 0), Simulator.getInstance().getCurrentTime());
    }

    @Test
    public void testSendFivePacketsLater() {
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
        for (int i = 0; i < 5; i++) {
            packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());
            Simulator.getInstance().schedule(new Time(10, 1000), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));
        }
        packet = new Packet(500 - new MacHeader().getSize() - new IpHeader().getSize());
        Simulator.getInstance().schedule(new Time(10, 1000), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));

        Simulator.getInstance().run();

        assertEquals(new Time(65, 1000 + 500000000), Simulator.getInstance().getCurrentTime());
    }

    @Test
    public void testEnqueDuringTransmission() {
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

        Packet packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());
        Simulator.getInstance().schedule(new Time(0, 0), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));

        packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());
        Simulator.getInstance().schedule(new Time(0, 500000000), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));

        Simulator.getInstance().run();

        assertEquals(new Time(52, 0), Simulator.getInstance().getCurrentTime());
    }

    @Test
    public void testQueueFull() {
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

        interfaceSrc.setQueueSizeMaxPackets(10);

        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(1000, 0));

        Packet packet;
        for (int i = 0; i < 20; i++) {
            packet = new Packet(1000 - new MacHeader().getSize() - new IpHeader().getSize());
            Simulator.getInstance().schedule(new Time(0, 0), nodeSrc, SchedulableMethod.END_DEVICE__SEND, packet, new IpAddress("192.168.0.2"));
        }

        Simulator.getInstance().run();

        assertEquals(new Time(61, 0), Simulator.getInstance().getCurrentTime());
    }
}
