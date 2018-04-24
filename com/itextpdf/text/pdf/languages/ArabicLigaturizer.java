package com.itextpdf.text.pdf.languages;

import com.itextpdf.text.pdf.BidiLine;
import com.itextpdf.text.pdf.BidiOrder;
import java.util.HashMap;
import org.bytedeco.javacpp.opencv_videoio;

public class ArabicLigaturizer implements LanguageProcessor {
    private static final char ALEF = 'ا';
    private static final char ALEFHAMZA = 'أ';
    private static final char ALEFHAMZABELOW = 'إ';
    private static final char ALEFMADDA = 'آ';
    private static final char ALEFMAKSURA = 'ى';
    private static final char DAMMA = 'ُ';
    public static final int DIGITS_AN2EN = 64;
    public static final int DIGITS_EN2AN = 32;
    public static final int DIGITS_EN2AN_INIT_AL = 128;
    public static final int DIGITS_EN2AN_INIT_LR = 96;
    public static final int DIGITS_MASK = 224;
    private static final int DIGITS_RESERVED = 160;
    public static final int DIGIT_TYPE_AN = 0;
    public static final int DIGIT_TYPE_AN_EXTENDED = 256;
    public static final int DIGIT_TYPE_MASK = 256;
    private static final char FARSIYEH = 'ی';
    private static final char FATHA = 'َ';
    private static final char HAMZA = 'ء';
    private static final char HAMZAABOVE = 'ٔ';
    private static final char HAMZABELOW = 'ٕ';
    private static final char KASRA = 'ِ';
    private static final char LAM = 'ل';
    private static final char LAM_ALEF = 'ﻻ';
    private static final char LAM_ALEFHAMZA = 'ﻷ';
    private static final char LAM_ALEFHAMZABELOW = 'ﻹ';
    private static final char LAM_ALEFMADDA = 'ﻵ';
    private static final char MADDA = 'ٓ';
    private static final char SHADDA = 'ّ';
    private static final char TATWEEL = 'ـ';
    private static final char WAW = 'و';
    private static final char WAWHAMZA = 'ؤ';
    private static final char YEH = 'ي';
    private static final char YEHHAMZA = 'ئ';
    private static final char ZWJ = '‍';
    public static final int ar_composedtashkeel = 4;
    public static final int ar_lig = 8;
    public static final int ar_nothing = 0;
    public static final int ar_novowel = 1;
    private static final char[][] chartable = new char[][]{new char[]{HAMZA, 'ﺀ'}, new char[]{ALEFMADDA, 'ﺁ', 'ﺂ'}, new char[]{ALEFHAMZA, 'ﺃ', 'ﺄ'}, new char[]{WAWHAMZA, 'ﺅ', 'ﺆ'}, new char[]{ALEFHAMZABELOW, 'ﺇ', 'ﺈ'}, new char[]{YEHHAMZA, 'ﺉ', 'ﺊ', 'ﺋ', 'ﺌ'}, new char[]{ALEF, 'ﺍ', 'ﺎ'}, new char[]{'ب', 'ﺏ', 'ﺐ', 'ﺑ', 'ﺒ'}, new char[]{'ة', 'ﺓ', 'ﺔ'}, new char[]{'ت', 'ﺕ', 'ﺖ', 'ﺗ', 'ﺘ'}, new char[]{'ث', 'ﺙ', 'ﺚ', 'ﺛ', 'ﺜ'}, new char[]{'ج', 'ﺝ', 'ﺞ', 'ﺟ', 'ﺠ'}, new char[]{'ح', 'ﺡ', 'ﺢ', 'ﺣ', 'ﺤ'}, new char[]{'خ', 'ﺥ', 'ﺦ', 'ﺧ', 'ﺨ'}, new char[]{'د', 'ﺩ', 'ﺪ'}, new char[]{'ذ', 'ﺫ', 'ﺬ'}, new char[]{'ر', 'ﺭ', 'ﺮ'}, new char[]{'ز', 'ﺯ', 'ﺰ'}, new char[]{'س', 'ﺱ', 'ﺲ', 'ﺳ', 'ﺴ'}, new char[]{'ش', 'ﺵ', 'ﺶ', 'ﺷ', 'ﺸ'}, new char[]{'ص', 'ﺹ', 'ﺺ', 'ﺻ', 'ﺼ'}, new char[]{'ض', 'ﺽ', 'ﺾ', 'ﺿ', 'ﻀ'}, new char[]{'ط', 'ﻁ', 'ﻂ', 'ﻃ', 'ﻄ'}, new char[]{'ظ', 'ﻅ', 'ﻆ', 'ﻇ', 'ﻈ'}, new char[]{'ع', 'ﻉ', 'ﻊ', 'ﻋ', 'ﻌ'}, new char[]{'غ', 'ﻍ', 'ﻎ', 'ﻏ', 'ﻐ'}, new char[]{TATWEEL, TATWEEL, TATWEEL, TATWEEL, TATWEEL}, new char[]{'ف', 'ﻑ', 'ﻒ', 'ﻓ', 'ﻔ'}, new char[]{'ق', 'ﻕ', 'ﻖ', 'ﻗ', 'ﻘ'}, new char[]{'ك', 'ﻙ', 'ﻚ', 'ﻛ', 'ﻜ'}, new char[]{LAM, 'ﻝ', 'ﻞ', 'ﻟ', 'ﻠ'}, new char[]{'م', 'ﻡ', 'ﻢ', 'ﻣ', 'ﻤ'}, new char[]{'ن', 'ﻥ', 'ﻦ', 'ﻧ', 'ﻨ'}, new char[]{'ه', 'ﻩ', 'ﻪ', 'ﻫ', 'ﻬ'}, new char[]{WAW, 'ﻭ', 'ﻮ'}, new char[]{ALEFMAKSURA, 'ﻯ', 'ﻰ', 'ﯨ', 'ﯩ'}, new char[]{YEH, 'ﻱ', 'ﻲ', 'ﻳ', 'ﻴ'}, new char[]{'ٱ', 'ﭐ', 'ﭑ'}, new char[]{'ٹ', 'ﭦ', 'ﭧ', 'ﭨ', 'ﭩ'}, new char[]{'ٺ', 'ﭞ', 'ﭟ', 'ﭠ', 'ﭡ'}, new char[]{'ٻ', 'ﭒ', 'ﭓ', 'ﭔ', 'ﭕ'}, new char[]{'پ', 'ﭖ', 'ﭗ', 'ﭘ', 'ﭙ'}, new char[]{'ٿ', 'ﭢ', 'ﭣ', 'ﭤ', 'ﭥ'}, new char[]{'ڀ', 'ﭚ', 'ﭛ', 'ﭜ', 'ﭝ'}, new char[]{'ڃ', 'ﭶ', 'ﭷ', 'ﭸ', 'ﭹ'}, new char[]{'ڄ', 'ﭲ', 'ﭳ', 'ﭴ', 'ﭵ'}, new char[]{'چ', 'ﭺ', 'ﭻ', 'ﭼ', 'ﭽ'}, new char[]{'ڇ', 'ﭾ', 'ﭿ', 'ﮀ', 'ﮁ'}, new char[]{'ڈ', 'ﮈ', 'ﮉ'}, new char[]{'ڌ', 'ﮄ', 'ﮅ'}, new char[]{'ڍ', 'ﮂ', 'ﮃ'}, new char[]{'ڎ', 'ﮆ', 'ﮇ'}, new char[]{'ڑ', 'ﮌ', 'ﮍ'}, new char[]{'ژ', 'ﮊ', 'ﮋ'}, new char[]{'ڤ', 'ﭪ', 'ﭫ', 'ﭬ', 'ﭭ'}, new char[]{'ڦ', 'ﭮ', 'ﭯ', 'ﭰ', 'ﭱ'}, new char[]{'ک', 'ﮎ', 'ﮏ', 'ﮐ', 'ﮑ'}, new char[]{'ڭ', 'ﯓ', 'ﯔ', 'ﯕ', 'ﯖ'}, new char[]{'گ', 'ﮒ', 'ﮓ', 'ﮔ', 'ﮕ'}, new char[]{'ڱ', 'ﮚ', 'ﮛ', 'ﮜ', 'ﮝ'}, new char[]{'ڳ', 'ﮖ', 'ﮗ', 'ﮘ', 'ﮙ'}, new char[]{'ں', 'ﮞ', 'ﮟ'}, new char[]{'ڻ', 'ﮠ', 'ﮡ', 'ﮢ', 'ﮣ'}, new char[]{'ھ', 'ﮪ', 'ﮫ', 'ﮬ', 'ﮭ'}, new char[]{'ۀ', 'ﮤ', 'ﮥ'}, new char[]{'ہ', 'ﮦ', 'ﮧ', 'ﮨ', 'ﮩ'}, new char[]{'ۅ', 'ﯠ', 'ﯡ'}, new char[]{'ۆ', 'ﯙ', 'ﯚ'}, new char[]{'ۇ', 'ﯗ', 'ﯘ'}, new char[]{'ۈ', 'ﯛ', 'ﯜ'}, new char[]{'ۉ', 'ﯢ', 'ﯣ'}, new char[]{'ۋ', 'ﯞ', 'ﯟ'}, new char[]{FARSIYEH, 'ﯼ', 'ﯽ', 'ﯾ', 'ﯿ'}, new char[]{'ې', 'ﯤ', 'ﯥ', 'ﯦ', 'ﯧ'}, new char[]{'ے', 'ﮮ', 'ﮯ'}, new char[]{'ۓ', 'ﮰ', 'ﮱ'}};
    private static HashMap<Character, char[]> maptable = new HashMap();
    protected int options = 0;
    protected int runDirection = 3;

