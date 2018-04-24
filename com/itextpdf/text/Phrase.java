package com.itextpdf.text;

import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.HyphenationEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Phrase extends ArrayList<Element> implements TextElementArray {
    private static final long serialVersionUID = 2643594602455068231L;
    protected Font font;
    protected HyphenationEvent hyphenation;
    protected float leading;
    protected float multipliedLeading;
    protected TabSettings tabSettings;

    public Phrase() {
        this(16.0f);
    }

    public Phrase(Phrase phrase) {
        this.leading = Float.NaN;
        this.multipliedLeading = 0.0f;
        this.hyphenation = null;
        this.tabSettings = null;
        addAll(phrase);
        setLeading(phrase.getLeading(), phrase.getMultipliedLeading());
        this.font = phrase.getFont();
        this.tabSettings = phrase.getTabSettings();
        setHyphenation(phrase.getHyphenation());
    }

    public Phrase(float leading) {
        this.leading = Float.NaN;
        this.multipliedLeading = 0.0f;
        this.hyphenation = null;
        this.tabSettings = null;
        this.leading = leading;
        this.font = new Font();
    }

    public Phrase(Chunk chunk) {
        this.leading = Float.NaN;
        this.multipliedLeading = 0.0f;
        this.hyphenation = null;
        this.tabSettings = null;
        super.add(chunk);
        this.font = chunk.getFont();
        setHyphenation(chunk.getHyphenation());
    }

    public Phrase(float leading, Chunk chunk) {
        this.leading = Float.NaN;
        this.multipliedLeading = 0.0f;
        this.hyphenation = null;
        this.tabSettings = null;
        this.leading = leading;
        super.add(chunk);
        this.font = chunk.getFont();
        setHyphenation(chunk.getHyphenation());
    }

    public Phrase(String string) {
        this(Float.NaN, string, new Font());
    }

    public Phrase(String string, Font font) {
        this(Float.NaN, string, font);
    }

    public Phrase(float leading, String string) {
        this(leading, string, new Font());
    }

    public Phrase(float leading, String string, Font font) {
        this.leading = Float.NaN;
        this.multipliedLeading = 0.0f;
        this.hyphenation = null;
        this.tabSettings = null;
        this.leading = leading;
        this.font = font;
        if (string != null && string.length() != 0) {
            super.add(new Chunk(string, font));
        }
    }

    public boolean process(ElementListener listener) {
        try {
            Iterator i$ = iterator();
            while (i$.hasNext()) {
                listener.add((Element) i$.next());
            }
            return true;
        } catch (DocumentException e) {
            return false;
        }
    }

    public int type() {
        return 11;
    }

    public List<Chunk> getChunks() {
        List<Chunk> tmp = new ArrayList();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            tmp.addAll(((Element) i$.next()).getChunks());
        }
        return tmp;
    }

    public boolean isContent() {
        return true;
    }

    public boolean isNestable() {
        return true;
    }

    public void add(int index, Element element) {
        if (element != null) {
            switch (element.type()) {
                case 10:
                    Chunk chunk = (Chunk) element;
                    if (!this.font.isStandardFont()) {
                        chunk.setFont(this.font.difference(chunk.getFont()));
                    }
                    if (!(this.hyphenation == null || chunk.getHyphenation() != null || chunk.isEmpty())) {
                        chunk.setHyphenation(this.hyphenation);
                    }
                    super.add(index, chunk);
                    return;
                case 11:
                case 12:
                case 14:
                case 17:
                case 23:
                case 29:
                case 37:
                case 50:
                case 55:
                case Element.WRITABLE_DIRECT /*666*/:
                    super.add(index, element);
                    return;
                default:
                    throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", element.getClass().getName()));
            }
        }
    }

    public boolean add(String s) {
        if (s == null) {
            return false;
        }
        return super.add(new Chunk(s, this.font));
    }

    public boolean add(Element element) {
        if (element == null) {
            return false;
        }
        try {
            switch (element.type()) {
                case 10:
                    return addChunk((Chunk) element);
                case 11:
                case 12:
                    boolean success = true;
                    Iterator i$ = ((Phrase) element).iterator();
                    while (i$.hasNext()) {
                        Element e = (Element) i$.next();
                        if (e instanceof Chunk) {
                            success &= addChunk((Chunk) e);
                        } else {
                            success &= add(e);
                        }
                    }
                    return success;
                case 14:
                case 17:
                case 23:
                case 29:
                case 37:
                case 50:
                case 55:
                case Element.WRITABLE_DIRECT /*666*/:
                    return super.add(element);
                default:
                    throw new ClassCastException(String.valueOf(element.type()));
            }
        } catch (ClassCastException cce) {
            throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", cce.getMessage()));
        }
        throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", cce.getMessage()));
    }

    public boolean addAll(Collection<? extends Element> collection) {
        for (Element e : collection) {
            add(e);
        }
        return true;
    }

    protected boolean addChunk(Chunk chunk) {
        Font f = chunk.getFont();
        String c = chunk.getContent();
        if (!(this.font == null || this.font.isStandardFont())) {
            f = this.font.difference(chunk.getFont());
        }
        if (size() > 0 && !chunk.hasAttributes()) {
            try {
                Chunk previous = (Chunk) get(size() - 1);
                if (!(previous.hasAttributes() || ((f != null && f.compareTo(previous.getFont()) != 0) || "".equals(previous.getContent().trim()) || "".equals(c.trim())))) {
                    previous.append(c);
                    return true;
                }
            } catch (ClassCastException e) {
            }
        }
        Chunk newChunk = new Chunk(c, f);
        newChunk.setAttributes(chunk.getAttributes());
        newChunk.role = chunk.getRole();
        newChunk.accessibleAttributes = chunk.getAccessibleAttributes();
        if (!(this.hyphenation == null || newChunk.getHyphenation() != null || newChunk.isEmpty())) {
            newChunk.setHyphenation(this.hyphenation);
        }
        return super.add(newChunk);
    }

    protected void addSpecial(Element object) {
        super.add(object);
    }

    public void setLeading(float fixedLeading, float multipliedLeading) {
        this.leading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
    }

    public void setLeading(float fixedLeading) {
        this.leading = fixedLeading;
        this.multipliedLeading = 0.0f;
    }

    public void setMultipliedLeading(float multipliedLeading) {
        this.leading = 0.0f;
        this.multipliedLeading = multipliedLeading;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public float getLeading() {
        if (!Float.isNaN(this.leading) || this.font == null) {
            return this.leading;
        }
        return this.font.getCalculatedLeading(1.5f);
    }

    public float getMultipliedLeading() {
        return this.multipliedLeading;
    }

    public float getTotalLeading() {
        float m = this.font == null ? HtmlUtilities.DEFAULT_FONT_SIZE * this.multipliedLeading : this.font.getCalculatedLeading(this.multipliedLeading);
        return (m <= 0.0f || hasLeading()) ? m + getLeading() : m;
    }

    public boolean hasLeading() {
        if (Float.isNaN(this.leading)) {
            return false;
        }
        return true;
    }

    public Font getFont() {
        return this.font;
    }

    public String getContent() {
        StringBuffer buf = new StringBuffer();
        for (Chunk c : getChunks()) {
            buf.append(c.toString());
        }
        return buf.toString();
    }

    public boolean isEmpty() {
        switch (size()) {
            case 0:
                return true;
            case 1:
                Element element = (Element) get(0);
                if (element.type() == 10 && ((Chunk) element).isEmpty()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    public HyphenationEvent getHyphenation() {
        return this.hyphenation;
    }

    public void setHyphenation(HyphenationEvent hyphenation) {
        this.hyphenation = hyphenation;
    }

    public TabSettings getTabSettings() {
        return this.tabSettings;
    }

    public void setTabSettings(TabSettings tabSettings) {
        this.tabSettings = tabSettings;
    }

    private Phrase(boolean dummy) {
        this.leading = Float.NaN;
        this.multipliedLeading = 0.0f;
        this.hyphenation = null;
        this.tabSettings = null;
    }

    public static final Phrase getInstance(String string) {
        return getInstance(16, string, new Font());
    }

    public static final Phrase getInstance(int leading, String string) {
        return getInstance(leading, string, new Font());
    }

    public static final Phrase getInstance(int leading, String string, Font font) {
        Phrase p = new Phrase(true);
        p.setLeading((float) leading);
        p.font = font;
        if (font.getFamily() != FontFamily.SYMBOL && font.getFamily() != FontFamily.ZAPFDINGBATS && font.getBaseFont() == null) {
            while (true) {
                int index = SpecialSymbol.index(string);
                if (index <= -1) {
                    break;
                }
                if (index > 0) {
                    p.add(new Chunk(string.substring(0, index), font));
                    string = string.substring(index);
                }
                Font symbol = new Font(FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
                StringBuffer buf = new StringBuffer();
                buf.append(SpecialSymbol.getCorrespondingSymbol(string.charAt(0)));
                string = string.substring(1);
                while (SpecialSymbol.index(string) == 0) {
                    buf.append(SpecialSymbol.getCorrespondingSymbol(string.charAt(0)));
                    string = string.substring(1);
                }
                p.add(new Chunk(buf.toString(), symbol));
            }
        }
        if (!(string == null || string.length() == 0)) {
            p.add(new Chunk(string, font));
        }
        return p;
    }

    public boolean trim() {
        while (size() > 0) {
            Element firstChunk = (Element) get(0);
            if (!(firstChunk instanceof Chunk) || !((Chunk) firstChunk).isWhitespace()) {
                break;
            }
            remove(firstChunk);
        }
        while (size() > 0) {
            Element lastChunk = (Element) get(size() - 1);
            if (!(lastChunk instanceof Chunk) || !((Chunk) lastChunk).isWhitespace()) {
                break;
            }
            remove(lastChunk);
        }
        return size() > 0;
    }
}
