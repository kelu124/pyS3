package com.itextpdf.text;

import com.itextpdf.text.api.Indentable;
import com.itextpdf.text.factories.RomanAlphabetFactory;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class List implements TextElementArray, Indentable, IAccessibleElement {
    public static final boolean ALPHABETICAL = true;
    public static final boolean LOWERCASE = true;
    public static final boolean NUMERICAL = false;
    public static final boolean ORDERED = true;
    public static final boolean UNORDERED = false;
    public static final boolean UPPERCASE = false;
    protected HashMap<PdfName, PdfObject> accessibleAttributes;
    protected boolean alignindent;
    protected boolean autoindent;
    protected int first;
    private AccessibleElementId id;
    protected float indentationLeft;
    protected float indentationRight;
    protected boolean lettered;
    protected ArrayList<Element> list;
    protected boolean lowercase;
    protected boolean numbered;
    protected String postSymbol;
    protected String preSymbol;
    protected PdfName role;
    protected Chunk symbol;
    protected float symbolIndent;

    public List() {
        this(false, false);
    }

    public List(float symbolIndent) {
        this.list = new ArrayList();
        this.numbered = false;
        this.lettered = false;
        this.lowercase = false;
        this.autoindent = false;
        this.alignindent = false;
        this.first = 1;
        this.symbol = new Chunk("- ");
        this.preSymbol = "";
        this.postSymbol = ". ";
        this.indentationLeft = 0.0f;
        this.indentationRight = 0.0f;
        this.symbolIndent = 0.0f;
        this.role = PdfName.f126L;
        this.accessibleAttributes = null;
        this.id = null;
        this.symbolIndent = symbolIndent;
    }

    public List(boolean numbered) {
        this(numbered, false);
    }

    public List(boolean numbered, boolean lettered) {
        this.list = new ArrayList();
        this.numbered = false;
        this.lettered = false;
        this.lowercase = false;
        this.autoindent = false;
        this.alignindent = false;
        this.first = 1;
        this.symbol = new Chunk("- ");
        this.preSymbol = "";
        this.postSymbol = ". ";
        this.indentationLeft = 0.0f;
        this.indentationRight = 0.0f;
        this.symbolIndent = 0.0f;
        this.role = PdfName.f126L;
        this.accessibleAttributes = null;
        this.id = null;
        this.numbered = numbered;
        this.lettered = lettered;
        this.autoindent = true;
        this.alignindent = true;
    }

    public List(boolean numbered, float symbolIndent) {
        this(numbered, false, symbolIndent);
    }

    public List(boolean numbered, boolean lettered, float symbolIndent) {
        this.list = new ArrayList();
        this.numbered = false;
        this.lettered = false;
        this.lowercase = false;
        this.autoindent = false;
        this.alignindent = false;
        this.first = 1;
        this.symbol = new Chunk("- ");
        this.preSymbol = "";
        this.postSymbol = ". ";
        this.indentationLeft = 0.0f;
        this.indentationRight = 0.0f;
        this.symbolIndent = 0.0f;
        this.role = PdfName.f126L;
        this.accessibleAttributes = null;
        this.id = null;
        this.numbered = numbered;
        this.lettered = lettered;
        this.symbolIndent = symbolIndent;
    }

    public boolean process(ElementListener listener) {
        try {
            Iterator i$ = this.list.iterator();
            while (i$.hasNext()) {
                listener.add((Element) i$.next());
            }
            return true;
        } catch (DocumentException e) {
            return false;
        }
    }

    public int type() {
        return 14;
    }

    public java.util.List<Chunk> getChunks() {
        java.util.List<Chunk> tmp = new ArrayList();
        Iterator i$ = this.list.iterator();
        while (i$.hasNext()) {
            tmp.addAll(((Element) i$.next()).getChunks());
        }
        return tmp;
    }

    public boolean add(String s) {
        if (s != null) {
            return add(new ListItem(s));
        }
        return false;
    }

    public boolean add(Element o) {
        if (o instanceof ListItem) {
            ListItem item = (ListItem) o;
            if (this.numbered || this.lettered) {
                Chunk chunk = new Chunk(this.preSymbol, this.symbol.getFont());
                chunk.setAttributes(this.symbol.getAttributes());
                int index = this.first + this.list.size();
                if (this.lettered) {
                    chunk.append(RomanAlphabetFactory.getString(index, this.lowercase));
                } else {
                    chunk.append(String.valueOf(index));
                }
                chunk.append(this.postSymbol);
                item.setListSymbol(chunk);
            } else {
                item.setListSymbol(this.symbol);
            }
            item.setIndentationLeft(this.symbolIndent, this.autoindent);
            item.setIndentationRight(0.0f);
            return this.list.add(item);
        } else if (!(o instanceof List)) {
            return false;
        } else {
            List nested = (List) o;
            nested.setIndentationLeft(nested.getIndentationLeft() + this.symbolIndent);
            this.first--;
            return this.list.add(nested);
        }
    }

    public void normalizeIndentation() {
        float max = 0.0f;
        Iterator i$ = this.list.iterator();
        while (i$.hasNext()) {
            Element o = (Element) i$.next();
            if (o instanceof ListItem) {
                max = Math.max(max, ((ListItem) o).getIndentationLeft());
            }
        }
        i$ = this.list.iterator();
        while (i$.hasNext()) {
            o = (Element) i$.next();
            if (o instanceof ListItem) {
                ((ListItem) o).setIndentationLeft(max);
            }
        }
    }

    public void setNumbered(boolean numbered) {
        this.numbered = numbered;
    }

    public void setLettered(boolean lettered) {
        this.lettered = lettered;
    }

    public void setLowercase(boolean uppercase) {
        this.lowercase = uppercase;
    }

    public void setAutoindent(boolean autoindent) {
        this.autoindent = autoindent;
    }

    public void setAlignindent(boolean alignindent) {
        this.alignindent = alignindent;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setListSymbol(Chunk symbol) {
        this.symbol = symbol;
    }

    public void setListSymbol(String symbol) {
        this.symbol = new Chunk(symbol);
    }

    public void setIndentationLeft(float indentation) {
        this.indentationLeft = indentation;
    }

    public void setIndentationRight(float indentation) {
        this.indentationRight = indentation;
    }

    public void setSymbolIndent(float symbolIndent) {
        this.symbolIndent = symbolIndent;
    }

    public ArrayList<Element> getItems() {
        return this.list;
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public float getTotalLeading() {
        if (this.list.size() < 1) {
            return -1.0f;
        }
        return ((ListItem) this.list.get(0)).getTotalLeading();
    }

    public boolean isNumbered() {
        return this.numbered;
    }

    public boolean isLettered() {
        return this.lettered;
    }

    public boolean isLowercase() {
        return this.lowercase;
    }

    public boolean isAutoindent() {
        return this.autoindent;
    }

    public boolean isAlignindent() {
        return this.alignindent;
    }

    public int getFirst() {
        return this.first;
    }

    public Chunk getSymbol() {
        return this.symbol;
    }

    public float getIndentationLeft() {
        return this.indentationLeft;
    }

    public float getIndentationRight() {
        return this.indentationRight;
    }

    public float getSymbolIndent() {
        return this.symbolIndent;
    }

    public boolean isContent() {
        return true;
    }

    public boolean isNestable() {
        return true;
    }

    public String getPostSymbol() {
        return this.postSymbol;
    }

    public void setPostSymbol(String postSymbol) {
        this.postSymbol = postSymbol;
    }

    public String getPreSymbol() {
        return this.preSymbol;
    }

    public void setPreSymbol(String preSymbol) {
        this.preSymbol = preSymbol;
    }

    public ListItem getFirstItem() {
        Element lastElement = this.list.size() > 0 ? (Element) this.list.get(0) : null;
        if (lastElement != null) {
            if (lastElement instanceof ListItem) {
                return (ListItem) lastElement;
            }
            if (lastElement instanceof List) {
                return ((List) lastElement).getFirstItem();
            }
        }
        return null;
    }

    public ListItem getLastItem() {
        Element lastElement = this.list.size() > 0 ? (Element) this.list.get(this.list.size() - 1) : null;
        if (lastElement != null) {
            if (lastElement instanceof ListItem) {
                return (ListItem) lastElement;
            }
            if (lastElement instanceof List) {
                return ((List) lastElement).getLastItem();
            }
        }
        return null;
    }

    public PdfObject getAccessibleAttribute(PdfName key) {
        if (this.accessibleAttributes != null) {
            return (PdfObject) this.accessibleAttributes.get(key);
        }
        return null;
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        if (this.accessibleAttributes == null) {
            this.accessibleAttributes = new HashMap();
        }
        this.accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return this.accessibleAttributes;
    }

    public PdfName getRole() {
        return this.role;
    }

    public void setRole(PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        if (this.id == null) {
            this.id = new AccessibleElementId();
        }
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }
}
