package db.operation;

import db.Column;

/**
 * Interface for arithmetic operation classes.
 */

public interface Operation {
    Column operate(String columnName);
}
