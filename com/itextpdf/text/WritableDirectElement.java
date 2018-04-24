package com.itextpdf.text;

import com.itextpdf.text.api.WriterOperation;
import java.util.ArrayList;
import java.util.List;

public abstract class WritableDirectElement implements Element, WriterOperation {
    public boolean process(ElementListener listener) {
        throw new UnsupportedOperationException();
    }

    public int type() {
        return Element.WRITABLE_DIRECT;
    }

    public boolean isContent() {
        return false;
    }

    public boolean isNestable() {
        throw new UnsupportedOperationException();
    }

    public List<Chunk> getChunks() {
        return new ArrayList(0);
    }
}
