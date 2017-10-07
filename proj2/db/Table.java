package db;

import java.util.ArrayList;

public class Table {
    private String name;
    private ArrayList<String> columnNames;
    private ArrayList<String> columnTypes;
    private ArrayList<Column> columns;

    public Table(String name) {
        this.name = name;
        this.columnNames = new ArrayList<>();
        this.columnTypes = new ArrayList<>();
        this.columns = new ArrayList<>();

    }

    /* Adds column to table */
    public void addColumn(String columnName, String type) {
        columnNames.add(columnName);
        columnTypes.add(type);

        if (type.equals("float")) {
            columns.add(new Column<Double>());
        } else if (type.equals("int")) {
            columns.add(new Column<Integer>());
        } else if (type.equals("string")) {
            columns.add(new Column<String>());
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    /* Adds row to table */
    public void addRow(ArrayList<String> row) {
        for (int i = 0; i < row.size(); i++) {
            String columnType = columnTypes.get(i);
            Column column = columns.get(i);

            if (columnType.equals("float")) {
                column.add(Double.parseDouble(row.get(i)));
            } else if (columnType.equals("int")) {
                column.add(Integer.parseInt(row.get(i)));
            } else if (columnType.equals("string")) {
                column.add(row.get(i));
            } else {
                throw new IllegalArgumentException("Invalid type");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columnNames.get(i)).append(" ").append(columnTypes.get(i)).append(",");
        }

        sb.setLength(sb.length() - 1);
        sb.append("\n");

        for (int i = 0; i < columns.get(0).size(); i++) {
            for (int j = 0; j < columns.size(); j++) {
                Column column = columns.get(j);
                if (columnTypes.get(j).equals("string")) {
                    sb.append("'").append(column.get(i)).append("',");
                } else {
                    sb.append(column.get(i)).append(",");
                }
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    public static void main(String[] args) {
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

        System.out.println(table);
    }

}
