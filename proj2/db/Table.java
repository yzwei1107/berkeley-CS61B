package db;

import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Table representation implemented using a list of columns.
 *
 * @author moboa
 */

public class Table {
    private String name;
    private ArrayList<String> columnNames;
    private ArrayList<Type> columnTypes;
    private ArrayList<Column> columns;
    private HashMap<String, Integer> nameToIndexMap;

    public Table(String name) {
        this.name = name;
        this.columnNames = new ArrayList<>();
        this.columnTypes = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.nameToIndexMap = new HashMap<>();

    }

    /* Adds column to table. */
    public void addColumn(String columnName, Type type) {
        columnNames.add(columnName);
        columnTypes.add(type);

        if (type == Type.FLOAT) {
            columns.add(new Column<Double>());
        } else if (type == Type.INT) {
            columns.add(new Column<Integer>());
        } else if (type == Type.STRING) {
            columns.add(new Column<String>());
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
        nameToIndexMap.put(columnName, columns.size() - 1);
    }

    /* Adds row to table. */
    public void addRow(List<String> row) {
        for (int i = 0; i < row.size(); i++) {
            Type columnType = columnTypes.get(i);
            Column column = columns.get(i);

            if (columnType == Type.FLOAT) {
                column.add(Double.parseDouble(row.get(i)));
            } else if (columnType == Type.INT) {
                column.add(Integer.parseInt(row.get(i)));
            } else if (columnType == Type.STRING) {
                column.add(row.get(i));
            } else {
                throw new IllegalArgumentException("Invalid type");
            }
        }
    }

    public boolean isEmpty() {
        return columns.isEmpty();
    }

    @Override
    public String toString() {
        if(isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columnNames.get(i)).append(" ");
            sb.append(columnTypes.get(i).toString().toLowerCase()).append(",");
        }

        sb.setLength(sb.length() - 1);
        sb.append("\n");

        for (int i = 0; i < columns.get(0).size(); i++) {
            for (int j = 0; j < columns.size(); j++) {
                Column column = columns.get(j);
                if (columnTypes.get(j) == Type.STRING) {
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

    /* Joins a list of tables from left to right and returns the final table. */
    public static Table join(String name, List<Table> tableList) {
        Stack<Table> tableStack = joinMultipleTableHelper(tableList);

        while (tableStack.size() > 1) {
            tableStack.push(join("", tableStack.pop(), tableStack.pop()));
        }

        tableStack.peek().name = name;
        return tableStack.pop();
    }

    /* Returns a stack of tables to be joined. */
    private static Stack<Table> joinMultipleTableHelper(List<Table> tableList) {
        Stack<Table> tableStack = new Stack<>();
        for (int i = tableList.size() - 1; i >= 0; i--) {
            tableStack.push(tableList.get(i));
        }

        return tableStack;
    }

    /* Joins 2 tables and returns joined table. */
    public static Table join(String name, Table table1, Table table2) {
        HashMap<String, Integer> t1CommonColumnIndices = new HashMap<>();
        HashMap<String, Integer> t2CommonColumnIndices = new HashMap<>();
        ArrayList<String> commonColumnNames = new ArrayList<>();

        for (int i = 0; i < table1.columns.size(); i++) {
            String t1ColumnName = table1.columnNames.get(i);
            Type t1ColumnType = table1.columnTypes.get(i);

            for (int j = 0; j < table2.columns.size(); j++) {
                String t2ColumnName = table2.columnNames.get(j);
                Type t2ColumnType = table2.columnTypes.get(j);

                if (t1ColumnName.equals(t2ColumnName) && t1ColumnType == t2ColumnType) {
                    t1CommonColumnIndices.put(t1ColumnName, i);
                    t2CommonColumnIndices.put(t2ColumnName, j);
                    commonColumnNames.add(t1ColumnName);
                    break;
                }
            }
        }

        if (commonColumnNames.isEmpty()) {
            Table joined = cartesianJoin(table1, table2);
            joined.name = name;
            return joined;
        }

        Table joinedTable = new Table(name);

        for (String columnName : commonColumnNames) {
            Type columnType = table1.columnTypes.get(t1CommonColumnIndices.get(columnName));
            joinedTable.addColumn(columnName, columnType);
        }

        addUnsharedColumns(joinedTable, table1, commonColumnNames);
        addUnsharedColumns(joinedTable, table2, commonColumnNames);

        Column t1Column = table1.columns.get(t1CommonColumnIndices.get(commonColumnNames.get(0)));
        Column t2Column = table2.columns.get(t2CommonColumnIndices.get(commonColumnNames.get(0)));
        for (int i = 0; i < t1Column.size(); i++) {
            for (int j = 0; j < t2Column.size(); j++) {
                if (canMergeRows(table1, table2, commonColumnNames, i, j)) {
                    joinedTable.addRow(mergeRows(table1, table2, joinedTable, i, j));
                }
            }
        }

        return joinedTable;
    }

    /* Returns the cartesian join of the two tables. */
    private static Table cartesianJoin(Table t1, Table t2) {
        Table joined = new Table("");
        List<String> emptyList = new ArrayList<>();

        addUnsharedColumns(joined, t1, emptyList);
        addUnsharedColumns(joined, t2, emptyList);

        for (int i = 0; i < t1.columns.get(0).size(); i++) {
            for (int j = 0; j < t2.columns.get(0).size(); j++) {
                joined.addRow(mergeRows(t1, t2, joined, i, j));
            }
        }

        return joined;
    }

    /* Adds columns that aren't in the common list from a table to the joined table. */
    private static void addUnsharedColumns(Table joined, Table source, List<String> commonNames) {
        for (int i = 0; i < source.columns.size(); i++) {
            String columnName = source.columnNames.get(i);
            if (!commonNames.contains(columnName)) {
                Type columnType = source.columnTypes.get(i);
                joined.addColumn(columnName, columnType);
            }
        }
    }

    /* Returns true if all shared columns have the same values, otherwise false. */
    private static boolean canMergeRows(Table t1, Table t2, List<String> sharedColumnNames,
                                       int t1Row, int t2Row) {
        for (String sharedColumnName : sharedColumnNames) {
            Column t1Column = t1.columns.get(t1.nameToIndexMap.get(sharedColumnName));
            Column t2Column = t2.columns.get(t2.nameToIndexMap.get(sharedColumnName));

            if (!t1Column.get(t1Row).equals(t2Column.get(t2Row))) {
                return false;
            }
        }

        return true;
    }

    /* Returns a row merged from the 2 others in 2 different tables. */
    private static List<String> mergeRows(Table t1, Table t2, Table joined, int t1Row, int t2Row) {
        ArrayList<String> row = new ArrayList<>();

        for (int i = 0; i < joined.columns.size(); i++) {
            String joinedColumnName = joined.columnNames.get(i);
            if (t1.columnNames.contains(joinedColumnName)) {
                Column relevantColumn = t1.columns.get(t1.nameToIndexMap.get(joinedColumnName));
                row.add(relevantColumn.get(t1Row).toString());
            } else {
                Column relevantColumn = t2.columns.get(t2.nameToIndexMap.get(joinedColumnName));
                row.add(relevantColumn.get(t2Row).toString());
            }

        }

        return row;
    }

    public static void main(String[] args) {
        Table table = new Table("table");
        table.addColumn("Name", Type.STRING);
        table.addColumn("Age", Type.INT);
        table.addColumn("Height", Type.FLOAT);

        List<String> row1 = new ArrayList<>();
        row1.add("Harold");
        row1.add("19");
        row1.add("1.75");

        List<String> row2 = new ArrayList<>();
        row2.add("Mike");
        row2.add("29");
        row2.add("1.79");

        List<String> row3 = new ArrayList<>();
        row3.add("Arnold");
        row3.add("42");
        row3.add("1.83");

        table.addRow(row1);
        table.addRow(row2);
        table.addRow(row3);

        System.out.println(table);
    }

}
