package riddles;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.junit.Test;

import static org.junit.Assert.*;

public class Riddle4Test {

    @Test
    public void solve() {
        var subject = PublishSubject.create();

        @NonNull TestObserver<Boolean> o = Riddle4.solve(subject)
                .test()
                .assertValuesOnly(false);

        subject.onNext(new Object());
        o.assertValuesOnly(false, true);

        subject.onNext(new Object());
        o.assertValuesOnly(false, true, false);

        subject.onNext(new Object());
        o.assertValuesOnly(false, true, false, true);

    }
}