import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalculatorTest {

    private static Calculator calculator;

    @BeforeClass
    public static void createNewCalculator(){
        calculator = new Calculator();
    }
    @Test
    public void functionShouldCalculateAOperationFromString(){
        Assert.assertEquals("0.0", calculator.calculate("1+(2-6)/4"));
    }
    @Test
    public void functionShouldCalculateAOperationWithNegativeNumberInParentheses(){
        Assert.assertEquals("-1.0", calculator.calculate("1+(-2-6)/4"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void otherSymbolsShouldNotBeReceived(){
        calculator.calculate("1+(2$-6@)/4");
    }
    @Test(expected = IllegalArgumentException.class)
    public void incorrectStructureOfOperationShouldNotBeAccepted(){
        calculator.calculate("+324)");
    }
    @Test
    public void emptyParameterContentShouldReturnAnEmptyString(){
        Assert.assertEquals("", calculator.calculate(""));
    }
    @Test
    public void functionShouldReturnAEnteredString(){
        Assert.assertEquals("324", calculator.calculate("324"));
    }
    @Test
    public void functionShouldToCalculateTheContentOfParenthesesWithPositiveResult(){
        Assert.assertEquals("9.0", calculator.resolveParentheses("(3+6)"));
    }
    @Test
    public void functionShouldToCalculateTheContentOfParenthesesWithNegativeResult(){
        Assert.assertEquals("(-4.0)", calculator.resolveParentheses("(2-6)"));
    }
    @Test
    public void functionShouldToCalculateTheContentOfParameters(){
        Assert.assertEquals("-4.0", calculator.evaluate("2", "-", "6"));
    }
}
