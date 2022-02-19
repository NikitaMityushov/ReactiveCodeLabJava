import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Supplier;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Class which describes all the ways to create Observable<T>
 *
 * @param <T>
 */
public class FactoryMethods<T> {
    /**
     * Observable.create()
     *
     * @param args is vararg of T type
     * @return Observable<T>
     */
    @SafeVarargs
    public final Observable<T> create(T... args) {
        return Observable.create(
                emitter -> {
                    try {
                        Arrays.stream(args).sequential().forEach(emitter::onNext);
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
        );
    }

    /**
     * Observable.fromIterable()
     *
     * @param value is any Iterable<T> of any type
     * @return Observable<T>
     */
    public final Observable<T> fromIterable(Iterable<T> value) {
        return Observable.fromIterable(value);
    }

    /**
     * Observable.just()
     *
     * @param item is T type value
     * @return Observable<T>
     */
    public final Observable<T> just(T item) {
        return Observable.just(item);
    }

    /**
     * Creates an Observable that emits a consecutive range of Integers. It emits each number from start
     * and increments each subsequent value by one until the specified count is reached.
     *
     * @param start is initial value
     * @param count is subsequent increment value
     * @return Observable<Integer>
     * Long equivalent is .rangeLong()
     */
    public final Observable<Integer> range(int start, int count) {
        return Observable.range(start, count);
    }


    /**
     * Creates an Observable that emits a consecutive long values starting at 0.
     * with the specified time interval between emissions.
     *
     * @param interval is interval
     * @param unit     is TimeUnit unit
     * @return Observable<Long>
     * Infinite stream.
     * Run on separate thread, Scheduler.computation() is default.
     * Cold.
     */
    public final Observable<Long> interval(long interval, TimeUnit unit) {
        return Observable.interval(interval, unit);
    }

    /**
     * Creates Observable<T> from Future<T>.
     *
     * @param future is Future<T> type.
     * @return Observable<T>
     */
    public final Observable<T> fromFuture(Future<T> future) {
        return Observable.fromFuture(future);
    }

    /**
     * Creates empty Observable emits nothing and calls onComplete Observer's method.
     *
     * @return Observable<T>
     * RxJava's concept of null.
     */
    public final Observable<T> empty() {
        return Observable.empty();
    }

    /**
     * Creates empty Observable emits nothing and leaves the observer waiting forever.
     *
     * @return Observable<T>
     */
    public final Observable<T> never() {
        return Observable.never();
    }

    /**
     * Creates empty Observable emits nothing and immediately generates an onError event with specified Exception.
     *
     * @return Observable<T>
     */
    public final Observable<T> error(Throwable throwable) {
        return Observable.error(throwable);
    }

    /**
     * Creates fresh Observable for each subscription.
     *
     * @return Observable<T>
     */
    public final Observable<T> defer(Supplier<ObservableSource<? extends T>> lambda) {
        return Observable.defer(lambda);
    }

    /**
     *
     * @param callable has type Supplier<? extends T>
     * @return
     */
    public final Observable<T> fromCallable(Callable<? extends T> callable) {
        return Observable.fromCallable(callable);
    }

}
