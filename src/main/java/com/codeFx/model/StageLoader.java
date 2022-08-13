package com.codeFx.model;

import com.codeFx.Controller.QRMain;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class StageLoader {

    public static Stage loadStage(String rscPath) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(QRMain.class.getResource(rscPath));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("CodeFX");
            stage.setScene(scene);
            stage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished( event -> stage.close() );
            delay.play();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stage;
    }
}
