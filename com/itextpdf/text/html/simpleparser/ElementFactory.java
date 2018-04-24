package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.HyphenationAuto;
import com.itextpdf.text.pdf.HyphenationEvent;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Deprecated
public class ElementFactory {
    private FontProvider provider = FontFactory.getFontImp();

    public void setFontProvider(FontProvider provider) {
        this.provider = provider;
    }

    public FontProvider getFontProvider() {
        return this.provider;
    }

    public Font getFont(ChainedProperties chain) {
        String face = chain.getProperty(HtmlTags.FACE);
        if (face == null || face.trim().length() == 0) {
            face = chain.getProperty(HtmlTags.FONTFAMILY);
        }
        if (face != null) {
            StringTokenizer tok = new StringTokenizer(face, ",");
            while (tok.hasMoreTokens()) {
                face = tok.nextToken().trim();
                if (face.startsWith("\"")) {
                    face = face.substring(1);
                }
                if (face.endsWith("\"")) {
                    face = face.substring(0, face.length() - 1);
                }
                if (this.provider.isRegistered(face)) {
                    break;
                }
            }
        }
        String encoding = chain.getProperty(HtmlTags.ENCODING);
        if (encoding == null) {
            encoding = "Cp1252";
        }
        String value = chain.getProperty(HtmlTags.SIZE);
        float size = HtmlUtilities.DEFAULT_FONT_SIZE;
        if (value != null) {
            size = Float.parseFloat(value);
        }
        int style = 0;
        String decoration = chain.getProperty(HtmlTags.TEXTDECORATION);
        if (!(decoration == null || decoration.trim().length() == 0)) {
            if (HtmlTags.UNDERLINE.equals(decoration)) {
                style = 0 | 4;
            } else if (HtmlTags.LINETHROUGH.equals(decoration)) {
                style = 0 | 8;
            }
        }
        if (chain.hasProperty("i")) {
            style |= 2;
        }
        if (chain.hasProperty(HtmlTags.f33B)) {
            style |= 1;
        }
        if (chain.hasProperty(HtmlTags.f37U)) {
            style |= 4;
        }
        if (chain.hasProperty(HtmlTags.f36S)) {
            style |= 8;
        }
        return this.provider.getFont(face, encoding, true, size, style, HtmlUtilities.decodeColor(chain.getProperty(HtmlTags.COLOR)));
    }

    public Chunk createChunk(String content, ChainedProperties chain) {
        Font font = getFont(chain);
        Chunk ck = new Chunk(content, font);
        if (chain.hasProperty(HtmlTags.SUB)) {
            ck.setTextRise((-font.getSize()) / BaseField.BORDER_WIDTH_MEDIUM);
        } else if (chain.hasProperty(HtmlTags.SUP)) {
            ck.setTextRise(font.getSize() / BaseField.BORDER_WIDTH_MEDIUM);
        }
        ck.setHyphenation(getHyphenation(chain));
        return ck;
    }

    public Paragraph createParagraph(ChainedProperties chain) {
        Paragraph paragraph = new Paragraph();
        updateElement(paragraph, chain);
        return paragraph;
    }

    public ListItem createListItem(ChainedProperties chain) {
        ListItem item = new ListItem();
        updateElement(item, chain);
        return item;
    }

    protected void updateElement(Paragraph paragraph, ChainedProperties chain) {
        paragraph.setAlignment(HtmlUtilities.alignmentValue(chain.getProperty(HtmlTags.ALIGN)));
        paragraph.setHyphenation(getHyphenation(chain));
        setParagraphLeading(paragraph, chain.getProperty(HtmlTags.LEADING));
        String value = chain.getProperty(HtmlTags.AFTER);
        if (value != null) {
            try {
                paragraph.setSpacingBefore(Float.parseFloat(value));
            } catch (Exception e) {
            }
        }
        value = chain.getProperty(HtmlTags.AFTER);
        if (value != null) {
            try {
                paragraph.setSpacingAfter(Float.parseFloat(value));
            } catch (Exception e2) {
            }
        }
        value = chain.getProperty(HtmlTags.EXTRAPARASPACE);
        if (value != null) {
            try {
                paragraph.setExtraParagraphSpace(Float.parseFloat(value));
            } catch (Exception e3) {
            }
        }
        value = chain.getProperty(HtmlTags.INDENT);
        if (value != null) {
            try {
                paragraph.setIndentationLeft(Float.parseFloat(value));
            } catch (Exception e4) {
            }
        }
    }

    protected static void setParagraphLeading(Paragraph paragraph, String leading) {
        if (leading == null) {
            paragraph.setLeading(0.0f, 1.5f);
            return;
        }
        try {
            StringTokenizer tk = new StringTokenizer(leading, " ,");
            float v1 = Float.parseFloat(tk.nextToken());
            if (tk.hasMoreTokens()) {
                paragraph.setLeading(v1, Float.parseFloat(tk.nextToken()));
            } else {
                paragraph.setLeading(v1, 0.0f);
            }
        } catch (Exception e) {
            paragraph.setLeading(0.0f, 1.5f);
        }
    }

