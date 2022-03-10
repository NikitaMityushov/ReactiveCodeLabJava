package riddles;

import org.junit.Test;

import static org.junit.Assert.*;

public class Riddle1Test {

    @Test
    public void solve() {
        Riddle1.solve(5)
                .test()
                .assertResult(5);
    }
}