    static class charstruct {
        char basechar;
        int lignum;
        char mark1;
        int numshapes = 1;
        char vowel;

        charstruct() {
        }
    }

    static {
        for (char[] c : chartable) {
            maptable.put(Character.valueOf(c[0]), c);
        }
    }

    static boolean isVowel(char s) {
        return (s >= 'ً' && s <= HAMZABELOW) || s == 'ٰ';
    }

    static char charshape(char s, int which) {
        if (s >= HAMZA && s <= 'ۓ') {
            char[] c = (char[]) maptable.get(Character.valueOf(s));
            if (c != null) {
                return c[which + 1];
            }
            return s;
        } else if (s < LAM_ALEFMADDA || s > LAM_ALEF) {
            return s;
        } else {
            return (char) (s + which);
        }
    }

    static int shapecount(char s) {
        if (s >= HAMZA && s <= 'ۓ' && !isVowel(s)) {
            char[] c = (char[]) maptable.get(Character.valueOf(s));
            if (c != null) {
                return c.length - 1;
            }
        } else if (s == ZWJ) {
            return 4;
        }
        return 1;
    }

    static int ligature(char newchar, charstruct oldchar) {
        int retval = 0;
        if (oldchar.basechar == '\u0000') {
            return 0;
        }
        if (isVowel(newchar)) {
            retval = 1;
            if (!(oldchar.vowel == '\u0000' || newchar == SHADDA)) {
                retval = 2;
            }
            switch (newchar) {
                case 'ّ':
                    if (oldchar.mark1 == '\u0000') {
                        oldchar.mark1 = SHADDA;
                        break;
                    }
                    return 0;
                case 'ٓ':
                    switch (oldchar.basechar) {
                        case 'ا':
                            oldchar.basechar = ALEFMADDA;
                            retval = 2;
                            break;
                        default:
                            break;
                    }
                case 'ٔ':
                    switch (oldchar.basechar) {
                        case 'ا':
                            oldchar.basechar = ALEFHAMZA;
                            retval = 2;
                            break;
                        case 'و':
                            oldchar.basechar = WAWHAMZA;
                            retval = 2;
                            break;
                        case 'ى':
                        case opencv_videoio.CAP_OPENNI2_ASUS /*1610*/:
                        case 'ی':
                            oldchar.basechar = YEHHAMZA;
                            retval = 2;
                            break;
                        case 'ﻻ':
                            oldchar.basechar = LAM_ALEFHAMZA;
                            retval = 2;
                            break;
                        default:
                            oldchar.mark1 = HAMZAABOVE;
                            break;
                    }
                case 'ٕ':
                    switch (oldchar.basechar) {
                        case 'ا':
                            oldchar.basechar = ALEFHAMZABELOW;
                            retval = 2;
                            break;
                        case 'ﻻ':
                            oldchar.basechar = LAM_ALEFHAMZABELOW;
                            retval = 2;
                            break;
                        default:
                            oldchar.mark1 = HAMZABELOW;
                            break;
                    }
                default:
                    oldchar.vowel = newchar;
                    break;
            }
            if (retval == 1) {
                oldchar.lignum++;
            }
            return retval;
        } else if (oldchar.vowel != '\u0000') {
            return 0;
        } else {
            switch (oldchar.basechar) {
                case '\u0000':
                    oldchar.basechar = newchar;
                    oldchar.numshapes = shapecount(newchar);
                    retval = 1;
                    break;
                case 'ل':
                    switch (newchar) {
                        case 'آ':
                            oldchar.basechar = LAM_ALEFMADDA;
                            oldchar.numshapes = 2;
                            retval = 3;
                            break;
                        case 'أ':
                            oldchar.basechar = LAM_ALEFHAMZA;
                            oldchar.numshapes = 2;
                            retval = 3;
                            break;
                        case 'إ':
                            oldchar.basechar = LAM_ALEFHAMZABELOW;
                            oldchar.numshapes = 2;
                            retval = 3;
                            break;
                        case 'ا':
                            oldchar.basechar = LAM_ALEF;
                            oldchar.numshapes = 2;
                            retval = 3;
                            break;
                        default:
                            break;
                    }
            }
            return retval;
        }
    }

