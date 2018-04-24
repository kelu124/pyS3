package com.itextpdf.text.pdf.qrcode;

import java.util.ArrayList;

public final class ReedSolomonEncoder {
    private final ArrayList<GF256Poly> cachedGenerators;
    private final GF256 field;

    public ReedSolomonEncoder(GF256 field) {
        if (GF256.QR_CODE_FIELD.equals(field)) {
            this.field = field;
            this.cachedGenerators = new ArrayList();
            this.cachedGenerators.add(new GF256Poly(field, new int[]{1}));
            return;
        }
        throw new IllegalArgumentException("Only QR Code is supported at this time");
    }

    private GF256Poly buildGenerator(int degree) {
        if (degree >= this.cachedGenerators.size()) {
            GF256Poly lastGenerator = (GF256Poly) this.cachedGenerators.get(this.cachedGenerators.size() - 1);
            for (int d = this.cachedGenerators.size(); d <= degree; d++) {
                GF256Poly nextGenerator = lastGenerator.multiply(new GF256Poly(this.field, new int[]{1, this.field.exp(d - 1)}));
                this.cachedGenerators.add(nextGenerator);
                lastGenerator = nextGenerator;
            }
        }
        return (GF256Poly) this.cachedGenerators.get(degree);
    }

    public void encode(int[] toEncode, int ecBytes) {
        if (ecBytes == 0) {
            throw new IllegalArgumentException("No error correction bytes");
        }
        int dataBytes = toEncode.length - ecBytes;
        if (dataBytes <= 0) {
            throw new IllegalArgumentException("No data bytes provided");
        }
        GF256Poly generator = buildGenerator(ecBytes);
        int[] infoCoefficients = new int[dataBytes];
        System.arraycopy(toEncode, 0, infoCoefficients, 0, dataBytes);
        int[] coefficients = new GF256Poly(this.field, infoCoefficients).multiplyByMonomial(ecBytes, 1).divide(generator)[1].getCoefficients();
        int numZeroCoefficients = ecBytes - coefficients.length;
        for (int i = 0; i < numZeroCoefficients; i++) {
            toEncode[dataBytes + i] = 0;
        }
        System.arraycopy(coefficients, 0, toEncode, dataBytes + numZeroCoefficients, coefficients.length);
    }
}
