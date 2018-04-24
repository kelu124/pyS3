package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.util.ArrayList;

public class FontSelector {
    protected Font currentFont = null;
    protected ArrayList<Font> fonts = new ArrayList();

    public void addFont(Font font) {
        if (font.getBaseFont() != null) {
            this.fonts.add(font);
            return;
        }
        this.fonts.add(new Font(font.getCalculatedBaseFont(true), font.getSize(), font.getCalculatedStyle(), font.getColor()));
    }

    public Phrase process(String text) {
        if (this.fonts.size() == 0) {
            throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage("no.font.is.defined", new Object[0]));
        }
        char[] cc = text.toCharArray();
        int len = cc.length;
        StringBuffer sb = new StringBuffer();
        Phrase ret = new Phrase();
        this.currentFont = null;
        for (int k = 0; k < len; k++) {
            Element newChunk = processChar(cc, k, sb);
            if (newChunk != null) {
                ret.add(newChunk);
            }
        }
        if (sb.length() > 0) {
            ret.add(new Chunk(sb.toString(), this.currentFont != null ? this.currentFont : (Font) this.fonts.get(0)));
        }
        return ret;
    }

    protected Chunk processChar(char[] cc, int k, StringBuffer sb) {
        Chunk newChunk = null;
        char c = cc[k];
        if (c == '\n' || c == '\r') {
            sb.append(c);
        } else if (Utilities.isSurrogatePair(cc, k)) {
            int u = Utilities.convertToUtf32(cc, k);
            f = 0;
            while (f < this.fonts.size()) {
                font = (Font) this.fonts.get(f);
                if (font.getBaseFont().charExists(u) || Character.getType(u) == 16) {
                    if (this.currentFont != font) {
                        if (sb.length() > 0 && this.currentFont != null) {
                            newChunk = new Chunk(sb.toString(), this.currentFont);
                            sb.setLength(0);
                        }
                        this.currentFont = font;
                    }
                    sb.append(c);
                    sb.append(cc[k + 1]);
                } else {
                    f++;
                }
            }
        } else {
            f = 0;
            while (f < this.fonts.size()) {
                font = (Font) this.fonts.get(f);
                if (font.getBaseFont().charExists(c) || Character.getType(c) == 16) {
                    if (this.currentFont != font) {
                        if (sb.length() > 0 && this.currentFont != null) {
                            newChunk = new Chunk(sb.toString(), this.currentFont);
                            sb.setLength(0);
                        }
                        this.currentFont = font;
                    }
                    sb.append(c);
                } else {
                    f++;
                }
            }
        }
        return newChunk;
    }
}
