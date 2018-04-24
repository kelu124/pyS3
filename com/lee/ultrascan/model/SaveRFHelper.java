package com.lee.ultrascan.model;

import android.content.Context;
import com.lee.ultrascan.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveRFHelper {
    private static final String SAVE_DIR = "RFdata";
    private Context context;

    public SaveRFHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public void saveRF2PGM(String filename, RFFrame rfFrame) throws IOException {
        File file = FileUtils.openFile(FileUtils.getApplicationDir(this.context, SAVE_DIR), filename + ".pgm");
        String header = getPGMHeader(rfFrame.getSamplesPerLine(), rfFrame.getLines(), 255);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(header.getBytes());
        outputStream.write(rfFrame.getEnvelopData());
        outputStream.flush();
        outputStream.close();
    }

    private String getPGMHeader(int width, int height, int maxPixelValue) {
        StringBuffer pgmHeader = new StringBuffer();
        pgmHeader.append("P5\n");
        pgmHeader.append(String.valueOf(width)).append(" ").append(String.valueOf(height)).append("\n");
        pgmHeader.append(String.valueOf(maxPixelValue)).append("\n");
        return pgmHeader.toString();
    }
}
