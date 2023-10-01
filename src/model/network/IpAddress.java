package model.network;

import java.util.Objects;

/**
 * This class represents an IP address
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class IpAddress implements Comparable<IpAddress> {

    /**
     * Size of an IP address in an IP header, without mask
     */
    public static final int SIZE_NO_MASK_BYTES = 4;

    /**
     * Array of 4 bytes representing a MAC address
     */
    private int[] address;

    /**
     * Netmask of this address
     */
    private int mask;

    /**
     * Default IP address constructor
     */
    public IpAddress() {
        this.address = new int[4];
        this.mask = 32;
    }

    /**
     * IP address constructor using array
     * 
     * @param address The address to use
     * @param mask    Netmask of this address
     */
    public IpAddress(int[] address, int mask) {
        if (address.length != 4) {
            throw new IllegalArgumentException("IP address constructor argument does not have 4 bytes");
        }
        this.address = new int[4];

        for (int i = 0; i < 4; i++) {
            int element = address[i];
            if (element < 0 || element > 255) {
                throw new IllegalArgumentException("IP address bytes must be between 0 and 255:" + element);
            }
            this.address[i] = element;
        }

        if (mask < 0 || mask > 32) {
            throw new IllegalArgumentException("IP address mask must be between 0 and 32: " + mask);
        }
        this.mask = mask;
    }

    /**
     * IP address constructor using string
     * 
     * @param address The address to use, must use format XX.XX.XX.XX/p
     */
    public IpAddress(String address) {
        String[] elts = address.split("/");
        if (elts.length > 2) {
            throw new IllegalArgumentException(
                    "IP address constructor argument must have an address and optionnally a netmask: " + address);
        }

        int mask;
        if (elts.length == 2) {
            mask = Integer.parseInt(elts[1]);
        } else {
            mask = 32;
        }

        if (mask < 0 || mask > 32) {
            throw new IllegalArgumentException("IP address mask must be between 0 and 32: " + elts[1]);
        }
        this.mask = mask;

        String[] elements = elts[0].split("\\.");
        if (elements.length != 4) {
            throw new IllegalArgumentException("IP address constructor argument does not have 4 bytes: " + elts[0]);
        }
        this.address = new int[4];

        for (int i = 0; i < 4; i++) {
            int element = Integer.parseInt(elements[i]);
            if (element < 0 || element > 255) {
                throw new IllegalArgumentException("IP address bytes must be between 0 and 255: " + element);
            }
            this.address[i] = element;
        }
    }

    /**
     * IP address copy constructor
     * 
     * @param other The address to copy
     */
    public IpAddress(IpAddress other) {
        this.address = new int[4];
        for (int i = 0; i < 4; i++) {
            this.address[i] = other.address[i];
        }
        this.mask = other.mask;
    }

    /**
     * IP address copy constructor with new mask
     * 
     * @param other The address to copy
     * @param mask  The new mask
     */
    public IpAddress(IpAddress other, int mask) {
        this.address = new int[4];
        for (int i = 0; i < 4; i++) {
            this.address[i] = other.address[i];
        }

        if (mask < 0 || mask > 32) {
            throw new IllegalArgumentException("IP address mask must be between 0 and 32: " + mask);
        }
        this.mask = mask;
    }

    /**
     * Get the address of the network represented by this IP address
     * 
     * @return The network address
     */
    public IpAddress getNetwork() {
        IpAddress network = new IpAddress(this);

        if (this.mask == 32) {
            return network;
        }

        int bloc = this.mask / 8;
        int index = this.mask % 8;

        if (index == 0) {
            for (int i = bloc; i < 4; i++) {
                network.address[i] = 0;
            }
        } else {
            network.address[bloc] &= (256 - 1 << (8 - index));

            for (int i = bloc + 1; i < 4; i++) {
                network.address[i] = 0;
            }
        }

        return network;
    }

    /**
     * Get mask of this address
     * 
     * @return The IP address mask
     */
    public int getMask() {
        return this.mask;
    }

    /**
     * Test if current IP address is in given IP network
     * 
     * @param network The IP network
     * @return True if the address is in this network
     */
    public boolean isInNetwork(IpAddress network) {
        IpAddress ipAddress = new IpAddress(this);
        ipAddress.mask = network.mask;
        return ipAddress.getNetwork().equals(network.getNetwork());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof IpAddress)) {
            return false;
        }

        IpAddress ipAddress = (IpAddress) other;
        for (int i = 1; i < 4; i++) {
            if (this.address[i] != ipAddress.address[i]) {
                return false;
            }
        }

        return this.mask == ipAddress.mask;
    }

    @Override
    public int compareTo(IpAddress other) {
        if (this.mask != other.mask) {
            return this.mask - other.mask;
        }
        for (int i = 0; i < 4; i++) {
            if (this.address[i] != other.address[i]) {
                return this.address[i] - other.address[i];
            }
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, mask);
    }

    @Override
    public String toString() {
        return toStringNoMask() + "/" + String.valueOf(this.mask);
    }

    /**
     * Get String representation of this IP address, without mask
     * 
     * @return The String generated
     */
    public String toStringNoMask() {
        String ret = String.valueOf(this.address[0]);
        for (int i = 1; i < 4; i++) {
            ret += "." + String.valueOf(this.address[i]);
        }
        return ret;
    }
}
