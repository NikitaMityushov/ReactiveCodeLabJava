package riddles;

import io.reactivex.rxjava3.core.Observable;

/**
 * Increment each emitted value of the given [source] by 1.
 *
 * Use case: You want to transform the data.
 */
public class Riddle2 {
    public static Observable<Integer> solve(Observable<Integer> source) {
        return source.map(item -> item + 1);
    }
}
