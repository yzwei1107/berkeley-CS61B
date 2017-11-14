package db.operation;

import db.Column;

/**
 * Interface for arithmetic operation classes.
 */

public interface Operation {
    Column operation(String columnName, Column column, String literal);
    Column operation(String columnName, Column c1, Column c2);
}
