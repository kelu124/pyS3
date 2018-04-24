package org.apache.poi.hssf.usermodel;

import com.itextpdf.text.html.HtmlTags;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

final class StaticFontMetrics {
    private static final POILogger LOGGER = POILogFactory.getLogger(StaticFontMetrics.class);
    private static final Map<String, FontDetails> fontDetailsMap = new HashMap();
    private static Properties fontMetricsProps;

    private StaticFontMetrics() {
    }

    public static synchronized FontDetails getFontDetails(Font font) {
        FontDetails fontDetails;
        synchronized (StaticFontMetrics.class) {
            if (fontMetricsProps == null) {
                try {
                    fontMetricsProps = loadMetrics();
                } catch (IOException e) {
                    throw new RuntimeException("Could not load font metrics", e);
                }
            }
            String fontName = font.getName();
            String fontStyle = "";
            if (font.isPlain()) {
                fontStyle = fontStyle + "plain";
            }
            if (font.isBold()) {
                fontStyle = fontStyle + HtmlTags.BOLD;
            }
            if (font.isItalic()) {
                fontStyle = fontStyle + HtmlTags.ITALIC;
            }
            String fontHeight = FontDetails.buildFontHeightProperty(fontName);
            String styleHeight = FontDetails.buildFontHeightProperty(fontName + "." + fontStyle);
            if (fontMetricsProps.get(fontHeight) == null && fontMetricsProps.get(styleHeight) != null) {
                fontName = fontName + "." + fontStyle;
            }
            fontDetails = (FontDetails) fontDetailsMap.get(fontName);
            if (fontDetails == null) {
                fontDetails = FontDetails.create(fontName, fontMetricsProps);
                fontDetailsMap.put(fontName, fontDetails);
            }
        }
        return fontDetails;
    }

    private static Properties loadMetrics() throws IOException {
        SecurityException e;
        InputStream metricsIn;
        Properties props;
        File propFile = null;
        try {
            String propFileName = System.getProperty("font.metrics.filename");
            if (propFileName != null) {
                File propFile2 = new File(propFileName);
                try {
                    if (propFile2.exists()) {
                        propFile = propFile2;
                    } else {
                        LOGGER.log(5, new Object[]{"font_metrics.properties not found at path " + propFile2.getAbsolutePath()});
                        propFile = null;
                    }
                } catch (SecurityException e2) {
                    e = e2;
                    propFile = propFile2;
                    LOGGER.log(5, new Object[]{"Can't access font.metrics.filename system property", e});
                    metricsIn = null;
                    if (propFile != null) {
                        metricsIn = FontDetails.class.getResourceAsStream("/font_metrics.properties");
                        if (metricsIn == null) {
                            throw new IOException("font_metrics.properties not found in classpath");
                        }
                    }
                    try {
                        metricsIn = new FileInputStream(propFile);
                    } catch (Throwable th) {
                        if (metricsIn != null) {
                            metricsIn.close();
                        }
                    }
                    props = new Properties();
                    props.load(metricsIn);
                    if (metricsIn != null) {
                        metricsIn.close();
                    }
                    return props;
                }
            }
        } catch (SecurityException e3) {
            e = e3;
            LOGGER.log(5, new Object[]{"Can't access font.metrics.filename system property", e});
            metricsIn = null;
            if (propFile != null) {
                metricsIn = new FileInputStream(propFile);
            } else {
                metricsIn = FontDetails.class.getResourceAsStream("/font_metrics.properties");
                if (metricsIn == null) {
                    throw new IOException("font_metrics.properties not found in classpath");
                }
            }
            props = new Properties();
            props.load(metricsIn);
            if (metricsIn != null) {
                metricsIn.close();
            }
            return props;
        }
        metricsIn = null;
        if (propFile != null) {
            metricsIn = new FileInputStream(propFile);
        } else {
            metricsIn = FontDetails.class.getResourceAsStream("/font_metrics.properties");
            if (metricsIn == null) {
                throw new IOException("font_metrics.properties not found in classpath");
            }
        }
        props = new Properties();
        props.load(metricsIn);
        if (metricsIn != null) {
            metricsIn.close();
        }
        return props;
    }
}
