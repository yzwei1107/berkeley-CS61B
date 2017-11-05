package db.comparison;

import db.Column;

/**
 * Interface for comparison operation classes.
 */

public interface Comparison {
    boolean compare(Column column, String literal);
    boolean compare(Column c1, Column c2);
}
