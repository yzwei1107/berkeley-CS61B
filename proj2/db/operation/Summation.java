package db.operation;

import db.Column;
import db.Type;

/**
 * Implementation of the addition/concatenation operator '+'.
 */
public class Summation extends AbstractOperation {

    public Summation(Column c1, Column c2) {
        super(c1, c2);
    }

    @Override
    public  Column operate(String columnName) {
        if (c1.getType() == Type.FLOAT) {
            return c1.size() > c2.size() ? sumFloat(columnName, c1, c2) : sumFloat(columnName, c2, c1);
        } else if (c1.getType() == Type.INT) {
            return c1.size() > c2.size() ? sumInt(columnName, c1, c2) : sumInt(columnName, c2, c1);
        }
        return concat(columnName, c1, c2);
    }

    private static Column<Double> sumFloat(
            String columnName, Column<Double> longerCol, Column<Double> shorterCol) {
        Column<Double> result = new Column<>(columnName, longerCol.getType());
            for (int i = 0; i < longerCol.size(); i++) {
                if (longerCol.isNaN(i) || shorterCol.isNaN(i)) {
                    result.add(Type.NAN);
                } else if (longerCol.isNOVALUE(i) && shorterCol.isNOVALUE(i)) {
                    result.add(Type.NOVALUE);
                } else if(shorterCol.isNOVALUE(i)) {
                    result.add(longerCol.get(i));
                } else if (i < shorterCol.size()) {
                    if (longerCol.isNOVALUE(i)) {
                        result.add(shorterCol.get(i));
                    } else {
                        result.add(longerCol.get(i) + shorterCol.get(i));
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

    private static Column<Integer> sumInt(
            String columnName, Column<Integer> longerCol, Column<Integer> shorterCol) {
        Column<Integer> result = new Column<>(columnName, longerCol.getType());
        for (int i = 0; i < longerCol.size(); i++) {
            if (longerCol.isNaN(i) || shorterCol.isNaN(i)) {
                result.add(Type.NAN);
            } else if (longerCol.isNOVALUE(i) && shorterCol.isNOVALUE(i)) {
                result.add(Type.NOVALUE);
            } else if(shorterCol.isNOVALUE(i)) {
                result.add(longerCol.get(i));
            } else if (i < shorterCol.size()) {
                if (longerCol.isNOVALUE(i)) {
                    result.add(shorterCol.get(i));
                } else {
                    result.add(longerCol.get(i) + shorterCol.get(i));
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


    static Column<String> concat(
            String columnName, Column<String> c1, Column<String> c2) {
        Column<String> result = new Column<>(columnName, c1.getType());
        if (c1.size() > c2.size()) {
            for (int i = 0; i < c1.size(); i++) {
                if (c1.isNaN(i) || c2.isNaN(i)) {
                    result.add(Type.NAN);
                } else if (c1.isNOVALUE(i) && c2.isNOVALUE(i)) {
                    result.add(Type.NOVALUE);
                } else if(c2.isNOVALUE(i)) {
                    result.add(c1.get(i));
                } else if (i < c2.size()) {
                    if (c1.isNOVALUE(i)) {
                        result.add(c2.get(i));
                    } else {
                        result.add(c1.get(i) + c2.get(i));
                    }
                } else {
                    if (c1.isNOVALUE(i)) {
                        result.add(Type.NOVALUE);
                    } else {
                        result.add(c1.get(i));
                    }
                }
            }
        } else {
            for (int i = 0; i < c2.size(); i++) {
                if (c2.isNaN(i) || c1.isNaN(i)) {
                    result.add(Type.NAN);
                } else if (c2.isNOVALUE(i) && c1.isNOVALUE(i)) {
                    result.add(Type.NOVALUE);
                } else if(c1.isNOVALUE(i)) {
                    result.add(c2.get(i));
                } else if (i < c1.size()) {
                    if (c2.isNOVALUE(i)) {
                        result.add(c1.get(i));
                    } else {
                        result.add(c1.get(i) + c2.get(i));
                    }
                } else {
                    if (c2.isNOVALUE(i)) {
                        result.add(Type.NOVALUE);
                    } else {
                        result.add(c2.get(i));
                    }
                }
            }

        }

        return result;
    }

}
