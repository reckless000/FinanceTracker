package client.controllers;

import client.ClientConnection;
import client.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button logoutBtn;
    @FXML private TabPane tabPane;
    @FXML private Tab addTab;
    @FXML private Tab viewTab;
    @FXML private Tab summaryTab;

    private ClientConnection connection;
    private Stage primaryStage;
    private String username;

    public void setConnection(ClientConnection connection) { this.connection = connection; }
    public void setStage(Stage stage) { this.primaryStage = stage; }

    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
        loadAddTransactionTab();
        loadViewTransactionsTab();
        loadSummaryTab();
    }

    @FXML
    private void initialize() {
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void loadAddTransactionTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_transaction.fxml"));
            Parent content = loader.load();
            AddTransactionController ctrl = loader.getController();
            ctrl.setConnection(connection);
            ctrl.setUsername(username);
            addTab.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
            addTab.setContent(new Label("⚠ Error loading tab."));
        }
    }

    private void loadViewTransactionsTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view_transactions.fxml"));
            Parent content = loader.load();
            ViewTransactionsController ctrl = loader.getController();
            ctrl.setConnection(connection);
            ctrl.setUsername(username);
            viewTab.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
            viewTab.setContent(new Label("⚠ Error loading tab."));
        }
    }

    private void loadSummaryTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/summary.fxml"));
            Parent content = loader.load();
            SummaryController ctrl = loader.getController();
            ctrl.setConnection(connection);
            ctrl.setUsername(username);
            summaryTab.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
            summaryTab.setContent(new Label("⚠ Error loading tab."));
        }
    }

    private void handleLogout() {
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
}
