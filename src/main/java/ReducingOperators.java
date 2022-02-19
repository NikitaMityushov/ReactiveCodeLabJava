import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Provided possibility of aggregation emitted items into a single value(usually emitted through a Single).
 * Works only with finite loop.
 */
public class ReducingOperators {
    private final Observable<String> dateSource = Observable.just("1/3/2016", "5/9/2018", "7/12/2020");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 1000, 312);

    /**
     * Counts the number of emitted items and emits the result through a Single once onComplete() is called.
     * If you need to count items in the infinity loop, consider using scan() to emit rolling count instead.
     */
    public final void count() {
        dateSource.count()
                .subscribe(System.out::println);

    }

    /**
     * Emit the final result when source Observable calls onComplete().
     * Depending on which overloaded version is used, it can yield Single or Maybe.
     * Seed should be immutable. If not - use collect() or seedWith()
     */
    public final void reduce() {
//        1) Rolling sum
        numericSource.reduce((total, item) -> total + item)
                .subscribe(System.out::println);
//        2) Initial value(seed) and concat to String
        numericSource.reduce("", (total, item) -> total + (total.equals("") ? "" : ", ") + item)
                .subscribe(System.out::println);

    }
// _______________ Boolean Operators_______________________________________

    /**
     * Verifies that all emissions meet the specified criterion and returns a Single<Boolean> object.
     * If they pass all pass, it returns Single<Boolean> object that contains true.
     * If it encounters one value that fails the criterion, it immediately calls onComplete() and
     * returns false.
     * If empty Observable it returns Single<Boolean> true.
     */
    public final void all() {
        stringSource.all(item -> item.length() < 6)
                .subscribe(System.out::println);
    }

    /**
     * Checks whether at least one emission meets a specified criterion and returns a Single<Boolean>.
     * If empty Observable it returns Single<Boolean> false.
     */
    public final void any() {
        // convert to LocalDate type and check whether any are in the month of June or later:
        var formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        dateSource.map(item -> LocalDate.parse(item, formatter))
                .any(date -> date.getMonthValue() >= 6)
                .subscribe(System.out::println);
    }

    /**
     * Checks whether an Observable is going to emit more emits.
     * Returns a Single<Boolean> with true if the Observable does not emit items.
     */
    public final void isEmpty() {
        stringSource.filter(str -> str.contains("Z"))
                .isEmpty()
                .subscribe(System.out::println);
    }

    /**
     * Checks whether the specified item(based on the hashcode()/equals() implementations)
     * has been emitted by the source Observable.
     * Returns a Single<Boolean> with true if was emitted, false if not.
     */
    public final void contains() {
        numericSource.contains(232)
                .subscribe(System.out::println);
    }

    /**
     * Checks whether two observables emit the same values in the same order.
     * Returns Single<Boolean> true if the emitted sequences are the same pairwise.
     * The sequence of values emitted by the observables should be equal in size, values, and order for
     * returning Single<Boolean> that contains true. Otherwise false.
     */
    public final void sequenceEqual() {
        final var obs1 = Observable.just(1, 2, 3);
        final var obs2 = Observable.just(1, 2, 3);
        final var obs3 = Observable.just(3, 2, 1);

        // 1) true
        Observable.sequenceEqual(obs1, obs2)
                .subscribe(System.out::println);
        // 2) false
        Observable.sequenceEqual(obs1, obs3)
                .subscribe(System.out::println);
    }

}
