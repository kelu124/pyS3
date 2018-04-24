package com.itextpdf.text.pdf;

import java.util.ArrayList;

public class PdfTextArray {
    ArrayList<Object> arrayList = new ArrayList();
    private Float lastNum;
    private String lastStr;

    public PdfTextArray(String str) {
        add(str);
    }

    public void add(PdfNumber number) {
        add((float) number.doubleValue());
    }

    public void add(float number) {
        if (number != 0.0f) {
            if (this.lastNum != null) {
                this.lastNum = new Float(this.lastNum.floatValue() + number);
                if (this.lastNum.floatValue() != 0.0f) {
                    replaceLast(this.lastNum);
                } else {
                    this.arrayList.remove(this.arrayList.size() - 1);
                }
            } else {
                this.lastNum = new Float(number);
                this.arrayList.add(this.lastNum);
            }
            this.lastStr = null;
        }
    }

    public void add(String str) {
        if (str.length() > 0) {
            if (this.lastStr != null) {
                this.lastStr += str;
                replaceLast(this.lastStr);
            } else {
                this.lastStr = str;
                this.arrayList.add(this.lastStr);
            }
            this.lastNum = null;
        }
    }

    ArrayList<Object> getArrayList() {
        return this.arrayList;
    }

    private void replaceLast(Object obj) {
        this.arrayList.set(this.arrayList.size() - 1, obj);
    }
}
