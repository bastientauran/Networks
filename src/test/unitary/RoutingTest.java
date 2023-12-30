package test.unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.link.PointToPointLink;
import model.network.IpAddress;
import model.node.Interface;
import model.node.RoutingTable;

public class RoutingTest extends GenericTest {

    @Test
    public void testWrongEntry() {
        RoutingTable table = new RoutingTable();
        Interface i = new Interface("", null, new PointToPointLink());
        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        IpAddress nextHop = new IpAddress("192.168.1.0/24");

        assertThrows(
                RuntimeException.class,
                () -> table.addEntry(ipAddress, i, nextHop));
    }

    @Test
    public void testOneEntry() {
        RoutingTable table = new RoutingTable();
        Interface i = new Interface("", null, new PointToPointLink());
        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        IpAddress nextHop = new IpAddress("192.168.1.1/32");

        table.addEntry(ipAddress, i, nextHop);
        assertTrue(table.hasEntry(ipAddress));
        assertThrows(
                RuntimeException.class,
                () -> table.addEntry(ipAddress, i, nextHop));
        assertTrue(table.hasEntry(ipAddress));
        assertTrue(table.hasEntry(new IpAddress("192.168.100.0/24")));
        assertTrue(table.hasEntry(new IpAddress("192.168.100.1/32")));
        assertNotNull(table.getEntry(new IpAddress("192.168.100.0/24")));
        assertNotNull(table.getEntry(new IpAddress("192.168.100.1/32")));
        assertNull(table.getEntry(new IpAddress("192.168.101.1/32")));
    }

    @Test
    public void testUpdateEntry() {
        RoutingTable table = new RoutingTable();
        Interface i = new Interface("", null, new PointToPointLink());
        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        IpAddress nextHop = new IpAddress("192.168.1.1/32");
        IpAddress nextHop2 = new IpAddress("192.168.2.1/32");

        assertThrows(
                RuntimeException.class,
                () -> table.updateEntry(ipAddress, i, nextHop));

        assertFalse(table.hasEntry(ipAddress));
        table.addEntry(ipAddress, i, nextHop);
        assertTrue(table.hasEntry(ipAddress));
        table.updateEntry(ipAddress, i, nextHop2);
        assertTrue(table.hasEntry(ipAddress));
        assertNotNull(table.getEntry(ipAddress));
    }

    @Test
    public void testSeveralEntryCandidates() {
        RoutingTable table = new RoutingTable();
        Interface i = new Interface("", null, new PointToPointLink());

        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        IpAddress nextHop = new IpAddress("192.168.1.1/32");

        IpAddress ipAddress2 = new IpAddress("192.168.0.0/16");
        IpAddress nextHop2 = new IpAddress("192.168.2.1/32");

        IpAddress ipAddressDefault = new IpAddress("0.0.0.0/0");
        IpAddress nextHopDefault = new IpAddress("192.168.3.1/32");

        assertFalse(table.hasEntry(ipAddress));
        assertFalse(table.hasEntry(ipAddress2));
        assertFalse(table.hasEntry(ipAddressDefault));
        table.addEntry(ipAddress, i, nextHop);
        table.addEntry(ipAddressDefault, i, nextHopDefault);
        table.addEntry(ipAddress2, i, nextHop2);
        assertTrue(table.hasEntry(ipAddress));
        assertTrue(table.hasEntry(ipAddress2));
        assertTrue(table.hasEntry(ipAddressDefault));

        assertEquals(nextHop, table.getEntry(new IpAddress("192.168.100.1")).second);
        assertEquals(nextHop2, table.getEntry(new IpAddress("192.168.0.1")).second);
        assertEquals(nextHopDefault, table.getEntry(new IpAddress("10.10.0.1")).second);

        table.deleteEntry(ipAddress2);
        assertTrue(table.hasEntry(ipAddress));
        assertTrue(table.hasEntry(ipAddress2));
        assertTrue(table.hasEntry(ipAddressDefault));

        assertEquals(nextHop, table.getEntry(new IpAddress("192.168.100.1")).second);
        assertEquals(nextHopDefault, table.getEntry(new IpAddress("192.168.0.1")).second);
        assertEquals(nextHopDefault, table.getEntry(new IpAddress("10.10.0.1")).second);

        table.flush();
        assertFalse(table.hasEntry(ipAddress));
        assertFalse(table.hasEntry(ipAddress2));
        assertFalse(table.hasEntry(ipAddressDefault));
    }
}