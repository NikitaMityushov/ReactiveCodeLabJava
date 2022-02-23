package basicOperators;

import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Operators that have diverse functionality that cannot be captured under the
 * specific functional title.
 */
public class UtilityOperators {
    private final Observable<String> dateSource = Observable.just("1/3/2016", "5/9/2018", "7/12/2020");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);
    private final Observable<Long> infiniteSource = Observable.interval(2, TimeUnit.SECONDS);

    /**
     * Can postpone emissions using the delay() operator. It will hold any received emissions and
     * delay each one for the specified time period.
     * Operates on a different scheduler.
     */
    public final void delay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:ss");
        System.out.println(LocalDateTime.now().format(formatter));
        stringSource.delay(3, TimeUnit.SECONDS)
                .subscribe(str -> System.out.println(LocalDateTime.now().format(formatter) + " Received: " + str));
        sleep(5000);
    }

    /**
     * repeat() operator will repeat subscription after onComplete() a specified
     * number of times.
     * If you do not specify a number, it will repeat infinitely, forever resubscribing
     * after every onComplete().
     * There is also a repeatUntil() operator that accepts a BooleanSupplier function
     * and continues repeating until the provided function returns true.
     */
    public final void repeat() {
        stringSource.repeat(3)
                .subscribe(System.out::println);
    }

    /**
     * The single() operator returns a Single that emits the item emitted by
     * this Observable.
     * If the Observable emits more than one item, the single()
     * operator throws an exception.
     * If the Observable emits no item, the Single(),
     * produced by single() operator, emits the item passed to the operator as a
     * parameter.
     * There is also singleElement() operator that returns Maybe when the Observable
     * emits one item or nothing and throws an exception otherwise.
     * SingleOnError() operator that returns Single when the Observable emits one item
     * only and throws an exception otherwise.
     */
    public final void single() {
//        1) with one item
        Observable.just("one")
                .single("four")
                .subscribe(System.out::println);
//        2) with more than one
        Observable.just("one", "two")
                .single("four")
                .onErrorReturnItem("-1")
                .subscribe(System.out::println,
                        Throwable::printStackTrace);
//        3) with no items
        Observable.empty()
                .single("four")
                .subscribe(System.out::println);
    }

    /**
     * The timestamp() operator attaches a timestamp to every item emitted
     * by an Observable. The result are wrapped inside the object of the Timed
     * class, which provide accessors to the values that we can unwrap.
     */
    public final void timestamp() {
//        1) wrap variant
        stringSource.timestamp(TimeUnit.SECONDS)
                .subscribe(System.out::println);
//        2) unwrap variant
        stringSource.timestamp(TimeUnit.SECONDS)
                .subscribe(item -> System.out.println("Received: "
                        + item.time() + " " + item.unit() + " " + item.value()));
    }

    /**
     * The timeInterval() operator emits the time lapses between the
     * consecutive emissions of a source Observable.
     * The result are wrapped inside the object of the Timed
     * class, which provide accessors to the values that we can unwrap.
     */
    public final void timeInterval() {
//        1) wrap
        infiniteSource.doOnNext(i -> System.out.println("Emitted item: " + i))
                .take(3)
                .timeInterval(TimeUnit.SECONDS)
                .subscribe(System.out::println);
        sleep(7000);

//        2) unwrap
        infiniteSource.doOnNext(i -> System.out.println("Emitted item: " + i))
                .take(3)
                .timeInterval(TimeUnit.SECONDS)
                .subscribe(item -> System.out.println("Received: "
                        + item.time() + " " + item.unit() + " " + item.value()));
        sleep(7000);
    }

    /*
     * private utility methods
     */
    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
