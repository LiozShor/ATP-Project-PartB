package Server;

import algorithms.mazeGenerators.*;
import IO.MyCompressorOutputStream;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try (ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
             ObjectOutputStream toClient = new ObjectOutputStream(outToClient)) {

            int[] mazeDimensions = (int[]) fromClient.readObject();
            int rows = mazeDimensions[0];
            int cols = mazeDimensions[1];


            Configurations configurations = Configurations.getInstance();
            String mazeGeneratingAlgorithm = configurations.getMazeGeneratingAlgorithm();
            IMazeGenerator mazeGenerator = null;
            switch (mazeGeneratingAlgorithm) {
                case "SimpleMazeGenerator" -> mazeGenerator = new SimpleMazeGenerator();
                case "MyMazeGenerator" -> mazeGenerator = new MyMazeGenerator();
                case "EmptyMazeGenerator" -> mazeGenerator = new EmptyMazeGenerator();
            }

            if (mazeGenerator == null) {
                throw new Exception("Maze generating algorithm not found");
            }
            Maze maze = mazeGenerator.generate(rows, cols);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (MyCompressorOutputStream compressorOutputStream = new MyCompressorOutputStream(byteArrayOutputStream)) {
                compressorOutputStream.write(maze.toByteArray());
                compressorOutputStream.flush();
            }

            byte[] compressedMaze = byteArrayOutputStream.toByteArray();
            toClient.writeObject(compressedMaze);
            toClient.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
