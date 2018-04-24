package com.lee.ultrascan.model;

public class RFFrame {
    private byte[] envelopData;
    private int lines;
    private int samplesPerLine;

    public RFFrame(int lines, int samplesPerLine, byte[] envelopData) {
        this.lines = lines;
        this.samplesPerLine = samplesPerLine;
        this.envelopData = envelopData;
    }

    public int getLines() {
        return this.lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getSamplesPerLine() {
        return this.samplesPerLine;
    }

    public void setSamplesPerLine(int samplesPerLine) {
        this.samplesPerLine = samplesPerLine;
    }

    public byte[] getEnvelopData() {
        return this.envelopData;
    }

    public void setEnvelopData(byte[] envelopData) {
        this.envelopData = envelopData;
    }
}
