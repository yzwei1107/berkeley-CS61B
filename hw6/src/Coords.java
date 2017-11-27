public class Coords {
    int row;
    int column;

    Coords(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Coords oCoords = (Coords) o;
        if (row == oCoords.row && column == oCoords.column) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 31 * row + column;
    }
}
