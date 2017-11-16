package db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CommandParser {
    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command tableName.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");

    public static String eval(String command, Database db) {
        Matcher matcher;
        if ((matcher = CREATE_CMD.matcher(command)).matches()) {
            return createTable(matcher.group(1), db);
        } else if ((matcher = LOAD_CMD.matcher(command)).matches()) {
            return loadTable(matcher.group(1), db);
        } else if ((matcher = STORE_CMD.matcher(command)).matches()) {
            return storeTable(matcher.group(1), db);
        } else if ((matcher = DROP_CMD.matcher(command)).matches()) {
            return dropTable(matcher.group(1), db);
        } else if ((matcher = INSERT_CMD.matcher(command)).matches()) {
            return insertRow(matcher.group(1), db);
        } else if ((matcher = PRINT_CMD.matcher(command)).matches()) {
            return printTable(matcher.group(1), db);
        } else if ((matcher = SELECT_CMD.matcher(command)).matches()) {
            return select(matcher.group(1), db);
        } else {
            return "ERROR: Malformed query";
        }
    }

    private static String createTable(String command, Database db) {
        Matcher matcher;
        if ((matcher = CREATE_NEW.matcher(command)).matches()) {
            return createNewTable(matcher.group(1), matcher.group(2).split(COMMA), db);
        } else if ((matcher = CREATE_SEL.matcher(command)).matches()) {
            return createSelectedTable(matcher.group(1), matcher.group(2), matcher.group(3),
                    matcher.group(4), db);
        } else {
            return "ERROR: Malformed query";
        }
    }

    private static String createNewTable(String tableName, String[] cols, Database db) {
        ArrayList<String> colNames = new ArrayList<>();
        ArrayList<String> colTypes = new ArrayList<>();

        for (String col : cols) {
            String[] colParams = col.split("\\s+");

            if (colParams.length != 2) {
                return "ERROR: Columns are declared as follows: <column tableName> <type>";
            }

            if (!colParams[1].equals("float") && !colParams[1].equals("int")
                    && !colParams[1].equals("string")) {
                return "ERROR: Invalid column type specified. Expected float, int or string";
            }

            colNames.add(colParams[0]);
            colTypes.add(colParams[1]);
        }

        if (colNames.size() != colTypes.size()) {
            return "ERROR: Column tableNames and types don't match up";
        }

        return db.createNewTable(tableName, colNames, colTypes);
    }

    private static String createSelectedTable(String name, String exprs, String tableNames,
                                              String conds, Database db) {

        String[] conditions = null;
        if (conds != null) {
            conditions = conds.split(AND);
        }

        return db.createSelectedTable(name, exprs.split(COMMA),
                tableNames.split(COMMA), conditions);
    }

    private static String loadTable(String tableName, Database db) {
        String filename = tableName + ".tbl";
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            return "ERROR: Could not open " + filename;
        }

        if (lines.size() <= 1) {
            return "ERROR: Invalid .tbl file";
        }

        String[] cols = lines.get(0).split(COMMA);

        String ret = createNewTable(tableName, cols, db);
        if (!ret.equals("")) {
            return ret;
        }

        for (int i = 1; i < lines.size(); i++) {
            String[] row = lines.get(i).split(COMMA);
            ret = db.insertRow(tableName, row);
            if (!ret.equals("")) {
                db.dropTable(tableName);
                return ret;
            }
        }

        return "";
    }

    private static String insertRow(String expr, Database db) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed insert: " + expr;
        }

        String tableName = m.group(1);
        String[] row = m.group(2).split(COMMA);
        return db.insertRow(tableName, row);
    }

    private static String storeTable(String tableName, Database db) {
        return db.storeTable(tableName);
    }

    private static String dropTable(String tableName, Database db) {
        return db.dropTable(tableName);
    }

    private static String printTable(String tableName, Database db) {
        return db.printTable(tableName);
    }

    private static String select(String command, Database db) {
        Matcher matcher = SELECT_CLS.matcher(command);
        if (!matcher.matches()) {
            return "ERROR: Malformed select: " + command;
        }

        String[] expressions = matcher.group(1).split(COMMA);
        String[] tableNames = matcher.group(2).split(COMMA);
        String[] conditions = null;

        if (matcher.group(3) != null) {
            conditions = matcher.group(3).split(AND);
        }

        return db.select(expressions, tableNames, conditions);
    }

}

