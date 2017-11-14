package comparison;

import db.Column;
import db.Type;
import db.comparison.GreaterOrEqualTo;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GreaterOrEqualToTest {
    @Test
    public void greaterOrEqualToColumns() {
        Column<Double> c1Double = new Column<>("c1", Type.FLOAT);
        Column<Integer> c2Int = new Column<>("c2", Type.INT);

        c1Double.add(1.0);
        c1Double.add(Type.NAN);

        c2Int.add(1);
        c2Int.add(44);

        GreaterOrEqualTo greaterOrEqualTo = new GreaterOrEqualTo();
        assertTrue(greaterOrEqualTo.compare(c1Double, c2Int, 0));
        assertTrue(greaterOrEqualTo.compare(c1Double, c2Int, 1));

        c1Double.add(12.0);
        c2Int.add(15);

        assertFalse(greaterOrEqualTo.compare(c1Double, c2Int, 2));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(3);
        c1Int.add(122);
        c1Int.add(15);

        assertTrue(greaterOrEqualTo.compare(c1Int, c2Int, 0));
        assertTrue(greaterOrEqualTo.compare(c1Int, c2Int, 1));
        assertTrue(greaterOrEqualTo.compare(c1Int, c2Int, 2));

        c1Int.add(40);
        c2Int.add(44);
        assertFalse(greaterOrEqualTo.compare(c1Int, c2Int, 3));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Zoo");
        c1String.add("Gal");

        c2String.add("Foo");
        c2String.add("Gal");

        assertTrue(greaterOrEqualTo.compare(c1String, c2String, 0));
        assertTrue(greaterOrEqualTo.compare(c1String, c2String, 1));

        c1String.add("As");
        c2String.add("Is");

        assertFalse(greaterOrEqualTo.compare(c1String, c2String, 2));
    }

    @Test
    public void greaterOrEqualTo() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(12.0);
        colDouble.add(121.0);

        GreaterOrEqualTo greaterOrEqualTo = new GreaterOrEqualTo();
        assertTrue(greaterOrEqualTo.compare(colDouble, litDouble, 0));
        assertTrue(greaterOrEqualTo.compare(colDouble, litInt, 1));

        colDouble.add(5.0);

        assertFalse(greaterOrEqualTo.compare(colDouble, litDouble, 2));
        assertFalse(greaterOrEqualTo.compare(colDouble, litInt, 2));

        litDouble = "100";
        litInt = "100";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(142);
        colInt.add(Type.NAN);
        colInt.add(100);

        assertTrue(greaterOrEqualTo.compare(colInt, litDouble, 0));
        assertTrue(greaterOrEqualTo.compare(colInt, litInt, 1));
        assertTrue(greaterOrEqualTo.compare(colInt, litDouble, 2));

        colInt.add(Type.NOVALUE);

        assertFalse(greaterOrEqualTo.compare(colInt, litDouble, 3));
        assertFalse(greaterOrEqualTo.compare(colInt, litInt, 3));


        String litStr = "Apple";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Apple");
        colStr.add("More");
        colStr.add("Door");

        assertTrue(greaterOrEqualTo.compare(colStr, litStr, 0));
        assertTrue(greaterOrEqualTo.compare(colStr, litStr, 1));
        assertTrue(greaterOrEqualTo.compare(colStr, litStr, 2));

        colStr.add("Ad");

        assertFalse(greaterOrEqualTo.compare(colStr, litStr, 3));
    }
}
