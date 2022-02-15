import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class SuppressingOperators {
    private Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Beta");

    /**
     * The filter() operator accepts Predicate<T>.
     * May return empty Observable with no emissions before onComplete() is called.
     */
    public void filter() {
        source.filter(item -> item.length() < 5)
                .subscribe(System.out::println);
    }

    /**
     * The take() takes the specified number of emissions and calls on onComplete() after all of
     * them reach it. It will also dispose of entire subscription so that no more emissions will occur.
     * Second version of take() accepts the specific time duration and then emits onComplete.
     * takeLast() takes last specified number of emissions or time duration before onComplete is generated.
     */
    public void take() {
        // 1)
        source.take(2)
                .subscribe(System.out::println);
        // 2)
        var observable = Observable.interval(50, TimeUnit.MILLISECONDS);
        observable.take(400, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);

        // 3)
        source.takeLast(2)
                .subscribe(System.out::println);
    }

    /**
     * skip() operator is opposite of the take(). It ignores the specified number of emissions or accepts
     * time duration.
     * skipLast() delays last specified number of emissions in that scope.
     */
    public void skip() {
        // 1)
        source.skip(1)
                .subscribe(System.out::println);
        // 2)
        var observable = Observable.interval(50, TimeUnit.MILLISECONDS);
        observable.skip(400, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);

        // 3)
        source.skipLast(2)
                .subscribe(System.out::println);

    }

    /**
     * The distinct() operator emits only unique emissions.
     * Equality based on the hashcode() and equals().
     * distinct(Function<T,K> keySelector) accepts a function that maps each emission to a key used for
     * equality logic. Then uniqueness of each emitted item is based on the uniqueness of this generated key not hte item itself.
     */
    public void distinct() {
//        1)
        source.distinct()
                .subscribe(System.out::println);
//        2)
        source.distinct(String::length)
                .subscribe(System.out::println);
    }

    /**
     * The distinctUntilChanged() function ignores consecutive duplicate emissions. If
     * the same value is being emitted repeatedly, all the duplicates are ignored until a new value is emitted.
     * Like with distinct(), you can use lambda for key generation.
     */
    public void distinctUntilChanged() {
        var source = Observable.just(1, 1, 3, 3, 15, 15);

//        1)
        source.distinctUntilChanged()
                .subscribe(System.out::println);
//        2)
        source.distinctUntilChanged(item -> item.toString().length())
                .subscribe(System.out::println);
    }

    /**
     * You can get a specific emission by its index specified by the long value, starting at 0.
     * After the item is found and emitted, onComplete() is called ad the subscription is disposed of.
     * Returns Maybe<T> because it yields ane emission, but if there are fewer emissions than the index sought, it will be empty.
     * Other flours: elementAtOrError() returns a Single and emits an error if an element at thar index is not found.
     *               singleElement() turns an Observable into a Maybe, but produces an error if there is more than oe element.
     *               firstElement() and lastElement() emit the first and the last items, respectively.
     */
    public void elementAt() {
//        1)
        source.elementAt(2)
                .subscribe(System.out::println);

//        2)
        source.firstElement()
                .subscribe(System.out::println);
//        3)
        source.lastElement()
                .subscribe(System.out::println);
    }
}
