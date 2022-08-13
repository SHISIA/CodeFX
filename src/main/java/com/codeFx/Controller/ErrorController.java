package com.codeFx.Controller;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController implements Initializable {
    public ImageView imageIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            imageIcon.setImage(new Image(new File(getClass().getResource("/images/error.png").toURI().toString()).toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
