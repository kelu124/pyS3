package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

public class PdfStructTreeController {
    private PdfIndirectReference nullReference = null;
    private PdfDictionary parentTree;
    protected PdfReader reader;
    private PdfDictionary roleMap = null;
    private PdfDictionary sourceClassMap = null;
    private PdfDictionary sourceRoleMap = null;
    private PdfDictionary structTreeRoot;
    private PdfStructureTreeRoot structureTreeRoot;
    private PdfCopy writer;

    public enum returnType {
        BELOW,
        FOUND,
        ABOVE,
        NOTFOUND
    }

    protected PdfStructTreeController(PdfReader reader, PdfCopy writer) throws BadPdfFormatException {
        if (writer.isTagged()) {
            this.writer = writer;
            this.structureTreeRoot = writer.getStructureTreeRoot();
            this.structureTreeRoot.put(PdfName.PARENTTREE, new PdfDictionary(PdfName.STRUCTELEM));
            setReader(reader);
            return;
        }
        throw new BadPdfFormatException(MessageLocalization.getComposedMessage("no.structtreeroot.found", new Object[0]));
    }

    protected void setReader(PdfReader reader) throws BadPdfFormatException {
        this.reader = reader;
        PdfObject obj = getDirectObject(reader.getCatalog().get(PdfName.STRUCTTREEROOT));
        if (obj == null || !obj.isDictionary()) {
            throw new BadPdfFormatException(MessageLocalization.getComposedMessage("no.structtreeroot.found", new Object[0]));
        }
        this.structTreeRoot = (PdfDictionary) obj;
        obj = getDirectObject(this.structTreeRoot.get(PdfName.PARENTTREE));
        if (obj == null || !obj.isDictionary()) {
            throw new BadPdfFormatException(MessageLocalization.getComposedMessage("the.document.does.not.contain.parenttree", new Object[0]));
        }
        this.parentTree = (PdfDictionary) obj;
        this.sourceRoleMap = null;
        this.sourceClassMap = null;
        this.nullReference = null;
    }

    public static boolean checkTagged(PdfReader reader) {
        PdfObject obj = getDirectObject(reader.getCatalog().get(PdfName.STRUCTTREEROOT));
        if (obj == null || !obj.isDictionary()) {
            return false;
        }
        obj = getDirectObject(((PdfDictionary) obj).get(PdfName.PARENTTREE));
        if (obj == null || !obj.isDictionary()) {
            return false;
        }
        return true;
    }

    public static PdfObject getDirectObject(PdfObject object) {
        if (object == null) {
            return null;
        }
        while (object.isIndirect()) {
            object = PdfReader.getPdfObjectRelease(object);
        }
        return object;
    }

    public void copyStructTreeForPage(PdfNumber sourceArrayNumber, int newArrayNumber) throws BadPdfFormatException, IOException {
        if (copyPageMarks(this.parentTree, sourceArrayNumber, newArrayNumber) == returnType.NOTFOUND) {
            throw new BadPdfFormatException(MessageLocalization.getComposedMessage("invalid.structparent", new Object[0]));
        }
    }

    private returnType copyPageMarks(PdfDictionary parentTree, PdfNumber arrayNumber, int newArrayNumber) throws BadPdfFormatException, IOException {
        PdfArray pages = (PdfArray) getDirectObject(parentTree.get(PdfName.NUMS));
        if (pages == null) {
            PdfArray kids = (PdfArray) getDirectObject(parentTree.get(PdfName.KIDS));
            if (kids == null) {
                return returnType.NOTFOUND;
            }
            int cur = kids.size() / 2;
            int begin = 0;
            while (true) {
                switch (copyPageMarks((PdfDictionary) getDirectObject(kids.getPdfObject(cur + begin)), arrayNumber, newArrayNumber)) {
                    case FOUND:
                        return returnType.FOUND;
                    case ABOVE:
                        begin += cur;
                        cur /= 2;
                        if (cur == 0) {
                            cur = 1;
                        }
                        if (cur + begin != kids.size()) {
                            break;
                        }
                        return returnType.ABOVE;
                    case BELOW:
                        if (cur + begin != 0) {
                            if (cur != 0) {
                                cur /= 2;
                                break;
                            }
                            return returnType.NOTFOUND;
                        }
                        return returnType.BELOW;
                    default:
                        return returnType.NOTFOUND;
                }
            }
        } else if (pages.size() == 0) {
            return returnType.NOTFOUND;
        } else {
            return findAndCopyMarks(pages, arrayNumber.intValue(), newArrayNumber);
        }
    }

