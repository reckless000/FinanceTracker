package client.controllers;

import client.ClientConnection;
import client.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel;

    private ClientConnection connection;
    private Stage primaryStage;

    public void setConnection(ClientConnection connection) { this.connection = connection; }
    public void setStage(Stage stage) { this.primaryStage = stage; }

    @FXML
    private void initialize() {
        registerBtn.setOnAction(e -> handleRegister());
        backBtn.setOnAction(e -> goBackToLogin());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm  = confirmField.getText();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Please fill in all fields."); return;
        }
        if (username.length() < 3) {
            showError("Username must be at least 3 characters."); return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters."); return;
        }
        if (!password.equals(confirm)) {
            showError("Passwords do not match."); return;
        }
        try {
            String response = connection.sendRequest("REGISTER|" + username + "|" + password);
            String[] parts = response.split("\\|");
            if (parts[0].equals("SUCCESS")) {
                showSuccess("✓ Account created! Redirecting...");
                goBackToLogin();
            } else {
                showError("✗ " + (parts.length > 1 ? parts[1] : "Registration failed."));
            }
        } catch (Exception ex) {
            showError("✗ Connection error: " + ex.getMessage());
        }
    }

    private void goBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setConnection(connection);
            ctrl.setStage(primaryStage);
            Scene scene = new Scene(root, 420, 500);
            if (MainApp.getCss() != null) scene.getStylesheets().add(MainApp.getCss());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Finance Tracker – Login");
            primaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg)   { messageLabel.setStyle("-fx-text-fill: #e53935;"); messageLabel.setText(msg); }
    private void showSuccess(String msg) { messageLabel.setStyle("-fx-text-fill: #43a047;"); messageLabel.setText(msg); }
}
