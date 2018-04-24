package com.itextpdf.text.pdf.security;

import org.spongycastle.cms.Recipient;
import org.spongycastle.cms.RecipientId;

public interface ExternalDecryptionProcess {
    Recipient getCmsRecipient();

    RecipientId getCmsRecipientId();
}
