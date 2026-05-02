package client.controllers;

import client.ClientConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class AddTransactionController {

    @FXML private TextField amountField;
    @FXML private ComboBox<String> typeBox;
    @FXML private ComboBox<String> categoryBox;
    @FXML private DatePicker datePicker;
    @FXML private Button submitBtn;
    @FXML private Label messageLabel;

    private ClientConnection connection;
    private String username;

    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private void initialize() {
        typeBox.getItems().addAll("EXPENSE", "INCOME");
        typeBox.setValue("EXPENSE");

        categoryBox.getItems().addAll("Food", "Transport", "Shopping", "Entertainment", "Bills", "Salary", "Other");
        categoryBox.setValue("Food");

        datePicker.setValue(LocalDate.now());

        // Swap category list when type changes
        typeBox.setOnAction(e -> {
            categoryBox.getItems().clear();
            if ("INCOME".equals(typeBox.getValue())) {
                categoryBox.getItems().addAll("Salary", "Freelance", "Bonus", "Investment", "Other");
                categoryBox.setValue("Salary");
            } else {
                categoryBox.getItems().addAll("Food", "Transport", "Shopping", "Entertainment", "Bills", "Other");
                categoryBox.setValue("Food");
            }
        });

        submitBtn.setOnAction(e -> handleAddTransaction());
    }

    private void handleAddTransaction() {
        String rawAmount = amountField.getText().trim();
        if (rawAmount.isEmpty()) { showError("Please enter an amount."); return; }

        double amount;
        try {
            amount = Double.parseDouble(rawAmount);
            if (amount <= 0) { showError("Amount must be greater than zero."); return; }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid numeric amount.");
            return;
        }

        String type     = typeBox.getValue();
        String category = categoryBox.getValue();
        String date     = datePicker.getValue() != null
                          ? datePicker.getValue().toString()
                          : LocalDate.now().toString();

        try {
            String response = connection.sendRequest(
                "ADD_TRANSACTION|" + username + "|" + amount + "|" + type + "|" + category + "|" + date
            );
            String[] parts = response.split("\\|");

            if (parts[0].equals("SUCCESS")) {
                showSuccess("✓ Transaction added successfully!");
                amountField.clear();
                datePicker.setValue(LocalDate.now());
            } else {
                showError("✗ " + (parts.length > 1 ? parts[1] : "Failed."));
            }
        } catch (Exception ex) {
            showError("✗ Error: " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #e53935;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #43a047;");
        messageLabel.setText(msg);
    }
}
