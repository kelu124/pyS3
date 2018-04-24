package com.itextpdf.text.pdf;

import com.itextpdf.text.SplitCharacter;

public class DefaultSplitCharacter implements SplitCharacter {
    public static final SplitCharacter DEFAULT = new DefaultSplitCharacter();
    protected char[] characters;

    public DefaultSplitCharacter(char character) {
        this(new char[]{character});
    }

    public DefaultSplitCharacter(char[] characters) {
        this.characters = characters;
    }

    public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        char c = getCurrentCharacter(current, cc, ck);
        if (this.characters != null) {
            for (char c2 : this.characters) {
                if (c == c2) {
                    return true;
                }
            }
            return false;
        } else if (c <= ' ' || c == '-' || c == '‐') {
            return true;
        } else {
            if (c < ' ') {
                return false;
            }
            if (c >= ' ' && c <= '​') {
                return true;
            }
            if (c >= '⺀' && c < '힠') {
                return true;
            }
            if (c >= '豈' && c < 'ﬀ') {
                return true;
            }
            if (c >= '︰' && c < '﹐') {
                return true;
            }
            if (c < '｡' || c >= 'ﾠ') {
                return false;
            }
            return true;
        }
    }

    protected char getCurrentCharacter(int current, char[] cc, PdfChunk[] ck) {
        if (ck == null) {
            return cc[current];
        }
        return (char) ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
    }
}
