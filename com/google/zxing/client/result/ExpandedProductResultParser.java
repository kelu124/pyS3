package com.google.zxing.client.result;

public final class ExpandedProductResultParser extends ResultParser {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.client.result.ExpandedProductParsedResult parse(com.google.zxing.Result r25) {
        /*
        r24 = this;
        r20 = r25.getBarcodeFormat();
        r3 = com.google.zxing.BarcodeFormat.RSS_EXPANDED;
        r0 = r20;
        if (r0 == r3) goto L_0x000c;
    L_0x000a:
        r3 = 0;
    L_0x000b:
        return r3;
    L_0x000c:
        r4 = com.google.zxing.client.result.ResultParser.getMassagedText(r25);
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r12 = 0;
        r13 = 0;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r17 = 0;
        r18 = new java.util.HashMap;
        r18.<init>();
        r21 = 0;
    L_0x0026:
        r3 = r4.length();
        r0 = r21;
        if (r0 < r3) goto L_0x0034;
    L_0x002e:
        r3 = new com.google.zxing.client.result.ExpandedProductParsedResult;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        goto L_0x000b;
    L_0x0034:
        r0 = r21;
        r19 = findAIvalue(r0, r4);
        if (r19 != 0) goto L_0x003e;
    L_0x003c:
        r3 = 0;
        goto L_0x000b;
    L_0x003e:
        r3 = r19.length();
        r3 = r3 + 2;
        r21 = r21 + r3;
        r0 = r21;
        r22 = findValue(r0, r4);
        r3 = r22.length();
        r21 = r21 + r3;
        r3 = r19.hashCode();
        switch(r3) {
            case 1536: goto L_0x0063;
            case 1537: goto L_0x0070;
            case 1567: goto L_0x007d;
            case 1568: goto L_0x008a;
            case 1570: goto L_0x0097;
            case 1572: goto L_0x00a4;
            case 1574: goto L_0x00b2;
            case 1567966: goto L_0x00c0;
            case 1567967: goto L_0x00d7;
            case 1567968: goto L_0x00e3;
            case 1567969: goto L_0x00ef;
            case 1567970: goto L_0x00fb;
            case 1567971: goto L_0x0107;
            case 1567972: goto L_0x0113;
            case 1567973: goto L_0x011f;
            case 1567974: goto L_0x012b;
            case 1567975: goto L_0x0137;
            case 1568927: goto L_0x0143;
            case 1568928: goto L_0x015a;
            case 1568929: goto L_0x0166;
            case 1568930: goto L_0x0172;
            case 1568931: goto L_0x017e;
            case 1568932: goto L_0x018a;
            case 1568933: goto L_0x0196;
            case 1568934: goto L_0x01a2;
            case 1568935: goto L_0x01ae;
            case 1568936: goto L_0x01ba;
            case 1575716: goto L_0x01c6;
            case 1575717: goto L_0x01db;
            case 1575718: goto L_0x01e7;
            case 1575719: goto L_0x01f3;
            case 1575747: goto L_0x01ff;
            case 1575748: goto L_0x0216;
            case 1575749: goto L_0x0222;
            case 1575750: goto L_0x022e;
            default: goto L_0x0059;
        };
    L_0x0059:
        r0 = r18;
        r1 = r19;
        r2 = r22;
        r0.put(r1, r2);
        goto L_0x0026;
    L_0x0063:
        r3 = "00";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x006d:
        r6 = r22;
        goto L_0x0026;
    L_0x0070:
        r3 = "01";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x007a:
        r5 = r22;
        goto L_0x0026;
    L_0x007d:
        r3 = "10";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x0087:
        r7 = r22;
        goto L_0x0026;
    L_0x008a:
        r3 = "11";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x0094:
        r8 = r22;
        goto L_0x0026;
    L_0x0097:
        r3 = "13";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x00a1:
        r9 = r22;
        goto L_0x0026;
    L_0x00a4:
        r3 = "15";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x00ae:
        r10 = r22;
        goto L_0x0026;
    L_0x00b2:
        r3 = "17";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x00bc:
        r11 = r22;
        goto L_0x0026;
    L_0x00c0:
        r3 = "3100";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x00ca:
        r12 = r22;
        r13 = "KG";
        r3 = 3;
        r0 = r19;
        r14 = r0.substring(r3);
        goto L_0x0026;
    L_0x00d7:
        r3 = "3101";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x00e1:
        goto L_0x0059;
    L_0x00e3:
        r3 = "3102";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x00ed:
        goto L_0x0059;
    L_0x00ef:
        r3 = "3103";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x00f9:
        goto L_0x0059;
    L_0x00fb:
        r3 = "3104";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x0105:
        goto L_0x0059;
    L_0x0107:
        r3 = "3105";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x0111:
        goto L_0x0059;
    L_0x0113:
        r3 = "3106";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x011d:
        goto L_0x0059;
    L_0x011f:
        r3 = "3107";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x0129:
        goto L_0x0059;
    L_0x012b:
        r3 = "3108";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x0135:
        goto L_0x0059;
    L_0x0137:
        r3 = "3109";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x00ca;
    L_0x0141:
        goto L_0x0059;
    L_0x0143:
        r3 = "3200";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x014d:
        r12 = r22;
        r13 = "LB";
        r3 = 3;
        r0 = r19;
        r14 = r0.substring(r3);
        goto L_0x0026;
    L_0x015a:
        r3 = "3201";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x0164:
        goto L_0x0059;
    L_0x0166:
        r3 = "3202";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x0170:
        goto L_0x0059;
    L_0x0172:
        r3 = "3203";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x017c:
        goto L_0x0059;
    L_0x017e:
        r3 = "3204";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x0188:
        goto L_0x0059;
    L_0x018a:
        r3 = "3205";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x0194:
        goto L_0x0059;
    L_0x0196:
        r3 = "3206";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x01a0:
        goto L_0x0059;
    L_0x01a2:
        r3 = "3207";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x01ac:
        goto L_0x0059;
    L_0x01ae:
        r3 = "3208";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x01b8:
        goto L_0x0059;
    L_0x01ba:
        r3 = "3209";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x014d;
    L_0x01c4:
        goto L_0x0059;
    L_0x01c6:
        r3 = "3920";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x01d0:
        r15 = r22;
        r3 = 3;
        r0 = r19;
        r16 = r0.substring(r3);
        goto L_0x0026;
    L_0x01db:
        r3 = "3921";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x01d0;
    L_0x01e5:
        goto L_0x0059;
    L_0x01e7:
        r3 = "3922";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x01d0;
    L_0x01f1:
        goto L_0x0059;
    L_0x01f3:
        r3 = "3923";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x01d0;
    L_0x01fd:
        goto L_0x0059;
    L_0x01ff:
        r3 = "3930";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0059;
    L_0x0209:
        r3 = r22.length();
        r23 = 4;
        r0 = r23;
        if (r3 >= r0) goto L_0x023a;
    L_0x0213:
        r3 = 0;
        goto L_0x000b;
    L_0x0216:
        r3 = "3931";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x0209;
    L_0x0220:
        goto L_0x0059;
    L_0x0222:
        r3 = "3932";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x0209;
    L_0x022c:
        goto L_0x0059;
    L_0x022e:
        r3 = "3933";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x0209;
    L_0x0238:
        goto L_0x0059;
    L_0x023a:
        r3 = 3;
        r0 = r22;
        r15 = r0.substring(r3);
        r3 = 0;
        r23 = 3;
        r0 = r22;
        r1 = r23;
        r17 = r0.substring(r3, r1);
        r3 = 3;
        r0 = r19;
        r16 = r0.substring(r3);
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.client.result.ExpandedProductResultParser.parse(com.google.zxing.Result):com.google.zxing.client.result.ExpandedProductParsedResult");
    }

    private static String findAIvalue(int i, String rawText) {
        if (rawText.charAt(i) != '(') {
            return null;
        }
        CharSequence rawTextAux = rawText.substring(i + 1);
        StringBuilder buf = new StringBuilder();
        for (int index = 0; index < rawTextAux.length(); index++) {
            char currentChar = rawTextAux.charAt(index);
            if (currentChar == ')') {
                return buf.toString();
            }
            if (currentChar < '0' || currentChar > '9') {
                return null;
            }
            buf.append(currentChar);
        }
        return buf.toString();
    }

    private static String findValue(int i, String rawText) {
        StringBuilder buf = new StringBuilder();
        String rawTextAux = rawText.substring(i);
        for (int index = 0; index < rawTextAux.length(); index++) {
            char c = rawTextAux.charAt(index);
            if (c == '(') {
                if (findAIvalue(index, rawTextAux) != null) {
                    break;
                }
                buf.append('(');
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
