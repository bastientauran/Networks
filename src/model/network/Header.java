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
     * Get the type of this header
     * 
     * @return The type of the header
     */
    public HeaderType getType() {
        return this.type;
    }
}
