package db.comparison;

import db.Column;

/**
 * Implementation of the '<' operator.
 */

public class LessThan extends AbstractComparison {
    @Override
    protected boolean compareToNumber(Column column, Double literal) {
        for (int i = 0; i < column.size(); i++) {
            if (column.isNOVALUE(i) || column.isNaN(i)) {
                return false;
            }

            Number value = (Number) column.get(i);
            if (value.doubleValue() >= literal) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean compareToString(Column<String> column, String literal) {
        for (int i = 0; i < column.size(); i++) {
            if (column.isNOVALUE(i) || column.isNaN(i) || column.get(i).compareTo(literal) >= 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected boolean compareNumericColumns(Column c1, Column c2) {
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNOVALUE(i) || c2.isNOVALUE(i) || c1.isNaN(i)) {
                return false;
            } else if (c2.isNaN(i)) {
                continue;
            }

            Number value1 = (Number) c1.get(i);
            Number value2 = (Number) c2.get(i);
            if (value1.doubleValue() >= value2.doubleValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean compareStringColumns(Column<String> c1, Column<String> c2) {
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNOVALUE(i) || c2.isNOVALUE(i) || c1.isNaN(i)) {
                return false;
            } else if (c2.isNaN(i)) {
                continue;
            } else if (c1.get(i).compareTo(c2.get(i)) >= 0) {
                return false;
            }
        }
        return true;
    }
}
