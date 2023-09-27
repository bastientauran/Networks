package model.simulator;

import java.util.ArrayList;

/**
 * Interface that must be implemeted by all classes that contains at least one
 * schedulable method
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public interface Schedulable {
    /**
     * Run the desired method with selected arguments
     * 
     * @param method    The method to launch
     * @param arguments The arguments to add to this method
     */
    public void run(String method, ArrayList<Object> arguments);
}
