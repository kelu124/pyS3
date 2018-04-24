package com.itextpdf.text.pdf.codec;

import java.io.IOException;
import java.io.OutputStream;

public class BitFile {
    int bitsLeft_;
    boolean blocks_ = false;
    byte[] buffer_;
    int index_;
    OutputStream output_;

    public BitFile(OutputStream output, boolean blocks) {
        this.output_ = output;
        this.blocks_ = blocks;
        this.buffer_ = new byte[256];
        this.index_ = 0;
        this.bitsLeft_ = 8;
    }

    public void flush() throws IOException {
        int numBytes = this.index_ + (this.bitsLeft_ == 8 ? 0 : 1);
        if (numBytes > 0) {
            if (this.blocks_) {
                this.output_.write(numBytes);
            }
            this.output_.write(this.buffer_, 0, numBytes);
            this.buffer_[0] = (byte) 0;
            this.index_ = 0;
            this.bitsLeft_ = 8;
        }
    }

    public void writeBits(int bits, int numbits) throws IOException {
        int bitsWritten = 0;
        do {
            if ((this.index_ == 254 && this.bitsLeft_ == 0) || this.index_ > 254) {
                if (this.blocks_) {
                    this.output_.write(255);
                }
                this.output_.write(this.buffer_, 0, 255);
                this.buffer_[0] = (byte) 0;
                this.index_ = 0;
                this.bitsLeft_ = 8;
            }
            byte[] bArr;
            int i;
            if (numbits <= this.bitsLeft_) {
                if (this.blocks_) {
                    bArr = this.buffer_;
                    i = this.index_;
                    bArr[i] = (byte) (bArr[i] | ((((1 << numbits) - 1) & bits) << (8 - this.bitsLeft_)));
                    bitsWritten += numbits;
                    this.bitsLeft_ -= numbits;
                    numbits = 0;
                    continue;
                } else {
                    bArr = this.buffer_;
                    i = this.index_;
                    bArr[i] = (byte) (bArr[i] | ((((1 << numbits) - 1) & bits) << (this.bitsLeft_ - numbits)));
                    bitsWritten += numbits;
                    this.bitsLeft_ -= numbits;
                    numbits = 0;
                    continue;
                }
            } else if (this.blocks_) {
                bArr = this.buffer_;
                i = this.index_;
                bArr[i] = (byte) (bArr[i] | ((((1 << this.bitsLeft_) - 1) & bits) << (8 - this.bitsLeft_)));
                bitsWritten += this.bitsLeft_;
                bits >>= this.bitsLeft_;
                numbits -= this.bitsLeft_;
                bArr = this.buffer_;
                i = this.index_ + 1;
                this.index_ = i;
                bArr[i] = (byte) 0;
                this.bitsLeft_ = 8;
                continue;
            } else {
                int topbits = (bits >>> (numbits - this.bitsLeft_)) & ((1 << this.bitsLeft_) - 1);
                bArr = this.buffer_;
                i = this.index_;
                bArr[i] = (byte) (bArr[i] | topbits);
                numbits -= this.bitsLeft_;
                bitsWritten += this.bitsLeft_;
                bArr = this.buffer_;
                i = this.index_ + 1;
                this.index_ = i;
                bArr[i] = (byte) 0;
                this.bitsLeft_ = 8;
                continue;
            }
        } while (numbits != 0);
    }
}
