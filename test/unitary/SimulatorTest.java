package unitary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.simulator.Schedulable;
import model.simulator.Simulator;
import model.simulator.Time;

public class SimulatorTest {

    private class DummyMethods implements Schedulable {

        public int valueInt;
        public String valueString;
        public Time valueTime;

        public String totalString;

        public DummyMethods() {
            this.valueInt = 0;
            this.valueString = "";
            this.valueTime = null;
            this.totalString = "";
        }

        public void method1(int arg) {
            this.valueInt = arg;
            this.totalString += arg;
        }

        public double method2(String arg1, Time arg2) {
            this.valueString = arg1;
            this.valueTime = arg2;
            this.totalString += arg1;
            return 0.0;
        }

        @Override
        public void run(String method, Object[] arguments) {
            switch (method) {
                case "method1": {
                    if (arguments.length != 1) {
                        throw new IllegalArgumentException("method1 of class SummyMethods must have one argument");
                    }
                    method1((int) arguments[0]);
                    break;
                }
                case "method2": {
                    if (arguments.length != 2) {
                        throw new IllegalArgumentException("method2 of class SummyMethods must have one argument");
                    }
                    method2((String) arguments[0], (Time) arguments[1]);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknow method for class DummyMethods: " + method);
                }
            }
        }
    }

    @Test
    public void testSingletonTemplate() {
        Simulator.getInstance().reset();
        assertEquals(Simulator.getInstance(), Simulator.getInstance());
    }

    @Test
    public void testinitialTime() {
        Simulator.getInstance().reset();
        assertEquals(Simulator.getInstance().getCurrentTime(), new Time());
        assertEquals(Simulator.getInstance().getCurrentTime().toString(), "0s0ns");
    }

    @Test
    public void testScheduling() {
        DummyMethods d1 = new DummyMethods();
        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(10, 0));
        Simulator.getInstance().schedule(new Time(5, 0), d1, "method1", new Object[] { 1 });
        Simulator.getInstance().schedule(new Time(1, 0), d1, "method2", new Object[] { "test", new Time(4, 100) });
        Simulator.getInstance().schedule(new Time(5, 10), d1, "method1", new Object[] { -40 });
        Simulator.getInstance().schedule(new Time(5, 1), d1, "method2", new Object[] { "other", new Time(14, 3000) });
        Simulator.getInstance().schedule(new Time(15, 0), d1, "method1", new Object[] { 21 });
        Simulator.getInstance().run();
        assertEquals(-40, d1.valueInt);
        assertEquals("other", d1.valueString);
        assertEquals(new Time(14, 3000), d1.valueTime);
        assertEquals("test1other-40", d1.totalString);
    }
}
