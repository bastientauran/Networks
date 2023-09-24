package model.nodes;

import java.util.ArrayList;

/**
 * Abstract class representing a node
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Interface
 */
public abstract class Node {

    /**
     * All the interfaces that compose this node
     */
    protected ArrayList<Interface> interfaces;

    /**
     * Node contructor
     */
    public Node() {
        this.interfaces = new ArrayList<Interface>();
    }
}
