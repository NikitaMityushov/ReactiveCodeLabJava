package combiningOperators;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * A common task done in ReactiveX is taking two or more Observable<T> instances and merging
 * them into one Observable<T>.
 */
public class MergingFactoriesAndOperators {
    private final Observable<String> dateSource1 = Observable.just("1/3/2016", "5/9/2018", "7/12/2021");
    private final Observable<String> dateSource2 = Observable.just("4/7/2017", "10/5/2021", "7/1/2022");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);
    private final Observable<Long> infiniteSource1 = Observable.interval(500, TimeUnit.MICROSECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(400, TimeUnit.MICROSECONDS);

    /**
     * This factory will take two or more Observable<T> sources emitting the same type T and then
     * consolidate them into a single Observable<T>.
     * It will subscribe to all the specified sources simultaneously, but will likely fire the
     * emissions in order if they are cold and on the same thread. This just an implementation detail
     * that is not guaranteed to work the same way every time. If you want to fire elements of each
     * Observable sequentially and keep their emissions in sequential order, you should use Observable.concat().
     */
    public final void merge() {
//        1)
        Observable.merge(dateSource1, dateSource2)
                .subscribe(System.out::println);
//        2) merge array
        Observable.mergeArray(dateSource1, dateSource2)
                .subscribe(System.out::println);
//        3) There is also overloaded version that accepts Iterable<Observable<T>> and produces the same result in a more type-safer manner
        var list = Arrays.asList(dateSource1, dateSource2);
        Observable.merge(list)
                .subscribe(System.out::println);
//        4) Merge infinite Observables
        System.out.println("4) Infinite Observables: \n");
        Observable.merge(infiniteSource1, infiniteSource2)
                .subscribe(System.out::println);

    }

    /**
     * Alternatively, you can use mergeWith() which the operator version of Observable.merge().
     */
    public final void mergeWith() {
        dateSource1.mergeWith(dateSource2)
                .subscribe(System.out::println);
    }

    /**
     * flatMap(Function<T, Observable<R>> mapper)
     * Take each emission and mapping it to an Observable. Then it merges the resulting observables
     * into a single stream.
     * One emission to many emissions.
     * Can map emissions to infinite instances of Observable and merge them.
     * There is a flatMapIterable operator to map each T value into an Iterable<R> instead Observable<R>.
     * There are also flatMapSingle(), flatMapMaybe() and flatMapCompletable() operators.
     */
    public final void flatMap() {
//        1)
        stringSource.flatMap(str -> Observable.fromArray(str.split("")))
                .subscribe(System.out::println);
//        2) Here is another example: take a sequence of Strings separated by "/", use flatMap and filter only num values
        Observable.just("812349823/234234/dfgsdfsd", "27834/etier/2342342")
                .flatMap(str -> Observable.fromArray(str.split("/")))
                .filter(str -> str.matches("[0-9]+"))
                .map(Integer::valueOf)
                .subscribe(System.out::println);
//        3) Receive simple Integers from Observable, but use flatMap to drive an Observable.interval() where each Integer value serves as the period
//        System.out.println("flatMap produces infinite Observables: \n");
//        numericSource.flatMap(item -> Observable.interval(item, TimeUnit.MILLISECONDS).map(i -> "Interval: " + item + " and i= " + i))
//                .subscribe(System.out::println); временно закомментировал
        /**
         * 4)
         * flatMap(Function<T,Observable<R>> mapper, BiFunction<T,U,R> combiner) with combiner.
         * Allows the provision of a combiner along with the mapper function.
         */
        System.out.println("flatMap() with combiner: \n");
        stringSource.flatMap(str -> Observable.fromArray(str.split("")), (a, b) -> a + "-" + b)
                .subscribe(System.out::println);
    }
}
