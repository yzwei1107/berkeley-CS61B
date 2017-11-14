package db.comparison;

import db.Column;

/**
 * Implementation of the '>' operator.
 */

public class GreaterThan extends AbstractComparison {
    @Override
    protected boolean compareToNumber(Column column, Double literal, int row) {
        if (column.isNaN(row)) {
            return true;
        } else if (column.isNOVALUE(row)) {
            return false;
        }

        Number value = (Number) column.get(row);
        return value.doubleValue() > literal;
    }

    @Override
    protected boolean compareToString(Column<String> column, String literal, int row) {
        return !column.isNOVALUE(row) && column.get(row).compareTo(literal) > 0;
    }

    @Override
    protected boolean compareNumericColumns(Column c1, Column c2, int row) {
            if (c1.isNOVALUE(row) || c2.isNOVALUE(row) || c2.isNaN(row)) {
                return false;
            } else if (c1.isNaN(row)) {
                return true;
            }

            Number value1 = (Number) c1.get(row);
            Number value2 = (Number) c2.get(row);
            return value1.doubleValue() > value2.doubleValue();
    }

    @Override
    protected boolean compareStringColumns(Column<String> c1, Column<String> c2, int row) {
        if (c1.isNOVALUE(row) || c2.isNOVALUE(row)) {
            return false;
        }
        return c1.get(row).compareTo(c2.get(row)) > 0;
    }
}
