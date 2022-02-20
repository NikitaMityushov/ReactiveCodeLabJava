package combiningOperators;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Accepts an Iterable<Observable<T>> object as a parameter and emits the values
 * of the first Observable that emits, while the other are disposed of. This is helpful
 * when there are multiple sources of the same data or events, and you want the fastest
 * one to win.
 */
public class AmbiguousOperators {
    private final Observable<Long> infiniteSource1 = Observable.interval(500, TimeUnit.MICROSECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(1000, TimeUnit.MICROSECONDS);

    public final void amb() {
        Observable.amb(Arrays.asList(infiniteSource1, infiniteSource2))
                .subscribe(System.out::println);
    }

    public final void ambWith() {
        infiniteSource1.ambWith(infiniteSource2)
                .subscribe(System.out::println);
    }

    public final void ambArray() {
        Observable.ambArray(infiniteSource1, infiniteSource2)
                .subscribe(System.out::println);
    }
}
