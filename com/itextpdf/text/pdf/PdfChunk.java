package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.SplitCharacter;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class PdfChunk {
    private static final float ITALIC_ANGLE = 0.21256f;
    private static final String TABSTOP = "TABSTOP";
    public static final float UNDERLINE_OFFSET = -0.33333334f;
    public static final float UNDERLINE_THICKNESS = 0.06666667f;
    private static final HashSet<String> keysAttributes = new HashSet();
    private static final HashSet<String> keysNoStroke = new HashSet();
    private static final char[] singleSpace = new char[]{' '};
    private static final PdfChunk[] thisChunk = new PdfChunk[1];
    protected IAccessibleElement accessibleElement;
    protected HashMap<String, Object> attributes;
    protected BaseFont baseFont;
    protected boolean changeLeading;
    protected String encoding;
    protected PdfFont font;
    protected Image image;
    protected float imageScalePercentage;
    protected float leading;
    protected boolean newlineSplit;
    protected HashMap<String, Object> noStroke;
    protected float offsetX;
    protected float offsetY;
    protected SplitCharacter splitCharacter;
    protected String value;

    static {
        keysAttributes.add(Chunk.ACTION);
        keysAttributes.add(Chunk.UNDERLINE);
        keysAttributes.add(Chunk.REMOTEGOTO);
        keysAttributes.add(Chunk.LOCALGOTO);
        keysAttributes.add(Chunk.LOCALDESTINATION);
        keysAttributes.add(Chunk.GENERICTAG);
        keysAttributes.add(Chunk.NEWPAGE);
        keysAttributes.add(Chunk.IMAGE);
        keysAttributes.add(Chunk.BACKGROUND);
        keysAttributes.add(Chunk.PDFANNOTATION);
        keysAttributes.add(Chunk.SKEW);
        keysAttributes.add(Chunk.HSCALE);
        keysAttributes.add(Chunk.SEPARATOR);
        keysAttributes.add(Chunk.TAB);
        keysAttributes.add(Chunk.TABSETTINGS);
        keysAttributes.add(Chunk.CHAR_SPACING);
        keysAttributes.add(Chunk.WORD_SPACING);
        keysAttributes.add(Chunk.LINEHEIGHT);
        keysNoStroke.add(Chunk.SUBSUPSCRIPT);
        keysNoStroke.add(Chunk.SPLITCHARACTER);
        keysNoStroke.add(Chunk.HYPHENATION);
        keysNoStroke.add(Chunk.TEXTRENDERMODE);
    }

    PdfChunk(String string, PdfChunk other) {
        this.value = "";
        this.encoding = "Cp1252";
        this.attributes = new HashMap();
        this.noStroke = new HashMap();
        this.imageScalePercentage = BaseField.BORDER_WIDTH_THIN;
        this.changeLeading = false;
        this.leading = 0.0f;
        this.accessibleElement = null;
        thisChunk[0] = this;
        this.value = string;
        this.font = other.font;
        this.attributes = other.attributes;
        this.noStroke = other.noStroke;
        this.baseFont = other.baseFont;
        this.changeLeading = other.changeLeading;
        this.leading = other.leading;
        Object[] obj = (Object[]) this.attributes.get(Chunk.IMAGE);
        if (obj == null) {
            this.image = null;
        } else {
            this.image = (Image) obj[0];
            this.offsetX = ((Float) obj[1]).floatValue();
            this.offsetY = ((Float) obj[2]).floatValue();
            this.changeLeading = ((Boolean) obj[3]).booleanValue();
        }
        this.encoding = this.font.getFont().getEncoding();
        this.splitCharacter = (SplitCharacter) this.noStroke.get(Chunk.SPLITCHARACTER);
        if (this.splitCharacter == null) {
            this.splitCharacter = DefaultSplitCharacter.DEFAULT;
        }
        this.accessibleElement = other.accessibleElement;
    }

    PdfChunk(Chunk chunk, PdfAction action) {
        this.value = "";
        this.encoding = "Cp1252";
        this.attributes = new HashMap();
        this.noStroke = new HashMap();
        this.imageScalePercentage = BaseField.BORDER_WIDTH_THIN;
        this.changeLeading = false;
        this.leading = 0.0f;
        this.accessibleElement = null;
        thisChunk[0] = this;
        this.value = chunk.getContent();
        Font f = chunk.getFont();
        float size = f.getSize();
        if (size == -1.0f) {
            size = HtmlUtilities.DEFAULT_FONT_SIZE;
        }
        this.baseFont = f.getBaseFont();
        int style = f.getStyle();
        if (style == -1) {
            style = 0;
        }
        if (this.baseFont == null) {
            this.baseFont = f.getCalculatedBaseFont(false);
        } else {
            if ((style & 1) != 0) {
                this.attributes.put(Chunk.TEXTRENDERMODE, new Object[]{Integer.valueOf(2), new Float(size / 30.0f), null});
            }
            if ((style & 2) != 0) {
                this.attributes.put(Chunk.SKEW, new float[]{0.0f, ITALIC_ANGLE});
            }
        }
        this.font = new PdfFont(this.baseFont, size);
        HashMap<String, Object> attr = chunk.getAttributes();
        if (attr != null) {
            for (Entry<String, Object> entry : attr.entrySet()) {
                String name = (String) entry.getKey();
                if (keysAttributes.contains(name)) {
                    this.attributes.put(name, entry.getValue());
                } else if (keysNoStroke.contains(name)) {
                    this.noStroke.put(name, entry.getValue());
                }
            }
            if ("".equals(attr.get(Chunk.GENERICTAG))) {
                this.attributes.put(Chunk.GENERICTAG, chunk.getContent());
            }
        }
        if (f.isUnderlined()) {
            this.attributes.put(Chunk.UNDERLINE, Utilities.addToArray((Object[][]) this.attributes.get(Chunk.UNDERLINE), new Object[]{null, new float[]{0.0f, UNDERLINE_THICKNESS, 0.0f, UNDERLINE_OFFSET, 0.0f}}));
        }
        if (f.isStrikethru()) {
            this.attributes.put(Chunk.UNDERLINE, Utilities.addToArray((Object[][]) this.attributes.get(Chunk.UNDERLINE), new Object[]{null, new float[]{0.0f, UNDERLINE_THICKNESS, 0.0f, 0.33333334f, 0.0f}}));
        }
        if (action != null) {
            this.attributes.put(Chunk.ACTION, action);
        }
        this.noStroke.put(Chunk.COLOR, f.getColor());
        this.noStroke.put(Chunk.ENCODING, this.font.getFont().getEncoding());
        Float lh = (Float) this.attributes.get(Chunk.LINEHEIGHT);
        if (lh != null) {
            this.changeLeading = true;
            this.leading = lh.floatValue();
        }
        Object[] obj = (Object[]) this.attributes.get(Chunk.IMAGE);
        if (obj == null) {
            this.image = null;
        } else {
            this.attributes.remove(Chunk.HSCALE);
            this.image = (Image) obj[0];
            this.offsetX = ((Float) obj[1]).floatValue();
            this.offsetY = ((Float) obj[2]).floatValue();
            this.changeLeading = ((Boolean) obj[3]).booleanValue();
        }
        Float hs = (Float) this.attributes.get(Chunk.HSCALE);
        if (hs != null) {
            this.font.setHorizontalScaling(hs.floatValue());
        }
        this.encoding = this.font.getFont().getEncoding();
        this.splitCharacter = (SplitCharacter) this.noStroke.get(Chunk.SPLITCHARACTER);
        if (this.splitCharacter == null) {
            this.splitCharacter = DefaultSplitCharacter.DEFAULT;
        }
        this.accessibleElement = chunk;
    }

    PdfChunk(Chunk chunk, PdfAction action, TabSettings tabSettings) {
        this(chunk, action);
        if (tabSettings != null && this.attributes.get(Chunk.TABSETTINGS) == null) {
            this.attributes.put(Chunk.TABSETTINGS, tabSettings);
        }
    }

    public int getUnicodeEquivalent(int c) {
        return this.baseFont.getUnicodeEquivalent(c);
    }

    protected int getWord(String text, int start) {
        int len = text.length();
        while (start < len && Character.isLetter(text.charAt(start))) {
            start++;
        }
        return start;
    }

    PdfChunk split(float width) {
        this.newlineSplit = false;
        if (this.image == null) {
            String returnValue;
            HyphenationEvent hyphenationEvent = (HyphenationEvent) this.noStroke.get(Chunk.HYPHENATION);
            int currentPosition = 0;
            int splitPosition = -1;
            float currentWidth = 0.0f;
            int lastSpace = -1;
            float lastSpaceWidth = 0.0f;
            int length = this.value.length();
            char[] valueArray = this.value.toCharArray();
            BaseFont ft = this.font.getFont();
            char character;
            if (ft.getFontType() != 2 || ft.getUnicodeEquivalent(32) == 32) {
                while (currentPosition < length) {
                    character = valueArray[currentPosition];
                    if (character == '\r' || character == '\n') {
                        this.newlineSplit = true;
                        int inc = 1;
                        if (character == '\r' && currentPosition + 1 < length && valueArray[currentPosition + 1] == '\n') {
                            inc = 2;
                        }
                        returnValue = this.value.substring(currentPosition + inc);
                        this.value = this.value.substring(0, currentPosition);
                        if (this.value.length() < 1) {
                            this.value = " ";
                        }
                        return new PdfChunk(returnValue, this);
                    }
                    boolean surrogate = Utilities.isSurrogatePair(valueArray, currentPosition);
                    if (surrogate) {
                        currentWidth += getCharWidth(Utilities.convertToUtf32(valueArray[currentPosition], valueArray[currentPosition + 1]));
                    } else {
                        currentWidth += getCharWidth(character);
                    }
                    if (character == ' ') {
                        lastSpace = currentPosition + 1;
                        lastSpaceWidth = currentWidth;
                    }
                    if (surrogate) {
                        currentPosition++;
                    }
                    if (currentWidth > width) {
                        break;
                    }
                    if (this.splitCharacter.isSplitCharacter(0, currentPosition, length, valueArray, null)) {
                        splitPosition = currentPosition + 1;
                    }
                    currentPosition++;
                }
            } else {
                while (currentPosition < length) {
                    char cidChar = valueArray[currentPosition];
                    character = (char) ft.getUnicodeEquivalent(cidChar);
                    if (character == '\n') {
                        this.newlineSplit = true;
                        returnValue = this.value.substring(currentPosition + 1);
                        this.value = this.value.substring(0, currentPosition);
                        if (this.value.length() < 1) {
                            this.value = "\u0001";
                        }
                        return new PdfChunk(returnValue, this);
                    }
                    currentWidth += getCharWidth(cidChar);
                    if (character == ' ') {
                        lastSpace = currentPosition + 1;
                        lastSpaceWidth = currentWidth;
                    }
                    if (currentWidth > width) {
                        break;
                    }
                    if (this.splitCharacter.isSplitCharacter(0, currentPosition, length, valueArray, thisChunk)) {
                        splitPosition = currentPosition + 1;
                    }
                    currentPosition++;
                }
            }
            if (currentPosition == length) {
                return null;
            }
            if (splitPosition < 0) {
                returnValue = this.value;
                this.value = "";
                return new PdfChunk(returnValue, this);
            }
            if (lastSpace > splitPosition && this.splitCharacter.isSplitCharacter(0, 0, 1, singleSpace, null)) {
                splitPosition = lastSpace;
            }
            if (hyphenationEvent != null && lastSpace >= 0 && lastSpace < currentPosition) {
                int wordIdx = getWord(this.value, lastSpace);
                if (wordIdx > lastSpace) {
                    String pre = hyphenationEvent.getHyphenatedWordPre(this.value.substring(lastSpace, wordIdx), this.font.getFont(), this.font.size(), width - lastSpaceWidth);
                    String post = hyphenationEvent.getHyphenatedWordPost();
                    if (pre.length() > 0) {
                        returnValue = post + this.value.substring(wordIdx);
                        this.value = trim(this.value.substring(0, lastSpace) + pre);
                        return new PdfChunk(returnValue, this);
                    }
                }
            }
            returnValue = this.value.substring(splitPosition);
            this.value = trim(this.value.substring(0, splitPosition));
            return new PdfChunk(returnValue, this);
        } else if (this.image.getScaledWidth() <= width) {
            return null;
        } else {
            PdfChunk pdfChunk = new PdfChunk(Chunk.OBJECT_REPLACEMENT_CHARACTER, this);
            this.value = "";
            this.attributes = new HashMap();
            this.image = null;
            this.font = PdfFont.getDefaultFont();
            return pdfChunk;
        }
    }

    PdfChunk truncate(float width) {
        if (this.image == null) {
            int currentPosition = 0;
            float currentWidth = 0.0f;
            String returnValue;
            if (width < this.font.width()) {
                returnValue = this.value.substring(1);
                this.value = this.value.substring(0, 1);
                return new PdfChunk(returnValue, this);
            }
            int length = this.value.length();
            boolean surrogate = false;
            while (currentPosition < length) {
                surrogate = Utilities.isSurrogatePair(this.value, currentPosition);
                if (surrogate) {
                    currentWidth += getCharWidth(Utilities.convertToUtf32(this.value, currentPosition));
                } else {
                    currentWidth += getCharWidth(this.value.charAt(currentPosition));
                }
                if (currentWidth > width) {
                    break;
                }
                if (surrogate) {
                    currentPosition++;
                }
                currentPosition++;
            }
            if (currentPosition == length) {
                return null;
            }
            if (currentPosition == 0) {
                currentPosition = 1;
                if (surrogate) {
                    currentPosition = 1 + 1;
                }
            }
            returnValue = this.value.substring(currentPosition);
            this.value = this.value.substring(0, currentPosition);
            return new PdfChunk(returnValue, this);
        } else if (this.image.getScaledWidth() <= width) {
            return null;
        } else {
            if (this.image.isScaleToFitLineWhenOverflow()) {
                setImageScalePercentage(width / this.image.getWidth());
                return null;
            }
            PdfChunk pc = new PdfChunk("", this);
            this.value = "";
            this.attributes.remove(Chunk.IMAGE);
            this.image = null;
            this.font = PdfFont.getDefaultFont();
            return pc;
        }
    }

    PdfFont font() {
        return this.font;
    }

    BaseColor color() {
        return (BaseColor) this.noStroke.get(Chunk.COLOR);
    }

    float width() {
        return width(this.value);
    }

    float width(String str) {
        if (isAttribute(Chunk.SEPARATOR)) {
            return 0.0f;
        }
        if (isImage()) {
            return getImageWidth();
        }
        float width = this.font.width(str);
        if (isAttribute(Chunk.CHAR_SPACING)) {
            width += ((float) str.length()) * ((Float) getAttribute(Chunk.CHAR_SPACING)).floatValue();
        }
        if (!isAttribute(Chunk.WORD_SPACING)) {
            return width;
        }
        int numberOfSpaces = 0;
        int idx = -1;
        while (true) {
            idx = str.indexOf(32, idx + 1);
            if (idx < 0) {
                return width + (((float) numberOfSpaces) * ((Float) getAttribute(Chunk.WORD_SPACING)).floatValue());
            }
            numberOfSpaces++;
        }
    }

    float height() {
        if (isImage()) {
            return getImageHeight();
        }
        return this.font.size();
    }

    public boolean isNewlineSplit() {
        return this.newlineSplit;
    }

    public float getWidthCorrected(float charSpacing, float wordSpacing) {
        if (this.image != null) {
            return this.image.getScaledWidth() + charSpacing;
        }
        int numberOfSpaces = 0;
        int idx = -1;
        while (true) {
            idx = this.value.indexOf(32, idx + 1);
            if (idx < 0) {
                return (this.font.width(this.value) + (((float) this.value.length()) * charSpacing)) + (((float) numberOfSpaces) * wordSpacing);
            }
            numberOfSpaces++;
        }
    }

    public float getTextRise() {
        Float f = (Float) getAttribute(Chunk.SUBSUPSCRIPT);
        if (f != null) {
            return f.floatValue();
        }
        return 0.0f;
    }

    public float trimLastSpace() {
        BaseFont ft = this.font.getFont();
        if (ft.getFontType() != 2 || ft.getUnicodeEquivalent(32) == 32) {
            if (this.value.length() > 1 && this.value.endsWith(" ")) {
                this.value = this.value.substring(0, this.value.length() - 1);
                return this.font.width(32);
            }
        } else if (this.value.length() > 1 && this.value.endsWith("\u0001")) {
            this.value = this.value.substring(0, this.value.length() - 1);
            return this.font.width(1);
        }
        return 0.0f;
    }

    public float trimFirstSpace() {
        BaseFont ft = this.font.getFont();
        if (ft.getFontType() != 2 || ft.getUnicodeEquivalent(32) == 32) {
            if (this.value.length() > 1 && this.value.startsWith(" ")) {
                this.value = this.value.substring(1);
                return this.font.width(32);
            }
        } else if (this.value.length() > 1 && this.value.startsWith("\u0001")) {
            this.value = this.value.substring(1);
            return this.font.width(1);
        }
        return 0.0f;
    }

    Object getAttribute(String name) {
        if (this.attributes.containsKey(name)) {
            return this.attributes.get(name);
        }
        return this.noStroke.get(name);
    }

    boolean isAttribute(String name) {
        if (this.attributes.containsKey(name)) {
            return true;
        }
        return this.noStroke.containsKey(name);
    }

    boolean isStroked() {
        return !this.attributes.isEmpty();
    }

    boolean isSeparator() {
        return isAttribute(Chunk.SEPARATOR);
    }

    boolean isHorizontalSeparator() {
        if (!isAttribute(Chunk.SEPARATOR)) {
            return false;
        }
        if (((Boolean) ((Object[]) getAttribute(Chunk.SEPARATOR))[1]).booleanValue()) {
            return false;
        }
        return true;
    }

    boolean isTab() {
        return isAttribute(Chunk.TAB);
    }

    @Deprecated
    void adjustLeft(float newValue) {
        if (((Object[]) this.attributes.get(Chunk.TAB)) != null) {
            this.attributes.put(Chunk.TAB, new Object[]{o[0], o[1], o[2], new Float(newValue)});
        }
    }

    static TabStop getTabStop(PdfChunk tab, float tabPosition) {
        Object[] o = (Object[]) tab.attributes.get(Chunk.TAB);
        if (o == null) {
            return null;
        }
        Float tabInterval = o[0];
        if (Float.isNaN(tabInterval.floatValue())) {
            return TabSettings.getTabStopNewInstance(tabPosition, (TabSettings) tab.attributes.get(Chunk.TABSETTINGS));
        }
        return TabStop.newInstance(tabPosition, tabInterval.floatValue());
    }

    TabStop getTabStop() {
        return (TabStop) this.attributes.get(TABSTOP);
    }

    void setTabStop(TabStop tabStop) {
        this.attributes.put(TABSTOP, tabStop);
    }

    boolean isImage() {
        return this.image != null;
    }

    Image getImage() {
        return this.image;
    }

    float getImageHeight() {
        return this.image.getScaledHeight() * this.imageScalePercentage;
    }

    float getImageWidth() {
        return this.image.getScaledWidth() * this.imageScalePercentage;
    }

    public float getImageScalePercentage() {
        return this.imageScalePercentage;
    }

    public void setImageScalePercentage(float imageScalePercentage) {
        this.imageScalePercentage = imageScalePercentage;
    }

    void setImageOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    float getImageOffsetX() {
        return this.offsetX;
    }

    void setImageOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    float getImageOffsetY() {
        return this.offsetY;
    }

    void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    boolean isSpecialEncoding() {
        return this.encoding.equals("UnicodeBigUnmarked") || this.encoding.equals(BaseFont.IDENTITY_H);
    }

    String getEncoding() {
        return this.encoding;
    }

    int length() {
        return this.value.length();
    }

    int lengthUtf32() {
        if (!BaseFont.IDENTITY_H.equals(this.encoding)) {
            return this.value.length();
        }
        int total = 0;
        int len = this.value.length();
        int k = 0;
        while (k < len) {
            if (Utilities.isSurrogateHigh(this.value.charAt(k))) {
                k++;
            }
            total++;
            k++;
        }
        return total;
    }

    boolean isExtSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        return this.splitCharacter.isSplitCharacter(start, current, end, cc, ck);
    }

    String trim(String string) {
        BaseFont ft = this.font.getFont();
        if (ft.getFontType() != 2 || ft.getUnicodeEquivalent(32) == 32) {
            while (true) {
                if (!string.endsWith(" ") && !string.endsWith("\t")) {
                    break;
                }
                string = string.substring(0, string.length() - 1);
            }
        } else {
            while (string.endsWith("\u0001")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }

    public boolean changeLeading() {
        return this.changeLeading;
    }

    public float getLeading() {
        return this.leading;
    }

    float getCharWidth(int c) {
        if (noPrint(c)) {
            return 0.0f;
        }
        if (isAttribute(Chunk.CHAR_SPACING)) {
            return this.font.width(c) + (((Float) getAttribute(Chunk.CHAR_SPACING)).floatValue() * this.font.getHorizontalScaling());
        } else if (isImage()) {
            return getImageWidth();
        } else {
            return this.font.width(c);
        }
    }

    public static boolean noPrint(int c) {
        return (c >= 8203 && c <= 8207) || (c >= 8234 && c <= 8238);
    }
}
