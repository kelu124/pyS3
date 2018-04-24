package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.factories.RomanAlphabetFactory;
import com.itextpdf.text.factories.RomanNumberFactory;
import com.itextpdf.text.html.HtmlTags;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class PdfPageLabels {
    public static final int DECIMAL_ARABIC_NUMERALS = 0;
    public static final int EMPTY = 5;
    public static final int LOWERCASE_LETTERS = 4;
    public static final int LOWERCASE_ROMAN_NUMERALS = 2;
    public static final int UPPERCASE_LETTERS = 3;
    public static final int UPPERCASE_ROMAN_NUMERALS = 1;
    static PdfName[] numberingStyle = new PdfName[]{PdfName.f120D, PdfName.f132R, new PdfName("r"), PdfName.f117A, new PdfName(HtmlTags.f32A)};
    private HashMap<Integer, PdfDictionary> map = new HashMap();

    public static class PdfPageLabelFormat {
        public int logicalPage;
        public int numberStyle;
        public int physicalPage;
        public String prefix;

        public PdfPageLabelFormat(int physicalPage, int numberStyle, String prefix, int logicalPage) {
            this.physicalPage = physicalPage;
            this.numberStyle = numberStyle;
            this.prefix = prefix;
            this.logicalPage = logicalPage;
        }
    }

    public PdfPageLabels() {
        addPageLabel(1, 0, null, 1);
    }

    public void addPageLabel(int page, int numberStyle, String text, int firstPage) {
        if (page < 1 || firstPage < 1) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("in.a.page.label.the.page.numbers.must.be.greater.or.equal.to.1", new Object[0]));
        }
        PdfDictionary dic = new PdfDictionary();
        if (numberStyle >= 0 && numberStyle < numberingStyle.length) {
            dic.put(PdfName.f133S, numberingStyle[numberStyle]);
        }
        if (text != null) {
            dic.put(PdfName.f130P, new PdfString(text, PdfObject.TEXT_UNICODE));
        }
        if (firstPage != 1) {
            dic.put(PdfName.ST, new PdfNumber(firstPage));
        }
        this.map.put(Integer.valueOf(page - 1), dic);
    }

    public void addPageLabel(int page, int numberStyle, String text) {
        addPageLabel(page, numberStyle, text, 1);
    }

    public void addPageLabel(int page, int numberStyle) {
        addPageLabel(page, numberStyle, null, 1);
    }

    public void addPageLabel(PdfPageLabelFormat format) {
        addPageLabel(format.physicalPage, format.numberStyle, format.prefix, format.logicalPage);
    }

    public void removePageLabel(int page) {
        if (page > 1) {
            this.map.remove(Integer.valueOf(page - 1));
        }
    }

    public PdfDictionary getDictionary(PdfWriter writer) {
        try {
            return PdfNumberTree.writeTree(this.map, writer);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    public static String[] getPageLabels(PdfReader reader) {
        int n = reader.getNumberOfPages();
        PdfDictionary labels = (PdfDictionary) PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.PAGELABELS));
        if (labels == null) {
            return null;
        }
        String[] labelstrings = new String[n];
        HashMap<Integer, PdfObject> numberTree = PdfNumberTree.readTree(labels);
        int pagecount = 1;
        String prefix = "";
        char type = 'D';
        for (int i = 0; i < n; i++) {
            Integer current = Integer.valueOf(i);
            if (numberTree.containsKey(current)) {
                PdfDictionary d = (PdfDictionary) PdfReader.getPdfObjectRelease((PdfObject) numberTree.get(current));
                if (d.contains(PdfName.ST)) {
                    pagecount = ((PdfNumber) d.get(PdfName.ST)).intValue();
                } else {
                    pagecount = 1;
                }
                if (d.contains(PdfName.f130P)) {
                    prefix = ((PdfString) d.get(PdfName.f130P)).toUnicodeString();
                }
                if (d.contains(PdfName.f133S)) {
                    type = ((PdfName) d.get(PdfName.f133S)).toString().charAt(1);
                } else {
                    type = Barcode128.CODE_BC_TO_A;
                }
            }
            switch (type) {
                case 'A':
                    labelstrings[i] = prefix + RomanAlphabetFactory.getUpperCaseString(pagecount);
                    break;
                case 'R':
                    labelstrings[i] = prefix + RomanNumberFactory.getUpperCaseString(pagecount);
                    break;
                case 'a':
                    labelstrings[i] = prefix + RomanAlphabetFactory.getLowerCaseString(pagecount);
                    break;
                case 'e':
                    labelstrings[i] = prefix;
                    break;
                case 'r':
                    labelstrings[i] = prefix + RomanNumberFactory.getLowerCaseString(pagecount);
                    break;
                default:
                    labelstrings[i] = prefix + pagecount;
                    break;
            }
            pagecount++;
        }
        return labelstrings;
    }

    public static PdfPageLabelFormat[] getPageLabelFormats(PdfReader reader) {
        PdfDictionary labels = (PdfDictionary) PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.PAGELABELS));
        if (labels == null) {
            return null;
        }
        HashMap<Integer, PdfObject> numberTree = PdfNumberTree.readTree(labels);
        Integer[] numbers = (Integer[]) numberTree.keySet().toArray(new Integer[numberTree.size()]);
        Arrays.sort(numbers);
        PdfPageLabelFormat[] formats = new PdfPageLabelFormat[numberTree.size()];
        for (int k = 0; k < numbers.length; k++) {
            int pagecount;
            String prefix;
            int numberStyle;
            Integer key = numbers[k];
            PdfDictionary d = (PdfDictionary) PdfReader.getPdfObjectRelease((PdfObject) numberTree.get(key));
            if (d.contains(PdfName.ST)) {
                pagecount = ((PdfNumber) d.get(PdfName.ST)).intValue();
            } else {
                pagecount = 1;
            }
            if (d.contains(PdfName.f130P)) {
                prefix = ((PdfString) d.get(PdfName.f130P)).toUnicodeString();
            } else {
                prefix = "";
            }
            if (d.contains(PdfName.f133S)) {
                switch (((PdfName) d.get(PdfName.f133S)).toString().charAt(1)) {
                    case 'A':
                        numberStyle = 3;
                        break;
                    case 'R':
                        numberStyle = 1;
                        break;
                    case 'a':
                        numberStyle = 4;
                        break;
                    case 'r':
                        numberStyle = 2;
                        break;
                    default:
                        numberStyle = 0;
                        break;
                }
            }
            numberStyle = 5;
            formats[k] = new PdfPageLabelFormat(key.intValue() + 1, numberStyle, prefix, pagecount);
        }
        return formats;
    }
}
