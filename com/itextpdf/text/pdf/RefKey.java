package com.itextpdf.text.pdf;

public class RefKey {
    int gen;
    int num;

    RefKey(int num, int gen) {
        this.num = num;
        this.gen = gen;
    }

    public RefKey(PdfIndirectReference ref) {
        this.num = ref.getNumber();
        this.gen = ref.getGeneration();
    }

    RefKey(PRIndirectReference ref) {
        this.num = ref.getNumber();
        this.gen = ref.getGeneration();
    }

    public int hashCode() {
        return (this.gen << 16) + this.num;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RefKey)) {
            return false;
        }
        RefKey other = (RefKey) o;
        if (this.gen == other.gen && this.num == other.num) {
            return true;
        }
        return false;
    }

    public String toString() {
        return Integer.toString(this.num) + ' ' + this.gen;
    }
}
