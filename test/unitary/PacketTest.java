package unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.network.Header;
import model.network.IpAddress;
import model.network.IpHeader;
import model.network.MacAddress;
import model.network.MacHeader;
import model.network.Packet;

public class PacketTest {

   @Test
   public void testEmptyConstructor() {
      Packet packet = new Packet();
      assertEquals(packet.getPayloadSizeBytes(), 0);
      assertEquals(packet.getTotalSizeBytes(), 0);
      assertEquals(packet.getPayload(), "");
   }

   @Test
   public void testConstructorSize() {
      Packet packet = new Packet(1500);
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1500);
      assertEquals(packet.getPayload(), "");
   }

   @Test
   public void testConstructorPayload() {
      Packet packet = new Packet("dummy payload", 1500);
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1500);
      assertEquals(packet.getPayload(), "dummy payload");
   }

   @Test
   public void testHeaders() {
      Packet packet = new Packet("dummy payload", 1500);
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1500);
      assertEquals(packet.getPayload(), "dummy payload");
      String sol = "Packet: ";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1500]";
      assertEquals(packet.toString(), sol);

      IpHeader ipHeader = new IpHeader(new IpAddress("192.168.1.0/24"), new IpAddress("192.168.2.2/24"));
      packet.addHeader(ipHeader);
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1508);
      assertEquals(packet.getPayload(), "dummy payload");
      sol = "Packet: ";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1508]";
      assertEquals(packet.toString(), sol);

      MacHeader macHeader = new MacHeader(new MacAddress("00:11:22:33:44:55"), new MacAddress("AA:BB:CC:DD:EE:FF"));
      packet.addHeader(macHeader);
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1520);
      assertEquals(packet.getPayload(), "dummy payload");
      sol = "Packet: ";
      sol += "[MAC header: source=00:11:22:33:44:55, destination=AA:BB:CC:DD:EE:FF]";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1520]";
      assertEquals(packet.toString(), sol);

      Header header = packet.peekHeader();
      assertTrue(header instanceof MacHeader);
      MacHeader macHeaderPeek = (MacHeader) header;
      assertEquals(macHeaderPeek.getSource().toString(), "00:11:22:33:44:55");
      assertEquals(macHeaderPeek.getDestination().toString(), "AA:BB:CC:DD:EE:FF");
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1520);
      assertEquals(packet.getPayload(), "dummy payload");
      sol = "Packet: ";
      sol += "[MAC header: source=00:11:22:33:44:55, destination=AA:BB:CC:DD:EE:FF]";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1520]";
      assertEquals(packet.toString(), sol);

      header = packet.popHeader();
      assertTrue(header instanceof MacHeader);
      MacHeader macHeaderPop = (MacHeader) header;
      assertEquals(macHeaderPop.getSource().toString(), "00:11:22:33:44:55");
      assertEquals(macHeaderPop.getDestination().toString(), "AA:BB:CC:DD:EE:FF");
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1508);
      assertEquals(packet.getPayload(), "dummy payload");
      sol = "Packet: ";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1508]";
      assertEquals(packet.toString(), sol);

      header = packet.popHeader();
      assertTrue(header instanceof IpHeader);
      IpHeader ipHeaderPop = (IpHeader) header;
      assertEquals(ipHeaderPop.getSource().toString(), "192.168.1.0/32");
      assertEquals(ipHeaderPop.getDestination().toString(), "192.168.2.2/32");
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1500);
      assertEquals(packet.getPayload(), "dummy payload");
      sol = "Packet: ";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1500]";
      assertEquals(packet.toString(), sol);

      header = packet.popHeader();
      assertNull(header);
      assertEquals(packet.getPayloadSizeBytes(), 1500);
      assertEquals(packet.getTotalSizeBytes(), 1500);
      assertEquals(packet.getPayload(), "dummy payload");
      sol = "Packet: ";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1500]";
      assertEquals(packet.toString(), sol);
   }
}
