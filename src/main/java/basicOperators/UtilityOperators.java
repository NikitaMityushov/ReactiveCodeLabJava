package basicOperators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Operators that have diverse functionality that cannot be captured under the
 * specific functional title.
 */
public class UtilityOperators {
    private final Observable<String> dateSource = Observable.just("1/3/2016", "5/9/2018", "7/12/2020");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);

    /**
     *
     */
    public final void delay() {

    }

    /**
     *
     */
    public final void repeat() {

    }

    /**
     *
     */
    public final void single() {

    }

    /**
     *
     */
    public final void timestamp() {

    }

    /**
     *
     */
    public final void timeInterval() {

    }


}
