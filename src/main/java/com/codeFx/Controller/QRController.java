package com.codeFx.Controller;

import com.codeFx.model.QRModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class QRController implements Initializable {
    @FXML
    public ImageView qrImage;
    public Button encodeBtn;
    public Button decodeBtn;
    public TextField inOutput;
    QRModel model;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            model=new QRModel(qrImage,encodeBtn,decodeBtn,inOutput);
            qrImage.setImage(new Image(getClass().getResource("/images/qr-code.gif").toURI().toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void onHelloButtonClick() {
        model.loadView();
    }
}