package org.seba.eventrack.bll.services.qrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class QRCodeService {

    public String generateQrCode(Long ticketId) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            String qrContent = "https://fr.wikipedia.org/wiki/Rickroll";

            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

            Path qrCodePath = Path.of("qrcodes/ticket_" + ticketId + ".png");
            Files.createDirectories(qrCodePath.getParent());
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                Files.write(qrCodePath, outputStream.toByteArray(), StandardOpenOption.CREATE);
            }
            return qrCodePath.toString();
        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Failed to generate QR Code", e);
        }
    }
}