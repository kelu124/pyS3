package org.apache.poi.hssf.util;

import org.apache.poi.ss.util.CellReference;

@Deprecated
public final class AreaReference extends org.apache.poi.ss.util.AreaReference {
    public AreaReference(String reference) {
        super(reference);
    }

    public AreaReference(CellReference topLeft, CellReference botRight) {
        super((CellReference) topLeft, (CellReference) botRight);
    }
}