    private returnType findAndCopyMarks(PdfArray pages, int arrayNumber, int newArrayNumber) throws BadPdfFormatException, IOException {
        if (pages.getAsNumber(0).intValue() > arrayNumber) {
            return returnType.BELOW;
        }
        if (pages.getAsNumber(pages.size() - 2).intValue() < arrayNumber) {
            return returnType.ABOVE;
        }
        int cur = pages.size() / 4;
        int begin = 0;
        while (true) {
            int curNumber = pages.getAsNumber((begin + cur) * 2).intValue();
            if (curNumber == arrayNumber) {
                break;
            } else if (curNumber < arrayNumber) {
                begin += cur;
                cur /= 2;
                if (cur == 0) {
                    cur = 1;
                }
                if (cur + begin == pages.size()) {
                    return returnType.NOTFOUND;
                }
            } else if (cur + begin == 0) {
                return returnType.BELOW;
            } else {
                if (cur == 0) {
                    return returnType.NOTFOUND;
                }
                cur /= 2;
            }
        }
        PdfObject obj = pages.getPdfObject(((begin + cur) * 2) + 1);
        PdfObject obj1 = obj;
        while (obj.isIndirect()) {
            obj = PdfReader.getPdfObjectRelease(obj);
        }
        if (obj.isArray()) {
            PdfObject firstNotNullKid = null;
            Iterator i$ = ((PdfArray) obj).iterator();
            while (i$.hasNext()) {
                PdfObject numObj = (PdfObject) i$.next();
                if (numObj.isNull()) {
                    if (this.nullReference == null) {
                        this.nullReference = this.writer.addToBody(new PdfNull()).getIndirectReference();
                    }
                    this.structureTreeRoot.setPageMark(newArrayNumber, this.nullReference);
                } else {
                    PdfObject res = this.writer.copyObject(numObj, true, false);
                    if (firstNotNullKid == null) {
                        firstNotNullKid = res;
                    }
                    this.structureTreeRoot.setPageMark(newArrayNumber, (PdfIndirectReference) res);
                }
            }
            attachStructTreeRootKids(firstNotNullKid);
        } else if (!obj.isDictionary()) {
            return returnType.NOTFOUND;
        } else {
            if (getKDict((PdfDictionary) obj) == null) {
                return returnType.NOTFOUND;
            }
            int i = newArrayNumber;
            this.structureTreeRoot.setAnnotationMark(i, (PdfIndirectReference) this.writer.copyObject(obj1, true, false));
        }
        return returnType.FOUND;
    }

    protected void attachStructTreeRootKids(PdfObject firstNotNullKid) throws IOException, BadPdfFormatException {
        PdfObject structKids = this.structTreeRoot.get(PdfName.f125K);
        if (structKids == null || !(structKids.isArray() || structKids.isIndirect())) {
            addKid(this.structureTreeRoot, firstNotNullKid);
        } else if (structKids.isIndirect()) {
            addKid(structKids);
        } else {
            Iterator i$ = ((PdfArray) structKids).iterator();
            while (i$.hasNext()) {
                addKid((PdfObject) i$.next());
            }
        }
    }

