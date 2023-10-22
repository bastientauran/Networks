package unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import model.link.PointToPointLink;
import model.network.IpAddress;
import model.nodes.Interface;
import model.nodes.RoutingTable;

public class RoutingTest {

    @Test
    public void testWrongEntry() {
        RoutingTable table = new RoutingTable();
        Interface i = new Interface("", null, new PointToPointLink());
        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        IpAddress nextHop = new IpAddress("192.168.1.0/24");

        assertThrows(
                IllegalArgumentException.class,
                () -> table.addEntry(ipAddress, i, nextHop));
    }

    @Test
    public void testOneEntry() {
        RoutingTable table = new RoutingTable();
        Interface i = new Interface("", null, new PointToPointLink());
        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        IpAddress nextHop = new IpAddress("192.168.1.1/32");

        table.addEntry(ipAddress, i, nextHop);
        assertEquals(table.hasEntry(ipAddress), true);
        assertThrows(
                IllegalArgumentException.class,
                () -> table.addEntry(ipAddress, i, nextHop));
        assertEquals(table.hasEntry(ipAddress), true);
        assertEquals(table.hasEntry(new IpAddress("192.168.100.0/24")), true);
        assertEquals(table.hasEntry(new IpAddress("192.168.100.1/32")), true);
        assertNotEquals(table.getEntry(new IpAddress("192.168.100.0/24")), null);
        assertNotEquals(table.getEntry(new IpAddress("192.168.100.1/32")), null);
        assertEquals(table.getEntry(new IpAddress("192.168.101.1/32")), null);
    }

    @Test
    public void testUpdateEntry() {
        RoutingTable table = new RoutingTable();
        Interface i = new Interface("", null, new PointToPointLink());
        IpAddress ipAddress = new IpAddress("192.168.100.0/24");
        IpAddress nextHop = new IpAddress("192.168.1.1/32");
        IpAddress nextHop2 = new IpAddress("192.168.2.1/32");

        assertThrows(
                IllegalArgumentException.class,
                () -> table.updateEntry(ipAddress, i, nextHop));

        assertEquals(table.hasEntry(ipAddress), false);
        table.addEntry(ipAddress, i, nextHop);
        assertEquals(table.hasEntry(ipAddress), true);
        table.updateEntry(ipAddress, i, nextHop2);
        assertEquals(table.hasEntry(ipAddress), true);
        assertNotEquals(table.getEntry(ipAddress), null);
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

        assertEquals(table.hasEntry(ipAddress), false);
        assertEquals(table.hasEntry(ipAddress2), false);
        assertEquals(table.hasEntry(ipAddressDefault), false);
        table.addEntry(ipAddress, i, nextHop);
        table.addEntry(ipAddressDefault, i, nextHopDefault);
        table.addEntry(ipAddress2, i, nextHop2);
        assertEquals(table.hasEntry(ipAddress), true);
        assertEquals(table.hasEntry(ipAddress2), true);
        assertEquals(table.hasEntry(ipAddressDefault), true);

        assertEquals(table.getEntry(new IpAddress("192.168.100.1")).second, nextHop);
        assertEquals(table.getEntry(new IpAddress("192.168.0.1")).second, nextHop2);
        assertEquals(table.getEntry(new IpAddress("10.10.0.1")).second, nextHopDefault);

        table.deleteEntry(ipAddress2);
        assertEquals(table.hasEntry(ipAddress), true);
        assertEquals(table.hasEntry(ipAddress2), true);
        assertEquals(table.hasEntry(ipAddressDefault), true);

        assertEquals(table.getEntry(new IpAddress("192.168.100.1")).second, nextHop);
        assertEquals(table.getEntry(new IpAddress("192.168.0.1")).second, nextHopDefault);
        assertEquals(table.getEntry(new IpAddress("10.10.0.1")).second, nextHopDefault);

        table.flush();
        assertEquals(table.hasEntry(ipAddress), false);
        assertEquals(table.hasEntry(ipAddress2), false);
        assertEquals(table.hasEntry(ipAddressDefault), false);
    }
}