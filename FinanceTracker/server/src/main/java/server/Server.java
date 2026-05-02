package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║   Finance Tracker Server Started   ║");
            System.out.println("║        Listening on port 5000      ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("✓ New client connected from: " + socket.getInetAddress());
                ClientHandler handler = new ClientHandler(socket);
                new Thread(handler).start();
            }

        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}