    static void copycstostring(StringBuffer string, charstruct s, int level) {
        if (s.basechar != '\u0000') {
            string.append(s.basechar);
            s.lignum--;
            if (s.mark1 != '\u0000') {
                if ((level & 1) == 0) {
                    string.append(s.mark1);
                    s.lignum--;
                } else {
                    s.lignum--;
                }
            }
            if (s.vowel == '\u0000') {
                return;
            }
            if ((level & 1) == 0) {
                string.append(s.vowel);
                s.lignum--;
                return;
            }
            s.lignum--;
        }
    }

    static void doublelig(StringBuffer string, int level) {
        int len = string.length();
        int olen = len;
        int j = 0;
        int si = 1;
        while (si < olen) {
            char lapresult = '\u0000';
            if ((level & 4) != 0) {
                switch (string.charAt(j)) {
                    case 'َ':
                        if (string.charAt(si) == SHADDA) {
                            lapresult = 'ﱠ';
                            break;
                        }
                        break;
                    case 'ُ':
                        if (string.charAt(si) == SHADDA) {
                            lapresult = 'ﱡ';
                            break;
                        }
                        break;
                    case 'ِ':
                        if (string.charAt(si) == SHADDA) {
                            lapresult = 'ﱢ';
                            break;
                        }
                        break;
                    case 'ّ':
                        switch (string.charAt(si)) {
                            case 'ٌ':
                                lapresult = 'ﱞ';
                                break;
                            case 'ٍ':
                                lapresult = 'ﱟ';
                                break;
                            case 'َ':
                                lapresult = 'ﱠ';
                                break;
                            case 'ُ':
                                lapresult = 'ﱡ';
                                break;
                            case 'ِ':
                                lapresult = 'ﱢ';
                                break;
                            default:
                                break;
                        }
                }
            }
            if ((level & 8) != 0) {
                switch (string.charAt(j)) {
                    case 'ﺑ':
                        switch (string.charAt(si)) {
                            case 'ﺠ':
                                lapresult = 'ﲜ';
                                break;
                            case 'ﺤ':
                                lapresult = 'ﲝ';
                                break;
                            case 'ﺨ':
                                lapresult = 'ﲞ';
                                break;
                            default:
                                break;
                        }
                    case 'ﺗ':
                        switch (string.charAt(si)) {
                            case 'ﺠ':
                                lapresult = 'ﲡ';
                                break;
                            case 'ﺤ':
                                lapresult = 'ﲢ';
                                break;
                            case 'ﺨ':
                                lapresult = 'ﲣ';
                                break;
                            default:
                                break;
                        }
                    case 'ﻓ':
                        switch (string.charAt(si)) {
                            case 'ﻲ':
                                lapresult = 'ﰲ';
                                break;
                            default:
                                break;
                        }
                    case 'ﻟ':
                        switch (string.charAt(si)) {
                            case 'ﺞ':
                                lapresult = 'ﰿ';
                                break;
                            case 'ﺠ':
                                lapresult = 'ﳉ';
                                break;
                            case 'ﺢ':
                                lapresult = 'ﱀ';
                                break;
                            case 'ﺤ':
                                lapresult = 'ﳊ';
                                break;
                            case 'ﺦ':
                                lapresult = 'ﱁ';
                                break;
                            case 'ﺨ':
                                lapresult = 'ﳋ';
                                break;
                            case 'ﻢ':
                                lapresult = 'ﱂ';
                                break;
                            case 'ﻤ':
                                lapresult = 'ﳌ';
                                break;
                            default:
                                break;
                        }
                    case 'ﻣ':
                        switch (string.charAt(si)) {
                            case 'ﺠ':
                                lapresult = 'ﳎ';
                                break;
                            case 'ﺤ':
                                lapresult = 'ﳏ';
                                break;
                            case 'ﺨ':
                                lapresult = 'ﳐ';
                                break;
                            case 'ﻤ':
                                lapresult = 'ﳑ';
                                break;
                            default:
                                break;
                        }
                    case 'ﻧ':
                        switch (string.charAt(si)) {
                            case 'ﺠ':
                                lapresult = 'ﳒ';
                                break;
                            case 'ﺤ':
                                lapresult = 'ﳓ';
                                break;
                            case 'ﺨ':
                                lapresult = 'ﳔ';
                                break;
                            default:
                                break;
                        }
                    case 'ﻨ':
                        switch (string.charAt(si)) {
                            case 'ﺮ':
                                lapresult = 'ﲊ';
                                break;
                            case 'ﺰ':
                                lapresult = 'ﲋ';
                                break;
                            default:
                                break;
                        }
                }
            }
            if (lapresult != '\u0000') {
                string.setCharAt(j, lapresult);
                len--;
                si++;
            } else {
                j++;
                string.setCharAt(j, string.charAt(si));
                si++;
            }
        }
        string.setLength(len);
    }

