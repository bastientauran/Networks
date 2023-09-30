package model.simulator;

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
    DUMMY_CLASS__METHOD_2("DummyClass", "method2", new Class<?>[] { String.class, Time.class });

    private String className;
    private String methodName;
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
     * @return The name of the class that launch this method
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Get the name of the method
     * @return The name of the method
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * Get the array containing all the argument types needed by this method
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
