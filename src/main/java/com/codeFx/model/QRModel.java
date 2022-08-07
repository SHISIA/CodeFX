package com.codeFx.model;

import com.codeFx.test.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;

public class QRModel {
   ImageView imageView;
   Button encodeBtn;
   Button decodeBtn;
   TextField inOutPut;

    public QRModel(ImageView img, Button encodeBtn,Button decodeBtn,TextField in_out_field) {
        imageView = img;
        this.inOutPut=in_out_field;
        this.encodeBtn=encodeBtn;
        this.decodeBtn=decodeBtn;
        startOperations();
    }

    private void startOperations(){
        encodeBtn.setDefaultButton(true);
        encodeBtn.setOnAction(e->{
            String text=inOutPut.getText();
            try {
                createQRImage("shisia.png",text);
            } catch (WriterException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        decodeBtn.setOnAction(e->{
            File file=new File(imageView.getId());
            try {
                inOutPut.setText(decodeQRCode(file));
            } catch (IOException ex) {
                //System.out.println("Input File Empty or Cannot Be Read");
                ex.printStackTrace();
            }
        });

        imageView.setOnMouseClicked(e->{
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
                    ,new FileChooser.ExtensionFilter("GIF Files", "*.gif")
                    ,new FileChooser.ExtensionFilter("JPEG Files", "*.jpg")
            );
            imageView.setId(String.valueOf(selectedFile));
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        });

    }

    public void loadView(){
        try {
            URL url = getClass().getResource("/images/qr.png");
            File file= new File(url.toURI());
            imageView.setImage(new Image(file.toURI().toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createQRImage(String filePath, String qrCodeText)
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
        ImageIO.write(image, "PNG", qrFile);
    }

    public   String decodeQRCode(File qrCodeImage) throws IOException {
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
