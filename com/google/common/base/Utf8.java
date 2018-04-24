package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public final class Utf8 {
    public static int encodedLength(CharSequence sequence) {
        int utf16Length = sequence.length();
        int utf8Length = utf16Length;
        int i = 0;
        while (i < utf16Length && sequence.charAt(i) < '') {
            i++;
        }
        while (i < utf16Length) {
            char c = sequence.charAt(i);
            if (c >= 'ࠀ') {
                utf8Length += encodedLengthGeneral(sequence, i);
                break;
            }
            utf8Length += (127 - c) >>> 31;
            i++;
        }
        if (utf8Length >= utf16Length) {
            return utf8Length;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) utf8Length) + 4294967296L));
    }

    private static int encodedLengthGeneral(CharSequence sequence, int start) {
        int utf16Length = sequence.length();
        int utf8Length = 0;
        int i = start;
        while (i < utf16Length) {
            char c = sequence.charAt(i);
            if (c < 'ࠀ') {
                utf8Length += (127 - c) >>> 31;
            } else {
                utf8Length += 2;
                if ('?' <= c && c <= '?') {
                    if (Character.codePointAt(sequence, i) == c) {
                        throw new IllegalArgumentException(unpairedSurrogateMsg(i));
                    }
                    i++;
                }
            }
            i++;
        }
        return utf8Length;
    }

    public static boolean isWellFormed(byte[] bytes) {
        return isWellFormed(bytes, 0, bytes.length);
    }

    public static boolean isWellFormed(byte[] bytes, int off, int len) {
        int end = off + len;
        Preconditions.checkPositionIndexes(off, end, bytes.length);
        for (int i = off; i < end; i++) {
            if (bytes[i] < (byte) 0) {
                return isWellFormedSlowPath(bytes, i, end);
            }
        }
        return true;
    }

    private static boolean isWellFormedSlowPath(byte[] bytes, int off, int end) {
        int index;
        int index2 = off;
        while (index2 < end) {
            index = index2 + 1;
            int byte1 = bytes[index2];
            if (byte1 < 0) {
                if (byte1 < -32) {
                    if (index == end || byte1 < -62) {
                        return false;
                    }
                    index2 = index + 1;
                    if (bytes[index] > (byte) -65) {
                    }
                    index = index2;
                    index2 = index;
                } else if (byte1 < -16) {
                    if (index + 1 >= end) {
                        return false;
                    }
                    index2 = index + 1;
                    byte2 = bytes[index];
                    if (byte2 <= -65 && ((byte1 != -32 || byte2 >= -96) && (byte1 != -19 || -96 > byte2))) {
                        index = index2 + 1;
                        if (bytes[index2] > (byte) -65) {
                            return false;
                        }
                        index2 = index;
                    }
                } else if (index + 2 >= end) {
                    return false;
                } else {
                    index2 = index + 1;
                    byte2 = bytes[index];
                    if (byte2 <= -65 && (((byte1 << 28) + (byte2 + 112)) >> 30) == 0) {
                        index = index2 + 1;
                        if (bytes[index2] > (byte) -65) {
                            return false;
                        }
                        index2 = index + 1;
                        if (bytes[index] > (byte) -65) {
                        }
                        index = index2;
                        index2 = index;
                    }
                }
                index = index2;
                return false;
            }
            index2 = index;
        }
        index = index2;
        return true;
    }

    private static String unpairedSurrogateMsg(int i) {
        return "Unpaired surrogate at index " + i;
    }

    private Utf8() {
    }
}
