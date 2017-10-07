package db;

import java.util.ArrayList;

 class Column<Item> {
    private ArrayList<Item> items;

    Column() {
        this.items = new ArrayList<>();
    }

    /* Adds the item to column */
    void add(Item item) {
        items.add(item);
    }

    /* Returns the item in the specified column */
    Item get(int row) {
        return items.get(row);
    }

    /* Returns number of items in column */
    int size() {
        return items.size();
    }


}
