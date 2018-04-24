package org.apache.poi.hssf.record.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.cont.ContinuableRecordInput;
import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.StringUtil;

public class UnicodeString implements Comparable<UnicodeString> {
    private static POILogger _logger = POILogFactory.getLogger(UnicodeString.class);
    private static final BitField extBit = BitFieldFactory.getInstance(4);
    private static final BitField highByte = BitFieldFactory.getInstance(1);
    private static final BitField richText = BitFieldFactory.getInstance(8);
    private short field_1_charCount;
    private byte field_2_optionflags;
    private String field_3_string;
    private List<FormatRun> field_4_format_runs;
    private ExtRst field_5_ext_rst;

    public static class ExtRst implements Comparable<ExtRst> {
        private byte[] extraData;
        private short formattingFontIndex;
        private short formattingOptions;
        private int numberOfRuns;
        private PhRun[] phRuns;
        private String phoneticText;
        private short reserved;

        private void populateEmpty() {
            this.reserved = (short) 1;
            this.phoneticText = "";
            this.phRuns = new PhRun[0];
            this.extraData = new byte[0];
        }

        protected ExtRst() {
            populateEmpty();
        }

        protected ExtRst(LittleEndianInput in, int expectedLength) {
            this.reserved = in.readShort();
            if (this.reserved == (short) -1) {
                populateEmpty();
            } else if (this.reserved != (short) 1) {
                UnicodeString._logger.log(5, new Object[]{"Warning - ExtRst has wrong magic marker, expecting 1 but found " + this.reserved + " - ignoring"});
                for (i = 0; i < expectedLength - 2; i++) {
                    in.readByte();
                }
                populateEmpty();
            } else {
                short stringDataSize = in.readShort();
                this.formattingFontIndex = in.readShort();
                this.formattingOptions = in.readShort();
                this.numberOfRuns = in.readUShort();
                short length1 = in.readShort();
                short length2 = in.readShort();
                if (length1 == (short) 0 && length2 > (short) 0) {
                    length2 = (short) 0;
                }
                if (length1 != length2) {
                    throw new IllegalStateException("The two length fields of the Phonetic Text don't agree! " + length1 + " vs " + length2);
                }
                this.phoneticText = StringUtil.readUnicodeLE(in, length1);
                int runData = ((stringDataSize - 4) - 6) - (this.phoneticText.length() * 2);
                int numRuns = runData / 6;
                this.phRuns = new PhRun[numRuns];
                for (i = 0; i < this.phRuns.length; i++) {
                    this.phRuns[i] = new PhRun(in);
                }
                int extraDataLength = runData - (numRuns * 6);
                if (extraDataLength < 0) {
                    UnicodeString._logger.log(5, new Object[]{"Warning - ExtRst overran by " + (0 - extraDataLength) + " bytes"});
                    extraDataLength = 0;
                }
                this.extraData = new byte[extraDataLength];
                for (i = 0; i < this.extraData.length; i++) {
                    this.extraData[i] = in.readByte();
                }
            }
        }

        protected int getDataSize() {
            return (((this.phoneticText.length() * 2) + 10) + (this.phRuns.length * 6)) + this.extraData.length;
        }

        protected void serialize(ContinuableRecordOutput out) {
            int dataSize = getDataSize();
            out.writeContinueIfRequired(8);
            out.writeShort(this.reserved);
            out.writeShort(dataSize);
            out.writeShort(this.formattingFontIndex);
            out.writeShort(this.formattingOptions);
            out.writeContinueIfRequired(6);
            out.writeShort(this.numberOfRuns);
            out.writeShort(this.phoneticText.length());
            out.writeShort(this.phoneticText.length());
            out.writeContinueIfRequired(this.phoneticText.length() * 2);
            StringUtil.putUnicodeLE(this.phoneticText, out);
            for (PhRun access$200 : this.phRuns) {
                access$200.serialize(out);
            }
            out.write(this.extraData);
        }

