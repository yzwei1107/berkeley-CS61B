package db;

import java.util.ArrayList;
import java.util.HashSet;

public class Column<Item> {
    private ArrayList<Item> items;
    private String name;
    private Type type;
    private HashSet<Integer> noValueSet;
    private HashSet<Integer> nanSet;

    public Column(String name, Type type) {
        this.items = new ArrayList<>();
        this.name = name;
        this.type = type;
        this.noValueSet = new HashSet<>();
        this.nanSet = new HashSet<>();
    }

    public Column(Column<Item> otherCol) {
        this.items = new ArrayList<>();
        this.name = otherCol.name;
        this.type = otherCol.type;
        this.noValueSet = new HashSet<>();
        this.nanSet = new HashSet<>();

        for (int i = 0; i < otherCol.size(); i++) {
            if (otherCol.isNOVALUE(i)) {
                this.add(Type.NOVALUE);
            } else if (otherCol.isNaN(i)) {
                this.add(Type.NAN);
            } else {
                this.add(otherCol.items.get(i));
            }
        }
    }

    /* Adds the item to column */
    public void add(Item item) {
        items.add(item);
    }

    public void remove(int index) {
        items.remove(index);
    }

    /* Adds NaN or NOVALUE to column */
    public void add(Type type) {
        items.add(null);
        if (type == Type.NOVALUE) {
            noValueSet.add(items.size() - 1);
        } else if (type == Type.NAN) {
            nanSet.add(items.size() - 1);
        } else {
            throw new IllegalArgumentException("Type must be NOVALUE OR NaN");
        }
    }

    public boolean isNOVALUE(int row) {
        return noValueSet.contains(row);
    }


    public boolean isNaN(int row) {
        return nanSet.contains(row);
    }

    /* Returns the item in the specified column */
    public Item get(int row) {
        return items.get(row);
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getItemString(int row){
        if (isNaN(row)) {
            return "NaN";
        } else if (isNOVALUE(row)) {
            return "NOVALUE";
        } else if (getType() == Type.STRING) {
            return "'" + items.get(row).toString() + "'";
        } else if (getType() == Type.FLOAT) {
            return String.format("%.3f", items.get(row));
        } else {
            return items.get(row).toString();
        }
    }

    /* Returns number of items in column */
    public int size() {
        return items.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name).append(" ").append(type.toString().toLowerCase()).append("\n");

        for (int i = 0; i < items.size(); i++) {
            sb.append(getItemString(i)).append("\n");
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    /* Returns true if column is empty, false otherwise. */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
