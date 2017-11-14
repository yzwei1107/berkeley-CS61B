package db.comparison;

import db.Column;

/**
 * Interface for comparison operation classes.
 */

public interface Comparison {
    boolean compare(Column column, String literal, int row);
    boolean compare(Column c1, Column c2, int row);
}
