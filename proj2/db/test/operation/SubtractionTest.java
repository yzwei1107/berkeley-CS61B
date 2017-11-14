package operation;

import db.*;

import db.operation.Subtraction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SubtractionTest {
    @Test
    public void subtractColumns() {
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

        Subtraction subtractor = new Subtraction();
        Column<Double> c3Double = subtractor.operation("c3", c1Double, c2Double);
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

        Subtraction subtractorInt = new Subtraction();
        Column<Integer> c3Integer = subtractorInt.operation("c3", c1Integer, c2Integer);
        assertEquals(expectedInteger.toString(), c3Integer.toString());
    }

    @Test
    public void subtractLiteral() {
        Subtraction subtraction = new Subtraction();

        String litDouble = "1.0";
        String litInt = "1";

        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(12.0);
        colDouble.add(Type.NOVALUE);
        colDouble.add(Type.NAN);

        Column<Double> expectedDouble = new Column<>("result", Type.FLOAT);
        expectedDouble.add(11.0);
        expectedDouble.add(-1.0);
        expectedDouble.add(Type.NAN);

        Column<Double> resultDouble = subtraction.operation("result", colDouble, litDouble);

        assertEquals(expectedDouble.toString(), resultDouble.toString());


        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(12);
        colInt.add(Type.NOVALUE);
        colInt.add(Type.NAN);

        Column<Integer> expectedInt = new Column<>("result", Type.INT);
        expectedInt.add(11);
        expectedInt.add(-1);
        expectedInt.add(Type.NAN);

        Column<Integer> resultInt = subtraction.operation("result", colInt, litInt);

        assertEquals(expectedInt.toString(), resultInt.toString());
    }
}
