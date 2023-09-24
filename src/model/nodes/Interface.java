package model.nodes;

import model.network.IpAddress;
import model.network.MacAddress;

/**
 * Class representing an interface.
 */
public class Interface {

    /**
     * MAC address of this interface
     */
    private MacAddress macAddress;

    /**
     * IP address of this interface
     */
    private IpAddress ipAddress;
}
