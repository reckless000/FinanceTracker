package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.err.println("Error creating handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("📨 Received: " + request);

                String[] parts = request.split("\\|");

                if (parts.length < 1) {
                    out.println("ERROR|Invalid request format");
                    continue;
                }

                switch (parts[0]) {
                    case "LOGIN":
                        if (parts.length >= 3) {
                            handleLogin(parts[1], parts[2]);
                        } else {
                            out.println("ERROR|Missing username or password");
                        }
                        break;

                    case "REGISTER":
                        if (parts.length >= 3) {
                            handleRegister(parts[1], parts[2]);
                        } else {
                            out.println("ERROR|Missing username or password");
                        }
                        break;

                    case "ADD_TRANSACTION":
                        if (parts.length >= 6) {
                            try {
                                double amount = Double.parseDouble(parts[2]);
                                handleAddTransaction(parts[1], amount, parts[3], parts[4], parts[5]);
                            } catch (NumberFormatException e) {
                                out.println("ERROR|Invalid amount format");
                            }
                        } else {
                            out.println("ERROR|Missing transaction data");
                        }
                        break;

                    case "GET_TRANSACTIONS":
                        if (parts.length >= 2) {
                            handleGetTransactions(parts[1]);
                        } else {
                            out.println("ERROR|Missing username");
                        }
                        break;

                    case "GET_SUMMARY":
                        if (parts.length >= 2) {
                            handleGetSummary(parts[1]);
                        } else {
                            out.println("ERROR|Missing username");
                        }
                        break;

                    default:
                        out.println("ERROR|Unknown command: " + parts[0]);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠ Client disconnected: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin(String username, String password) {
        if (DataStore.login(username, password)) {
            out.println("SUCCESS|Login successful|" + username);
            System.out.println("✅ Login success for: " + username);
        } else {
            out.println("ERROR|Invalid username or password");
            System.out.println("❌ Login failed for: " + username);
        }
    }

    private void handleRegister(String username, String password) {
        if (DataStore.register(username, password)) {
            out.println("SUCCESS|Registration successful|" + username);
            System.out.println("✅ New user registered: " + username);
        } else {
            out.println("ERROR|Username already exists");
            System.out.println("❌ Registration failed for: " + username);
        }
    }

    private void handleAddTransaction(String username, double amount, String type, String category, String date) {
        if (DataStore.addTransaction(username, amount, type, category, date)) {
            out.println("SUCCESS|Transaction added successfully");
            System.out.println("💰 Transaction added for " + username);
        } else {
            out.println("ERROR|Failed to add transaction");
            System.out.println("❌ Failed to add transaction for: " + username);
        }
    }

    private void handleGetTransactions(String username) {
        String transactions = DataStore.getTransactions(username);
        // Send as DATA|transaction1|transaction2|transaction3
        out.println("DATA|" + transactions);
        System.out.println("📋 Sent transactions for: " + username);
    }

    private void handleGetSummary(String username) {
        String summary = DataStore.getSummary(username);
        out.println("SUMMARY|" + summary);
        System.out.println("📊 Sent summary for: " + username);
    }
}