package comparison;

import db.Column;
import db.Type;
import db.comparison.LessThan;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LessThanTest {
    @Test
    public void lessThanColumns() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        Column<Integer> c2Int = new Column<>("c2", Type.INT);

        c1Double.add(1.3);
        c1Double.add(44.11);

        c2Int.add(2);
        c2Int.add(Type.NAN);

        LessThan lessThan = new LessThan();
        assertTrue(lessThan.compare(c1Double, c2Int, 0));
        assertTrue(lessThan.compare(c1Double, c2Int, 1));

        c1Double.add(15.0);
        c2Int.add(12);

        assertFalse(lessThan.compare(c1Double, c2Int, 2));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(1);
        c1Int.add(44);
        c1Int.add(10);

        assertTrue(lessThan.compare(c1Int, c2Int, 0));
        assertTrue(lessThan.compare(c1Int, c2Int, 1));
        assertTrue(lessThan.compare(c1Int, c2Int, 2));

        c1Int.add(Type.NAN);
        c2Int.add(Type.NOVALUE);
        assertFalse(lessThan.compare(c1Int, c2Int, 3));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Foo");
        c1String.add("Gal");

        c2String.add("Zoo");
        c2String.add("Gao");

        assertTrue(lessThan.compare(c1String, c2String, 0));
        assertTrue(lessThan.compare(c1String, c2String, 1));

        c1String.add("Is");
        c2String.add("As");

        assertFalse(lessThan.compare(c1String, c2String, 2));
    }

    @Test
    public void lessThanLiteral() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(2.0);
        colDouble.add(1.0);

        LessThan lessThan = new LessThan();
        assertTrue(lessThan.compare(colDouble, litDouble, 0));
        assertTrue(lessThan.compare(colDouble, litInt, 1));

        colDouble.add(15.0);

        assertFalse(lessThan.compare(colDouble, litDouble, 2));
        assertFalse(lessThan.compare(colDouble, litInt, 2));

        litDouble = "153.3";
        litInt = "153";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(142);

        assertTrue(lessThan.compare(colInt, litDouble, 0));
        assertTrue(lessThan.compare(colInt, litInt, 0));

        colInt.add(Type.NOVALUE);

        assertFalse(lessThan.compare(colInt, litDouble, 1));
        assertFalse(lessThan.compare(colInt, litInt, 1));


        String litStr = "Morse";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Coarse");
        colStr.add("More");
        colStr.add("Door");

        assertTrue(lessThan.compare(colStr, litStr, 0));
        assertTrue(lessThan.compare(colStr, litStr, 1));
        assertTrue(lessThan.compare(colStr, litStr, 2));

        colStr.add("Morse");

        assertFalse(lessThan.compare(colStr, litStr, 3));
    }
}
