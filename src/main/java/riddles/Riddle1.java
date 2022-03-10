package riddles;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

/**
 * Transform the given [value] into an Observable that emits the value and then completes.
 *
 * Use case: You want to transform some value to the reactive world.
 */
public class Riddle1 {
    public static Observable<Integer> solve(@NonNull Integer value) {
        return Observable.just(value);
    }
}

