package switchingThrottlingWindowingBuffering;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * The throttling() operator omits emissions when they occur rapidly.
 * This is helpful when rapid emissions are assumed to be redundant
 * or unwanted, such as a user clicking a button repeatedly. For these
 * situations, you can use the throttleLast(), throttleFirst(), and
 * throttleWithTimeout() operators to only let the first or last element
 * in a rapid sequence of emissions through.
 */
public class Throttling {
    private final Observable<Long> infiniteSource1 = Observable.interval(100, TimeUnit.MILLISECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(300, TimeUnit.MILLISECONDS);
    private final Observable<Long> infiniteSource3 = Observable.interval(2, TimeUnit.SECONDS);

    /**
     * The throttleLast operator pushes the last emission at every fixed time interval.
     * <p>
     * Emits on the computation Scheduler, but you can specify your own Scheduler
     * as a third argument.
     */
    public final void throttleLast(long time, TimeUnit unit) {
        infiniteSource2.take(10)
                .throttleLast(time, unit)
                .subscribe(System.out::println);

        // with another Scheduler
        infiniteSource2.take(10)
                .throttleLast(time, unit, Schedulers.io())
                .subscribe(System.out::println);
    }

    /**
     * The throttleFirst operator pushes the first emission at every fixed time interval.
     * <p>
     * Emits on the computation Scheduler, but you can specify your own Scheduler
     * as a third argument.
     */
    public final void throttleFirst(long time, TimeUnit unit) {
        infiniteSource2.take(10)
                .throttleFirst(time, unit)
                .subscribe(System.out::println);
    }

    /**
     * Also called "debounce()".
     * Accepts time-interval arguments that specify how long
     * a period of inactivity (which means no emissions are coming
     * from the source) must be before the last emission can be pushed
     * forward.
     * throttleWithTimeout() is an effective way to handle excessive inputs
     * (such as a user clicking on a button rapidly) and other noisy, redundant
     * events that sporadically speed up, slow down, or cease.
     * <p>
     * The only disadvantage of throttleWithTimeout() that it delays each winning
     * emission.
     */
    public final void throttleWithTimeout(long time, TimeUnit unit) {
        infiniteSource2.take(10)
                .throttleWithTimeout(time, unit)
                .subscribe(System.out::println);
    }
}