    public HyphenationEvent getHyphenation(ChainedProperties chain) {
        String value = chain.getProperty(HtmlTags.HYPHENATION);
        if (value == null || value.length() == 0) {
            return null;
        }
        int pos = value.indexOf(95);
        if (pos == -1) {
            return new HyphenationAuto(value, null, 2, 2);
        }
        String lang = value.substring(0, pos);
        String country = value.substring(pos + 1);
        pos = country.indexOf(44);
        if (pos == -1) {
            return new HyphenationAuto(lang, country, 2, 2);
        }
        int leftMin;
        int rightMin = 2;
        value = country.substring(pos + 1);
        country = country.substring(0, pos);
        pos = value.indexOf(44);
        if (pos == -1) {
            leftMin = Integer.parseInt(value);
        } else {
            leftMin = Integer.parseInt(value.substring(0, pos));
            rightMin = Integer.parseInt(value.substring(pos + 1));
        }
        return new HyphenationAuto(lang, country, leftMin, rightMin);
    }

    public LineSeparator createLineSeparator(Map<String, String> attrs, float offset) {
        float lineWidth = BaseField.BORDER_WIDTH_THIN;
        String size = (String) attrs.get(HtmlTags.SIZE);
        if (size != null) {
            float tmpSize = HtmlUtilities.parseLength(size, HtmlUtilities.DEFAULT_FONT_SIZE);
            if (tmpSize > 0.0f) {
                lineWidth = tmpSize;
            }
        }
        String width = (String) attrs.get(HtmlTags.WIDTH);
        float percentage = 100.0f;
        if (width != null) {
            float tmpWidth = HtmlUtilities.parseLength(width, HtmlUtilities.DEFAULT_FONT_SIZE);
            if (tmpWidth > 0.0f) {
                percentage = tmpWidth;
            }
            if (!width.endsWith("%")) {
                percentage = 100.0f;
            }
        }
        return new LineSeparator(lineWidth, percentage, null, HtmlUtilities.alignmentValue((String) attrs.get(HtmlTags.ALIGN)), offset);
    }

    public Image createImage(String src, Map<String, String> attrs, ChainedProperties chain, DocListener document, ImageProvider img_provider, HashMap<String, Image> img_store, String img_baseurl) throws DocumentException, IOException {
        Image img = null;
        if (img_provider != null) {
            img = img_provider.getImage(src, attrs, chain, document);
        }
        if (img == null && img_store != null) {
            Image tim = (Image) img_store.get(src);
            if (tim != null) {
                img = Image.getInstance(tim);
            }
        }
        if (img != null) {
            return img;
        }
        if (!src.startsWith("http") && img_baseurl != null) {
            src = img_baseurl + src;
        } else if (img == null) {
            if (!src.startsWith("http")) {
                String path = chain.getProperty(HtmlTags.IMAGEPATH);
                if (path == null) {
                    path = "";
                }
                src = new File(path, src).getPath();
            }
        }
        img = Image.getInstance(src);
        if (img == null) {
            return null;
        }
        float actualFontSize = HtmlUtilities.parseLength(chain.getProperty(HtmlTags.SIZE), HtmlUtilities.DEFAULT_FONT_SIZE);
        if (actualFontSize <= 0.0f) {
            actualFontSize = HtmlUtilities.DEFAULT_FONT_SIZE;
        }
        float widthInPoints = HtmlUtilities.parseLength((String) attrs.get(HtmlTags.WIDTH), actualFontSize);
        float heightInPoints = HtmlUtilities.parseLength((String) attrs.get(HtmlTags.HEIGHT), actualFontSize);
        if (widthInPoints > 0.0f && heightInPoints > 0.0f) {
            img.scaleAbsolute(widthInPoints, heightInPoints);
        } else if (widthInPoints > 0.0f) {
            img.scaleAbsolute(widthInPoints, (img.getHeight() * widthInPoints) / img.getWidth());
        } else if (heightInPoints > 0.0f) {
            img.scaleAbsolute((img.getWidth() * heightInPoints) / img.getHeight(), heightInPoints);
        }
        String before = chain.getProperty(HtmlTags.BEFORE);
        if (before != null) {
            img.setSpacingBefore(Float.parseFloat(before));
        }
        String after = chain.getProperty(HtmlTags.AFTER);
        if (after != null) {
            img.setSpacingAfter(Float.parseFloat(after));
        }
        img.setWidthPercentage(0.0f);
        return img;
    }

    public List createList(String tag, ChainedProperties chain) {
        List list;
        if (HtmlTags.UL.equalsIgnoreCase(tag)) {
            list = new List(false);
            list.setListSymbol("• ");
        } else {
            list = new List(true);
        }
        try {
            list.setIndentationLeft(new Float(chain.getProperty(HtmlTags.INDENT)).floatValue());
        } catch (Exception e) {
            list.setAutoindent(true);
        }
        return list;
    }
}
