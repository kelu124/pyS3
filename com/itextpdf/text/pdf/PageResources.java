package com.itextpdf.text.pdf;

import java.util.HashMap;
import java.util.HashSet;

class PageResources {
    protected PdfDictionary colorDictionary = new PdfDictionary();
    protected PdfDictionary extGStateDictionary = new PdfDictionary();
    protected PdfDictionary fontDictionary = new PdfDictionary();
    protected HashSet<PdfName> forbiddenNames;
    protected int[] namePtr = new int[]{0};
    protected PdfDictionary originalResources;
    protected PdfDictionary patternDictionary = new PdfDictionary();
    protected PdfDictionary propertyDictionary = new PdfDictionary();
    protected PdfDictionary shadingDictionary = new PdfDictionary();
    protected HashMap<PdfName, PdfName> usedNames;
    protected PdfDictionary xObjectDictionary = new PdfDictionary();

    PageResources() {
    }

    void setOriginalResources(PdfDictionary resources, int[] newNamePtr) {
        if (newNamePtr != null) {
            this.namePtr = newNamePtr;
        }
        this.forbiddenNames = new HashSet();
        this.usedNames = new HashMap();
        if (resources != null) {
            this.originalResources = new PdfDictionary();
            this.originalResources.merge(resources);
            for (PdfName key : resources.getKeys()) {
                PdfObject sub = PdfReader.getPdfObject(resources.get(key));
                if (sub != null && sub.isDictionary()) {
                    PdfDictionary dic = (PdfDictionary) sub;
                    for (PdfName element2 : dic.getKeys()) {
                        this.forbiddenNames.add(element2);
                    }
                    PdfDictionary dic2 = new PdfDictionary();
                    dic2.merge(dic);
                    this.originalResources.put(key, dic2);
                }
            }
        }
    }

    PdfName translateName(PdfName name) {
        PdfName translated = name;
        if (this.forbiddenNames != null) {
            translated = (PdfName) this.usedNames.get(name);
            if (translated == null) {
                do {
                    StringBuilder append = new StringBuilder().append("Xi");
                    int[] iArr = this.namePtr;
                    int i = iArr[0];
                    iArr[0] = i + 1;
                    translated = new PdfName(append.append(i).toString());
                } while (this.forbiddenNames.contains(translated));
                this.usedNames.put(name, translated);
            }
        }
        return translated;
    }

    PdfName addFont(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        this.fontDictionary.put(name, reference);
        return name;
    }

    PdfName addXObject(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        this.xObjectDictionary.put(name, reference);
        return name;
    }

    PdfName addColor(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        this.colorDictionary.put(name, reference);
        return name;
    }

    void addDefaultColor(PdfName name, PdfObject obj) {
        if (obj == null || obj.isNull()) {
            this.colorDictionary.remove(name);
        } else {
            this.colorDictionary.put(name, obj);
        }
    }

    void addDefaultColor(PdfDictionary dic) {
        this.colorDictionary.merge(dic);
    }

    void addDefaultColorDiff(PdfDictionary dic) {
        this.colorDictionary.mergeDifferent(dic);
    }

    PdfName addShading(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        this.shadingDictionary.put(name, reference);
        return name;
    }

    PdfName addPattern(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        this.patternDictionary.put(name, reference);
        return name;
    }

    PdfName addExtGState(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        this.extGStateDictionary.put(name, reference);
        return name;
    }

    PdfName addProperty(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        this.propertyDictionary.put(name, reference);
        return name;
    }

    PdfDictionary getResources() {
        PdfResources resources = new PdfResources();
        if (this.originalResources != null) {
            resources.putAll(this.originalResources);
        }
        resources.add(PdfName.FONT, this.fontDictionary);
        resources.add(PdfName.XOBJECT, this.xObjectDictionary);
        resources.add(PdfName.COLORSPACE, this.colorDictionary);
        resources.add(PdfName.PATTERN, this.patternDictionary);
        resources.add(PdfName.SHADING, this.shadingDictionary);
        resources.add(PdfName.EXTGSTATE, this.extGStateDictionary);
        resources.add(PdfName.PROPERTIES, this.propertyDictionary);
        return resources;
    }

    boolean hasResources() {
        return this.fontDictionary.size() > 0 || this.xObjectDictionary.size() > 0 || this.colorDictionary.size() > 0 || this.patternDictionary.size() > 0 || this.shadingDictionary.size() > 0 || this.extGStateDictionary.size() > 0 || this.propertyDictionary.size() > 0;
    }
}
