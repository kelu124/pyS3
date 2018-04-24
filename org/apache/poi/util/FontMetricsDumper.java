package org.apache.poi.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FontMetricsDumper {
    @SuppressForbidden("command line tool")
    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font fontName : allFonts) {
            char c;
            String fontName2 = fontName.getFontName();
            FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(new Font(fontName2, 1, 10));
            props.setProperty("font." + fontName2 + ".height", fontMetrics.getHeight() + "");
            StringBuffer characters = new StringBuffer();
            for (c = 'a'; c <= 'z'; c = (char) (c + 1)) {
                characters.append(c + ", ");
            }
            for (c = 'A'; c <= 'Z'; c = (char) (c + 1)) {
                characters.append(c + ", ");
            }
            for (c = '0'; c <= '9'; c = (char) (c + 1)) {
                characters.append(c + ", ");
            }
            StringBuffer widths = new StringBuffer();
            for (c = 'a'; c <= 'z'; c = (char) (c + 1)) {
                widths.append(fontMetrics.getWidths()[c] + ", ");
            }
            for (c = 'A'; c <= 'Z'; c = (char) (c + 1)) {
                widths.append(fontMetrics.getWidths()[c] + ", ");
            }
            for (c = '0'; c <= '9'; c = (char) (c + 1)) {
                widths.append(fontMetrics.getWidths()[c] + ", ");
            }
            props.setProperty("font." + fontName2 + ".characters", characters.toString());
            props.setProperty("font." + fontName2 + ".widths", widths.toString());
        }
        FileOutputStream fileOut = new FileOutputStream("font_metrics.properties");
        try {
            props.store(fileOut, "Font Metrics");
        } finally {
            fileOut.close();
        }
    }
}
