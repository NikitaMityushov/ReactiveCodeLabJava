import basicOperators.ActionOperators;
import basicOperators.ErrorRecoveryOperators;
import basicOperators.FactoryMethods;
import basicOperators.UtilityOperators;
import combiningOperators.*;
import concurrency.ConcurrencyOperators;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import multicasting.Multicasting;
import switchingThrottlingWindowingBuffering.Buffering;
import switchingThrottlingWindowingBuffering.Switching;
import switchingThrottlingWindowingBuffering.Throttling;
import switchingThrottlingWindowingBuffering.Windowing;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        var operators = new Switching();
        operators.switchMap();
        sleep(20000);
    }

    public static void subscribeColdObservable() {
        System.out.println("1) Start subscribeColdObservable() method");
        FactoryMethods<String> observables = new FactoryMethods<>();

//        1) without custom observer
        var observable = observables.create("1", "2", "3", "5");
        observable.subscribe(System.out::println);

//        2) init Observer
        Observer<String> observer = createObserver();
        observable.subscribe(observer);

//        3) Shorthand Observers with Lambdas
        Consumer<String> onNext = i -> System.out.println("Without shorthands. Next value received by Observer2 is " + i);
        Consumer<Throwable> onError = Throwable::printStackTrace;
        Action onComplete = () -> System.out.println("Done without shorthands by Observer2");
        observable.subscribe(onNext, onError, onComplete);

        observable.subscribe(
                i -> System.out.println("With shorthands. Value received by Observer3 is " + i),
                Throwable::printStackTrace,
                () -> System.out.println("Done with shorthands by Observer3")
        );
//        prints that:
//        1) Start subscribeColdObservable() method
//        1
//        2
//        3
//        5
//        Received value by custom Observer1 is 1
//        Received value by custom Observer1 is 2
//        Received value by custom Observer1 is 3
//        Received value by custom Observer1 is 5
//        Done for custom Observer1 successfully!
//                Without shorthands. Next value received by Observer2 is 1
//        Without shorthands. Next value received by Observer2 is 2
//        Without shorthands. Next value received by Observer2 is 3
//        Without shorthands. Next value received by Observer2 is 5
//        Done without shorthands by Observer2
//        With shorthands. Value received by Observer3 is 1
//        With shorthands. Value received by Observer3 is 2
//        With shorthands. Value received by Observer3 is 3
//        With shorthands. Value received by Observer3 is 5
//        Done with shorthands by Observer3

    }

    /**
     * ConnectableObservable makes all emissions at once.
     * Hot.
     * If new subscriptions occur after connect() is called, they will have missed emissions fired earlier.
     */
    public static void subscribeWithHotObservable() {
        System.out.println("\n2) Start subscribeWithHotObservable() method");
        FactoryMethods<String> observables = new FactoryMethods<>();
        var observable = observables.create("Alpha", "Beta", "Gamma", "Trulala").publish(); // creates ConnectableObservable to make flow hot

        observable.subscribe(
                (item) -> System.out.println("Received item by Observer1 is " + item),
                Throwable::printStackTrace,
                () -> System.out.println("For Observer1 all is done!")
        );


        observable.connect(); // you need to call connect() method to start emission

        // this subscription occur after connect(), therefore it'll have missed emissions
        observable.subscribe(
                (item) -> System.out.println("Received item by Observer2 is " + item),
                Throwable::printStackTrace,
                () -> System.out.println("For Observer2 all is done!")
        );
//        prints that:
//        2) Start subscribeWithHotObservable() method
//        Received item by Observer1 is Alpha
//        Received item by Observer1 is Beta
//        Received item by Observer1 is Gamma
//        Received item by Observer1 is Trulala
//        For Observer1 all is done!
//                For Observer2 all is done!
    }

    public static void createIntervalObservable() {
        System.out.println("\n3) Start createIntervalObserver() method");
        var observable = new FactoryMethods<Long>();
        observable.interval(1, TimeUnit.SECONDS)
                .subscribe((item) -> System.out.println("Interval item is " + item));
        sleep(4000);
    }

    public static void createDeferObservable() {
        var start = 3;
        var count = 3;
        var observable = new FactoryMethods<Integer>();
        var source = observable.defer(() -> Observable.range(3, 3));
        source.subscribe((item) -> System.out.println("Defer observer1 item " + item));
        start = 10; // change the start value, Observer2 must see it.
        source.subscribe((item) -> System.out.println("Defer observer2 item " + item));
    }

    /**
     * Creates Observer<T>
     *
     * @param <T>
     * @return Observer<T>
     */
    public static <T> Observer<T> createObserver() {
        return new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
//                do nothing
            }

            @Override
            public void onNext(@NonNull T t) {
                System.out.println("Received value by custom Observer1 is " + t);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done for custom Observer1 successfully!");
            }
        };
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


