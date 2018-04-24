package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.poi.util.CodePageUtil;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.StringUtil;

@Internal
class CodePageString {
    private static final POILogger logger = POILogFactory.getLogger(CodePageString.class);
    private byte[] _value;

    CodePageString(byte[] data, int startOffset) {
        int offset = startOffset;
        int size = LittleEndian.getInt(data, offset);
        this._value = LittleEndian.getByteArray(data, offset + 4, size);
        if (size != 0 && this._value[size - 1] != (byte) 0) {
            logger.log(5, "CodePageString started at offset #" + offset + " is not NULL-terminated");
        }
    }

    CodePageString(String string, int codepage) throws UnsupportedEncodingException {
        setJavaValue(string, codepage);
    }

    String getJavaValue(int codepage) throws UnsupportedEncodingException {
        String result;
        if (codepage == -1) {
            result = new String(this._value, StringUtil.UTF8);
        } else {
            result = CodePageUtil.getStringFromCodePage(this._value, codepage);
        }
        int terminator = result.indexOf(0);
        if (terminator == -1) {
            logger.log(5, "String terminator (\\0) for CodePageString property value not found.Continue without trimming and hope for the best.");
            return result;
        }
        if (terminator != result.length() - 1) {
            logger.log(5, "String terminator (\\0) for CodePageString property value occured before the end of string. Trimming and hope for the best.");
        }
        return result.substring(0, terminator);
    }

    int getSize() {
        return this._value.length + 4;
    }

    void setJavaValue(String string, int codepage) throws UnsupportedEncodingException {
        String stringNT = string + "\u0000";
        if (codepage == -1) {
            this._value = stringNT.getBytes(StringUtil.UTF8);
        } else {
            this._value = CodePageUtil.getBytesInCodePage(stringNT, codepage);
        }
    }

    int write(OutputStream out) throws IOException {
        LittleEndian.putInt(this._value.length, out);
        out.write(this._value);
        return this._value.length + 4;
    }
}
