package db.operation;

import db.Column;
import db.Type;

/**
 * Implementation of the multiplication arithmetic operator '*'.
 */

public class Multiplication extends AbstractOperation {

    @Override
    protected Column<Double> operationFloat(String columnName, Column c1, Column c2) {
        Column<Double> result = new Column<>(columnName, Type.FLOAT);
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNaN(i) || c2.isNaN(i)) {
                result.add(Type.NAN);
            } else if (c1.isNOVALUE(i) && c2.isNOVALUE(i)) {
                result.add(Type.NOVALUE);
            } else {
                Number c1Val = c1.isNOVALUE(i) ? 0 : (Number) c1.get(i);
                Number c2Val = c2.isNOVALUE(i) ? 0 : (Number) c2.get(i);

                result.add(c1Val.doubleValue() * c2Val.doubleValue());
            }
        }

        return result;
    }

    @Override
    protected Column<Double> operationFloat(String columnName, Column column, double literal) {
        Column<Double> result = new Column<>(columnName, Type.FLOAT);
        for (int i = 0; i < column.size(); i++) {
            if (column.isNaN(i)) {
                result.add(Type.NAN);
            } else if (column.isNOVALUE(i)) {
                result.add(0.0);
            } else {
                result.add(((Number) column.get(i)).doubleValue() * literal);
            }
        }

        return result;
    }

    protected Column<Integer> operationInt(String columnName, Column<Integer> c1,
                                           Column<Integer> c2) {
        Column<Integer> result = new Column<>(columnName, Type.INT);
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNaN(i) || c2.isNaN(i)) {
                result.add(Type.NAN);
            } else if (c1.isNOVALUE(i) && c2.isNOVALUE(i)) {
                result.add(Type.NOVALUE);
            } else {
                int c1Val = c1.isNOVALUE(i) ? 0 : c1.get(i);
                int c2Val = c2.isNOVALUE(i) ? 0 : c2.get(i);

                result.add(c1Val * c2Val);
            }
        }

        return result;
    }

    @Override
    protected Column<Integer> operationInt(String columnName, Column<Integer> column, int literal) {
        Column<Integer> result = new Column<>(columnName, Type.INT);
        for (int i = 0; i < column.size(); i++) {
            if (column.isNaN(i)) {
                result.add(Type.NAN);
            } else if (column.isNOVALUE(i)) {
                result.add(0);
            } else {
                result.add(column.get(i) * literal);
            }
        }

        return result;
    }

}
