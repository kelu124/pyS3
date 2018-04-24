package com.itextpdf.text.pdf.codec;

import java.io.IOException;
import java.io.OutputStream;

public class LZWCompressor {
    BitFile bf_;
    int clearCode_;
    int codeSize_;
    int endOfInfo_;
    int limit_;
    LZWStringTable lzss_;
    int numBits_;
    short prefix_;
    boolean tiffFudge_;

    public LZWCompressor(OutputStream out, int codeSize, boolean TIFF) throws IOException {
        this.bf_ = new BitFile(out, !TIFF);
        this.codeSize_ = codeSize;
        this.tiffFudge_ = TIFF;
        this.clearCode_ = 1 << this.codeSize_;
        this.endOfInfo_ = this.clearCode_ + 1;
        this.numBits_ = this.codeSize_ + 1;
        this.limit_ = (1 << this.numBits_) - 1;
        if (this.tiffFudge_) {
            this.limit_--;
        }
        this.prefix_ = (short) -1;
        this.lzss_ = new LZWStringTable();
        this.lzss_.ClearTable(this.codeSize_);
        this.bf_.writeBits(this.clearCode_, this.numBits_);
    }

    public void compress(byte[] buf, int offset, int length) throws IOException {
        int maxOffset = offset + length;
        for (int idx = offset; idx < maxOffset; idx++) {
            byte c = buf[idx];
            short index = this.lzss_.FindCharString(this.prefix_, c);
            if (index != (short) -1) {
                this.prefix_ = index;
            } else {
                this.bf_.writeBits(this.prefix_, this.numBits_);
                if (this.lzss_.AddCharString(this.prefix_, c) > this.limit_) {
                    if (this.numBits_ == 12) {
                        this.bf_.writeBits(this.clearCode_, this.numBits_);
                        this.lzss_.ClearTable(this.codeSize_);
                        this.numBits_ = this.codeSize_ + 1;
                    } else {
                        this.numBits_++;
                    }
                    this.limit_ = (1 << this.numBits_) - 1;
                    if (this.tiffFudge_) {
                        this.limit_--;
                    }
                }
                this.prefix_ = (short) (((short) c) & 255);
            }
        }
    }

    public void flush() throws IOException {
        if (this.prefix_ != (short) -1) {
            this.bf_.writeBits(this.prefix_, this.numBits_);
        }
        this.bf_.writeBits(this.endOfInfo_, this.numBits_);
        this.bf_.flush();
    }
}
