package com.itextpdf.text.pdf.hyphenation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class HyphenationTree extends TernaryTree implements PatternConsumer {
    private static final long serialVersionUID = -7763254239309429432L;
    protected TernaryTree classmap = new TernaryTree();
    private transient TernaryTree ivalues;
    protected HashMap<String, ArrayList<Object>> stoplist = new HashMap(23);
    protected ByteVector vspace = new ByteVector();

    public HyphenationTree() {
        this.vspace.alloc(1);
    }

    protected int packValues(String values) {
        int n = values.length();
        int m = (n & 1) == 1 ? (n >> 1) + 2 : (n >> 1) + 1;
        int offset = this.vspace.alloc(m);
        byte[] va = this.vspace.getArray();
        for (int i = 0; i < n; i++) {
            int j = i >> 1;
            byte v = (byte) (((values.charAt(i) - 48) + 1) & 15);
            if ((i & 1) == 1) {
                va[j + offset] = (byte) (va[j + offset] | v);
            } else {
                va[j + offset] = (byte) (v << 4);
            }
        }
        va[(m - 1) + offset] = (byte) 0;
        return offset;
    }

    protected String unpackValues(int k) {
        StringBuffer buf = new StringBuffer();
        int k2 = k + 1;
        byte v = this.vspace.get(k);
        while (v != (byte) 0) {
            buf.append((char) (((v >>> 4) - 1) + 48));
            char c = (char) (v & 15);
            if (c == '\u0000') {
                break;
            }
            buf.append((char) ((c - 1) + 48));
            k = k2 + 1;
            v = this.vspace.get(k2);
            k2 = k;
        }
        return buf.toString();
    }

    public void loadSimplePatterns(InputStream stream) {
        SimplePatternParser pp = new SimplePatternParser();
        this.ivalues = new TernaryTree();
        pp.parse(stream, this);
        trimToSize();
        this.vspace.trimToSize();
        this.classmap.trimToSize();
        this.ivalues = null;
    }

    public String findPattern(String pat) {
        int k = super.find(pat);
        if (k >= 0) {
            return unpackValues(k);
        }
        return "";
    }

    protected int hstrcmp(char[] s, int si, char[] t, int ti) {
        while (s[si] == t[ti]) {
            if (s[si] == '\u0000') {
                return 0;
            }
            si++;
            ti++;
        }
        if (t[ti] != '\u0000') {
            return s[si] - t[ti];
        }
        return 0;
    }

    protected byte[] getValues(int k) {
        StringBuffer buf = new StringBuffer();
        int k2 = k + 1;
        byte v = this.vspace.get(k);
        while (v != (byte) 0) {
            buf.append((char) ((v >>> 4) - 1));
            char c = (char) (v & 15);
            if (c == '\u0000') {
                break;
            }
            buf.append((char) (c - 1));
            k = k2 + 1;
            v = this.vspace.get(k2);
            k2 = k;
        }
        byte[] res = new byte[buf.length()];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) buf.charAt(i);
        }
        return res;
    }

    protected void searchPatterns(char[] word, int index, byte[] il) {
        int i = index;
        char sp = word[i];
        char p = this.root;
        while (p > '\u0000' && p < this.sc.length) {
            int j;
            if (this.sc[p] != '￿') {
                int d = sp - this.sc[p];
                if (d != 0) {
                    p = d < 0 ? this.lo[p] : this.hi[p];
                } else if (sp != '\u0000') {
                    i++;
                    sp = word[i];
                    p = this.eq[p];
                    char q = p;
                    while (q > '\u0000' && q < this.sc.length && this.sc[q] != '￿') {
                        if (this.sc[q] == '\u0000') {
                            j = index;
                            for (byte value : getValues(this.eq[q])) {
                                if (j < il.length && value > il[j]) {
                                    il[j] = value;
                                }
                                j++;
                            }
                        } else {
                            q = this.lo[q];
                        }
                    }
                } else {
                    return;
                }
            } else if (hstrcmp(word, i, this.kv.getArray(), this.lo[p]) == 0) {
                j = index;
                for (byte value2 : getValues(this.eq[p])) {
                    if (j < il.length && value2 > il[j]) {
                        il[j] = value2;
                    }
                    j++;
                }
                return;
            } else {
                return;
            }
        }
    }

    public Hyphenation hyphenate(String word, int remainCharCount, int pushCharCount) {
        char[] w = word.toCharArray();
        return hyphenate(w, 0, w.length, remainCharCount, pushCharCount);
    }

    public Hyphenation hyphenate(char[] w, int offset, int len, int remainCharCount, int pushCharCount) {
        int i;
        char[] word = new char[(len + 3)];
        char[] c = new char[2];
        int iIgnoreAtBeginning = 0;
        int iLength = len;
        boolean bEndOfLetters = false;
        for (i = 1; i <= len; i++) {
            c[0] = w[(offset + i) - 1];
            int nc = this.classmap.find(c, 0);
            if (nc < 0) {
                if (i == iIgnoreAtBeginning + 1) {
                    iIgnoreAtBeginning++;
                } else {
                    bEndOfLetters = true;
                }
                iLength--;
            } else if (bEndOfLetters) {
                return null;
            } else {
                word[i - iIgnoreAtBeginning] = (char) nc;
            }
        }
        int origlen = len;
        len = iLength;
        if (len < remainCharCount + pushCharCount) {
            return null;
        }
        Object result = new int[(len + 1)];
        int k = 0;
        String str = new String(word, 1, len);
        int k2;
        if (this.stoplist.containsKey(str)) {
            ArrayList<Object> hw = (ArrayList) this.stoplist.get(str);
            int j = 0;
            for (i = 0; i < hw.size(); i++) {
                Object o = hw.get(i);
                if (o instanceof String) {
                    j += ((String) o).length();
                    if (j >= remainCharCount && j < len - pushCharCount) {
                        k2 = k + 1;
                        result[k] = j + iIgnoreAtBeginning;
                        k = k2;
                    }
                }
            }
        } else {
            word[0] = '.';
            word[len + 1] = '.';
            word[len + 2] = '\u0000';
            byte[] il = new byte[(len + 3)];
            for (i = 0; i < len + 1; i++) {
                searchPatterns(word, i, il);
            }
            i = 0;
            k2 = 0;
            while (i < len) {
                if ((il[i + 1] & 1) != 1 || i < remainCharCount || i > len - pushCharCount) {
                    k = k2;
                } else {
                    k = k2 + 1;
                    result[k2] = i + iIgnoreAtBeginning;
                }
                i++;
                k2 = k;
            }
            k = k2;
        }
        if (k <= 0) {
            return null;
        }
        Object res = new int[k];
        System.arraycopy(result, 0, res, 0, k);
        return new Hyphenation(new String(w, offset, origlen), res);
    }

    public void addClass(String chargroup) {
        if (chargroup.length() > 0) {
            char equivChar = chargroup.charAt(0);
            char[] key = new char[2];
            key[1] = '\u0000';
            for (int i = 0; i < chargroup.length(); i++) {
                key[0] = chargroup.charAt(i);
                this.classmap.insert(key, 0, equivChar);
            }
        }
    }

    public void addException(String word, ArrayList<Object> hyphenatedword) {
        this.stoplist.put(word, hyphenatedword);
    }

    public void addPattern(String pattern, String ivalue) {
        int k = this.ivalues.find(ivalue);
        if (k <= 0) {
            k = packValues(ivalue);
            this.ivalues.insert(ivalue, (char) k);
        }
        insert(pattern, (char) k);
    }

    public void printStats() {
        System.out.println("Value space size = " + Integer.toString(this.vspace.length()));
        super.printStats();
    }
}
