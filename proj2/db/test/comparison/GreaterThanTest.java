package comparison;

import db.Column;
import db.Type;
import db.comparison.GreaterThan;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GreaterThanTest {
    @Test
    public void greaterThanColumns() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        Column<Integer> c2Int = new Column<>("c2", Type.INT);

        c1Double.add(2.3);
        c1Double.add(Type.NAN);

        c2Int.add(1);
        c2Int.add(44);

        GreaterThan greaterThan = new GreaterThan();
        assertTrue(greaterThan.compare(c1Double, c2Int, 0));
        assertTrue(greaterThan.compare(c1Double, c2Int, 1));

        c1Double.add(12.0);
        c2Int.add(15);

        assertFalse(greaterThan.compare(c1Double, c2Int, 2));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(3);
        c1Int.add(122);
        c1Int.add(30);

        assertTrue(greaterThan.compare(c1Int, c2Int, 0));
        assertTrue(greaterThan.compare(c1Int, c2Int, 1));
        assertTrue(greaterThan.compare(c1Int, c2Int, 2));

        c1Int.add(40);
        c2Int.add(40);
        assertFalse(greaterThan.compare(c1Int, c2Int, 3));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Zoo");
        c1String.add("Gao");

        c2String.add("Foo");
        c2String.add("Gal");

        assertTrue(greaterThan.compare(c1String, c2String, 0));
        assertTrue(greaterThan.compare(c1String, c2String, 1));

        c1String.add("As");
        c2String.add("Is");

        assertFalse(greaterThan.compare(c1String, c2String, 2));
    }

    @Test
    public void greaterThanLiteral() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(22.0);
        colDouble.add(121.0);

        GreaterThan greaterThan = new GreaterThan();
        assertTrue(greaterThan.compare(colDouble, litDouble, 0));
        assertTrue(greaterThan.compare(colDouble, litInt, 1));

        colDouble.add(5.0);

        assertFalse(greaterThan.compare(colDouble, litDouble, 2));
        assertFalse(greaterThan.compare(colDouble, litInt, 2));

        litDouble = "100";
        litInt = "100";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(142);
        colInt.add(Type.NAN);

        assertTrue(greaterThan.compare(colInt, litDouble, 0));
        assertTrue(greaterThan.compare(colInt, litInt, 1));

        colInt.add(Type.NOVALUE);

        assertFalse(greaterThan.compare(colInt, litDouble, 2));
        assertFalse(greaterThan.compare(colInt, litInt, 2));


        String litStr = "Apple";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Coarse");
        colStr.add("More");
        colStr.add("Door");

        assertTrue(greaterThan.compare(colStr, litStr, 0));
        assertTrue(greaterThan.compare(colStr, litStr, 1));
        assertTrue(greaterThan.compare(colStr, litStr, 2));

        colStr.add("Ad");

        assertFalse(greaterThan.compare(colStr, litStr, 3));
    }
}
