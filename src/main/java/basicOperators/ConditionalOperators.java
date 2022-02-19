package basicOperators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Conditional operators emit or transform Observable conditionally.
 */
public class ConditionalOperators {
    // creating Observable emits integer from 0 to 99
    private final Observable<Integer> source = Observable.range(0, 100);

    /**
     * takes emissions while a condition derived from each emission is true.
     */
    public final void takeWhile() {
        source.takeWhile(item -> item < 5)
                .subscribe(System.out::println);
    }

    /**
     * skips emissions while a condition derived from each emission is true.
     */
    public final void skipWhile() {
        source.skipWhile(item -> item < 95)
                .subscribe(System.out::println);
    }

    /**
     * Accepts another Observable as a parameter.
     * It keeps taking emissions until that other Observable pushes an emission.
     */
    public final void takeUntil() {
        var anotherObservable = Observable.range(1000, 3);
        source.takeUntil(anotherObservable)
                .subscribe(System.out::println);
    }

    /**
     * Accepts another Observable as a parameter.
     * It keeps skipping emissions until that other Observable emits something.
     */
    public final void skipUntil() {
        var anotherObservable = Observable.range(1000, 3);
        source.skipUntil(anotherObservable)
                .subscribe(System.out::println);
    }

    /**
     * If an Observable turns out to be empty, we can use defaultEmpty() method for emitting default value.
     */
    public final void defaultIfEmpty() {
        source.filter(item -> item > 100)
                .defaultIfEmpty(0)
                .subscribe(System.out::println);
    }

    /**
     * specifies a different Observable to emit values from if source Observable is empty rather than emitting
     * just one value, as in case of defaultIfEmpty
     */
    public final void switchIfEmpty() {
        var anotherObservable = Observable.range(1000, 3);
        source.filter(item -> item > 100)
                .switchIfEmpty(anotherObservable)
                .subscribe(System.out::println);
    }
}
