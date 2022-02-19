package basicOperators;

import io.reactivex.rxjava3.core.Observable;

/**
 * Action operators assists in debugging as well as getting visibility into
 * an Observable chain. They do not modify the Observable, but use it for side effects.
 */
public class ActionOperators {
    private final Observable<String> dateSource = Observable.just("1/3/2016", "5/9/2018", "7/12/2020");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);

    /**
     * Allows a peek at each received value before letting it flow into the next operator.
     * Does not affect the processing or transform the emission in any way.
     */
    public final void doOnNext() {
        stringSource.doOnNext(str -> System.out.println("Processing item: " + str))
                .map(String::length)
                .subscribe(System.out::println);
    }

    /**
     * Allows to perform an action after the item is passed downstream rather than before.
     */
    public final void doAfterNext() {
        stringSource.doAfterNext(str -> System.out.println("Processing item: " + str))
                .map(String::length)
                .subscribe(System.out::println);
    }

    /**
     * Allows firing off an action when an onComplete() event is emitted at the point in the
     * Observable chain.
     */
    public final void doOnComplete() {
        stringSource.doOnComplete(() -> System.out.println("Source is done emitting"))
                .map(String::length)
                .subscribe(System.out::println);
    }

    /**
     * Will peek at the error being emitted up the chain, and you can
     * perform an action with it. This can be helpful to put between operators
     * to see which one is to blame for an error.
     * <p>
     * There is also a doOnTerminate() operator, which fires for an onComplete
     * or onError event(but before the event), and the doAfterTerminate() operator,
     * which fires for an onComplete or onError event too, but only after event.
     */
    public final void doOnError() {
        numericSource.doOnError(e -> System.out.println("Source failed"))
                .map(item -> 10 / item)
                .doOnError(e -> System.out.println("Division failed"))
                .subscribe(
                        item -> System.out.println(item),
                        e -> System.out.println("Error: " + e)
                );
    }

    /**
     * Operator is very similar to doOnNext(). The only difference is that the emitted item comes
     * wrapped inside a Notification that also contains the type of the event.
     * You can check which of the three events -onNext(), onComplete(), onError() - has happened
     * and select an appropriate action.
     */
    public final void doOnEach() {
        stringSource.doOnEach(s -> System.out.println("doOnEach: " + s.getError() + ", " + s.getValue()))
                .subscribe(System.out::println);
    }

    /**
     * onSubscribe(Consumer<Disposable> onSubscribe) executes the function provided at the
     * moment subscription occurs. It provides access to the Disposable object in case
     * you want to call dispose() in that action.
     */
    public final void doOnSubscribe() {
        stringSource.doOnSubscribe(d -> System.out.println("Subscribing!"))
                .subscribe(System.out::println);
    }

    /**
     * doOnDispose(Action onDispose) operator performs the specified action when disposal is executed.
     */
    public final void doOnDispose() {
        var disposable = stringSource.doOnSubscribe(d -> System.out.println("Subscribing!"))
                .doOnDispose(() -> System.out.println("Disposing!!!"))
                .subscribe(System.out::println);

        try {
            Thread.sleep(3000);
            disposable.dispose();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Like doOnNext(), but for Single and Maybe
     */
    public final void doOnSuccess() {
        numericSource.reduce((total, item) -> total + item)
                .doOnSuccess(item -> System.out.println("Emitting: " + item))
                .subscribe(System.out::println);
    }

    /**
     * operator is executed when the onComplete(), onError(), or disposal happens.
     * It is executed after the same conditions as doAfterTerminate(), plus it is also
     * executed after the disposal.
     * The location of these operators in the chain does not matter, because they
     * are driven by the events, not by emitted data.
     */
    public final void doFinally() {
        stringSource.doFinally(() -> System.out.println("Do finally!"))
                .doAfterTerminate(() -> System.out.println("Do after terminate!"))
                .subscribe(System.out::println);
    }
}
