package model.nodes;

import model.network.IpAddress;
import model.network.MacAddress;

/**
 * Class representing an interface.
 * 
 * @author Bastien Tauran
 * @version 1.0
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
