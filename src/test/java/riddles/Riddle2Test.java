package riddles;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static org.junit.Assert.*;

public class Riddle2Test {

    @Test
    public void solve() {
        Riddle2.solve(Observable.just(-1, 0, 5))
                .test()
                .assertResult(0, 1, 6);
    }
}