package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.io.Serializable;

public class MazeState extends AState implements Serializable {
    private int row;
    private int col;
    private int cost;
    private MazeState parent;
    private static final long serialVersionUID = 1857140346937818910L;



    public MazeState() {
        super(0, 0, null);
        this.row = 0;
        this.col = 0;
        this.cost = 0;
        this.parent = null;
    }

    public MazeState(Position pos, MazeState parent) {
        super(pos.getRow(), pos.getColumn(),  parent);
        this.row = pos.getRow();
        this.col = pos.getColumn();
    }

    public MazeState(int row, int col, MazeState state,int cost) {
        super(row, col, state);
        this.row = row;
        this.col = col;
        this.cost = cost;

    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public MazeState getParent() {
        return parent;
    }

    @Override
    public AState getState() {
        return this;
    }

    @Override
    public String toString() {
        return "{" + row + "," + col + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MazeState) {
            MazeState other = (MazeState) obj;
            return row == other.row && col == other.col;
        }
        return false;
    }

    public void setParent(MazeState parent) {
        this.parent = parent;
    }

    public Position getPosition() {
        return new Position(row, col);
    }

    @Override
    public int getCost() {
        return 0;
    }
}
