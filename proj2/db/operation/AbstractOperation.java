package db.operation;

import db.Column;

abstract class AbstractOperation implements Operation {
    protected Column c1;
    protected Column c2;


    public AbstractOperation(Column c1, Column c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    abstract public Column operate(String columnName);
}
