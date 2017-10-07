import db.Table;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

public class TableTest {

    @Test
    public void createTableAndAddRows() {
        Table table = new Table("table");
        table.addColumn("Name", "string");
        table.addColumn("Age", "int");
        table.addColumn("Height", "float");

        ArrayList<String> row1 = new ArrayList<>();
        row1.add("Harold");
        row1.add("19");
        row1.add("1.75");

        ArrayList<String> row2 = new ArrayList<>();
        row2.add("Mike");
        row2.add("29");
        row2.add("1.79");

        ArrayList<String> row3 = new ArrayList<>();
        row3.add("Arnold");
        row3.add("42");
        row3.add("1.83");

        table.addRow(row1);
        table.addRow(row2);
        table.addRow(row3);

        String expected = "Name string,Age int,Height float\n" +
                "'Harold',19,1.75\n" +
                "'Mike',29,1.79\n" +
                "'Arnold',42,1.83";

        assertEquals(expected, table.toString());
    }
}