    static PdfDictionary getKDict(PdfDictionary obj) {
        PdfDictionary k = obj.getAsDict(PdfName.f125K);
        if (k == null) {
            PdfArray k1 = obj.getAsArray(PdfName.f125K);
            if (k1 == null) {
                return null;
            }
            for (int i = 0; i < k1.size(); i++) {
                k = k1.getAsDict(i);
                if (k != null && PdfName.OBJR.equals(k.getAsName(PdfName.TYPE))) {
                    return k;
                }
            }
            return null;
        } else if (PdfName.OBJR.equals(k.getAsName(PdfName.TYPE))) {
            return k;
        } else {
            return null;
        }
    }

    private void addKid(PdfObject obj) throws IOException, BadPdfFormatException {
        if (obj.isIndirect()) {
            PRIndirectReference currRef = (PRIndirectReference) obj;
            RefKey key = new RefKey(currRef);
            if (!this.writer.indirects.containsKey(key)) {
                this.writer.copyIndirect(currRef, true, false);
            }
            PdfIndirectReference newKid = ((IndirectReferences) this.writer.indirects.get(key)).getRef();
            if (this.writer.updateRootKids) {
                addKid(this.structureTreeRoot, newKid);
                this.writer.structureTreeRootKidsForReaderImported(this.reader);
            }
        }
    }

    private static PdfArray getDirectArray(PdfArray in) {
        PdfArray out = new PdfArray();
        for (int i = 0; i < in.size(); i++) {
            PdfObject value = getDirectObject(in.getPdfObject(i));
            if (value != null) {
                if (value.isArray()) {
                    out.add(getDirectArray((PdfArray) value));
                } else if (value.isDictionary()) {
                    out.add(getDirectDict((PdfDictionary) value));
                } else {
                    out.add(value);
                }
            }
        }
        return out;
    }

    private static PdfDictionary getDirectDict(PdfDictionary in) {
        PdfDictionary out = new PdfDictionary();
        for (Entry<PdfName, PdfObject> entry : in.hashMap.entrySet()) {
            PdfObject value = getDirectObject((PdfObject) entry.getValue());
            if (value != null) {
                if (value.isArray()) {
                    out.put((PdfName) entry.getKey(), getDirectArray((PdfArray) value));
                } else if (value.isDictionary()) {
                    out.put((PdfName) entry.getKey(), getDirectDict((PdfDictionary) value));
                } else {
                    out.put((PdfName) entry.getKey(), value);
                }
            }
        }
        return out;
    }

