package unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import model.network.IpAddress;
import model.network.MacAddress;
import model.nodes.ArpTable;

public class ArpTest {

    @Test
    public void testWrongEntry() {
        ArpTable table = new ArpTable();
        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        MacAddress macAddress = new MacAddress("11:22:33:44:55:70");
        assertThrows(
                IllegalArgumentException.class,
                () -> table.addEntry(ipAddress, macAddress));
    }

    @Test
    public void testOneEntry() {
        ArpTable table = new ArpTable();
        IpAddress ipAddress = new IpAddress("192.168.100.2/32");
        MacAddress macAddress = new MacAddress("11:22:33:44:55:71");
        table.addEntry(ipAddress, macAddress);
        assertEquals(table.hasEntry(ipAddress), true);
        assertEquals(table.getEntry(ipAddress), macAddress);
    }

    @Test
    public void testNoEntry() {
        ArpTable table = new ArpTable();
        IpAddress ipAddress = new IpAddress("192.168.100.2/32");
        IpAddress ipAddress2 = new IpAddress("192.168.100.3/32");
        MacAddress macAddress = new MacAddress("11:22:33:44:55:72");
        assertEquals(table.hasEntry(ipAddress), false);
        table.addEntry(ipAddress, macAddress);
        assertEquals(table.hasEntry(ipAddress), true);
        assertEquals(table.hasEntry(ipAddress2), false);
        assertEquals(table.getEntry(ipAddress), macAddress);
    }

    @Test
    public void testUpdateEntry() {
        ArpTable table = new ArpTable();
        IpAddress ipAddress = new IpAddress("192.168.100.2/32");
        MacAddress macAddress = new MacAddress("11:22:33:44:55:73");
        MacAddress macAddress2 = new MacAddress("11:22:33:44:55:74");
        assertThrows(
                IllegalArgumentException.class,
                () -> table.updateEntry(ipAddress, macAddress));
        table.addEntry(ipAddress, macAddress);
        assertEquals(table.hasEntry(ipAddress), true);
        assertEquals(table.getEntry(ipAddress), macAddress);
        table.updateEntry(ipAddress, macAddress2);
        assertEquals(table.hasEntry(ipAddress), true);
        assertEquals(table.getEntry(ipAddress), macAddress2);
    }

}