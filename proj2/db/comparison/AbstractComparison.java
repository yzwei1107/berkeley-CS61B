package db.comparison;

import db.Column;
import db.Type;

import java.util.regex.Pattern;

/**
 * Template for comparison operator classes
 */

public abstract class AbstractComparison implements Comparison {

    /* Compares every row of the column to a literal. */
    @Override
    public boolean compare(Column column, String literal, int row) {
        if (columnAndLiteralAreNumeric(column, literal)) {
            return compareToNumber(column, Double.parseDouble(literal), row);
        }
        return compareToString(column, literal, row);
    }

    /* Compares two columns, row by row. */
    @Override
    public boolean compare(Column c1, Column c2, int row) {
        if (containNumbers(c1, c2)) {
            return compareNumericColumns(c1, c2, row);
        }
        return compareStringColumns(c1, c2, row);
    }

    /* Returns true if both columns contain floats or integers, false otherwise. */
    protected boolean containNumbers(Column c1, Column c2) {
        return (c1.getType() == Type.FLOAT || c1.getType() == Type.INT)
                && (c2.getType() == Type.FLOAT || c2.getType() == Type.INT);
    }

    /* Returns true if column contains numbers and literal is a number, false otherwise. */
    protected boolean columnAndLiteralAreNumeric(Column column, String literal) {
        String floatPattern = "([0-9].*)\\.([0-9].*)";
        String intPattern = "\\d+";
        boolean literalIsFloat = Pattern.matches(floatPattern, literal);
        boolean literalIsInt = Pattern.matches(intPattern, literal);

        return !literal.contains("'")
                && (column.getType() == Type.FLOAT || column.getType() == Type.INT)
                && (literalIsFloat || literalIsInt);
    }

    /* Compares every row of the column to a float or integer. */
    protected abstract boolean compareToNumber(Column column, Double literal, int row);

    /* Compares every row of the column to a string. */
    protected abstract boolean compareToString(Column<String> column, String literal, int row);

    /* Compares two columns containing floats or integers row-by-row. */
    protected abstract boolean compareNumericColumns(Column c1, Column c2, int row);

    /* Compares two columns containing strings row-by-row. */
    protected abstract boolean compareStringColumns(Column<String> c1, Column<String> c2, int row);
}
