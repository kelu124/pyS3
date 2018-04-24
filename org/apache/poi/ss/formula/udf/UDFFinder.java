package org.apache.poi.ss.formula.udf;

import org.apache.poi.ss.formula.atp.AnalysisToolPak;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

public interface UDFFinder {
    @Deprecated
    public static final UDFFinder DEFAULT = new AggregatingUDFFinder(new UDFFinder[]{AnalysisToolPak.instance});

    FreeRefFunction findFunction(String str);
}
