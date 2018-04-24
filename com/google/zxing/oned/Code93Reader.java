package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import java.util.Arrays;
import java.util.Map;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

public final class Code93Reader extends OneDReader {
    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    private static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*";
    private static final int ASTERISK_ENCODING = CHARACTER_ENCODINGS[47];
    private static final int[] CHARACTER_ENCODINGS = new int[]{276, 328, 324, 322, 296, TIFFConstants.TIFFTAG_GROUP3OPTIONS, TIFFConstants.TIFFTAG_GRAYRESPONSEUNIT, 336, TIFFConstants.TIFFTAG_ORIENTATION, TIFFConstants.TIFFTAG_FILLORDER, 424, 420, 418, 404, 402, 394, dc1394.DC1394_COLOR_CODING_RGB16S, dc1394.DC1394_COLOR_CODING_RGB8, dc1394.DC1394_COLOR_CODING_YUV422, avutil.AV_PIX_FMT_YUV444P12LE, TIFFConstants.TIFFTAG_XRESOLUTION, 344, 332, 326, 300, TIFFConstants.TIFFTAG_ROWSPERSTRIP, 436, 434, 428, 422, 406, 410, 364, dc1394.DC1394_COLOR_CODING_RGB16, avutil.AV_PIX_FMT_YUV444P14LE, avutil.AV_PIX_FMT_GBRP14LE, 302, opencv_videoio.CV_CAP_PROP_XI_CHIP_TEMP, opencv_videoio.CV_CAP_PROP_XI_COOLING, 458, 366, 374, 430, 294, opencv_videoio.CV_CAP_PROP_XI_IMAGE_IS_COLOR, opencv_videoio.CV_CAP_PROP_XI_CMS, 306, 350};
    private final int[] counters = new int[6];
    private final StringBuilder decodeRowResult = new StringBuilder(20);

