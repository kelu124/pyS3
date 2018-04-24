package com.itextpdf.text.pdf;

import android.support.v4.view.ViewCompat;
import com.itextpdf.text.html.HtmlTags;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class CFFFontSubset extends CFFFont {
    static final byte ENDCHAR_OP = (byte) 14;
    static final byte RETURN_OP = (byte) 11;
    static final String[] SubrsEscapeFuncs = new String[]{"RESERVED_0", "RESERVED_1", "RESERVED_2", "and", "or", "not", "RESERVED_6", "RESERVED_7", "RESERVED_8", "abs", "add", HtmlTags.SUB, HtmlTags.DIV, "RESERVED_13", "neg", "eq", "RESERVED_16", "RESERVED_17", "drop", "RESERVED_19", "put", "get", "ifelse", "random", "mul", "RESERVED_25", "sqrt", "dup", "exch", "index", "roll", "RESERVED_31", "RESERVED_32", "RESERVED_33", "hflex", "flex", "hflex1", "flex1", "RESERVED_REST"};
    static final String[] SubrsFunctions = new String[]{"RESERVED_0", "hstem", "RESERVED_2", "vstem", "vmoveto", "rlineto", "hlineto", "vlineto", "rrcurveto", "RESERVED_9", "callsubr", "return", "escape", "RESERVED_13", "endchar", "RESERVED_15", "RESERVED_16", "RESERVED_17", "hstemhm", "hintmask", "cntrmask", "rmoveto", "hmoveto", "vstemhm", "rcurveline", "rlinecurve", "vvcurveto", "hhcurveto", "shortint", "callgsubr", "vhcurveto", "hvcurveto"};
    HashSet<Integer> FDArrayUsed = new HashSet();
    int GBias = 0;
    HashMap<Integer, int[]> GlyphsUsed;
    byte[] NewCharStringsIndex;
    byte[] NewGSubrsIndex;
    byte[][] NewLSubrsIndex;
    byte[] NewSubrsIndexNonCID;
    int NumOfHints = 0;
    LinkedList<Item> OutputList;
    ArrayList<Integer> glyphsInList;
    HashMap<Integer, int[]> hGSubrsUsed = new HashMap();
    HashMap<Integer, int[]>[] hSubrsUsed;
    HashMap<Integer, int[]> hSubrsUsedNonCID = new HashMap();
    ArrayList<Integer> lGSubrsUsed = new ArrayList();
    ArrayList<Integer>[] lSubrsUsed;
    ArrayList<Integer> lSubrsUsedNonCID = new ArrayList();

    public CFFFontSubset(RandomAccessFileOrArray rf, HashMap<Integer, int[]> GlyphsUsed) {
        super(rf);
        this.GlyphsUsed = GlyphsUsed;
        this.glyphsInList = new ArrayList(GlyphsUsed.keySet());
        for (int i = 0; i < this.fonts.length; i++) {
            seek(this.fonts[i].charstringsOffset);
            this.fonts[i].nglyphs = getCard16();
            seek(this.stringIndexOffset);
            this.fonts[i].nstrings = getCard16() + standardStrings.length;
            this.fonts[i].charstringsOffsets = getIndex(this.fonts[i].charstringsOffset);
            if (this.fonts[i].fdselectOffset >= 0) {
                readFDSelect(i);
                BuildFDArrayUsed(i);
            }
            if (this.fonts[i].isCID) {
                ReadFDArray(i);
            }
            this.fonts[i].CharsetLength = CountCharset(this.fonts[i].charsetOffset, this.fonts[i].nglyphs);
        }
    }

    int CountCharset(int Offset, int NumofGlyphs) {
        seek(Offset);
        switch (getCard8()) {
            case 0:
                return (NumofGlyphs * 2) + 1;
            case 1:
                return (CountRange(NumofGlyphs, 1) * 3) + 1;
            case 2:
                return (CountRange(NumofGlyphs, 2) * 4) + 1;
            default:
                return 0;
        }
    }

    int CountRange(int NumofGlyphs, int Type) {
        int num = 0;
        int i = 1;
        while (i < NumofGlyphs) {
            int nLeft;
            num++;
            char Sid = getCard16();
            if (Type == 1) {
                nLeft = getCard8();
            } else {
                nLeft = getCard16();
            }
            i += nLeft + 1;
        }
        return num;
    }

    protected void readFDSelect(int Font) {
        int NumOfGlyphs = this.fonts[Font].nglyphs;
        int[] FDSelect = new int[NumOfGlyphs];
        seek(this.fonts[Font].fdselectOffset);
        this.fonts[Font].FDSelectFormat = getCard8();
        int i;
        switch (this.fonts[Font].FDSelectFormat) {
            case 0:
                for (i = 0; i < NumOfGlyphs; i++) {
                    FDSelect[i] = getCard8();
                }
                this.fonts[Font].FDSelectLength = this.fonts[Font].nglyphs + 1;
                break;
            case 3:
                int nRanges = getCard16();
                int l = 0;
                int first = getCard16();
                for (i = 0; i < nRanges; i++) {
                    int fd = getCard8();
                    int last = getCard16();
                    int steps = last - first;
                    for (int k = 0; k < steps; k++) {
                        FDSelect[l] = fd;
                        l++;
                    }
                    first = last;
                }
                this.fonts[Font].FDSelectLength = ((nRanges * 3) + 3) + 2;
                break;
        }
        this.fonts[Font].FDSelect = FDSelect;
    }

    protected void BuildFDArrayUsed(int Font) {
        int[] FDSelect = this.fonts[Font].FDSelect;
        for (int i = 0; i < this.glyphsInList.size(); i++) {
            this.FDArrayUsed.add(Integer.valueOf(FDSelect[((Integer) this.glyphsInList.get(i)).intValue()]));
        }
    }

    protected void ReadFDArray(int Font) {
        seek(this.fonts[Font].fdarrayOffset);
        this.fonts[Font].FDArrayCount = getCard16();
        this.fonts[Font].FDArrayOffsize = getCard8();
        if (this.fonts[Font].FDArrayOffsize < 4) {
            Font font = this.fonts[Font];
            font.FDArrayOffsize++;
        }
        this.fonts[Font].FDArrayOffsets = getIndex(this.fonts[Font].fdarrayOffset);
    }

    public byte[] Process(String fontName) throws IOException {
        try {
            byte[] bArr;
            this.buf.reOpen();
            int j = 0;
            while (j < this.fonts.length && !fontName.equals(this.fonts[j].name)) {
                j++;
            }
            if (j == this.fonts.length) {
                bArr = null;
            } else {
                if (this.gsubrIndexOffset >= 0) {
                    this.GBias = CalcBias(this.gsubrIndexOffset, j);
                }
                BuildNewCharString(j);
                BuildNewLGSubrs(j);
                bArr = BuildNewFile(j);
                try {
                    this.buf.close();
                } catch (Exception e) {
                }
            }
            return bArr;
        } finally {
            try {
                this.buf.close();
            } catch (Exception e2) {
            }
        }
    }

    protected int CalcBias(int Offset, int Font) {
        seek(Offset);
        int nSubrs = getCard16();
        if (this.fonts[Font].CharstringType == 1) {
            return 0;
        }
        if (nSubrs < 1240) {
            return 107;
        }
        if (nSubrs < 33900) {
            return 1131;
        }
        return 32768;
    }

    protected void BuildNewCharString(int FontIndex) throws IOException {
        this.NewCharStringsIndex = BuildNewIndex(this.fonts[FontIndex].charstringsOffsets, this.GlyphsUsed, (byte) 14);
    }

    protected void BuildNewLGSubrs(int Font) throws IOException {
        if (this.fonts[Font].isCID) {
            this.hSubrsUsed = new HashMap[this.fonts[Font].fdprivateOffsets.length];
            this.lSubrsUsed = new ArrayList[this.fonts[Font].fdprivateOffsets.length];
            this.NewLSubrsIndex = new byte[this.fonts[Font].fdprivateOffsets.length][];
            this.fonts[Font].PrivateSubrsOffset = new int[this.fonts[Font].fdprivateOffsets.length];
            this.fonts[Font].PrivateSubrsOffsetsArray = new int[this.fonts[Font].fdprivateOffsets.length][];
            ArrayList<Integer> FDInList = new ArrayList(this.FDArrayUsed);
            for (int j = 0; j < FDInList.size(); j++) {
                int FD = ((Integer) FDInList.get(j)).intValue();
                this.hSubrsUsed[FD] = new HashMap();
                this.lSubrsUsed[FD] = new ArrayList();
                BuildFDSubrsOffsets(Font, FD);
                if (this.fonts[Font].PrivateSubrsOffset[FD] >= 0) {
                    BuildSubrUsed(Font, FD, this.fonts[Font].PrivateSubrsOffset[FD], this.fonts[Font].PrivateSubrsOffsetsArray[FD], this.hSubrsUsed[FD], this.lSubrsUsed[FD]);
                    this.NewLSubrsIndex[FD] = BuildNewIndex(this.fonts[Font].PrivateSubrsOffsetsArray[FD], this.hSubrsUsed[FD], (byte) 11);
                }
            }
        } else if (this.fonts[Font].privateSubrs >= 0) {
            this.fonts[Font].SubrsOffsets = getIndex(this.fonts[Font].privateSubrs);
            BuildSubrUsed(Font, -1, this.fonts[Font].privateSubrs, this.fonts[Font].SubrsOffsets, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID);
        }
        BuildGSubrsUsed(Font);
        if (this.fonts[Font].privateSubrs >= 0) {
            this.NewSubrsIndexNonCID = BuildNewIndex(this.fonts[Font].SubrsOffsets, this.hSubrsUsedNonCID, (byte) 11);
        }
        this.NewGSubrsIndex = BuildNewIndex(this.gsubrOffsets, this.hGSubrsUsed, (byte) 11);
    }

    protected void BuildFDSubrsOffsets(int Font, int FD) {
        this.fonts[Font].PrivateSubrsOffset[FD] = -1;
        seek(this.fonts[Font].fdprivateOffsets[FD]);
        while (getPosition() < this.fonts[Font].fdprivateOffsets[FD] + this.fonts[Font].fdprivateLengths[FD]) {
            getDictItem();
            if (this.key == "Subrs") {
                this.fonts[Font].PrivateSubrsOffset[FD] = ((Integer) this.args[0]).intValue() + this.fonts[Font].fdprivateOffsets[FD];
            }
        }
        if (this.fonts[Font].PrivateSubrsOffset[FD] >= 0) {
            this.fonts[Font].PrivateSubrsOffsetsArray[FD] = getIndex(this.fonts[Font].PrivateSubrsOffset[FD]);
        }
    }

    protected void BuildSubrUsed(int Font, int FD, int SubrOffset, int[] SubrsOffsets, HashMap<Integer, int[]> hSubr, ArrayList<Integer> lSubr) {
        int i;
        int LBias = CalcBias(SubrOffset, Font);
        for (i = 0; i < this.glyphsInList.size(); i++) {
            int glyph = ((Integer) this.glyphsInList.get(i)).intValue();
            int Start = this.fonts[Font].charstringsOffsets[glyph];
            int End = this.fonts[Font].charstringsOffsets[glyph + 1];
            if (FD >= 0) {
                EmptyStack();
                this.NumOfHints = 0;
                if (this.fonts[Font].FDSelect[glyph] == FD) {
                    ReadASubr(Start, End, this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
                }
            } else {
                ReadASubr(Start, End, this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
            }
        }
        for (i = 0; i < lSubr.size(); i++) {
            int Subr = ((Integer) lSubr.get(i)).intValue();
            if (Subr < SubrsOffsets.length - 1 && Subr >= 0) {
                ReadASubr(SubrsOffsets[Subr], SubrsOffsets[Subr + 1], this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
            }
        }
    }

    protected void BuildGSubrsUsed(int Font) {
        int LBias = 0;
        int SizeOfNonCIDSubrsUsed = 0;
        if (this.fonts[Font].privateSubrs >= 0) {
            LBias = CalcBias(this.fonts[Font].privateSubrs, Font);
            SizeOfNonCIDSubrsUsed = this.lSubrsUsedNonCID.size();
        }
        for (int i = 0; i < this.lGSubrsUsed.size(); i++) {
            int Subr = ((Integer) this.lGSubrsUsed.get(i)).intValue();
            if (Subr < this.gsubrOffsets.length - 1 && Subr >= 0) {
                int Start = this.gsubrOffsets[Subr];
                int End = this.gsubrOffsets[Subr + 1];
                if (this.fonts[Font].isCID) {
                    ReadASubr(Start, End, this.GBias, 0, this.hGSubrsUsed, this.lGSubrsUsed, null);
                } else {
                    ReadASubr(Start, End, this.GBias, LBias, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID, this.fonts[Font].SubrsOffsets);
                    if (SizeOfNonCIDSubrsUsed < this.lSubrsUsedNonCID.size()) {
                        for (int j = SizeOfNonCIDSubrsUsed; j < this.lSubrsUsedNonCID.size(); j++) {
                            int LSubr = ((Integer) this.lSubrsUsedNonCID.get(j)).intValue();
                            if (LSubr < this.fonts[Font].SubrsOffsets.length - 1 && LSubr >= 0) {
                                ReadASubr(this.fonts[Font].SubrsOffsets[LSubr], this.fonts[Font].SubrsOffsets[LSubr + 1], this.GBias, LBias, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID, this.fonts[Font].SubrsOffsets);
                            }
                        }
                        SizeOfNonCIDSubrsUsed = this.lSubrsUsedNonCID.size();
                    }
                }
            }
        }
    }

    protected void ReadASubr(int begin, int end, int GBias, int LBias, HashMap<Integer, int[]> hSubr, ArrayList<Integer> lSubr, int[] LSubrsOffsets) {
        EmptyStack();
        this.NumOfHints = 0;
        seek(begin);
        while (getPosition() < end) {
            ReadCommand();
            int pos = getPosition();
            Object TopElement = null;
            if (this.arg_count > 0) {
                TopElement = this.args[this.arg_count - 1];
            }
            int NumOfArgs = this.arg_count;
            HandelStack();
            int Subr;
            if (this.key == "callsubr") {
                if (NumOfArgs > 0) {
                    Subr = ((Integer) TopElement).intValue() + LBias;
                    if (!hSubr.containsKey(Integer.valueOf(Subr))) {
                        hSubr.put(Integer.valueOf(Subr), null);
                        lSubr.add(Integer.valueOf(Subr));
                    }
                    CalcHints(LSubrsOffsets[Subr], LSubrsOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
                    seek(pos);
                }
            } else if (this.key == "callgsubr") {
                if (NumOfArgs > 0) {
                    Subr = ((Integer) TopElement).intValue() + GBias;
                    if (!this.hGSubrsUsed.containsKey(Integer.valueOf(Subr))) {
                        this.hGSubrsUsed.put(Integer.valueOf(Subr), null);
                        this.lGSubrsUsed.add(Integer.valueOf(Subr));
                    }
                    CalcHints(this.gsubrOffsets[Subr], this.gsubrOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
                    seek(pos);
                }
            } else if (this.key == "hstem" || this.key == "vstem" || this.key == "hstemhm" || this.key == "vstemhm") {
                this.NumOfHints += NumOfArgs / 2;
            } else if (this.key == "hintmask" || this.key == "cntrmask") {
                int SizeOfMask = this.NumOfHints / 8;
                if (this.NumOfHints % 8 != 0 || SizeOfMask == 0) {
                    SizeOfMask++;
                }
                for (int i = 0; i < SizeOfMask; i++) {
                    getCard8();
                }
            }
        }
    }

    protected void HandelStack() {
        int StackHandel = StackOpp();
        if (StackHandel >= 2) {
            EmptyStack();
        } else if (StackHandel == 1) {
            PushStack();
        } else {
            StackHandel *= -1;
            for (int i = 0; i < StackHandel; i++) {
                PopStack();
            }
        }
    }

    protected int StackOpp() {
        if (this.key == "ifelse") {
            return -3;
        }
        if (this.key == "roll" || this.key == "put") {
            return -2;
        }
        if (this.key == "callsubr" || this.key == "callgsubr" || this.key == "add" || this.key == HtmlTags.SUB || this.key == HtmlTags.DIV || this.key == "mul" || this.key == "drop" || this.key == "and" || this.key == "or" || this.key == "eq") {
            return -1;
        }
        if (this.key == "abs" || this.key == "neg" || this.key == "sqrt" || this.key == "exch" || this.key == "index" || this.key == "get" || this.key == "not" || this.key == "return") {
            return 0;
        }
        if (this.key == "random" || this.key == "dup") {
            return 1;
        }
        return 2;
    }

    protected void EmptyStack() {
        for (int i = 0; i < this.arg_count; i++) {
            this.args[i] = null;
        }
        this.arg_count = 0;
    }

    protected void PopStack() {
        if (this.arg_count > 0) {
            this.args[this.arg_count - 1] = null;
            this.arg_count--;
        }
    }

    protected void PushStack() {
        this.arg_count++;
    }

    protected void ReadCommand() {
        this.key = null;
        boolean gotKey = false;
        while (!gotKey) {
            char b0 = getCard8();
            int first;
            if (b0 == '\u001c') {
                first = getCard8();
                this.args[this.arg_count] = Integer.valueOf((first << 8) | getCard8());
                this.arg_count++;
            } else if (b0 >= ' ' && b0 <= 'ö') {
                this.args[this.arg_count] = Integer.valueOf(b0 - 139);
                this.arg_count++;
            } else if (b0 >= '÷' && b0 <= 'ú') {
                this.args[this.arg_count] = Integer.valueOf((((b0 - 247) * 256) + getCard8()) + 108);
                this.arg_count++;
            } else if (b0 >= 'û' && b0 <= 'þ') {
                this.args[this.arg_count] = Integer.valueOf((((-(b0 - 251)) * 256) - getCard8()) - 108);
                this.arg_count++;
            } else if (b0 == 'ÿ') {
                first = getCard8();
                int second = getCard8();
                int third = getCard8();
                this.args[this.arg_count] = Integer.valueOf((((first << 24) | (second << 16)) | (third << 8)) | getCard8());
                this.arg_count++;
            } else if (b0 <= '\u001f' && b0 != '\u001c') {
                gotKey = true;
                if (b0 == '\f') {
                    int b1 = getCard8();
                    if (b1 > SubrsEscapeFuncs.length - 1) {
                        b1 = SubrsEscapeFuncs.length - 1;
                    }
                    this.key = SubrsEscapeFuncs[b1];
                } else {
                    this.key = SubrsFunctions[b0];
                }
            }
        }
    }

    protected int CalcHints(int begin, int end, int LBias, int GBias, int[] LSubrsOffsets) {
        seek(begin);
        while (getPosition() < end) {
            ReadCommand();
            int pos = getPosition();
            Object TopElement = null;
            if (this.arg_count > 0) {
                TopElement = this.args[this.arg_count - 1];
            }
            int NumOfArgs = this.arg_count;
            HandelStack();
            int Subr;
            if (this.key == "callsubr") {
                if (NumOfArgs > 0) {
                    Subr = ((Integer) TopElement).intValue() + LBias;
                    CalcHints(LSubrsOffsets[Subr], LSubrsOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
                    seek(pos);
                }
            } else if (this.key == "callgsubr") {
                if (NumOfArgs > 0) {
                    Subr = ((Integer) TopElement).intValue() + GBias;
                    CalcHints(this.gsubrOffsets[Subr], this.gsubrOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
                    seek(pos);
                }
            } else if (this.key == "hstem" || this.key == "vstem" || this.key == "hstemhm" || this.key == "vstemhm") {
                this.NumOfHints += NumOfArgs / 2;
            } else if (this.key == "hintmask" || this.key == "cntrmask") {
                int SizeOfMask = this.NumOfHints / 8;
                if (this.NumOfHints % 8 != 0 || SizeOfMask == 0) {
                    SizeOfMask++;
                }
                for (int i = 0; i < SizeOfMask; i++) {
                    getCard8();
                }
            }
        }
        return this.NumOfHints;
    }

    protected byte[] BuildNewIndex(int[] Offsets, HashMap<Integer, int[]> Used, byte OperatorForUnusedEntries) throws IOException {
        int i;
        int unusedCount = 0;
        int Offset = 0;
        int[] NewOffsets = new int[Offsets.length];
        for (i = 0; i < Offsets.length; i++) {
            NewOffsets[i] = Offset;
            if (Used.containsKey(Integer.valueOf(i))) {
                Offset += Offsets[i + 1] - Offsets[i];
            } else {
                unusedCount++;
            }
        }
        byte[] NewObjects = new byte[(Offset + unusedCount)];
        int unusedOffset = 0;
        for (i = 0; i < Offsets.length - 1; i++) {
            int start = NewOffsets[i];
            int end = NewOffsets[i + 1];
            NewOffsets[i] = start + unusedOffset;
            if (start != end) {
                this.buf.seek((long) Offsets[i]);
                this.buf.readFully(NewObjects, start + unusedOffset, end - start);
            } else {
                NewObjects[start + unusedOffset] = OperatorForUnusedEntries;
                unusedOffset++;
            }
        }
        int length = Offsets.length - 1;
        NewOffsets[length] = NewOffsets[length] + unusedOffset;
        return AssembleIndex(NewOffsets, NewObjects);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected byte[] AssembleIndex(int[] r16, byte[] r17) {
        /*
        r15 = this;
        r0 = r16;
        r13 = r0.length;
        r13 = r13 + -1;
        r1 = (char) r13;
        r0 = r16;
        r13 = r0.length;
        r13 = r13 + -1;
        r7 = r16[r13];
        r13 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r7 > r13) goto L_0x004b;
    L_0x0011:
        r4 = 1;
    L_0x0012:
        r13 = r1 + 1;
        r13 = r13 * r4;
        r13 = r13 + 3;
        r0 = r17;
        r14 = r0.length;
        r13 = r13 + r14;
        r2 = new byte[r13];
        r5 = 0;
        r6 = r5 + 1;
        r13 = r1 >>> 8;
        r13 = r13 & 255;
        r13 = (byte) r13;
        r2[r5] = r13;
        r5 = r6 + 1;
        r13 = r1 >>> 0;
        r13 = r13 & 255;
        r13 = (byte) r13;
        r2[r6] = r13;
        r6 = r5 + 1;
        r2[r5] = r4;
        r8 = r16;
        r10 = r8.length;
        r9 = 0;
    L_0x0038:
        if (r9 >= r10) goto L_0x0082;
    L_0x003a:
        r12 = r8[r9];
        r13 = 0;
        r13 = r16[r13];
        r13 = r12 - r13;
        r3 = r13 + 1;
        switch(r4) {
            case 1: goto L_0x0077;
            case 2: goto L_0x006d;
            case 3: goto L_0x0093;
            case 4: goto L_0x005b;
            default: goto L_0x0046;
        };
    L_0x0046:
        r5 = r6;
        r9 = r9 + 1;
        r6 = r5;
        goto L_0x0038;
    L_0x004b:
        r13 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r7 > r13) goto L_0x0052;
    L_0x0050:
        r4 = 2;
        goto L_0x0012;
    L_0x0052:
        r13 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
        if (r7 > r13) goto L_0x0059;
    L_0x0057:
        r4 = 3;
        goto L_0x0012;
    L_0x0059:
        r4 = 4;
        goto L_0x0012;
    L_0x005b:
        r5 = r6 + 1;
        r13 = r3 >>> 24;
        r13 = r13 & 255;
        r13 = (byte) r13;
        r2[r6] = r13;
    L_0x0064:
        r6 = r5 + 1;
        r13 = r3 >>> 16;
        r13 = r13 & 255;
        r13 = (byte) r13;
        r2[r5] = r13;
    L_0x006d:
        r5 = r6;
        r6 = r5 + 1;
        r13 = r3 >>> 8;
        r13 = r13 & 255;
        r13 = (byte) r13;
        r2[r5] = r13;
    L_0x0077:
        r5 = r6;
        r6 = r5 + 1;
        r13 = r3 >>> 0;
        r13 = r13 & 255;
        r13 = (byte) r13;
        r2[r5] = r13;
        goto L_0x0046;
    L_0x0082:
        r8 = r17;
        r10 = r8.length;
        r9 = 0;
    L_0x0086:
        if (r9 >= r10) goto L_0x0092;
    L_0x0088:
        r11 = r8[r9];
        r5 = r6 + 1;
        r2[r6] = r11;
        r9 = r9 + 1;
        r6 = r5;
        goto L_0x0086;
    L_0x0092:
        return r2;
    L_0x0093:
        r5 = r6;
        goto L_0x0064;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.CFFFontSubset.AssembleIndex(int[], byte[]):byte[]");
    }

    protected byte[] BuildNewFile(int Font) {
        this.OutputList = new LinkedList();
        CopyHeader();
        BuildIndexHeader(1, 1, 1);
        this.OutputList.addLast(new UInt8Item((char) (this.fonts[Font].name.length() + 1)));
        this.OutputList.addLast(new StringItem(this.fonts[Font].name));
        BuildIndexHeader(1, 2, 1);
        OffsetItem topdictIndex1Ref = new IndexOffsetItem(2);
        this.OutputList.addLast(topdictIndex1Ref);
        IndexBaseItem topdictBase = new IndexBaseItem();
        this.OutputList.addLast(topdictBase);
        OffsetItem charsetRef = new DictOffsetItem();
        OffsetItem charstringsRef = new DictOffsetItem();
        OffsetItem fdarrayRef = new DictOffsetItem();
        OffsetItem fdselectRef = new DictOffsetItem();
        OffsetItem privateRef = new DictOffsetItem();
        if (!this.fonts[Font].isCID) {
            this.OutputList.addLast(new DictNumberItem(this.fonts[Font].nstrings));
            this.OutputList.addLast(new DictNumberItem(this.fonts[Font].nstrings + 1));
            this.OutputList.addLast(new DictNumberItem(0));
            this.OutputList.addLast(new UInt8Item('\f'));
            this.OutputList.addLast(new UInt8Item('\u001e'));
            this.OutputList.addLast(new DictNumberItem(this.fonts[Font].nglyphs));
            this.OutputList.addLast(new UInt8Item('\f'));
            this.OutputList.addLast(new UInt8Item('\"'));
        }
        seek(this.topdictOffsets[Font]);
        while (getPosition() < this.topdictOffsets[Font + 1]) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if (!(this.key == "Encoding" || this.key == "Private" || this.key == "FDSelect" || this.key == "FDArray" || this.key == "charset" || this.key == "CharStrings")) {
                this.OutputList.add(new RangeItem(this.buf, p1, p2 - p1));
            }
        }
        CreateKeys(fdarrayRef, fdselectRef, charsetRef, charstringsRef);
        this.OutputList.addLast(new IndexMarkerItem(topdictIndex1Ref, topdictBase));
        if (this.fonts[Font].isCID) {
            this.OutputList.addLast(getEntireIndexRange(this.stringIndexOffset));
        } else {
            CreateNewStringIndex(Font);
        }
        this.OutputList.addLast(new RangeItem(new RandomAccessFileOrArray(this.NewGSubrsIndex), 0, this.NewGSubrsIndex.length));
        if (this.fonts[Font].isCID) {
            this.OutputList.addLast(new MarkerItem(fdselectRef));
            if (this.fonts[Font].fdselectOffset >= 0) {
                this.OutputList.addLast(new RangeItem(this.buf, this.fonts[Font].fdselectOffset, this.fonts[Font].FDSelectLength));
            } else {
                CreateFDSelect(fdselectRef, this.fonts[Font].nglyphs);
            }
            this.OutputList.addLast(new MarkerItem(charsetRef));
            this.OutputList.addLast(new RangeItem(this.buf, this.fonts[Font].charsetOffset, this.fonts[Font].CharsetLength));
            if (this.fonts[Font].fdarrayOffset >= 0) {
                this.OutputList.addLast(new MarkerItem(fdarrayRef));
                Reconstruct(Font);
            } else {
                CreateFDArray(fdarrayRef, privateRef, Font);
            }
        } else {
            CreateFDSelect(fdselectRef, this.fonts[Font].nglyphs);
            CreateCharset(charsetRef, this.fonts[Font].nglyphs);
            CreateFDArray(fdarrayRef, privateRef, Font);
        }
        if (this.fonts[Font].privateOffset >= 0) {
            IndexBaseItem PrivateBase = new IndexBaseItem();
            this.OutputList.addLast(PrivateBase);
            this.OutputList.addLast(new MarkerItem(privateRef));
            OffsetItem Subr = new DictOffsetItem();
            CreateNonCIDPrivate(Font, Subr);
            CreateNonCIDSubrs(Font, PrivateBase, Subr);
        }
        this.OutputList.addLast(new MarkerItem(charstringsRef));
        this.OutputList.addLast(new RangeItem(new RandomAccessFileOrArray(this.NewCharStringsIndex), 0, this.NewCharStringsIndex.length));
        int[] currentOffset = new int[]{0};
        Iterator<Item> listIter = this.OutputList.iterator();
        while (listIter.hasNext()) {
            ((Item) listIter.next()).increment(currentOffset);
        }
        listIter = this.OutputList.iterator();
        while (listIter.hasNext()) {
            ((Item) listIter.next()).xref();
        }
        byte[] b = new byte[currentOffset[0]];
        listIter = this.OutputList.iterator();
        while (listIter.hasNext()) {
            ((Item) listIter.next()).emit(b);
        }
        return b;
    }

    protected void CopyHeader() {
        seek(0);
        int major = getCard8();
        int minor = getCard8();
        int hdrSize = getCard8();
        int offSize = getCard8();
        this.nextIndexOffset = hdrSize;
        this.OutputList.addLast(new RangeItem(this.buf, 0, hdrSize));
    }

    protected void BuildIndexHeader(int Count, int Offsize, int First) {
        this.OutputList.addLast(new UInt16Item((char) Count));
        this.OutputList.addLast(new UInt8Item((char) Offsize));
        switch (Offsize) {
            case 1:
                this.OutputList.addLast(new UInt8Item((char) First));
                return;
            case 2:
                this.OutputList.addLast(new UInt16Item((char) First));
                return;
            case 3:
                this.OutputList.addLast(new UInt24Item((char) First));
                return;
            case 4:
                this.OutputList.addLast(new UInt32Item((char) First));
                return;
            default:
                return;
        }
    }

    protected void CreateKeys(OffsetItem fdarrayRef, OffsetItem fdselectRef, OffsetItem charsetRef, OffsetItem charstringsRef) {
        this.OutputList.addLast(fdarrayRef);
        this.OutputList.addLast(new UInt8Item('\f'));
        this.OutputList.addLast(new UInt8Item('$'));
        this.OutputList.addLast(fdselectRef);
        this.OutputList.addLast(new UInt8Item('\f'));
        this.OutputList.addLast(new UInt8Item('%'));
        this.OutputList.addLast(charsetRef);
        this.OutputList.addLast(new UInt8Item('\u000f'));
        this.OutputList.addLast(charstringsRef);
        this.OutputList.addLast(new UInt8Item('\u0011'));
    }

    protected void CreateNewStringIndex(int Font) {
        byte stringsIndexOffSize;
        String fdFontName = this.fonts[Font].name + "-OneRange";
        if (fdFontName.length() > 127) {
            fdFontName = fdFontName.substring(0, 127);
        }
        String extraStrings = "AdobeIdentity" + fdFontName;
        int origStringsLen = this.stringOffsets[this.stringOffsets.length - 1] - this.stringOffsets[0];
        int stringsBaseOffset = this.stringOffsets[0] - 1;
        if (extraStrings.length() + origStringsLen <= 255) {
            stringsIndexOffSize = (byte) 1;
        } else if (extraStrings.length() + origStringsLen <= 65535) {
            stringsIndexOffSize = (byte) 2;
        } else if (extraStrings.length() + origStringsLen <= ViewCompat.MEASURED_SIZE_MASK) {
            stringsIndexOffSize = (byte) 3;
        } else {
            stringsIndexOffSize = (byte) 4;
        }
        this.OutputList.addLast(new UInt16Item((char) ((this.stringOffsets.length - 1) + 3)));
        this.OutputList.addLast(new UInt8Item((char) stringsIndexOffSize));
        for (int stringOffset : this.stringOffsets) {
            this.OutputList.addLast(new IndexOffsetItem(stringsIndexOffSize, stringOffset - stringsBaseOffset));
        }
        int currentStringsOffset = (this.stringOffsets[this.stringOffsets.length - 1] - stringsBaseOffset) + "Adobe".length();
        this.OutputList.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
        currentStringsOffset += "Identity".length();
        this.OutputList.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
        this.OutputList.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset + fdFontName.length()));
        this.OutputList.addLast(new RangeItem(this.buf, this.stringOffsets[0], origStringsLen));
        this.OutputList.addLast(new StringItem(extraStrings));
    }

    protected void CreateFDSelect(OffsetItem fdselectRef, int nglyphs) {
        this.OutputList.addLast(new MarkerItem(fdselectRef));
        this.OutputList.addLast(new UInt8Item('\u0003'));
        this.OutputList.addLast(new UInt16Item('\u0001'));
        this.OutputList.addLast(new UInt16Item('\u0000'));
        this.OutputList.addLast(new UInt8Item('\u0000'));
        this.OutputList.addLast(new UInt16Item((char) nglyphs));
    }

    protected void CreateCharset(OffsetItem charsetRef, int nglyphs) {
        this.OutputList.addLast(new MarkerItem(charsetRef));
        this.OutputList.addLast(new UInt8Item('\u0002'));
        this.OutputList.addLast(new UInt16Item('\u0001'));
        this.OutputList.addLast(new UInt16Item((char) (nglyphs - 1)));
    }

    protected void CreateFDArray(OffsetItem fdarrayRef, OffsetItem privateRef, int Font) {
        this.OutputList.addLast(new MarkerItem(fdarrayRef));
        BuildIndexHeader(1, 1, 1);
        OffsetItem privateIndex1Ref = new IndexOffsetItem(1);
        this.OutputList.addLast(privateIndex1Ref);
        IndexBaseItem privateBase = new IndexBaseItem();
        this.OutputList.addLast(privateBase);
        int NewSize = this.fonts[Font].privateLength;
        int OrgSubrsOffsetSize = CalcSubrOffsetSize(this.fonts[Font].privateOffset, this.fonts[Font].privateLength);
        if (OrgSubrsOffsetSize != 0) {
            NewSize += 5 - OrgSubrsOffsetSize;
        }
        this.OutputList.addLast(new DictNumberItem(NewSize));
        this.OutputList.addLast(privateRef);
        this.OutputList.addLast(new UInt8Item('\u0012'));
        this.OutputList.addLast(new IndexMarkerItem(privateIndex1Ref, privateBase));
    }

    void Reconstruct(int Font) {
        OffsetItem[] fdPrivate = new DictOffsetItem[(this.fonts[Font].FDArrayOffsets.length - 1)];
        IndexBaseItem[] fdPrivateBase = new IndexBaseItem[this.fonts[Font].fdprivateOffsets.length];
        OffsetItem[] fdSubrs = new DictOffsetItem[this.fonts[Font].fdprivateOffsets.length];
        ReconstructFDArray(Font, fdPrivate);
        ReconstructPrivateDict(Font, fdPrivate, fdPrivateBase, fdSubrs);
        ReconstructPrivateSubrs(Font, fdPrivateBase, fdSubrs);
    }

    void ReconstructFDArray(int Font, OffsetItem[] fdPrivate) {
        BuildIndexHeader(this.fonts[Font].FDArrayCount, this.fonts[Font].FDArrayOffsize, 1);
        OffsetItem[] fdOffsets = new IndexOffsetItem[(this.fonts[Font].FDArrayOffsets.length - 1)];
        for (int i = 0; i < this.fonts[Font].FDArrayOffsets.length - 1; i++) {
            fdOffsets[i] = new IndexOffsetItem(this.fonts[Font].FDArrayOffsize);
            this.OutputList.addLast(fdOffsets[i]);
        }
        IndexBaseItem fdArrayBase = new IndexBaseItem();
        this.OutputList.addLast(fdArrayBase);
        for (int k = 0; k < this.fonts[Font].FDArrayOffsets.length - 1; k++) {
            seek(this.fonts[Font].FDArrayOffsets[k]);
            while (getPosition() < this.fonts[Font].FDArrayOffsets[k + 1]) {
                int p1 = getPosition();
                getDictItem();
                int p2 = getPosition();
                if (this.key == "Private") {
                    int NewSize = ((Integer) this.args[0]).intValue();
                    int OrgSubrsOffsetSize = CalcSubrOffsetSize(this.fonts[Font].fdprivateOffsets[k], this.fonts[Font].fdprivateLengths[k]);
                    if (OrgSubrsOffsetSize != 0) {
                        NewSize += 5 - OrgSubrsOffsetSize;
                    }
                    this.OutputList.addLast(new DictNumberItem(NewSize));
                    fdPrivate[k] = new DictOffsetItem();
                    this.OutputList.addLast(fdPrivate[k]);
                    this.OutputList.addLast(new UInt8Item('\u0012'));
                    seek(p2);
                } else {
                    this.OutputList.addLast(new RangeItem(this.buf, p1, p2 - p1));
                }
            }
            this.OutputList.addLast(new IndexMarkerItem(fdOffsets[k], fdArrayBase));
        }
    }

    void ReconstructPrivateDict(int Font, OffsetItem[] fdPrivate, IndexBaseItem[] fdPrivateBase, OffsetItem[] fdSubrs) {
        for (int i = 0; i < this.fonts[Font].fdprivateOffsets.length; i++) {
            this.OutputList.addLast(new MarkerItem(fdPrivate[i]));
            fdPrivateBase[i] = new IndexBaseItem();
            this.OutputList.addLast(fdPrivateBase[i]);
            seek(this.fonts[Font].fdprivateOffsets[i]);
            while (getPosition() < this.fonts[Font].fdprivateOffsets[i] + this.fonts[Font].fdprivateLengths[i]) {
                int p1 = getPosition();
                getDictItem();
                int p2 = getPosition();
                if (this.key == "Subrs") {
                    fdSubrs[i] = new DictOffsetItem();
                    this.OutputList.addLast(fdSubrs[i]);
                    this.OutputList.addLast(new UInt8Item('\u0013'));
                } else {
                    this.OutputList.addLast(new RangeItem(this.buf, p1, p2 - p1));
                }
            }
        }
    }

    void ReconstructPrivateSubrs(int Font, IndexBaseItem[] fdPrivateBase, OffsetItem[] fdSubrs) {
        int i = 0;
        while (i < this.fonts[Font].fdprivateLengths.length) {
            if (fdSubrs[i] != null && this.fonts[Font].PrivateSubrsOffset[i] >= 0) {
                this.OutputList.addLast(new SubrMarkerItem(fdSubrs[i], fdPrivateBase[i]));
                if (this.NewLSubrsIndex[i] != null) {
                    this.OutputList.addLast(new RangeItem(new RandomAccessFileOrArray(this.NewLSubrsIndex[i]), 0, this.NewLSubrsIndex[i].length));
                }
            }
            i++;
        }
    }

    int CalcSubrOffsetSize(int Offset, int Size) {
        int OffsetSize = 0;
        seek(Offset);
        while (getPosition() < Offset + Size) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if (this.key == "Subrs") {
                OffsetSize = (p2 - p1) - 1;
            }
        }
        return OffsetSize;
    }

    protected int countEntireIndexRange(int indexOffset) {
        seek(indexOffset);
        int count = getCard16();
        if (count == 0) {
            return 2;
        }
        int indexOffSize = getCard8();
        seek(((indexOffset + 2) + 1) + (count * indexOffSize));
        return (((count + 1) * indexOffSize) + 3) + (getOffset(indexOffSize) - 1);
    }

    void CreateNonCIDPrivate(int Font, OffsetItem Subr) {
        seek(this.fonts[Font].privateOffset);
        while (getPosition() < this.fonts[Font].privateOffset + this.fonts[Font].privateLength) {
            int p1 = getPosition();
            getDictItem();
            int p2 = getPosition();
            if (this.key == "Subrs") {
                this.OutputList.addLast(Subr);
                this.OutputList.addLast(new UInt8Item('\u0013'));
            } else {
                this.OutputList.addLast(new RangeItem(this.buf, p1, p2 - p1));
            }
        }
    }

    void CreateNonCIDSubrs(int Font, IndexBaseItem PrivateBase, OffsetItem Subrs) {
        this.OutputList.addLast(new SubrMarkerItem(Subrs, PrivateBase));
        if (this.NewSubrsIndexNonCID != null) {
            this.OutputList.addLast(new RangeItem(new RandomAccessFileOrArray(this.NewSubrsIndexNonCID), 0, this.NewSubrsIndexNonCID.length));
        }
    }
}
