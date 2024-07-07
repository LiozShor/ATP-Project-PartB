package algorithms.mazeGenerators;

import java.io.Serializable;

public class Maze implements Serializable {
    private  int[][] maze;
    private Position start;
    private Position goal;

    public Maze(byte[] mazeInBytes) {
        if (mazeInBytes.length < 12) {
            System.err.println("Error: Insufficient data in mazeInBytes array.");
            return; // Exit constructor or handle error condition
        }

        int expectedLength = ((mazeInBytes[0] & 0xFF) << 8) | (mazeInBytes[1] & 0xFF);
        int expectedWidth = ((mazeInBytes[2] & 0xFF) << 8) | (mazeInBytes[3] & 0xFF);
        int startRow = ((mazeInBytes[4] & 0xFF) << 8) | (mazeInBytes[5] & 0xFF);
        int startCol = ((mazeInBytes[6] & 0xFF) << 8) | (mazeInBytes[7] & 0xFF);
        int goalRow = ((mazeInBytes[8] & 0xFF) << 8) | (mazeInBytes[9] & 0xFF);
        int goalCol = ((mazeInBytes[10] & 0xFF) << 8) | (mazeInBytes[11] & 0xFF);

        this.start = new Position(startRow, startCol);
        this.goal = new Position(goalRow, goalCol);
        this.maze = new int[expectedLength][expectedWidth];

        int index = 12;
        for (int i = 0; i < expectedLength; i++) {
            for (int j = 0; j < expectedWidth; j++) {
                if (index < mazeInBytes.length) {
                    maze[i][j] = mazeInBytes[index];
                    index++;
                } else {
                    return; // Exit constructor or handle error condition
                }
            }
        }
    }




    public Maze(int[][] maze, Position start, Position goal) {
        this.maze = maze;
        this.start = start;
        this.goal = goal;
    }
    public int[][] getMaze() {
        return maze;
    }

    public void setWall(int row, int column) {
        maze[row][column] = 1;
    }

    public Position getStartPosition() {
        return this.start;
    }
    public Position getGoalPosition() {
        return this.goal;
    }
    public  void print() {
        for (int i = 0; i < maze.length; i++) {
            System.out.print("{ ");
            for (int j = 0; j < maze[i].length; j++) {
                if (i == start.getRow() && j == start.getColumn()) {
                    System.out.print("\033[32mS\033[0m "); // Green
                    continue;
                }
                else if (i == goal.getRow() && j == goal.getColumn()) {
                    System.out.print("\033[31mE\033[0m "); // Red
                    continue;
                }
                else
                    System.out.print(maze[i][j] + " ");
            }
            System.out.println("} ");

        }

    }

    public byte[] toByteArray() {
        int rows = maze.length;
        int cols = maze[0].length;

        // Calculate total length needed for the byte array
        int totalLength = rows * cols + 12;
        byte[] mazeInBytes = new byte[totalLength];

        // Store dimensions (rows and columns)
        mazeInBytes[0] = (byte) (rows >> 8);
        mazeInBytes[1] = (byte) rows;
        mazeInBytes[2] = (byte) (cols >> 8);
        mazeInBytes[3] = (byte) cols;

        // Store start and goal positions
        storePosition(start, mazeInBytes, 4);
        storePosition(goal, mazeInBytes, 8);

        // Store maze data
        int index = 12;
        for (int[] row : maze) {
            for (int value : row) {
                mazeInBytes[index++] = (byte) value;
            }
        }

        return mazeInBytes;
    }

    private void storePosition(Position pos, byte[] mazeInBytes, int startIndex) {
        mazeInBytes[startIndex] = (byte) (pos.getRow() >> 8);
        mazeInBytes[startIndex + 1] = (byte) pos.getRow();
        mazeInBytes[startIndex + 2] = (byte) (pos.getColumn() >> 8);
        mazeInBytes[startIndex + 3] = (byte) pos.getColumn();
    }







}