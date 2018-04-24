package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.OutputStream;

class PdfCopyFormsImp extends PdfCopyFieldsImp {
    PdfCopyFormsImp(OutputStream os) throws DocumentException {
        super(os);
    }

    public void copyDocumentFields(PdfReader reader) throws DocumentException {
        if (reader.isOpenedWithFullPermissions()) {
            if (this.readers2intrefs.containsKey(reader)) {
                reader = new PdfReader(reader);
            } else if (reader.isTampered()) {
                throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused", new Object[0]));
            } else {
                reader.consolidateNamedDestinations();
                reader.setTampered(true);
            }
            reader.shuffleSubsetNames();
            this.readers2intrefs.put(reader, new IntHashtable());
            this.visited.put(reader, new IntHashtable());
            this.fields.add(reader.getAcroFields());
            updateCalculationOrder(reader);
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0]));
    }

    void mergeFields() {
        for (int k = 0; k < this.fields.size(); k++) {
            mergeWithMaster(((AcroFields) this.fields.get(k)).getFields());
        }
    }
}
