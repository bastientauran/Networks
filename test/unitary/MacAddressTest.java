package unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import model.network.MacAddress;

public class MacAddressTest {

   @Test
   public void testEmptyConstructor() {
      MacAddress address = new MacAddress();
      assertEquals("00:00:00:00:00:00", address.toString());
   }

   @Test
   public void testConstructorWithArray() {
      MacAddress address = new MacAddress(new int[] { 0, 5, 6, 22, 7, 15 });
      assertEquals("00:05:06:16:07:0F", address.toString());
   }

   @Test
   public void testConstructorWithString() {
      MacAddress address = new MacAddress("AA:BB:1E:F6:00:19");
      assertEquals("AA:BB:1E:F6:00:19", address.toString());
   }

   @Test
   public void testEquals() {
      MacAddress address = new MacAddress(new int[] { 0, 5, 6, 22, 7, 14 });
      assertThrows(
            IllegalStateException.class,
            () -> new MacAddress("00:05:06:16:07:0E"));
      assertEquals("00:05:06:16:07:0E", address.toString());
   }

   @Test
   public void testIncorrectValue() {
      assertThrows(
            IllegalArgumentException.class,
            () -> new MacAddress(new int[] { -1, 5, 6, 22, 7, 15 }));
      assertThrows(
            IllegalArgumentException.class,
            () -> new MacAddress(new int[] { 256, 5, 6, 22, 7, 15 }));
      assertThrows(
            IllegalArgumentException.class,
            () -> new MacAddress("AA:BB:CC:DD:EE:GG"));
      assertThrows(
            IllegalArgumentException.class,
            () -> new MacAddress("AA:BB:CC:DD:EE:-1"));
      assertThrows(
            IllegalArgumentException.class,
            () -> new MacAddress("something"));
   }
}