        public boolean equals(Object obj) {
            if ((obj instanceof ExtRst) && compareTo((ExtRst) obj) == 0) {
                return true;
            }
            return false;
        }

        public int compareTo(ExtRst o) {
            int result = this.reserved - o.reserved;
            if (result != 0) {
                return result;
            }
            result = this.formattingFontIndex - o.formattingFontIndex;
            if (result != 0) {
                return result;
            }
            result = this.formattingOptions - o.formattingOptions;
            if (result != 0) {
                return result;
            }
            result = this.numberOfRuns - o.numberOfRuns;
            if (result != 0) {
                return result;
            }
            result = this.phoneticText.compareTo(o.phoneticText);
            if (result != 0) {
                return result;
            }
            result = this.phRuns.length - o.phRuns.length;
            if (result != 0) {
                return result;
            }
            for (int i = 0; i < this.phRuns.length; i++) {
                result = this.phRuns[i].phoneticTextFirstCharacterOffset - o.phRuns[i].phoneticTextFirstCharacterOffset;
                if (result != 0) {
                    return result;
                }
                result = this.phRuns[i].realTextFirstCharacterOffset - o.phRuns[i].realTextFirstCharacterOffset;
                if (result != 0) {
                    return result;
                }
                result = this.phRuns[i].realTextLength - o.phRuns[i].realTextLength;
                if (result != 0) {
                    return result;
                }
            }
            return Arrays.hashCode(this.extraData) - Arrays.hashCode(o.extraData);
        }

        public int hashCode() {
            int hash = (((((((this.reserved * 31) + this.formattingFontIndex) * 31) + this.formattingOptions) * 31) + this.numberOfRuns) * 31) + this.phoneticText.hashCode();
            if (this.phRuns != null) {
                for (PhRun ph : this.phRuns) {
                    hash = (((((hash * 31) + ph.phoneticTextFirstCharacterOffset) * 31) + ph.realTextFirstCharacterOffset) * 31) + ph.realTextLength;
                }
            }
            return hash;
        }

        protected ExtRst clone() {
            ExtRst ext = new ExtRst();
            ext.reserved = this.reserved;
            ext.formattingFontIndex = this.formattingFontIndex;
            ext.formattingOptions = this.formattingOptions;
            ext.numberOfRuns = this.numberOfRuns;
            ext.phoneticText = this.phoneticText;
            ext.phRuns = new PhRun[this.phRuns.length];
            for (int i = 0; i < ext.phRuns.length; i++) {
                ext.phRuns[i] = new PhRun(this.phRuns[i].phoneticTextFirstCharacterOffset, this.phRuns[i].realTextFirstCharacterOffset, this.phRuns[i].realTextLength);
            }
            return ext;
        }

        public short getFormattingFontIndex() {
            return this.formattingFontIndex;
        }

        public short getFormattingOptions() {
            return this.formattingOptions;
        }

        public int getNumberOfRuns() {
            return this.numberOfRuns;
        }

        public String getPhoneticText() {
            return this.phoneticText;
        }

        public PhRun[] getPhRuns() {
            return this.phRuns;
        }
    }

    public static class FormatRun implements Comparable<FormatRun> {
        static final /* synthetic */ boolean $assertionsDisabled = (!UnicodeString.class.desiredAssertionStatus());
        final short _character;
        short _fontIndex;

        public FormatRun(short character, short fontIndex) {
            this._character = character;
            this._fontIndex = fontIndex;
        }

        public FormatRun(LittleEndianInput in) {
            this(in.readShort(), in.readShort());
        }

        public short getCharacterPos() {
            return this._character;
        }

        public short getFontIndex() {
            return this._fontIndex;
        }

        public boolean equals(Object o) {
            if (!(o instanceof FormatRun)) {
                return false;
            }
            FormatRun other = (FormatRun) o;
            if (this._character == other._character && this._fontIndex == other._fontIndex) {
                return true;
            }
            return false;
        }

