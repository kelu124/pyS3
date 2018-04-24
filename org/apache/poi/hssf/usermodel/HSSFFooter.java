package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.FooterRecord;
import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
import org.apache.poi.ss.usermodel.Footer;

public final class HSSFFooter extends HeaderFooter implements Footer {
    private final PageSettingsBlock _psb;

    protected HSSFFooter(PageSettingsBlock psb) {
        this._psb = psb;
    }

    protected String getRawText() {
        FooterRecord hf = this._psb.getFooter();
        if (hf == null) {
            return "";
        }
        return hf.getText();
    }

    protected void setHeaderFooterText(String text) {
        FooterRecord hfr = this._psb.getFooter();
        if (hfr == null) {
            this._psb.setFooter(new FooterRecord(text));
            return;
        }
        hfr.setText(text);
    }
}
