package riddles;

import io.reactivex.rxjava3.core.Observable;

/**
 * Sum up the latest values of [first] and [second] whenever one of the Observables emits a new value.
 *
 * Use case: Two input fields in a calculator that need to be summed up and the result should be updated every time one of the inputs change.
 */
public class Riddle5 {
    public static Observable<Integer> solve(Observable<Integer> first, Observable<Integer> second) {
        return Observable.combineLatest(first, second, Integer::sum);
    }
}
