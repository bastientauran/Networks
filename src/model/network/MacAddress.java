package model.network;

import model.logger.LogSeverity;
import model.logger.Logger;

/**
 * This class represents a MAC address
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class MacAddress implements Comparable<MacAddress> {

    /**
     * Size of a MAC address in a MAC header
     */
    public static final int SIZE_BYTES = 6;

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
     * MAC address constructor using array
     * 
     * @param address       The address to use
     * @param checkExisting Check if address already exists in simulation
     */
    public MacAddress(int[] address, boolean checkExisting) {
        if (address.length != 6) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "MAC address constructor argument does not have 6 bytes");
        }
        this.address = new int[6];

        for (int i = 0; i < 6; i++) {
            int element = address[i];
            if (element < 0 || element > 255) {
                Logger.getInstance().log(LogSeverity.CRITICAL,
                        "MAC address bytes must be between 0 and 255: " + element);
            }
            this.address[i] = element;
        }

        if (checkExisting && !MacAddressContainer.getInstance().addMacAddress(this)) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Address already in MAC address container: " + this);
        }
    }

    /**
     * MAC address constructor using string
     * 
     * @param address       The address to use, must use format XX:XX:XX:XX:XX:XX
     * @param checkExisting Check if address already exists in simulation
     */
    public MacAddress(String address, boolean checkExisting) {
        String[] elements = address.split(":");
        if (elements.length != 6) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "MAC address constructor argument does not have 6 bytes");
        }
        this.address = new int[6];

        for (int i = 0; i < 6; i++) {
            int element = Integer.parseInt(elements[i], 16);
            if (element < 0 || element > 255) {
                Logger.getInstance().log(LogSeverity.CRITICAL,
                        "MAC address bytes must be between 0 and 255: " + element);
            }
            this.address[i] = element;
        }

        if (!MacAddressContainer.getInstance().addMacAddress(this) && checkExisting) {
            Logger.getInstance().log(LogSeverity.CRITICAL, "Address already in MAC address container: " + this);
        }
    }

    /**
     * MAC address copy constructor
     * 
     * @param other The address to copy
     */
    public MacAddress(MacAddress other) {
        this.address = new int[6];
        for (int i = 0; i < 6; i++) {
            this.address[i] = other.address[i];
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof MacAddress)) {
            return false;
        }

        MacAddress macAddress = (MacAddress) other;
        for (int i = 1; i < 6; i++) {
            if (this.address[i] != macAddress.address[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String ret = String.format("%02X", this.address[0]);
        for (int i = 1; i < 6; i++) {
            ret += ":" + String.format("%02X", this.address[i]);
        }
        return ret;
    }

    @Override
    public int compareTo(MacAddress other) {
        for (int i = 0; i < 6; i++) {
            if (this.address[i] != other.address[i]) {
                return this.address[i] - other.address[i];
            }
        }
        return 0;
    }
}
