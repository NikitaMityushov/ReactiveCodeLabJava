package combiningOperators;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Contains Observable.combineLatest() and withLatestFrom() methods.
 */
public class CombiningTheLatestOperators {
    // sources
    private final Observable<String> dateSource1 = Observable.just("1/3/2016", "5/9/2018", "7/12/2021");
    private final Observable<String> dateSource2 = Observable.just("4/7/2017", "10/5/2021", "7/1/2022");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);
    private final Observable<Long> infiniteSource1 = Observable.interval(500, TimeUnit.MILLISECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(1, TimeUnit.MILLISECONDS);

    /**
     * Factory method Observable.combineLatest().
     * For every emission from one of sources, it will immediately couple up with the
     * latest emission from every other source.
     * When one source fires, it couples with the latest emissions from the others.
     * Especially helpful in combining UI inputs as previous user inputs are frequently
     * irrelevant and only the latest is of concerns.
     */
    public final void combineLatest() {
        Observable.combineLatest(infiniteSource1, infiniteSource2, (i1, i2) -> "Source 1 " + i1 + " . Source 2 " + i2)
                .subscribe(System.out::println);
    }

    /**
     * The method withLatestFrom() will map each T emission with the latest values from
     * other Observables and combine them, but it will only take one emission from each
     * of the other Observables.
     */
    public final void withLatestFrom() {
        infiniteSource1.withLatestFrom(infiniteSource2, (i1, i2) -> "Source 1 " + i1 + " . Source 2 " + i2)
                .subscribe(System.out::println);
    }
}
