package db.comparison;

import db.Column;

/**
 * Implementation of the '!=' operator.
 */

public class NotEquals extends AbstractComparison {

    @Override
    protected boolean compareToNumber(Column column, Double literal) {
        for (int i = 0; i < column.size(); i++) {
            if (column.isNaN(i)) {
                return true;
            } else if (column.isNOVALUE(i)) {
                return false;
            }

            Number value = (Number) column.get(i);
            if (value.doubleValue() != literal) {
                return true;
            }
        }
            return false;
    }

    @Override
    protected boolean compareToString(Column<String> column, String literal) {
        for (int i = 0; i < column.size(); i++) {
            if (column.isNOVALUE(i)) {
                return false;
            } else if (column.isNaN(i) || !column.get(i).equals(literal)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean compareNumericColumns(Column c1, Column c2) {
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNOVALUE(i) || c2.isNOVALUE(i)) {
                return false;
            }else if (c1.isNaN(i) && c2.isNaN(i)) {
                continue;
            } else if (c1.isNaN(i) || c2.isNaN(i)) {
                return true;
            }

            Number val1 = (Number) c1.get(i);
            Number val2 = (Number) c2.get(i);
            if (val1.doubleValue() != val2.doubleValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean compareStringColumns(Column<String> c1, Column<String> c2) {
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNOVALUE(i) || c2.isNOVALUE(i)) {
                return false;
            } else if (c1.isNaN(i) && c2.isNaN(i)) {
                continue;
            } else if (c1.isNaN(i) || c2.isNaN(i)) {
                return true;

            } else if (!c1.get(i).equals(c2.get(i))) {
                return true;
            }
        }
        return false;
    }

}
