package unitary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.network.IpAddress;
import model.network.IpHeader;

public class IpHeaderTest {

   @Test
   public void testEmptyConstructor() {
      IpHeader header = new IpHeader();
      assertEquals(header.getSource().toString(), "0.0.0.0/32");
      assertEquals(header.getDestination().toString(), "0.0.0.0/32");
   }

   @Test
   public void testConstructor() {
      IpHeader header = new IpHeader(new IpAddress("192.168.0.1/24"), new IpAddress("192.168.2.4/24"));
      assertEquals(header.getSource().toString(), "192.168.0.1/32");
      assertEquals(header.getDestination().toString(), "192.168.2.4/32");
   }

   @Test
   public void testSize() {
      IpHeader header = new IpHeader(new IpAddress("192.168.0.1/24"), new IpAddress("192.168.2.4/24"));
      assertEquals(header.getSize(), 8);
   }

   @Test
   public void testToString() {
      IpHeader header = new IpHeader(new IpAddress("192.168.0.1/24"), new IpAddress("192.168.2.4/24"));
      assertEquals(header.toString(), "[IP header: source=192.168.0.1, destination=192.168.2.4]");
   }
}
