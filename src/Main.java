import IO.SimpleCompressorOutputStream;
import IO.SimpleDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

public class Main {
    public static void main(String[] args) {
        MyMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(100, 100);
        maze.print();
        System.out.println();

        byte[] mazeInBytes = maze.toByteArray();
        SimpleCompressorOutputStream scos = new SimpleCompressorOutputStream();
        byte[] compressedMaze = scos.compress(mazeInBytes);
        System.out.println("Compressed maze size: " + compressedMaze.length);
        SimpleDecompressorInputStream sdis = new SimpleDecompressorInputStream();
        byte[] decompressedMaze = sdis.decompress(compressedMaze);
        Maze mazeAfterDecompression = new Maze(decompressedMaze);
        mazeAfterDecompression.print();



    }
}