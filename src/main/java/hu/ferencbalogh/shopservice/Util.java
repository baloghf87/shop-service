package hu.ferencbalogh.shopservice;

import java.util.LinkedList;
import java.util.List;

public final class Util {

    private Util() {
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        List<T> results = new LinkedList<>();
        iterable.iterator().forEachRemaining(results::add);
        return results;
    }
}
