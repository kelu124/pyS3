package org.apache.poi.ss.usermodel;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.util.Removal;

public interface CreationHelper {
    ClientAnchor createClientAnchor();

    DataFormat createDataFormat();

    ExtendedColor createExtendedColor();

    FormulaEvaluator createFormulaEvaluator();

    @Deprecated
    @Removal(version = "3.17")
    Hyperlink createHyperlink(int i);

    Hyperlink createHyperlink(HyperlinkType hyperlinkType);

    RichTextString createRichTextString(String str);
}
