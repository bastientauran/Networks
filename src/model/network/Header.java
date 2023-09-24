package model.network;

/**
 * Abstract class representing a header
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public abstract class Header {
    
    /**
     * Get header size in bytes
     * @return The header size
     */
    public abstract int getSize();
}
