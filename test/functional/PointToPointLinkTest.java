package functional;

import org.junit.Test;

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

      Packet packet = new Packet(1000 - new MacHeader().getSize() -  - new IpHeader().getSize());
      IpHeader ipHeader = new IpHeader(new IpAddress("192.168.0.1"), new IpAddress("192.168.0.2"));
      packet.addHeader(ipHeader);

      Simulator.getInstance().reset();
      Simulator.getInstance().setStopTime(new Time(1000, 0));
      Simulator.getInstance().schedule(new Time(), nodeSrc, SchedulableMethod.END_DEVICE_SEND, packet);
      Simulator.getInstance().run();
   }
}
