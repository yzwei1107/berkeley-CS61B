package db.operation;

import db.Column;
import db.Type;

/**
 * Implementation of the multiplication arithmetic operator '*'.
 */

public class Multiplication extends AbstractOperation {

    public Multiplication(Column c1, Column c2) {
        super(c1, c2);
    }

    @Override
    public Column operate(String columnName) {
        if (c1.getType() == Type.FLOAT) {
            return c1.size() > c2.size()? multiplyFloat(columnName, c1, c2) : multiplyFloat(columnName, c2, c1);
        }
        return c1.size() > c2.size()? multiplyInt(columnName, c1, c2) : multiplyInt(columnName, c2, c1);
    }

    private static Column<Double> multiplyFloat(
            String columnName, Column<Double> longerCol, Column<Double> shorterCol) {
        Column<Double> result = new Column<>(columnName, longerCol.getType());
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

    private static Column<Integer> multiplyInt (
            String columnName, Column<Integer> longerCol, Column<Integer> shorterCol) {
        Column<Integer> result = new Column<>(columnName, longerCol.getType());
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
