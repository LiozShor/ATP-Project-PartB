package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerStrategySolveSearchProblem implements IServerStrategy {
    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            Maze maze = (Maze) fromClient.readObject();
            String mazeHash = String.valueOf(maze.hashCode());
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");
            Path solutionFilePath = Paths.get(tempDirectoryPath, mazeHash);
            Solution solution;
            if (Files.exists(solutionFilePath)) {   //finds if their is a solution file
                ObjectInputStream solutionReader = new ObjectInputStream(new FileInputStream(solutionFilePath.toFile()));
                solution = (Solution) solutionReader.readObject();
                solutionReader.close();
            } else {  //solves and saves the solution
                SearchableMaze searchableMaze = new SearchableMaze(maze);
                ISearchingAlgorithm searcher = null;
                Configurations configurations = Configurations.getInstance();
                String mazeSearchingAlgorithm = configurations.getMazeSearchingAlgorithm();
                switch (mazeSearchingAlgorithm) {
                    case "BestFirstSearch" -> searcher = new BestFirstSearch();
                    case "BreadthFirstSearch" -> searcher = new BreadthFirstSearch();
                    case "DepthFirstSearch" -> searcher = new DepthFirstSearch();
                }
                if (searcher == null) {
                    throw new Exception("Maze searching algorithm not found");
                }
                solution = searcher.solve(searchableMaze);

                ObjectOutputStream solutionWriter = new ObjectOutputStream(new FileOutputStream(solutionFilePath.toFile()));
                solutionWriter.writeObject(solution);
                solutionWriter.close();
            }

            toClient.writeObject(solution);
            fromClient.close();
            toClient.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
