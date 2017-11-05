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
        assertTrue(greaterThan.compare(c1Double, c2Int));

        c1Double.add(12.0);
        c2Int.add(15);

        assertFalse(greaterThan.compare(c1Double, c2Int));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(3);
        c1Int.add(122);
        c1Int.add(30);

        assertTrue(greaterThan.compare(c1Int, c2Int));

        c1Int.add(40);
        c2Int.add(40);
        assertFalse(greaterThan.compare(c1Int, c2Int));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Zoo");
        c1String.add("Gao");

        c2String.add("Foo");
        c2String.add("Gal");

        assertTrue(greaterThan.compare(c1String, c2String));

        c1String.add("As");
        c2String.add("Is");

        assertFalse(greaterThan.compare(c1String, c2String));
    }

    @Test
    public void greaterThanLiteral() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(22.0);
        colDouble.add(121.0);
        colDouble.add(512.0);

        GreaterThan greaterThan = new GreaterThan();
        assertTrue(greaterThan.compare(colDouble, litDouble));
        assertTrue(greaterThan.compare(colDouble, litInt));

        colDouble.add(5.0);

        assertFalse(greaterThan.compare(colDouble, litDouble));
        assertFalse(greaterThan.compare(colDouble, litInt));

        litDouble = "100";
        litInt = "100";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(142);
        colInt.add(Type.NAN);
        colInt.add(121);

        assertTrue(greaterThan.compare(colInt, litDouble));
        assertTrue(greaterThan.compare(colInt, litInt));

        colInt.add(Type.NOVALUE);

        assertFalse(greaterThan.compare(colInt, litDouble));
        assertFalse(greaterThan.compare(colInt, litInt));


        String litStr = "Apple";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Coarse");
        colStr.add("More");
        colStr.add("Door");

        assertTrue(greaterThan.compare(colStr, litStr));

        colStr.add("Ad");

        assertFalse(greaterThan.compare(colStr, litStr));
    }
}
