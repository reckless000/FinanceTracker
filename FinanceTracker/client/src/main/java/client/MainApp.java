package client;

import client.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static String cssUrl;
    private ClientConnection connection = new ClientConnection();

    @Override
    public void start(Stage primaryStage) throws Exception {
        connection.connect();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        LoginController ctrl = loader.getController();
        ctrl.setConnection(connection);
        ctrl.setStage(primaryStage);

        Scene scene = new Scene(root, 420, 500);

        // Load CSS only if it exists on classpath — won't crash if missing
        java.net.URL cssResource = getClass().getResource("/css/style/style.css");
        if (cssResource != null) {
            cssUrl = cssResource.toExternalForm();
            scene.getStylesheets().add(cssUrl);
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Finance Tracker – Login");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getCss() {
        return cssUrl; // may be null if CSS not found — controllers handle this safely
    }
}
