package test.unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import model.network.MacAddress;
import model.simulator.Schedulable;
import model.simulator.SchedulableMethod;
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
        public void run(SchedulableMethod method, Object[] arguments) {
            switch (method) {
                case DUMMY_CLASS__METHOD_1: {
                    this.method1((int) arguments[0]);
                    break;
                }
                case DUMMY_CLASS__METHOD_2: {
                    this.method2((String) arguments[0], (Time) arguments[1]);
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
        assertEquals(new Time(), Simulator.getInstance().getCurrentTime());
        assertEquals("0s0ns", Simulator.getInstance().getCurrentTime().toString());
    }

    @Test
    public void testScheduling() {
        DummyMethods d = new DummyMethods();
        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(10, 0));

        Simulator.getInstance().schedule(new Time(5, 0), d, SchedulableMethod.DUMMY_CLASS__METHOD_1, 1);
        Simulator.getInstance().schedule(new Time(1, 0), d, SchedulableMethod.DUMMY_CLASS__METHOD_2, "test",
                new Time(4, 100));
        Simulator.getInstance().schedule(new Time(5, 10), d, SchedulableMethod.DUMMY_CLASS__METHOD_1, -40);
        Simulator.getInstance().schedule(new Time(5, 1), d, SchedulableMethod.DUMMY_CLASS__METHOD_2, "other",
                new Time(14, 3000));
        Simulator.getInstance().schedule(new Time(15, 0), d, SchedulableMethod.DUMMY_CLASS__METHOD_1, 21);
        Simulator.getInstance().run();

        assertEquals(-40, d.valueInt);
        assertEquals("other", d.valueString);
        assertEquals(new Time(14, 3000), d.valueTime);
        assertEquals("test1other-40", d.totalString);
    }

    @Test
    public void testSchedulingError() {
        DummyMethods d = new DummyMethods();
        Simulator.getInstance().reset();
        Simulator.getInstance().setStopTime(new Time(10, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> Simulator.getInstance().schedule(new Time(5, 0), d, SchedulableMethod.DUMMY_CLASS__METHOD_1,
                        1.5));
        assertThrows(
                IllegalArgumentException.class,
                () -> Simulator.getInstance().schedule(new Time(1, 0), d, SchedulableMethod.DUMMY_CLASS__METHOD_2,
                        "test",
                        new MacAddress()));
    }
}