        public int compareTo(FormatRun r) {
            if (this._character == r._character && this._fontIndex == r._fontIndex) {
                return 0;
            }
            if (this._character == r._character) {
                return this._fontIndex - r._fontIndex;
            }
            return this._character - r._character;
        }

        public int hashCode() {
            if ($assertionsDisabled) {
                return 42;
            }
            throw new AssertionError("hashCode not designed");
        }

        public String toString() {
            return "character=" + this._character + ",fontIndex=" + this._fontIndex;
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(this._character);
            out.writeShort(this._fontIndex);
        }
    }

    public static class PhRun {
        private int phoneticTextFirstCharacterOffset;
        private int realTextFirstCharacterOffset;
        private int realTextLength;

        public PhRun(int phoneticTextFirstCharacterOffset, int realTextFirstCharacterOffset, int realTextLength) {
            this.phoneticTextFirstCharacterOffset = phoneticTextFirstCharacterOffset;
            this.realTextFirstCharacterOffset = realTextFirstCharacterOffset;
            this.realTextLength = realTextLength;
        }

        private PhRun(LittleEndianInput in) {
            this.phoneticTextFirstCharacterOffset = in.readUShort();
            this.realTextFirstCharacterOffset = in.readUShort();
            this.realTextLength = in.readUShort();
        }

        private void serialize(ContinuableRecordOutput out) {
            out.writeContinueIfRequired(6);
            out.writeShort(this.phoneticTextFirstCharacterOffset);
            out.writeShort(this.realTextFirstCharacterOffset);
            out.writeShort(this.realTextLength);
        }
    }

    private UnicodeString() {
    }

    public UnicodeString(String str) {
        setString(str);
    }

