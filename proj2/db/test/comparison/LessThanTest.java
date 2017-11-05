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
        assertTrue(lessThan.compare(c1Double, c2Int));

        c1Double.add(15.0);
        c2Int.add(12);

        assertFalse(lessThan.compare(c1Double, c2Int));


        Column<Integer> c1Int = new Column<>("c1", Type.INT);
        c1Int.add(1);
        c1Int.add(44);
        c1Int.add(10);

        assertTrue(lessThan.compare(c1Int, c2Int));

        c1Int.add(Type.NAN);
        c2Int.add(Type.NOVALUE);
        assertFalse(lessThan.compare(c1Int, c2Int));


        Column<String> c1String = new Column<>("c1", Type.STRING);
        Column<String> c2String = new Column<>("c2", Type.STRING);
        c1String.add("Foo");
        c1String.add("Gal");

        c2String.add("Zoo");
        c2String.add("Gao");

        assertTrue(lessThan.compare(c1String, c2String));

        c1String.add("Is");
        c2String.add("As");

        assertFalse(lessThan.compare(c1String, c2String));
    }

    @Test
    public void lessThanLiteral() {
        String litDouble = "12.0";
        String litInt = "12";
        Column<Double> colDouble = new Column<>("col", Type.FLOAT);
        colDouble.add(2.0);
        colDouble.add(1.0);
        colDouble.add(5.0);

        LessThan lessThan = new LessThan();
        assertTrue(lessThan.compare(colDouble, litDouble));
        assertTrue(lessThan.compare(colDouble, litInt));

        colDouble.add(15.0);

        assertFalse(lessThan.compare(colDouble, litDouble));
        assertFalse(lessThan.compare(colDouble, litInt));

        litDouble = "153.3";
        litInt = "153";
        Column<Integer> colInt = new Column<>("col", Type.INT);
        colInt.add(142);
        colInt.add(142);
        colInt.add(142);

        assertTrue(lessThan.compare(colInt, litDouble));
        assertTrue(lessThan.compare(colInt, litInt));

        colInt.add(Type.NOVALUE);

        assertFalse(lessThan.compare(colInt, litDouble));
        assertFalse(lessThan.compare(colInt, litInt));


        String litStr = "Morse";
        Column<String> colStr = new Column<>("col", Type.STRING);
        colStr.add("Coarse");
        colStr.add("More");
        colStr.add("Door");

        assertTrue(lessThan.compare(colStr, litStr));

        colStr.add("Morse");

        assertFalse(lessThan.compare(colStr, litStr));
    }
}
