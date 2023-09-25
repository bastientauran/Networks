package model.nodes;

import model.network.IpAddress;
import model.network.MacAddress;
import model.network.MacAddressContainer;

/**
 * Class representing an interface.
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class Interface {

    /**
     * Name of this interface
     */
    private String name;

    /**
     * MAC address of this interface
     */
    private MacAddress macAddress;

    /**
     * IP address of this interface
     */
    private IpAddress ipAddress;

    /**
     * Create a new interface
     * 
     * @param name Name of the interface
     */
    public Interface(String name) {
        this.name = name;
        this.macAddress = MacAddressContainer.getInstance().getNewMacAddress();
        this.ipAddress = new IpAddress();
    }


    @Override
    public String toString() {
        return "[ Interface " + this.name + ", macAddress='" + this.macAddress + "', ipAddress='" + this.ipAddress + "']";
    }

}
