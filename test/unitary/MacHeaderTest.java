package unitary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.network.MacAddress;
import model.network.MacHeader;

public class MacHeaderTest {

   @Test
   public void testEmptyConstructor() {
      MacHeader header = new MacHeader();
      assertEquals(header.getSource().toString(), "00:00:00:00:00:00");
      assertEquals(header.getDestination().toString(), "00:00:00:00:00:00");
   }

   @Test
   public void testConstructor() {
      MacHeader header = new MacHeader(new MacAddress("AA:BB:CC:DD:1E:F0"), new MacAddress("22:11:87:65:A5:9D"));
      assertEquals(header.getSource().toString(), "AA:BB:CC:DD:1E:F0");
      assertEquals(header.getDestination().toString(), "22:11:87:65:A5:9D");
   }

   @Test
   public void testSize() {
      MacHeader header = new MacHeader(new MacAddress("AA:BB:CC:DD:1E:F0"), new MacAddress("22:11:87:65:A5:9D"));
      assertEquals(header.getSize(), 12);
   }

   @Test
   public void testToString() {
      MacHeader header = new MacHeader(new MacAddress("AA:BB:CC:DD:1E:F0"), new MacAddress("22:11:87:65:A5:9D"));
      assertEquals(header.toString(), "[MAC header: source=AA:BB:CC:DD:1E:F0, destination=22:11:87:65:A5:9D]");
   }
}
