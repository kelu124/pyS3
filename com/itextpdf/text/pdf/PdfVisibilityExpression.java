package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;

public class PdfVisibilityExpression extends PdfArray {
    public static final int AND = 1;
    public static final int NOT = -1;
    public static final int OR = 0;

    public PdfVisibilityExpression(int type) {
        switch (type) {
            case -1:
                super.add(PdfName.NOT);
                return;
            case 0:
                super.add(PdfName.OR);
                return;
            case 1:
                super.add(PdfName.AND);
                return;
            default:
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value", new Object[0]));
        }
    }

    public void add(int index, PdfObject element) {
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value", new Object[0]));
    }

    public boolean add(PdfObject object) {
        if (object instanceof PdfLayer) {
            return super.add(((PdfLayer) object).getRef());
        }
        if (object instanceof PdfVisibilityExpression) {
            return super.add(object);
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value", new Object[0]));
    }

    public void addFirst(PdfObject object) {
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value", new Object[0]));
    }

    public boolean add(float[] values) {
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value", new Object[0]));
    }

    public boolean add(int[] values) {
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value", new Object[0]));
    }
}
