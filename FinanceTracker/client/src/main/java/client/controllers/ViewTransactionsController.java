package client.controllers;

import client.ClientConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ViewTransactionsController {

    @FXML private Button refreshBtn;
    @FXML private ListView<String> transactionList;
    @FXML private Label messageLabel;

    private ClientConnection connection;
    private String username;

    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

    /** Called by DashboardController after injection — triggers first load. */
    public void setUsername(String username) {
        this.username = username;
        loadTransactions();
    }

    @FXML
    private void initialize() {
        refreshBtn.setOnAction(e -> loadTransactions());
    }

    private void loadTransactions() {
        if (connection == null || username == null) return;

        transactionList.getItems().setAll("⏳ Loading transactions...");

        try {
            String response = connection.sendRequest("GET_TRANSACTIONS|" + username);

            if (!response.startsWith("DATA|")) {
                setError("Unexpected server response.");
                return;
            }

            String dataPart = response.substring(5);

            if (dataPart.equals("EMPTY") || dataPart.isEmpty()) {
                transactionList.getItems().setAll(
                    "📭 No transactions yet.",
                    "Use the 'Add Transaction' tab to get started!"
                );
                setWarning("No transactions found.");
                return;
            }

            String[] fields = dataPart.split("\\|");
            int numTransactions = fields.length / 4;
            transactionList.getItems().clear();
            int count = 0;

            for (int i = 0; i < numTransactions; i++) {
                int base = i * 4;
                try {
                    double amount   = Double.parseDouble(fields[base]);
                    String type     = fields[base + 1];
                    String category = fields[base + 2];
                    String date     = fields[base + 3];

                    String icon    = "INCOME".equals(type) ? "💰" : "💸";
                    String display = String.format("%s  %s   |   %s: $%.2f   |   %s",
                                                   icon, date, type, amount, category);
                    transactionList.getItems().add(display);
                    count++;
                } catch (NumberFormatException ignored) { }
            }

            if (count == 0) {
                transactionList.getItems().setAll("⚠ No valid transactions found.");
                setWarning("No valid transactions.");
            } else {
                setSuccess(String.format("✓ Loaded %d transaction%s.", count, count == 1 ? "" : "s"));
            }

        } catch (Exception ex) {
            transactionList.getItems().setAll("❌ Error: " + ex.getMessage());
            setError("Error: " + ex.getMessage());
        }
    }

    private void setSuccess(String msg) { messageLabel.setStyle("-fx-text-fill: #43a047;"); messageLabel.setText(msg); }
    private void setWarning(String msg) { messageLabel.setStyle("-fx-text-fill: #FB8C00;"); messageLabel.setText(msg); }
    private void setError(String msg)   { messageLabel.setStyle("-fx-text-fill: #e53935;"); messageLabel.setText(msg); }
}
