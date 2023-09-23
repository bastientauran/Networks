package model.nodes;

import java.util.ArrayList;

/**
 * Abstract class representing a node
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
