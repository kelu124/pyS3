package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

public class PdfLister {
    PrintStream out;

    public PdfLister(PrintStream out) {
        this.out = out;
    }

    public void listAnyObject(PdfObject object) {
        switch (object.type()) {
            case 3:
                this.out.println("(" + object.toString() + ")");
                return;
            case 5:
                listArray((PdfArray) object);
                return;
            case 6:
                listDict((PdfDictionary) object);
                return;
            default:
                this.out.println(object.toString());
                return;
        }
    }

    public void listDict(PdfDictionary dictionary) {
        this.out.println("<<");
        for (PdfName key : dictionary.getKeys()) {
            PdfObject value = dictionary.get(key);
            this.out.print(key.toString());
            this.out.print(' ');
            listAnyObject(value);
        }
        this.out.println(">>");
    }

    public void listArray(PdfArray array) {
        this.out.println('[');
        Iterator<PdfObject> i = array.listIterator();
        while (i.hasNext()) {
            listAnyObject((PdfObject) i.next());
        }
        this.out.println(']');
    }

    public void listStream(PRStream stream, PdfReaderInstance reader) {
        try {
            listDict(stream);
            this.out.println("startstream");
            byte[] b = PdfReader.getStreamBytes(stream);
            int len = b.length - 1;
            int k = 0;
            while (k < len) {
                if (b[k] == (byte) 13 && b[k + 1] != (byte) 10) {
                    b[k] = (byte) 10;
                }
                k++;
            }
            this.out.println(new String(b));
            this.out.println("endstream");
        } catch (IOException e) {
            System.err.println("I/O exception: " + e);
        }
    }

    public void listPage(PdfImportedPage iPage) {
        int pageNum = iPage.getPageNumber();
        PdfReaderInstance readerInst = iPage.getPdfReaderInstance();
        PdfDictionary page = readerInst.getReader().getPageN(pageNum);
        listDict(page);
        PdfObject obj = PdfReader.getPdfObject(page.get(PdfName.CONTENTS));
        if (obj != null) {
            switch (obj.type) {
                case 5:
                    Iterator<PdfObject> i = ((PdfArray) obj).listIterator();
                    while (i.hasNext()) {
                        listStream((PRStream) PdfReader.getPdfObject((PdfObject) i.next()), readerInst);
                        this.out.println("-----------");
                    }
                    return;
                case 7:
                    listStream((PRStream) obj, readerInst);
                    return;
                default:
                    return;
            }
        }
    }
}
