package client.controllers;

import client.ClientConnection;
import client.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label messageLabel;

    private ClientConnection connection;
    private Stage primaryStage;

    public void setConnection(ClientConnection connection) { this.connection = connection; }
    public void setStage(Stage stage) { this.primaryStage = stage; }

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> navigateToRegister());
        passwordField.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }
        try {
            String response = connection.sendRequest("LOGIN|" + username + "|" + password);
            String[] parts = response.split("\\|");
            if (parts[0].equals("SUCCESS")) {
                showSuccess("✓ Login successful!");
                navigateToDashboard(username);
            } else {
                showError("✗ " + (parts.length > 1 ? parts[1] : "Login failed."));
            }
        } catch (Exception ex) {
            showError("✗ Connection error: " + ex.getMessage());
        }
    }

    private void navigateToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            RegisterController ctrl = loader.getController();
            ctrl.setConnection(connection);
            ctrl.setStage(primaryStage);
            Scene scene = new Scene(root, 420, 560);
            if (MainApp.getCss() != null) scene.getStylesheets().add(MainApp.getCss());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Finance Tracker – Register");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to open registration screen.");
        }
    }

    private void navigateToDashboard(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            DashboardController ctrl = loader.getController();
            ctrl.setConnection(connection);
            ctrl.setStage(primaryStage);
            ctrl.setUsername(username);
            Scene scene = new Scene(root, 860, 620);
            if (MainApp.getCss() != null) scene.getStylesheets().add(MainApp.getCss());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Finance Tracker – Dashboard");
            primaryStage.setResizable(true);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to open dashboard.");
        }
    }

    private void showError(String msg)   { messageLabel.setStyle("-fx-text-fill: #e53935;"); messageLabel.setText(msg); }
    private void showSuccess(String msg) { messageLabel.setStyle("-fx-text-fill: #43a047;"); messageLabel.setText(msg); }
}
