package model.network;

/**
 * This class represents a MAC address
 */
public class MacAddress {

    /**
     * Array of 6 bytes representing a MAC address
     */
    private int[] address;

    /**
     * Default MAC address constructor
     */
    public MacAddress() {
        this.address = new int[6];
    }

    /**
     * Mac address constructor
     * @param address The address to use
     */
    public MacAddress(int[] address) {
        if (address.length != 6) {
            throw new IllegalArgumentException("Mac address constructor argument does not have 6 bytes");
        }
        this.address = new int[6];

        for(int i = 0; i < 6; i++) {
            int element = address[i];
            if (element < 0 || element > 255) {
                throw new IllegalArgumentException("MAC address bytes must be between 0 and 255");
            }
            this.address[i] = element;
        }
    }

    @Override
    public String toString(){
        String ret = String.format("%02X", this.address[0]);
        for(int i = 1; i < 6; i++) {
            ret += ":" + String.format("%02X", this.address[i]);
        }
        return ret;
    }
}
