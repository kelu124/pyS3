package org.apache.poi.hssf.usermodel;

import java.util.Locale;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.LocaleUtil;

public final class HSSFDataFormatter extends DataFormatter {
    public HSSFDataFormatter(Locale locale) {
        super(locale);
    }

    public HSSFDataFormatter() {
        this(LocaleUtil.getUserLocale());
    }
}
