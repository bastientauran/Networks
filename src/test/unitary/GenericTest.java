package test.unitary;

import model.logger.Logger;

/**
 * Parent class of all unitary tests, used to initialize correctly all tests
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class GenericTest {
    public GenericTest() {
        Logger.getInstance().enableLogger();
    }
}
