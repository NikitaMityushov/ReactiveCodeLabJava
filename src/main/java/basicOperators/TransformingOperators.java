package basicOperators;

import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class TransformingOperators {

    private final Observable<String> dateSource = Observable.just("1/3/2016", "5/9/2018", "7/12/2020");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 1000, 312);

    /**
     * map() transforms an emitted value of the T type into a value of the R type,
     * that may or may not be the same type, using the Function<T,R> lambda expression provided.
     * Does a one-to-one conversion of each emitted value.
     */
    public void map() {
        var formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        dateSource.map(item -> LocalDate.parse(item, formatter))
                .subscribe(System.out::println);
    }

    /**
     * cast(Class<Object> cls) is a simple, map-like operator that casts each emitted item to another type.
     * Use instead of for example map(item -> (Object) item)
     * Effective brute-force way to cast everything down to a common base type.
     */
    public void cast() {
        stringSource.cast(Object.class);
    }

    /**
     * This operator called startWith() in RxJava 2.x.
     * Allows to insert a value of type T that will be emitted before
     * all the other values.
     * If you want to start with more than one value emitted first, ust starWithArray(),
     * which accepts varargs.
     * The same result can be achieved using startWithIterable().
     */
    public void startWithItem() {
        stringSource.startWithItem("First")
                .subscribe(System.out::println);
    }

    /**
     * If Observable emits items that are implements Comparable<T>, you can use sorted()
     * to sort the emissions.
     * There is an overloaded version which accepts Comparator<T>.
     */
    public void sorted() {
        System.out.println("With comparable: ");
        numericSource.sorted()
                .subscribe(System.out::println);

        System.out.println("With comparator: ");
        numericSource.sorted(Comparator.reverseOrder())
                .subscribe(System.out::println);

        System.out.println("With comparator and lambda: ");
        stringSource.sorted(Comparator.comparingInt(String::length))
                .subscribe(System.out::println);
    }

    /**
     * It adds every emitted item to the provided accumulator and emits each incremental
     * accumulated value.
     * May use it for rolling sums, string concatenations or boolean reductions.
     * You can effectively use scan() as a counter.
     */
    public void scan() {
        System.out.println("Rolling sum:");
        numericSource.scan((acc, i) -> acc + i)
                .subscribe(System.out::print);
        System.out.println("\nCounter: ");
        stringSource.scan(0, (acc, item) -> acc + 1)
                .takeLast(1)
                .subscribe(System.out::println);
    }
}
