package operation;

import db.*;

import db.operation.Summation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SummationTest {
    @Test
    public void sumColumns() {
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

        Summation adder = new Summation();
        Column<Double> c3Double = adder.operation("c3", c1Double, c2Double);
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

        Summation adderInt = new Summation();
        Column<Integer> c3Integer = adderInt.operation("c3", c1Integer, c2Integer);
        assertEquals(expectedInteger.toString(), c3Integer.toString());


        Column<String> c1String = new Column<>("c1", Type.STRING);
        c1String.add("Foo");
        c1String.add(Type.NOVALUE);
        c1String.add("Offset");
        c1String.add(Type.NOVALUE);

        Column<String> c2String = new Column<>("c2", Type.STRING);
        c2String.add("Bar");
        c2String.add(Type.NOVALUE);
        c2String.add(Type.NOVALUE);
        c2String.add("Takeoff");
        c2String.add("Quavo");
        c2String.add(Type.NOVALUE);

        Column<String> expectedString = new Column<>("c3", Type.STRING);
        expectedString.add("FooBar");
        expectedString.add(Type.NOVALUE);
        expectedString.add("Offset");
        expectedString.add("Takeoff");
        expectedString.add("Quavo");
        expectedString.add(Type.NOVALUE);

        Summation concatenator = new Summation();
        Column<String> c3String = concatenator.operation("c3", c1String, c2String);
        assertEquals(expectedString.toString(), c3String.toString());
    }

    @Test
    public void sumLiteral() {
        String litDouble = "4.0";
        String litInt = "4";
        Column<Double> colDouble = new Column<>("c1", Type.FLOAT);
        colDouble.add(3.0);
        colDouble.add(Type.NAN);
        colDouble.add(Type.NOVALUE);
        colDouble.add(4.0);
        colDouble.add(Type.NOVALUE);
        colDouble.add(5.0);
        colDouble.add(Type.NOVALUE);

        Column<Double> expectedDouble =  new Column<>("result", Type.FLOAT);
        expectedDouble.add(7.0);
        expectedDouble.add(Type.NAN);
        expectedDouble.add(4.0);
        expectedDouble.add(8.0);
        expectedDouble.add(4.0);
        expectedDouble.add(9.0);
        expectedDouble.add(4.0);

        Summation summation = new Summation();
        Column<Double> resultDouble = summation.operation("result", colDouble, litDouble);

        assertEquals(expectedDouble.toString(), resultDouble.toString());
        resultDouble = summation.operation("result", colDouble, litInt);
        assertEquals(expectedDouble.toString(), resultDouble.toString());

        Column<Integer> colInt = new Column<>("c1", Type.INT);
        colInt.add(3);
        colInt.add(Type.NAN);
        colInt.add(Type.NOVALUE);
        colInt.add(4);
        colInt.add(Type.NOVALUE);
        colInt.add(5);
        colInt.add(Type.NOVALUE);

        Column<Integer> expectedInt =  new Column<>("result", Type.INT);
        expectedInt.add(7);
        expectedInt.add(Type.NAN);
        expectedInt.add(4);
        expectedInt.add(8);
        expectedInt.add(4);
        expectedInt.add(9);
        expectedInt.add(4);

        Column<Integer> resultInt = summation.operation("result", colInt, litInt);
        assertEquals(expectedInt.toString(), resultInt.toString());

        Column<String> colString = new Column<>("col", Type.STRING);
        colString.add("Hello");
        colString.add(Type.NOVALUE);

        Column<String> expectedString = new Column<>("result", Type.STRING);
        expectedString.add("HelloWorld");
        expectedString.add("World");

        Column<String> resultString = summation.operation("result", colString, "World");
        assertEquals(expectedString.toString(), resultString.toString());
    }
}
