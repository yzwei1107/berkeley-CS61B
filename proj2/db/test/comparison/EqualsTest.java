package comparison;

import db.Column;
import db.Type;
import db.comparison.Equals;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsTest {
    @Test
    public void equalColumns() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        Column<Integer> c2Int = new Column<>("c2", Type.INT);

        c1Double.add(1.0);
        c1Double.add(Type.NAN);

        c2Int.add(1);
        c2Int.add(Type.NAN);

        Equals equalCheck = new Equals();
        assertTrue(equalCheck.compare(c1Double, c2Int, 0));
        assertTrue(equalCheck.compare(c1Double, c2Int, 1));

        c1Double.add(Type.NOVALUE);
        c1Double.add(Type.NAN);
        c1Double.add(Type.NOVALUE);
        c2Int.add(12);

        assertFalse(equalCheck.compare(c1Double, c2Int, 2));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(1);
        c1Int.add(Type.NAN);
        c1Int.add(12);

        assertTrue(equalCheck.compare(c1Int, c2Int, 0));
        assertTrue(equalCheck.compare(c1Int, c2Int, 1));
        assertTrue(equalCheck.compare(c1Int, c2Int, 2));

        c1Int.add(15);
        c2Int.add(52);
        assertFalse(equalCheck.compare(c1Int, c2Int, 3));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Foo");

        c2String.add("Foo");

        assertTrue(equalCheck.compare(c1String, c2String, 0));

        c1String.add("Is");
        c2String.add("As");

        assertFalse(equalCheck.compare(c1String, c2String, 1));
    }

    @Test
    public void equalsLiteral() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(12.0);
        colDouble.add(12.0);
        colDouble.add(12.0);

        Equals equals = new Equals();
        assertTrue(equals.compare(colDouble, litDouble, 0));
        assertTrue(equals.compare(colDouble, litInt, 1));

        colDouble.add(Type.NAN);

        assertFalse(equals.compare(colDouble, litDouble, 3));
        assertFalse(equals.compare(colDouble, litInt, 3));


        litDouble = "142.0";
        litInt = "142";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(142);
        colInt.add(142);
        colInt.add(142);

        assertTrue(equals.compare(colInt, litDouble, 0));
        assertTrue(equals.compare(colInt, litInt, 1));

        colInt.add(Type.NOVALUE);

        assertFalse(equals.compare(colInt, litDouble, 3));
        assertFalse(equals.compare(colInt, litInt, 3));


        String litStr = "Morse";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Morse");

        assertTrue(equals.compare(colStr, litStr, 0));

        colStr.add("Foo");

        assertFalse(equals.compare(colStr, litStr, 1));
    }

}
