package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Deprecated
public class StyleSheet {
    protected Map<String, Map<String, String>> classMap = new HashMap();
    protected Map<String, Map<String, String>> tagMap = new HashMap();

    public void loadTagStyle(String tag, Map<String, String> attrs) {
        this.tagMap.put(tag.toLowerCase(), attrs);
    }

    public void loadTagStyle(String tag, String key, String value) {
        tag = tag.toLowerCase();
        Map<String, String> styles = (Map) this.tagMap.get(tag);
        if (styles == null) {
            styles = new HashMap();
            this.tagMap.put(tag, styles);
        }
        styles.put(key, value);
    }

    public void loadStyle(String className, HashMap<String, String> attrs) {
        this.classMap.put(className.toLowerCase(), attrs);
    }

    public void loadStyle(String className, String key, String value) {
        className = className.toLowerCase();
        Map<String, String> styles = (Map) this.classMap.get(className);
        if (styles == null) {
            styles = new HashMap();
            this.classMap.put(className, styles);
        }
        styles.put(key, value);
    }

    public void applyStyle(String tag, Map<String, String> attrs) {
        Map<String, String> map = (Map) this.tagMap.get(tag.toLowerCase());
        if (map != null) {
            Map<String, String> temp = new HashMap(map);
            temp.putAll(attrs);
            attrs.putAll(temp);
        }
        String cm = (String) attrs.get(HtmlTags.CLASS);
        if (cm != null) {
            map = (Map) this.classMap.get(cm.toLowerCase());
            if (map != null) {
                attrs.remove(HtmlTags.CLASS);
                temp = new HashMap(map);
                temp.putAll(attrs);
                attrs.putAll(temp);
            }
        }
    }

    public static void resolveStyleAttribute(Map<String, String> h, ChainedProperties chain) {
        String style = (String) h.get(HtmlTags.STYLE);
        if (style != null) {
            Properties prop = HtmlUtilities.parseAttributes(style);
            for (String key : prop.keySet()) {
                if (key.equals(HtmlTags.FONTFAMILY)) {
                    h.put(HtmlTags.FACE, prop.getProperty(key));
                } else if (key.equals(HtmlTags.FONTSIZE)) {
                    actualFontSize = HtmlUtilities.parseLength(chain.getProperty(HtmlTags.SIZE), HtmlUtilities.DEFAULT_FONT_SIZE);
                    if (actualFontSize <= 0.0f) {
                        actualFontSize = HtmlUtilities.DEFAULT_FONT_SIZE;
                    }
                    h.put(HtmlTags.SIZE, Float.toString(HtmlUtilities.parseLength(prop.getProperty(key), actualFontSize)) + "pt");
                } else if (key.equals(HtmlTags.FONTSTYLE)) {
                    ss = prop.getProperty(key).trim().toLowerCase();
                    if (ss.equals(HtmlTags.ITALIC) || ss.equals(HtmlTags.OBLIQUE)) {
                        h.put("i", null);
                    }
                } else if (key.equals(HtmlTags.FONTWEIGHT)) {
                    ss = prop.getProperty(key).trim().toLowerCase();
                    if (ss.equals(HtmlTags.BOLD) || ss.equals("700") || ss.equals("800") || ss.equals("900")) {
                        h.put(HtmlTags.f33B, null);
                    }
                } else if (key.equals(HtmlTags.TEXTDECORATION)) {
                    if (prop.getProperty(key).trim().toLowerCase().equals(HtmlTags.UNDERLINE)) {
                        h.put(HtmlTags.f37U, null);
                    }
                } else if (key.equals(HtmlTags.COLOR)) {
                    BaseColor c = HtmlUtilities.decodeColor(prop.getProperty(key));
                    if (c != null) {
                        String hs = "000000" + Integer.toHexString(c.getRGB());
                        h.put(HtmlTags.COLOR, "#" + hs.substring(hs.length() - 6));
                    }
                } else if (key.equals(HtmlTags.LINEHEIGHT)) {
                    ss = prop.getProperty(key).trim();
                    actualFontSize = HtmlUtilities.parseLength(chain.getProperty(HtmlTags.SIZE), HtmlUtilities.DEFAULT_FONT_SIZE);
                    if (actualFontSize <= 0.0f) {
                        actualFontSize = HtmlUtilities.DEFAULT_FONT_SIZE;
                    }
                    float v = HtmlUtilities.parseLength(prop.getProperty(key), actualFontSize);
                    if (ss.endsWith("%")) {
                        h.put(HtmlTags.LEADING, "0," + (v / 100.0f));
                        return;
                    } else if (HtmlTags.NORMAL.equalsIgnoreCase(ss)) {
                        h.put(HtmlTags.LEADING, "0,1.5");
                        return;
                    } else {
                        h.put(HtmlTags.LEADING, v + ",0");
                    }
                } else if (key.equals(HtmlTags.TEXTALIGN)) {
                    h.put(HtmlTags.ALIGN, prop.getProperty(key).trim().toLowerCase());
                } else if (key.equals(HtmlTags.PADDINGLEFT)) {
                    h.put(HtmlTags.INDENT, Float.toString(HtmlUtilities.parseLength(prop.getProperty(key).trim().toLowerCase())));
                }
            }
        }
    }
}
