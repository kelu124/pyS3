package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated
public class ChainedProperties {
    public List<TagAttributes> chain = new ArrayList();

    public String getProperty(String key) {
        for (int k = this.chain.size() - 1; k >= 0; k--) {
            String ret = (String) ((TagAttributes) this.chain.get(k)).attrs.get(key);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    public boolean hasProperty(String key) {
        for (int k = this.chain.size() - 1; k >= 0; k--) {
            if (((TagAttributes) this.chain.get(k)).attrs.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public void addToChain(String tag, Map<String, String> props) {
        adjustFontSize(props);
        this.chain.add(new TagAttributes(tag, props));
    }

    public void removeChain(String tag) {
        for (int k = this.chain.size() - 1; k >= 0; k--) {
            if (tag.equals(((TagAttributes) this.chain.get(k)).tag)) {
                this.chain.remove(k);
                return;
            }
        }
    }

    protected void adjustFontSize(Map<String, String> attrs) {
        String value = (String) attrs.get(HtmlTags.SIZE);
        if (value != null) {
            if (value.endsWith("pt")) {
                attrs.put(HtmlTags.SIZE, value.substring(0, value.length() - 2));
                return;
            }
            attrs.put(HtmlTags.SIZE, Integer.toString(HtmlUtilities.getIndexedFontSize(value, getProperty(HtmlTags.SIZE))));
        }
    }
}
