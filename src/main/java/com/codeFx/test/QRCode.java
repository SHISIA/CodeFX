//package com.codeFx.test;
//
//import com.google.zxing.*;
//import com.google.zxing.common.HybridBinarizer;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class QRCode {
//    public   String decodeQRCode(File qrCodeimage) throws IOException {
//        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
//        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
//        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//        try {
//            Result result = new MultiFormatReader().decode(bitmap);
//            return result.getText();
//        } catch (NotFoundException e) {
//            System.out.println("There is no QR code in the image");
//            return null;
//        }
//    }
//}