package combiningOperators;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Concatenating is similar to merging, but with an important nuance: it emits
 * items of each provided Observable sequentially and in the order specified.
 * It does not move on to the next Observable until the current one calls onComplete().
 * You should prefer concatenation when you want to guarantee that the Observable instances
 * fire their emission in the specified order.
 */
public class ConcatenatingFactoriesAndOperators {
    // sources
    private final Observable<String> dateSource1 = Observable.just("1/3/2016", "5/9/2018", "7/12/2021");
    private final Observable<String> dateSource2 = Observable.just("4/7/2017", "10/5/2021", "7/1/2022");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);
    private final Observable<Long> infiniteSource1 = Observable.interval(500, TimeUnit.MICROSECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(400, TimeUnit.MICROSECONDS);

    /**
     * Concatenation equivalent to Observable.merge(). It will combine the emitted values of multiple
     * Observables, but will fire each one sequentially and only move to the next after onComplete() is called.
     * If we use concat() with infinite observables, it will forever emit from the first one it encounters and
     * prevent any following. If we ever want to put an infinite Observable anywhere in concatenation, it should
     * be listed last! We can also use take() operator to make an infinite Observable finite.
     * <p>
     * There are also concatMapIterable(Observable<T>), concatMapEager()(operator that eagerly subscribes to
     * all Observable sources it receives and caches the emissions until it is their turn to emit).
     */
    public final void concat() {
//        1)
        Observable.concat(dateSource1, dateSource2)
                .subscribe(System.out::println);
    }

    /**
     *
     */
    public final void concatWith() {
//        1)
        dateSource1.concatWith(dateSource2)
                .subscribe(System.out::println);
//        2) with infinite Observable
        infiniteSource1.take(3).concatWith(infiniteSource2.take(4))
                .subscribe(System.out::println);
    }

    /**
     * Like flatMap() dynamically merges observables, there is a concatenation
     * counterpart called concatMap().
     * Use this operator if you care about ordering and want each Observable being mapped from
     * each emission before starting the next one.
     * More specifically, concatMap() merges each mapped Observable sequentially and fires them
     * one at a time. It moves to the next Observable when the current one calls onComplete().
     */
    public final void concatMap() {
        stringSource.concatMap(str -> Observable.fromArray(str.split("")))
                .subscribe(System.out::println);
    }
}
