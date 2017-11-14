package db.operation;

import db.Column;
import db.Type;

import java.util.regex.Pattern;

abstract class AbstractOperation implements Operation {

    /* Returns the resulting column of the operation on a columns and a literal. */
    public Column operation(String columnName, Column column, String literal) {
        if (columnAndLiteralAreIntegers(column, literal)) {
            return operationInt(columnName, column, Integer.parseInt(literal));
        }
        return operationFloat(columnName, column, Double.parseDouble(literal));
    }

    /* Returns the resulting column of the operation on 2 columns. */
    public Column operation(String columnName, Column c1, Column c2) {
        if (bothColumnsContainIntegers(c1, c2)) {
            return operationInt(columnName, c1, c2);
        }
        return operationFloat(columnName, c1, c2);
    }

    abstract protected Column<Double> operationFloat(String columnName, Column c1, Column c2);

    abstract protected Column<Double> operationFloat(String columnName, Column column,
                                                     double literal);

    abstract protected Column<Integer> operationInt(String columnName, Column<Integer> c1,
                                                  Column<Integer> c2);

    abstract protected Column<Integer> operationInt(String columnName, Column<Integer> column,
                                                    int literal);

    /* Returns true if both columns contain floats or integers, false otherwise. */
    protected static boolean bothColumnsContainIntegers(Column c1, Column c2) {
        return c1.getType() == Type.INT && c2.getType() == Type.INT;
    }

    /* Returns true if column contains numbers and literal is a number, false otherwise. */
    protected static boolean columnAndLiteralAreIntegers(Column column, String literal) {
        String intPattern = "\\d+";
        boolean literalIsInt = Pattern.matches(intPattern, literal);

        return !literal.contains("'") && literalIsInt && column.getType() == Type.INT;
    }

}
