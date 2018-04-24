package org.bytedeco.javacpp.tools;

import java.util.ArrayList;
import java.util.List;

class TokenIndexer {
    Token[] array = null;
    int counter = 0;
    int index = 0;
    InfoMap infoMap = null;
    final boolean isCFile;
    boolean raw = false;

    TokenIndexer(InfoMap infoMap, Token[] array, boolean isCFile) {
        this.infoMap = infoMap;
        this.array = array;
        this.isCFile = isCFile;
    }

    Token[] filter(Token[] array, int index) {
        if (index + 1 >= array.length) {
            return array;
        }
        if (!array[index].match(Character.valueOf('#'))) {
            return array;
        }
        if (!array[index + 1].match(Token.IF, Token.IFDEF, Token.IFNDEF)) {
            return array;
        }
        List<Token> tokens = new ArrayList();
        for (int i = 0; i < index; i++) {
            tokens.add(array[i]);
        }
        int count = 0;
        Info info = null;
        boolean define = true;
        boolean defined = false;
        while (index < array.length) {
            String spacing = array[index].spacing;
            int n = spacing.lastIndexOf(10) + 1;
            Token keyword = null;
            if (array[index].match(Character.valueOf('#'))) {
                if (array[index + 1].match(Token.IF, Token.IFDEF, Token.IFNDEF)) {
                    count++;
                }
                if (count == 1) {
                    if (array[index + 1].match(Token.IF, Token.IFDEF, Token.IFNDEF, Token.ELIF, Token.ELSE, Token.ENDIF)) {
                        keyword = array[index + 1];
                    }
                }
                if (array[index + 1].match(Token.ENDIF)) {
                    count--;
                }
            }
            if (keyword != null) {
                index += 2;
                Token comment = new Token();
                comment.type = 4;
                comment.spacing = spacing.substring(0, n);
                comment.value = "// " + spacing.substring(n) + "#" + keyword.spacing + keyword;
                tokens.add(comment);
                String value = "";
                while (index < array.length && array[index].spacing.indexOf(10) < 0) {
                    String str;
                    if (!array[index].match(Integer.valueOf(4))) {
                        value = value + array[index].spacing + array[index];
                    }
                    StringBuilder append = new StringBuilder().append(comment.value);
                    if (array[index].match("\n")) {
                        str = "\n// ";
                    } else {
                        str = array[index].spacing + array[index].toString().replaceAll("\n", "\n// ");
                    }
                    comment.value = append.append(str).toString();
                    index++;
                }
                if (!keyword.match(Token.IF, Token.IFDEF, Token.IFNDEF, Token.ELIF)) {
                    if (!keyword.match(Token.ELSE)) {
                        if (keyword.match(Token.ENDIF) && count == 0) {
                            break;
                        }
                    }
                    define = info == null || !define;
                } else {
                    define = info == null || !defined;
                    info = this.infoMap.getFirst(value);
                    if (info != null) {
                        define = keyword.match(Token.IFNDEF) ? !info.define : info.define;
                    } else {
                        try {
                            define = Integer.parseInt(value.trim()) != 0;
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            } else if (define) {
                int index2 = index + 1;
                tokens.add(array[index]);
                index = index2;
            } else {
                index++;
            }
            defined = define || defined;
        }
        while (index < array.length) {
            tokens.add(array[index]);
            index++;
        }
        return (Token[]) tokens.toArray(new Token[tokens.size()]);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    org.bytedeco.javacpp.tools.Token[] expand(org.bytedeco.javacpp.tools.Token[] r25, int r26) {
        /*
        r24 = this;
        r0 = r25;
        r0 = r0.length;
        r19 = r0;
        r0 = r26;
        r1 = r19;
        if (r0 >= r1) goto L_0x0423;
    L_0x000b:
        r0 = r24;
        r0 = r0.infoMap;
        r19 = r0;
        r20 = r25[r26];
        r0 = r20;
        r0 = r0.value;
        r20 = r0;
        r19 = r19.containsKey(r20);
        if (r19 == 0) goto L_0x0423;
    L_0x001f:
        r13 = r26;
        r0 = r24;
        r0 = r0.infoMap;
        r19 = r0;
        r20 = r25[r26];
        r0 = r20;
        r0 = r0.value;
        r20 = r0;
        r10 = r19.getFirst(r20);
        if (r10 == 0) goto L_0x0423;
    L_0x0035:
        r0 = r10.cppText;
        r19 = r0;
        if (r19 == 0) goto L_0x0423;
    L_0x003b:
        r17 = new org.bytedeco.javacpp.tools.Tokenizer;	 Catch:{ IOException -> 0x02c0 }
        r0 = r10.cppText;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r17;
        r1 = r19;
        r0.<init>(r1);	 Catch:{ IOException -> 0x02c0 }
        r19 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
        r20 = 1;
        r0 = r20;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r21 = 0;
        r22 = 35;
        r22 = java.lang.Character.valueOf(r22);	 Catch:{ IOException -> 0x02c0 }
        r20[r21] = r22;	 Catch:{ IOException -> 0x02c0 }
        r19 = r19.match(r20);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x009a;
    L_0x0064:
        r19 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
        r20 = 1;
        r0 = r20;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r21 = 0;
        r22 = org.bytedeco.javacpp.tools.Token.DEFINE;	 Catch:{ IOException -> 0x02c0 }
        r20[r21] = r22;	 Catch:{ IOException -> 0x02c0 }
        r19 = r19.match(r20);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x009a;
    L_0x007c:
        r19 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
        r20 = 1;
        r0 = r20;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r21 = 0;
        r0 = r10.cppNames;	 Catch:{ IOException -> 0x02c0 }
        r22 = r0;
        r23 = 0;
        r22 = r22[r23];	 Catch:{ IOException -> 0x02c0 }
        r20[r21] = r22;	 Catch:{ IOException -> 0x02c0 }
        r19 = r19.match(r20);	 Catch:{ IOException -> 0x02c0 }
        if (r19 != 0) goto L_0x009d;
    L_0x009a:
        r4 = r25;
    L_0x009c:
        return r4;
    L_0x009d:
        r18 = new java.util.ArrayList;	 Catch:{ IOException -> 0x02c0 }
        r18.<init>();	 Catch:{ IOException -> 0x02c0 }
        r9 = 0;
    L_0x00a3:
        r0 = r26;
        if (r9 >= r0) goto L_0x00af;
    L_0x00a7:
        r19 = r25[r9];	 Catch:{ IOException -> 0x02c0 }
        r18.add(r19);	 Catch:{ IOException -> 0x02c0 }
        r9 = r9 + 1;
        goto L_0x00a3;
    L_0x00af:
        r12 = new java.util.ArrayList;	 Catch:{ IOException -> 0x02c0 }
        r12.<init>();	 Catch:{ IOException -> 0x02c0 }
        r3 = 0;
        r15 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
        r0 = r10.cppNames;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r19 = r19[r20];	 Catch:{ IOException -> 0x02c0 }
        r20 = "__COUNTER__";
        r19 = r19.equals(r20);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x00df;
    L_0x00c9:
        r0 = r24;
        r0 = r0.counter;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = r19 + 1;
        r0 = r20;
        r1 = r24;
        r1.counter = r0;	 Catch:{ IOException -> 0x02c0 }
        r19 = java.lang.Integer.toString(r19);	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r15.value = r0;	 Catch:{ IOException -> 0x02c0 }
    L_0x00df:
        r19 = r25[r26];	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r11 = r0.value;	 Catch:{ IOException -> 0x02c0 }
        r19 = 1;
        r0 = r19;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r21 = 40;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r19 = r15.match(r0);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x02fc;
    L_0x00ff:
        r15 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
    L_0x0103:
        r19 = r15.isEmpty();	 Catch:{ IOException -> 0x02c0 }
        if (r19 != 0) goto L_0x014f;
    L_0x0109:
        r19 = 1;
        r0 = r19;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r21 = 5;
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r19 = r15.match(r0);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x0131;
    L_0x0123:
        r0 = r15.value;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r19;
        r12.add(r0);	 Catch:{ IOException -> 0x02c0 }
    L_0x012c:
        r15 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
        goto L_0x0103;
    L_0x0131:
        r19 = 1;
        r0 = r19;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r21 = 41;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r19 = r15.match(r0);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x012c;
    L_0x014b:
        r15 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
    L_0x014f:
        r26 = r26 + 1;
        r19 = r12.size();	 Catch:{ IOException -> 0x02c0 }
        if (r19 <= 0) goto L_0x0180;
    L_0x0157:
        r0 = r25;
        r0 = r0.length;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r26;
        r1 = r19;
        if (r0 >= r1) goto L_0x017c;
    L_0x0162:
        r19 = r25[r26];	 Catch:{ IOException -> 0x02c0 }
        r20 = 1;
        r0 = r20;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r21 = 0;
        r22 = 40;
        r22 = java.lang.Character.valueOf(r22);	 Catch:{ IOException -> 0x02c0 }
        r20[r21] = r22;	 Catch:{ IOException -> 0x02c0 }
        r19 = r19.match(r20);	 Catch:{ IOException -> 0x02c0 }
        if (r19 != 0) goto L_0x0180;
    L_0x017c:
        r4 = r25;
        goto L_0x009c;
    L_0x0180:
        r19 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x02c0 }
        r19.<init>();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r19 = r0.append(r11);	 Catch:{ IOException -> 0x02c0 }
        r20 = r25[r26];	 Catch:{ IOException -> 0x02c0 }
        r0 = r20;
        r0 = r0.spacing;	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r19 = r19.append(r20);	 Catch:{ IOException -> 0x02c0 }
        r20 = r25[r26];	 Catch:{ IOException -> 0x02c0 }
        r19 = r19.append(r20);	 Catch:{ IOException -> 0x02c0 }
        r11 = r19.toString();	 Catch:{ IOException -> 0x02c0 }
        r19 = r12.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r3 = new java.util.List[r0];	 Catch:{ IOException -> 0x02c0 }
        r5 = 0;
        r6 = 0;
        r26 = r26 + 1;
    L_0x01ad:
        r0 = r25;
        r0 = r0.length;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r26;
        r1 = r19;
        if (r0 >= r1) goto L_0x01f9;
    L_0x01b8:
        r16 = r25[r26];	 Catch:{ IOException -> 0x02c0 }
        r19 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x02c0 }
        r19.<init>();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r19 = r0.append(r11);	 Catch:{ IOException -> 0x02c0 }
        r0 = r16;
        r0 = r0.spacing;	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r19 = r19.append(r20);	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r1 = r16;
        r19 = r0.append(r1);	 Catch:{ IOException -> 0x02c0 }
        r11 = r19.toString();	 Catch:{ IOException -> 0x02c0 }
        if (r6 != 0) goto L_0x024e;
    L_0x01dd:
        r19 = 1;
        r0 = r19;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r21 = 41;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r0 = r16;
        r1 = r19;
        r19 = r0.match(r1);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x024e;
    L_0x01f9:
        r9 = 0;
    L_0x01fa:
        r0 = r3.length;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r19;
        if (r9 >= r0) goto L_0x02fc;
    L_0x0201:
        r0 = r24;
        r0 = r0.infoMap;	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r19 = r3[r9];	 Catch:{ IOException -> 0x02c0 }
        r21 = 0;
        r0 = r19;
        r1 = r21;
        r19 = r0.get(r1);	 Catch:{ IOException -> 0x02c0 }
        r19 = (org.bytedeco.javacpp.tools.Token) r19;	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r0 = r0.value;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r20;
        r1 = r19;
        r19 = r0.containsKey(r1);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x024b;
    L_0x0225:
        r19 = r3[r9];	 Catch:{ IOException -> 0x02c0 }
        r20 = r3[r9];	 Catch:{ IOException -> 0x02c0 }
        r20 = r20.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r20;
        r0 = new org.bytedeco.javacpp.tools.Token[r0];	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r19 = r19.toArray(r20);	 Catch:{ IOException -> 0x02c0 }
        r19 = (org.bytedeco.javacpp.tools.Token[]) r19;	 Catch:{ IOException -> 0x02c0 }
        r20 = 0;
        r0 = r24;
        r1 = r19;
        r2 = r20;
        r19 = r0.expand(r1, r2);	 Catch:{ IOException -> 0x02c0 }
        r19 = java.util.Arrays.asList(r19);	 Catch:{ IOException -> 0x02c0 }
        r3[r9] = r19;	 Catch:{ IOException -> 0x02c0 }
    L_0x024b:
        r9 = r9 + 1;
        goto L_0x01fa;
    L_0x024e:
        if (r6 != 0) goto L_0x0272;
    L_0x0250:
        r19 = 1;
        r0 = r19;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r21 = 44;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r0 = r16;
        r1 = r19;
        r19 = r0.match(r1);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x0272;
    L_0x026c:
        r5 = r5 + 1;
    L_0x026e:
        r26 = r26 + 1;
        goto L_0x01ad;
    L_0x0272:
        r19 = 3;
        r0 = r19;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r21 = 40;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r20 = 1;
        r21 = 91;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r20 = 2;
        r21 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r0 = r16;
        r1 = r19;
        r19 = r0.match(r1);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x02c9;
    L_0x02a2:
        r6 = r6 + 1;
    L_0x02a4:
        r0 = r3.length;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r19;
        if (r5 >= r0) goto L_0x026e;
    L_0x02ab:
        r19 = r3[r5];	 Catch:{ IOException -> 0x02c0 }
        if (r19 != 0) goto L_0x02b6;
    L_0x02af:
        r19 = new java.util.ArrayList;	 Catch:{ IOException -> 0x02c0 }
        r19.<init>();	 Catch:{ IOException -> 0x02c0 }
        r3[r5] = r19;	 Catch:{ IOException -> 0x02c0 }
    L_0x02b6:
        r19 = r3[r5];	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r1 = r16;
        r0.add(r1);	 Catch:{ IOException -> 0x02c0 }
        goto L_0x026e;
    L_0x02c0:
        r7 = move-exception;
        r19 = new java.lang.RuntimeException;
        r0 = r19;
        r0.<init>(r7);
        throw r19;
    L_0x02c9:
        r19 = 3;
        r0 = r19;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = 0;
        r21 = 41;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r20 = 1;
        r21 = 93;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r20 = 2;
        r21 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r21 = java.lang.Character.valueOf(r21);	 Catch:{ IOException -> 0x02c0 }
        r19[r20] = r21;	 Catch:{ IOException -> 0x02c0 }
        r0 = r16;
        r1 = r19;
        r19 = r0.match(r1);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x02a4;
    L_0x02f9:
        r6 = r6 + -1;
        goto L_0x02a4;
    L_0x02fc:
        r14 = r18.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r24;
        r0 = r0.infoMap;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r19;
        r10 = r0.getFirst(r11);	 Catch:{ IOException -> 0x02c0 }
    L_0x030c:
        if (r10 == 0) goto L_0x0314;
    L_0x030e:
        r0 = r10.skip;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        if (r19 != 0) goto L_0x035b;
    L_0x0314:
        r19 = r15.isEmpty();	 Catch:{ IOException -> 0x02c0 }
        if (r19 != 0) goto L_0x035b;
    L_0x031a:
        r8 = 0;
        r9 = 0;
    L_0x031c:
        r19 = r12.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        if (r9 >= r0) goto L_0x033a;
    L_0x0324:
        r19 = r12.get(r9);	 Catch:{ IOException -> 0x02c0 }
        r19 = (java.lang.String) r19;	 Catch:{ IOException -> 0x02c0 }
        r0 = r15.value;	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r19 = r19.equals(r20);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x0358;
    L_0x0334:
        r19 = r3[r9];	 Catch:{ IOException -> 0x02c0 }
        r18.addAll(r19);	 Catch:{ IOException -> 0x02c0 }
        r8 = 1;
    L_0x033a:
        if (r8 != 0) goto L_0x0353;
    L_0x033c:
        r0 = r15.type;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x034e;
    L_0x0348:
        r19 = 4;
        r0 = r19;
        r15.type = r0;	 Catch:{ IOException -> 0x02c0 }
    L_0x034e:
        r0 = r18;
        r0.add(r15);	 Catch:{ IOException -> 0x02c0 }
    L_0x0353:
        r15 = r17.nextToken();	 Catch:{ IOException -> 0x02c0 }
        goto L_0x030c;
    L_0x0358:
        r9 = r9 + 1;
        goto L_0x031c;
    L_0x035b:
        r9 = r14;
    L_0x035c:
        r19 = r18.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        if (r9 >= r0) goto L_0x03d8;
    L_0x0364:
        r0 = r18;
        r19 = r0.get(r9);	 Catch:{ IOException -> 0x02c0 }
        r19 = (org.bytedeco.javacpp.tools.Token) r19;	 Catch:{ IOException -> 0x02c0 }
        r20 = 1;
        r0 = r20;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r21 = 0;
        r22 = "##";
        r20[r21] = r22;	 Catch:{ IOException -> 0x02c0 }
        r19 = r19.match(r20);	 Catch:{ IOException -> 0x02c0 }
        if (r19 == 0) goto L_0x03d5;
    L_0x0380:
        if (r9 <= 0) goto L_0x03d5;
    L_0x0382:
        r19 = r9 + 1;
        r20 = r18.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r1 = r20;
        if (r0 >= r1) goto L_0x03d5;
    L_0x038e:
        r20 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x02c0 }
        r20.<init>();	 Catch:{ IOException -> 0x02c0 }
        r19 = r9 + -1;
        r19 = r18.get(r19);	 Catch:{ IOException -> 0x02c0 }
        r19 = (org.bytedeco.javacpp.tools.Token) r19;	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r0 = r0.value;	 Catch:{ IOException -> 0x02c0 }
        r21 = r0;
        r21 = r20.append(r21);	 Catch:{ IOException -> 0x02c0 }
        r20 = r9 + 1;
        r0 = r18;
        r1 = r20;
        r20 = r0.get(r1);	 Catch:{ IOException -> 0x02c0 }
        r20 = (org.bytedeco.javacpp.tools.Token) r20;	 Catch:{ IOException -> 0x02c0 }
        r0 = r20;
        r0 = r0.value;	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r0 = r21;
        r1 = r20;
        r20 = r0.append(r1);	 Catch:{ IOException -> 0x02c0 }
        r20 = r20.toString();	 Catch:{ IOException -> 0x02c0 }
        r0 = r20;
        r1 = r19;
        r1.value = r0;	 Catch:{ IOException -> 0x02c0 }
        r0 = r18;
        r0.remove(r9);	 Catch:{ IOException -> 0x02c0 }
        r0 = r18;
        r0.remove(r9);	 Catch:{ IOException -> 0x02c0 }
        r9 = r9 + -1;
    L_0x03d5:
        r9 = r9 + 1;
        goto L_0x035c;
    L_0x03d8:
        r26 = r26 + 1;
    L_0x03da:
        r0 = r25;
        r0 = r0.length;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r0 = r26;
        r1 = r19;
        if (r0 >= r1) goto L_0x03ed;
    L_0x03e5:
        r19 = r25[r26];	 Catch:{ IOException -> 0x02c0 }
        r18.add(r19);	 Catch:{ IOException -> 0x02c0 }
        r26 = r26 + 1;
        goto L_0x03da;
    L_0x03ed:
        if (r10 == 0) goto L_0x03f5;
    L_0x03ef:
        r0 = r10.skip;	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        if (r19 != 0) goto L_0x0413;
    L_0x03f5:
        r19 = r18.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        if (r14 >= r0) goto L_0x0413;
    L_0x03fd:
        r0 = r18;
        r19 = r0.get(r14);	 Catch:{ IOException -> 0x02c0 }
        r19 = (org.bytedeco.javacpp.tools.Token) r19;	 Catch:{ IOException -> 0x02c0 }
        r20 = r25[r13];	 Catch:{ IOException -> 0x02c0 }
        r0 = r20;
        r0 = r0.spacing;	 Catch:{ IOException -> 0x02c0 }
        r20 = r0;
        r0 = r20;
        r1 = r19;
        r1.spacing = r0;	 Catch:{ IOException -> 0x02c0 }
    L_0x0413:
        r19 = r18.size();	 Catch:{ IOException -> 0x02c0 }
        r0 = r19;
        r0 = new org.bytedeco.javacpp.tools.Token[r0];	 Catch:{ IOException -> 0x02c0 }
        r19 = r0;
        r25 = r18.toArray(r19);	 Catch:{ IOException -> 0x02c0 }
        r25 = (org.bytedeco.javacpp.tools.Token[]) r25;	 Catch:{ IOException -> 0x02c0 }
    L_0x0423:
        r4 = r25;
        goto L_0x009c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.TokenIndexer.expand(org.bytedeco.javacpp.tools.Token[], int):org.bytedeco.javacpp.tools.Token[]");
    }

    int preprocess(int index, int count) {
        Token[] a;
        while (index < this.array.length) {
            a = null;
            while (a != this.array) {
                a = this.array;
                this.array = filter(this.array, index);
                this.array = expand(this.array, index);
            }
            if (!this.array[index].match(Integer.valueOf(4))) {
                count--;
                if (count < 0) {
                    break;
                }
            }
            index++;
        }
        a = null;
        while (a != this.array) {
            a = this.array;
            this.array = filter(this.array, index);
            this.array = expand(this.array, index);
        }
        return index;
    }

    Token get() {
        return get(0);
    }

    Token get(int i) {
        int k = this.raw ? this.index + i : preprocess(this.index, i);
        return k < this.array.length ? this.array[k] : Token.EOF;
    }

    Token next() {
        this.index = this.raw ? this.index + 1 : preprocess(this.index, 1);
        return this.index < this.array.length ? this.array[this.index] : Token.EOF;
    }
}
