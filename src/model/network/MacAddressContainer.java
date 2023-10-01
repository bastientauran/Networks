package model.network;

import java.util.TreeSet;

/**
 * Container to store all the MAC addresses existing.
 * It uses Singleton design pattern.
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class MacAddressContainer {

    /**
     * List of already generated MAC addresses
     */
    private TreeSet<MacAddress> addresses;

    /**
     * Instance of MacAddressContainer
     */
    private static MacAddressContainer instance;

    /**
     * Private constructor
     */
    private MacAddressContainer() {
        this.addresses = new TreeSet<MacAddress>();
    }

    /**
     * Get the instance of MacAddressContainer. Construct it if not already created.
     * 
     * @return The instance of MacAddressContainer
     */
    public static MacAddressContainer getInstance() {
        if (instance == null) {
            instance = new MacAddressContainer();
        }
        return instance;
    }

    /**
     * Get a new available random MAC address
     * 
     * @return The address generated
     */
    public MacAddress getNewMacAddress() {
        MacAddress address = null;
        do {
            int[] values = new int[6];
            for (int i = 0; i < 6; i++) {
                values[i] = (int) (256 * Math.random());
            }
            try {
                address = new MacAddress(values);
                break;
            } catch (IllegalStateException e) {
                continue;
            }
        } while (true);

        this.addresses.add(address);

        return address;
    }

    /**
     * Add a new MAC address to the container
     * 
     * @param address The MAC address to add
     * @return True if the address was added, false if already in container
     */
    public boolean addMacAddress(MacAddress address) {
        if (this.addresses.contains(address)) {
            return false;
        }
        this.addresses.add(address);

        return true;
    }

    /**
     * Remove new MAC address to the container
     * 
     * @param address The MAC address to remove
     * @return True if the address was remove, false if address is not found in
     *         container
     */
    public boolean removeMacAddress(MacAddress address) {
        return this.addresses.remove(address);
    }
}
