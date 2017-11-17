package db;

import db.comparison.Comparison;
import db.comparison.Equals;
import db.comparison.NotEquals;
import db.comparison.GreaterThan;
import db.comparison.LessThan;
import db.comparison.GreaterOrEqualTo;
import db.comparison.LessOrEqualTo;
import db.operation.Operation;
import db.operation.Summation;
import db.operation.Subtraction;
import db.operation.Multiplication;
import db.operation.Division;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {

    private final String FLOAT_PATTERN = "([0-9].*)\\.([0-9].*)";
    private final String INT_PATTERN = "\\d+";
    private final String STRING_PATTERN = "'.*'";

    private HashMap<String, Table> tableMap;
    private Table selectedTable = null;

    public Database() {
        tableMap = new HashMap<>();
    }

    public String transact(String query) {
        return CommandParser.eval(query, this);
    }

    public String createNewTable(String name, List<String> colNames, List<String> colTypes) {
        if (tableMap.containsKey(name)) {
            return "ERROR: Table with the name " + name + " already exists.";
        }

        Table table = new Table(name);

        for (int i = 0; i < colNames.size(); i++) {
            Type colType;
            switch (colTypes.get(i)) {
                case "float":
                    colType = Type.FLOAT;
                    break;
                case "int":
                    colType = Type.INT;
                    break;
                default:
                    colType = Type.STRING;
                    break;
            }

            table.addColumn(colNames.get(i), colType);
        }

        tableMap.put(name, table);

        return "";
    }

    public String createSelectedTable(String name, String[] exprs,
                                      String[] tableNames, String[] conds) {
        String retVal = select(exprs, tableNames, conds);
        if (retVal.substring(0, 6).equals("ERROR:")) {
            return retVal;
        }
        selectedTable.setName(name);
        tableMap.put(name, selectedTable);
        return "";
    }

    /* Inserts row in table of provided name */
    public String insertRow(String tableName, String[] rowArray) {
        if (!tableMap.containsKey(tableName)) {
            return "ERROR: Table " + tableName + " could not be found";
        }

        Table table = tableMap.get(tableName);
        ArrayList<String> rowList = new ArrayList<>();

        if (rowArray.length != table.getNumColumns()) {
            return "ERROR: Row size different from number of columns in table.";
        }

        for (int i = 0; i < rowArray.length; i++) {
            boolean isNOVALUE = rowArray[i].equals("NOVALUE");
            switch (table.getColumnType(i)) {
                case FLOAT:
                    String floatPattern = "-?(\\d*\\.\\d+|\\d+.\\d*)";
                    boolean literalIsFloat = Pattern.matches(floatPattern, rowArray[i]);

                    if (!isNOVALUE && !literalIsFloat) {
                        return "ERROR: Could not find float at index " + i + " of the row.";
                    }

                    break;

                case INT:
                    String intPattern = "-?\\d+";
                    boolean literalIsInt = Pattern.matches(intPattern, rowArray[i]);

                    if (!isNOVALUE && !literalIsInt) {
                        return "ERROR: Could not find int at index " + i + " of the row.";
                    }

                    break;

                case STRING:
                    String stringPattern = "'.+'";
                    boolean literalIsString = Pattern.matches(stringPattern, rowArray[i]);
                    if (!isNOVALUE && !literalIsString) {
                        return "ERROR: Could not find string at index " + i + " of the row.";
                    }

                    if (!isNOVALUE) {
                        // Remove single quotes from string.
                        rowArray[i] = rowArray[i].substring(1, rowArray[i].length() - 1);
                    }

                    break;

                default:
                    throw new IllegalArgumentException("Illegal column type");
            }

            rowList.add(rowArray[i]);
        }

        table.addRow(rowList);
        return "";
    }

    public String storeTable(String tableName) throws FileNotFoundException {
        if (!tableMap.containsKey(tableName)) {
            return "ERROR: Table " + tableName + " could not be found";
        }

        String filename = tableName + ".tbl";
        PrintWriter out = new PrintWriter(filename);

        out.print(tableMap.get(tableName).toString());
        out.close();

        return "";
    }

    public String dropTable(String tableName) {
        if (!tableMap.containsKey(tableName)) {
            return "ERROR: Table " + tableName + " could not be found";
        }

        tableMap.remove(tableName);
        return "";
    }

    public String printTable(String tableName) {
        if (!tableMap.containsKey(tableName)) {
            return "ERROR: Table " + tableName + " could not be found";
        }

        return tableMap.get(tableName).toString();
    }

    public String select(String[] expressions, String[] tableNames, String[] conditions) {
        for (String name : tableNames) {
            if (!tableMap.containsKey(name))  {
                return "ERROR: Table " + name + " could not be found";
            }
        }

        Table result;
        if (expressions.length == 1 && expressions[0].equals("*")) {
            result = join(tableNames);
        } else {
            result = new Table("");
            Table joined = join(tableNames);
            String retVal = evaluateColumnExpressions(result, joined, expressions);

            if (!retVal.equals("")) {
                return retVal;
            }

        }

        if (conditions != null) {
            String retVal = evaluateTableConditions(result, conditions);

            if (!retVal.equals("")) {
                return retVal;
            }
        }

        selectedTable = result;
        return result.toString();
    }

    private String evaluateColumnExpressions(Table table, Table joined, String[] expressions) {
        for (String colExpr : expressions) {
            String[] expr = colExpr.split("\\s+");

            if (expr.length == 1) {
                if (joined.containsColumn(expr[0])) {
                    table.addColumnFromTable(joined, expr[0]);
                } else {
                    return "ERROR: Could not find " + expr[0] + " in join of provided table names.";
                }
            } else if (expr.length == 5 && expr[3].equals("as")
                    || expr.length == 3 && expr[1].equals("as")) {
                String retVal = evaluateArithmeticExpression(table, joined,
                        colExpr.split("\\s+as\\s+"));

                if (!retVal.equals("")) {
                    return retVal;
                }
            } else {
                return "ERROR: " + colExpr + " is not a valid column expression.";
            }
        }
        return "";
    }

    private String evaluateArithmeticExpression(Table table, Table joined,
                                                String[] arithmeticExpr) {
        String firstOperand;
        String operator;
        String secondOperand;

        final Pattern operationPattern = Pattern.compile("(\\w+)\\s*([+\\-*/])\\s*(\\w+)");
        Matcher matcher;

        if ((matcher = operationPattern.matcher(arithmeticExpr[0])).matches()) {
            firstOperand = matcher.group(1);
            operator = matcher.group(2);
            secondOperand = matcher.group(3);
        } else {
            String arrayString = Arrays.toString(arithmeticExpr).replace(",", "");
            String expr = arrayString.substring(1, arrayString.length() - 1);
            return "ERROR: " + expr + " is an invalid column expression.";
        }

        if (!joined.containsColumn(firstOperand)) {
            return "ERROR: " + firstOperand + " is not in the table.";
        }

        boolean literalsFloat = Pattern.matches(FLOAT_PATTERN, secondOperand);
        boolean literalsInt = Pattern.matches(INT_PATTERN, secondOperand);
        boolean literalsString = Pattern.matches(STRING_PATTERN, secondOperand);
        if (!joined.containsColumn(secondOperand)
                && !literalsFloat && !literalsInt && !literalsString) {
            return "ERROR: Cannot process " + secondOperand;
        }

        boolean oneOperandIsOfTypeString = joined.getColumnType(firstOperand) == Type.STRING;
        boolean bothOperandsAreStrings = joined.getColumnType(firstOperand) == Type.STRING;
        if (joined.containsColumn(secondOperand)) {
            oneOperandIsOfTypeString  |= joined.getColumnType(secondOperand) == Type.STRING;
            bothOperandsAreStrings &= joined.getColumnType(secondOperand) == Type.STRING;
        } else {
            oneOperandIsOfTypeString |= literalsString;
            bothOperandsAreStrings &= literalsString;
        }

        if (!operator.equals("+") && bothOperandsAreStrings) {
            return "ERROR: Only + operator can be used on strings";
        } else if (!bothOperandsAreStrings && oneOperandIsOfTypeString) {
            return "ERROR: String and int or float operand in arithmetic expression";
        }

        Operation operation = getArithmeticOperator(operator);

        if (operation == null) {
            return "ERROR: Invalid arithmetic operator " + operator;
        }

        table.evaluateArithmeticExpression(joined, operation, firstOperand, secondOperand,
                arithmeticExpr[1]);
        return "";
    }

    private String evaluateTableConditions(Table table, String[] conditions) {
        for (String cond : conditions) {
            // Split by spaces that are not between single quotes.
            String[] condition = cond.split("\\s+(?=([^']*'[^']*')*[^']*$)");

            if (condition.length != 3) {
                return "ERROR: Malformed conditions.";
            }

            if (!table.containsColumn(condition[0])) {
                return "ERROR: Table does not contain the column " + condition[0];
            }

            Comparison comparison = getComparisonOperator(condition[1]);
            if (comparison == null) {
                return "ERROR: Unknown operator " + condition[1] + "processed.";
            }

            boolean literalsFloat = Pattern.matches(FLOAT_PATTERN, condition[2]);
            boolean literalsInt = Pattern.matches(INT_PATTERN, condition[2]);
            boolean literalsString = Pattern.matches(STRING_PATTERN, condition[2]);

            if (!table.containsColumn(condition[2]) && !literalsFloat && !literalsInt
                    && !literalsString) {
                return "ERROR: Cannot process conditional parameter " + condition[2];
            } else if (literalsString) {
                condition[2] = condition[2].substring(1, condition[2].length() - 1);
            }

            table.makeTableCompliant(comparison, condition);
        }
        return "";
    }

    private static Comparison getComparisonOperator(String operator) {
        switch (operator) {
            case "==":
                return new Equals();
            case "!=":
                return new NotEquals();
            case "<":
                return new LessThan();
            case ">":
                return new GreaterThan();
            case "<=":
                return new LessOrEqualTo();
            case ">=":
                return new GreaterOrEqualTo();
            default:
                return null;
        }
    }

    private static Operation getArithmeticOperator(String operator) {
        switch (operator) {
            case "+":
                return new Summation();
            case "-":
                return new Subtraction();
            case "*":
                return new Multiplication();
            case "/":
                return new Division();
            default:
                return null;
        }
    }

    private Table join(String[] tableNames) {
        LinkedList<Table> tableList = new LinkedList<>();
        for (String name : tableNames) {
            tableList.add(tableMap.get(name));
        }

        return Table.join("", tableList);
    }

}
