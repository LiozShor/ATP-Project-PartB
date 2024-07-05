package algorithms.mazeGenerators;

public class Maze {
     private  int[][] maze;
     private Position start;
     private Position goal;


     public Maze(byte[] mazeInBytes) {
         int rows = mazeInBytes[0] * 256 + mazeInBytes[1];
         int cols = mazeInBytes[2] * 256 + mazeInBytes[3];
         int startRow = mazeInBytes[4] * 256 + mazeInBytes[5];
         int startCol = mazeInBytes[6] * 256 + mazeInBytes[7];
         int goalRow = mazeInBytes[8] * 256 + mazeInBytes[9];
         int goalCol = mazeInBytes[10] * 256 + mazeInBytes[11];
         this.start = new Position(startRow, startCol);
         this.goal = new Position(goalRow, goalCol);
         this.maze = new int[rows][cols];
         int index = 12;
         for (int i = 0; i < rows; i++) {
             for (int j = 0; j < cols; j++) {
                 maze[i][j] = mazeInBytes[index];
                 index++;
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

    public byte[] toByteArray()
    {
        int rows = maze.length;
        int cols = maze[0].length;
        byte[] mazeInBytes = new byte[rows*cols + 12];
        mazeInBytes[0] = (byte) (rows / 256);
        mazeInBytes[1] = (byte) (rows % 256);
        mazeInBytes[2] = (byte) (cols / 256);
        mazeInBytes[3] = (byte) (cols % 256);
        mazeInBytes[4] = (byte) (start.getRow() / 256);
        mazeInBytes[5] = (byte) (start.getRow() % 256);
        mazeInBytes[6] = (byte) (start.getColumn() / 256);
        mazeInBytes[7] = (byte) (start.getColumn() % 256);
        mazeInBytes[8] = (byte) (goal.getRow() / 256);
        mazeInBytes[9] = (byte) (goal.getRow() % 256);
        mazeInBytes[10] = (byte) (goal.getColumn() / 256);
        mazeInBytes[11] = (byte) (goal.getColumn() % 256);
        int index = 12;
        for (int[] ints : maze) {
            for (int j = 0; j < cols; j++) {
                mazeInBytes[index] = (byte) ints[j];
                index++;
            }
        }
        return mazeInBytes;
    }





}
