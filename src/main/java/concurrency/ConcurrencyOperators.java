package concurrency;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * These operators are used to dealing with concurrency issues.
 */
public class ConcurrencyOperators {
    private final Observable<String> dateSource1 = Observable.just("1/3/2016", "5/9/2018", "7/12/2021");
    private final Observable<String> dateSource2 = Observable.just("4/7/2017", "10/5/2021", "7/1/2022");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);
    private final Observable<Long> infiniteSource1 = Observable.interval(500, TimeUnit.MICROSECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(400, TimeUnit.MICROSECONDS);

    /**
     * Specifies which Scheduler the source Observable should
     * use, and it will use a worker from this Scheduler to push
     * emissions all the way to the final Observer.
     * You can put subscribeOn() anywhere in the  Observable chain.
     * <p>
     * subscribeOn() operator has no practical effect with certain sources.
     * This might be because an Observable already uses a Scheduler!
     * For example, Observable.interval() will use Schedulers.computation()
     * and will ignore any subscribeOn() you specify.
     */
    public final void subscribeOn() {
//        1)
        stringSource.subscribeOn(Schedulers.computation())
                .map(String::length)
                .subscribe(item -> System.out.println("Received " + item + " on thread " + Thread.currentThread().getName()));
//        2) for Observable.interval() you should provide a Scheduler as a third argument to specify a different Scheduler.
        Observable.interval(500, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .take(10)
                .subscribe(item -> System.out.println("Received " + item + " on thread " + Thread.currentThread().getName()));
    }

    /**
     * observeOn() operator will intercept emissions at that point in the Observable chain
     * and switch them to a different Scheduler going forward.
     * The placement of observeOn() matters! Operator intercepts each emission and pushes it
     * forward on a different Scheduler. Upstream operators before observeOn() are NOT impacted,
     * but downstream ones are.
     * <p>
     * This operator can be useful for switching to UI thread for updating
     * the interface.
     * <p>
     * The observeOn() operator comes with nuances to be aware of, especially when
     * it comes to performance implications due to lack of backpressure.
     */
    public final void observeOn() {
//        1)
        var disposable = Observable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .map(item -> 100 * item)
                .take(10)
                .doOnNext(item -> System.out.println("Current thread: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.io())
                .subscribe(item -> System.out.println("Current thread: " + Thread.currentThread().getName()));
    }

    /**
     * Disposing the Observable can be an expensive operation. If this is a UI thread, this can
     * cause an undesirable UI freezing. You can use an operator unsubscribeOn() to prevent this
     * behavior.
     * unsubscribeOn() helps us to specify a different Scheduler to dispose of operations on,
     * preventing subtle hang-ups on threads we want to keep free and available even if they call
     * the dispose() method.
     */
    public final void unsubscribeOn() {
        //        1)
        var disposable = Observable.interval(100, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .map(item -> 100 * item)
                .take(10)
                .doOnNext(item -> System.out.println("Current thread: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.io())
                .doOnDispose(() -> System.out.println("onDispose: thread " + Thread.currentThread().getName()))
                .unsubscribeOn(Schedulers.computation()) // disposing in Schedulers.computation()
                .subscribe(item -> System.out.println("Current thread: " + Thread.currentThread().getName()));
        sleep(3000);
        disposable.dispose();
        sleep(5000);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
