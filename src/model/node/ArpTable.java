package model.node;

import java.util.TreeMap;

import model.network.IpAddress;
import model.network.MacAddress;

/**
 * Class representing an ARP table.
 * Each node must have an ARP table.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Node
 * @see MacAddress
 * @see IpAddress
 */
public class ArpTable {
    /**
     * ARP table
     */
    private TreeMap<IpAddress, MacAddress> table;

    /**
     * Construct a new APR table
     */
    public ArpTable() {
        this.table = new TreeMap<IpAddress, MacAddress>();
    }

    /**
     * Add a new entry in the ARP table.
     * Raise an exception if IP address is already in the map
     * 
     * @param ipAddress  The IP address
     * @param macAddress Corresponding MAC address
     */
    public void addEntry(IpAddress ipAddress, MacAddress macAddress) {
        if (ipAddress.getMask() != 32) {
            throw new IllegalArgumentException("IP address mask must be 32, but got " + ipAddress.getMask());
        }
        if (this.table.containsKey(ipAddress)) {
            throw new IllegalArgumentException("IP adress " + ipAddress + "is already in ARP table");
        }
        this.table.put(ipAddress, macAddress);
    }

    /**
     * Update entry in the ARP table.
     * Raise an exception if IP address is not in the map
     * 
     * @param ipAddress  The IP address
     * @param macAddress Corresponding MAC address
     */
    public void updateEntry(IpAddress ipAddress, MacAddress macAddress) {
        if (ipAddress.getMask() != 32) {
            throw new IllegalArgumentException("IP address mask must be 32, but got " + ipAddress.getMask());
        }
        if (!this.table.containsKey(ipAddress)) {
            throw new IllegalArgumentException("IP adress " + ipAddress + "is not in ARP table");
        }
        this.table.put(ipAddress, macAddress);
    }

    /**
     * Remove an entry from the table. It must be in the table.
     * 
     * @param ipAddress The IP destination to remove
     */
    public void deleteEntry(IpAddress ipAddress) {
        if (ipAddress.getMask() != 32) {
            throw new IllegalArgumentException("IP address mask must be 32, but got " + ipAddress.getMask());
        }
        if (!this.table.containsKey(ipAddress)) {
            throw new IllegalArgumentException("IP adress " + ipAddress + "is not in ARP table");
        }
        this.table.remove(ipAddress);
    }

    /**
     * Remove all the entries from the table
     */
    public void flush() {
        this.table.clear();
    }

    /**
     * Get the MAC address corresponding to the IP address
     * 
     * @param ipAddress The IP address
     * @return The associated MAC address, or null if no such key
     */
    public MacAddress getEntry(IpAddress ipAddress) {
        if (ipAddress.getMask() != 32) {
            throw new IllegalArgumentException("IP address mask must be 32, but got " + ipAddress.getMask());
        }
        return this.table.get(ipAddress);
    }

    /**
     * Indicates if IP address is in this table
     * 
     * @param ipAddress The IP address to check
     * @return True if IP address is in table, False otherwise
     */
    public boolean hasEntry(IpAddress ipAddress) {
        if (ipAddress.getMask() != 32) {
            throw new IllegalArgumentException("IP address mask must be 32, but got " + ipAddress.getMask());
        }
        return this.table.containsKey(ipAddress);
    }
}