    public Result decodeRow(int rowNumber, BitArray row, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        int[] start = findAsteriskPattern(row);
        int nextStart = row.getNextSet(start[1]);
        int end = row.getSize();
        int[] theCounters = this.counters;
        Arrays.fill(theCounters, 0);
        StringBuilder result = this.decodeRowResult;
        result.setLength(0);
        char decodedChar;
        do {
            OneDReader.recordPattern(row, nextStart, theCounters);
            int pattern = toPattern(theCounters);
            if (pattern < 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            decodedChar = patternToChar(pattern);
            result.append(decodedChar);
            int lastStart = nextStart;
            for (int counter : theCounters) {
                nextStart += counter;
            }
            nextStart = row.getNextSet(nextStart);
        } while (decodedChar != '*');
        result.deleteCharAt(result.length() - 1);
        int lastPatternSize = 0;
        for (int counter2 : theCounters) {
            lastPatternSize += counter2;
        }
        if (nextStart == end || !row.get(nextStart)) {
            throw NotFoundException.getNotFoundInstance();
        } else if (result.length() < 2) {
            throw NotFoundException.getNotFoundInstance();
        } else {
            checkChecksums(result);
            result.setLength(result.length() - 2);
            float left = ((float) (start[1] + start[0])) / BaseField.BORDER_WIDTH_MEDIUM;
            float right = ((float) lastStart) + (((float) lastPatternSize) / BaseField.BORDER_WIDTH_MEDIUM);
            return new Result(decodeExtended(result), null, new ResultPoint[]{new ResultPoint(left, (float) rowNumber), new ResultPoint(right, (float) rowNumber)}, BarcodeFormat.CODE_93);
        }
    }

    private int[] findAsteriskPattern(BitArray row) throws NotFoundException {
        int width = row.getSize();
        int rowOffset = row.getNextSet(0);
        Arrays.fill(this.counters, 0);
        int[] theCounters = this.counters;
        int patternStart = rowOffset;
        boolean isWhite = false;
        int patternLength = theCounters.length;
        int counterPosition = 0;
        for (int i = rowOffset; i < width; i++) {
            if ((row.get(i) ^ isWhite) != 0) {
                theCounters[counterPosition] = theCounters[counterPosition] + 1;
            } else {
                if (counterPosition != patternLength - 1) {
                    counterPosition++;
                } else if (toPattern(theCounters) == ASTERISK_ENCODING) {
                    return new int[]{patternStart, i};
                } else {
                    patternStart += theCounters[0] + theCounters[1];
                    System.arraycopy(theCounters, 2, theCounters, 0, patternLength - 2);
                    theCounters[patternLength - 2] = 0;
                    theCounters[patternLength - 1] = 0;
                    counterPosition--;
                }
                theCounters[counterPosition] = 1;
                if (isWhite) {
                    isWhite = false;
                } else {
                    isWhite = true;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int toPattern(int[] counters) {
        int max = counters.length;
        int sum = 0;
        for (int counter : counters) {
            sum += counter;
        }
        int pattern = 0;
        for (int i = 0; i < max; i++) {
            int scaled = Math.round((((float) counters[i]) * 9.0f) / ((float) sum));
            if (scaled < 1 || scaled > 4) {
                return -1;
            }
            if ((i & 1) == 0) {
                for (int j = 0; j < scaled; j++) {
                    pattern = (pattern << 1) | 1;
                }
            } else {
                pattern <<= scaled;
            }
        }
        return pattern;
    }

    private static char patternToChar(int pattern) throws NotFoundException {
        for (int i = 0; i < CHARACTER_ENCODINGS.length; i++) {
            if (CHARACTER_ENCODINGS[i] == pattern) {
                return ALPHABET[i];
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static String decodeExtended(CharSequence encoded) throws FormatException {
        int length = encoded.length();
        StringBuilder decoded = new StringBuilder(length);
        int i = 0;
        while (i < length) {
            char c = encoded.charAt(i);
            if (c < 'a' || c > Barcode128.CODE_AC_TO_B) {
                decoded.append(c);
            } else if (i >= length - 1) {
                throw FormatException.getFormatInstance();
            } else {
                char next = encoded.charAt(i + 1);
                char decodedChar = '\u0000';
                switch (c) {
                    case 'a':
                        if (next >= 'A' && next <= 'Z') {
                            decodedChar = (char) (next - 64);
                            break;
                        }
                        throw FormatException.getFormatInstance();
                        break;
                    case 'b':
                        if (next < 'A' || next > 'E') {
                            if (next >= 'F' && next <= 'W') {
                                decodedChar = (char) (next - 11);
                                break;
                            }
                            throw FormatException.getFormatInstance();
                        }
                        decodedChar = (char) (next - 38);
                        break;
                        break;
                    case 'c':
                        if (next >= 'A' && next <= 'O') {
                            decodedChar = (char) (next - 32);
                            break;
                        } else if (next == 'Z') {
                            decodedChar = ':';
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    case 'd':
                        if (next >= 'A' && next <= 'Z') {
                            decodedChar = (char) (next + 32);
                            break;
                        }
                        throw FormatException.getFormatInstance();
                }
                decoded.append(decodedChar);
                i++;
            }
            i++;
        }
        return decoded.toString();
    }

    private static void checkChecksums(CharSequence result) throws ChecksumException {
        int length = result.length();
        checkOneChecksum(result, length - 2, 20);
        checkOneChecksum(result, length - 1, 15);
    }

    private static void checkOneChecksum(CharSequence result, int checkPosition, int weightMax) throws ChecksumException {
        int weight = 1;
        int total = 0;
        for (int i = checkPosition - 1; i >= 0; i--) {
            total += ALPHABET_STRING.indexOf(result.charAt(i)) * weight;
            weight++;
            if (weight > weightMax) {
                weight = 1;
            }
        }
        if (result.charAt(checkPosition) != ALPHABET[total % 47]) {
            throw ChecksumException.getChecksumInstance();
        }
    }
}
