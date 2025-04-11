import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class MaxCalculatorTest {

    MaxCalculator calculator;

    @Before
    public void setup() {
        calculator = new MaxCalculator();
    }

    @Test
    public void testMaxWithPositiveNumbers() {
        assertEquals(10, calculator.max(10, 5));
    }

    @Test
    public void testMaxWithNegativeNumbers() {
        assertEquals(-1, calculator.max(-1, -5));
    }

    @Test
    public void testMaxWithEqualNumbers() {
        assertEquals(7, calculator.max(7, 7));
    }
}
