package algorithms.mazeGenerators;

import java.io.Serializable;

public class Position implements Serializable{
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return "{" + row + "," + column + "}";
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
