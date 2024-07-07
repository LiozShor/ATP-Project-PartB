package test;

import Client.*;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MainTest {

    private static final String resultsFilePath = "results.txt";
    private static final String logFilePath = "results.log";
    private static int Port_ServerMazeGenerating = getRandomNumber(5000, 6000);
    private static int Port_ServerSearchProblemSolver = getRandomNumber(6001, 7000);
    private static int total_test = 0;
    private static int total_pass = 0;
    private static final int ServerListeningIntervalMS = 100000;

    public static void main(String[] args) {
        try {
            Test_CompressDecompressMaze();
            Test_CommunicateWithServers();
        } catch (Exception e) {
            System.out.println("Error during tests execution: " + e.getMessage());
        }
    }

    //<editor-fold desc="Utility Methods">
    private static int getRandomNumber(int from, int to) {
        if (from < to)
            return from + new Random().nextInt(Math.abs(to - from));
        return from - new Random().nextInt(Math.abs(to - from));
    }

    private static void appendToFile(String text, String filePath) {
        try (java.io.FileWriter fw = new java.io.FileWriter(filePath, true)) {
            fw.write(text + "\r\n");
        } catch (IOException ex) {
            System.out.println(String.format("Error appending text to file: %s", filePath));
        }
    }

    private static void appendToResultsFile(String text) {
        appendToFile(text, resultsFilePath);
    }

    private static void appendToLogFile(String text) {
        appendToFile(text, logFilePath);
    }

    private static void appendToResultsAndLogFiles(String text) {
        appendToResultsFile(text);
        appendToLogFile(text);
    }

    private static void appendExceptionToFile(Exception e, String filePath) {
        String message = e.getMessage();
        if (message == null) {
            message = String.valueOf(e);
        }
        appendToFile(String.format("Exception: %s", message), filePath);
        if (e.getStackTrace().length > 1) {
            String msg = String.valueOf(e.getStackTrace()[0]);
            appendToFile(String.format("Exception Stack Trace: %s", msg), filePath);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Test_CompressDecompressMaze">
    private static void Test_CompressDecompressMaze() {
        double averageCompressionRate = 0;
        int size = 5;
        String mazeFileName = "savedMaze.maze";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
        Maze maze = mazeGenerator.generate(size, size); //Generate new maze
        double mazeOriginalSize = maze.toByteArray().length;

        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
            System.out.println("Maze successfully compressed and saved.");
        } catch (IOException e) {
            appendToResultsFile(String.valueOf(total_test));
            System.err.println("Error during compression: " + e.getMessage());
        }

        byte[] savedMazeBytes = new byte[0];
        try {
            InputStream in = new MyDecompressorInputStream(new FileInputStream(mazeFileName));
            savedMazeBytes = new byte[maze.toByteArray().length];
            in.read(savedMazeBytes);
            in.close();
            System.out.println("Maze successfully decompressed.");
        } catch (IOException e) {
            System.err.println("Error during decompression: " + e.getMessage());
        }

        File compressed = new File(mazeFileName);
        double current_comp = compressed.length();
        Maze loadedMaze = new Maze(savedMazeBytes);
        boolean areMazesEquals = Arrays.equals(loadedMaze.toByteArray(), maze.toByteArray());
        if (areMazesEquals) {
            total_pass++;
            System.out.println("Compression/Decompression test passed.");
        } else {
            appendToResultsFile(String.valueOf(total_test));
            System.err.println("Compression/Decompression test failed.");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Test_CommunicateWithServers">
    private static void Test_CommunicateWithServers() {
        System.out.println("Starting servers...");
        int counter = 0;
        Port_ServerMazeGenerating = getRandomNumber(5000, 6000);
        Port_ServerSearchProblemSolver = getRandomNumber(6001, 7000);
        Server mazeGeneratingServer = new Server(Port_ServerMazeGenerating, 1000, new ServerStrategyGenerateMaze());
        Server solveSearchProblemServer = new Server(Port_ServerSearchProblemSolver, 1000, new ServerStrategySolveSearchProblem());

        solveSearchProblemServer.start();
        mazeGeneratingServer.start();

        System.out.println("Communicating with maze generating server...");
        CommunicateWithServer_MazeGenerating(counter);

        System.out.println("Communicating with search problem solver server...");
        CommunicateWithServer_SolveSearchProblem(counter);

        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
        System.out.println("Servers stopped.");
    }

    private static void CommunicateWithServer_MazeGenerating(int i) {
        AtomicInteger testsPassed = new AtomicInteger(0);
        try {
            new Client(InetAddress.getLocalHost(), Port_ServerMazeGenerating, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        total_test++;
                        int size = (int) (50 * (i+1));
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{size, size};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[1000000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        if (maze.toByteArray().length > 1000) {
                            testsPassed.incrementAndGet();
                            System.out.println("Maze generation test passed.");
                        } else {
                            appendToResultsFile(String.valueOf(total_test));
                            System.err.println("Maze generation test failed.");
                        }
                    } catch (Exception e) {
                        appendToResultsFile(String.valueOf(total_test));
                        System.err.println("Error during maze generation: " + e.getMessage());
                    }
                }
            }).communicateWithServer();
        } catch (Exception e) {
            System.err.println("Error communicating with maze generating server: " + e.getMessage());
        }
        total_pass += testsPassed.get();
    }

    private static void CommunicateWithServer_SolveSearchProblem(int i) {
        AtomicInteger testsPassed = new AtomicInteger(0);
        try {
            new Client(InetAddress.getLocalHost(), Port_ServerSearchProblemSolver, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        total_test++;
                        int size = (int) (50 * (i+1));
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(size, size);
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read solution from server
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        if (!mazeSolutionSteps.isEmpty()) {
                            testsPassed.incrementAndGet();
                            System.out.println("Maze solving test passed.");
                        } else {
                            appendToResultsFile(String.valueOf(total_test));
                            System.err.println("Maze solving test failed.");
                        }
                    } catch (Exception e) {
                        appendToResultsFile(String.valueOf(total_test));
                        System.err.println("Error during maze solving: " + e.getMessage());
                    }
                }
            }).communicateWithServer();
        } catch (Exception e) {
            System.err.println("Error communicating with search problem solver server: " + e.getMessage());
        }
        total_pass += testsPassed.get();
    }
    //</editor-fold>
}
