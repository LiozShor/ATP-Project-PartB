package Client;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress serverIP;
    private int serverPort;
    private IClientStrategy strategy;

    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }

    public void communicateWithServer(){
        try(Socket serverSocket = new Socket(serverIP, serverPort)){
            System.out.println("connected to server - IP = " + serverIP + ", Port = " + serverPort);
            strategy.clientStrategy(serverSocket.getInputStream(), serverSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try {
//            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
//                @Override
//                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
//                    try {
//                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
//                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
//                        toServer.flush();
//                        int[] mazeDimensions = new int[]{50, 50};
//                        toServer.writeObject(mazeDimensions); // send maze dimensions to server
//                        toServer.flush();
//                        byte[] compressedMaze = (byte[]) fromServer.readObject(); // read generated maze (compressed with MyCompressor) from server
//                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
//                        byte[] decompressedMaze = new byte[1000]; // allocate byte[] for the decompressed maze
//                        is.read(decompressedMaze); // Fill decompressedMaze with bytes
//                        Maze maze = new Maze(decompressedMaze);
//                        maze.print();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

//                @Override
//                public void applyStrategy(InputStream inFromServer, OutputStream outToServer) {
//                    try {
//                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
//                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
//                        toServer.flush();
//                        int[] mazeDimensions = new int[]{50, 50};
//                        toServer.writeObject(mazeDimensions); // send maze dimensions to server
//                        toServer.flush();
//                        byte[] compressedMaze = (byte[]) fromServer.readObject(); // read generated maze (compressed with MyCompressor) from server
//                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
//                        byte[] decompressedMaze = new byte[1000]; // allocate byte[] for the decompressed maze
//                        is.read(decompressedMaze); // Fill decompressedMaze with bytes
//                        Maze maze = new Maze(decompressedMaze);
//                        maze.print();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            client.communicateWithServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
