package com.itextpdf.text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Anchor extends Phrase {
    private static final long serialVersionUID = -852278536049236911L;
    protected String name = null;
    protected String reference = null;

    public Anchor() {
        super(16.0f);
    }

    public Anchor(float leading) {
        super(leading);
    }

    public Anchor(Chunk chunk) {
        super(chunk);
    }

    public Anchor(String string) {
        super(string);
    }

    public Anchor(String string, Font font) {
        super(string, font);
    }

    public Anchor(float leading, Chunk chunk) {
        super(leading, chunk);
    }

    public Anchor(float leading, String string) {
        super(leading, string);
    }

    public Anchor(float leading, String string, Font font) {
        super(leading, string, font);
    }

    public Anchor(Phrase phrase) {
        super(phrase);
        if (phrase instanceof Anchor) {
            Anchor a = (Anchor) phrase;
            setName(a.name);
            setReference(a.reference);
        }
    }

    public boolean process(ElementListener listener) {
        try {
            boolean localDestination;
            if (this.reference == null || !this.reference.startsWith("#")) {
                localDestination = false;
            } else {
                localDestination = true;
            }
            boolean notGotoOK = true;
            for (Chunk chunk : getChunks()) {
                if (!(this.name == null || !notGotoOK || chunk.isEmpty())) {
                    chunk.setLocalDestination(this.name);
                    notGotoOK = false;
                }
                if (localDestination) {
                    chunk.setLocalGoto(this.reference.substring(1));
                }
                listener.add(chunk);
            }
            return true;
        } catch (DocumentException e) {
            return false;
        }
    }

    public List<Chunk> getChunks() {
        boolean localDestination = this.reference != null && this.reference.startsWith("#");
        boolean notGotoOK = true;
        List<Chunk> tmp = new ArrayList();
        Iterator<Element> i = iterator();
        while (i.hasNext()) {
            Element element = (Element) i.next();
            if (element instanceof Chunk) {
                Chunk chunk = (Chunk) element;
                notGotoOK = applyAnchor(chunk, notGotoOK, localDestination);
                tmp.add(chunk);
            } else {
                for (Chunk c : element.getChunks()) {
                    notGotoOK = applyAnchor(c, notGotoOK, localDestination);
                    tmp.add(c);
                }
            }
        }
        return tmp;
    }

    protected boolean applyAnchor(Chunk chunk, boolean notGotoOK, boolean localDestination) {
        if (!(this.name == null || !notGotoOK || chunk.isEmpty())) {
            chunk.setLocalDestination(this.name);
            notGotoOK = false;
        }
        if (localDestination) {
            chunk.setLocalGoto(this.reference.substring(1));
        } else if (this.reference != null) {
            chunk.setAnchor(this.reference);
        }
        return notGotoOK;
    }

    public int type() {
        return 17;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return this.name;
    }

    public String getReference() {
        return this.reference;
    }

    public URL getUrl() {
        try {
            return new URL(this.reference);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
