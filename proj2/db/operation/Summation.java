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
        String floatPattern = "-?(\\d*\\.\\d+|\\d+\\.\\d*)";
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
                return operationInt(columnName, c1, c2);
            } else {
                return operationFloat(columnName, c1, c2);
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
        Column<Double> result = new Column<>(columnName, Type.FLOAT);
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNaN(i) || c2.isNaN(i)) {
                result.add(Type.NAN);
            } else if (c1.isNOVALUE(i) && c2.isNOVALUE(i)) {
                result.add(Type.NOVALUE);
            } else {

                Number c1Val = c1.isNOVALUE(i) ? 0 : (Number) c1.get(i);
                Number c2Val = c2.isNOVALUE(i) ? 0 : (Number) c2.get(i);

                result.add(c1Val.doubleValue() + c2Val.doubleValue());
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
                result.add(literal);
            } else {
                result.add(((Number) column.get(i)).doubleValue() + literal);
            }
        }

        return result;
    }

    @Override
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

                result.add(c1Val + c2Val);
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
                result.add(literal);
            } else {
                result.add(column.get(i) + literal);
            }
        }

        return result;
    }

    private static Column<String> concat(String columnName, Column<String> c1, Column<String> c2) {
        Column<String> result = new Column<>(columnName, Type.STRING);
        for (int i = 0; i < c1.size(); i++) {
            if (c1.isNaN(i) || c2.isNaN(i)) {
                result.add(Type.NAN);
            } else if (c1.isNOVALUE(i) && c2.isNOVALUE(i)) {
                result.add(Type.NOVALUE);
            } else {
                String c1Val = c1.isNOVALUE(i) ? "" : c1.get(i);
                String c2Val = c2.isNOVALUE(i) ? "" : c2.get(i);

                result.add(c1Val + c2Val);
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
