package com.hcl.parceltracking.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Implementation of BarcodeService for generating barcodes.
 * 
 * Uses ZXing library to generate Code 128 barcodes.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Service
public class BarcodeServiceImpl implements BarcodeService {

    private static final Logger logger = LoggerFactory.getLogger(BarcodeServiceImpl.class);
    private static final int BARCODE_WIDTH = 300;
    private static final int BARCODE_HEIGHT = 100;

    /**
     * Generate a Code 128 barcode for a given tracking ID.
     * 
     * @param trackingId the tracking identifier to encode
     * @return Base64 encoded PNG image of the barcode
     */
    @Override
    public String generateBarcode(String trackingId) {
        try {
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(trackingId, BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_HEIGHT);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            byte[] barcodeBytes = outputStream.toByteArray();
            String base64Barcode = Base64.getEncoder().encodeToString(barcodeBytes);

            logger.debug("Successfully generated barcode for tracking ID: {}", trackingId);
            return "data:image/png;base64," + base64Barcode;

        } catch (WriterException | IOException e) {
            logger.error("Error generating barcode for tracking ID: {}", trackingId, e);
            throw new RuntimeException("Failed to generate barcode", e);
        }
    }
}