    static boolean connects_to_left(charstruct a) {
        return a.numshapes > 2;
    }

    static void shape(char[] text, StringBuffer string, int level) {
        int which;
        int p = 0;
        charstruct oldchar = new charstruct();
        charstruct curchar = new charstruct();
        while (p < text.length) {
            int p2 = p + 1;
            char nextletter = text[p];
            int join = ligature(nextletter, curchar);
            if (join == 0) {
                int nc = shapecount(nextletter);
                if (nc == 1) {
                    which = 0;
                } else {
                    which = 2;
                }
                if (connects_to_left(oldchar)) {
                    which++;
                }
                curchar.basechar = charshape(curchar.basechar, which % curchar.numshapes);
                copycstostring(string, oldchar, level);
                oldchar = curchar;
                curchar = new charstruct();
                curchar.basechar = nextletter;
                curchar.numshapes = nc;
                curchar.lignum++;
                p = p2;
            } else if (join == 1) {
                p = p2;
            } else {
                p = p2;
            }
        }
        if (connects_to_left(oldchar)) {
            which = 1;
        } else {
            which = 0;
        }
        curchar.basechar = charshape(curchar.basechar, which % curchar.numshapes);
        copycstostring(string, oldchar, level);
        copycstostring(string, curchar, level);
    }

