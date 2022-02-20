package combiningOperators;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 *
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
     *
     */
    public final void concat() {

    }

    /**
     *
     */
    public final void concatWith() {

    }
}
