package com.itextpdf.text.pdf.qrcode;

import java.util.Map;

public final class QRCodeWriter {
    private static final int QUIET_ZONE_SIZE = 4;

    public ByteMatrix encode(String contents, int width, int height) throws WriterException {
        return encode(contents, width, height, null);
    }

    public ByteMatrix encode(String contents, int width, int height, Map<EncodeHintType, Object> hints) throws WriterException {
        if (contents == null || contents.length() == 0) {
            throw new IllegalArgumentException("Found empty contents");
        } else if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' + height);
        } else {
            ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.f160L;
            if (hints != null) {
                ErrorCorrectionLevel requestedECLevel = (ErrorCorrectionLevel) hints.get(EncodeHintType.ERROR_CORRECTION);
                if (requestedECLevel != null) {
                    errorCorrectionLevel = requestedECLevel;
                }
            }
            QRCode code = new QRCode();
            Encoder.encode(contents, errorCorrectionLevel, hints, code);
            return renderResult(code, width, height);
        }
    }

    private static ByteMatrix renderResult(QRCode code, int width, int height) {
        int y;
        ByteMatrix input = code.getMatrix();
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + 8;
        int qrHeight = inputHeight + 8;
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);
        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
        ByteMatrix output = new ByteMatrix(outputWidth, outputHeight);
        byte[][] outputArray = output.getArray();
        Object row = new byte[outputWidth];
        for (y = 0; y < topPadding; y++) {
            setRowColor(outputArray[y], (byte) -1);
        }
        byte[][] inputArray = input.getArray();
        for (y = 0; y < inputHeight; y++) {
            int x;
            for (x = 0; x < leftPadding; x++) {
                row[x] = (byte) -1;
            }
            int offset = leftPadding;
            for (x = 0; x < inputWidth; x++) {
                int z;
                byte value = inputArray[y][x] == (byte) 1 ? (byte) 0 : (byte) -1;
                for (z = 0; z < multiple; z++) {
                    row[offset + z] = value;
                }
                offset += multiple;
            }
            for (x = leftPadding + (inputWidth * multiple); x < outputWidth; x++) {
                row[x] = (byte) -1;
            }
            offset = topPadding + (y * multiple);
            for (z = 0; z < multiple; z++) {
                System.arraycopy(row, 0, outputArray[offset + z], 0, outputWidth);
            }
        }
        for (y = topPadding + (inputHeight * multiple); y < outputHeight; y++) {
            setRowColor(outputArray[y], (byte) -1);
        }
        return output;
    }

    private static void setRowColor(byte[] row, byte value) {
        for (int x = 0; x < row.length; x++) {
            row[x] = value;
        }
    }
}
