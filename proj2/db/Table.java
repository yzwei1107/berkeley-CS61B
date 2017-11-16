package db;

import db.comparison.*;
import db.operation.*;
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
    private HashMap<String, Integer> colNameToIndex;
    private int numColumns;

    public Table(String name) {
        this.name = name;
        this.columnNames = new ArrayList<>();
        this.columnTypes = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.colNameToIndex = new HashMap<>();

    }

    public Table(Table otherTable) {
        this.name = otherTable.name;
        this.columnNames = new ArrayList<>(otherTable.columnNames);
        this.columnTypes = new ArrayList<>(otherTable.columnTypes);
        this.columns = new ArrayList<>();
        this.colNameToIndex = new HashMap<>(otherTable.colNameToIndex);

        for (int i = 0; i < otherTable.columns.size(); i++) {
            this.columns.add(new Column(otherTable.columns.get(i)));
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Adds column to table. */
    public void addColumn(String columnName, Type type) {
        columnNames.add(columnName);
        columnTypes.add(type);
        numColumns++;

        if (type == Type.FLOAT) {
            columns.add(new Column<Double>(columnName, type));
        } else if (type == Type.INT) {
            columns.add(new Column<Integer>(columnName, type));
        } else if (type == Type.STRING) {
            columns.add(new Column<String>(columnName, type));
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
        colNameToIndex.put(columnName, columns.size() - 1);
    }

    /* Removes column with the specified name if found */
    public void removeColumn(String columnName) {
        if (!containsColumn(columnName)) {
            return;
        }

        int index = colNameToIndex.get(columnName);
        colNameToIndex.remove(columnName);
        columnNames.remove(index);
        columnTypes.remove(index);
        columns.remove(index);
        numColumns--;
    }

    /* Adds row to table. */
    public void addRow(List<String> row) {
        for (int i = 0; i < row.size(); i++) {
            Column column = columns.get(i);

            if (row.get(i).equals("NOVALUE")) {
                column.add(Type.NOVALUE);
                continue;
            }

            Type columnType = columnTypes.get(i);
            switch (columnType) {
                case FLOAT:
                    if (row.get(i).equals("NaN")) {
                        column.add(Type.NAN);
                    } else {
                        column.add(Double.parseDouble(row.get(i)));
                    }
                    break;
                case INT:
                    if (row.get(i).equals("NaN")) {
                        column.add(Type.NAN);
                    } else {
                        column.add(Integer.parseInt(row.get(i)));
                    }
                    break;
                case STRING:
                    column.add(row.get(i));
                    break;
            }
        }
    }

    /* Returns number of columns. */
    public int getNumColumns() {
        return numColumns;
    }

    public String getColumnName(int index) {
        return columnNames.get(index);
    }

    /* Returns type of column at specified index. */
    public Type getColumnType(int index) {
        return columnTypes.get(index);
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

        for (int row = 0; row < columns.get(0).size(); row++) {
            for (int col = 0; col < columns.size(); col++) {
                Column column = columns.get(col);
                sb.append(column.getItemString(row)).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    /* Joins a list of tables from left to right and returns the final table. */
    public static Table join(String name, List<Table> tableList) {
        if (tableList.size() == 1) {
            return new Table(tableList.get(0));
        }

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
            Column t1Column = t1.columns.get(t1.colNameToIndex.get(sharedColumnName));
            Column t2Column = t2.columns.get(t2.colNameToIndex.get(sharedColumnName));

            if  (t1Column.isNOVALUE(t1Row) || t1Column.isNaN(t1Row)
                    || t2Column.isNOVALUE(t2Row) || t2Column.isNaN(t2Row)) {
                return false;
            }

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
                Column relevantColumn = t1.columns.get(t1.colNameToIndex.get(joinedColumnName));
                if (relevantColumn.isNaN(t1Row)) {
                    row.add("NaN");
                } else if (relevantColumn.isNOVALUE(t1Row)) {
                    row.add("NOVALUE");
                } else {
                    row.add(relevantColumn.get(t1Row).toString());
                }
            } else {
                Column relevantColumn = t2.columns.get(t2.colNameToIndex.get(joinedColumnName));
                if (relevantColumn.isNaN(t2Row)) {
                    row.add("NaN");
                } else if (relevantColumn.isNOVALUE(t2Row)) {
                    row.add("NOVALUE");
                } else {
                    row.add(relevantColumn.get(t2Row).toString());
                }
            }

        }

        return row;
    }

    public boolean containsColumn(String columnName) {
        return colNameToIndex.containsKey(columnName);
    }

    public void makeTableCompliant(Comparison comparison, String[] condition) {
        int colIndex = colNameToIndex.get(condition[0]);
        Column c1 = columns.get(colIndex);

        if (containsColumn(condition[2])) {
            Column c2 = columns.get(colNameToIndex.get(condition[2]));

            for (int i = 0; i < c1.size(); i++) {
                if (!comparison.compare(c1, c2, i)) {
                    removeRow(i);
                }
            }
        } else {
            for (int i = 0; i < c1.size(); i++) {
                if (!comparison.compare(c1, condition[2], i)) {
                    removeRow(i);
                }
            }
        }
    }

    private void removeRow(int index) {
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).remove(index);
        }
    }


    public void evaluateArithmeticExpression(Table joined, Operation operation, String firstOperand,
                                             String secondOperand, String resultName) {
        Column c1 = joined.columns.get(joined.colNameToIndex.get(firstOperand));

        Column result;
        if (joined.containsColumn(secondOperand)) {
            Column c2 = joined.columns.get(joined.colNameToIndex.get(secondOperand));
            result = operation.operation(resultName, c1, c2);
        } else {
            result = operation.operation(resultName, c1, secondOperand);
        }

        this.columnNames.add(result.getName());
        this.columnTypes.add(result.getType());
        this.columns.add(result);
    }

    public void addColumnFromTable(Table otherTable, String columnName) {
        int index = otherTable.colNameToIndex.get(columnName);
        this.columns.add(new Column(otherTable.columns.get(index)));
        this.columnTypes.add(otherTable.columnTypes.get(index));
        this.columnNames.add(otherTable.columnNames.get(index));
        this.colNameToIndex.put(columnName, this.columns.size() - 1);
    }
}
