package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
import java.io.IOException;
import java.util.ArrayList;

public class PdfContentParser {
    public static final int COMMAND_TYPE = 200;
    private PRTokeniser tokeniser;

    public PdfContentParser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    public ArrayList<PdfObject> parse(ArrayList<PdfObject> ls) throws IOException {
        if (ls == null) {
            ls = new ArrayList();
        } else {
            ls.clear();
        }
        PdfObject ob;
        do {
            ob = readPRObject();
            if (ob == null) {
                break;
            }
            ls.add(ob);
        } while (ob.type() != 200);
        return ls;
    }

    public PRTokeniser getTokeniser() {
        return this.tokeniser;
    }

    public void setTokeniser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    public PdfDictionary readDictionary() throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (nextValidToken()) {
            if (this.tokeniser.getTokenType() == TokenType.END_DIC) {
                return dic;
            }
            if (this.tokeniser.getTokenType() != TokenType.OTHER || !"def".equals(this.tokeniser.getStringValue())) {
                if (this.tokeniser.getTokenType() != TokenType.NAME) {
                    throw new IOException(MessageLocalization.getComposedMessage("dictionary.key.1.is.not.a.name", this.tokeniser.getStringValue()));
                }
                PdfName name = new PdfName(this.tokeniser.getStringValue(), false);
                PdfObject obj = readPRObject();
                int type = obj.type();
                if ((-type) == TokenType.END_DIC.ordinal()) {
                    throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0]));
                } else if ((-type) == TokenType.END_ARRAY.ordinal()) {
                    throw new IOException(MessageLocalization.getComposedMessage("unexpected.close.bracket", new Object[0]));
                } else {
                    dic.put(name, obj);
                }
            }
        }
        throw new IOException(MessageLocalization.getComposedMessage("unexpected.end.of.file", new Object[0]));
    }

    public PdfArray readArray() throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readPRObject();
            int type = obj.type();
            if ((-type) == TokenType.END_ARRAY.ordinal()) {
                return array;
            }
            if ((-type) == TokenType.END_DIC.ordinal()) {
                throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0]));
            }
            array.add(obj);
        }
    }

    public PdfObject readPRObject() throws IOException {
        if (!nextValidToken()) {
            return null;
        }
        TokenType type = this.tokeniser.getTokenType();
        switch (type) {
            case START_DIC:
                return readDictionary();
            case START_ARRAY:
                return readArray();
            case STRING:
                return new PdfString(this.tokeniser.getStringValue(), null).setHexWriting(this.tokeniser.isHexString());
            case NAME:
                return new PdfName(this.tokeniser.getStringValue(), false);
            case NUMBER:
                return new PdfNumber(this.tokeniser.getStringValue());
            case OTHER:
                return new PdfLiteral(200, this.tokeniser.getStringValue());
            default:
                return new PdfLiteral(-type.ordinal(), this.tokeniser.getStringValue());
        }
    }

    public boolean nextValidToken() throws IOException {
        while (this.tokeniser.nextToken()) {
            if (this.tokeniser.getTokenType() != TokenType.COMMENT) {
                return true;
            }
        }
        return false;
    }
}
