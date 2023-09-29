package unitary;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

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

        public DummyMethods(){
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
        public void run(String method, ArrayList<Object> arguments) {
            switch (method) {
                case "method1": {
                    if (arguments.size() != 1) {
                        throw new IllegalArgumentException("method1 of class SummyMethods must have one argument");
                    }
                    method1((int) arguments.get(0));
                    break;
                }
                case "method2": {
                    if (arguments.size() != 2) {
                        throw new IllegalArgumentException("method2 of class SummyMethods must have one argument");
                    }
                    method2((String) arguments.get(0), (Time) arguments.get(1));
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
        assertEquals(Simulator.getInstance(), Simulator.getInstance());
    }

    @Test
    public void testinitialTime() {
        assertEquals(Simulator.getInstance().getCurrentTime(), new Time());
        assertEquals(Simulator.getInstance().getCurrentTime().toString(), "0s0ns");
    }

    @Test
    public void testScheduling() {
        DummyMethods d1 = new DummyMethods();
        ArrayList<Object> args1 = new ArrayList<Object>();
        args1.add(1);
        ArrayList<Object> args2 = new ArrayList<Object>();
        args2.add("test");
        args2.add(new Time(4, 100));
        ArrayList<Object> args3 = new ArrayList<Object>();
        args3.add(-40);
        ArrayList<Object> args4 = new ArrayList<Object>();
        args4.add("other");
        args4.add(new Time(14, 3000));
        Simulator.getInstance().setStopTime(new Time(10, 0));
        Simulator.getInstance().schedule(new Time(5, 0), d1, "method1", args1);
        Simulator.getInstance().schedule(new Time(1, 0), d1, "method2", args2);
        Simulator.getInstance().schedule(new Time(5, 10), d1, "method1", args3);
        Simulator.getInstance().schedule(new Time(5, 1), d1, "method2", args4);
        Simulator.getInstance().run();
        assertEquals(-40, d1.valueInt);
        assertEquals("other", d1.valueString);
        assertEquals(new Time(14, 3000), d1.valueTime);
        assertEquals("test1other-40", d1.totalString);
    }
}
