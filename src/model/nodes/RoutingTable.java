package model.nodes;

import java.util.HashMap;

import model.network.IpAddress;
import model.utils.Pair;

/**
 * Class representing a routing table.
 * Each node must have such table.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Node
 * @see Interface
 * @see IpAddress
 */
public class RoutingTable {
    /**
     * Routing table. Format is [network -> (Interface, nextHop)]
     */
    private HashMap<IpAddress, Pair<Interface, IpAddress>> table;

    /**
     * Construct a new routing table
     */
    public RoutingTable() {
        this.table = new HashMap<IpAddress, Pair<Interface, IpAddress>>();
    }

    /**
     * Add a new entry in the routing table.
     * Raise an exception if destination network is already in the map
     * 
     * @param network The destination network
     * @param inter   The interface to send packet
     * @param nextHop Next hop IP address
     */
    public void addEntry(IpAddress network, Interface inter, IpAddress nextHop) {
        if (this.table.containsKey(network)) {
            throw new IllegalArgumentException("Network " + network + "is already in routing table");
        }
        this.table.put(network, new Pair<Interface, IpAddress>(inter, nextHop));
    }

    /**
     * Update entry in the routing table.
     * Raise an exception if destination network is not in the map
     * 
     * @param network The destination network
     * @param inter   The interface to send packet
     * @param nextHop Next hop IP address
     */
    public void updateEntry(IpAddress network, Interface inter, IpAddress nextHop) {
        if (!this.table.containsKey(network)) {
            throw new IllegalArgumentException("Network " + network + "is not in routing table");
        }
        this.table.put(network, new Pair<Interface, IpAddress>(inter, nextHop));
    }

    /**
     * Get the pair [Interface, nextHop] corresponding to the network
     * 
     * @param network The destination network
     * @return The pair Interface/nextHop, or null if no such key
     */
    public Pair<Interface, IpAddress> getEntry(IpAddress network) {
        return this.table.get(network);
    }

    /**
     * Indicates if IP address is in this table
     * 
     * @param network The destination network
     * @return True if IP address is in table, False otherwise
     */
    public boolean hasEntry(IpAddress network) {
        return this.table.containsKey(network);
    }
}
