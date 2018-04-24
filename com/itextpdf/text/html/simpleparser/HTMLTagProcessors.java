package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.html.HtmlTags;
import java.util.HashMap;

@Deprecated
public class HTMLTagProcessors extends HashMap<String, HTMLTagProcessor> {
    public static final HTMLTagProcessor f38A = new 2();
    public static final HTMLTagProcessor BR = new 3();
    public static final HTMLTagProcessor DIV = new 10();
    public static final HTMLTagProcessor EM_STRONG_STRIKE_SUP_SUP = new 1();
    public static final HTMLTagProcessor f39H = new 7();
    public static final HTMLTagProcessor HR = new 5();
    public static final HTMLTagProcessor IMG = new 14();
    public static final HTMLTagProcessor LI = new 8();
    public static final HTMLTagProcessor PRE = new 9();
    public static final HTMLTagProcessor SPAN = new 6();
    public static final HTMLTagProcessor TABLE = new 11();
    public static final HTMLTagProcessor TD = new 13();
    public static final HTMLTagProcessor TR = new 12();
    public static final HTMLTagProcessor UL_OL = new 4();
    private static final long serialVersionUID = -959260811961222824L;

    public HTMLTagProcessors() {
        put(HtmlTags.f32A, f38A);
        put(HtmlTags.f33B, EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.BODY, DIV);
        put(HtmlTags.BR, BR);
        put(HtmlTags.DIV, DIV);
        put(HtmlTags.EM, EM_STRONG_STRIKE_SUP_SUP);
        put("font", SPAN);
        put(HtmlTags.H1, f39H);
        put(HtmlTags.H2, f39H);
        put(HtmlTags.H3, f39H);
        put(HtmlTags.H4, f39H);
        put(HtmlTags.H5, f39H);
        put(HtmlTags.H6, f39H);
        put(HtmlTags.HR, HR);
        put("i", EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.IMG, IMG);
        put(HtmlTags.LI, LI);
        put(HtmlTags.OL, UL_OL);
        put(HtmlTags.f35P, DIV);
        put(HtmlTags.PRE, PRE);
        put(HtmlTags.f36S, EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.SPAN, SPAN);
        put(HtmlTags.STRIKE, EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.STRONG, EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.SUB, EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.SUP, EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.TABLE, TABLE);
        put(HtmlTags.TD, TD);
        put(HtmlTags.TH, TD);
        put(HtmlTags.TR, TR);
        put(HtmlTags.f37U, EM_STRONG_STRIKE_SUP_SUP);
        put(HtmlTags.UL, UL_OL);
    }
}