    public static boolean compareObjects(PdfObject value1, PdfObject value2) {
        boolean z = true;
        value2 = getDirectObject(value2);
        if (value2 == null || value1.type() != value2.type()) {
            return false;
        }
        if (value1.isBoolean()) {
            if (value1 == value2) {
                return true;
            }
            if (!(value2 instanceof PdfBoolean)) {
                return false;
            }
            return ((PdfBoolean) value1).booleanValue() == ((PdfBoolean) value2).booleanValue();
        } else if (value1.isName()) {
            return value1.equals(value2);
        } else {
            if (value1.isNumber()) {
                if (value1 == value2) {
                    return true;
                }
                if (!(value2 instanceof PdfNumber)) {
                    return false;
                }
                if (((PdfNumber) value1).doubleValue() != ((PdfNumber) value2).doubleValue()) {
                    z = false;
                }
                return z;
            } else if (value1.isNull()) {
                if (value1 == value2) {
                    return true;
                }
                if (value2 instanceof PdfNull) {
                    return true;
                }
                return false;
            } else if (value1.isString()) {
                if (value1 == value2) {
                    return true;
                }
                if (!(value2 instanceof PdfString)) {
                    return false;
                }
                if ((((PdfString) value2).value != null || ((PdfString) value1).value != null) && (((PdfString) value1).value == null || !((PdfString) value1).value.equals(((PdfString) value2).value))) {
                    return false;
                }
                return true;
            } else if (value1.isArray()) {
                PdfArray array1 = (PdfArray) value1;
                PdfArray array2 = (PdfArray) value2;
                if (array1.size() != array2.size()) {
                    return false;
                }
                for (int i = 0; i < array1.size(); i++) {
                    if (!compareObjects(array1.getPdfObject(i), array2.getPdfObject(i))) {
                        return false;
                    }
                }
                return true;
            } else if (!value1.isDictionary()) {
                return false;
            } else {
                PdfDictionary first = (PdfDictionary) value1;
                PdfDictionary second = (PdfDictionary) value2;
                if (first.size() != second.size()) {
                    return false;
                }
                for (PdfName name : first.hashMap.keySet()) {
                    if (!compareObjects(first.get(name), second.get(name))) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    protected void addClass(PdfObject object) throws BadPdfFormatException {
        object = getDirectObject(object);
        if (object.isDictionary()) {
            PdfObject curClass = ((PdfDictionary) object).get(PdfName.f119C);
            if (curClass != null) {
                if (curClass.isArray()) {
                    PdfArray array = (PdfArray) curClass;
                    for (int i = 0; i < array.size(); i++) {
                        addClass(array.getPdfObject(i));
                    }
                } else if (curClass.isName()) {
                    addClass(curClass);
                }
            }
        } else if (object.isName()) {
            PdfName name = (PdfName) object;
            if (this.sourceClassMap == null) {
                object = getDirectObject(this.structTreeRoot.get(PdfName.CLASSMAP));
                if (object != null && object.isDictionary()) {
                    this.sourceClassMap = (PdfDictionary) object;
                } else {
                    return;
                }
            }
            object = getDirectObject(this.sourceClassMap.get(name));
            if (object != null) {
                PdfObject put = this.structureTreeRoot.getMappedClass(name);
                if (put != null) {
                    if (!compareObjects(put, object)) {
                        throw new BadPdfFormatException(MessageLocalization.getComposedMessage("conflict.in.classmap", name));
                    }
                } else if (object.isDictionary()) {
                    this.structureTreeRoot.mapClass(name, getDirectDict((PdfDictionary) object));
                } else if (object.isArray()) {
                    this.structureTreeRoot.mapClass(name, getDirectArray((PdfArray) object));
                }
            }
        }
    }

    protected void addRole(PdfName structType) throws BadPdfFormatException {
        if (structType != null) {
            PdfObject object;
            for (PdfName name : this.writer.getStandardStructElems()) {
                if (name.equals(structType)) {
                    return;
                }
            }
            if (this.sourceRoleMap == null) {
                object = getDirectObject(this.structTreeRoot.get(PdfName.ROLEMAP));
                if (object != null && object.isDictionary()) {
                    this.sourceRoleMap = (PdfDictionary) object;
                } else {
                    return;
                }
            }
            object = this.sourceRoleMap.get(structType);
            if (object != null && object.isName()) {
                if (this.roleMap == null) {
                    this.roleMap = new PdfDictionary();
                    this.structureTreeRoot.put(PdfName.ROLEMAP, this.roleMap);
                    this.roleMap.put(structType, object);
                    return;
                }
                PdfObject currentRole = this.roleMap.get(structType);
                if (currentRole == null) {
                    this.roleMap.put(structType, object);
                } else if (!currentRole.equals(object)) {
                    throw new BadPdfFormatException(MessageLocalization.getComposedMessage("conflict.in.rolemap", object));
                }
            }
        }
    }

    protected void addKid(PdfDictionary parent, PdfObject kid) {
        PdfArray kids;
        PdfObject kidObj = parent.get(PdfName.f125K);
        if (kidObj instanceof PdfArray) {
            kids = (PdfArray) kidObj;
        } else {
            kids = new PdfArray();
            if (kidObj != null) {
                kids.add(kidObj);
            }
        }
        kids.add(kid);
        parent.put(PdfName.f125K, kids);
    }
}
