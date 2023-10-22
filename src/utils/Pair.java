package utils;

/**
 * Class used to store a pair of values.
 * Once created, values in pair are immuable
 * 
 * @author Bastien Tauran
 * @version 1.0
 */
public class Pair<E, F> {
    /**
     * First element of the pair
     */
    public final E first;

    /**
     * Second element of the pair
     */
    public final F second;

    /**
     * Create a new Pair
     * 
     * @param first  The first element
     * @param second The second element
     */
    public Pair(E first, F second) {
        this.first = first;
        this.second = second;
    }
}
