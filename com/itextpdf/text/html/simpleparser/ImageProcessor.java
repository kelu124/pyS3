package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocListener;
import com.itextpdf.text.Image;
import java.util.Map;

@Deprecated
public interface ImageProcessor {
    boolean process(Image image, Map<String, String> map, ChainedProperties chainedProperties, DocListener docListener);
}
