package comparison;

import db.Column;
import db.Type;
import db.comparison.LessOrEqualTo;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LessOrEqualToTest {
    @Test
    public void lessThanColumns() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        Column<Integer> c2Int = new Column<>("c2", Type.INT);

        c1Double.add(1.3);
        c1Double.add(Type.NAN);

        c2Int.add(2);
        c2Int.add(Type.NAN);

        LessOrEqualTo lessOrEqualTo = new LessOrEqualTo();
        assertTrue(lessOrEqualTo.compare(c1Double, c2Int, 0));
        assertTrue(lessOrEqualTo.compare(c1Double, c2Int, 1));

        c1Double.add(15.0);
        c2Int.add(12);

        assertFalse(lessOrEqualTo.compare(c1Double, c2Int, 2));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(2);
        c1Int.add(44);
        c1Int.add(10);

        assertTrue(lessOrEqualTo.compare(c1Int, c2Int, 0));
        assertTrue(lessOrEqualTo.compare(c1Int, c2Int, 1));
        assertTrue(lessOrEqualTo.compare(c1Int, c2Int, 2));

        c1Int.add(Type.NAN);
        c2Int.add(Type.NOVALUE);
        assertFalse(lessOrEqualTo.compare(c1Int, c2Int, 3));
        assertFalse(lessOrEqualTo.compare(c1Int, c2Int, 3));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Foo");
        c1String.add("Gal");

        c2String.add("Foo");
        c2String.add("Gao");

        assertTrue(lessOrEqualTo.compare(c1String, c2String, 0));
        assertTrue(lessOrEqualTo.compare(c1String, c2String, 1));

        c1String.add("Is");
        c2String.add("As");

        assertFalse(lessOrEqualTo.compare(c1String, c2String, 2));
    }

    @Test
    public void LessOrEqualTo() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(12.0);
        colDouble.add(1.0);

        LessOrEqualTo lessOrEqualTo = new LessOrEqualTo();
        assertTrue(lessOrEqualTo.compare(colDouble, litDouble, 0));
        assertTrue(lessOrEqualTo.compare(colDouble, litInt, 1));

        colDouble.add(15.0);

        assertFalse(lessOrEqualTo.compare(colDouble, litDouble, 2));
        assertFalse(lessOrEqualTo.compare(colDouble, litInt, 2));

        litDouble = "153.3";
        litInt = "153";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(153);
        colInt.add(142);

        assertTrue(lessOrEqualTo.compare(colInt, litDouble, 0));
        assertTrue(lessOrEqualTo.compare(colInt, litInt, 1));

        colInt.add(Type.NOVALUE);

        assertFalse(lessOrEqualTo.compare(colInt, litDouble, 2));
        assertFalse(lessOrEqualTo.compare(colInt, litInt, 2));


        String litStr = "Morse";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Coarse");
        colStr.add("Morse");
        colStr.add("Door");

        assertTrue(lessOrEqualTo.compare(colStr, litStr, 0));
        assertTrue(lessOrEqualTo.compare(colStr, litStr, 1));
        assertTrue(lessOrEqualTo.compare(colStr, litStr, 2));

        colStr.add("Poor");

        assertFalse(lessOrEqualTo.compare(colStr, litStr, 3));
    }
}
