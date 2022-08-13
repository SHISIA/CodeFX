package com.codeFx.model;

import com.codeFx.lib.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class QRModel {
   ImageView imageView;

    TextField inOutPut;

    public QRModel(ImageView img, TextField in_out_field) {
        imageView = img;
        this.inOutPut=in_out_field;
        startOperations();
    }

    private void startOperations(){
        inOutPut.setOnKeyReleased(e->{
            String text=inOutPut.getText();
            if (!text.isEmpty()){
                try {
                    createQRImage("/Users/apple/Documents/QrImages/QRTempOutPut.jpg",text,"JPG");
                    imageView.setImage(new Image(new File("/Users/apple/Documents/QrImages/QRTempOutPut.jpg").toURI().toString()));
                } catch (WriterException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //opens FileChooser to navigate to QR Image to be loaded
        imageView.setOnMouseClicked(e->{
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
                    ,new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
                    ,new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
            );

            //File path saved here for later easier access
            imageView.setId(String.valueOf(selectedFile));
            try {
                imageView.setImage(new Image(selectedFile.toURI().toString()));

                //Sets the information decoded from QR image to textField
                File file=new File(imageView.getId());

                //prints success message if QR image is loaded successfully
                StageLoader.loadStage("/com/codeFX/AlertMessage.fxml");
                try {
                    inOutPut.setText(decodeQRCode(file));
                } catch (IOException ex) {
                    throw new RuntimeException("File Appears to be Empty");
                }
            }catch (NullPointerException np){
                System.out.println("Selected file is null");
            }
        });

    }

    private static void createQRImage(String filePath, String qrCodeText,String imgExtension)
            throws WriterException, IOException {
        File qrFile = new File(filePath);

        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 125, 125, hintMap);

        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);

        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ImageIO.write(image, imgExtension, qrFile);
    }

    public String decodeQRCode(File qrCodeImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }

}
