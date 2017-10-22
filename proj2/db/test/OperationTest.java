import db.*;

import db.operation.Division;
import db.operation.Multiplication;
import db.operation.Subtraction;
import db.operation.Summation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OperationTest {
    @Test
    public void sum() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        c1Double.add(3.0);
        c1Double.add(Type.NAN);
        c1Double.add(Type.NOVALUE);
        c1Double.add(4.0);
        c1Double.add(Type.NOVALUE);
        c1Double.add(5.0);
        c1Double.add(Type.NOVALUE);

        Column<Double> c2Double = new Column<>("c2", Type.FLOAT);
        c2Double.add(2.0);
        c2Double.add(2.0);
        c2Double.add(Type.NOVALUE);
        c2Double.add(Type.NOVALUE);
        c2Double.add(7.0);


        Column<Double> expectedDouble = new Column<>("c3", Type.FLOAT);
        expectedDouble.add(5.0);
        expectedDouble.add(Type.NAN);
        expectedDouble.add(Type.NOVALUE);
        expectedDouble.add(4.0);
        expectedDouble.add(7.0);
        expectedDouble.add(5.0);
        expectedDouble.add(Type.NOVALUE);

        Summation adder = new Summation(c1Double, c2Double);
        Column<Double> c3Double = adder.operate("c3");
        assertEquals(expectedDouble.toString(), c3Double.toString());


        Column<Integer> c1Integer = new Column<>("c1", Type.INT);
        c1Integer.add(3);
        c1Integer.add(Type.NAN);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(4);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(5);
        c1Integer.add(Type.NOVALUE);

        Column<Integer> c2Integer = new Column<>("c2", Type.INT);
        c2Integer.add(2);
        c2Integer.add(2);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(7);

        Column<Integer> expectedInteger = new Column<>("c3", Type.INT);
        expectedInteger.add(5);
        expectedInteger.add(Type.NAN);
        expectedInteger.add(Type.NOVALUE);
        expectedInteger.add(4);
        expectedInteger.add(7);
        expectedInteger.add(5);
        expectedInteger.add(Type.NOVALUE);

        Summation adderInt = new Summation(c1Integer, c2Integer);
        Column<Integer> c3Integer = adderInt.operate("c3");
        assertEquals(expectedInteger.toString(), c3Integer.toString());


        Column<String> c1String = new Column<>("c1", Type.STRING);
        c1String.add("Foo");
        c1String.add(Type.NAN);
        c1String.add(Type.NOVALUE);
        c1String.add("Offset");
        c1String.add(Type.NOVALUE);

        Column<String> c2String = new Column<>("c2", Type.STRING);
        c2String.add("Bar");
        c2String.add("a");
        c2String.add(Type.NOVALUE);
        c2String.add(Type.NOVALUE);
        c2String.add("Takeoff");
        c2String.add("Quavo");
        c2String.add(Type.NOVALUE);

        Column<String> expectedString = new Column<>("c3", Type.STRING);
        expectedString.add("FooBar");
        expectedString.add(Type.NAN);
        expectedString.add(Type.NOVALUE);
        expectedString.add("Offset");
        expectedString.add("Takeoff");
        expectedString.add("Quavo");
        expectedString.add(Type.NOVALUE);

        Summation concatenator = new Summation(c1String, c2String);
        Column<String> c3String = concatenator.operate("c3");
        assertEquals(expectedString.toString(), c3String.toString());
    }

    @Test
    public void subtract() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        c1Double.add(3.0);
        c1Double.add(Type.NAN);
        c1Double.add(Type.NOVALUE);
        c1Double.add(4.0);
        c1Double.add(Type.NOVALUE);
        c1Double.add(5.0);
        c1Double.add(Type.NOVALUE);

        Column<Double> c2Double = new Column<>("c2", Type.FLOAT);
        c2Double.add(2.0);
        c2Double.add(2.0);
        c2Double.add(Type.NOVALUE);
        c2Double.add(Type.NOVALUE);
        c2Double.add(7.0);

        Column<Double> expectedDouble = new Column<>("c3", Type.FLOAT);
        expectedDouble.add(1.0);
        expectedDouble.add(Type.NAN);
        expectedDouble.add(Type.NOVALUE);
        expectedDouble.add(4.0);
        expectedDouble.add(-7.0);
        expectedDouble.add(5.0);
        expectedDouble.add(Type.NOVALUE);

        Subtraction subtractor = new Subtraction(c1Double, c2Double);
        Column<Double> c3Double = subtractor.operate("c3");
        assertEquals(expectedDouble.toString(), c3Double.toString());


        Column<Integer> c1Integer = new Column<>("c1", Type.INT);
        c1Integer.add(3);
        c1Integer.add(Type.NAN);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(4);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(5);
        c1Integer.add(Type.NOVALUE);

        Column<Integer> c2Integer = new Column<>("c2", Type.INT);
        c2Integer.add(2);
        c2Integer.add(2);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(7);

        Column<Integer> expectedInteger = new Column<>("c3", Type.INT);
        expectedInteger.add(1);
        expectedInteger.add(Type.NAN);
        expectedInteger.add(Type.NOVALUE);
        expectedInteger.add(4);
        expectedInteger.add(-7);
        expectedInteger.add(5);
        expectedInteger.add(Type.NOVALUE);

        Subtraction subtractorInt = new Subtraction(c1Integer, c2Integer);
        Column<Integer> c3Integer = subtractorInt.operate("c3");
        assertEquals(expectedInteger.toString(), c3Integer.toString());
    }

    @Test
    public void multiply() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        c1Double.add(2.0);
        c1Double.add(2.0);
        c1Double.add(Type.NOVALUE);
        c1Double.add(Type.NOVALUE);
        c1Double.add(7.0);

        Column<Double> c2Double = new Column<>("c2", Type.FLOAT);
        c2Double.add(3.0);
        c2Double.add(Type.NAN);
        c2Double.add(Type.NOVALUE);
        c2Double.add(4.0);
        c2Double.add(Type.NOVALUE);
        c2Double.add(5.0);
        c2Double.add(Type.NOVALUE);


        Column<Double> expectedDouble = new Column<>("c3", Type.FLOAT);
        expectedDouble.add(6.0);
        expectedDouble.add(Type.NAN);
        expectedDouble.add(Type.NOVALUE);
        expectedDouble.add(0.0);
        expectedDouble.add(0.0);
        expectedDouble.add(5.0);
        expectedDouble.add(Type.NOVALUE);

        Multiplication multiplier = new Multiplication(c1Double, c2Double);
        Column<Double> c3Double = multiplier.operate("c3");
        assertEquals(expectedDouble.toString(), c3Double.toString());


        Column<Integer> c1Integer = new Column<>("c1", Type.INT);
        c1Integer.add(2);
        c1Integer.add(2);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(7);

        Column<Integer> c2Integer = new Column<>("c2", Type.INT);
        c2Integer.add(3);
        c2Integer.add(Type.NAN);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(4);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(5);
        c2Integer.add(Type.NOVALUE);


        Column<Integer> expectedInteger = new Column<>("c3", Type.INT);
        expectedInteger.add(6);
        expectedInteger.add(Type.NAN);
        expectedInteger.add(Type.NOVALUE);
        expectedInteger.add(0);
        expectedInteger.add(0);
        expectedInteger.add(5);
        expectedInteger.add(Type.NOVALUE);

        Multiplication multiplierInt = new Multiplication(c1Integer, c2Integer);
        Column<Integer> c3Integer = multiplierInt.operate("c3");
        assertEquals(expectedInteger.toString(), c3Integer.toString());
    }

    @Test
    public void divide() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        c1Double.add(2.0);
        c1Double.add(2.0);
        c1Double.add(Type.NOVALUE);
        c1Double.add(Type.NOVALUE);
        c1Double.add(7.0);

        Column<Double> c2Double = new Column<>("c2", Type.FLOAT);
        c2Double.add(3.0);
        c2Double.add(Type.NAN);
        c2Double.add(Type.NOVALUE);
        c2Double.add(4.0);
        c2Double.add(Type.NOVALUE);
        c2Double.add(5.0);
        c2Double.add(Type.NOVALUE);


        Column<Double> expectedDouble = new Column<>("c3", Type.FLOAT);
        expectedDouble.add(2.0 / 3.0);
        expectedDouble.add(Type.NAN);
        expectedDouble.add(Type.NOVALUE);
        expectedDouble.add(0.0);
        expectedDouble.add(Type.NAN);
        expectedDouble.add(5.0);
        expectedDouble.add(Type.NOVALUE);

        Division divider = new Division(c1Double, c2Double);
        Column<Double> c3Double = divider.operate("c3");
        assertEquals(expectedDouble.toString(), c3Double.toString());


        Column<Integer> c1Integer = new Column<>("c1", Type.INT);
        c1Integer.add(2);
        c1Integer.add(2);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(7);

        Column<Integer> c2Integer = new Column<>("c2", Type.INT);
        c2Integer.add(3);
        c2Integer.add(Type.NAN);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(4);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(5);
        c2Integer.add(Type.NOVALUE);


        Column<Integer> expectedInteger = new Column<>("c3", Type.INT);
        expectedInteger.add(2 / 3);
        expectedInteger.add(Type.NAN);
        expectedInteger.add(Type.NOVALUE);
        expectedInteger.add(0);
        expectedInteger.add(Type.NAN);
        expectedInteger.add(5);
        expectedInteger.add(Type.NOVALUE);

        Division dividerInt = new Division(c1Integer, c2Integer);
        Column<Integer> c3Integer = dividerInt.operate("c3");
        assertEquals(expectedInteger.toString(), c3Integer.toString());
    }
}
