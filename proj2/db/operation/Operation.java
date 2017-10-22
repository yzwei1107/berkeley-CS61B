package db.operation;

import db.Column;

public interface Operation {
    Column operate(String columnName);
}
