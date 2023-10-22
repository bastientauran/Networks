package unitary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.network.IpAddress;
import model.network.IpHeader;

public class IpHeaderTest {

   @Test
   public void testEmptyConstructor() {
      IpHeader header = new IpHeader();
      assertEquals("0.0.0.0/32", header.getSource().toString());
      assertEquals("0.0.0.0/32", header.getDestination().toString());
   }

   @Test
   public void testConstructor() {
      IpHeader header = new IpHeader(new IpAddress("192.168.0.1/24"), new IpAddress("192.168.2.4/24"));
      assertEquals("192.168.0.1/32", header.getSource().toString());
      assertEquals("192.168.2.4/32", header.getDestination().toString());
   }

   @Test
   public void testSize() {
      IpHeader header = new IpHeader(new IpAddress("192.168.0.1/24"), new IpAddress("192.168.2.4/24"));
      assertEquals(8, header.getSize());
   }

   @Test
   public void testToString() {
      IpHeader header = new IpHeader(new IpAddress("192.168.0.1/24"), new IpAddress("192.168.2.4/24"));
      assertEquals("[IP header: source=192.168.0.1, destination=192.168.2.4]", header.toString());
   }
}