    public static int arabic_shape(char[] src, int srcoffset, int srclength, char[] dest, int destoffset, int destlength, int level) {
        char[] str = new char[srclength];
        for (int k = (srclength + srcoffset) - 1; k >= srcoffset; k--) {
            str[k - srcoffset] = src[k];
        }
        StringBuffer string = new StringBuffer(srclength);
        shape(str, string, level);
        if ((level & 12) != 0) {
            doublelig(string, level);
        }
        System.arraycopy(string.toString().toCharArray(), 0, dest, destoffset, string.length());
        return string.length();
    }

    public static void processNumbers(char[] text, int offset, int length, int options) {
        int limit = offset + length;
        if ((options & 224) != 0) {
            char digitBase = '0';
            switch (options & 256) {
                case 0:
                    digitBase = '٠';
                    break;
                case 256:
                    digitBase = '۰';
                    break;
            }
            int digitDelta;
            int i;
            char ch;
            switch (options & 224) {
                case 32:
                    digitDelta = digitBase - 48;
                    for (i = offset; i < limit; i++) {
                        ch = text[i];
                        if (ch <= '9' && ch >= '0') {
                            text[i] = (char) (text[i] + digitDelta);
                        }
                    }
                    return;
                case 64:
                    char digitTop = (char) (digitBase + 9);
                    digitDelta = 48 - digitBase;
                    for (i = offset; i < limit; i++) {
                        ch = text[i];
                        if (ch <= digitTop && ch >= digitBase) {
                            text[i] = (char) (text[i] + digitDelta);
                        }
                    }
                    return;
                case 96:
                    shapeToArabicDigitsWithContext(text, 0, length, digitBase, false);
                    return;
                case 128:
                    shapeToArabicDigitsWithContext(text, 0, length, digitBase, true);
                    return;
                default:
                    return;
            }
        }
    }

    static void shapeToArabicDigitsWithContext(char[] dest, int start, int length, char digitBase, boolean lastStrongWasAL) {
        digitBase = (char) (digitBase - 48);
        int limit = start + length;
        for (int i = start; i < limit; i++) {
            char ch = dest[i];
            switch (BidiOrder.getDirection(ch)) {
                case (byte) 0:
                case (byte) 3:
                    lastStrongWasAL = false;
                    break;
                case (byte) 4:
                    lastStrongWasAL = true;
                    break;
                case (byte) 8:
                    if (lastStrongWasAL && ch <= '9') {
                        dest[i] = (char) (ch + digitBase);
                        break;
                    }
                default:
                    break;
            }
        }
    }

    public ArabicLigaturizer(int runDirection, int options) {
        this.runDirection = runDirection;
        this.options = options;
    }

    public String process(String s) {
        return BidiLine.processLTR(s, this.runDirection, this.options);
    }

    public boolean isRTL() {
        return true;
    }
}
