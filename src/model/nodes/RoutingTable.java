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
        if (nextHop.getMask() != 32) {
            throw new IllegalArgumentException("Next hop address mask must be 32, but got " + nextHop.getMask());
        }
        IpAddress net = network.getNetwork();
        if (this.table.containsKey(net)) {
            throw new IllegalArgumentException("Network " + net + "is already in routing table");
        }
        this.table.put(net, new Pair<Interface, IpAddress>(inter, nextHop));
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
        if (nextHop.getMask() != 32) {
            throw new IllegalArgumentException("Next hop address mask must be 32, but got " + nextHop.getMask());
        }
        IpAddress net = network.getNetwork();
        if (!this.table.containsKey(net)) {
            throw new IllegalArgumentException("Network " + net + "is not in routing table");
        }
        this.table.put(net, new Pair<Interface, IpAddress>(inter, nextHop));
    }

    /**
     * Get the pair [Interface, nextHop] corresponding to the network
     * 
     * @param network The destination network
     * @return The pair Interface/nextHop, or null if no such key
     */
    public Pair<Interface, IpAddress> getEntry(IpAddress network) {
        IpAddress candidate = null;
        int bestMask = -1;
        for (IpAddress key : this.table.keySet()) {
            if (network.isInNetwork(key)) {
                if (key.getMask() > bestMask) {
                    candidate = key;
                    bestMask = key.getMask();
                }
            }
        }
        return this.table.get(candidate);
    }

    /**
     * Indicates if IP address is in this table
     * 
     * @param network The destination network
     * @return True if IP address is in table, False otherwise
     */
    public boolean hasEntry(IpAddress network) {
        for (IpAddress key : this.table.keySet()) {
            if (network.isInNetwork(key)) {
                return true;
            }
        }
        return false;
    }
}
