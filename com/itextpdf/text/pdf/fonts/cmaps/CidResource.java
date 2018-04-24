package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.IOException;
import java.io.InputStream;

public class CidResource implements CidLocation {
    public PRTokeniser getLocation(String location) throws IOException {
        InputStream inp = StreamUtil.getResourceStream(CJKFont.RESOURCE_PATH_CMAP + location);
        if (inp != null) {
            return new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(inp)));
        }
        throw new IOException(MessageLocalization.getComposedMessage("the.cmap.1.was.not.found", fullName));
    }
}
