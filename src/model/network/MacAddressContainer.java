package model.network;

import java.util.TreeSet;

/**
 * Container to store all the MAC addresses existing.
 * It uses Singleton design pattern.
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
    private MacAddressContainer()
    {
        this.addresses = new TreeSet<MacAddress>();
    }

    /**
     * Get the instance of MacAddressContainer. Construct it if not already created.
     * @return The instance of MacAddressContainer
     */
    public static MacAddressContainer getInstance() 
    {
        if(instance == null) {
            instance = new MacAddressContainer();
        }
        return instance;
    }

    public MacAddress getNewMacAddress() {
        MacAddress address = null;
        do {
            int[] values = new int[6];
            for(int i = 0; i < 6; i++) {
                values[i] = (int) (256*Math.random());
            }
            address = new MacAddress(values);
        } while(this.addresses.contains(address));

        addresses.add(address);

        return address;
    }
}
