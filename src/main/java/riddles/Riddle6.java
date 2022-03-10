package riddles;

import io.reactivex.rxjava3.core.Single;

import java.util.*;

/**
 * Execute both [first] and [second] Single's in parallel and provide both results as a pair.
 * Assume both [first] and [second] will execute on a different thread already.
 * This is a slightly simpler version of [Riddle102], where no schedulers are applied by default.
 *
 * Use case: Execute two network requests in parallel and wait for each other and process the combined data.
 */
public class Riddle6 {
    public static Single<List<Integer>> solve(Single<Integer> first, Single<Integer> second) {
        return first.zipWith(second, ((i1, i2) -> {
            var list = new ArrayList<Integer>();
            list.add(i1);
            list.add(i2);
            return list;
        }));
    }
}
