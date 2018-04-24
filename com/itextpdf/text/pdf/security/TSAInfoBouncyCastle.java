package com.itextpdf.text.pdf.security;

import org.spongycastle.tsp.TimeStampTokenInfo;

public interface TSAInfoBouncyCastle {
    void inspectTimeStampTokenInfo(TimeStampTokenInfo timeStampTokenInfo);
}
