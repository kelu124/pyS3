package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import java.util.ArrayList;
import org.bytedeco.javacpp.avutil;

public class CMapByteCid extends AbstractCMap {
    private ArrayList<char[]> planes = new ArrayList();

    public CMapByteCid() {
        this.planes.add(new char[256]);
    }

    void addChar(PdfString mark, PdfObject code) {
        if (code instanceof PdfNumber) {
            encodeSequence(AbstractCMap.decodeStringToByte(mark), (char) ((PdfNumber) code).intValue());
        }
    }

    private void encodeSequence(byte[] seqs, char cid) {
        char[] plane;
        int one;
        int size = seqs.length - 1;
        int nextPlane = 0;
        int idx = 0;
        while (idx < size) {
            plane = (char[]) this.planes.get(nextPlane);
            one = seqs[idx] & 255;
            char c = plane[one];
            if (c == '\u0000' || (c & 32768) != 0) {
                if (c == '\u0000') {
                    this.planes.add(new char[256]);
                    c = (char) ((this.planes.size() - 1) | 32768);
                    plane[one] = c;
                }
                nextPlane = c & avutil.FF_LAMBDA_MAX;
                idx++;
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0]));
            }
        }
        plane = (char[]) this.planes.get(nextPlane);
        one = seqs[size] & 255;
        if ((plane[one] & 32768) != 0) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0]));
        }
        plane[one] = cid;
    }

    public int decodeSingle(CMapSequence seq) {
        int end = seq.off + seq.len;
        int currentPlane = 0;
        while (seq.off < end) {
            byte[] bArr = seq.seq;
            int i = seq.off;
            seq.off = i + 1;
            int one = bArr[i] & 255;
            seq.len--;
            int cid = ((char[]) this.planes.get(currentPlane))[one];
            if ((32768 & cid) == 0) {
                return cid;
            }
            currentPlane = cid & avutil.FF_LAMBDA_MAX;
        }
        return -1;
    }

    public String decodeSequence(CMapSequence seq) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int cid = decodeSingle(seq);
            if (cid < 0) {
                return sb.toString();
            }
            sb.append((char) cid);
        }
    }
}
