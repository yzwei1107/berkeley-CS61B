package db.operation;

import db.Column;
import db.Type;

import java.util.regex.Pattern;

/**
 * Implementation of the addition/concatenation operator '+'.
 */
public class Summation extends AbstractOperation {

    private boolean bothColumnsContainNumbers(Column c1, Column c2) {
        return (c1.getType() == Type.FLOAT || c1.getType() == Type.INT)
                && (c2.getType() == Type.FLOAT || c2.getType() == Type.INT);
    }

    private boolean columnAndLiteralAreNumeric(Column column, String literal) {
        String floatPattern = "-?([0-9].*)\\.([0-9].*)";
        String intPattern = "-?\\d+";
        boolean literalIsFloat = Pattern.matches(floatPattern, literal);
        boolean literalIsInt = Pattern.matches(intPattern, literal);

        return !literal.contains("'")
                && (column.getType() == Type.FLOAT || column.getType() == Type.INT)
                && (literalIsFloat || literalIsInt);
    }

    @Override
    public Column operation(String columnName, Column c1, Column c2) {
        if (bothColumnsContainNumbers(c1, c2)) {
            if (bothColumnsContainIntegers(c1, c2)) {
                return c1.size() > c2.size() ? sumInt(columnName, c1, c2) : sumInt(columnName, c2, c1);
            } else {
                return c1.size() > c2.size() ? sumFloat(columnName, c1, c2) : sumFloat(columnName, c2, c1);
            }
        }
        return concat(columnName, c1, c2);
    }

    @Override
    public Column operation(String columnName, Column column, String literal) {
        if (columnAndLiteralAreNumeric(column, literal)) {
            if (columnAndLiteralAreIntegers(column, literal)) {
                return operationInt(columnName, column, Integer.parseInt(literal));
            } else {
                return operationFloat(columnName, column, Double.parseDouble(literal));
            }
        }
        return concat(columnName, column, literal);
    }

    @Override
    protected Column<Double> operationFloat(String columnName, Column c1, Column c2) {
        return c1.size() > c2.size()? sumFloat(columnName, c1, c2) : sumFloat(columnName, c2, c1);
    }

    @Override
    protected Column<Double> operationFloat(String columnName, Column column, double literal) {
        Column<Double> result = new Column<>(columnName, Type.FLOAT);
        for (int i = 0; i < column.size(); i++) {
            if (column.isNaN(i)) {
                result.add(Type.NAN);
            } else if (column.isNOVALUE(i)) {
                result.add(literal);
            } else {
                result.add((double) column.get(i) + literal);
            }
        }

        return result;
    }

    @Override
    protected Column<Integer> operationInt(String columnName, Column<Integer> c1,
                                           Column<Integer> c2) {
        return c1.size() > c2.size()? sumInt(columnName, c1, c2) : sumInt(columnName, c2, c1);
    }

    @Override
    protected Column<Integer> operationInt(String columnName, Column<Integer> column, int literal) {
        Column<Integer> result = new Column<>(columnName, Type.INT);
        for (int i = 0; i < column.size(); i++) {
            if (column.isNaN(i)) {
                result.add(Type.NAN);
            } else if (column.isNOVALUE(i)) {
                result.add(literal);
            } else {
                result.add(column.get(i) + literal);
            }
        }

        return result;
    }

    private static Column<Double> sumFloat(String columnName, Column longerCol, Column shorterCol) {
        Column<Double> result = new Column<>(columnName, Type.FLOAT);
            for (int i = 0; i < longerCol.size(); i++) {
                if (longerCol.isNaN(i) || shorterCol.isNaN(i)) {
                    result.add(Type.NAN);
                } else if (longerCol.isNOVALUE(i) && shorterCol.isNOVALUE(i)) {
                    result.add(Type.NOVALUE);
                } else if(shorterCol.isNOVALUE(i)) {
                    result.add((double) longerCol.get(i));
                } else if (i < shorterCol.size()) {
                    if (longerCol.isNOVALUE(i)) {
                        result.add((double) shorterCol.get(i));
                    } else {
                        result.add((double) longerCol.get(i) + (double) shorterCol.get(i));
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

    private static Column<Integer> sumInt(String columnName, Column<Integer> longerCol,
                                          Column<Integer> shorterCol) {
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


    private static Column<String> concat(String columnName, Column<String> c1, Column<String> c2) {
        Column<String> result = new Column<>(columnName, c1.getType());
        if (c1.size() > c2.size()) {
            for (int i = 0; i < c1.size(); i++) {
                if (c1.isNOVALUE(i) && c2.isNOVALUE(i)) {
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
                if (c2.isNOVALUE(i) && c1.isNOVALUE(i)) {
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

    private static Column<String> concat(String columnName, Column<String> column, String literal) {
        Column<String> result = new Column<>(columnName, Type.STRING);
        for (int i = 0; i < column.size(); i++) {
            if (column.isNOVALUE(i)) {
                result.add(literal);
            } else {
                result.add(column.get(i) + literal);
            }
        }
        return result;
    }

}
