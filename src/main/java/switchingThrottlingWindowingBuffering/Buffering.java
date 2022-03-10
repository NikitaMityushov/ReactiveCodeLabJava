package switchingThrottlingWindowingBuffering;

import io.reactivex.rxjava3.core.Observable;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * The buffer() operator gathers emissions within a certain scope and
 * emits each batch as a list or another collection type. The scope can
 * be defined by a fixed buffer sizing or a timing window that cuts off
 * at intervals or even slices by the emissions of another Observable.
 */
public class Buffering {
    private final Observable<Integer> intSource1 = Observable.range(1, 50);
    private final Observable<Integer> intSource2 = Observable.range(1, 50);
    private final Observable<Long> infiniteSource1 = Observable.interval(1, TimeUnit.SECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(300, TimeUnit.MILLISECONDS);

    /*
               1) Fixed-size buffering
     */

    /**
     * Fixed-size buffering.
     * The operator buffer(int count) accepts a count as a buffer size
     * and groups emissions in the batches of the specified size.
     */
    public final void buffer(int count) {
        intSource1
                .buffer(count)
                .subscribe(System.out::println);
    }

    /**
     * You can also supply another arg in the buffer() - bufferSupplier,
     * that puts emissions in another collection.
     */
    public final void bufferWithSupplier(int count) {
        intSource1
                .buffer(count, HashSet::new)
                .subscribe(System.out::println);
    }

    /**
     * You can also provide a skip argument that specifies haw
     * many items should be skipped before starting a new buffer.
     * If skip equals count, the skip has no effect.
     * There is some interesting behavior. For instance, you can
     * buffer 2 emissions but skip 3 before the next buffer starts.
     * This will essentially cause every third element to not be buffered!
     * <p>
     * Definitely playing with the skip arg for buffer() and you may
     * find surprising use cases for it))
     */
    public final void bufferWithSkip(int count, int skip) {
        intSource1
                .buffer(count, skip)
                .subscribe(System.out::println);
    }

    /*
               2) Time-based buffering
     */

    /**
     * You can use the buffer(long timespan, TimeUnit unit) operator at
     * fixed time intervals by providing timespan and unit values.
     * <p>
     * Times-based buffer() operators will operate on the computation
     * Scheduler. This make sense since a separate thread needs to run
     * on a timer to execute the cutoffs.
     */
    public final void bufferWithTimeSpan(long timespan, TimeUnit unit) {
        infiniteSource2
                .map(i -> (i + 1) * 300)
                .buffer(timespan, unit)
                .subscribe(System.out::println);
    }

    /**
     * You can also leverage a third count argument to provide a maximum buffer size.
     * This will result in a buffer emissions at each time interval or when count is
     * reached, whichever happens first. If the count is reached right before the time
     * window closes, it will result in an empty buffer being emitted.
     */
    public final void bufferWithTimeSpanAndBufferSize(long timespan, TimeUnit unit, int count) {
        infiniteSource2
                .map(i -> (i + 1) * 300)
                .buffer(timespan, unit, count)
                .subscribe(System.out::println);
    }

    /*
               3) Boundary-based buffering
     */

    /**
     * Boundary-based buffer(Observable<B> boundary) accepts another
     * Observable as a boundary arg. It does not matter what type this other Observable
     * emits. All that matters is that every time it emits something, it will use the
     * timing of that emission as the buffer cutoff. In other words, the arbitrary
     * occurrence of emissions of another Observable will determine when to slice
     * each buffer.
     */

    public final void bufferWithBoundary() {
        infiniteSource2
                .map(i -> (i + 1) * 300)
                .buffer(infiniteSource1)
                .subscribe(System.out::println);
    }
}
