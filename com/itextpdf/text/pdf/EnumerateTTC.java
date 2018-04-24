package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import java.io.IOException;

class EnumerateTTC extends TrueTypeFont {
    protected String[] names;

    void findNames() throws com.itextpdf.text.DocumentException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r13 = this;
        r8 = new java.util.HashMap;
        r8.<init>();
        r13.tables = r8;
        r8 = 4;
        r4 = r13.readStandardString(r8);	 Catch:{ all -> 0x0028 }
        r8 = "ttcf";	 Catch:{ all -> 0x0028 }
        r8 = r4.equals(r8);	 Catch:{ all -> 0x0028 }
        if (r8 != 0) goto L_0x0033;	 Catch:{ all -> 0x0028 }
    L_0x0014:
        r8 = new com.itextpdf.text.DocumentException;	 Catch:{ all -> 0x0028 }
        r9 = "1.is.not.a.valid.ttc.file";	 Catch:{ all -> 0x0028 }
        r10 = 1;	 Catch:{ all -> 0x0028 }
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x0028 }
        r11 = 0;	 Catch:{ all -> 0x0028 }
        r12 = r13.fileName;	 Catch:{ all -> 0x0028 }
        r10[r11] = r12;	 Catch:{ all -> 0x0028 }
        r9 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r9, r10);	 Catch:{ all -> 0x0028 }
        r8.<init>(r9);	 Catch:{ all -> 0x0028 }
        throw r8;	 Catch:{ all -> 0x0028 }
    L_0x0028:
        r8 = move-exception;
        r9 = r13.rf;
        if (r9 == 0) goto L_0x0032;
    L_0x002d:
        r9 = r13.rf;
        r9.close();
    L_0x0032:
        throw r8;
    L_0x0033:
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r9 = 4;	 Catch:{ all -> 0x0028 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r0 = r8.readInt();	 Catch:{ all -> 0x0028 }
        r8 = new java.lang.String[r0];	 Catch:{ all -> 0x0028 }
        r13.names = r8;	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r8 = r8.getFilePointer();	 Catch:{ all -> 0x0028 }
        r2 = (int) r8;	 Catch:{ all -> 0x0028 }
        r1 = 0;	 Catch:{ all -> 0x0028 }
    L_0x004b:
        if (r1 >= r0) goto L_0x00d0;	 Catch:{ all -> 0x0028 }
    L_0x004d:
        r8 = r13.tables;	 Catch:{ all -> 0x0028 }
        r8.clear();	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r10 = (long) r2;	 Catch:{ all -> 0x0028 }
        r8.seek(r10);	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r9 = r1 * 4;	 Catch:{ all -> 0x0028 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r8 = r8.readInt();	 Catch:{ all -> 0x0028 }
        r13.directoryOffset = r8;	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r9 = r13.directoryOffset;	 Catch:{ all -> 0x0028 }
        r10 = (long) r9;	 Catch:{ all -> 0x0028 }
        r8.seek(r10);	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r8 = r8.readInt();	 Catch:{ all -> 0x0028 }
        r9 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;	 Catch:{ all -> 0x0028 }
        if (r8 == r9) goto L_0x008d;	 Catch:{ all -> 0x0028 }
    L_0x0079:
        r8 = new com.itextpdf.text.DocumentException;	 Catch:{ all -> 0x0028 }
        r9 = "1.is.not.a.valid.ttf.file";	 Catch:{ all -> 0x0028 }
        r10 = 1;	 Catch:{ all -> 0x0028 }
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x0028 }
        r11 = 0;	 Catch:{ all -> 0x0028 }
        r12 = r13.fileName;	 Catch:{ all -> 0x0028 }
        r10[r11] = r12;	 Catch:{ all -> 0x0028 }
        r9 = com.itextpdf.text.error_messages.MessageLocalization.getComposedMessage(r9, r10);	 Catch:{ all -> 0x0028 }
        r8.<init>(r9);	 Catch:{ all -> 0x0028 }
        throw r8;	 Catch:{ all -> 0x0028 }
    L_0x008d:
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r5 = r8.readUnsignedShort();	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r9 = 6;	 Catch:{ all -> 0x0028 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0028 }
        r3 = 0;	 Catch:{ all -> 0x0028 }
    L_0x009a:
        if (r3 >= r5) goto L_0x00c4;	 Catch:{ all -> 0x0028 }
    L_0x009c:
        r8 = 4;	 Catch:{ all -> 0x0028 }
        r7 = r13.readStandardString(r8);	 Catch:{ all -> 0x0028 }
        r8 = r13.rf;	 Catch:{ all -> 0x0028 }
        r9 = 4;	 Catch:{ all -> 0x0028 }
        r8.skipBytes(r9);	 Catch:{ all -> 0x0028 }
        r8 = 2;	 Catch:{ all -> 0x0028 }
        r6 = new int[r8];	 Catch:{ all -> 0x0028 }
        r8 = 0;	 Catch:{ all -> 0x0028 }
        r9 = r13.rf;	 Catch:{ all -> 0x0028 }
        r9 = r9.readInt();	 Catch:{ all -> 0x0028 }
        r6[r8] = r9;	 Catch:{ all -> 0x0028 }
        r8 = 1;	 Catch:{ all -> 0x0028 }
        r9 = r13.rf;	 Catch:{ all -> 0x0028 }
        r9 = r9.readInt();	 Catch:{ all -> 0x0028 }
        r6[r8] = r9;	 Catch:{ all -> 0x0028 }
        r8 = r13.tables;	 Catch:{ all -> 0x0028 }
        r8.put(r7, r6);	 Catch:{ all -> 0x0028 }
        r3 = r3 + 1;	 Catch:{ all -> 0x0028 }
        goto L_0x009a;	 Catch:{ all -> 0x0028 }
    L_0x00c4:
        r8 = r13.names;	 Catch:{ all -> 0x0028 }
        r9 = r13.getBaseFont();	 Catch:{ all -> 0x0028 }
        r8[r1] = r9;	 Catch:{ all -> 0x0028 }
        r1 = r1 + 1;
        goto L_0x004b;
    L_0x00d0:
        r8 = r13.rf;
        if (r8 == 0) goto L_0x00d9;
    L_0x00d4:
        r8 = r13.rf;
        r8.close();
    L_0x00d9:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.EnumerateTTC.findNames():void");
    }

    EnumerateTTC(String ttcFile) throws DocumentException, IOException {
        this.fileName = ttcFile;
        this.rf = new RandomAccessFileOrArray(ttcFile);
        findNames();
    }

    EnumerateTTC(byte[] ttcArray) throws DocumentException, IOException {
        this.fileName = "Byte array TTC";
        this.rf = new RandomAccessFileOrArray(ttcArray);
        findNames();
    }

    String[] getNames() {
        return this.names;
    }
}
