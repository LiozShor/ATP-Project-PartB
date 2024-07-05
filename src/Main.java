import IO.SimpleCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

public class Main {
    public static void main(String[] args) {
        MyMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(10, 10);
        maze.print();
        SimpleCompressorOutputStream scos = new SimpleCompressorOutputStream(maze.toByteArray());
        byte[] compressed = scos.compress(maze.toByteArray());
        System.out.println("Compressed size: " + compressed.length);
        //print the compressed maze
        for (int i = 0; i < compressed.length; i++) {
            System.out.print(compressed[i] + " ");
        }
//        Maze decompressed = new Maze(compressed, maze.getStartPosition(), maze.getGoalPosition());

    }
}