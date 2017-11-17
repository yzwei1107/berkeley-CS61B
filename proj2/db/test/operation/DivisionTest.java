package operation;

import db.*;

import db.operation.Division;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DivisionTest {

    @Test
    public void divideColumns() {
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


        Column<Double> expectedDouble = new Column<>("c3", Type.FLOAT);
        expectedDouble.add(2.0 / 3.0);
        expectedDouble.add(Type.NAN);
        expectedDouble.add(Type.NOVALUE);
        expectedDouble.add(0.0);
        expectedDouble.add(Type.NAN);

        Division divider = new Division();
        Column<Double> c3Double = divider.operation("c3", c1Double, c2Double);
        assertEquals(expectedDouble.toString(), c3Double.toString());


        Column<Integer> c1Integer = new Column<>("c1", Type.INT);
        c1Integer.add(2);
        c1Integer.add(3);
        c1Integer.add(2);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(Type.NOVALUE);
        c1Integer.add(7);

        Column<Integer> c2Integer = new Column<>("c2", Type.INT);
        c2Integer.add(3);
        c2Integer.add(0);
        c2Integer.add(Type.NAN);
        c2Integer.add(Type.NOVALUE);
        c2Integer.add(4);
        c2Integer.add(Type.NOVALUE);

        Column<Integer> expectedInteger = new Column<>("c3", Type.INT);
        expectedInteger.add(2 / 3);
        expectedInteger.add(Type.NAN);
        expectedInteger.add(Type.NAN);
        expectedInteger.add(Type.NOVALUE);
        expectedInteger.add(0);
        expectedInteger.add(Type.NAN);

        Division dividerInt = new Division();
        Column<Integer> c3Integer = dividerInt.operation("c3", c1Integer, c2Integer);
        assertEquals(expectedInteger.toString(), c3Integer.toString());
    }

    @Test
    public void divideLiteral() {
        Division division = new Division();

        String litDouble = "2.0";
        String litInt = "0";

        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(12.0);
        colDouble.add(Type.NOVALUE);
        colDouble.add(Type.NAN);

        Column<Double> expectedDouble = new Column<>("result", Type.FLOAT);
        expectedDouble.add(6.0);
        expectedDouble.add(0.0);
        expectedDouble.add(Type.NAN);

        Column<Double> resultDouble = division.operation("result", colDouble, litDouble);

        assertEquals(expectedDouble.toString(), resultDouble.toString());


        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(12);
        colInt.add(Type.NOVALUE);
        colInt.add(Type.NAN);

        Column<Integer> expectedInt = new Column<>("result", Type.INT);
        expectedInt.add(Type.NAN);
        expectedInt.add(Type.NAN);
        expectedInt.add(Type.NAN);

        Column<Integer> resultInt = division.operation("result", colInt, litInt);

        assertEquals(expectedInt.toString(), resultInt.toString());
    }
}
