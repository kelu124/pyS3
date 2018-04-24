package com.itextpdf.text;

public interface FontProvider {
    Font getFont(String str, String str2, boolean z, float f, int i, BaseColor baseColor);

    boolean isRegistered(String str);
}
