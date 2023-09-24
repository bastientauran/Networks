package model.network;

import java.util.Stack;

public class Packet {

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
        for(int i = this.headers.size() - 1; i >= 0; i--) {
            str += this.headers.elementAt(i);
        }
        str += "[payload='" + this.payload + "', payloadSize=" + this.payloadSizeBytes + ", totalSize=" + this.getTotalSizeBytes() + "]";
        return str;
    }
}
