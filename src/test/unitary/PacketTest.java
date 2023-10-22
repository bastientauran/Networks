package test.unitary;

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
      assertEquals(0, packet.getPayloadSizeBytes());
      assertEquals(0, packet.getTotalSizeBytes());
      assertEquals("", packet.getPayload());
   }

   @Test
   public void testConstructorSize() {
      Packet packet = new Packet(1500);
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1500, packet.getTotalSizeBytes());
      assertEquals("", packet.getPayload());
   }

   @Test
   public void testConstructorPayload() {
      Packet packet = new Packet("dummy payload", 1500);
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1500, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
   }

   @Test
   public void testHeaders() {
      Packet packet = new Packet("dummy payload", 1500);
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1500, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
      String sol = "Packet: ";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1500]";
      assertEquals(sol, packet.toString());

      IpHeader ipHeader = new IpHeader(new IpAddress("192.168.1.0/24"), new IpAddress("192.168.2.2/24"));
      packet.addHeader(ipHeader);
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1508, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
      sol = "Packet: ";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1508]";
      assertEquals(sol, packet.toString());

      MacHeader macHeader = new MacHeader(new MacAddress("00:11:22:33:44:55"), new MacAddress("AA:BB:CC:DD:EE:FF"));
      packet.addHeader(macHeader);
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1520, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
      sol = "Packet: ";
      sol += "[MAC header: source=00:11:22:33:44:55, destination=AA:BB:CC:DD:EE:FF]";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1520]";
      assertEquals(sol, packet.toString());

      Header header = packet.peekHeader();
      assertTrue(header instanceof MacHeader);
      MacHeader macHeaderPeek = (MacHeader) header;
      assertEquals("00:11:22:33:44:55", macHeaderPeek.getSource().toString());
      assertEquals("AA:BB:CC:DD:EE:FF", macHeaderPeek.getDestination().toString());
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1520, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
      sol = "Packet: ";
      sol += "[MAC header: source=00:11:22:33:44:55, destination=AA:BB:CC:DD:EE:FF]";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1520]";
      assertEquals(sol, packet.toString());

      header = packet.popHeader();
      assertTrue(header instanceof MacHeader);
      MacHeader macHeaderPop = (MacHeader) header;
      assertEquals("00:11:22:33:44:55", macHeaderPop.getSource().toString());
      assertEquals("AA:BB:CC:DD:EE:FF", macHeaderPop.getDestination().toString());
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1508, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
      sol = "Packet: ";
      sol += "[IP header: source=192.168.1.0, destination=192.168.2.2]";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1508]";
      assertEquals(sol, packet.toString());

      header = packet.popHeader();
      assertTrue(header instanceof IpHeader);
      IpHeader ipHeaderPop = (IpHeader) header;
      assertEquals("192.168.1.0/32", ipHeaderPop.getSource().toString());
      assertEquals("192.168.2.2/32", ipHeaderPop.getDestination().toString());
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1500, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
      sol = "Packet: ";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1500]";
      assertEquals(sol, packet.toString());

      header = packet.popHeader();
      assertNull(header);
      assertEquals(1500, packet.getPayloadSizeBytes());
      assertEquals(1500, packet.getTotalSizeBytes());
      assertEquals("dummy payload", packet.getPayload());
      sol = "Packet: ";
      sol += "[payload='dummy payload', payloadSize=1500, totalSize=1500]";
      assertEquals(sol, packet.toString());
   }
}
