package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.util.ArrayList;

public class PdfLayer extends PdfDictionary implements PdfOCG {
    protected ArrayList<PdfLayer> children;
    private boolean on = true;
    private boolean onPanel = true;
    protected PdfLayer parent;
    protected PdfIndirectReference ref;
    protected String title;

    PdfLayer(String title) {
        this.title = title;
    }

    public static PdfLayer createTitle(String title, PdfWriter writer) {
        if (title == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("title.cannot.be.null", new Object[0]));
        }
        PdfLayer layer = new PdfLayer(title);
        writer.registerLayer(layer);
        return layer;
    }

    public PdfLayer(String name, PdfWriter writer) throws IOException {
        super(PdfName.OCG);
        setName(name);
        if (writer instanceof PdfStamperImp) {
            this.ref = writer.addToBody(this).getIndirectReference();
        } else {
            this.ref = writer.getPdfIndirectReference();
        }
        writer.registerLayer(this);
    }

    String getTitle() {
        return this.title;
    }

    public void addChild(PdfLayer child) {
        if (child.parent != null) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.layer.1.already.has.a.parent", child.getAsString(PdfName.NAME).toUnicodeString()));
        }
        child.parent = this;
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(child);
    }

    public PdfLayer getParent() {
        return this.parent;
    }

    public ArrayList<PdfLayer> getChildren() {
        return this.children;
    }

    public PdfIndirectReference getRef() {
        return this.ref;
    }

    void setRef(PdfIndirectReference ref) {
        this.ref = ref;
    }

    public void setName(String name) {
        put(PdfName.NAME, new PdfString(name, PdfObject.TEXT_UNICODE));
    }

    public PdfObject getPdfObject() {
        return this;
    }

    public boolean isOn() {
        return this.on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    private PdfDictionary getUsage() {
        PdfDictionary usage = getAsDict(PdfName.USAGE);
        if (usage != null) {
            return usage;
        }
        usage = new PdfDictionary();
        put(PdfName.USAGE, usage);
        return usage;
    }

    public void setCreatorInfo(String creator, String subtype) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.CREATOR, new PdfString(creator, PdfObject.TEXT_UNICODE));
        dic.put(PdfName.SUBTYPE, new PdfName(subtype));
        usage.put(PdfName.CREATORINFO, dic);
    }

    public void setLanguage(String lang, boolean preferred) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.LANG, new PdfString(lang, PdfObject.TEXT_UNICODE));
        if (preferred) {
            dic.put(PdfName.PREFERRED, PdfName.ON);
        }
        usage.put(PdfName.LANGUAGE, dic);
    }

    public void setExport(boolean export) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.EXPORTSTATE, export ? PdfName.ON : PdfName.OFF);
        usage.put(PdfName.EXPORT, dic);
    }

    public void setZoom(float min, float max) {
        if (min > 0.0f || max >= 0.0f) {
            PdfDictionary usage = getUsage();
            PdfDictionary dic = new PdfDictionary();
            if (min > 0.0f) {
                dic.put(PdfName.MIN_LOWER_CASE, new PdfNumber(min));
            }
            if (max >= 0.0f) {
                dic.put(PdfName.MAX_LOWER_CASE, new PdfNumber(max));
            }
            usage.put(PdfName.ZOOM, dic);
        }
    }

    public void setPrint(String subtype, boolean printstate) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.SUBTYPE, new PdfName(subtype));
        dic.put(PdfName.PRINTSTATE, printstate ? PdfName.ON : PdfName.OFF);
        usage.put(PdfName.PRINT, dic);
    }

    public void setView(boolean view) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.VIEWSTATE, view ? PdfName.ON : PdfName.OFF);
        usage.put(PdfName.VIEW, dic);
    }

    public void setPageElement(String pe) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.SUBTYPE, new PdfName(pe));
        usage.put(PdfName.PAGEELEMENT, dic);
    }

    public void setUser(String type, String... names) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.TYPE, new PdfName(type));
        PdfArray arr = new PdfArray();
        for (String s : names) {
            arr.add(new PdfString(s, PdfObject.TEXT_UNICODE));
        }
        usage.put(PdfName.NAME, arr);
        usage.put(PdfName.USER, dic);
    }

    public boolean isOnPanel() {
        return this.onPanel;
    }

    public void setOnPanel(boolean onPanel) {
        this.onPanel = onPanel;
    }
}
