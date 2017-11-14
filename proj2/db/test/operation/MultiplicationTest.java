package operation;

import db.*;

import db.operation.Multiplication;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultiplicationTest {

    @Test
    public void multiplyColumns() {
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

        Multiplication multiplier = new Multiplication();
        Column<Double> c3Double = multiplier.operation("c3", c1Double, c2Double);
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

        Multiplication multiplierInt = new Multiplication();
        Column<Integer> c3Integer = multiplierInt.operation("c3", c1Integer, c2Integer);
        assertEquals(expectedInteger.toString(), c3Integer.toString());
    }

    @Test
    public void multiplyLiteral() {
        Multiplication multiplication = new Multiplication();

        String litDouble = "2.5";
        String litInt = "2";

        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(12.0);
        colDouble.add(Type.NOVALUE);
        colDouble.add(Type.NAN);

        Column<Double> expectedDouble = new Column<>("result", Type.FLOAT);
        expectedDouble.add(30.0);
        expectedDouble.add(0.0);
        expectedDouble.add(Type.NAN);

        Column<Double> resultDouble = multiplication.operation("result", colDouble, litDouble);

        assertEquals(expectedDouble.toString(), resultDouble.toString());


        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(12);
        colInt.add(Type.NOVALUE);
        colInt.add(Type.NAN);

        Column<Integer> expectedInt = new Column<>("result", Type.INT);
        expectedInt.add(24);
        expectedInt.add(0);
        expectedInt.add(Type.NAN);

        Column<Integer> resultInt = multiplication.operation("result", colInt, litInt);

        assertEquals(expectedInt.toString(), resultInt.toString());
    }
}
