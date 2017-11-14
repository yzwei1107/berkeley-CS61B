package db.operation;

import db.Column;
import db.Type;

/**
 * Implementation of the multiplication arithmetic operator '*'.
 */

public class Multiplication extends AbstractOperation {

    @Override
    protected Column<Double> operationFloat(String columnName, Column c1, Column c2) {
        return c1.size() > c2.size()? multiplyFloat(columnName, c1, c2)
                : multiplyFloat(columnName, c2, c1);
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
                result.add((double) column.get(i) * literal);
            }
        }

        return result;
    }

    protected Column<Integer> operationInt(String columnName, Column<Integer> c1,
                                           Column<Integer> c2) {
        return c1.size() > c2.size()? multiplyInt(columnName, c1, c2)
                : multiplyInt(columnName, c2, c1);
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

    private static Column<Double> multiplyFloat(String columnName, Column longerCol,
                                                Column shorterCol) {
        Column<Double> result = new Column<>(columnName, Type.FLOAT);
        for (int i = 0; i < longerCol.size(); i++) {
            if (longerCol.isNaN(i) || shorterCol.isNaN(i)) {
                result.add(Type.NAN);
            } else if (longerCol.isNOVALUE(i) && shorterCol.isNOVALUE(i)) {
                result.add(Type.NOVALUE);
            } else if(shorterCol.isNOVALUE(i)) {
                result.add(0.0);
            } else if (i < shorterCol.size()) {
                if (longerCol.isNOVALUE(i)) {
                    result.add(0.0);
                } else {
                    result.add((double) longerCol.get(i) * (double) shorterCol.get(i));
                }
            } else {
                if (longerCol.isNOVALUE(i)) {
                    result.add(Type.NOVALUE);
                } else {
                    result.add((double) longerCol.get(i));
                }
            }
        }

        return result;
    }

    private static Column<Integer> multiplyInt (String columnName, Column<Integer> longerCol,
                                                Column<Integer> shorterCol) {
        Column<Integer> result = new Column<>(columnName, Type.INT);
        for (int i = 0; i < longerCol.size(); i++) {
            if (longerCol.isNaN(i) || shorterCol.isNaN(i)) {
                result.add(Type.NAN);
            } else if (longerCol.isNOVALUE(i) && shorterCol.isNOVALUE(i)) {
                result.add(Type.NOVALUE);
            } else if(shorterCol.isNOVALUE(i)) {
                result.add(0);
            } else if (i < shorterCol.size()) {
                if (longerCol.isNOVALUE(i)) {
                    result.add(0);
                } else {
                    result.add(longerCol.get(i) * shorterCol.get(i));
                }
            } else {
                if (longerCol.isNOVALUE(i)) {
                    result.add(Type.NOVALUE);
                } else {
                    result.add(longerCol.get(i));
                }
            }
        }

        return result;
    }

}
