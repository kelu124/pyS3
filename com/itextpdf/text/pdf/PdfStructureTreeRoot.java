package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.interfaces.IPdfStructureElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class PdfStructureTreeRoot extends PdfDictionary implements IPdfStructureElement {
    private PdfDictionary classMap = null;
    protected HashMap<PdfName, PdfObject> classes = null;
    private HashMap<Integer, PdfIndirectReference> numTree = null;
    private HashMap<Integer, PdfObject> parentTree = new HashMap();
    private PdfIndirectReference reference;
    private PdfWriter writer;

    PdfStructureTreeRoot(PdfWriter writer) {
        super(PdfName.STRUCTTREEROOT);
        this.writer = writer;
        this.reference = writer.getPdfIndirectReference();
    }

    private void createNumTree() throws IOException {
        if (this.numTree == null) {
            this.numTree = new HashMap();
            for (Integer i : this.parentTree.keySet()) {
                PdfObject obj = (PdfObject) this.parentTree.get(i);
                if (obj.isArray()) {
                    this.numTree.put(i, this.writer.addToBody((PdfArray) obj).getIndirectReference());
                } else if (obj instanceof PdfIndirectReference) {
                    this.numTree.put(i, (PdfIndirectReference) obj);
                }
            }
        }
    }

    public void mapRole(PdfName used, PdfName standard) {
        PdfDictionary rm = (PdfDictionary) get(PdfName.ROLEMAP);
        if (rm == null) {
            rm = new PdfDictionary();
            put(PdfName.ROLEMAP, rm);
        }
        rm.put(used, standard);
    }

    public void mapClass(PdfName name, PdfObject object) {
        if (this.classMap == null) {
            this.classMap = new PdfDictionary();
            this.classes = new HashMap();
        }
        this.classes.put(name, object);
    }

    public PdfObject getMappedClass(PdfName name) {
        if (this.classes == null) {
            return null;
        }
        return (PdfObject) this.classes.get(name);
    }

    public PdfWriter getWriter() {
        return this.writer;
    }

    public HashMap<Integer, PdfIndirectReference> getNumTree() throws IOException {
        if (this.numTree == null) {
            createNumTree();
        }
        return this.numTree;
    }

    public PdfIndirectReference getReference() {
        return this.reference;
    }

    void setPageMark(int page, PdfIndirectReference struc) {
        Integer i = Integer.valueOf(page);
        PdfArray ar = (PdfArray) this.parentTree.get(i);
        if (ar == null) {
            ar = new PdfArray();
            this.parentTree.put(i, ar);
        }
        ar.add(struc);
    }

    void setAnnotationMark(int structParentIndex, PdfIndirectReference struc) {
        this.parentTree.put(Integer.valueOf(structParentIndex), struc);
    }

    private void nodeProcess(PdfDictionary struc, PdfIndirectReference reference) throws IOException {
        PdfObject obj = struc.get(PdfName.f125K);
        if (obj != null && obj.isArray()) {
            PdfArray ar = (PdfArray) obj;
            int k = 0;
            while (k < ar.size()) {
                PdfDictionary dictionary = ar.getAsDict(k);
                if (dictionary != null && PdfName.STRUCTELEM.equals(dictionary.get(PdfName.TYPE)) && (ar.getPdfObject(k) instanceof PdfStructureElement)) {
                    PdfStructureElement e = (PdfStructureElement) dictionary;
                    ar.set(k, e.getReference());
                    nodeProcess(e, e.getReference());
                }
                k++;
            }
        }
        if (reference != null) {
            this.writer.addToBody(struc, reference);
        }
    }

    void buildTree() throws IOException {
        createNumTree();
        PdfDictionary dicTree = PdfNumberTree.writeTree(this.numTree, this.writer);
        if (dicTree != null) {
            put(PdfName.PARENTTREE, this.writer.addToBody(dicTree).getIndirectReference());
        }
        if (!(this.classMap == null || this.classes.isEmpty())) {
            for (Entry<PdfName, PdfObject> entry : this.classes.entrySet()) {
                PdfObject value = (PdfObject) entry.getValue();
                if (value.isDictionary()) {
                    this.classMap.put((PdfName) entry.getKey(), this.writer.addToBody(value).getIndirectReference());
                } else if (value.isArray()) {
                    PdfArray newArray = new PdfArray();
                    PdfArray array = (PdfArray) value;
                    for (int i = 0; i < array.size(); i++) {
                        if (array.getPdfObject(i).isDictionary()) {
                            newArray.add(this.writer.addToBody(array.getAsDict(i)).getIndirectReference());
                        }
                    }
                    this.classMap.put((PdfName) entry.getKey(), newArray);
                }
            }
            put(PdfName.CLASSMAP, this.writer.addToBody(this.classMap).getIndirectReference());
        }
        nodeProcess(this, this.reference);
    }

    public PdfObject getAttribute(PdfName name) {
        PdfDictionary attr = getAsDict(PdfName.f117A);
        if (attr == null || !attr.contains(name)) {
            return null;
        }
        return attr.get(name);
    }

    public void setAttribute(PdfName name, PdfObject obj) {
        PdfDictionary attr = getAsDict(PdfName.f117A);
        if (attr == null) {
            attr = new PdfDictionary();
            put(PdfName.f117A, attr);
        }
        attr.put(name, obj);
    }
}
