package riddles;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static org.junit.Assert.*;

public class Riddle3Test {

    @Test
    public void solve() {
        Riddle3.solve(Observable.range(0, 10))
                .test()
                .assertResult(0, 2, 4, 6, 8);
    }
}