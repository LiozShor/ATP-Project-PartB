import IO.MyCompressorOutputStream;
import IO.SimpleCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {

        Maze maze = new MyMazeGenerator().generate(50, 50);
        maze.print();
        byte[] mazeBytes = maze.toByteArray();
        byte[] compressed = new byte[mazeBytes.length];
        SimpleCompressorOutputStream compressor = new SimpleCompressorOutputStream(System.out);
        compressor.compress(compressed);
        MyCompressorOutputStream myCompressor = new MyCompressorOutputStream(System.out);
        myCompressor.write(mazeBytes);
        System.out.println(Arrays.toString(mazeBytes));
        System.out.println(Arrays.toString(compressed));


    }
}