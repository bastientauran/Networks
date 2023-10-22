package test.unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.network.IpAddress;

public class IpAddressTest {

   @Test
   public void testEmptyConstructor() {
      IpAddress address = new IpAddress();
      assertEquals("0.0.0.0/32", address.toString());
   }

   @Test
   public void testConstructorWithArray() {
      IpAddress address = new IpAddress(new int[] { 192, 168, 0, 1 }, 16);
      assertEquals("192.168.0.1/16", address.toString());
   }

   @Test
   public void testConstructorWithString() {
      IpAddress address = new IpAddress("10.10.16.65/24");
      assertEquals("10.10.16.65/24", address.toString());
   }

   @Test
   public void testConstructorCopyNewMask() {
      IpAddress address = new IpAddress("10.10.16.65/24");
      assertEquals("10.10.16.65/30", new IpAddress(address, 30).toString());
   }

   @Test
   public void testEquals() {
      IpAddress address1 = new IpAddress(new int[] { 192, 168, 0, 1 }, 20);
      IpAddress address2 = new IpAddress("192.168.0.1/20");
      assertEquals(address1, address2);
   }

   @Test
   public void testToStringNoMask() {
      IpAddress address = new IpAddress("192.168.0.1/14");
      assertEquals("192.168.0.1", address.toStringNoMask());

      address = new IpAddress("192.168.100.192/14");
      assertEquals("192.168.100.192", address.toStringNoMask());
   }

   @Test
   public void testNetworks() {
      IpAddress address = new IpAddress("192.168.100.1/24");
      assertEquals("192.168.100.0/24", address.getNetwork().toString());

      address = new IpAddress("192.168.100.1/16");
      assertEquals("192.168.0.0/16", address.getNetwork().toString());

      address = new IpAddress("192.168.100.1/32");
      assertEquals("192.168.100.1/32", address.getNetwork().toString());

      address = new IpAddress("192.168.100.1/0");
      assertEquals("0.0.0.0/0", address.getNetwork().toString());

      address = new IpAddress("192.168.100.1/17");
      assertEquals("192.168.0.0/17", address.getNetwork().toString());

      address = new IpAddress("192.168.100.1/18");
      assertEquals("192.168.64.0/18", address.getNetwork().toString());

      address = new IpAddress("192.168.100.1/20");
      assertEquals("192.168.96.0/20", address.getNetwork().toString());

      address = new IpAddress("192.168.100.1/23");
      assertEquals("192.168.100.0/23", address.getNetwork().toString());
   }

   @Test
   public void testIsInNetwork() {
      IpAddress address = new IpAddress("192.168.100.1/32");
      IpAddress network = new IpAddress("192.168.100.0/24");
      assertTrue(address.isInNetwork(network));

      address = new IpAddress("192.168.100.1/32");
      network = new IpAddress("192.168.109.0/24");
      assertFalse(address.isInNetwork(network));

      address = new IpAddress("192.168.111.190/24");
      network = new IpAddress("192.168.100.0/18");
      assertTrue(address.isInNetwork(network));

      address = new IpAddress("1.2.3.4/24");
      network = new IpAddress("0.0.0.0/0");
      assertTrue(address.isInNetwork(network));
   }

   @Test
   public void testIncorrectValue() {
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress(new int[] { 192, 300, 0, 1 }, 14));
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress(new int[] { 192, -8, 0, 1 }, 19));
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress(new int[] { 192, 168, 0, 1 }, -2));
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress(new int[] { 192, 168, 0, 1 }, 35));
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress("192.168.0.4/50"));
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress("192.280.0.4/24"));
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress("192.-1.0.4/24"));
      assertThrows(
            IllegalArgumentException.class,
            () -> new IpAddress("something"));
   }
}
