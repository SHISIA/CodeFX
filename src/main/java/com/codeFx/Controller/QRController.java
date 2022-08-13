package com.codeFx.Controller;

import com.codeFx.model.QRModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class QRController implements Initializable {
    @FXML
    public ImageView qrImage;
    public TextField inOutput;
    QRModel model;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            /*
            *Checks the Operating System type
            *Checks if the storage directory is created:
            * if not created, it creates one that is platform specific
            */
            String osType=System.getProperty("os.name").toLowerCase();
            System.out.println(osType);
            switch (osType){
                case "mac os x":
                    createFile("/Users/apple/Documents/QrImages");
                    break;
                case "linux":
                    createFile("");
                    break;
                case "windows":
                    createFile("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\QrImages");
                    break;
            }
            model=new QRModel(qrImage,inOutput);
            qrImage.setImage(new Image(getClass().getResource("/images/qr-code.gif").toURI().toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void createFile(String osDocPath){
        File file = new File(osDocPath);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            }
        }else {
            System.out.println("Directory Exists");
        }
    }
}