package com.example.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        HelloController controller = fxmlLoader.getController();

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/ui/style.css")).toExternalForm());
        stage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.setAlwaysOnTop(true);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        SpotifyPoller.start(controller);
        stage.setScene(scene);
        stage.show();
    }
}
