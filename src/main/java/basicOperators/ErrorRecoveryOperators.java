package basicOperators;

import io.reactivex.rxjava3.core.Observable;

public class ErrorRecoveryOperators {
    private final Observable<String> dateSource = Observable.just("1/3/2016", "5/9/2018", "7/12/2020");
    private final Observable<String> stringSource = Observable.just("Alpha", "Beta", "Gamma", "Beta");
    private final Observable<Integer> numericSource = Observable.just(15, 2, 371, 232, 1, 0, 1000, 312);

    /**
     * When you want to resort to a default value when an exception occurs,
     * you can use the onErrorReturnItem() operator.
     */
    public final void onErrorReturnItem() {
        numericSource.map(item -> 10 / item)
                .onErrorReturnItem(-1)
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e));
    }

    /**
     * You can also use the onErrorReturn(Function<Throwable, T> valueSupplier) operator
     * to dynamically produce the value using the specified function. This gives you access
     * to a Throwable object, which you can use while calculating the returned value.
     * Note that emission is terminated after error.
     * If we want to resume emission, we can handle error in operator, where occurs error.
     */
    public final void onErrorReturn() {
//        1)
        numericSource.map(item -> 10 / item)
                .onErrorReturn(e -> e instanceof ArithmeticException ? -1 : 0)
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e));
//        2) want to resume emission
        numericSource.map(item -> {
                    try {
                        return 10 / item;
                    } catch (ArithmeticException e) {
                        return -1;
                    }
                })
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e));
    }

    /**
     * onErrorResumeNext() in RxJava 2.x.
     * It accepts another Observable as a parameter to emit potentially multiple values,
     * not a single value, in the event of the exception.
     */
    public final void onErrorResumeWith() {
//        1)
        numericSource.map(item -> 10 / item)
                .onErrorResumeWith(Observable.just(-1, 2, 3))
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e));
//        2) quietly stop emissions in the event that there is an error and
//           gracefully call the onComplete() function
        numericSource.map(item -> 10 / item)
                .onErrorResumeWith(Observable.empty())
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e));
//        3) Instead of another Observable you can provide the Function<Throwable, Observable<T>>
//        function to produce an Observable dynamically from the emitted Throwable.
        numericSource.map(item -> 10 / item)
                .onErrorResumeNext((Throwable e) -> Observable.just(7).repeat(3)) // note that onErrorResumeNext!!!
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e));
    }

    /**
     * retry() has several overloaded versions.
     * It will re-subscribe to the preceding Observable and. hopefully,
     * not have the error again.
     * If you call retry() without args, it'll resubscribe an infinite number
     * of times for each error.
     * It might be safer to specify retry() a fixed number of times before it gives up and
     * just emits the error to the Observer.
     * There are retryUntil(BooleanSupplier stop) and retryWhen()(can do delaying retries for example) advanced operators.
     */
    public final void retry() {
        numericSource.map(item -> 10 / item)
                .retry(2)
                .subscribe(System.out::println,
                        e -> System.out.println("Error: " + e));

    }
}
