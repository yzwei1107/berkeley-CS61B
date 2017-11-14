package comparison;

import db.Column;
import db.Type;
import db.comparison.NotEquals;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotEqualsTest {
    @Test
    public void notEqualsColumns() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        Column<Integer> c2Int = new Column<>("c2", Type.INT);

        c1Double.add(1.0);
        c1Double.add(Type.NAN);

        c2Int.add(1);
        c2Int.add(Type.NAN);

        NotEquals notEquals = new NotEquals();
        assertFalse(notEquals.compare(c1Double, c2Int,0));
        assertFalse(notEquals.compare(c1Double, c2Int,1));

        c1Double.add(12.0);
        c1Double.add(Type.NAN);
        c2Int.add(10);
        c2Int.add(12);

        assertTrue(notEquals.compare(c1Double, c2Int, 2));
        assertTrue(notEquals.compare(c1Double, c2Int, 3));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(1);
        c1Int.add(Type.NAN);
        c1Int.add(10);
        c1Int.add(12);

        assertFalse(notEquals.compare(c1Int, c2Int, 0));
        assertFalse(notEquals.compare(c1Int, c2Int, 1));
        assertFalse(notEquals.compare(c1Int, c2Int, 2));
        assertFalse(notEquals.compare(c1Int, c2Int, 3));

        c1Int.add(15);
        c2Int.add(52);
        assertTrue(notEquals.compare(c1Int, c2Int, 4));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Foo");
        c1String.add(Type.NOVALUE);

        c2String.add("Foo");
        c2String.add(Type.NOVALUE);

        assertFalse(notEquals.compare(c1String, c2String, 0));
        assertFalse(notEquals.compare(c1String, c2String, 1));

        c1String.add("Is");
        c2String.add("As");

        assertTrue(notEquals.compare(c1String, c2String, 2));
    }

    @Test
    public void notEqualsLiteral() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(12.0);

        NotEquals notEquals = new NotEquals();
        assertFalse(notEquals.compare(colDouble, litDouble, 0));
        assertFalse(notEquals.compare(colDouble, litInt, 0));

        colDouble.add(Type.NAN);

        assertTrue(notEquals.compare(colDouble, litDouble,1));
        assertTrue(notEquals.compare(colDouble, litInt, 1));


        litDouble = "142.0";
        litInt = "142";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(142);

        assertFalse(notEquals.compare(colInt, litDouble, 0));
        assertFalse(notEquals.compare(colInt, litInt, 0));

        colInt.add(Type.NAN);

        assertTrue(notEquals.compare(colInt, litDouble, 1));
        assertTrue(notEquals.compare(colInt, litInt, 1));


        String litStr = "Morse";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Morse");

        assertFalse(notEquals.compare(colStr, litStr, 0));

        colStr.add("Foo");

        assertTrue(notEquals.compare(colStr, litStr, 1));
    }
}
