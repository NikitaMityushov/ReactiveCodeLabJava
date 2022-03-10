package riddles;

import io.reactivex.rxjava3.core.Observable;

/**
 * Don't emit odd numbers from the [source] Observable.
 *
 * Use case: You want to filter certain items out.
 */
public class Riddle3 {
    public static Observable<Integer> solve(Observable<Integer> source) {
        return source.filter(item -> item % 2 == 0);
    }
}
