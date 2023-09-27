package unitary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.simulator.Time;

public class TimeTest {

    @Test
    public void testEmptyConstructor() {
        Time time = new Time();
        assertEquals(time.toString(), "0s0ns");
    }

    @Test
    public void testCustomEmpty() {
        Time time = new Time(10, 50);
        assertEquals(time.toString(), "10s50ns");
        assertThrows(
                IllegalArgumentException.class,
                () -> new Time(-1, 20));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Time(40, -1556));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Time(40, 1000000000));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Time(40, 1234567890));
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
                IllegalStateException.class,
                () -> time.remove(new Time(10, 51)));
        assertThrows(
                IllegalStateException.class,
                () -> time.remove(new Time(11, 50)));
    }
}
