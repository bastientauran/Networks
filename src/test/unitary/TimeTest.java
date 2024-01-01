package test.unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.simulator.Time;

public class TimeTest extends GenericTest {

    @Test
    public void testEmptyConstructor() {
        Time time = new Time();
        assertEquals("0s0ns", time.toString());
    }

    @Test
    public void testCustomConstructor() {
        Time time = new Time(10, 50);
        assertEquals("10s50ns", time.toString());
        assertThrows(
                RuntimeException.class,
                () -> new Time(-1, 20));
        assertThrows(
                RuntimeException.class,
                () -> new Time(40, -1556));
        assertThrows(
                RuntimeException.class,
                () -> new Time(40, 1000000000));
        assertThrows(
                RuntimeException.class,
                () -> new Time(40, 1234567890));
    }

    @Test
    public void testStaticConstructors() {
        Time time = Time.seconds(50);
        assertEquals(new Time(50, 0), time);
        time = Time.milliSeconds(42);
        assertEquals(new Time(0, 42000000), time);
        time = Time.microSeconds(67);
        assertEquals(new Time(0, 67000), time);
        time = Time.nanoSeconds(31);
        assertEquals(new Time(0, 31), time);
        time = Time.nanoSeconds(123456789123456789L);
        assertEquals(new Time(123456789, 123456789), time);
        time = Time.milliSeconds(987654321);
        assertEquals(new Time(987654, 321000000), time);
        assertThrows(
                RuntimeException.class,
                () -> Time.milliSeconds(-1));
        assertThrows(
                RuntimeException.class,
                () -> Time.microSeconds(-10));
        assertThrows(
                RuntimeException.class,
                () -> Time.nanoSeconds(-100));
    }

    @Test
    public void testCompareTo() {
        Time time = new Time(10, 50);
        assertTrue(time.compareTo(new Time(10, 51)) < 0);
        assertTrue(time.compareTo(new Time(11, 50)) < 0);
        assertTrue(time.compareTo(new Time(10, 49)) > 0);
        assertTrue(time.compareTo(new Time(9, 50)) > 0);
        assertTrue(time.compareTo(new Time(10, 50)) == 0);
    }

    @Test
    public void testAdd() {
        Time time = new Time(10, 50);
        assertTrue(time.add(new Time(2, 4)).equals(new Time(12, 54)));
        assertTrue(time.add(new Time(2, 999999990)).equals(new Time(13, 40)));
    }

    @Test
    public void testRemove() {
        Time time = new Time(10, 50);
        assertTrue(time.remove(new Time(2, 4)).equals(new Time(8, 46)));
        assertTrue(time.remove(new Time(2, 100)).equals(new Time(7, 999999950)));
        assertThrows(
                RuntimeException.class,
                () -> time.remove(new Time(10, 51)));
        assertThrows(
                RuntimeException.class,
                () -> time.remove(new Time(11, 50)));
    }

    @Test
    public void testGetters() {
        Time time = new Time(123, 456789321);
        assertEquals(123.456789321, time.getSeconds(), 0.0);
        assertEquals(123456.789321, time.getMilliSeconds(), 0.0);
        assertEquals(123456789.321, time.getMicroSeconds(), 0.0);
        assertEquals(123456789321.0, time.getNanoSeconds(), 0.0);
    }

    @Test
    public void testTruncate() {
        Time time = new Time(123, 456789321);
        assertEquals(Time.milliSeconds(123456), time.truncate(3));
        assertEquals(Time.seconds(123), time.truncate(0));
        assertEquals(new Time(123, 456789321), time.truncate(9));
        assertThrows(
                RuntimeException.class,
                () -> time.truncate(-1));
        assertThrows(
                RuntimeException.class,
                () -> time.truncate(10));
    }

    @Test
    public void testDivide() {
        Time time = Time.seconds(100);
        assertEquals(Time.seconds(10), time.divide(10));
        assertEquals(Time.seconds(1), time.divide(100));
        assertEquals(Time.milliSeconds(100), time.divide(1000));
        assertEquals(Time.seconds(40), time.divide(2.5));
        assertThrows(
                RuntimeException.class,
                () -> time.divide(-0.5));
    }

    @Test
    public void testToString() {
        Time time = new Time(10, 123456789);
        assertEquals("10s123456789ns", time.toString());
        assertEquals("10.12s", time.toStringWithPrecision(2));
        assertEquals("10s", time.toStringWithPrecision(0));
        assertEquals("10.123456789s", time.toStringWithPrecision(9));

        assertThrows(
                RuntimeException.class,
                () -> time.toStringWithPrecision(-1));
        assertThrows(
                RuntimeException.class,
                () -> time.toStringWithPrecision(10));

        Time time2 = Time.seconds(42);
        assertEquals("42s0ns", time2.toString());
        assertEquals("42s", time2.toStringWithPrecision(2));
        assertEquals("42s", time2.toStringWithPrecision(0));
        assertEquals("42s", time2.toStringWithPrecision(9));

        Time time3 = Time.milliSeconds(12789);
        assertEquals("12s789000000ns", time3.toString());
        assertEquals("12s", time3.toStringWithPrecision(0));
        assertEquals("12.78s", time3.toStringWithPrecision(2));
        assertEquals("12.789s", time3.toStringWithPrecision(3));
        assertEquals("12.789s", time3.toStringWithPrecision(4));
        assertEquals("12.789s", time3.toStringWithPrecision(9));

        Time time4 = Time.nanoSeconds(100);
        assertEquals("0s100ns", time4.toString());
        assertEquals("0s", time4.toStringWithPrecision(0));
        assertEquals("0s", time4.toStringWithPrecision(2));
        assertEquals("0.0000001s", time4.toStringWithPrecision(7));
        assertEquals("0.0000001s", time4.toStringWithPrecision(9));
    }
}
