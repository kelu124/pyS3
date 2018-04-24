package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class Document implements DocListener, IAccessibleElement {
    public static boolean compress = true;
    public static boolean plainRandomAccess = false;
    public static float wmfFontCorrection = 0.86f;
    protected HashMap<PdfName, PdfObject> accessibleAttributes;
    protected int chapternumber;
    protected boolean close;
    protected String htmlStyleClass;
    protected AccessibleElementId id;
    protected String javaScript_onLoad;
    protected String javaScript_onUnLoad;
    protected ArrayList<DocListener> listeners;
    protected float marginBottom;
    protected float marginLeft;
    protected boolean marginMirroring;
    protected boolean marginMirroringTopBottom;
    protected float marginRight;
    protected float marginTop;
    protected boolean open;
    protected int pageN;
    protected Rectangle pageSize;
    protected PdfName role;

    public Document() {
        this(PageSize.A4);
    }

    public Document(Rectangle pageSize) {
        this(pageSize, TabSettings.DEFAULT_TAB_INTERVAL, TabSettings.DEFAULT_TAB_INTERVAL, TabSettings.DEFAULT_TAB_INTERVAL, TabSettings.DEFAULT_TAB_INTERVAL);
    }

    public Document(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom) {
        this.listeners = new ArrayList();
        this.marginLeft = 0.0f;
        this.marginRight = 0.0f;
        this.marginTop = 0.0f;
        this.marginBottom = 0.0f;
        this.marginMirroring = false;
        this.marginMirroringTopBottom = false;
        this.javaScript_onLoad = null;
        this.javaScript_onUnLoad = null;
        this.htmlStyleClass = null;
        this.pageN = 0;
        this.chapternumber = 0;
        this.role = PdfName.DOCUMENT;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.pageSize = pageSize;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }

    public void addDocListener(DocListener listener) {
        this.listeners.add(listener);
        if (listener instanceof IAccessibleElement) {
            IAccessibleElement ae = (IAccessibleElement) listener;
            ae.setRole(this.role);
            ae.setId(this.id);
            if (this.accessibleAttributes != null) {
                for (PdfName key : this.accessibleAttributes.keySet()) {
                    ae.setAccessibleAttribute(key, (PdfObject) this.accessibleAttributes.get(key));
                }
            }
        }
    }

    public void removeDocListener(DocListener listener) {
        this.listeners.remove(listener);
    }

    public boolean add(Element element) throws DocumentException {
        if (this.close) {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.document.has.been.closed.you.can.t.add.any.elements", new Object[0]));
        } else if (this.open || !element.isContent()) {
            boolean success = false;
            if (element instanceof ChapterAutoNumber) {
                this.chapternumber = ((ChapterAutoNumber) element).setAutomaticNumber(this.chapternumber);
            }
            Iterator i$ = this.listeners.iterator();
            while (i$.hasNext()) {
                success |= ((DocListener) i$.next()).add(element);
            }
            if (element instanceof LargeElement) {
                LargeElement e = (LargeElement) element;
                if (!e.isComplete()) {
                    e.flushContent();
                }
            }
            return success;
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information", new Object[0]));
        }
    }

    public void open() {
        if (!this.close) {
            this.open = true;
        }
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            DocListener listener = (DocListener) i$.next();
            listener.setPageSize(this.pageSize);
            listener.setMargins(this.marginLeft, this.marginRight, this.marginTop, this.marginBottom);
            listener.open();
        }
    }

    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).setPageSize(pageSize);
        }
        return true;
    }

    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).setMargins(marginLeft, marginRight, marginTop, marginBottom);
        }
        return true;
    }

    public boolean newPage() {
        if (!this.open || this.close) {
            return false;
        }
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).newPage();
        }
        return true;
    }

    public void resetPageCount() {
        this.pageN = 0;
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).resetPageCount();
        }
    }

    public void setPageCount(int pageN) {
        this.pageN = pageN;
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).setPageCount(pageN);
        }
    }

    public int getPageNumber() {
        return this.pageN;
    }

    public void close() {
        if (!this.close) {
            this.open = false;
            this.close = true;
        }
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).close();
        }
    }

    public boolean addHeader(String name, String content) {
        try {
            return add(new Header(name, content));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addTitle(String title) {
        try {
            return add(new Meta(1, title));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addSubject(String subject) {
        try {
            return add(new Meta(2, subject));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addKeywords(String keywords) {
        try {
            return add(new Meta(3, keywords));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addAuthor(String author) {
        try {
            return add(new Meta(4, author));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addCreator(String creator) {
        try {
            return add(new Meta(7, creator));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addProducer() {
        try {
            return add(new Meta(5, Version.getInstance().getVersion()));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addLanguage(String language) {
        try {
            return add(new Meta(8, language));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public boolean addCreationDate() {
        try {
            return add(new Meta(6, new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(new Date())));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public float leftMargin() {
        return this.marginLeft;
    }

    public float rightMargin() {
        return this.marginRight;
    }

    public float topMargin() {
        return this.marginTop;
    }

    public float bottomMargin() {
        return this.marginBottom;
    }

    public float left() {
        return this.pageSize.getLeft(this.marginLeft);
    }

    public float right() {
        return this.pageSize.getRight(this.marginRight);
    }

    public float top() {
        return this.pageSize.getTop(this.marginTop);
    }

    public float bottom() {
        return this.pageSize.getBottom(this.marginBottom);
    }

    public float left(float margin) {
        return this.pageSize.getLeft(this.marginLeft + margin);
    }

    public float right(float margin) {
        return this.pageSize.getRight(this.marginRight + margin);
    }

    public float top(float margin) {
        return this.pageSize.getTop(this.marginTop + margin);
    }

    public float bottom(float margin) {
        return this.pageSize.getBottom(this.marginBottom + margin);
    }

    public Rectangle getPageSize() {
        return this.pageSize;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setJavaScript_onLoad(String code) {
        this.javaScript_onLoad = code;
    }

    public String getJavaScript_onLoad() {
        return this.javaScript_onLoad;
    }

    public void setJavaScript_onUnLoad(String code) {
        this.javaScript_onUnLoad = code;
    }

    public String getJavaScript_onUnLoad() {
        return this.javaScript_onUnLoad;
    }

    public void setHtmlStyleClass(String htmlStyleClass) {
        this.htmlStyleClass = htmlStyleClass;
    }

    public String getHtmlStyleClass() {
        return this.htmlStyleClass;
    }

    public boolean setMarginMirroring(boolean marginMirroring) {
        this.marginMirroring = marginMirroring;
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).setMarginMirroring(marginMirroring);
        }
        return true;
    }

    public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom) {
        this.marginMirroringTopBottom = marginMirroringTopBottom;
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((DocListener) i$.next()).setMarginMirroringTopBottom(marginMirroringTopBottom);
        }
        return true;
    }

    public boolean isMarginMirroring() {
        return this.marginMirroring;
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
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }
}
