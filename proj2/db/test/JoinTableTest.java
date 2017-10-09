import db.Table;
import db.Type;

import javafx.scene.control.Tab;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

public class JoinTableTest {
    @Test
    public void OneColumnOneMatch() {
        Table table1 = new Table("t1");
        table1.addColumn("X", Type.INT);
        table1.addColumn("Y", Type.INT);
        table1.addRow(Arrays.asList("2", "5"));
        table1.addRow(Arrays.asList("8", "3"));
        table1.addRow(Arrays.asList("13", "7"));

        Table table2 = new Table("t2");
        table2.addColumn("X", Type.INT);
        table2.addColumn("Z", Type.INT);
        table2.addRow(Arrays.asList("2", "4"));
        table2.addRow(Arrays.asList("8", "9"));
        table2.addRow(Arrays.asList("10", "1"));

        Table joined = Table.join("t3", table1, table2);

        Table expected = new Table("t3");
        expected.addColumn("X", Type.INT);
        expected.addColumn("Y", Type.INT);
        expected.addColumn("Z", Type.INT);
        expected.addRow(Arrays.asList("2", "5", "4"));
        expected.addRow(Arrays.asList("8", "3", "9"));

        assertEquals(expected.toString(), joined.toString());
    }


    @Test
    public void OneColumnThreeMatches() {
        Table table1 = new Table("t1");
        table1.addColumn("X", Type.INT);
        table1.addColumn("Y", Type.INT);
        table1.addRow(Arrays.asList("1", "4"));
        table1.addRow(Arrays.asList("2", "5"));
        table1.addRow(Arrays.asList("3", "6"));

        Table table2 = new Table("t2");
        table2.addColumn("X", Type.INT);
        table2.addColumn("Z", Type.INT);
        table2.addRow(Arrays.asList("1", "7"));
        table2.addRow(Arrays.asList("7", "7"));
        table2.addRow(Arrays.asList("1", "9"));
        table2.addRow(Arrays.asList("1", "11"));

        Table joined = Table.join("t3", table1, table2);

        Table expected = new Table("t3");
        expected.addColumn("X", Type.INT);
        expected.addColumn("Y", Type.INT);
        expected.addColumn("Z", Type.INT);
        expected.addRow(Arrays.asList("1", "4", "7"));
        expected.addRow(Arrays.asList("1", "4", "9"));
        expected.addRow(Arrays.asList("1", "4", "11"));

        assertEquals(expected.toString(), joined.toString());
    }

    @Test
    public void OneColumnNoMatch() {
        Table table1 = new Table("t1");
        table1.addColumn("X", Type.INT);
        table1.addColumn("Y", Type.INT);
        table1.addRow(Arrays.asList("1", "7"));
        table1.addRow(Arrays.asList("7", "7"));
        table1.addRow(Arrays.asList("1", "9"));

        Table table2 = new Table("t2");
        table2.addColumn("X", Type.INT);
        table2.addColumn("Z", Type.INT);
        table2.addRow(Arrays.asList("3", "8"));
        table2.addRow(Arrays.asList("4", "9"));
        table2.addRow(Arrays.asList("5", "10"));

        Table joined = Table.join("t3", table1, table2);

        Table expected = new Table("t3");
        expected.addColumn("X", Type.INT);
        expected.addColumn("Y", Type.INT);
        expected.addColumn("Z", Type.INT);

        assertEquals(expected.toString(), joined.toString());
    }

    @Test
    public void TwoColumnsOneMatch() {
        Table table1 = new Table("t1");
        table1.addColumn("X", Type.INT);
        table1.addColumn("Y", Type.INT);
        table1.addColumn("Z", Type.INT);
        table1.addColumn("W", Type.INT);
        table1.addRow(Arrays.asList("1", "7", "2", "10"));
        table1.addRow(Arrays.asList("7", "7", "4", "1"));
        table1.addRow(Arrays.asList("1", "9", "9", "1"));

        Table table2 = new Table("t2");
        table2.addColumn("W", Type.INT);
        table2.addColumn("B", Type.INT);
        table2.addColumn("Z", Type.INT);
        table2.addRow(Arrays.asList("1", "7", "4"));
        table2.addRow(Arrays.asList("7", "7", "3"));
        table2.addRow(Arrays.asList("1", "9", "6"));
        table2.addRow(Arrays.asList("1", "11", "9"));

        Table joined = Table.join("t3", table1, table2);

        Table expected = new Table("t3");
        expected.addColumn("Z", Type.INT);
        expected.addColumn("W", Type.INT);
        expected.addColumn("X", Type.INT);
        expected.addColumn("Y", Type.INT);
        expected.addColumn("B", Type.INT);
        expected.addRow(Arrays.asList("4", "1", "7", "7", "7"));
        expected.addRow(Arrays.asList("9", "1", "1", "9", "11"));

        assertEquals(expected.toString(), joined.toString());
    }