    public int hashCode() {
        int stringHash = 0;
        if (this.field_3_string != null) {
            stringHash = this.field_3_string.hashCode();
        }
        return this.field_1_charCount + stringHash;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (!(o instanceof UnicodeString)) {
            return false;
        }
        UnicodeString other = (UnicodeString) o;
        if (this.field_1_charCount != other.field_1_charCount || this.field_2_optionflags != other.field_2_optionflags || !this.field_3_string.equals(other.field_3_string)) {
            return false;
        }
        if (this.field_4_format_runs == null) {
            if (other.field_4_format_runs != null) {
                z = false;
            }
            return z;
        } else if (other.field_4_format_runs == null) {
            return false;
        } else {
            int size = this.field_4_format_runs.size();
            if (size != other.field_4_format_runs.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!((FormatRun) this.field_4_format_runs.get(i)).equals((FormatRun) other.field_4_format_runs.get(i))) {
                    return false;
                }
            }
            if (this.field_5_ext_rst == null) {
                if (other.field_5_ext_rst != null) {
                    z = false;
                }
                return z;
            } else if (other.field_5_ext_rst != null) {
                return this.field_5_ext_rst.equals(other.field_5_ext_rst);
            } else {
                return false;
            }
        }
    }

    public UnicodeString(RecordInputStream in) {
        boolean isCompressed;
        this.field_1_charCount = in.readShort();
        this.field_2_optionflags = in.readByte();
        int runCount = 0;
        int extensionLength = 0;
        if (isRichText()) {
            runCount = in.readShort();
        }
        if (isExtendedText()) {
            extensionLength = in.readInt();
        }
        if ((this.field_2_optionflags & 1) == 0) {
            isCompressed = true;
        } else {
            isCompressed = false;
        }
        int cc = getCharCount();
        this.field_3_string = isCompressed ? in.readCompressedUnicode(cc) : in.readUnicodeLEString(cc);
        if (isRichText() && runCount > 0) {
            this.field_4_format_runs = new ArrayList(runCount);
            for (int i = 0; i < runCount; i++) {
                this.field_4_format_runs.add(new FormatRun(in));
            }
        }
        if (isExtendedText() && extensionLength > 0) {
            this.field_5_ext_rst = new ExtRst(new ContinuableRecordInput(in), extensionLength);
            if (this.field_5_ext_rst.getDataSize() + 4 != extensionLength) {
                _logger.log(5, new Object[]{"ExtRst was supposed to be " + extensionLength + " bytes long, but seems to actually be " + (this.field_5_ext_rst.getDataSize() + 4)});
            }
        }
    }

    public int getCharCount() {
        if (this.field_1_charCount < (short) 0) {
            return this.field_1_charCount + 65536;
        }
        return this.field_1_charCount;
    }

    public short getCharCountShort() {
        return this.field_1_charCount;
    }

    public void setCharCount(short cc) {
        this.field_1_charCount = cc;
    }

    public byte getOptionFlags() {
        return this.field_2_optionflags;
    }

    public void setOptionFlags(byte of) {
        this.field_2_optionflags = of;
    }

    public String getString() {
        return this.field_3_string;
    }

    public void setString(String string) {
        this.field_3_string = string;
        setCharCount((short) this.field_3_string.length());
        boolean useUTF16 = false;
        int strlen = string.length();
        for (int j = 0; j < strlen; j++) {
            if (string.charAt(j) > 'ÿ') {
                useUTF16 = true;
                break;
            }
        }
        if (useUTF16) {
            this.field_2_optionflags = highByte.setByte(this.field_2_optionflags);
        } else {
            this.field_2_optionflags = highByte.clearByte(this.field_2_optionflags);
        }
    }

    public int getFormatRunCount() {
        return this.field_4_format_runs == null ? 0 : this.field_4_format_runs.size();
    }

    public FormatRun getFormatRun(int index) {
        if (this.field_4_format_runs != null && index >= 0 && index < this.field_4_format_runs.size()) {
            return (FormatRun) this.field_4_format_runs.get(index);
        }
        return null;
    }

    private int findFormatRunAt(int characterPos) {
        int size = this.field_4_format_runs.size();
        for (int i = 0; i < size; i++) {
            FormatRun r = (FormatRun) this.field_4_format_runs.get(i);
            if (r._character == characterPos) {
                return i;
            }
            if (r._character > characterPos) {
                return -1;
            }
        }
        return -1;
    }

    public void addFormatRun(FormatRun r) {
        if (this.field_4_format_runs == null) {
            this.field_4_format_runs = new ArrayList();
        }
        int index = findFormatRunAt(r._character);
        if (index != -1) {
            this.field_4_format_runs.remove(index);
        }
        this.field_4_format_runs.add(r);
        Collections.sort(this.field_4_format_runs);
        this.field_2_optionflags = richText.setByte(this.field_2_optionflags);
    }

    public Iterator<FormatRun> formatIterator() {
        if (this.field_4_format_runs != null) {
            return this.field_4_format_runs.iterator();
        }
        return null;
    }

    public void removeFormatRun(FormatRun r) {
        this.field_4_format_runs.remove(r);
        if (this.field_4_format_runs.size() == 0) {
            this.field_4_format_runs = null;
            this.field_2_optionflags = richText.clearByte(this.field_2_optionflags);
        }
    }

    public void clearFormatting() {
        this.field_4_format_runs = null;
        this.field_2_optionflags = richText.clearByte(this.field_2_optionflags);
    }

    public ExtRst getExtendedRst() {
        return this.field_5_ext_rst;
    }

    void setExtendedRst(ExtRst ext_rst) {
        if (ext_rst != null) {
            this.field_2_optionflags = extBit.setByte(this.field_2_optionflags);
        } else {
            this.field_2_optionflags = extBit.clearByte(this.field_2_optionflags);
        }
        this.field_5_ext_rst = ext_rst;
    }

    public void swapFontUse(short oldFontIndex, short newFontIndex) {
        for (FormatRun run : this.field_4_format_runs) {
            if (run._fontIndex == oldFontIndex) {
                run._fontIndex = newFontIndex;
            }
        }
    }

    public String toString() {
        return getString();
    }

    public String getDebugInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[UNICODESTRING]\n");
        buffer.append("    .charcount       = ").append(Integer.toHexString(getCharCount())).append("\n");
        buffer.append("    .optionflags     = ").append(Integer.toHexString(getOptionFlags())).append("\n");
        buffer.append("    .string          = ").append(getString()).append("\n");
        if (this.field_4_format_runs != null) {
            for (int i = 0; i < this.field_4_format_runs.size(); i++) {
                buffer.append("      .format_run" + i + "          = ").append(((FormatRun) this.field_4_format_runs.get(i)).toString()).append("\n");
            }
        }
        if (this.field_5_ext_rst != null) {
            buffer.append("    .field_5_ext_rst          = ").append("\n");
            buffer.append(this.field_5_ext_rst.toString()).append("\n");
        }
        buffer.append("[/UNICODESTRING]\n");
        return buffer.toString();
    }

    public void serialize(ContinuableRecordOutput out) {
        int numberOfRichTextRuns = 0;
        int extendedDataSize = 0;
        if (isRichText() && this.field_4_format_runs != null) {
            numberOfRichTextRuns = this.field_4_format_runs.size();
        }
        if (isExtendedText() && this.field_5_ext_rst != null) {
            extendedDataSize = this.field_5_ext_rst.getDataSize() + 4;
        }
        out.writeString(this.field_3_string, numberOfRichTextRuns, extendedDataSize);
        if (numberOfRichTextRuns > 0) {
            for (int i = 0; i < numberOfRichTextRuns; i++) {
                if (out.getAvailableSpace() < 4) {
                    out.writeContinue();
                }
                ((FormatRun) this.field_4_format_runs.get(i)).serialize(out);
            }
        }
        if (extendedDataSize > 0) {
            this.field_5_ext_rst.serialize(out);
        }
    }

    public int compareTo(UnicodeString str) {
        int result = getString().compareTo(str.getString());
        if (result != 0) {
            return result;
        }
        if (this.field_4_format_runs == null) {
            if (str.field_4_format_runs != null) {
                return 1;
            }
            return 0;
        } else if (str.field_4_format_runs == null) {
            return -1;
        } else {
            int size = this.field_4_format_runs.size();
            if (size != str.field_4_format_runs.size()) {
                return size - str.field_4_format_runs.size();
            }
            for (int i = 0; i < size; i++) {
                result = ((FormatRun) this.field_4_format_runs.get(i)).compareTo((FormatRun) str.field_4_format_runs.get(i));
                if (result != 0) {
                    return result;
                }
            }
            if (this.field_5_ext_rst == null) {
                if (str.field_5_ext_rst != null) {
                    return 1;
                }
                return 0;
            } else if (str.field_5_ext_rst == null) {
                return -1;
            } else {
                return this.field_5_ext_rst.compareTo(str.field_5_ext_rst);
            }
        }
    }

    private boolean isRichText() {
        return richText.isSet(getOptionFlags());
    }

    private boolean isExtendedText() {
        return extBit.isSet(getOptionFlags());
    }

    public Object clone() {
        UnicodeString str = new UnicodeString();
        str.field_1_charCount = this.field_1_charCount;
        str.field_2_optionflags = this.field_2_optionflags;
        str.field_3_string = this.field_3_string;
        if (this.field_4_format_runs != null) {
            str.field_4_format_runs = new ArrayList();
            for (FormatRun r : this.field_4_format_runs) {
                str.field_4_format_runs.add(new FormatRun(r._character, r._fontIndex));
            }
        }
        if (this.field_5_ext_rst != null) {
            str.field_5_ext_rst = this.field_5_ext_rst.clone();
        }
        return str;
    }
}
