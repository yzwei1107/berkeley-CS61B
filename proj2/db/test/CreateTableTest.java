import db.Table;
import db.Type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

public class CreateTableTest {

    @Test
    public void createTableAndAddRows() {
        Table table = new Table("table");
        table.addColumn("Name", Type.STRING);
        table.addColumn("Age", Type.INT);
        table.addColumn("Height", Type.FLOAT);

        List<String> row1 = Arrays.asList("Harold", "19", "1.75");
        List<String> row2 = Arrays.asList("Mike", "29", "1.79");
        List<String> row3 = Arrays.asList("Arnold", "42", "1.83");

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
