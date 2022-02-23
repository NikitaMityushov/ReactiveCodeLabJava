package combiningOperators;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Grouping emissions by a specified key into separate Observables.
 */
public class GroupingOperators {
    // sources
    private final Observable<String> dateSource1 = Observable.just("1/3/2016", "5/9/2018", "7/12/2021");
    private final Observable<String> dateSource2 = Observable.just("4/7/2017", "10/5/2021", "7/1/2022");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);
    private final Observable<Long> infiniteSource1 = Observable.interval(500, TimeUnit.MILLISECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(1, TimeUnit.MILLISECONDS);

    /**
     * groupBy(Function<T,K> keySelector) operator accepts a function that maps each emission to a key.
     * It will return an Observable<GroupedObservable<K,T>>, which emits a special type of Observable
     * called GroupedObservable. It has the key K value accessible as a property. It emits T type values
     * that are mapped for that given key.
     */
    public final void groupBy() {
        var byLength = stringSource.groupBy(item -> item.length());
        byLength.flatMapSingle(item -> item.toList())
                .subscribe(System.out::println);
    }
}
