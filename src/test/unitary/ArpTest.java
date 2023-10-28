package test.unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.network.IpAddress;
import model.network.MacAddress;
import model.node.ArpTable;

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
        assertTrue(table.hasEntry(ipAddress));
        assertEquals(macAddress, table.getEntry(ipAddress));
    }

    @Test
    public void testNoEntry() {
        ArpTable table = new ArpTable();
        IpAddress ipAddress = new IpAddress("192.168.100.2/32");
        IpAddress ipAddress2 = new IpAddress("192.168.100.3/32");
        MacAddress macAddress = new MacAddress("11:22:33:44:55:72");
        assertFalse(table.hasEntry(ipAddress));
        table.addEntry(ipAddress, macAddress);
        assertTrue(table.hasEntry(ipAddress));
        assertFalse(table.hasEntry(ipAddress2));
        assertEquals(macAddress, table.getEntry(ipAddress));
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
        assertTrue(table.hasEntry(ipAddress));
        assertEquals(macAddress, table.getEntry(ipAddress));
        table.updateEntry(ipAddress, macAddress2);
        assertTrue(table.hasEntry(ipAddress));
        assertEquals(macAddress2, table.getEntry(ipAddress));
    }

    @Test
    public void testSeveralEntries() {
        ArpTable table = new ArpTable();
        IpAddress ipAddress = new IpAddress("192.168.100.2/32");
        IpAddress ipAddress2 = new IpAddress("192.168.101.2/32");
        MacAddress macAddress = new MacAddress("11:22:33:44:55:75");
        MacAddress macAddress2 = new MacAddress("11:22:33:44:55:76");

        table.addEntry(ipAddress, macAddress);
        table.addEntry(ipAddress2, macAddress2);
        assertTrue(table.hasEntry(ipAddress));
        assertTrue(table.hasEntry(ipAddress2));
        assertEquals(macAddress, table.getEntry(ipAddress));
        assertEquals(macAddress2, table.getEntry(ipAddress2));

        table.deleteEntry(ipAddress);
        assertFalse(table.hasEntry(ipAddress));
        assertTrue(table.hasEntry(ipAddress2));
        assertNull(table.getEntry(ipAddress));
        assertEquals(macAddress2, table.getEntry(ipAddress2));

        table.addEntry(ipAddress, macAddress);
        assertTrue(table.hasEntry(ipAddress));
        assertTrue(table.hasEntry(ipAddress2));
        assertEquals(macAddress, table.getEntry(ipAddress));
        assertEquals(macAddress2, table.getEntry(ipAddress2));

        table.flush();
        assertFalse(table.hasEntry(ipAddress));
        assertFalse(table.hasEntry(ipAddress2));
        assertNull(table.getEntry(ipAddress));
        assertNull(table.getEntry(ipAddress2));
    }

}