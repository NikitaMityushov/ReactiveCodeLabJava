package combiningOperators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Allows you to take an emitted value from each Observable source and combine them into a single
 * emission. Each Observable can emit a different type, but you can combine these types into
 * single emission.
 * One-to-one pairing.
 */
public class ZippingOperators {
    private final Observable<String> dateSource2 = Observable.just("4/7/2017", "10/5/2021", "7/1/2022");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);

    /**
     * The emission from one Observable must wait to get paired with an emission from the
     * other Observable. If one calls onComplete() and the other still has emissions waiting
     * to get paired, those emissions will simply be dropped.
     * <p>
     * Zipping can also be helpful in SLOWING DOWN EMISSIONS using Observable.interval() for example.
     */
    public final void zip() {
        Observable.zip(dateSource2, numericSource, (d, i) -> d + " - " + i)
                .subscribe(System.out::println);
    }
}
