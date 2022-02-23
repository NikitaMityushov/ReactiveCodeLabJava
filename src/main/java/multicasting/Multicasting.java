package multicasting;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Forces emissions from the source to become hot, pushing a single stream of emissions
 * to all observers at the same time, rather than giving a separate stream to each Observer.
 * Multicasting is helpful when you need to send the same data to several observers.
 * If the emitted data has to be processed the same way for each Observer, do it BEFORE calling
 * publish(see the example below).
 * Create hot ConnectableObservable.
 */
public class Multicasting {
    private final Observable<Integer> intSource1 = Observable.range(1, 3);
    private final Observable<Integer> intSource2 = Observable.range(1, 3);
    private final Observable<Long> infiniteSource1 = Observable.interval(1, TimeUnit.SECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(300, TimeUnit.MILLISECONDS);

    /*
        1) In this example we use a pair publish()-connect()
     */
    public final void publish() {
        var obs = intSource1.map(i -> randomInt())
                .publish(); // for valid multicasting publish() must stay after transforming operators!!!
        obs.subscribe(i -> System.out.println("Subscriber 1, number: " + i));
        obs.subscribe(i -> System.out.println("Subscriber 2, number: " + i));
        obs.connect(); // don't forget calling connect()  after subscriptions to emit values from Observable
    }

    /**
     * 2) autoConnect(int numberOfSubscribers) operator on ConnectableObservable returns
     * an Observable<T> that will automatically call connect after a specified number of
     * Observers are subscribed.
     * <p>
     * Obviously, this does not work well when you have an unknown number of observers.
     * Even if all observers finish or been disposed of, the autoConnect() persist its
     * subscriptions to the source. If source finite and disposed of, autoConnect() does not
     * subscribe to source again when a new Observer subscribes downstream.
     * <p>
     * You can pass 0 to autoConnect(0). It will start firing immediately and not wait for
     * any Observers. This can be handy to start firing emissions without waiting for any
     * subscription.
     * autoConnect() without arguments defaults to one subscriber only.
     */
    public final void autoConnect() {
        var obs = intSource2.map(i -> randomInt()).publish().autoConnect(2);
        obs.subscribe(i -> System.out.println("Subscriber 1, number: " + i));
        obs.subscribe(i -> System.out.println("Subscriber 2, number: " + i));
    }

    /**
     * Similar to autoConnect(1), but after all its observers have been disposed of, it disposes of itself and
     * starts over when a new subscription comes on. It does not persist the subscription to the source when it has
     * no subscribers. When another subscription happens, it essentially STARTS OVER.
     * <p>
     * You can also use an alias to publish().refCount() using the share() operator.
     */
    public final void refCount() {
        var obs = intSource2.map(i -> randomInt()).publish().refCount();
        obs.subscribe(i -> System.out.println("Subscriber 1, number: " + i));
        sleep(1000);
        obs.subscribe(i -> System.out.println("Subscriber 2, number: " + i));
        sleep(1000);
    }

    /**
     * Multicasting allows you caching values that are shared across multiple observers.
     * replay() operator is a powerful way to hold onto previous emissions and re-emit
     * them when a new Observer comes in. It returns ConnectableObservable, which both
     * multicasts emissions and emits previous emissions. It emits the cached values
     * immediately when new Observer subscribes, so it is caught up, and then it fires current
     * emissions from that point forward.
     * Can be very expensive with memory, as replay() will keep caching all emissions it
     * receives.
     * Note, if you want to persist the cached values in your replay() even if there are
     * no subscriptions, use it in conjunction with autoConnect(), not refCount().
     */
    public final void replay() {
//        1) replay without arguments replays all previous emissions to tardy observers and then emit current emissions as
//        the tardy Observer is caught up.
        System.out.println("1) without args.");
        var source = infiniteSource1.replay().autoConnect();
        source.subscribe(item -> System.out.println("Observer 1: " + item));
        sleep(3000);
        source.subscribe(item -> System.out.println("Observer 2: " + item));
        sleep(3000);
//        2) replay(int bufferSize) use to limit to replaying only a certain number of LAST emissions.
        System.out.println("2) With args(2): ");
        var source2 = infiniteSource1.replay(2).autoConnect();
        source2.subscribe(item -> System.out.println("Observer 3: " + item));
        sleep(3000);
        source2.subscribe(item -> System.out.println("Observer 4: " + item));
        sleep(3000);
//        3) Time-based replay(TimeUnit unit)
        var source3 = infiniteSource2.map(item -> (item + 1) * 300)
                .replay(1, TimeUnit.SECONDS).autoConnect();
        source3.subscribe(item -> System.out.println("Observer 5: " + item));
        sleep(2000);
        source3.subscribe(item -> System.out.println("Observer 6: " + item));
        sleep(1000);
//        4) Time-based and buffered replay(int bufferSize, TimeUnit unit), in addition to time interval
//        so only a certain number of last emissions are buffered within that time period.
        var source4 = infiniteSource2.map(item -> (item + 1) * 300)
                .replay(1, 1, TimeUnit.SECONDS).autoConnect();
        source4.subscribe(item -> System.out.println("Observer 7: " + item));
        sleep(2000);
        source4.subscribe(item -> System.out.println("Observer 8: " + item));
        sleep(1000);
    }

    /**
     * When you want to cache all emissions indefinitely for the long term and do not need to control the
     * subscription behavior to the source with ConnectableObservable, you can use the cache() operator.
     * This will subscribe to the source on the first downstream Observer that subscribes and hold all
     * values indefinitely.
     * This can exhaust the memory.
     * Do not use this! :)
     */
    public final void cache() {
//        1) cache
        var cacheRollingTotals1 = Observable.just(2, 5, 7, 9, 11)
                .scan((total, next) -> total + next)
                .cache();
        cacheRollingTotals1.subscribe(System.out::println);
//        2) cacheWithInitialCapacity(int expectedBufferSize)
        var cacheRollingTotals2 = Observable.just(2, 5, 7, 9, 11)
                .scan((total, next) -> total + next)
                .cacheWithInitialCapacity(5);
        cacheRollingTotals2.subscribe(System.out::println);
    }

    /*
     * private utility methods
     */
    private static int randomInt() {
        return ThreadLocalRandom.current().nextInt(100000);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
