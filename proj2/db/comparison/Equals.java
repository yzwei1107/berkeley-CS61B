package db.comparison;

import db.Column;

/**
 * Implementation of the '=' operator.
 */

public class Equals extends AbstractComparison {

    @Override
    protected boolean compareToNumber(Column column, Double literal, int row) {
        if (column.isNOVALUE(row) || column.isNaN(row) ) {
            return false;
        }

        Number value = (Number) column.get(row);
        return value.doubleValue() == literal;
    }

    @Override
    protected boolean compareToString(Column<String> column, String literal, int row) {
        return !column.isNOVALUE(row) && column.get(row).equals(literal);
    }

    @Override
    protected boolean compareNumericColumns(Column c1, Column c2, int row) {
        if (c1.isNaN(row) && c2.isNaN(row)) {
            return true;
        } else if (c1.isNaN(row) || c2.isNaN(row) || c1.isNOVALUE(row) || c2.isNOVALUE(row)) {
            return false;
        }

        Number val1 = (Number) c1.get(row);
        Number val2 = (Number) c2.get(row);
        return val1.doubleValue() == val2.doubleValue();
    }

    @Override
    protected boolean compareStringColumns(Column<String> c1, Column<String> c2, int row) {
        if (c1.isNOVALUE(row) || c2.isNOVALUE(row)) {
            return false;
        }
        return c1.get(row).equals(c2.get(row));
    }

}
