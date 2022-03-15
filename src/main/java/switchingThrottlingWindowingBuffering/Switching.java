package switchingThrottlingWindowingBuffering;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * There is a powerful operator called switchMap(). Its usage is similar
 * to flatMap(), but it has one important behavioral difference: it emits
 * the latest Observable derived from the latest emission and disposes of any
 * previous observables that were processing. In other words, it allows you to
 * cancel an emitting Observable and switch to a new one, thereby preventing
 * stale or redundant processing.
 */
public class Switching {
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta",
            "Eta", "Theta", "Iota");
    private final Observable<Long> infiniteSource2 = Observable.interval(1200, TimeUnit.MILLISECONDS);

    /**
     * For switchMap() to work effectively, the thread pushing emissions into switchMap()
     * cannot be occupied while doing the work inside switchMap().
     */
    public final void switchMap() {
        var source = stringSource.concatMap(s -> Observable.just(s)
                .delay(randomSleepTime(), TimeUnit.MILLISECONDS));

        Observable.interval(5, TimeUnit.SECONDS)
                .switchMap(i -> source.doOnDispose(() -> System.out.println("Disposing! Starting next")))
                .subscribe(System.out::println);
    }

    /**
     * This can be helpful to instantly send search requests or autocomplete
     * suggestions while the user is typing.
     */
    public final void groupingKeystrokes() {
        stringSource.throttleWithTimeout(500, TimeUnit.MILLISECONDS)
                .delay(1200, TimeUnit.MILLISECONDS)
                .switchMap(s -> {
                    System.out.println("The last emitted: " + s);
                    return stringSource.scan((total, item) -> total + item);
                })
                .observeOn(Schedulers.newThread())
                .subscribe(System.out::println);
    }

    // private methods
    // returns random sleep time between 0 and 2000 milliseconds
    private int randomSleepTime() {
        return ThreadLocalRandom.current().nextInt(3000);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
