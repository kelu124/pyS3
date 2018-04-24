package org.apache.poi.poifs.dev;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class POIFSViewEngine {
    private static final String _EOL = System.getProperty("line.separator");

    public static List<String> inspectViewable(Object viewable, boolean drilldown, int indentLevel, String indentString) {
        List<String> objects = new ArrayList();
        if (viewable instanceof POIFSViewable) {
            POIFSViewable inspected = (POIFSViewable) viewable;
            objects.add(indent(indentLevel, indentString, inspected.getShortDescription()));
            if (drilldown) {
                if (inspected.preferArray()) {
                    Object[] data = inspected.getViewableArray();
                    for (Object inspectViewable : data) {
                        objects.addAll(inspectViewable(inspectViewable, drilldown, indentLevel + 1, indentString));
                    }
                } else {
                    Iterator<Object> iter = inspected.getViewableIterator();
                    while (iter.hasNext()) {
                        objects.addAll(inspectViewable(iter.next(), drilldown, indentLevel + 1, indentString));
                    }
                }
            }
        } else {
            objects.add(indent(indentLevel, indentString, viewable.toString()));
        }
        return objects;
    }

    private static String indent(int indentLevel, String indentString, String data) {
        StringBuffer finalBuffer = new StringBuffer();
        StringBuffer indentPrefix = new StringBuffer();
        for (int j = 0; j < indentLevel; j++) {
            indentPrefix.append(indentString);
        }
        LineNumberReader reader = new LineNumberReader(new StringReader(data));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                finalBuffer.append(indentPrefix).append(line).append(_EOL);
            }
        } catch (IOException e) {
            finalBuffer.append(indentPrefix).append(e.getMessage()).append(_EOL);
        }
        return finalBuffer.toString();
    }
}
