package test.unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import model.logger.NetworksCriticalException;
import model.network.MacAddress;

public class MacAddressTest extends GenericTest {

   @Test
   public void testEmptyConstructor() {
      MacAddress address = new MacAddress();
      assertEquals("00:00:00:00:00:00", address.toString());
   }

   @Test
   public void testConstructorWithArray() {
      MacAddress address = new MacAddress(new int[] { 0, 5, 6, 22, 7, 15 }, true);
      assertEquals("00:05:06:16:07:0F", address.toString());
   }

   @Test
   public void testConstructorWithString() {
      MacAddress address = new MacAddress("AA:BB:1E:F6:00:19", true);
      assertEquals("AA:BB:1E:F6:00:19", address.toString());
   }

   @Test
   public void testEquals() {
      MacAddress address = new MacAddress(new int[] { 0, 5, 6, 22, 7, 14 }, true);
      assertThrows(
            NetworksCriticalException.class,
            () -> new MacAddress("00:05:06:16:07:0E", true));
      assertEquals("00:05:06:16:07:0E", address.toString());
   }

   @Test
   public void testIncorrectValue() {
      assertThrows(
            RuntimeException.class,
            () -> new MacAddress(new int[] { -1, 5, 6, 22, 7, 15 }, true));
      assertThrows(
            RuntimeException.class,
            () -> new MacAddress(new int[] { 256, 5, 6, 22, 7, 15 }, true));
      assertThrows(
            RuntimeException.class,
            () -> new MacAddress("AA:BB:CC:DD:EE:GG", true));
      assertThrows(
            RuntimeException.class,
            () -> new MacAddress("AA:BB:CC:DD:EE:-1", true));
      assertThrows(
            RuntimeException.class,
            () -> new MacAddress("something", true));
   }
}
