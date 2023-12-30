package model.simulator;

import model.network.IpAddress;
import model.network.Packet;

/**
 * Enumeration giving the methods that can be scheduled
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Event
 * @see Simulator
 */
public enum SchedulableMethod {
    DUMMY_CLASS__METHOD_1("DummyClass", "method1", new Class<?>[] { Integer.class }),
    DUMMY_CLASS__METHOD_2("DummyClass", "method2", new Class<?>[] { String.class, Time.class }),
    SIMULATOR__PRINT_PROGRESS_BAR("Simulator", "printProgressBar", new Class<?>[] {}),
    POINT_TO_POINT_LINK__END_TX("PointToPointLink", "endTx", new Class<?>[] { Packet.class, Integer.class }),
    POINT_TO_POINT_LINK__START_RX("PointToPointLink", "startRx", new Class<?>[] { Packet.class, Integer.class }),
    POINT_TO_POINT_LINK__END_RX("PointToPointLink", "endRx", new Class<?>[] { Packet.class, Integer.class }),
    END_DEVICE__SEND("EndDevice", "send", new Class<?>[] { Packet.class, IpAddress.class });

    /**
     * Name of class that launch this method
     */
    private String className;

    /**
     * The name of the method
     */
    private String methodName;

    /**
     * Array containing all the argument types needed by this method
     */
    private Class<?>[] argumentTypes;

    /**
     * Create an instance of SchedulableMethod Enum
     * 
     * @param className     The name of the class that launch this method
     * @param methodName    The name of the method
     * @param argumentTypes Array containing all the argument types needed by this
     *                      method
     */
    private SchedulableMethod(String className, String methodName, Class<?>[] argumentTypes) {
        this.className = className;
        this.methodName = methodName;
        this.argumentTypes = argumentTypes;
    }

    /**
     * Get the name of the class that launch this method
     * 
     * @return The name of the class that launch this method
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Get the name of the method
     * 
     * @return The name of the method
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * Get the array containing all the argument types needed by this method
     * 
     * @return The array containing all the argument types needed by this method
     */
    public Class<?>[] getArgumentTypes() {
        return this.argumentTypes;
    }

    @Override
    public String toString() {
        return this.className + "." + this.methodName;
    }
}