    @Test
    public void TwoColumnsTwoMatches() {
        Table table1 = new Table("t1");
        table1.addColumn("Y", Type.INT);
        table1.addColumn("Z", Type.INT);
        table1.addColumn("W", Type.INT);
        table1.addRow(Arrays.asList("1", "1", "5"));
        table1.addRow(Arrays.asList("1", "0", "2"));
        table1.addRow(Arrays.asList("2", "7", "3"));

        Table table2 = new Table("t2");
        table2.addColumn("W", Type.INT);
        table2.addColumn("B", Type.INT);
        table2.addColumn("Z", Type.INT);
        table2.addRow(Arrays.asList("4", "3", "1"));
        table2.addRow(Arrays.asList("5", "7", "1"));
        table2.addRow(Arrays.asList("5", "8", "1"));
        table2.addRow(Arrays.asList("3", "11", "7"));

        Table joined = Table.join("t3", table1, table2);

        Table expected = new Table("t3");
        expected.addColumn("Z", Type.INT);
        expected.addColumn("W", Type.INT);
        expected.addColumn("Y", Type.INT);
        expected.addColumn("B", Type.INT);
        expected.addRow(Arrays.asList("1", "5", "1", "7"));
        expected.addRow(Arrays.asList("1", "5", "1", "8"));
        expected.addRow(Arrays.asList("7", "3", "2", "11"));

        assertEquals(expected.toString(), joined.toString());
    }

    @Test
    public void ThreeTables() {
        Table t1 = new Table("t1");
        t1.addColumn("A", Type.INT);
        t1.addColumn("B", Type.INT);
        t1.addColumn("C", Type.INT);
        t1.addRow(Arrays.asList("1", "2", "5"));
        t1.addRow(Arrays.asList("1", "0", "1"));

        Table t2 = new Table("t2");
        t2.addColumn("D", Type.INT);
        t2.addColumn("A", Type.INT);
        t2.addColumn("B", Type.INT);
        t2.addRow(Arrays.asList("3", "1", "2"));
        t2.addRow(Arrays.asList("5", "2", "1"));

        Table t3 = new Table("t3");
        t3.addColumn("B", Type.INT);
        t3.addColumn("A", Type.INT);
        t3.addColumn("E", Type.INT);
        t3.addRow(Arrays.asList("2", "1", "4"));
        t3.addRow(Arrays.asList("4", "3", "7"));

        List<Table> tableList = Arrays.asList(t1, t2, t3);

        Table expected = new Table("joined");
        expected.addColumn("A", Type.INT);
        expected.addColumn("B", Type.INT);
        expected.addColumn("C", Type.INT);
        expected.addColumn("D", Type.INT);
        expected.addColumn("E", Type.INT);
        expected.addRow(Arrays.asList("1", "2", "5", "3", "4"));

        Table joined = Table.join("joined", tableList);
        assertEquals(expected.toString(), joined.toString());
    }

    @Test
    public void cartesianJoin() {
        Table t1 = new Table("t1");
        t1.addColumn("X", Type.INT);
        t1.addColumn("Y", Type.INT);
        t1.addColumn("Z", Type.INT);
        t1.addRow(Arrays.asList("2", "5", "4"));
        t1.addRow(Arrays.asList("8", "3", "9"));

        Table t2 = new Table("t2");
        t2.addColumn("A", Type.INT);
        t2.addColumn("B", Type.INT);
        t2.addRow(Arrays.asList("7", "0"));
        t2.addRow(Arrays.asList("2", "8"));

        Table expected = new Table("t3");
        expected.addColumn("X", Type.INT);
        expected.addColumn("Y", Type.INT);
        expected.addColumn("Z", Type.INT);
        expected.addColumn("A", Type.INT);
        expected.addColumn("B", Type.INT);
        expected.addRow(Arrays.asList("2", "5", "4", "7", "0"));
        expected.addRow(Arrays.asList("2", "5", "4", "2", "8"));
        expected.addRow(Arrays.asList("8", "3", "9", "7", "0"));
        expected.addRow(Arrays.asList("8", "3", "9", "2", "8"));

        Table joined = Table.join("t3", t1, t2);

        assertEquals(expected.toString(), joined.toString());
    }
}
