package test.unitary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.network.MacAddress;
import model.network.MacHeader;

public class MacHeaderTest extends GenericTest {

   @Test
   public void testEmptyConstructor() {
      MacHeader header = new MacHeader();
      assertEquals("00:00:00:00:00:00", header.getSource().toString());
      assertEquals("00:00:00:00:00:00", header.getDestination().toString());
   }

   @Test
   public void testConstructor() {
      MacHeader header = new MacHeader(new MacAddress("AA:BB:CC:DD:2E:F0", true), new MacAddress("22:14:87:65:A5:9D", true));
      assertEquals("AA:BB:CC:DD:2E:F0", header.getSource().toString());
      assertEquals("22:14:87:65:A5:9D", header.getDestination().toString());
   }

   @Test
   public void testSize() {
      MacHeader header = new MacHeader(new MacAddress("AA:BB:CC:DD:1E:F0", true), new MacAddress("22:11:87:65:A5:9D", true));
      assertEquals(12, header.getSize());
   }

   @Test
   public void testToString() {
      MacHeader header = new MacHeader(new MacAddress("AA:BB:CC:DD:1E:F1", true), new MacAddress("22:11:87:65:A5:7D", true));
      assertEquals("[MAC header: source=AA:BB:CC:DD:1E:F1, destination=22:11:87:65:A5:7D]", header.toString());
   }
}
