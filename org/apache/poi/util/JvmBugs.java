package org.apache.poi.util;

import java.util.Locale;

public class JvmBugs {
    private static final POILogger LOG = POILogFactory.getLogger(JvmBugs.class);

    public static boolean hasLineBreakMeasurerBug() {
        String version = System.getProperty("java.version");
        boolean hasBug = !Boolean.getBoolean("org.apache.poi.JvmBugs.LineBreakMeasurer.ignore") && System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win") && ("1.6.0_45".equals(version) || "1.7.0_21".equals(version));
        if (hasBug) {
            LOG.log(5, "JVM has LineBreakMeasurer bug - see POI bug #54904 - caller code might default to Lucida Sans");
        }
        return hasBug;
    }
}
