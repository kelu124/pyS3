package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.util.Internal;

@Internal
public interface EvaluationWorkbook {
    void clearAllCachedResultValues();

    int convertFromExternSheetIndex(int i);

    ExternalName getExternalName(int i, int i2);

    ExternalName getExternalName(String str, String str2, int i);

    ExternalSheet getExternalSheet(int i);

    ExternalSheet getExternalSheet(String str, String str2, int i);

    Ptg[] getFormulaTokens(EvaluationCell evaluationCell);

    EvaluationName getName(String str, int i);

    EvaluationName getName(NamePtg namePtg);

    EvaluationSheet getSheet(int i);

    int getSheetIndex(String str);

    int getSheetIndex(EvaluationSheet evaluationSheet);

    String getSheetName(int i);

    UDFFinder getUDFFinder();

    String resolveNameXText(NameXPtg nameXPtg);
}
