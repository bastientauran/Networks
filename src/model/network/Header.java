package model.network;

/**
 * Abstract class representing a header
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see HeaderType
 */
public abstract class Header {

    /**
     * Type of this header
     */
    private HeaderType type;

    /**
     * Construct a new header
     * 
     * @param type The type of the header
     */
    public Header(HeaderType type) {
        this.type = type;
    }

    /**
     * Get header size in bytes
     * 
     * @return The header size
     */
    public abstract int getSize();

    /**
     * Format header to be printed in a trace file
     * 
     * @return String representation of the header to trace
     */
    public abstract String formatToTrace();

    /**
     * Get the type of this header
     * 
     * @return The type of the header
     */
    public HeaderType getType() {
        return this.type;
    }
}
