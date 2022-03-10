package riddles;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import org.junit.Test;

public class Riddle5Test {

    @Test
    public void solve() {
        var first = BehaviorSubject.createDefault(0);
        var second = BehaviorSubject.createDefault(0);

        @NonNull TestObserver<Integer> o = Riddle5.solve(first, second)
                .test()
                .assertValuesOnly(0);

        first.onNext(5);
        o.assertValuesOnly(0, 5);

        second.onNext(6);
        o.assertValuesOnly(0, 5, 11);

        first.onNext(-6);
        o.assertValuesOnly(0, 5, 11, 0);
    }
}