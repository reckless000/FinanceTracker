package client.controllers;

import client.ClientConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SummaryController {

    @FXML private Button refreshBtn;
    @FXML private Label  incomeValue;
    @FXML private Label  expenseValue;
    @FXML private Label  balanceValue;
    @FXML private Label  messageLabel;

    private ClientConnection connection;
    private String username;

    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

    public void setUsername(String username) {
        this.username = username;
        loadSummary();
    }

    @FXML
    private void initialize() {
        incomeValue.setText("$0.00");
        expenseValue.setText("$0.00");
        balanceValue.setText("$0.00");
        messageLabel.setText("");
        refreshBtn.setOnAction(e -> loadSummary());
    }

    private void loadSummary() {
        if (connection == null || username == null) return;

        try {
            String response = connection.sendRequest("GET_SUMMARY|" + username);

            if (response == null || response.trim().isEmpty()) {
                setError("No response from server.");
                return;
            }

            String[] parts = response.split("\\|");

            if (parts[0].equals("SUMMARY") && parts.length >= 4) {
                double totalIncome  = Double.parseDouble(parts[1].trim());
                double totalExpense = Double.parseDouble(parts[2].trim());
                double balance      = Double.parseDouble(parts[3].trim());

                incomeValue.setText(String.format("$%.2f", totalIncome));
                expenseValue.setText(String.format("$%.2f", totalExpense));
                balanceValue.setText(String.format("$%.2f", balance));

                balanceValue.setStyle(balance >= 0
                        ? "-fx-text-fill: #43a047; -fx-font-weight: bold;"
                        : "-fx-text-fill: #e53935; -fx-font-weight: bold;");

                setSuccess("✓ Summary loaded.");
            } else {
                setError("Unexpected response: " + response);
            }

        } catch (Exception ex) {
            setError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void setSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #43a047;");
        messageLabel.setText(msg);
    }

    private void setError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #e53935;");
        messageLabel.setText(msg);
    }
}