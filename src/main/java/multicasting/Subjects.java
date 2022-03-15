package multicasting;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.*;
import org.w3c.dom.ls.LSOutput;

import java.util.concurrent.TimeUnit;

/**
 * A Subject is both an Observer and an Observable, acting as a proxy multicasting device.
 * A Subject abstract class extends the Observable abstract class and implement the
 * Observer interface.
 * <p>
 * When to use subject:
 * You can use subject to eagerly subscribe to an unknown number of multiple source
 * observables and consolidate their emissions in a single Observable object.
 * Since Subject is an Observer too, you can pass it to a subscribe() method. This
 * can be helpful in the modularized code where decoupling between observables and
 * observers takes place and executing Observable.merge() is not that easy.
 * <p>
 * When a Subject goes wrong:
 * Since Subject is hot, executing onNext() calls before an Observer is set up would
 * result in these emissions being missed with our Subject.
 * If you want to make a source Observable object hot, it is much better to keep
 * it cold and to multicast using publish() or replay().
 * <p>
 * A critical gotcha to note with Subjects is this: the onSubscribe(). onNext(), onError(), and
 * onComplete() calls are not threadsafe! A good practice is call toSerialized() on the Subject
 * object to produce a safely serialized Subject implementation. This will safely sequentialize
 * concurrent event calls so that no train wreck occurs downstream.
 */
public class Subjects {

    private final Observable<Long> infiniteSource1 = Observable.interval(1, TimeUnit.SECONDS);
    private final Observable<Long> infiniteSource2 = Observable.interval(300, TimeUnit.MILLISECONDS);

    /**
     * A PublishSubject hotly broadcasts to the Observer objects that subscribe to it.
     */
    public final void publishSubject() {
        System.out.println("1)");
        Subject<String> subject1 = PublishSubject.create();
        subject1.map(String::length)
                .subscribe(System.out::println);

        subject1.onNext("Alpha");
        subject1.onNext("Beta");
        subject1.onNext("Gamma");
        subject1.onComplete();

        System.out.println("2)");
        var source1 = infiniteSource2.map(i -> ((i + 1) * 300) + " milliseconds");
        var source2 = infiniteSource1.map(i -> i + " seconds");
        Subject<String> subject2 = PublishSubject.create();
        subject2.subscribe(System.out::println);
        source2.subscribe(subject2);
        source1.subscribe(subject2);

    }

    /**
     * It behaves almost the same way as PublishSubject, but it also replays the last emitted
     * item to each new Observer downstream.
     */
    public final void behaviorSubject() {
        System.out.println("1)");
        Subject<String> subject1 = BehaviorSubject.create();
        subject1.subscribe(item -> System.out.println("Observer 1: " + item));

        subject1.onNext("Alpha");
        subject1.onNext("Beta");
        subject1.onNext("Gamma");
        subject1.subscribe(item -> System.out.println("Observer 2: " + item));
        subject1.onComplete();
    }

    /**
     * The ReplaySubject class behaves similar to PublishSubject followed by a cache() operator.
     * Just like using a replay() or cache() operator, you need to be wary of using this
     * with a large volume of emissions or infinite sources because it will cache them all
     * and take up memory.
     */
    public final void replaySubject() {
        System.out.println("1)");
        Subject<String> subject1 = ReplaySubject.create();
        subject1.subscribe(item -> System.out.println("Observer 1: " + item));

        subject1.onNext("Alpha");
        subject1.onNext("Beta");
        subject1.onNext("Gamma");
        subject1.subscribe(item -> System.out.println("Observer 2: " + item));
        subject1.onComplete();
    }

    /**
     * The AsyncSubject class has a highly tailored, finite-specific behavior:
     * it pushes only the last value it receives, followed by an onComplete() event.
     * You can also imitate AsyncSubject using takeLast(1).replay(1) on an Observable.
     */
    public final void asyncSubject() {
        System.out.println("1)");
        Subject<String> subject1 = AsyncSubject.create();
        subject1.subscribe(item -> System.out.println("Observer 1: " + item),
                Throwable::printStackTrace,
                () -> System.out.println("Observer 1 done!"));

        subject1.onNext("Alpha");
        subject1.onNext("Beta");
        subject1.onNext("Gamma");
        subject1.subscribe(item -> System.out.println("Observer 2: " + item),
                Throwable::printStackTrace,
                () -> System.out.println("Observer 2 done!"));
        subject1.onComplete();
    }

    /**
     * It buffers all the emissions it receives until an Observer subscribes
     * to it, and then it releases all the emissions to the Observer and clears
     * its cache.
     * Works with only one Observer and throws an error for any subsequent
     * one.
     */
    public final void unicastSubject() {
        // 1) for one Observer:\
        System.out.println("1)");
        Subject<String> subject = UnicastSubject.create();
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300 + " milliseconds")
                .subscribe(subject);
        sleep(2000);
        subject.subscribe(i -> System.out.println("Observer 1: " + i));
        sleep(2000);

        // 2) for many Observers: if you want to support more than one Observer and
//        let them receive the live emissions without received missed emissions, you can
//        call publish() to create single Observer proxy that multicast to more than one
//        Observer, as shown in the following code snippet:
        System.out.println("2)");
        Subject<String> subject2 = UnicastSubject.create();
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300 + " milliseconds")
                .subscribe(subject2);
        sleep(2000);

        Observable<String> multicast = subject2.publish().autoConnect();
        // bring the first Observer
        multicast.subscribe(i -> System.out.println("Observer 1: " + i));
        sleep(2000);
        // bring the second Observer
        multicast.subscribe(i -> System.out.println("Observer 2: " + i));
        sleep(1000);
    }

    /*
        private methods
     */
    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
