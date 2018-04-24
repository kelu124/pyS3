package org.apache.poi.hssf.usermodel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.FormatRecord;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormat;

public final class HSSFDataFormat implements DataFormat {
    private static final String[] _builtinFormats = BuiltinFormats.getAll();
    private final Vector<String> _formats = new Vector();
    private boolean _movedBuiltins = false;
    private final InternalWorkbook _workbook;

    HSSFDataFormat(InternalWorkbook workbook) {
        this._workbook = workbook;
        for (FormatRecord r : workbook.getFormats()) {
            ensureFormatsSize(r.getIndexCode());
            this._formats.set(r.getIndexCode(), r.getFormatString());
        }
    }

    public static List<String> getBuiltinFormats() {
        return Arrays.asList(_builtinFormats);
    }

    public static short getBuiltinFormat(String format) {
        return (short) BuiltinFormats.getBuiltinFormat(format);
    }

    public short getFormat(String pFormat) {
        String format;
        int i;
        if (pFormat.toUpperCase(Locale.ROOT).equals("TEXT")) {
            format = "@";
        } else {
            format = pFormat;
        }
        if (!this._movedBuiltins) {
            for (i = 0; i < _builtinFormats.length; i++) {
                ensureFormatsSize(i);
                if (this._formats.get(i) == null) {
                    this._formats.set(i, _builtinFormats[i]);
                }
            }
            this._movedBuiltins = true;
        }
        for (i = 0; i < this._formats.size(); i++) {
            if (format.equals(this._formats.get(i))) {
                return (short) i;
            }
        }
        short index = this._workbook.getFormat(format, true);
        ensureFormatsSize(index);
        this._formats.set(index, format);
        return index;
    }

    public String getFormat(short index) {
        String fmt = null;
        if (this._movedBuiltins) {
            return (String) this._formats.get(index);
        }
        if (index == (short) -1) {
            return null;
        }
        if (this._formats.size() > index) {
            fmt = (String) this._formats.get(index);
        }
        if (_builtinFormats.length <= index || _builtinFormats[index] == null || fmt != null) {
            return fmt;
        }
        return _builtinFormats[index];
    }

    public static String getBuiltinFormat(short index) {
        return BuiltinFormats.getBuiltinFormat((int) index);
    }

    public static int getNumberOfBuiltinBuiltinFormats() {
        return _builtinFormats.length;
    }

    private void ensureFormatsSize(int index) {
        if (this._formats.size() <= index) {
            this._formats.setSize(index + 1);
        }
    }
}
