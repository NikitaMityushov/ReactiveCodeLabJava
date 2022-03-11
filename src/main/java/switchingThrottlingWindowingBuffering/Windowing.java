package switchingThrottlingWindowingBuffering;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 * The window() operator is almost identical to the buffer() operator,
 * except, that it buffers into another Observable rather than a
 * collection. The result is Observable<Observable<T>> that emits
 * observables. Each Observable emission caches emissions for each scope and then flushes
 * them once subscribed. This allows emissions to be worked with immediately as they
 * become available, rather than waiting for each list or collection to be finalized and
 * emitted. The window operator is also convenient to work with if you want to use
 * operators to transform each batch.
 */
public class Windowing {
    private final Observable<Integer> intSource1 = Observable.range(1, 50);
    private final Observable<Integer> intSource2 = Observable.range(1, 50);
    private final Observable<Long> infiniteSource1 = Observable.interval(1, TimeUnit.SECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(300, TimeUnit.MILLISECONDS);

    /*
            Fixed-size windowing
     */

    /**
     * see the buffering
     */
    public final void windowWithCount(int count) {
        intSource1.window(count)
                .flatMapSingle(obs -> obs.reduce("", (total, next) -> total + (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);
    }

    /**
     * see the buffering
     */
    public final void windowWithCountAndSkip(int count, int skip) {
        intSource1.window(count, skip)
                .flatMapSingle(obs -> obs.reduce("", (total, next) -> total + (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);
    }

    /*
            Time-based windowing
     */

    /**
     * see the buffering
     */
    public final void windowWithTimespan(long timespan, TimeUnit unit) {
        infiniteSource2.map(i -> (i + 1) * 300)
                .window(timespan, unit)
                .flatMapSingle(obs -> obs.reduce("", (total, next) -> total + (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);
    }

    /*
            Boundary-based windowing
     */

    /**
     * see the buffering
     */
    public final void windowWithBoundary() {
        infiniteSource2.map(i -> (i + 1) * 300)
                .window(infiniteSource1)
                .flatMapSingle(obs -> obs.reduce("", (total, next) -> total + (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);
    }
}
