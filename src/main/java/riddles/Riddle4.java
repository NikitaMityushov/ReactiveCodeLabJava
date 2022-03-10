package riddles;

import io.reactivex.rxjava3.core.Observable;

/**
 * Implement a toggle mechanism. Initially we want to return false.
 * Every time [source] emits, we want to negate the previous value.
 *
 * Use case: Some button that can toggle two states. For instance a switch between White & Dark theme.
 */
public class Riddle4 {
    public static Observable<Boolean> solve(Observable<Object> source) {
        return source.scan(false, (acc, item) -> !acc);
    }
}
