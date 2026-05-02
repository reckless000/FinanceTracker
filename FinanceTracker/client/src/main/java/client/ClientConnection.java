package client;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected = false;

    public void connect() throws Exception {
        socket = new Socket("localhost", 5000);
        socket.setSoTimeout(10000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        connected = true;
    }

    public boolean isConnected() {
        return connected;
    }

    public String sendRequest(String request) throws Exception {
        if (!connected) {
            throw new Exception("Not connected to server");
        }
        out.println(request);
        return in.readLine();
    }

    public void disconnect() throws Exception {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        connected = false;
    }
}