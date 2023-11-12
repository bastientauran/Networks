package model.network;

import java.util.Stack;

import model.utils.IdGenerator;

/**
 * Class representing a packet.
 * It is represented by a payload (with a size and an optional String
 * representation),
 * and headers, using a LIFO rule.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Header
 */
public class Packet {

    /**
     * ID of the packet, generated automatically
     */
    protected int packetId;

    /**
     * Payload of the packet
     */
    private String payload;

    /**
     * Size of payload in bytes
     */
    private int payloadSizeBytes;

    /**
     * Array containing all the packet headers
     */
    private Stack<Header> headers;

    /**
     * Default constructor
     */
    public Packet() {
        this.packetId = IdGenerator.getInstance().getNextPacketId();
        this.payload = "";
        this.payloadSizeBytes = 0;
        this.headers = new Stack<Header>();
    }

    /**
     * Constructs a packet of given size
     * 
     * @param payloadSizeBytes The packet payload size in bytes
     */
    public Packet(int payloadSizeBytes) {
        this.packetId = IdGenerator.getInstance().getNextPacketId();
        this.payload = "";
        this.payloadSizeBytes = payloadSizeBytes;
        this.headers = new Stack<Header>();
    }

    /**
     * Constructs a packet of given size and custom payload+
     * 
     * @param payload          The packet payload
     * @param payloadSizeBytes The packet payload size in bytes
     */
    public Packet(String payload, int payloadSizeBytes) {
        this.packetId = IdGenerator.getInstance().getNextPacketId();
        this.payload = payload;
        this.payloadSizeBytes = payloadSizeBytes;
        this.headers = new Stack<Header>();
    }

    /**
     * Add a header to this packet
     * 
     * @param header The header to add
     */
    public void addHeader(Header header) {
        this.headers.push(header);
    }

    /**
     * Get the current packet header but does not remove it from packet
     * 
     * @return The current packet header, or null if no header in this packet
     */
    public Header peekHeader() {
        if (this.headers.isEmpty()) {
            return null;
        }
        return this.headers.peek();
    }

    /**
     * Get the current packet header and removes it from packet
     * 
     * @return The current packet header, or null if no header in this packet
     */
    public Header popHeader() {
        if (this.headers.isEmpty()) {
            return null;
        }
        return this.headers.pop();
    }

    /**
     * Get packet payload size in bytes
     * 
     * @return Packet payload size
     */
    public int getPayloadSizeBytes() {
        return this.payloadSizeBytes;
    }

    /**
     * Get packet total size (including headers) in bytes
     * 
     * @return Packet total size
     */
    public int getTotalSizeBytes() {
        int size = this.payloadSizeBytes;
        for (Header header : this.headers) {
            size += header.getSize();
        }
        return size;
    }

    /**
     * Get packet payload
     * 
     * @return Packet payload
     */
    public String getPayload() {
        return this.payload;
    }

    @Override
    public String toString() {
        String str = "Packet: ";
        for (int i = this.headers.size() - 1; i >= 0; i--) {
            str += this.headers.elementAt(i);
        }
        str += "[payload='" + this.payload + "', payloadSize=" + this.payloadSizeBytes + ", totalSize="
                + this.getTotalSizeBytes() + "]";
        return str;
    }

    /**
     * Format headers and payload to be printed in a trace file
     * 
     * @return String representation of the packet to trace
     */
    public String formatToTrace() {
        String[] output = new String[this.headers.size() + 1];
        int i = 0;
        for (Header e : this.headers) {
            output[i] = e.formatToTrace();
            i++;
        }
        if (this.payload != "") {
            output[this.headers.size()] = this.payload;
        } else {
            output[this.headers.size()] = "NoPayload";
        }

        return this.packetId + " " + String.join(" ", output);
    }
}
