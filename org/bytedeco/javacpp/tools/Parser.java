package org.bytedeco.javacpp.tools;

import com.itextpdf.text.Meta;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.xmp.XMPConst;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.Loader;

public class Parser {
    String[] docTags;
    InfoMap infoMap;
    InfoMap leafInfoMap;
    String lineSeparator;
    final Logger logger;
    final Properties properties;
    TokenIndexer tokens;

    public Parser(Logger logger, Properties properties) {
        this(logger, properties, null);
    }

    public Parser(Logger logger, Properties properties, String lineSeparator) {
        this.infoMap = null;
        this.leafInfoMap = null;
        this.tokens = null;
        this.lineSeparator = null;
        this.docTags = new String[]{Meta.AUTHOR, "deprecated", "exception", "param", "return", "see", "since", "throws", "version"};
        this.logger = logger;
        this.properties = properties;
        this.lineSeparator = lineSeparator;
    }

    Parser(Parser p, String text) {
        this.infoMap = null;
        this.leafInfoMap = null;
        this.tokens = null;
        this.lineSeparator = null;
        this.docTags = new String[]{Meta.AUTHOR, "deprecated", "exception", "param", "return", "see", "since", "throws", "version"};
        this.logger = p.logger;
        this.properties = p.properties;
        this.infoMap = p.infoMap;
        this.tokens = new TokenIndexer(this.infoMap, new Tokenizer(text).tokenize(), false);
        this.lineSeparator = p.lineSeparator;
    }

    String translate(String text) {
        int namespace = text.lastIndexOf("::");
        if (namespace < 0) {
            return text;
        }
        Info info2 = this.infoMap.getFirst(text.substring(0, namespace));
        text = text.substring(namespace + 2);
        if (info2 == null || info2.pointerTypes == null) {
            return text;
        }
        return info2.pointerTypes[0] + "." + text;
    }

    void containers(org.bytedeco.javacpp.tools.Context r40, org.bytedeco.javacpp.tools.DeclarationList r41) throws org.bytedeco.javacpp.tools.ParserException {
        /* JADX: method processing error */
/*
Error: java.lang.IndexOutOfBoundsException: bitIndex < 0: -1
	at java.util.BitSet.get(BitSet.java:623)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.usedArgAssign(CodeShrinker.java:138)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.access$300(CodeShrinker.java:43)
	at jadx.core.dex.visitors.CodeShrinker.canMoveBetweenBlocks(CodeShrinker.java:282)
	at jadx.core.dex.visitors.CodeShrinker.shrinkBlock(CodeShrinker.java:230)
	at jadx.core.dex.visitors.CodeShrinker.shrinkMethod(CodeShrinker.java:38)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkArrayForEach(LoopRegionVisitor.java:196)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkForIndexedLoop(LoopRegionVisitor.java:119)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:65)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r39 = this;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r0 = r39;
        r0 = r0.infoMap;
        r32 = r0;
        r33 = "basic/containers";
        r32 = r32.get(r33);
        r32 = r32.iterator();
    L_0x0015:
        r33 = r32.hasNext();
        if (r33 == 0) goto L_0x0031;
    L_0x001b:
        r20 = r32.next();
        r20 = (org.bytedeco.javacpp.tools.Info) r20;
        r0 = r20;
        r0 = r0.cppTypes;
        r33 = r0;
        r33 = java.util.Arrays.asList(r33);
        r0 = r33;
        r5.addAll(r0);
        goto L_0x0015;
    L_0x0031:
        r34 = r5.iterator();
    L_0x0035:
        r32 = r34.hasNext();
        if (r32 == 0) goto L_0x0fd8;
    L_0x003b:
        r9 = r34.next();
        r9 = (java.lang.String) r9;
        r0 = r39;
        r0 = r0.leafInfoMap;
        r32 = r0;
        r0 = r32;
        r21 = r0.get(r9);
        r35 = r21.iterator();
    L_0x0051:
        r32 = r35.hasNext();
        if (r32 == 0) goto L_0x0035;
    L_0x0057:
        r20 = r35.next();
        r20 = (org.bytedeco.javacpp.tools.Info) r20;
        r11 = new org.bytedeco.javacpp.tools.Declaration;
        r11.<init>();
        if (r20 == 0) goto L_0x0051;
    L_0x0064:
        r0 = r20;
        r0 = r0.skip;
        r32 = r0;
        if (r32 != 0) goto L_0x0051;
    L_0x006c:
        r0 = r20;
        r0 = r0.define;
        r32 = r0;
        if (r32 == 0) goto L_0x0051;
    L_0x0074:
        r32 = "std::pair";
        r0 = r32;
        r32 = r9.startsWith(r0);
        if (r32 == 0) goto L_0x0165;
    L_0x007e:
        r12 = 0;
    L_0x007f:
        r32 = new org.bytedeco.javacpp.tools.Parser;
        r0 = r20;
        r0 = r0.cppNames;
        r33 = r0;
        r36 = 0;
        r33 = r33[r36];
        r0 = r32;
        r1 = r39;
        r2 = r33;
        r0.<init>(r1, r2);
        r0 = r32;
        r1 = r40;
        r10 = r0.type(r1);
        r14 = 0;
        r28 = 0;
        r0 = r10.arguments;
        r32 = r0;
        if (r32 == 0) goto L_0x0051;
    L_0x00a5:
        r0 = r10.arguments;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        if (r32 == 0) goto L_0x0051;
    L_0x00b0:
        r0 = r10.arguments;
        r32 = r0;
        r33 = 0;
        r32 = r32[r33];
        if (r32 == 0) goto L_0x0051;
    L_0x00ba:
        r0 = r10.arguments;
        r32 = r0;
        r0 = r10.arguments;
        r33 = r0;
        r0 = r33;
        r0 = r0.length;
        r33 = r0;
        r33 = r33 + -1;
        r32 = r32[r33];
        if (r32 == 0) goto L_0x0051;
    L_0x00cd:
        r0 = r10.arguments;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 <= r1) goto L_0x0168;
    L_0x00de:
        r0 = r10.arguments;
        r32 = r0;
        r33 = 1;
        r32 = r32[r33];
        r0 = r32;
        r0 = r0.javaName;
        r32 = r0;
        r32 = r32.length();
        if (r32 <= 0) goto L_0x0168;
    L_0x00f2:
        r26 = 0;
        r0 = r10.arguments;
        r32 = r0;
        r33 = 0;
        r18 = r32[r33];
        r0 = r10.arguments;
        r32 = r0;
        r33 = 1;
        r31 = r32[r33];
    L_0x0104:
        r0 = r31;
        r0 = r0.javaName;
        r32 = r0;
        if (r32 == 0) goto L_0x0122;
    L_0x010c:
        r0 = r31;
        r0 = r0.javaName;
        r32 = r0;
        r32 = r32.length();
        if (r32 == 0) goto L_0x0122;
    L_0x0118:
        r32 = "std::bitset";
        r0 = r32;
        r32 = r9.startsWith(r0);
        if (r32 == 0) goto L_0x012c;
    L_0x0122:
        r32 = "boolean";
        r0 = r32;
        r1 = r31;
        r1.javaName = r0;
        r26 = 0;
    L_0x012c:
        r0 = r31;
        r0 = r0.cppName;
        r32 = r0;
        r0 = r32;
        r32 = r0.startsWith(r9);
        if (r32 == 0) goto L_0x01a5;
    L_0x013a:
        r0 = r39;
        r0 = r0.leafInfoMap;
        r32 = r0;
        r0 = r31;
        r0 = r0.cppName;
        r33 = r0;
        r36 = 0;
        r0 = r32;
        r1 = r33;
        r2 = r36;
        r32 = r0.get(r1, r2);
        r32 = r32.size();
        if (r32 != 0) goto L_0x01a5;
    L_0x0158:
        r12 = r12 + 1;
        r0 = r31;
        r0 = r0.arguments;
        r32 = r0;
        r33 = 0;
        r31 = r32[r33];
        goto L_0x012c;
    L_0x0165:
        r12 = 1;
        goto L_0x007f;
    L_0x0168:
        r0 = r10.arguments;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x01a2;
    L_0x0179:
        r26 = 1;
    L_0x017b:
        r18 = new org.bytedeco.javacpp.tools.Type;
        r18.<init>();
        r32 = "@Cast(\"size_t\") ";
        r0 = r32;
        r1 = r18;
        r1.annotations = r0;
        r32 = "size_t";
        r0 = r32;
        r1 = r18;
        r1.cppName = r0;
        r32 = "long";
        r0 = r32;
        r1 = r18;
        r1.javaName = r0;
        r0 = r10.arguments;
        r32 = r0;
        r33 = 0;
        r31 = r32[r33];
        goto L_0x0104;
    L_0x01a2:
        r26 = 0;
        goto L_0x017b;
    L_0x01a5:
        r32 = "std::pair";
        r0 = r32;
        r32 = r9.startsWith(r0);
        if (r32 == 0) goto L_0x01bf;
    L_0x01af:
        r0 = r10.arguments;
        r32 = r0;
        r33 = 0;
        r14 = r32[r33];
        r0 = r10.arguments;
        r32 = r0;
        r33 = 1;
        r28 = r32[r33];
    L_0x01bf:
        r0 = r31;
        r0 = r0.cppName;
        r32 = r0;
        r33 = "std::pair";
        r32 = r32.startsWith(r33);
        if (r32 == 0) goto L_0x01e1;
    L_0x01cd:
        r0 = r31;
        r0 = r0.arguments;
        r32 = r0;
        r33 = 0;
        r14 = r32[r33];
        r0 = r31;
        r0 = r0.arguments;
        r32 = r0;
        r33 = 1;
        r28 = r32[r33];
    L_0x01e1:
        if (r14 == 0) goto L_0x0226;
    L_0x01e3:
        r0 = r14.annotations;
        r32 = r0;
        if (r32 == 0) goto L_0x01f3;
    L_0x01e9:
        r0 = r14.annotations;
        r32 = r0;
        r32 = r32.length();
        if (r32 != 0) goto L_0x0226;
    L_0x01f3:
        r33 = new java.lang.StringBuilder;
        r33.<init>();
        r0 = r14.constValue;
        r32 = r0;
        if (r32 == 0) goto L_0x035f;
    L_0x01fe:
        r32 = "@Const ";
    L_0x0200:
        r0 = r33;
        r1 = r32;
        r33 = r0.append(r1);
        r0 = r14.indirections;
        r32 = r0;
        if (r32 != 0) goto L_0x0363;
    L_0x020e:
        r0 = r14.value;
        r32 = r0;
        if (r32 != 0) goto L_0x0363;
    L_0x0214:
        r32 = "@ByRef ";
    L_0x0216:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r32 = r32.toString();
        r0 = r32;
        r14.annotations = r0;
    L_0x0226:
        if (r28 == 0) goto L_0x0277;
    L_0x0228:
        r0 = r28;
        r0 = r0.annotations;
        r32 = r0;
        if (r32 == 0) goto L_0x023c;
    L_0x0230:
        r0 = r28;
        r0 = r0.annotations;
        r32 = r0;
        r32 = r32.length();
        if (r32 != 0) goto L_0x0277;
    L_0x023c:
        r33 = new java.lang.StringBuilder;
        r33.<init>();
        r0 = r28;
        r0 = r0.constValue;
        r32 = r0;
        if (r32 == 0) goto L_0x0367;
    L_0x0249:
        r32 = "@Const ";
    L_0x024b:
        r0 = r33;
        r1 = r32;
        r33 = r0.append(r1);
        r0 = r28;
        r0 = r0.indirections;
        r32 = r0;
        if (r32 != 0) goto L_0x036b;
    L_0x025b:
        r0 = r28;
        r0 = r0.value;
        r32 = r0;
        if (r32 != 0) goto L_0x036b;
    L_0x0263:
        r32 = "@ByRef ";
    L_0x0265:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r32 = r32.toString();
        r0 = r32;
        r1 = r28;
        r1.annotations = r0;
    L_0x0277:
        if (r31 == 0) goto L_0x02c8;
    L_0x0279:
        r0 = r31;
        r0 = r0.annotations;
        r32 = r0;
        if (r32 == 0) goto L_0x028d;
    L_0x0281:
        r0 = r31;
        r0 = r0.annotations;
        r32 = r0;
        r32 = r32.length();
        if (r32 != 0) goto L_0x02c8;
    L_0x028d:
        r33 = new java.lang.StringBuilder;
        r33.<init>();
        r0 = r31;
        r0 = r0.constValue;
        r32 = r0;
        if (r32 == 0) goto L_0x036f;
    L_0x029a:
        r32 = "@Const ";
    L_0x029c:
        r0 = r33;
        r1 = r32;
        r33 = r0.append(r1);
        r0 = r31;
        r0 = r0.indirections;
        r32 = r0;
        if (r32 != 0) goto L_0x0373;
    L_0x02ac:
        r0 = r31;
        r0 = r0.value;
        r32 = r0;
        if (r32 != 0) goto L_0x0373;
    L_0x02b4:
        r32 = "@ByRef ";
    L_0x02b6:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r32 = r32.toString();
        r0 = r32;
        r1 = r31;
        r1.annotations = r0;
    L_0x02c8:
        r0 = r39;
        r0 = r0.infoMap;
        r32 = r0;
        r0 = r31;
        r0 = r0.cppName;
        r33 = r0;
        r30 = r32.getFirst(r33);
        if (r30 == 0) goto L_0x03bf;
    L_0x02da:
        r0 = r30;
        r0 = r0.cast;
        r32 = r0;
        if (r32 == 0) goto L_0x03bf;
    L_0x02e2:
        r0 = r31;
        r8 = r0.cppName;
        r0 = r31;
        r0 = r0.constValue;
        r32 = r0;
        if (r32 == 0) goto L_0x030d;
    L_0x02ee:
        r32 = "const ";
        r0 = r32;
        r32 = r8.startsWith(r0);
        if (r32 != 0) goto L_0x030d;
    L_0x02f8:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "const ";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r8);
        r8 = r32.toString();
    L_0x030d:
        r0 = r31;
        r0 = r0.constPointer;
        r32 = r0;
        if (r32 == 0) goto L_0x0334;
    L_0x0315:
        r32 = " const";
        r0 = r32;
        r32 = r8.endsWith(r0);
        if (r32 != 0) goto L_0x0334;
    L_0x031f:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r32 = r0.append(r8);
        r33 = " const";
        r32 = r32.append(r33);
        r8 = r32.toString();
    L_0x0334:
        r0 = r31;
        r0 = r0.indirections;
        r32 = r0;
        if (r32 <= 0) goto L_0x0377;
    L_0x033c:
        r15 = 0;
    L_0x033d:
        r0 = r31;
        r0 = r0.indirections;
        r32 = r0;
        r0 = r32;
        if (r15 >= r0) goto L_0x0377;
    L_0x0347:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r32 = r0.append(r8);
        r33 = "*";
        r32 = r32.append(r33);
        r8 = r32.toString();
        r15 = r15 + 1;
        goto L_0x033d;
    L_0x035f:
        r32 = "";
        goto L_0x0200;
    L_0x0363:
        r32 = "";
        goto L_0x0216;
    L_0x0367:
        r32 = "";
        goto L_0x024b;
    L_0x036b:
        r32 = "";
        goto L_0x0265;
    L_0x036f:
        r32 = "";
        goto L_0x029c;
    L_0x0373:
        r32 = "";
        goto L_0x02b6;
    L_0x0377:
        r0 = r31;
        r0 = r0.reference;
        r32 = r0;
        if (r32 == 0) goto L_0x0394;
    L_0x037f:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r32 = r0.append(r8);
        r33 = "&";
        r32 = r32.append(r33);
        r8 = r32.toString();
    L_0x0394:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "@Cast(\"";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r8);
        r33 = "\") ";
        r32 = r32.append(r33);
        r0 = r31;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r1 = r31;
        r1.annotations = r0;
    L_0x03bf:
        r4 = "";
        r15 = 0;
    L_0x03c2:
        r32 = r12 + -1;
        r0 = r32;
        if (r15 >= r0) goto L_0x03e0;
    L_0x03c8:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r32 = r0.append(r4);
        r33 = "[]";
        r32 = r32.append(r33);
        r4 = r32.toString();
        r15 = r15 + 1;
        goto L_0x03c2;
    L_0x03e0:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r33 = r32.append(r33);
        if (r12 != 0) goto L_0x050e;
    L_0x03ef:
        r32 = "\n@NoOffset ";
    L_0x03f1:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r33 = "@Name(\"";
        r32 = r32.append(r33);
        r0 = r10.cppName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "\") public static class ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " extends Pointer {\n    static { Loader.load(); }\n    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */\n    public ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "(Pointer p) { super(p); }\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        if (r12 == 0) goto L_0x0444;
    L_0x0433:
        r0 = r10.arguments;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x053d;
    L_0x0444:
        if (r14 == 0) goto L_0x053d;
    L_0x0446:
        if (r28 == 0) goto L_0x053d;
    L_0x0448:
        r0 = r14.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0512;
    L_0x044e:
        r13 = r14.javaNames;
    L_0x0450:
        r0 = r28;
        r0 = r0.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0522;
    L_0x0458:
        r0 = r28;
        r0 = r0.javaNames;
        r27 = r0;
    L_0x045e:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r33 = r0.append(r4);
        if (r12 <= 0) goto L_0x0536;
    L_0x046b:
        r32 = "[]";
    L_0x046d:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r6 = r32.toString();
        r24 = 0;
    L_0x047b:
        r0 = r13.length;
        r32 = r0;
        r0 = r24;
        r1 = r32;
        if (r0 < r1) goto L_0x048f;
    L_0x0484:
        r0 = r27;
        r0 = r0.length;
        r32 = r0;
        r0 = r24;
        r1 = r32;
        if (r0 >= r1) goto L_0x05b3;
    L_0x048f:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "    public ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "(";
        r32 = r32.append(r33);
        r0 = r13.length;
        r33 = r0;
        r33 = r33 + -1;
        r0 = r24;
        r1 = r33;
        r33 = java.lang.Math.min(r0, r1);
        r33 = r13[r33];
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r6);
        r33 = " firstValue, ";
        r32 = r32.append(r33);
        r0 = r27;
        r0 = r0.length;
        r33 = r0;
        r33 = r33 + -1;
        r0 = r24;
        r1 = r33;
        r33 = java.lang.Math.min(r0, r1);
        r33 = r27[r33];
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r6);
        r33 = " secondValue) { this(";
        r33 = r32.append(r33);
        if (r12 <= 0) goto L_0x053a;
    L_0x04f2:
        r32 = "Math.min(firstValue.length, secondValue.length)";
    L_0x04f4:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r33 = "); put(firstValue, secondValue); }\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r24 = r24 + 1;
        goto L_0x047b;
    L_0x050e:
        r32 = "\n";
        goto L_0x03f1;
    L_0x0512:
        r32 = 1;
        r0 = r32;
        r13 = new java.lang.String[r0];
        r32 = 0;
        r0 = r14.javaName;
        r33 = r0;
        r13[r32] = r33;
        goto L_0x0450;
    L_0x0522:
        r32 = 1;
        r0 = r32;
        r0 = new java.lang.String[r0];
        r27 = r0;
        r32 = 0;
        r0 = r28;
        r0 = r0.javaName;
        r33 = r0;
        r27[r32] = r33;
        goto L_0x045e;
    L_0x0536:
        r32 = "";
        goto L_0x046d;
    L_0x053a:
        r32 = "";
        goto L_0x04f4;
    L_0x053d:
        if (r26 == 0) goto L_0x05b3;
    L_0x053f:
        if (r14 != 0) goto L_0x05b3;
    L_0x0541:
        if (r28 != 0) goto L_0x05b3;
    L_0x0543:
        r0 = r31;
        r0 = r0.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x05a0;
    L_0x054b:
        r0 = r31;
        r0 = r0.javaNames;
        r32 = r0;
    L_0x0551:
        r0 = r32;
        r0 = r0.length;
        r36 = r0;
        r33 = 0;
    L_0x0558:
        r0 = r33;
        r1 = r36;
        if (r0 >= r1) goto L_0x05b3;
    L_0x055e:
        r23 = r32[r33];
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r11.text;
        r38 = r0;
        r37 = r37.append(r38);
        r38 = "    public ";
        r37 = r37.append(r38);
        r0 = r10.javaName;
        r38 = r0;
        r37 = r37.append(r38);
        r38 = "(";
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r23;
        r37 = r0.append(r1);
        r0 = r37;
        r37 = r0.append(r4);
        r38 = " ... array) { this(array.length); put(array); }\n";
        r37 = r37.append(r38);
        r37 = r37.toString();
        r0 = r37;
        r11.text = r0;
        r33 = r33 + 1;
        goto L_0x0558;
    L_0x05a0:
        r32 = 1;
        r0 = r32;
        r0 = new java.lang.String[r0];
        r32 = r0;
        r33 = 0;
        r0 = r31;
        r0 = r0.javaName;
        r36 = r0;
        r32[r33] = r36;
        goto L_0x0551;
    L_0x05b3:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "    public ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "()       { allocate();  }\n";
        r33 = r32.append(r33);
        if (r26 != 0) goto L_0x06ae;
    L_0x05d6:
        r32 = "";
    L_0x05d8:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r33 = "    private native void allocate();\n";
        r33 = r32.append(r33);
        if (r26 != 0) goto L_0x06d9;
    L_0x05e8:
        r32 = "";
    L_0x05ea:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r33 = "    public native @Name(\"operator=\") @ByRef ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " put(@ByRef ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " x);\n\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = 0;
    L_0x061d:
        if (r15 >= r12) goto L_0x076c;
    L_0x061f:
        if (r15 <= 0) goto L_0x06e1;
    L_0x0621:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "@Index";
        r33 = r32.append(r33);
        r32 = 1;
        r0 = r32;
        if (r15 <= r0) goto L_0x06dd;
    L_0x0632:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r36 = "(";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r0 = r32;
        r32 = r0.append(r15);
        r36 = ") ";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r32 = r32.toString();
    L_0x0655:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r17 = r32.toString();
    L_0x0661:
        r19 = "";
        r29 = "";
        r22 = 0;
    L_0x0667:
        r0 = r22;
        if (r0 >= r15) goto L_0x06e5;
    L_0x066b:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r18;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r18;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " ";
        r32 = r32.append(r33);
        r33 = r22 + 105;
        r0 = r33;
        r0 = (char) r0;
        r33 = r0;
        r32 = r32.append(r33);
        r19 = r32.toString();
        r29 = ", ";
        r22 = r22 + 1;
        goto L_0x0667;
    L_0x06ae:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r36 = "    public ";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r0 = r10.javaName;
        r36 = r0;
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r36 = "(long n) { allocate(n); }\n";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r32 = r32.toString();
        goto L_0x05d8;
    L_0x06d9:
        r32 = "    private native void allocate(@Cast(\"size_t\") long n);\n";
        goto L_0x05ea;
    L_0x06dd:
        r32 = " ";
        goto L_0x0655;
    L_0x06e1:
        r17 = "";
        goto L_0x0661;
    L_0x06e5:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "    public native ";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r17;
        r32 = r0.append(r1);
        r33 = "long size(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r33 = ");\n";
        r33 = r32.append(r33);
        if (r26 != 0) goto L_0x072c;
    L_0x0716:
        r32 = "";
    L_0x0718:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = r15 + 1;
        goto L_0x061d;
    L_0x072c:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r36 = "    public native ";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r17;
        r32 = r0.append(r1);
        r36 = "void resize(";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r36 = "@Cast(\"size_t\") long n);\n";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r32 = r32.toString();
        goto L_0x0718;
    L_0x076c:
        r25 = "";
        r29 = "";
        r15 = 0;
    L_0x0771:
        if (r15 >= r12) goto L_0x07b6;
    L_0x0773:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r18;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r18;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " ";
        r32 = r32.append(r33);
        r33 = r15 + 105;
        r0 = r33;
        r0 = (char) r0;
        r33 = r0;
        r32 = r32.append(r33);
        r25 = r32.toString();
        r29 = ", ";
        r15 = r15 + 1;
        goto L_0x0771;
    L_0x07b6:
        if (r14 == 0) goto L_0x09b8;
    L_0x07b8:
        if (r28 == 0) goto L_0x09b8;
    L_0x07ba:
        if (r12 != 0) goto L_0x0903;
    L_0x07bc:
        r17 = "@MemberGetter ";
    L_0x07be:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "\n    ";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r17;
        r32 = r0.append(r1);
        r33 = "public native ";
        r32 = r32.append(r33);
        r0 = r14.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r14.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " first(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r33 = "); public native ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " first(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r14.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " first);\n    ";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r17;
        r32 = r0.append(r1);
        r33 = "public native ";
        r32 = r32.append(r33);
        r0 = r28;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r28;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " second(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r33 = ");  public native ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " second(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r28;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " second);\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = 1;
    L_0x089c:
        r0 = r14.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0948;
    L_0x08a2:
        r0 = r14.javaNames;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r0 = r32;
        if (r15 >= r0) goto L_0x0948;
    L_0x08af:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "    @MemberSetter @Index public native ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " first(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r14.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r14.javaNames;
        r33 = r0;
        r33 = r33[r15];
        r32 = r32.append(r33);
        r33 = " first);\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = r15 + 1;
        goto L_0x089c;
    L_0x0903:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "@Index";
        r33 = r32.append(r33);
        r32 = 1;
        r0 = r32;
        if (r12 <= r0) goto L_0x0945;
    L_0x0914:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r36 = "(";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r0 = r32;
        r32 = r0.append(r12);
        r36 = ") ";
        r0 = r32;
        r1 = r36;
        r32 = r0.append(r1);
        r32 = r32.toString();
    L_0x0937:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r17 = r32.toString();
        goto L_0x07be;
    L_0x0945:
        r32 = " ";
        goto L_0x0937;
    L_0x0948:
        r15 = 1;
    L_0x0949:
        r0 = r28;
        r0 = r0.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0b0d;
    L_0x0951:
        r0 = r28;
        r0 = r0.javaNames;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r0 = r32;
        if (r15 >= r0) goto L_0x0b0d;
    L_0x0960:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "    @MemberSetter @Index public native ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " second(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r28;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r28;
        r0 = r0.javaNames;
        r33 = r0;
        r33 = r33[r15];
        r32 = r32.append(r33);
        r33 = " second);\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = r15 + 1;
        goto L_0x0949;
    L_0x09b8:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "\n    @Index public native ";
        r32 = r32.append(r33);
        r0 = r31;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r31;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " get(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r33 = ");\n    public native ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " put(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r31;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " value);\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = 1;
    L_0x0a2a:
        r0 = r31;
        r0 = r0.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0a99;
    L_0x0a32:
        r0 = r31;
        r0 = r0.javaNames;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r0 = r32;
        if (r15 >= r0) goto L_0x0a99;
    L_0x0a41:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "    @ValueSetter @Index public native ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " put(";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r25;
        r32 = r0.append(r1);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r31;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r31;
        r0 = r0.javaNames;
        r33 = r0;
        r33 = r33[r15];
        r32 = r32.append(r33);
        r33 = " value);\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = r15 + 1;
        goto L_0x0a2a;
    L_0x0a99:
        r0 = r10.arguments;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 <= r1) goto L_0x0b0d;
    L_0x0aaa:
        r0 = r10.arguments;
        r32 = r0;
        r33 = 1;
        r32 = r32[r33];
        r0 = r32;
        r0 = r0.javaName;
        r32 = r0;
        r32 = r32.length();
        if (r32 <= 0) goto L_0x0b0d;
    L_0x0abe:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "\n    public native @ByVal Iterator begin();\n    public native @ByVal Iterator end();\n    @NoOffset @Name(\"iterator\") public static class Iterator extends Pointer {\n        public Iterator(Pointer p) { super(p); }\n        public Iterator() { }\n\n        public native @Name(\"operator++\") @ByRef Iterator increment();\n        public native @Name(\"operator==\") boolean equals(@ByRef Iterator it);\n        public native @Name(\"operator*().first\") @MemberGetter ";
        r32 = r32.append(r33);
        r0 = r18;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r18;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " first();\n        public native @Name(\"operator*().second\") @MemberGetter ";
        r32 = r32.append(r33);
        r0 = r31;
        r0 = r0.annotations;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r31;
        r0 = r0.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " second();\n    }\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
    L_0x0b0d:
        if (r12 == 0) goto L_0x0b20;
    L_0x0b0f:
        r0 = r10.arguments;
        r32 = r0;
        r0 = r32;
        r0 = r0.length;
        r32 = r0;
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x0d9f;
    L_0x0b20:
        if (r14 == 0) goto L_0x0d9f;
    L_0x0b22:
        if (r28 == 0) goto L_0x0d9f;
    L_0x0b24:
        r0 = r14.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0cb0;
    L_0x0b2a:
        r13 = r14.javaNames;
    L_0x0b2c:
        r0 = r28;
        r0 = r0.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0cc0;
    L_0x0b34:
        r0 = r28;
        r0 = r0.javaNames;
        r27 = r0;
    L_0x0b3a:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r33 = r0.append(r4);
        if (r12 <= 0) goto L_0x0cd4;
    L_0x0b47:
        r32 = "[]";
    L_0x0b49:
        r0 = r33;
        r1 = r32;
        r32 = r0.append(r1);
        r6 = r32.toString();
        r24 = 0;
    L_0x0b57:
        r0 = r13.length;
        r32 = r0;
        r0 = r24;
        r1 = r32;
        if (r0 < r1) goto L_0x0b6b;
    L_0x0b60:
        r0 = r27;
        r0 = r0.length;
        r32 = r0;
        r0 = r24;
        r1 = r32;
        if (r0 >= r1) goto L_0x0fb6;
    L_0x0b6b:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "\n    public ";
        r32 = r32.append(r33);
        r0 = r10.javaName;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = " put(";
        r32 = r32.append(r33);
        r0 = r13.length;
        r33 = r0;
        r33 = r33 + -1;
        r0 = r24;
        r1 = r33;
        r33 = java.lang.Math.min(r0, r1);
        r33 = r13[r33];
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r6);
        r33 = " firstValue, ";
        r32 = r32.append(r33);
        r0 = r27;
        r0 = r0.length;
        r33 = r0;
        r33 = r33 + -1;
        r0 = r24;
        r1 = r33;
        r33 = java.lang.Math.min(r0, r1);
        r33 = r27[r33];
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r6);
        r33 = " secondValue) {\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r16 = "        ";
        r19 = "";
        r3 = "";
        r29 = "";
        r15 = 0;
    L_0x0bdd:
        if (r15 >= r12) goto L_0x0cd8;
    L_0x0bdf:
        r32 = r15 + 105;
        r0 = r32;
        r7 = (char) r0;
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r16;
        r32 = r0.append(r1);
        r33 = "for (int ";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r7);
        r33 = " = 0; ";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r7);
        r33 = " < firstValue";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r33 = ".length && ";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r7);
        r33 = " < secondValue";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r33 = ".length; ";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r7);
        r33 = "++) {\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r1 = r16;
        r32 = r0.append(r1);
        r33 = "    ";
        r32 = r32.append(r33);
        r16 = r32.toString();
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r33 = "[";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r7);
        r33 = "]";
        r32 = r32.append(r33);
        r19 = r32.toString();
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r32;
        r32 = r0.append(r3);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r0 = r32;
        r32 = r0.append(r7);
        r3 = r32.toString();
        r29 = ", ";
        r15 = r15 + 1;
        goto L_0x0bdd;
    L_0x0cb0:
        r32 = 1;
        r0 = r32;
        r13 = new java.lang.String[r0];
        r32 = 0;
        r0 = r14.javaName;
        r33 = r0;
        r13[r32] = r33;
        goto L_0x0b2c;
    L_0x0cc0:
        r32 = 1;
        r0 = r32;
        r0 = new java.lang.String[r0];
        r27 = r0;
        r32 = 0;
        r0 = r28;
        r0 = r0.javaName;
        r33 = r0;
        r27[r32] = r33;
        goto L_0x0b3a;
    L_0x0cd4:
        r32 = "";
        goto L_0x0b49;
    L_0x0cd8:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r16;
        r32 = r0.append(r1);
        r33 = "first(";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r3);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r33 = "firstValue";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r33 = ");\n";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r16;
        r32 = r0.append(r1);
        r33 = "second(";
        r32 = r32.append(r33);
        r0 = r32;
        r32 = r0.append(r3);
        r0 = r32;
        r1 = r29;
        r32 = r0.append(r1);
        r33 = "secondValue";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r19;
        r32 = r0.append(r1);
        r33 = ");\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = 0;
    L_0x0d4e:
        if (r15 >= r12) goto L_0x0d80;
    L_0x0d50:
        r32 = 4;
        r0 = r16;
        r1 = r32;
        r16 = r0.substring(r1);
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r16;
        r32 = r0.append(r1);
        r33 = "}\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r15 = r15 + 1;
        goto L_0x0d4e;
    L_0x0d80:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "        return this;\n    }\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r24 = r24 + 1;
        goto L_0x0b57;
    L_0x0d9f:
        if (r26 == 0) goto L_0x0fb6;
    L_0x0da1:
        if (r14 != 0) goto L_0x0fb6;
    L_0x0da3:
        if (r28 != 0) goto L_0x0fb6;
    L_0x0da5:
        r0 = r31;
        r0 = r0.javaNames;
        r32 = r0;
        if (r32 == 0) goto L_0x0f0b;
    L_0x0dad:
        r0 = r31;
        r0 = r0.javaNames;
        r32 = r0;
    L_0x0db3:
        r0 = r32;
        r0 = r0.length;
        r36 = r0;
        r33 = 0;
    L_0x0dba:
        r0 = r33;
        r1 = r36;
        if (r0 >= r1) goto L_0x0fb6;
    L_0x0dc0:
        r23 = r32[r33];
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r11.text;
        r38 = r0;
        r37 = r37.append(r38);
        r38 = "\n    public ";
        r37 = r37.append(r38);
        r0 = r10.javaName;
        r38 = r0;
        r37 = r37.append(r38);
        r38 = " put(";
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r23;
        r37 = r0.append(r1);
        r0 = r37;
        r37 = r0.append(r4);
        r38 = " ... array) {\n";
        r37 = r37.append(r38);
        r37 = r37.toString();
        r0 = r37;
        r11.text = r0;
        r16 = "        ";
        r19 = "";
        r3 = "";
        r29 = "";
        r15 = 0;
    L_0x0e08:
        if (r15 >= r12) goto L_0x0f1f;
    L_0x0e0a:
        r37 = r15 + 105;
        r0 = r37;
        r7 = (char) r0;
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r11.text;
        r38 = r0;
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r16;
        r37 = r0.append(r1);
        r38 = "if (size(";
        r37 = r37.append(r38);
        r0 = r37;
        r37 = r0.append(r3);
        r38 = ") != array";
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r19;
        r37 = r0.append(r1);
        r38 = ".length) { resize(";
        r37 = r37.append(r38);
        r0 = r37;
        r37 = r0.append(r3);
        r0 = r37;
        r1 = r29;
        r37 = r0.append(r1);
        r38 = "array";
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r19;
        r37 = r0.append(r1);
        r38 = ".length); }\n";
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r16;
        r37 = r0.append(r1);
        r38 = "for (int ";
        r37 = r37.append(r38);
        r0 = r37;
        r37 = r0.append(r7);
        r38 = " = 0; ";
        r37 = r37.append(r38);
        r0 = r37;
        r37 = r0.append(r7);
        r38 = " < array";
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r19;
        r37 = r0.append(r1);
        r38 = ".length; ";
        r37 = r37.append(r38);
        r0 = r37;
        r37 = r0.append(r7);
        r38 = "++) {\n";
        r37 = r37.append(r38);
        r37 = r37.toString();
        r0 = r37;
        r11.text = r0;
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r37;
        r1 = r16;
        r37 = r0.append(r1);
        r38 = "    ";
        r37 = r37.append(r38);
        r16 = r37.toString();
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r37;
        r1 = r19;
        r37 = r0.append(r1);
        r38 = "[";
        r37 = r37.append(r38);
        r0 = r37;
        r37 = r0.append(r7);
        r38 = "]";
        r37 = r37.append(r38);
        r19 = r37.toString();
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r37;
        r37 = r0.append(r3);
        r0 = r37;
        r1 = r29;
        r37 = r0.append(r1);
        r0 = r37;
        r37 = r0.append(r7);
        r3 = r37.toString();
        r29 = ", ";
        r15 = r15 + 1;
        goto L_0x0e08;
    L_0x0f0b:
        r32 = 1;
        r0 = r32;
        r0 = new java.lang.String[r0];
        r32 = r0;
        r33 = 0;
        r0 = r31;
        r0 = r0.javaName;
        r36 = r0;
        r32[r33] = r36;
        goto L_0x0db3;
    L_0x0f1f:
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r11.text;
        r38 = r0;
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r16;
        r37 = r0.append(r1);
        r38 = "put(";
        r37 = r37.append(r38);
        r0 = r37;
        r37 = r0.append(r3);
        r0 = r37;
        r1 = r29;
        r37 = r0.append(r1);
        r38 = "array";
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r19;
        r37 = r0.append(r1);
        r38 = ");\n";
        r37 = r37.append(r38);
        r37 = r37.toString();
        r0 = r37;
        r11.text = r0;
        r15 = 0;
    L_0x0f65:
        if (r15 >= r12) goto L_0x0f97;
    L_0x0f67:
        r37 = 4;
        r0 = r16;
        r1 = r37;
        r16 = r0.substring(r1);
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r11.text;
        r38 = r0;
        r37 = r37.append(r38);
        r0 = r37;
        r1 = r16;
        r37 = r0.append(r1);
        r38 = "}\n";
        r37 = r37.append(r38);
        r37 = r37.toString();
        r0 = r37;
        r11.text = r0;
        r15 = r15 + 1;
        goto L_0x0f65;
    L_0x0f97:
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r0 = r11.text;
        r38 = r0;
        r37 = r37.append(r38);
        r38 = "        return this;\n    }\n";
        r37 = r37.append(r38);
        r37 = r37.toString();
        r0 = r37;
        r11.text = r0;
        r33 = r33 + 1;
        goto L_0x0dba;
    L_0x0fb6:
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r0 = r11.text;
        r33 = r0;
        r32 = r32.append(r33);
        r33 = "}\n";
        r32 = r32.append(r33);
        r32 = r32.toString();
        r0 = r32;
        r11.text = r0;
        r0 = r41;
        r0.add(r11);
        goto L_0x0051;
    L_0x0fd8:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Parser.containers(org.bytedeco.javacpp.tools.Context, org.bytedeco.javacpp.tools.DeclarationList):void");
    }

    TemplateMap template(Context context) throws ParserException {
        if (!this.tokens.get().match(Token.TEMPLATE)) {
            return null;
        }
        TemplateMap map = new TemplateMap(context.templateMap);
        this.tokens.next().expect(Character.valueOf('<'));
        Token token = this.tokens.next();
        while (true) {
            if (token.match(Token.EOF)) {
                return map;
            }
            if (token.match(Integer.valueOf(5))) {
                Token t = this.tokens.next();
                if (t.match("...")) {
                    map.variadic = true;
                    t = this.tokens.next();
                }
                if (t.match(Integer.valueOf(5))) {
                    String key = t.value;
                    map.put(key, map.get(key));
                    token = this.tokens.next();
                }
            }
            if (!token.match(Character.valueOf(','), Character.valueOf('>'))) {
                int count = 0;
                token = this.tokens.get();
                while (true) {
                    if (token.match(Token.EOF)) {
                        break;
                    }
                    if (count == 0) {
                        if (token.match(Character.valueOf(','), Character.valueOf('>'))) {
                            break;
                        }
                    }
                    if (token.match(Character.valueOf('<'), Character.valueOf('('))) {
                        count++;
                    } else {
                        if (token.match(Character.valueOf('>'), Character.valueOf(')'))) {
                            count--;
                        }
                    }
                    token = this.tokens.next();
                }
            }
            if (token.expect(Character.valueOf(','), Character.valueOf('>')).match(Character.valueOf('>'))) {
                if (!this.tokens.next().match(Token.TEMPLATE)) {
                    return map;
                }
                this.tokens.next().expect(Character.valueOf('<'));
            }
            token = this.tokens.next();
        }
    }

    Type[] templateArguments(Context context) throws ParserException {
        if (!this.tokens.get().match(Character.valueOf('<'))) {
            return null;
        }
        List<Type> arguments = new ArrayList();
        Token token = this.tokens.next();
        while (true) {
            if (token.match(Token.EOF)) {
                break;
            }
            Type type = type(context);
            arguments.add(type);
            token = this.tokens.get();
            if (!token.match(Character.valueOf(','), Character.valueOf('>'))) {
                int count = 0;
                token = this.tokens.get();
                while (true) {
                    if (token.match(Token.EOF)) {
                        break;
                    }
                    if (count == 0) {
                        if (token.match(Character.valueOf(','), Character.valueOf('>'))) {
                            break;
                        }
                    }
                    if (token.match(Character.valueOf('<'), Character.valueOf('('))) {
                        count++;
                    } else {
                        if (token.match(Character.valueOf('>'), Character.valueOf(')'))) {
                            count--;
                        }
                    }
                    type.cppName += token;
                    if (token.match(Token.CONST)) {
                        type.cppName += " ";
                    }
                    token = this.tokens.next();
                }
                if (type.cppName.endsWith("*")) {
                    type.javaName = "PointerPointer";
                    type.annotations += "@Cast(\"" + type.cppName + "*\") ";
                }
            }
            if (token.expect(Character.valueOf(','), Character.valueOf('>')).match(Character.valueOf('>'))) {
                break;
            }
            token = this.tokens.next();
        }
        return (Type[]) arguments.toArray(new Type[arguments.size()]);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    org.bytedeco.javacpp.tools.Type type(org.bytedeco.javacpp.tools.Context r28) throws org.bytedeco.javacpp.tools.ParserException {
        /* JADX: method processing error */
/*
Error: java.lang.IndexOutOfBoundsException: bitIndex < 0: -1
	at java.util.BitSet.get(BitSet.java:623)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.usedArgAssign(CodeShrinker.java:138)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.access$300(CodeShrinker.java:43)
	at jadx.core.dex.visitors.CodeShrinker.canMoveBetweenBlocks(CodeShrinker.java:282)
	at jadx.core.dex.visitors.CodeShrinker.shrinkBlock(CodeShrinker.java:230)
	at jadx.core.dex.visitors.CodeShrinker.shrinkMethod(CodeShrinker.java:38)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkArrayForEach(LoopRegionVisitor.java:196)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkForIndexedLoop(LoopRegionVisitor.java:119)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:65)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r27 = this;
        r20 = new org.bytedeco.javacpp.tools.Type;
        r20.<init>();
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r19 = r22.get();
    L_0x0014:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.EOF;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 != 0) goto L_0x0159;
    L_0x002c:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = "::";
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x0079;
    L_0x0044:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r0 = r22;
        r1 = r19;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x0065:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
    L_0x006e:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r19 = r22.get();
        goto L_0x0014;
    L_0x0079:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.DECLTYPE;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x01dc;
    L_0x0091:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = r19.toString();
        r22 = r22.append(r23);
        r0 = r27;
        r0 = r0.tokens;
        r23 = r0;
        r23 = r23.next();
        r24 = 1;
        r0 = r24;
        r0 = new java.lang.Object[r0];
        r24 = r0;
        r25 = 0;
        r26 = 40;
        r26 = java.lang.Character.valueOf(r26);
        r24[r25] = r26;
        r23 = r23.expect(r24);
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r6 = 0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r19 = r22.next();
    L_0x00e1:
        r22 = 2;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = 41;
        r24 = java.lang.Character.valueOf(r24);
        r22[r23] = r24;
        r23 = 1;
        r24 = org.bytedeco.javacpp.tools.Token.EOF;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 != 0) goto L_0x012f;
    L_0x0103:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r0 = r22;
        r1 = r19;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r19 = r22.next();
        goto L_0x00e1;
    L_0x012f:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r0 = r22;
        r1 = r19;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
    L_0x0159:
        r22 = r4.size();
        if (r22 <= 0) goto L_0x0177;
    L_0x015f:
        r22 = r4.size();
        r0 = r22;
        r0 = new org.bytedeco.javacpp.tools.Attribute[r0];
        r22 = r0;
        r0 = r22;
        r22 = r4.toArray(r0);
        r22 = (org.bytedeco.javacpp.tools.Attribute[]) r22;
        r0 = r22;
        r1 = r20;
        r1.attributes = r0;
    L_0x0177:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r22 = r22.trim();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22 = r22.get();
        r23 = 1;
        r0 = r23;
        r0 = new java.lang.Object[r0];
        r23 = r0;
        r24 = 0;
        r25 = "...";
        r23[r24] = r25;
        r22 = r22.match(r23);
        if (r22 == 0) goto L_0x0754;
    L_0x01a5:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22 = r22.get();
        r23 = 1;
        r0 = r23;
        r0 = new java.lang.Object[r0];
        r23 = r0;
        r24 = 0;
        r25 = 5;
        r25 = java.lang.Integer.valueOf(r25);
        r23[r24] = r25;
        r22 = r22.match(r23);
        if (r22 == 0) goto L_0x01d9;
    L_0x01d0:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
    L_0x01d9:
        r20 = 0;
    L_0x01db:
        return r20;
    L_0x01dc:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = 60;
        r24 = java.lang.Character.valueOf(r24);
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x0360;
    L_0x01f8:
        r22 = r27.templateArguments(r28);
        r0 = r22;
        r1 = r20;
        r1.arguments = r0;
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = "<";
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r14 = "";
        r0 = r20;
        r0 = r0.arguments;
        r23 = r0;
        r0 = r23;
        r0 = r0.length;
        r24 = r0;
        r22 = 0;
    L_0x0230:
        r0 = r22;
        r1 = r24;
        if (r0 >= r1) goto L_0x0326;
    L_0x0236:
        r15 = r23[r22];
        if (r15 != 0) goto L_0x023d;
    L_0x023a:
        r22 = r22 + 1;
        goto L_0x0230;
    L_0x023d:
        r25 = new java.lang.StringBuilder;
        r25.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r26 = r0;
        r25 = r25.append(r26);
        r0 = r25;
        r25 = r0.append(r14);
        r25 = r25.toString();
        r0 = r25;
        r1 = r20;
        r1.cppName = r0;
        r0 = r27;
        r0 = r0.infoMap;
        r25 = r0;
        r0 = r15.cppName;
        r26 = r0;
        r9 = r25.getFirst(r26);
        if (r9 == 0) goto L_0x02e5;
    L_0x026c:
        r0 = r9.cppTypes;
        r25 = r0;
        if (r25 == 0) goto L_0x02e5;
    L_0x0272:
        r0 = r9.cppTypes;
        r25 = r0;
        r26 = 0;
        r13 = r25[r26];
    L_0x027a:
        r0 = r15.constValue;
        r25 = r0;
        if (r25 == 0) goto L_0x029f;
    L_0x0280:
        r25 = "const ";
        r0 = r25;
        r25 = r13.startsWith(r0);
        if (r25 != 0) goto L_0x029f;
    L_0x028a:
        r25 = new java.lang.StringBuilder;
        r25.<init>();
        r26 = "const ";
        r25 = r25.append(r26);
        r0 = r25;
        r25 = r0.append(r13);
        r13 = r25.toString();
    L_0x029f:
        r0 = r15.constPointer;
        r25 = r0;
        if (r25 == 0) goto L_0x02c4;
    L_0x02a5:
        r25 = " const";
        r0 = r25;
        r25 = r13.endsWith(r0);
        if (r25 != 0) goto L_0x02c4;
    L_0x02af:
        r25 = new java.lang.StringBuilder;
        r25.<init>();
        r0 = r25;
        r25 = r0.append(r13);
        r26 = " const";
        r25 = r25.append(r26);
        r13 = r25.toString();
    L_0x02c4:
        r8 = 0;
    L_0x02c5:
        r0 = r15.indirections;
        r25 = r0;
        r0 = r25;
        if (r8 >= r0) goto L_0x02e8;
    L_0x02cd:
        r25 = new java.lang.StringBuilder;
        r25.<init>();
        r0 = r25;
        r25 = r0.append(r13);
        r26 = "*";
        r25 = r25.append(r26);
        r13 = r25.toString();
        r8 = r8 + 1;
        goto L_0x02c5;
    L_0x02e5:
        r13 = r15.cppName;
        goto L_0x027a;
    L_0x02e8:
        r0 = r15.reference;
        r25 = r0;
        if (r25 == 0) goto L_0x0303;
    L_0x02ee:
        r25 = new java.lang.StringBuilder;
        r25.<init>();
        r0 = r25;
        r25 = r0.append(r13);
        r26 = "&";
        r25 = r25.append(r26);
        r13 = r25.toString();
    L_0x0303:
        r25 = new java.lang.StringBuilder;
        r25.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r26 = r0;
        r25 = r25.append(r26);
        r0 = r25;
        r25 = r0.append(r13);
        r25 = r25.toString();
        r0 = r25;
        r1 = r20;
        r1.cppName = r0;
        r14 = ",";
        goto L_0x023a;
    L_0x0326:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r23 = r22.append(r23);
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r24 = ">";
        r0 = r22;
        r1 = r24;
        r22 = r0.endsWith(r1);
        if (r22 == 0) goto L_0x035d;
    L_0x0347:
        r22 = " >";
    L_0x0349:
        r0 = r23;
        r1 = r22;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        goto L_0x0065;
    L_0x035d:
        r22 = ">";
        goto L_0x0349;
    L_0x0360:
        r22 = 2;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.CONST;
        r22[r23] = r24;
        r23 = 1;
        r24 = org.bytedeco.javacpp.tools.Token.CONSTEXPR;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x039e;
    L_0x037e:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r22 = r22.length();
        if (r22 != 0) goto L_0x0394;
    L_0x038a:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.constValue = r0;
        goto L_0x0065;
    L_0x0394:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.constPointer = r0;
        goto L_0x0065;
    L_0x039e:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = 42;
        r24 = java.lang.Character.valueOf(r24);
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x03d3;
    L_0x03ba:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        r22 = r22 + 1;
        r0 = r22;
        r1 = r20;
        r1.indirections = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
        goto L_0x0159;
    L_0x03d3:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = 38;
        r24 = java.lang.Character.valueOf(r24);
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x0402;
    L_0x03ef:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.reference = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
        goto L_0x0159;
    L_0x0402:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = "&&";
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 != 0) goto L_0x0065;
    L_0x041a:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = 126; // 0x7e float:1.77E-43 double:6.23E-322;
        r24 = java.lang.Character.valueOf(r24);
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x045f;
    L_0x0436:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = "~";
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.destructor = r0;
        goto L_0x0065;
    L_0x045f:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.STATIC;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x0481;
    L_0x0477:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.staticMember = r0;
        goto L_0x0065;
    L_0x0481:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.OPERATOR;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x04d9;
    L_0x0499:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r22 = r22.length();
        if (r22 != 0) goto L_0x04b8;
    L_0x04a5:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.operator = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
        goto L_0x006e;
    L_0x04b8:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "::";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x0159;
    L_0x04c6:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.operator = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
        goto L_0x0159;
    L_0x04d9:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.FRIEND;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x04fb;
    L_0x04f1:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.friend = r0;
        goto L_0x0065;
    L_0x04fb:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.VIRTUAL;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x051d;
    L_0x0513:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.virtual = r0;
        goto L_0x0065;
    L_0x051d:
        r22 = 18;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.AUTO;
        r22[r23] = r24;
        r23 = 1;
        r24 = org.bytedeco.javacpp.tools.Token.ENUM;
        r22[r23] = r24;
        r23 = 2;
        r24 = org.bytedeco.javacpp.tools.Token.EXPLICIT;
        r22[r23] = r24;
        r23 = 3;
        r24 = org.bytedeco.javacpp.tools.Token.EXTERN;
        r22[r23] = r24;
        r23 = 4;
        r24 = org.bytedeco.javacpp.tools.Token.INLINE;
        r22[r23] = r24;
        r23 = 5;
        r24 = org.bytedeco.javacpp.tools.Token.CLASS;
        r22[r23] = r24;
        r23 = 6;
        r24 = org.bytedeco.javacpp.tools.Token.INTERFACE;
        r22[r23] = r24;
        r23 = 7;
        r24 = org.bytedeco.javacpp.tools.Token.__INTERFACE;
        r22[r23] = r24;
        r23 = 8;
        r24 = org.bytedeco.javacpp.tools.Token.MUTABLE;
        r22[r23] = r24;
        r23 = 9;
        r24 = org.bytedeco.javacpp.tools.Token.NAMESPACE;
        r22[r23] = r24;
        r23 = 10;
        r24 = org.bytedeco.javacpp.tools.Token.STRUCT;
        r22[r23] = r24;
        r23 = 11;
        r24 = org.bytedeco.javacpp.tools.Token.UNION;
        r22[r23] = r24;
        r23 = 12;
        r24 = org.bytedeco.javacpp.tools.Token.TYPEDEF;
        r22[r23] = r24;
        r23 = 13;
        r24 = org.bytedeco.javacpp.tools.Token.TYPENAME;
        r22[r23] = r24;
        r23 = 14;
        r24 = org.bytedeco.javacpp.tools.Token.USING;
        r22[r23] = r24;
        r23 = 15;
        r24 = org.bytedeco.javacpp.tools.Token.REGISTER;
        r22[r23] = r24;
        r23 = 16;
        r24 = org.bytedeco.javacpp.tools.Token.THREAD_LOCAL;
        r22[r23] = r24;
        r23 = 17;
        r24 = org.bytedeco.javacpp.tools.Token.VOLATILE;
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x05a7;
    L_0x059b:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r19 = r22.next();
        goto L_0x006e;
    L_0x05a7:
        r0 = r27;
        r0 = r0.infoMap;
        r22 = r0;
        r23 = "basic/types";
        r22 = r22.getFirst(r23);
        r0 = r22;
        r0 = r0.cppTypes;
        r22 = r0;
        r22 = (java.lang.Object[]) r22;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x060c;
    L_0x05c5:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r22 = r22.length();
        if (r22 == 0) goto L_0x05d9;
    L_0x05d1:
        r0 = r20;
        r0 = r0.simple;
        r22 = r0;
        if (r22 == 0) goto L_0x060c;
    L_0x05d9:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r0 = r19;
        r0 = r0.value;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = " ";
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.simple = r0;
        goto L_0x0065;
    L_0x060c:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = 5;
        r24 = java.lang.Integer.valueOf(r24);
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x0725;
    L_0x0628:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r0 = r22;
        r5 = r0.index;
        r3 = r27.attribute();
        if (r3 == 0) goto L_0x0664;
    L_0x0638:
        r0 = r3.annotation;
        r22 = r0;
        if (r22 == 0) goto L_0x0664;
    L_0x063e:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.annotations;
        r23 = r0;
        r22 = r22.append(r23);
        r0 = r3.javaName;
        r23 = r0;
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.annotations = r0;
        r4.add(r3);
        goto L_0x006e;
    L_0x0664:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r0 = r22;
        r0.index = r5;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r22 = r22.length();
        if (r22 == 0) goto L_0x0696;
    L_0x067a:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "::";
        r22 = r22.endsWith(r23);
        if (r22 != 0) goto L_0x0696;
    L_0x0688:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "~";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x06bb;
    L_0x0696:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r0 = r19;
        r0 = r0.value;
        r23 = r0;
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        goto L_0x0065;
    L_0x06bb:
        r0 = r27;
        r0 = r0.infoMap;
        r22 = r0;
        r0 = r27;
        r0 = r0.tokens;
        r23 = r0;
        r24 = 1;
        r23 = r23.get(r24);
        r0 = r23;
        r0 = r0.value;
        r23 = r0;
        r9 = r22.getFirst(r23);
        if (r9 == 0) goto L_0x06df;
    L_0x06d9:
        r0 = r9.annotations;
        r22 = r0;
        if (r22 != 0) goto L_0x0159;
    L_0x06df:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r23 = 1;
        r22 = r22.get(r23);
        r23 = 5;
        r0 = r23;
        r0 = new java.lang.Object[r0];
        r23 = r0;
        r24 = 0;
        r25 = 42;
        r25 = java.lang.Character.valueOf(r25);
        r23[r24] = r25;
        r24 = 1;
        r25 = 38;
        r25 = java.lang.Character.valueOf(r25);
        r23[r24] = r25;
        r24 = 2;
        r25 = 5;
        r25 = java.lang.Integer.valueOf(r25);
        r23[r24] = r25;
        r24 = 3;
        r25 = org.bytedeco.javacpp.tools.Token.CONST;
        r23[r24] = r25;
        r24 = 4;
        r25 = org.bytedeco.javacpp.tools.Token.CONSTEXPR;
        r23[r24] = r25;
        r22 = r22.match(r23);
        if (r22 != 0) goto L_0x0065;
    L_0x0723:
        goto L_0x0159;
    L_0x0725:
        r22 = 1;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r24 = java.lang.Character.valueOf(r24);
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 == 0) goto L_0x0159;
    L_0x0741:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.anonymous = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22.next();
        goto L_0x0159;
    L_0x0754:
        r0 = r20;
        r0 = r0.operator;
        r22 = r0;
        if (r22 == 0) goto L_0x07b4;
    L_0x075c:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r19 = r22.get();
    L_0x0766:
        r22 = 2;
        r0 = r22;
        r0 = new java.lang.Object[r0];
        r22 = r0;
        r23 = 0;
        r24 = org.bytedeco.javacpp.tools.Token.EOF;
        r22[r23] = r24;
        r23 = 1;
        r24 = 40;
        r24 = java.lang.Character.valueOf(r24);
        r22[r23] = r24;
        r0 = r19;
        r1 = r22;
        r22 = r0.match(r1);
        if (r22 != 0) goto L_0x07b4;
    L_0x0788:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r22 = r22.append(r23);
        r0 = r22;
        r1 = r19;
        r22 = r0.append(r1);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r19 = r22.next();
        goto L_0x0766;
    L_0x07b4:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "*";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x07ee;
    L_0x07c2:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        r22 = r22 + 1;
        r0 = r22;
        r1 = r20;
        r1.indirections = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -1;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x07ee:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "&";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x0822;
    L_0x07fc:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.reference = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -1;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x0822:
        r0 = r28;
        r0 = r0.templateMap;
        r22 = r0;
        if (r22 == 0) goto L_0x08c7;
    L_0x082a:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "::";
        r21 = r22.split(r23);
        r14 = "";
        r22 = "";
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r0 = r21;
        r0 = r0.length;
        r23 = r0;
        r22 = 0;
    L_0x084c:
        r0 = r22;
        r1 = r23;
        if (r0 >= r1) goto L_0x08a9;
    L_0x0852:
        r15 = r21[r22];
        r0 = r28;
        r0 = r0.templateMap;
        r24 = r0;
        r0 = r24;
        r16 = r0.get(r15);
        r24 = new java.lang.StringBuilder;
        r24.<init>();
        r0 = r20;
        r0 = r0.cppName;
        r25 = r0;
        r24 = r24.append(r25);
        r0 = r24;
        r24 = r0.append(r14);
        if (r16 == 0) goto L_0x087b;
    L_0x0877:
        r0 = r16;
        r15 = r0.cppName;
    L_0x087b:
        r0 = r24;
        r24 = r0.append(r15);
        r24 = r24.toString();
        r0 = r24;
        r1 = r20;
        r1.cppName = r0;
        if (r16 == 0) goto L_0x08a4;
    L_0x088d:
        r0 = r16;
        r0 = r0.arguments;
        r24 = r0;
        if (r24 == 0) goto L_0x08a4;
    L_0x0895:
        r0 = r16;
        r0 = r0.arguments;
        r24 = r0;
        r24 = java.util.Arrays.asList(r24);
        r0 = r24;
        r2.addAll(r0);
    L_0x08a4:
        r14 = "::";
        r22 = r22 + 1;
        goto L_0x084c;
    L_0x08a9:
        r22 = r2.size();
        if (r22 <= 0) goto L_0x08c7;
    L_0x08af:
        r22 = r2.size();
        r0 = r22;
        r0 = new org.bytedeco.javacpp.tools.Type[r0];
        r22 = r0;
        r0 = r22;
        r22 = r2.toArray(r0);
        r22 = (org.bytedeco.javacpp.tools.Type[]) r22;
        r0 = r22;
        r1 = r20;
        r1.arguments = r0;
    L_0x08c7:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "const ";
        r22 = r22.startsWith(r23);
        if (r22 == 0) goto L_0x08ef;
    L_0x08d5:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.constValue = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 6;
        r22 = r22.substring(r23);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x08ef:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "*";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x0939;
    L_0x08fd:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        r22 = r22 + 1;
        r0 = r22;
        r1 = r20;
        r1.indirections = r0;
        r0 = r20;
        r0 = r0.reference;
        r22 = r0;
        if (r22 == 0) goto L_0x091b;
    L_0x0913:
        r22 = 0;
        r0 = r22;
        r1 = r20;
        r1.constValue = r0;
    L_0x091b:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -1;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x0939:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "&";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x096d;
    L_0x0947:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.reference = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -1;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x096d:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = " const";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x09a1;
    L_0x097b:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.constPointer = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -6;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x09a1:
        r9 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r0 = r28;
        r1 = r22;
        r23 = r0.qualify(r1);
        r0 = r23;
        r0 = r0.length;
        r24 = r0;
        r22 = 0;
    L_0x09b7:
        r0 = r22;
        r1 = r24;
        if (r0 >= r1) goto L_0x09d5;
    L_0x09bd:
        r10 = r23[r22];
        r0 = r27;
        r0 = r0.infoMap;
        r25 = r0;
        r26 = 0;
        r0 = r25;
        r1 = r26;
        r9 = r0.getFirst(r10, r1);
        if (r9 == 0) goto L_0x0c3d;
    L_0x09d1:
        r0 = r20;
        r0.cppName = r10;
    L_0x09d5:
        if (r9 == 0) goto L_0x09f6;
    L_0x09d7:
        r0 = r9.cppTypes;
        r22 = r0;
        if (r22 == 0) goto L_0x09f6;
    L_0x09dd:
        r0 = r9.cppTypes;
        r22 = r0;
        r0 = r22;
        r0 = r0.length;
        r22 = r0;
        if (r22 <= 0) goto L_0x09f6;
    L_0x09e8:
        r0 = r9.cppTypes;
        r22 = r0;
        r23 = 0;
        r22 = r22[r23];
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x09f6:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "const ";
        r22 = r22.startsWith(r23);
        if (r22 == 0) goto L_0x0a1e;
    L_0x0a04:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.constValue = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 6;
        r22 = r22.substring(r23);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x0a1e:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "*";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x0a68;
    L_0x0a2c:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        r22 = r22 + 1;
        r0 = r22;
        r1 = r20;
        r1.indirections = r0;
        r0 = r20;
        r0 = r0.reference;
        r22 = r0;
        if (r22 == 0) goto L_0x0a4a;
    L_0x0a42:
        r22 = 0;
        r0 = r22;
        r1 = r20;
        r1.constValue = r0;
    L_0x0a4a:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -1;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x0a68:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "&";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x0a9c;
    L_0x0a76:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.reference = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -1;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x0a9c:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = " const";
        r22 = r22.endsWith(r23);
        if (r22 == 0) goto L_0x0ad0;
    L_0x0aaa:
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.constPointer = r0;
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 0;
        r0 = r20;
        r0 = r0.cppName;
        r24 = r0;
        r24 = r24.length();
        r24 = r24 + -6;
        r22 = r22.substring(r23, r24);
        r0 = r22;
        r1 = r20;
        r1.cppName = r0;
    L_0x0ad0:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = "::";
        r11 = r22.lastIndexOf(r23);
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = 60;
        r17 = r22.lastIndexOf(r23);
        if (r11 < 0) goto L_0x0c53;
    L_0x0aea:
        if (r17 >= 0) goto L_0x0c53;
    L_0x0aec:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r23 = r11 + 2;
        r22 = r22.substring(r23);
    L_0x0af8:
        r0 = r22;
        r1 = r20;
        r1.javaName = r0;
        if (r9 == 0) goto L_0x0b41;
    L_0x0b00:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        if (r22 != 0) goto L_0x0c5b;
    L_0x0b08:
        r0 = r20;
        r0 = r0.reference;
        r22 = r0;
        if (r22 != 0) goto L_0x0c5b;
    L_0x0b10:
        r0 = r9.valueTypes;
        r22 = r0;
        if (r22 == 0) goto L_0x0c5b;
    L_0x0b16:
        r0 = r9.valueTypes;
        r22 = r0;
        r0 = r22;
        r0 = r0.length;
        r22 = r0;
        if (r22 <= 0) goto L_0x0c5b;
    L_0x0b21:
        r0 = r9.valueTypes;
        r22 = r0;
        r23 = 0;
        r22 = r22[r23];
        r0 = r22;
        r1 = r20;
        r1.javaName = r0;
        r0 = r9.valueTypes;
        r22 = r0;
        r0 = r22;
        r1 = r20;
        r1.javaNames = r0;
        r22 = 1;
        r0 = r22;
        r1 = r20;
        r1.value = r0;
    L_0x0b41:
        r0 = r20;
        r0 = r0.operator;
        r22 = r0;
        if (r22 == 0) goto L_0x0bfa;
    L_0x0b49:
        r0 = r20;
        r0 = r0.constValue;
        r22 = r0;
        if (r22 == 0) goto L_0x0b70;
    L_0x0b51:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.annotations;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = "@Const ";
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.annotations = r0;
    L_0x0b70:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        if (r22 != 0) goto L_0x0c86;
    L_0x0b78:
        r0 = r20;
        r0 = r0.reference;
        r22 = r0;
        if (r22 != 0) goto L_0x0c86;
    L_0x0b80:
        r0 = r20;
        r0 = r0.value;
        r22 = r0;
        if (r22 != 0) goto L_0x0c86;
    L_0x0b88:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.annotations;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = "@ByVal ";
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.annotations = r0;
    L_0x0ba7:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.annotations;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = "@Name(\"operator ";
        r23 = r22.append(r23);
        r0 = r20;
        r0 = r0.constValue;
        r22 = r0;
        if (r22 == 0) goto L_0x0cbf;
    L_0x0bc4:
        r22 = "const ";
    L_0x0bc6:
        r0 = r23;
        r1 = r22;
        r22 = r0.append(r1);
        r0 = r20;
        r0 = r0.cppName;
        r23 = r0;
        r23 = r22.append(r23);
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        if (r22 <= 0) goto L_0x0cc3;
    L_0x0be0:
        r22 = "*";
    L_0x0be2:
        r0 = r23;
        r1 = r22;
        r22 = r0.append(r1);
        r23 = "\") ";
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.annotations = r0;
    L_0x0bfa:
        if (r9 == 0) goto L_0x0cd3;
    L_0x0bfc:
        r0 = r9.annotations;
        r22 = r0;
        if (r22 == 0) goto L_0x0cd3;
    L_0x0c02:
        r0 = r9.annotations;
        r23 = r0;
        r0 = r23;
        r0 = r0.length;
        r24 = r0;
        r22 = 0;
    L_0x0c0d:
        r0 = r22;
        r1 = r24;
        if (r0 >= r1) goto L_0x0cd3;
    L_0x0c13:
        r13 = r23[r22];
        r25 = new java.lang.StringBuilder;
        r25.<init>();
        r0 = r20;
        r0 = r0.annotations;
        r26 = r0;
        r25 = r25.append(r26);
        r0 = r25;
        r25 = r0.append(r13);
        r26 = " ";
        r25 = r25.append(r26);
        r25 = r25.toString();
        r0 = r25;
        r1 = r20;
        r1.annotations = r0;
        r22 = r22 + 1;
        goto L_0x0c0d;
    L_0x0c3d:
        r0 = r27;
        r0 = r0.infoMap;
        r25 = r0;
        r0 = r25;
        r25 = r0.getFirst(r10);
        if (r25 == 0) goto L_0x0c4f;
    L_0x0c4b:
        r0 = r20;
        r0.cppName = r10;
    L_0x0c4f:
        r22 = r22 + 1;
        goto L_0x09b7;
    L_0x0c53:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        goto L_0x0af8;
    L_0x0c5b:
        r0 = r9.pointerTypes;
        r22 = r0;
        if (r22 == 0) goto L_0x0b41;
    L_0x0c61:
        r0 = r9.pointerTypes;
        r22 = r0;
        r0 = r22;
        r0 = r0.length;
        r22 = r0;
        if (r22 <= 0) goto L_0x0b41;
    L_0x0c6c:
        r0 = r9.pointerTypes;
        r22 = r0;
        r23 = 0;
        r22 = r22[r23];
        r0 = r22;
        r1 = r20;
        r1.javaName = r0;
        r0 = r9.pointerTypes;
        r22 = r0;
        r0 = r22;
        r1 = r20;
        r1.javaNames = r0;
        goto L_0x0b41;
    L_0x0c86:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        if (r22 != 0) goto L_0x0ba7;
    L_0x0c8e:
        r0 = r20;
        r0 = r0.reference;
        r22 = r0;
        if (r22 == 0) goto L_0x0ba7;
    L_0x0c96:
        r0 = r20;
        r0 = r0.value;
        r22 = r0;
        if (r22 != 0) goto L_0x0ba7;
    L_0x0c9e:
        r22 = new java.lang.StringBuilder;
        r22.<init>();
        r0 = r20;
        r0 = r0.annotations;
        r23 = r0;
        r22 = r22.append(r23);
        r23 = "@ByRef ";
        r22 = r22.append(r23);
        r22 = r22.toString();
        r0 = r22;
        r1 = r20;
        r1.annotations = r0;
        goto L_0x0ba7;
    L_0x0cbf:
        r22 = "";
        goto L_0x0bc6;
    L_0x0cc3:
        r0 = r20;
        r0 = r0.reference;
        r22 = r0;
        if (r22 == 0) goto L_0x0ccf;
    L_0x0ccb:
        r22 = "&";
        goto L_0x0be2;
    L_0x0ccf:
        r22 = "";
        goto L_0x0be2;
    L_0x0cd3:
        r0 = r28;
        r0 = r0.cppName;
        r22 = r0;
        if (r22 == 0) goto L_0x01db;
    L_0x0cdb:
        r0 = r20;
        r0 = r0.javaName;
        r22 = r0;
        r22 = r22.length();
        if (r22 <= 0) goto L_0x01db;
    L_0x0ce7:
        r0 = r28;
        r7 = r0.cppName;
        if (r7 == 0) goto L_0x0d91;
    L_0x0ced:
        r22 = 60;
        r0 = r22;
        r18 = r7.lastIndexOf(r0);
    L_0x0cf5:
        if (r17 >= 0) goto L_0x0d03;
    L_0x0cf7:
        if (r18 < 0) goto L_0x0d03;
    L_0x0cf9:
        r22 = 0;
        r0 = r22;
        r1 = r18;
        r7 = r7.substring(r0, r1);
    L_0x0d03:
        if (r7 == 0) goto L_0x0d95;
    L_0x0d05:
        r22 = "::";
        r0 = r22;
        r12 = r7.lastIndexOf(r0);
    L_0x0d0d:
        if (r11 >= 0) goto L_0x0d19;
    L_0x0d0f:
        if (r12 < 0) goto L_0x0d19;
    L_0x0d11:
        r22 = r12 + 2;
        r0 = r22;
        r7 = r7.substring(r0);
    L_0x0d19:
        r0 = r20;
        r0 = r0.cppName;
        r22 = r0;
        r0 = r22;
        r22 = r0.equals(r7);
        if (r22 == 0) goto L_0x0d7b;
    L_0x0d27:
        r0 = r20;
        r0 = r0.destructor;
        r22 = r0;
        if (r22 != 0) goto L_0x0d98;
    L_0x0d2f:
        r0 = r20;
        r0 = r0.operator;
        r22 = r0;
        if (r22 != 0) goto L_0x0d98;
    L_0x0d37:
        r0 = r20;
        r0 = r0.indirections;
        r22 = r0;
        if (r22 != 0) goto L_0x0d98;
    L_0x0d3f:
        r0 = r20;
        r0 = r0.reference;
        r22 = r0;
        if (r22 != 0) goto L_0x0d98;
    L_0x0d47:
        r0 = r27;
        r0 = r0.tokens;
        r22 = r0;
        r22 = r22.get();
        r23 = 2;
        r0 = r23;
        r0 = new java.lang.Object[r0];
        r23 = r0;
        r24 = 0;
        r25 = 40;
        r25 = java.lang.Character.valueOf(r25);
        r23[r24] = r25;
        r24 = 1;
        r25 = 58;
        r25 = java.lang.Character.valueOf(r25);
        r23[r24] = r25;
        r22 = r22.match(r23);
        if (r22 == 0) goto L_0x0d98;
    L_0x0d73:
        r22 = 1;
    L_0x0d75:
        r0 = r22;
        r1 = r20;
        r1.constructor = r0;
    L_0x0d7b:
        r0 = r20;
        r0 = r0.javaName;
        r22 = r0;
        r0 = r28;
        r1 = r22;
        r22 = r0.shorten(r1);
        r0 = r22;
        r1 = r20;
        r1.javaName = r0;
        goto L_0x01db;
    L_0x0d91:
        r18 = -1;
        goto L_0x0cf5;
    L_0x0d95:
        r12 = -1;
        goto L_0x0d0d;
    L_0x0d98:
        r22 = 0;
        goto L_0x0d75;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Parser.type(org.bytedeco.javacpp.tools.Context):org.bytedeco.javacpp.tools.Type");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    org.bytedeco.javacpp.tools.Declarator declarator(org.bytedeco.javacpp.tools.Context r53, java.lang.String r54, int r55, boolean r56, int r57, boolean r58, boolean r59) throws org.bytedeco.javacpp.tools.ParserException {
        /* JADX: method processing error */
/*
Error: java.lang.IndexOutOfBoundsException: bitIndex < 0: -1
	at java.util.BitSet.get(BitSet.java:623)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.usedArgAssign(CodeShrinker.java:138)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.access$300(CodeShrinker.java:43)
	at jadx.core.dex.visitors.CodeShrinker.canMoveBetweenBlocks(CodeShrinker.java:282)
	at jadx.core.dex.visitors.CodeShrinker.shrinkBlock(CodeShrinker.java:230)
	at jadx.core.dex.visitors.CodeShrinker.shrinkMethod(CodeShrinker.java:38)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkArrayForEach(LoopRegionVisitor.java:196)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkForIndexedLoop(LoopRegionVisitor.java:119)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:65)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r52 = this;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = org.bytedeco.javacpp.tools.Token.TYPEDEF;
        r48[r49] = r50;
        r44 = r47.match(r48);
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = org.bytedeco.javacpp.tools.Token.USING;
        r48[r49] = r50;
        r45 = r47.match(r48);
        r15 = new org.bytedeco.javacpp.tools.Declarator;
        r15.<init>();
        r42 = r52.type(r53);
        if (r42 != 0) goto L_0x0045;
    L_0x0043:
        r15 = 0;
    L_0x0044:
        return r15;
    L_0x0045:
        r11 = 0;
        r33 = 0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.get();
    L_0x0052:
        r0 = r33;
        r1 = r57;
        if (r0 >= r1) goto L_0x0129;
    L_0x0058:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x0129;
    L_0x0070:
        r47 = 3;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 40;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r48 = 1;
        r49 = 91;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r48 = 2;
        r49 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x00ad;
    L_0x00a0:
        r11 = r11 + 1;
    L_0x00a2:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x0052;
    L_0x00ad:
        r47 = 3;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 41;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r48 = 1;
        r49 = 93;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r48 = 2;
        r49 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x00e0;
    L_0x00dd:
        r11 = r11 + -1;
        goto L_0x00a2;
    L_0x00e0:
        if (r11 > 0) goto L_0x00a2;
    L_0x00e2:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 44;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0101;
    L_0x00fe:
        r33 = r33 + 1;
        goto L_0x00a2;
    L_0x0101:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 59;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x00a2;
    L_0x011d:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47.next();
        r15 = 0;
        goto L_0x0044;
    L_0x0129:
        r35 = 0;
        r0 = r42;
        r10 = r0.cppName;
        r0 = r42;
        r0 = r0.constPointer;
        r47 = r0;
        if (r47 == 0) goto L_0x0152;
    L_0x0137:
        r47 = 1;
        r0 = r47;
        r15.constPointer = r0;
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = " const";
        r47 = r47.append(r48);
        r10 = r47.toString();
    L_0x0152:
        if (r57 != 0) goto L_0x0192;
    L_0x0154:
        r0 = r42;
        r0 = r0.indirections;
        r47 = r0;
        if (r47 <= 0) goto L_0x0192;
    L_0x015c:
        r0 = r15.indirections;
        r47 = r0;
        r0 = r42;
        r0 = r0.indirections;
        r48 = r0;
        r47 = r47 + r48;
        r0 = r47;
        r15.indirections = r0;
        r22 = 0;
    L_0x016e:
        r0 = r42;
        r0 = r0.indirections;
        r47 = r0;
        r0 = r22;
        r1 = r47;
        if (r0 >= r1) goto L_0x0192;
    L_0x017a:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "*";
        r47 = r47.append(r48);
        r10 = r47.toString();
        r22 = r22 + 1;
        goto L_0x016e;
    L_0x0192:
        if (r57 != 0) goto L_0x01b7;
    L_0x0194:
        r0 = r42;
        r0 = r0.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x01b7;
    L_0x019c:
        r47 = 1;
        r0 = r47;
        r15.reference = r0;
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "&";
        r47 = r47.append(r48);
        r10 = r47.toString();
    L_0x01b7:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.get();
    L_0x01c1:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x0282;
    L_0x01d9:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 42;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0221;
    L_0x01f5:
        r0 = r15.indirections;
        r47 = r0;
        r47 = r47 + 1;
        r0 = r47;
        r15.indirections = r0;
    L_0x01ff:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r0 = r47;
        r1 = r41;
        r47 = r0.append(r1);
        r10 = r47.toString();
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x01c1;
    L_0x0221:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 38;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0244;
    L_0x023d:
        r47 = 1;
        r0 = r47;
        r15.reference = r0;
        goto L_0x01ff;
    L_0x0244:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = "&&";
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x01ff;
    L_0x025c:
        r47 = 2;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.CONST;
        r47[r48] = r49;
        r48 = 1;
        r49 = org.bytedeco.javacpp.tools.Token.CONSTEXPR;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0282;
    L_0x027a:
        r47 = 1;
        r0 = r47;
        r15.constPointer = r0;
        goto L_0x01ff;
    L_0x0282:
        r6 = new java.util.ArrayList;
        r6.<init>();
        r0 = r42;
        r0 = r0.attributes;
        r47 = r0;
        if (r47 == 0) goto L_0x029e;
    L_0x028f:
        r0 = r42;
        r0 = r0.attributes;
        r47 = r0;
        r47 = java.util.Arrays.asList(r47);
        r0 = r47;
        r6.addAll(r0);
    L_0x029e:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r0 = r47;
        r7 = r0.index;
        r5 = r52.attribute();
    L_0x02ac:
        if (r5 == 0) goto L_0x02e7;
    L_0x02ae:
        r0 = r5.annotation;
        r47 = r0;
        if (r47 == 0) goto L_0x02e7;
    L_0x02b4:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r5.javaName;
        r48 = r0;
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        r6.add(r5);
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r0 = r47;
        r7 = r0.index;
        r5 = r52.attribute();
        goto L_0x02ac;
    L_0x02e7:
        r5 = 0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r0 = r47;
        r0.index = r7;
        r48 = r6.iterator();
    L_0x02f6:
        r47 = r48.hasNext();
        if (r47 == 0) goto L_0x0341;
    L_0x02fc:
        r4 = r48.next();
        r4 = (org.bytedeco.javacpp.tools.Attribute) r4;
        r0 = r4.arguments;
        r47 = r0;
        r47 = r47.length();
        if (r47 <= 0) goto L_0x033f;
    L_0x030c:
        r0 = r4.arguments;
        r47 = r0;
        r49 = 0;
        r0 = r47;
        r1 = r49;
        r47 = r0.charAt(r1);
        r47 = java.lang.Character.isJavaIdentifierStart(r47);
        if (r47 == 0) goto L_0x033f;
    L_0x0320:
        r5 = r4;
        r0 = r4.arguments;
        r47 = r0;
        r49 = r47.toCharArray();
        r0 = r49;
        r0 = r0.length;
        r50 = r0;
        r47 = 0;
    L_0x0330:
        r0 = r47;
        r1 = r50;
        if (r0 >= r1) goto L_0x033f;
    L_0x0336:
        r9 = r49[r47];
        r51 = java.lang.Character.isJavaIdentifierPart(r9);
        if (r51 != 0) goto L_0x0394;
    L_0x033e:
        r5 = 0;
    L_0x033f:
        if (r5 == 0) goto L_0x02f6;
    L_0x0341:
        r11 = 0;
    L_0x0342:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 40;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x0397;
    L_0x0364:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r48 = 1;
        r47 = r47.get(r48);
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 40;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x0397;
    L_0x0388:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47.next();
        r11 = r11 + 1;
        goto L_0x0342;
    L_0x0394:
        r47 = r47 + 1;
        goto L_0x0330;
    L_0x0397:
        r47 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r0 = r47;
        r0 = new int[r0];
        r18 = r0;
        r24 = 0;
        r47 = "";
        r0 = r47;
        r15.cppName = r0;
        r21 = 0;
        r16 = new org.bytedeco.javacpp.tools.Declaration;
        r16.<init>();
        r19 = 0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 40;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 != 0) goto L_0x03f8;
    L_0x03d2:
        if (r44 == 0) goto L_0x06d1;
    L_0x03d4:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r48 = 1;
        r47 = r47.get(r48);
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 40;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x06d1;
    L_0x03f8:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 40;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x0423;
    L_0x041a:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47.next();
    L_0x0423:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.get();
    L_0x042d:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x060d;
    L_0x0445:
        r47 = 2;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 5;
        r49 = java.lang.Integer.valueOf(r49);
        r47[r48] = r49;
        r48 = 1;
        r49 = "::";
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x048f;
    L_0x0467:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r41;
        r47 = r0.append(r1);
        r47 = r47.toString();
        r0 = r47;
        r15.cppName = r0;
    L_0x0484:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x042d;
    L_0x048f:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 42;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0588;
    L_0x04ab:
        r24 = r24 + 1;
        r0 = r15.cppName;
        r47 = r0;
        r48 = "::";
        r47 = r47.endsWith(r48);
        if (r47 == 0) goto L_0x0550;
    L_0x04b9:
        r0 = r15.cppName;
        r47 = r0;
        r48 = 0;
        r0 = r15.cppName;
        r49 = r0;
        r49 = r49.length();
        r49 = r49 + -2;
        r47 = r47.substring(r48, r49);
        r0 = r47;
        r15.cppName = r0;
        r0 = r15.cppName;
        r47 = r0;
        r0 = r53;
        r1 = r47;
        r48 = r0.qualify(r1);
        r0 = r48;
        r0 = r0.length;
        r49 = r0;
        r47 = 0;
    L_0x04e4:
        r0 = r47;
        r1 = r49;
        if (r0 >= r1) goto L_0x0504;
    L_0x04ea:
        r31 = r48[r47];
        r0 = r52;
        r0 = r0.infoMap;
        r50 = r0;
        r51 = 0;
        r0 = r50;
        r1 = r31;
        r2 = r51;
        r21 = r0.getFirst(r1, r2);
        if (r21 == 0) goto L_0x0539;
    L_0x0500:
        r0 = r31;
        r15.cppName = r0;
    L_0x0504:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r16;
        r0 = r0.text;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@Namespace(\"";
        r47 = r47.append(r48);
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "\") ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r16;
        r1.text = r0;
    L_0x0531:
        r47 = "";
        r0 = r47;
        r15.cppName = r0;
        goto L_0x0484;
    L_0x0539:
        r0 = r52;
        r0 = r0.infoMap;
        r50 = r0;
        r0 = r50;
        r1 = r31;
        r50 = r0.getFirst(r1);
        if (r50 == 0) goto L_0x054d;
    L_0x0549:
        r0 = r31;
        r15.cppName = r0;
    L_0x054d:
        r47 = r47 + 1;
        goto L_0x04e4;
    L_0x0550:
        r0 = r15.cppName;
        r47 = r0;
        r47 = r47.length();
        if (r47 <= 0) goto L_0x0531;
    L_0x055a:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r16;
        r0 = r0.text;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@Convention(\"";
        r47 = r47.append(r48);
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "\") ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r16;
        r1.text = r0;
        goto L_0x0531;
    L_0x0588:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 91;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x05e7;
    L_0x05a4:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r48 = 1;
        r30 = r47.get(r48);
        r0 = r15.indices;
        r48 = r0;
        r47 = r48 + 1;
        r0 = r47;
        r15.indices = r0;
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r49 = 0;
        r50 = 1;
        r50 = java.lang.Integer.valueOf(r50);
        r47[r49] = r50;
        r0 = r30;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x05e4;
    L_0x05d6:
        r0 = r30;
        r0 = r0.value;
        r47 = r0;
        r47 = java.lang.Integer.parseInt(r47);
    L_0x05e0:
        r18[r48] = r47;
        goto L_0x0484;
    L_0x05e4:
        r47 = -1;
        goto L_0x05e0;
    L_0x05e7:
        r47 = 2;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 40;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r48 = 1;
        r49 = 41;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0484;
    L_0x060d:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 41;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x0638;
    L_0x062f:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47.next();
    L_0x0638:
        r0 = r15.cppName;
        r47 = r0;
        r47 = r47.length();
        if (r47 != 0) goto L_0x0646;
    L_0x0642:
        r0 = r54;
        r15.cppName = r0;
    L_0x0646:
        r8 = 0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.get();
    L_0x0651:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x0aa1;
    L_0x0669:
        if (r8 != 0) goto L_0x0a9f;
    L_0x066b:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 91;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0a9f;
    L_0x0687:
        r8 = 1;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r48 = 1;
        r30 = r47.get(r48);
        r0 = r15.indices;
        r48 = r0;
        r47 = r48 + 1;
        r0 = r47;
        r15.indices = r0;
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r49 = 0;
        r50 = 1;
        r50 = java.lang.Integer.valueOf(r50);
        r47[r49] = r50;
        r0 = r30;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0a9b;
    L_0x06ba:
        r0 = r30;
        r0 = r0.value;
        r47 = r0;
        r47 = java.lang.Integer.parseInt(r47);
    L_0x06c4:
        r18[r48] = r47;
    L_0x06c6:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x0651;
    L_0x06d1:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 5;
        r50 = java.lang.Integer.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x0638;
    L_0x06f3:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.get();
    L_0x06fd:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x0638;
    L_0x0715:
        r0 = r15.cppName;
        r47 = r0;
        r47 = r47.length();
        if (r47 <= 0) goto L_0x0850;
    L_0x071f:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 42;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0850;
    L_0x073b:
        r0 = r15.cppName;
        r47 = r0;
        r48 = 0;
        r0 = r15.cppName;
        r49 = r0;
        r49 = r49.length();
        r49 = r49 + -2;
        r47 = r47.substring(r48, r49);
        r0 = r47;
        r15.cppName = r0;
        r0 = r15.cppName;
        r47 = r0;
        r0 = r53;
        r1 = r47;
        r48 = r0.qualify(r1);
        r0 = r48;
        r0 = r0.length;
        r49 = r0;
        r47 = 0;
    L_0x0766:
        r0 = r47;
        r1 = r49;
        if (r0 >= r1) goto L_0x0786;
    L_0x076c:
        r31 = r48[r47];
        r0 = r52;
        r0 = r0.infoMap;
        r50 = r0;
        r51 = 0;
        r0 = r50;
        r1 = r31;
        r2 = r51;
        r21 = r0.getFirst(r1, r2);
        if (r21 == 0) goto L_0x07fe;
    L_0x0782:
        r0 = r31;
        r15.cppName = r0;
    L_0x0786:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r16;
        r0 = r0.text;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@Namespace(\"";
        r47 = r47.append(r48);
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "\") ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r16;
        r1.text = r0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.get();
    L_0x07bd:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x0816;
    L_0x07d5:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 42;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0816;
    L_0x07f1:
        r24 = r24 + 1;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x07bd;
    L_0x07fe:
        r0 = r52;
        r0 = r0.infoMap;
        r50 = r0;
        r0 = r50;
        r1 = r31;
        r50 = r0.getFirst(r1);
        if (r50 == 0) goto L_0x0812;
    L_0x080e:
        r0 = r31;
        r15.cppName = r0;
    L_0x0812:
        r47 = r47 + 1;
        goto L_0x0766;
    L_0x0816:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 5;
        r49 = java.lang.Integer.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x084a;
    L_0x0832:
        r47 = r41.toString();
    L_0x0836:
        r0 = r47;
        r15.cppName = r0;
        if (r21 == 0) goto L_0x084d;
    L_0x083c:
        r19 = 1;
    L_0x083e:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x06fd;
    L_0x084a:
        r47 = "";
        goto L_0x0836;
    L_0x084d:
        r19 = 0;
        goto L_0x083e;
    L_0x0850:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = "::";
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0886;
    L_0x0868:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r41;
        r47 = r0.append(r1);
        r47 = r47.toString();
        r0 = r47;
        r15.cppName = r0;
        goto L_0x083e;
    L_0x0886:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.OPERATOR;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x096b;
    L_0x089e:
        r47 = 1;
        r0 = r47;
        r15.operator = r0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r48 = 1;
        r47 = r47.get(r48);
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 5;
        r50 = java.lang.Integer.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x08ee;
    L_0x08c8:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r48 = 1;
        r47 = r47.get(r48);
        r48 = 2;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = org.bytedeco.javacpp.tools.Token.NEW;
        r48[r49] = r50;
        r49 = 1;
        r50 = org.bytedeco.javacpp.tools.Token.DELETE;
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x083e;
    L_0x08ee:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "operator ";
        r47 = r47.append(r48);
        r0 = r52;
        r0 = r0.tokens;
        r48 = r0;
        r48 = r48.next();
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r15.cppName = r0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
    L_0x0921:
        r47 = 2;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r48 = 1;
        r49 = 40;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x0638;
    L_0x0943:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r41;
        r47 = r0.append(r1);
        r47 = r47.toString();
        r0 = r47;
        r15.cppName = r0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x0921;
    L_0x096b:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 60;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0a4a;
    L_0x0987:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r41;
        r47 = r0.append(r1);
        r47 = r47.toString();
        r0 = r47;
        r15.cppName = r0;
        r12 = 0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
    L_0x09af:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = org.bytedeco.javacpp.tools.Token.EOF;
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x083e;
    L_0x09c7:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r41;
        r47 = r0.append(r1);
        r47 = r47.toString();
        r0 = r47;
        r15.cppName = r0;
        if (r12 != 0) goto L_0x0a02;
    L_0x09e6:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 62;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 != 0) goto L_0x083e;
    L_0x0a02:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 60;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0a2b;
    L_0x0a1e:
        r12 = r12 + 1;
    L_0x0a20:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r41 = r47.next();
        goto L_0x09af;
    L_0x0a2b:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 62;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0a20;
    L_0x0a47:
        r12 = r12 + -1;
        goto L_0x0a20;
    L_0x0a4a:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 5;
        r49 = java.lang.Integer.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x0638;
    L_0x0a66:
        r0 = r15.cppName;
        r47 = r0;
        r47 = r47.length();
        if (r47 == 0) goto L_0x0a7c;
    L_0x0a70:
        r0 = r15.cppName;
        r47 = r0;
        r48 = "::";
        r47 = r47.endsWith(r48);
        if (r47 == 0) goto L_0x0638;
    L_0x0a7c:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r41;
        r47 = r0.append(r1);
        r47 = r47.toString();
        r0 = r47;
        r15.cppName = r0;
        goto L_0x083e;
    L_0x0a9b:
        r47 = -1;
        goto L_0x06c4;
    L_0x0a9f:
        if (r8 != 0) goto L_0x0aba;
    L_0x0aa1:
        r0 = r15.indices;
        r47 = r0;
        if (r47 <= 0) goto L_0x0adb;
    L_0x0aa7:
        if (r24 <= 0) goto L_0x0adb;
    L_0x0aa9:
        r0 = r15.indices;
        r47 = r0;
        r48 = r47 + 1;
        r0 = r48;
        r15.indices = r0;
        r48 = -1;
        r18[r47] = r48;
        r24 = r24 + -1;
        goto L_0x0aa1;
    L_0x0aba:
        if (r8 == 0) goto L_0x06c6;
    L_0x0abc:
        r47 = 1;
        r0 = r47;
        r0 = new java.lang.Object[r0];
        r47 = r0;
        r48 = 0;
        r49 = 93;
        r49 = java.lang.Character.valueOf(r49);
        r47[r48] = r49;
        r0 = r41;
        r1 = r47;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x06c6;
    L_0x0ad8:
        r8 = 0;
        goto L_0x06c6;
    L_0x0adb:
        if (r58 == 0) goto L_0x0b71;
    L_0x0add:
        r0 = r15.indices;
        r47 = r0;
        if (r47 <= 0) goto L_0x0b71;
    L_0x0ae3:
        r0 = r15.indirections;
        r47 = r0;
        r47 = r47 + 1;
        r0 = r47;
        r15.indirections = r0;
        r17 = "";
        r22 = 1;
    L_0x0af1:
        r0 = r15.indices;
        r47 = r0;
        r0 = r22;
        r1 = r47;
        if (r0 >= r1) goto L_0x0b25;
    L_0x0afb:
        r47 = r18[r22];
        if (r47 <= 0) goto L_0x0b22;
    L_0x0aff:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r1 = r17;
        r47 = r0.append(r1);
        r48 = "[";
        r47 = r47.append(r48);
        r48 = r18[r22];
        r47 = r47.append(r48);
        r48 = "]";
        r47 = r47.append(r48);
        r17 = r47.toString();
    L_0x0b22:
        r22 = r22 + 1;
        goto L_0x0af1;
    L_0x0b25:
        r47 = r17.isEmpty();
        if (r47 != 0) goto L_0x0e9f;
    L_0x0b2b:
        r47 = 0;
        r47 = r18[r47];
        r48 = -1;
        r0 = r47;
        r1 = r48;
        if (r0 == r1) goto L_0x0e88;
    L_0x0b37:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "(* /*[";
        r47 = r47.append(r48);
        r48 = 0;
        r48 = r18[r48];
        r47 = r47.append(r48);
        r48 = "]*/ )";
        r47 = r47.append(r48);
        r10 = r47.toString();
    L_0x0b5a:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r0 = r47;
        r1 = r17;
        r47 = r0.append(r1);
        r10 = r47.toString();
    L_0x0b71:
        if (r59 == 0) goto L_0x0baf;
    L_0x0b73:
        r0 = r15.indirections;
        r48 = r0;
        r0 = r42;
        r0 = r0.anonymous;
        r47 = r0;
        if (r47 == 0) goto L_0x0eb6;
    L_0x0b7f:
        r47 = 0;
    L_0x0b81:
        r0 = r48;
        r1 = r47;
        if (r0 <= r1) goto L_0x0baf;
    L_0x0b87:
        r0 = r15.indices;
        r47 = r0;
        r48 = r47 + 1;
        r0 = r48;
        r15.indices = r0;
        r48 = -1;
        r18[r47] = r48;
        r0 = r15.indirections;
        r47 = r0;
        r47 = r47 + -1;
        r0 = r47;
        r15.indirections = r0;
        r47 = 0;
        r48 = r10.length();
        r48 = r48 + -1;
        r0 = r47;
        r1 = r48;
        r10 = r10.substring(r0, r1);
    L_0x0baf:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 58;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x0c42;
    L_0x0bd1:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@NoOffset ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.next();
        r48 = 2;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 1;
        r50 = java.lang.Integer.valueOf(r50);
        r48[r49] = r50;
        r49 = 1;
        r50 = 5;
        r50 = java.lang.Integer.valueOf(r50);
        r48[r49] = r50;
        r47.expect(r48);
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.next();
        r48 = 2;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 44;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r49 = 1;
        r50 = 59;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47.expect(r48);
    L_0x0c42:
        r0 = r52;
        r1 = r53;
        r2 = r55;
        r3 = r56;
        r47 = r0.parameters(r1, r2, r3);
        r0 = r47;
        r15.parameters = r0;
        r27 = 1;
        r46 = 0;
        if (r58 == 0) goto L_0x0eba;
    L_0x0c58:
        r0 = r15.indices;
        r47 = r0;
        r48 = 1;
        r0 = r47;
        r1 = r48;
        if (r0 <= r1) goto L_0x0eba;
    L_0x0c64:
        r32 = 1;
    L_0x0c66:
        r23 = 0;
        r0 = r42;
        r0 = r0.constValue;
        r47 = r0;
        if (r47 == 0) goto L_0x0ebe;
    L_0x0c70:
        r0 = r15.indirections;
        r47 = r0;
        r48 = 2;
        r0 = r47;
        r1 = r48;
        if (r0 >= r1) goto L_0x0ebe;
    L_0x0c7c:
        r0 = r15.reference;
        r47 = r0;
        if (r47 != 0) goto L_0x0ebe;
    L_0x0c82:
        r36 = "const ";
    L_0x0c84:
        r0 = r52;
        r0 = r0.infoMap;
        r47 = r0;
        r48 = new java.lang.StringBuilder;
        r48.<init>();
        r0 = r48;
        r1 = r36;
        r48 = r0.append(r1);
        r0 = r42;
        r0 = r0.cppName;
        r49 = r0;
        r48 = r48.append(r49);
        r48 = r48.toString();
        r49 = 0;
        r25 = r47.getFirst(r48, r49);
        if (r44 == 0) goto L_0x0cb3;
    L_0x0cad:
        r0 = r15.parameters;
        r47 = r0;
        if (r47 == 0) goto L_0x0f36;
    L_0x0cb3:
        if (r25 == 0) goto L_0x0cca;
    L_0x0cb5:
        r0 = r25;
        r0 = r0.cppTypes;
        r47 = r0;
        if (r47 == 0) goto L_0x0f36;
    L_0x0cbd:
        r0 = r25;
        r0 = r0.cppTypes;
        r47 = r0;
        r0 = r47;
        r0 = r0.length;
        r47 = r0;
        if (r47 <= 0) goto L_0x0f36;
    L_0x0cca:
        r43 = r42;
        if (r25 == 0) goto L_0x0ceb;
    L_0x0cce:
        r47 = new org.bytedeco.javacpp.tools.Parser;
        r0 = r25;
        r0 = r0.cppTypes;
        r48 = r0;
        r49 = 0;
        r48 = r48[r49];
        r0 = r47;
        r1 = r52;
        r2 = r48;
        r0.<init>(r1, r2);
        r0 = r47;
        r1 = r53;
        r43 = r0.type(r1);
    L_0x0ceb:
        r0 = r52;
        r0 = r0.infoMap;
        r47 = r0;
        r0 = r43;
        r0 = r0.cppName;
        r48 = r0;
        r28 = r47.get(r48);
        r47 = r28.iterator();
    L_0x0cff:
        r48 = r47.hasNext();
        if (r48 == 0) goto L_0x0f36;
    L_0x0d05:
        r26 = r47.next();
        r26 = (org.bytedeco.javacpp.tools.Info) r26;
        r0 = r43;
        r0 = r0.arguments;
        r48 = r0;
        if (r48 == 0) goto L_0x0cff;
    L_0x0d13:
        r0 = r26;
        r0 = r0.annotations;
        r48 = r0;
        if (r48 == 0) goto L_0x0cff;
    L_0x0d1b:
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.constPointer;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.constPointer = r0;
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.constValue;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.constValue = r0;
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.simple;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.simple = r0;
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.indirections;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.indirections = r0;
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.reference;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.reference = r0;
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.annotations;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.cppName;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.cppName = r0;
        r0 = r43;
        r0 = r0.arguments;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r0 = r0.javaName;
        r47 = r0;
        r0 = r47;
        r1 = r42;
        r1.javaName = r0;
        r47 = 1;
        r0 = r47;
        r15.indirections = r0;
        r47 = 0;
        r0 = r47;
        r15.reference = r0;
        r0 = r53;
        r0 = r0.virtualize;
        r47 = r0;
        if (r47 == 0) goto L_0x0de3;
    L_0x0ddf:
        r32 = 1;
        r35 = r10;
    L_0x0de3:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "*";
        r47 = r47.append(r48);
        r10 = r47.toString();
        r0 = r42;
        r0 = r0.constValue;
        r47 = r0;
        if (r47 == 0) goto L_0x0e23;
    L_0x0e04:
        r47 = "const ";
        r0 = r47;
        r47 = r10.startsWith(r0);
        if (r47 != 0) goto L_0x0e23;
    L_0x0e0e:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r48 = "const ";
        r47 = r47.append(r48);
        r0 = r47;
        r47 = r0.append(r10);
        r10 = r47.toString();
    L_0x0e23:
        r0 = r42;
        r0 = r0.constPointer;
        r47 = r0;
        if (r47 == 0) goto L_0x0e4a;
    L_0x0e2b:
        r47 = " const";
        r0 = r47;
        r47 = r10.endsWith(r0);
        if (r47 != 0) goto L_0x0e4a;
    L_0x0e35:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = " const";
        r47 = r47.append(r48);
        r10 = r47.toString();
    L_0x0e4a:
        r0 = r42;
        r0 = r0.indirections;
        r47 = r0;
        if (r47 <= 0) goto L_0x0ec2;
    L_0x0e52:
        r0 = r15.indirections;
        r47 = r0;
        r0 = r42;
        r0 = r0.indirections;
        r48 = r0;
        r47 = r47 + r48;
        r0 = r47;
        r15.indirections = r0;
        r22 = 0;
    L_0x0e64:
        r0 = r42;
        r0 = r0.indirections;
        r47 = r0;
        r0 = r22;
        r1 = r47;
        if (r0 >= r1) goto L_0x0ec2;
    L_0x0e70:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "*";
        r47 = r47.append(r48);
        r10 = r47.toString();
        r22 = r22 + 1;
        goto L_0x0e64;
    L_0x0e88:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "(*)";
        r47 = r47.append(r48);
        r10 = r47.toString();
        goto L_0x0b5a;
    L_0x0e9f:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "*";
        r47 = r47.append(r48);
        r10 = r47.toString();
        goto L_0x0b71;
    L_0x0eb6:
        r47 = 1;
        goto L_0x0b81;
    L_0x0eba:
        r32 = 0;
        goto L_0x0c66;
    L_0x0ebe:
        r36 = "";
        goto L_0x0c84;
    L_0x0ec2:
        r0 = r42;
        r0 = r0.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x0ee5;
    L_0x0eca:
        r47 = 1;
        r0 = r47;
        r15.reference = r0;
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "&";
        r47 = r47.append(r48);
        r10 = r47.toString();
    L_0x0ee5:
        r0 = r26;
        r0 = r0.annotations;
        r48 = r0;
        r0 = r48;
        r0 = r0.length;
        r49 = r0;
        r47 = 0;
    L_0x0ef2:
        r0 = r47;
        r1 = r49;
        if (r0 >= r1) goto L_0x0f24;
    L_0x0ef8:
        r37 = r48[r47];
        r50 = new java.lang.StringBuilder;
        r50.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r51 = r0;
        r50 = r50.append(r51);
        r0 = r50;
        r1 = r37;
        r50 = r0.append(r1);
        r51 = " ";
        r50 = r50.append(r51);
        r50 = r50.toString();
        r0 = r50;
        r1 = r42;
        r1.annotations = r0;
        r47 = r47 + 1;
        goto L_0x0ef2;
    L_0x0f24:
        r0 = r52;
        r0 = r0.infoMap;
        r47 = r0;
        r0 = r42;
        r0 = r0.cppName;
        r48 = r0;
        r49 = 0;
        r25 = r47.getFirst(r48, r49);
    L_0x0f36:
        if (r45 != 0) goto L_0x0fd1;
    L_0x0f38:
        if (r25 == 0) goto L_0x0fd1;
    L_0x0f3a:
        r0 = r25;
        r0 = r0.valueTypes;
        r47 = r0;
        if (r47 == 0) goto L_0x11c4;
    L_0x0f42:
        r0 = r42;
        r0 = r0.constValue;
        r47 = r0;
        if (r47 == 0) goto L_0x0f50;
    L_0x0f4a:
        r0 = r15.reference;
        r47 = r0;
        if (r47 != 0) goto L_0x0f64;
    L_0x0f50:
        r0 = r15.indirections;
        r47 = r0;
        if (r47 != 0) goto L_0x0f5c;
    L_0x0f56:
        r0 = r15.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x0f64;
    L_0x0f5c:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        if (r47 != 0) goto L_0x11c4;
    L_0x0f64:
        r46 = 1;
    L_0x0f66:
        r0 = r25;
        r0 = r0.cppNames;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r48 = "const ";
        r23 = r47.startsWith(r48);
        if (r46 == 0) goto L_0x11c8;
    L_0x0f78:
        r0 = r25;
        r0 = r0.valueTypes;
        r47 = r0;
        r0 = r47;
        r0 = r0.length;
        r27 = r0;
    L_0x0f83:
        if (r55 >= 0) goto L_0x11e1;
    L_0x0f85:
        r47 = 0;
    L_0x0f87:
        r0 = r47;
        r15.infoNumber = r0;
        if (r46 == 0) goto L_0x11e5;
    L_0x0f8d:
        r0 = r25;
        r0 = r0.valueTypes;
        r47 = r0;
        r0 = r15.infoNumber;
        r48 = r0;
        r47 = r47[r48];
    L_0x0f99:
        r0 = r47;
        r1 = r42;
        r1.javaName = r0;
        r0 = r42;
        r0 = r0.javaName;
        r47 = r0;
        r0 = r53;
        r1 = r47;
        r47 = r0.shorten(r1);
        r0 = r47;
        r1 = r42;
        r1.javaName = r0;
        r0 = r25;
        r0 = r0.cast;
        r47 = r0;
        if (r47 == 0) goto L_0x1203;
    L_0x0fbb:
        r0 = r42;
        r0 = r0.cppName;
        r47 = r0;
        r0 = r42;
        r0 = r0.javaName;
        r48 = r0;
        r47 = r47.equals(r48);
        if (r47 != 0) goto L_0x1203;
    L_0x0fcd:
        r47 = 1;
    L_0x0fcf:
        r32 = r32 | r47;
    L_0x0fd1:
        if (r46 != 0) goto L_0x103f;
    L_0x0fd3:
        r0 = r15.indirections;
        r47 = r0;
        if (r47 != 0) goto L_0x1207;
    L_0x0fd9:
        r0 = r15.reference;
        r47 = r0;
        if (r47 != 0) goto L_0x1207;
    L_0x0fdf:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@ByVal ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
    L_0x0ffe:
        if (r32 != 0) goto L_0x103f;
    L_0x1000:
        r0 = r42;
        r0 = r0.javaName;
        r47 = r0;
        r48 = "@Cast";
        r47 = r47.contains(r48);
        if (r47 != 0) goto L_0x103f;
    L_0x100e:
        r0 = r42;
        r0 = r0.constValue;
        r47 = r0;
        if (r47 == 0) goto L_0x1315;
    L_0x1016:
        if (r23 != 0) goto L_0x1315;
    L_0x1018:
        r0 = r42;
        r0 = r0.constPointer;
        r47 = r0;
        if (r47 != 0) goto L_0x1315;
    L_0x1020:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r48 = "@Const ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
    L_0x103f:
        if (r32 == 0) goto L_0x10db;
    L_0x1041:
        r0 = r15.indirections;
        r47 = r0;
        if (r47 != 0) goto L_0x1059;
    L_0x1047:
        r0 = r15.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x1059;
    L_0x104d:
        r47 = 38;
        r48 = 42;
        r0 = r47;
        r1 = r48;
        r10 = r10.replace(r0, r1);
    L_0x1059:
        if (r46 == 0) goto L_0x1079;
    L_0x105b:
        r0 = r42;
        r0 = r0.constValue;
        r47 = r0;
        if (r47 == 0) goto L_0x1079;
    L_0x1063:
        r0 = r15.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x1079;
    L_0x1069:
        r47 = 0;
        r48 = r10.length();
        r48 = r48 + -1;
        r0 = r47;
        r1 = r48;
        r10 = r10.substring(r0, r1);
    L_0x1079:
        r0 = r42;
        r0 = r0.constValue;
        r47 = r0;
        if (r47 == 0) goto L_0x10a0;
    L_0x1081:
        r47 = "const ";
        r0 = r47;
        r47 = r10.startsWith(r0);
        if (r47 != 0) goto L_0x10a0;
    L_0x108b:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r48 = "const ";
        r47 = r47.append(r48);
        r0 = r47;
        r47 = r0.append(r10);
        r10 = r47.toString();
    L_0x10a0:
        if (r35 == 0) goto L_0x135e;
    L_0x10a2:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r48 = "@Cast({\"";
        r47 = r47.append(r48);
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "\", \"";
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r35;
        r47 = r0.append(r1);
        r48 = "\"}) ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
    L_0x10db:
        if (r5 == 0) goto L_0x13c6;
    L_0x10dd:
        r0 = r5.arguments;
        r47 = r0;
    L_0x10e1:
        r0 = r47;
        r15.javaName = r0;
        r0 = r15.cppName;
        r47 = r0;
        r0 = r53;
        r1 = r47;
        r48 = r0.qualify(r1);
        r0 = r48;
        r0 = r0.length;
        r49 = r0;
        r47 = 0;
    L_0x10f8:
        r0 = r47;
        r1 = r49;
        if (r0 >= r1) goto L_0x1118;
    L_0x10fe:
        r31 = r48[r47];
        r0 = r52;
        r0 = r0.infoMap;
        r50 = r0;
        r51 = 0;
        r0 = r50;
        r1 = r31;
        r2 = r51;
        r25 = r0.getFirst(r1, r2);
        if (r25 == 0) goto L_0x13cc;
    L_0x1114:
        r0 = r31;
        r15.cppName = r0;
    L_0x1118:
        if (r19 == 0) goto L_0x13e4;
    L_0x111a:
        r0 = r21;
        r0 = r0.pointerTypes;
        r47 = r0;
        r48 = 0;
        r34 = r47[r48];
    L_0x1124:
        if (r5 != 0) goto L_0x117b;
    L_0x1126:
        if (r54 != 0) goto L_0x117b;
    L_0x1128:
        if (r25 == 0) goto L_0x117b;
    L_0x112a:
        r0 = r25;
        r0 = r0.javaNames;
        r47 = r0;
        if (r47 == 0) goto L_0x117b;
    L_0x1132:
        r0 = r25;
        r0 = r0.javaNames;
        r47 = r0;
        r0 = r47;
        r0 = r0.length;
        r47 = r0;
        if (r47 <= 0) goto L_0x117b;
    L_0x113f:
        r0 = r15.operator;
        r47 = r0;
        if (r47 != 0) goto L_0x116d;
    L_0x1145:
        r0 = r25;
        r0 = r0.cppNames;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r48 = "<";
        r47 = r47.contains(r48);
        if (r47 == 0) goto L_0x116d;
    L_0x1157:
        r0 = r53;
        r0 = r0.templateMap;
        r47 = r0;
        if (r47 == 0) goto L_0x117b;
    L_0x115f:
        r0 = r53;
        r0 = r0.templateMap;
        r47 = r0;
        r0 = r47;
        r0 = r0.type;
        r47 = r0;
        if (r47 != 0) goto L_0x117b;
    L_0x116d:
        r0 = r25;
        r0 = r0.javaNames;
        r47 = r0;
        r48 = 0;
        r47 = r47[r48];
        r0 = r47;
        r15.javaName = r0;
    L_0x117b:
        if (r25 == 0) goto L_0x13ea;
    L_0x117d:
        r0 = r25;
        r0 = r0.annotations;
        r47 = r0;
        if (r47 == 0) goto L_0x13ea;
    L_0x1185:
        r0 = r25;
        r0 = r0.annotations;
        r48 = r0;
        r0 = r48;
        r0 = r0.length;
        r49 = r0;
        r47 = 0;
    L_0x1192:
        r0 = r47;
        r1 = r49;
        if (r0 >= r1) goto L_0x13ea;
    L_0x1198:
        r37 = r48[r47];
        r50 = new java.lang.StringBuilder;
        r50.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r51 = r0;
        r50 = r50.append(r51);
        r0 = r50;
        r1 = r37;
        r50 = r0.append(r1);
        r51 = " ";
        r50 = r50.append(r51);
        r50 = r50.toString();
        r0 = r50;
        r1 = r42;
        r1.annotations = r0;
        r47 = r47 + 1;
        goto L_0x1192;
    L_0x11c4:
        r46 = 0;
        goto L_0x0f66;
    L_0x11c8:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        if (r47 == 0) goto L_0x11dd;
    L_0x11d0:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        r0 = r47;
        r0 = r0.length;
        r27 = r0;
        goto L_0x0f83;
    L_0x11dd:
        r27 = 1;
        goto L_0x0f83;
    L_0x11e1:
        r47 = r55 % r27;
        goto L_0x0f87;
    L_0x11e5:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        if (r47 == 0) goto L_0x11fb;
    L_0x11ed:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        r0 = r15.infoNumber;
        r48 = r0;
        r47 = r47[r48];
        goto L_0x0f99;
    L_0x11fb:
        r0 = r42;
        r0 = r0.javaName;
        r47 = r0;
        goto L_0x0f99;
    L_0x1203:
        r47 = 0;
        goto L_0x0fcf;
    L_0x1207:
        r0 = r15.indirections;
        r47 = r0;
        if (r47 != 0) goto L_0x1258;
    L_0x120d:
        r0 = r15.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x1258;
    L_0x1213:
        r0 = r42;
        r0 = r0.javaName;
        r47 = r0;
        r48 = "@ByPtrPtr ";
        r47 = r47.contains(r48);
        if (r47 == 0) goto L_0x1237;
    L_0x1221:
        r0 = r42;
        r0 = r0.javaName;
        r47 = r0;
        r48 = "@ByPtrPtr ";
        r49 = "@ByPtrRef ";
        r47 = r47.replace(r48, r49);
        r0 = r47;
        r1 = r42;
        r1.javaName = r0;
        goto L_0x0ffe;
    L_0x1237:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@ByRef ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        goto L_0x0ffe;
    L_0x1258:
        r0 = r15.indirections;
        r47 = r0;
        r48 = 1;
        r0 = r47;
        r1 = r48;
        if (r0 != r1) goto L_0x128b;
    L_0x1264:
        r0 = r15.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x128b;
    L_0x126a:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@ByPtrRef ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        goto L_0x0ffe;
    L_0x128b:
        r0 = r15.indirections;
        r47 = r0;
        r48 = 2;
        r0 = r47;
        r1 = r48;
        if (r0 != r1) goto L_0x12ce;
    L_0x1297:
        r0 = r15.reference;
        r47 = r0;
        if (r47 != 0) goto L_0x12ce;
    L_0x129d:
        if (r55 < 0) goto L_0x12ce;
    L_0x129f:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@ByPtrPtr ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        r0 = r42;
        r0 = r0.cppName;
        r47 = r0;
        r48 = "void";
        r47 = r47.equals(r48);
        r32 = r32 | r47;
        goto L_0x0ffe;
    L_0x12ce:
        r0 = r15.indirections;
        r47 = r0;
        r48 = 2;
        r0 = r47;
        r1 = r48;
        if (r0 < r1) goto L_0x0ffe;
    L_0x12da:
        r0 = r15.infoNumber;
        r47 = r0;
        r47 = r47 + r27;
        r0 = r47;
        r15.infoNumber = r0;
        r32 = 1;
        r47 = "PointerPointer";
        r0 = r47;
        r1 = r42;
        r1.javaName = r0;
        r0 = r15.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x0ffe;
    L_0x12f4:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@ByRef ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        goto L_0x0ffe;
    L_0x1315:
        r0 = r42;
        r0 = r0.constPointer;
        r47 = r0;
        if (r47 == 0) goto L_0x103f;
    L_0x131d:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r48 = "@Const({";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.constValue;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = ", ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.constPointer;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "}) ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        goto L_0x103f;
    L_0x135e:
        if (r46 != 0) goto L_0x1399;
    L_0x1360:
        r0 = r15.indirections;
        r47 = r0;
        if (r47 != 0) goto L_0x1399;
    L_0x1366:
        r0 = r15.reference;
        r47 = r0;
        if (r47 != 0) goto L_0x1399;
    L_0x136c:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@Cast(\"";
        r47 = r47.append(r48);
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "*\") ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        goto L_0x10db;
    L_0x1399:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r48 = "@Cast(\"";
        r47 = r47.append(r48);
        r0 = r47;
        r47 = r0.append(r10);
        r48 = "\") ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        goto L_0x10db;
    L_0x13c6:
        r0 = r15.cppName;
        r47 = r0;
        goto L_0x10e1;
    L_0x13cc:
        r0 = r52;
        r0 = r0.infoMap;
        r50 = r0;
        r0 = r50;
        r1 = r31;
        r50 = r0.getFirst(r1);
        if (r50 == 0) goto L_0x13e0;
    L_0x13dc:
        r0 = r31;
        r15.cppName = r0;
    L_0x13e0:
        r47 = r47 + 1;
        goto L_0x10f8;
    L_0x13e4:
        r0 = r15.javaName;
        r34 = r0;
        goto L_0x1124;
    L_0x13ea:
        r0 = r42;
        r15.type = r0;
        r0 = r15.javaName;
        r47 = r0;
        r0 = r47;
        r15.signature = r0;
        r0 = r15.parameters;
        r47 = r0;
        if (r47 != 0) goto L_0x13fe;
    L_0x13fc:
        if (r19 == 0) goto L_0x1447;
    L_0x13fe:
        r0 = r15.parameters;
        r47 = r0;
        if (r47 == 0) goto L_0x141a;
    L_0x1404:
        r0 = r15.infoNumber;
        r47 = r0;
        r0 = r15.parameters;
        r48 = r0;
        r0 = r48;
        r0 = r0.infoNumber;
        r48 = r0;
        r47 = java.lang.Math.max(r47, r48);
        r0 = r47;
        r15.infoNumber = r0;
    L_0x141a:
        r0 = r15.parameters;
        r47 = r0;
        if (r47 == 0) goto L_0x1527;
    L_0x1420:
        if (r24 != 0) goto L_0x1527;
    L_0x1422:
        if (r44 != 0) goto L_0x1527;
    L_0x1424:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r15.signature;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r15.parameters;
        r48 = r0;
        r0 = r48;
        r0 = r0.signature;
        r48 = r0;
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r15.signature = r0;
    L_0x1447:
        r0 = r15.cppName;
        r47 = r0;
        if (r47 == 0) goto L_0x14f7;
    L_0x144d:
        r0 = r15.cppName;
        r29 = r0;
        r0 = r53;
        r0 = r0.namespace;
        r47 = r0;
        if (r47 == 0) goto L_0x1490;
    L_0x1459:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r53;
        r0 = r0.namespace;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "::";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r29;
        r1 = r47;
        r47 = r0.startsWith(r1);
        if (r47 == 0) goto L_0x1490;
    L_0x147c:
        r0 = r15.cppName;
        r47 = r0;
        r0 = r53;
        r0 = r0.namespace;
        r48 = r0;
        r48 = r48.length();
        r48 = r48 + 2;
        r29 = r47.substring(r48);
    L_0x1490:
        r47 = 60;
        r0 = r29;
        r1 = r47;
        r40 = r0.lastIndexOf(r1);
        if (r40 < 0) goto L_0x19b2;
    L_0x149c:
        r47 = 0;
        r0 = r29;
        r1 = r47;
        r2 = r40;
        r39 = r0.substring(r1, r2);
    L_0x14a8:
        r0 = r15.javaName;
        r47 = r0;
        r0 = r39;
        r1 = r47;
        r47 = r0.equals(r1);
        if (r47 != 0) goto L_0x14f7;
    L_0x14b6:
        r47 = "::";
        r0 = r29;
        r1 = r47;
        r47 = r0.contains(r1);
        if (r47 == 0) goto L_0x14ca;
    L_0x14c2:
        r0 = r53;
        r0 = r0.javaName;
        r47 = r0;
        if (r47 != 0) goto L_0x14f7;
    L_0x14ca:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "@Name(\"";
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r29;
        r47 = r0.append(r1);
        r48 = "\") ";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
    L_0x14f7:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r48 = 1;
        r0 = r48;
        r0 = new java.lang.Object[r0];
        r48 = r0;
        r49 = 0;
        r50 = 41;
        r50 = java.lang.Character.valueOf(r50);
        r48[r49] = r50;
        r47 = r47.match(r48);
        if (r47 == 0) goto L_0x0044;
    L_0x1519:
        if (r11 <= 0) goto L_0x0044;
    L_0x151b:
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47.next();
        r11 = r11 + -1;
        goto L_0x14f7;
    L_0x1527:
        r13 = "";
        r0 = r15.type;
        r47 = r0;
        if (r47 == 0) goto L_0x158b;
    L_0x152f:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r13);
        r0 = r15.type;
        r48 = r0;
        r0 = r48;
        r0 = r0.cppName;
        r48 = r0;
        r47 = r47.append(r48);
        r13 = r47.toString();
        r22 = 0;
    L_0x154e:
        r0 = r15.indirections;
        r47 = r0;
        r0 = r22;
        r1 = r47;
        if (r0 >= r1) goto L_0x1570;
    L_0x1558:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r13);
        r48 = "*";
        r47 = r47.append(r48);
        r13 = r47.toString();
        r22 = r22 + 1;
        goto L_0x154e;
    L_0x1570:
        r0 = r15.reference;
        r47 = r0;
        if (r47 == 0) goto L_0x158b;
    L_0x1576:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r13);
        r48 = "&";
        r47 = r47.append(r48);
        r13 = r47.toString();
    L_0x158b:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r47 = r0.append(r13);
        r48 = " (*)(";
        r47 = r47.append(r48);
        r13 = r47.toString();
        r38 = "";
        r0 = r15.parameters;
        r47 = r0;
        if (r47 == 0) goto L_0x162c;
    L_0x15a8:
        r0 = r15.parameters;
        r47 = r0;
        r0 = r47;
        r0 = r0.declarators;
        r48 = r0;
        r0 = r48;
        r0 = r0.length;
        r49 = r0;
        r47 = 0;
    L_0x15b9:
        r0 = r47;
        r1 = r49;
        if (r0 >= r1) goto L_0x162c;
    L_0x15bf:
        r14 = r48[r47];
        if (r14 == 0) goto L_0x1629;
    L_0x15c3:
        r50 = new java.lang.StringBuilder;
        r50.<init>();
        r0 = r50;
        r50 = r0.append(r13);
        r0 = r50;
        r1 = r38;
        r50 = r0.append(r1);
        r0 = r14.type;
        r51 = r0;
        r0 = r51;
        r0 = r0.cppName;
        r51 = r0;
        r50 = r50.append(r51);
        r13 = r50.toString();
        r22 = 0;
    L_0x15ea:
        r0 = r14.indirections;
        r50 = r0;
        r0 = r22;
        r1 = r50;
        if (r0 >= r1) goto L_0x160c;
    L_0x15f4:
        r50 = new java.lang.StringBuilder;
        r50.<init>();
        r0 = r50;
        r50 = r0.append(r13);
        r51 = "*";
        r50 = r50.append(r51);
        r13 = r50.toString();
        r22 = r22 + 1;
        goto L_0x15ea;
    L_0x160c:
        r0 = r14.reference;
        r50 = r0;
        if (r50 == 0) goto L_0x1627;
    L_0x1612:
        r50 = new java.lang.StringBuilder;
        r50.<init>();
        r0 = r50;
        r50 = r0.append(r13);
        r51 = "&";
        r50 = r50.append(r51);
        r13 = r50.toString();
    L_0x1627:
        r38 = ", ";
    L_0x1629:
        r47 = r47 + 1;
        goto L_0x15b9;
    L_0x162c:
        r0 = r52;
        r0 = r0.infoMap;
        r47 = r0;
        r48 = new java.lang.StringBuilder;
        r48.<init>();
        r0 = r48;
        r48 = r0.append(r13);
        r49 = ")";
        r48 = r48.append(r49);
        r13 = r48.toString();
        r0 = r47;
        r25 = r0.getFirst(r13);
        r20 = 0;
        if (r34 == 0) goto L_0x167a;
    L_0x1651:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r48 = 0;
        r0 = r34;
        r1 = r48;
        r48 = r0.charAt(r1);
        r48 = java.lang.Character.toUpperCase(r48);
        r47 = r47.append(r48);
        r48 = 1;
        r0 = r34;
        r1 = r48;
        r48 = r0.substring(r1);
        r47 = r47.append(r48);
        r20 = r47.toString();
    L_0x167a:
        if (r25 == 0) goto L_0x16e4;
    L_0x167c:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        if (r47 == 0) goto L_0x16e4;
    L_0x1684:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        r0 = r47;
        r0 = r0.length;
        r47 = r0;
        if (r47 <= 0) goto L_0x16e4;
    L_0x1691:
        r0 = r25;
        r0 = r0.pointerTypes;
        r47 = r0;
        r48 = 0;
        r20 = r47[r48];
    L_0x169b:
        if (r25 == 0) goto L_0x176b;
    L_0x169d:
        r0 = r25;
        r0 = r0.annotations;
        r47 = r0;
        if (r47 == 0) goto L_0x176b;
    L_0x16a5:
        r0 = r25;
        r0 = r0.annotations;
        r48 = r0;
        r0 = r48;
        r0 = r0.length;
        r49 = r0;
        r47 = 0;
    L_0x16b2:
        r0 = r47;
        r1 = r49;
        if (r0 >= r1) goto L_0x176b;
    L_0x16b8:
        r37 = r48[r47];
        r50 = new java.lang.StringBuilder;
        r50.<init>();
        r0 = r16;
        r0 = r0.text;
        r51 = r0;
        r50 = r50.append(r51);
        r0 = r50;
        r1 = r37;
        r50 = r0.append(r1);
        r51 = " ";
        r50 = r50.append(r51);
        r50 = r50.toString();
        r0 = r50;
        r1 = r16;
        r1.text = r0;
        r47 = r47 + 1;
        goto L_0x16b2;
    L_0x16e4:
        if (r44 == 0) goto L_0x16e9;
    L_0x16e6:
        r20 = r34;
        goto L_0x169b;
    L_0x16e9:
        r0 = r15.parameters;
        r47 = r0;
        if (r47 == 0) goto L_0x1720;
    L_0x16ef:
        r0 = r15.parameters;
        r47 = r0;
        r0 = r47;
        r0 = r0.signature;
        r47 = r0;
        r47 = r47.length();
        if (r47 <= 0) goto L_0x1720;
    L_0x16ff:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r47;
        r1 = r20;
        r47 = r0.append(r1);
        r0 = r15.parameters;
        r48 = r0;
        r0 = r48;
        r0 = r0.signature;
        r48 = r0;
        r47 = r47.append(r48);
        r20 = r47.toString();
        goto L_0x169b;
    L_0x1720:
        r0 = r42;
        r0 = r0.javaName;
        r47 = r0;
        r48 = "void";
        r47 = r47.equals(r48);
        if (r47 != 0) goto L_0x169b;
    L_0x172e:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r42;
        r0 = r0.javaName;
        r48 = r0;
        r49 = 0;
        r48 = r48.charAt(r49);
        r48 = java.lang.Character.toUpperCase(r48);
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.javaName;
        r48 = r0;
        r49 = 1;
        r48 = r48.substring(r49);
        r47 = r47.append(r48);
        r48 = "_";
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r20;
        r47 = r0.append(r1);
        r20 = r47.toString();
        goto L_0x169b;
    L_0x176b:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r16;
        r0 = r0.text;
        r48 = r0;
        r48 = r47.append(r48);
        r0 = r52;
        r0 = r0.tokens;
        r47 = r0;
        r47 = r47.get();
        r49 = 2;
        r0 = r49;
        r0 = new java.lang.Object[r0];
        r49 = r0;
        r50 = 0;
        r51 = org.bytedeco.javacpp.tools.Token.CONST;
        r49[r50] = r51;
        r50 = 1;
        r51 = org.bytedeco.javacpp.tools.Token.CONSTEXPR;
        r49[r50] = r51;
        r0 = r47;
        r1 = r49;
        r47 = r0.match(r1);
        if (r47 == 0) goto L_0x18b0;
    L_0x17a2:
        r47 = "@Const ";
    L_0x17a4:
        r0 = r48;
        r1 = r47;
        r47 = r0.append(r1);
        r48 = "public static class ";
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r20;
        r47 = r0.append(r1);
        r48 = " extends FunctionPointer {\n    static { Loader.load(); }\n    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */\n    public    ";
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r20;
        r47 = r0.append(r1);
        r48 = "(Pointer p) { super(p); }\n";
        r48 = r47.append(r48);
        if (r21 == 0) goto L_0x18b4;
    L_0x17d0:
        r47 = "";
    L_0x17d2:
        r0 = r48;
        r1 = r47;
        r47 = r0.append(r1);
        r47 = r47.toString();
        r0 = r47;
        r1 = r16;
        r1.text = r0;
        if (r19 == 0) goto L_0x18db;
    L_0x17e6:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r16;
        r0 = r0.text;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "    public native ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.javaName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = " get(";
        r47 = r47.append(r48);
        r0 = r21;
        r0 = r0.pointerTypes;
        r48 = r0;
        r49 = 0;
        r48 = r48[r49];
        r47 = r47.append(r48);
        r48 = " o);\n    public native ";
        r47 = r47.append(r48);
        r0 = r47;
        r1 = r20;
        r47 = r0.append(r1);
        r48 = " put(";
        r47 = r47.append(r48);
        r0 = r21;
        r0 = r0.pointerTypes;
        r48 = r0;
        r49 = 0;
        r48 = r48[r49];
        r47 = r47.append(r48);
        r48 = " o, ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.javaName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = " v);\n}\n";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r16;
        r1.text = r0;
    L_0x186f:
        r0 = r20;
        r1 = r16;
        r1.signature = r0;
        r47 = new org.bytedeco.javacpp.tools.Declarator;
        r47.<init>();
        r0 = r47;
        r1 = r16;
        r1.declarator = r0;
        r0 = r16;
        r0 = r0.declarator;
        r47 = r0;
        r0 = r15.parameters;
        r48 = r0;
        r0 = r48;
        r1 = r47;
        r1.parameters = r0;
        r0 = r16;
        r15.definition = r0;
        r0 = r24;
        r15.indirections = r0;
        if (r19 != 0) goto L_0x18a0;
    L_0x189a:
        r47 = 0;
        r0 = r47;
        r15.parameters = r0;
    L_0x18a0:
        r47 = "";
        r0 = r47;
        r1 = r42;
        r1.annotations = r0;
        r0 = r20;
        r1 = r42;
        r1.javaName = r0;
        goto L_0x1447;
    L_0x18b0:
        r47 = "";
        goto L_0x17a4;
    L_0x18b4:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r49 = "    protected ";
        r0 = r47;
        r1 = r49;
        r47 = r0.append(r1);
        r0 = r47;
        r1 = r20;
        r47 = r0.append(r1);
        r49 = "() { allocate(); }\n    private native void allocate();\n";
        r0 = r47;
        r1 = r49;
        r47 = r0.append(r1);
        r47 = r47.toString();
        goto L_0x17d2;
    L_0x18db:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r0 = r16;
        r0 = r0.text;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = "    public native ";
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.annotations;
        r48 = r0;
        r47 = r47.append(r48);
        r0 = r42;
        r0 = r0.javaName;
        r48 = r0;
        r47 = r47.append(r48);
        r48 = " call";
        r48 = r47.append(r48);
        if (r21 == 0) goto L_0x19a7;
    L_0x190c:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r49 = "(";
        r0 = r47;
        r1 = r49;
        r47 = r0.append(r1);
        r0 = r21;
        r0 = r0.pointerTypes;
        r49 = r0;
        r50 = 0;
        r49 = r49[r50];
        r0 = r47;
        r1 = r49;
        r47 = r0.append(r1);
        r49 = " o";
        r0 = r47;
        r1 = r49;
        r49 = r0.append(r1);
        r0 = r15.parameters;
        r47 = r0;
        r0 = r47;
        r0 = r0.list;
        r47 = r0;
        r50 = 1;
        r0 = r47;
        r1 = r50;
        r47 = r0.charAt(r1);
        r50 = 41;
        r0 = r47;
        r1 = r50;
        if (r0 != r1) goto L_0x197b;
    L_0x1953:
        r47 = ")";
    L_0x1955:
        r0 = r49;
        r1 = r47;
        r47 = r0.append(r1);
        r47 = r47.toString();
    L_0x1961:
        r0 = r48;
        r1 = r47;
        r47 = r0.append(r1);
        r48 = ";\n}\n";
        r47 = r47.append(r48);
        r47 = r47.toString();
        r0 = r47;
        r1 = r16;
        r1.text = r0;
        goto L_0x186f;
    L_0x197b:
        r47 = new java.lang.StringBuilder;
        r47.<init>();
        r50 = ", ";
        r0 = r47;
        r1 = r50;
        r47 = r0.append(r1);
        r0 = r15.parameters;
        r50 = r0;
        r0 = r50;
        r0 = r0.list;
        r50 = r0;
        r51 = 1;
        r50 = r50.substring(r51);
        r0 = r47;
        r1 = r50;
        r47 = r0.append(r1);
        r47 = r47.toString();
        goto L_0x1955;
    L_0x19a7:
        r0 = r15.parameters;
        r47 = r0;
        r0 = r47;
        r0 = r0.list;
        r47 = r0;
        goto L_0x1961;
    L_0x19b2:
        r39 = r29;
        goto L_0x14a8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Parser.declarator(org.bytedeco.javacpp.tools.Context, java.lang.String, int, boolean, int, boolean, boolean):org.bytedeco.javacpp.tools.Declarator");
    }

    String commentDoc(String s, int startIndex) {
        if (startIndex < 0 || startIndex > s.length()) {
            return s;
        }
        int index = s.indexOf("/**", startIndex);
        StringBuilder sb = new StringBuilder(s);
        while (index < sb.length()) {
            char c = sb.charAt(index);
            String ss = sb.substring(index + 1);
            if (c == '`' && ss.startsWith("``") && sb.length() - index > 3) {
                sb.replace(index, index + 3, "<pre>{@code" + (Character.isWhitespace(sb.charAt(index + 3)) ? "" : " "));
                index = sb.indexOf("```", index);
                if (index < 0) {
                    break;
                }
                sb.replace(index, index + 3, "}</pre>");
            } else if (c == '`') {
                sb.replace(index, index + 1, "{@code ");
                index = sb.indexOf("`", index);
                if (index < 0) {
                    break;
                }
                sb.replace(index, index + 1, "}");
            } else if ((c == '\\' || c == '@') && ss.startsWith("code")) {
                sb.replace(index, index + 5, "<pre>{@code" + (Character.isWhitespace(sb.charAt(index + 5)) ? "" : " "));
                index = sb.indexOf(c + "endcode", index);
                if (index < 0) {
                    break;
                }
                sb.replace(index, index + 8, "}</pre>");
            } else if ((c == '\\' || c == '@') && ss.startsWith("verbatim")) {
                sb.replace(index, index + 9, "<pre>{@literal" + (Character.isWhitespace(sb.charAt(index + 9)) ? "" : " "));
                index = sb.indexOf(c + "endverbatim", index);
                if (index < 0) {
                    break;
                }
                sb.replace(index, index + 12, "}</pre>");
            } else if (c == '\n' && ss.length() > 0 && ss.charAt(0) == '\n') {
                n = 0;
                while (n < ss.length() && ss.charAt(n) == '\n') {
                    n++;
                }
                String indent = "";
                while (n < ss.length() && Character.isWhitespace(ss.charAt(n))) {
                    indent = indent + ss.charAt(n);
                    n++;
                }
                sb.insert(index + 1, indent + "<p>");
            } else if (c == '\\' || c == '@') {
                String foundTag = null;
                for (String tag : this.docTags) {
                    if (ss.startsWith(tag)) {
                        foundTag = tag;
                        break;
                    }
                }
                if (foundTag != null) {
                    sb.setCharAt(index, '@');
                    n = (foundTag.length() + index) + 1;
                    if (sb.charAt(n) == 's' && !foundTag.endsWith(HtmlTags.f36S)) {
                        sb.deleteCharAt(n);
                    } else if (!Character.isWhitespace(sb.charAt(n))) {
                        sb.insert(n, ' ');
                    }
                } else {
                    sb.setCharAt(index, '\\');
                }
            } else if (c == '*' && ss.charAt(0) == '/') {
                index = sb.indexOf("/**", index);
                if (index < 0) {
                    break;
                }
            }
            index++;
        }
        return sb.toString();
    }

    String commentBefore() throws ParserException {
        String comment = "";
        this.tokens.raw = true;
        while (this.tokens.index > 0) {
            if (!this.tokens.get(-1).match(Integer.valueOf(4))) {
                break;
            }
            TokenIndexer tokenIndexer = this.tokens;
            tokenIndexer.index--;
        }
        boolean closeComment = false;
        int startDoc = -1;
        Token token = this.tokens.get();
        while (true) {
            if (!token.match(Integer.valueOf(4))) {
                break;
            }
            String s = token.value;
            if (!s.startsWith("/**") && !s.startsWith("/*!") && !s.startsWith("///") && !s.startsWith("//!")) {
                if (closeComment && !comment.endsWith("*/")) {
                    closeComment = false;
                    comment = comment + " */";
                }
                startDoc = comment.length();
                comment = comment + token.spacing + s;
            } else if (s.length() <= 3 || s.charAt(3) != '<') {
                if (s.length() < 3 || (!(s.startsWith("///") || s.startsWith("//!")) || s.startsWith("////") || s.startsWith("///*"))) {
                    if (s.length() > 3 && !s.startsWith("///")) {
                        s = "/**" + s.substring(3);
                    }
                    if (startDoc < 0 && s.startsWith("/**")) {
                        startDoc = comment.length();
                    }
                    comment = comment + token.spacing + s;
                } else {
                    String lastComment = comment.trim();
                    int n2 = lastComment.indexOf(10);
                    while (!lastComment.startsWith("/*") && n2 > 0) {
                        lastComment = n2 + 1 < lastComment.length() ? lastComment.substring(n2 + 1).trim() : "";
                        n2 = lastComment.indexOf(10);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    String str = (comment.length() == 0 || comment.contains("*/") || !lastComment.startsWith("/*")) ? "/**" : " * ";
                    s = stringBuilder.append(str).append(s.substring(3)).toString();
                    closeComment = true;
                    startDoc = comment.length();
                    comment = comment + token.spacing + s;
                }
            }
            token = this.tokens.next();
        }
        if (closeComment && !comment.endsWith("*/")) {
            comment = comment + " */";
        }
        this.tokens.raw = false;
        return commentDoc(comment, startDoc);
    }

    String commentAfter() throws ParserException {
        String comment = "";
        this.tokens.raw = true;
        while (this.tokens.index > 0) {
            if (!this.tokens.get(-1).match(Integer.valueOf(4))) {
                break;
            }
            TokenIndexer tokenIndexer = this.tokens;
            tokenIndexer.index--;
        }
        boolean closeComment = false;
        int startDoc = -1;
        Token token = this.tokens.get();
        while (true) {
            if (!token.match(Integer.valueOf(4))) {
                break;
            }
            String s = token.value;
            String spacing = token.spacing;
            int n = spacing.lastIndexOf(10) + 1;
            if ((s.startsWith("/**") || s.startsWith("/*!") || s.startsWith("///") || s.startsWith("//!")) && (s.length() <= 3 || s.charAt(3) == '<')) {
                if (s.length() > 4 && (s.startsWith("///") || s.startsWith("//!"))) {
                    String lastComment = comment.trim();
                    int n2 = lastComment.indexOf(10);
                    while (!lastComment.startsWith("/*") && n2 > 0) {
                        lastComment = n2 + 1 < lastComment.length() ? lastComment.substring(n2 + 1).trim() : "";
                        n2 = lastComment.indexOf(10);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    String str = (comment.length() == 0 || comment.contains("*/") || !lastComment.startsWith("/*")) ? "/**" : " * ";
                    s = stringBuilder.append(str).append(s.substring(4)).toString();
                    closeComment = true;
                } else if (s.length() > 4) {
                    s = "/**" + s.substring(4);
                }
                if (startDoc < 0 && s.startsWith("/**")) {
                    startDoc = comment.length();
                }
                comment = comment + spacing.substring(0, n) + s;
            }
            token = this.tokens.next();
        }
        if (closeComment && !comment.endsWith("*/")) {
            comment = comment + " */";
        }
        if (comment.length() > 0) {
            comment = comment + "\n";
        }
        this.tokens.raw = false;
        return commentDoc(comment, startDoc);
    }

    org.bytedeco.javacpp.tools.Attribute attribute() throws org.bytedeco.javacpp.tools.ParserException {
        /* JADX: method processing error */
/*
Error: java.lang.IndexOutOfBoundsException: bitIndex < 0: -1
	at java.util.BitSet.get(BitSet.java:623)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.usedArgAssign(CodeShrinker.java:138)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.access$300(CodeShrinker.java:43)
	at jadx.core.dex.visitors.CodeShrinker.canMoveBetweenBlocks(CodeShrinker.java:282)
	at jadx.core.dex.visitors.CodeShrinker.shrinkBlock(CodeShrinker.java:230)
	at jadx.core.dex.visitors.CodeShrinker.shrinkMethod(CodeShrinker.java:38)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkArrayForEach(LoopRegionVisitor.java:196)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkForIndexedLoop(LoopRegionVisitor.java:119)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:65)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r13 = this;
        r12 = 40;
        r6 = 1;
        r7 = 0;
        r5 = r13.tokens;
        r5 = r5.get();
        r8 = new java.lang.Object[r6];
        r9 = 5;
        r9 = java.lang.Integer.valueOf(r9);
        r8[r7] = r9;
        r5 = r5.match(r8);
        if (r5 != 0) goto L_0x001b;
    L_0x0019:
        r0 = 0;
    L_0x001a:
        return r0;
    L_0x001b:
        r0 = new org.bytedeco.javacpp.tools.Attribute;
        r0.<init>();
        r5 = r13.infoMap;
        r8 = r13.tokens;
        r8 = r8.get();
        r8 = r8.value;
        r0.cppName = r8;
        r2 = r5.getFirst(r8);
        if (r2 == 0) goto L_0x006d;
    L_0x0032:
        r5 = r2.annotations;
        if (r5 == 0) goto L_0x006d;
    L_0x0036:
        r5 = r2.javaNames;
        if (r5 != 0) goto L_0x006d;
    L_0x003a:
        r5 = r2.valueTypes;
        if (r5 != 0) goto L_0x006d;
    L_0x003e:
        r5 = r2.pointerTypes;
        if (r5 != 0) goto L_0x006d;
    L_0x0042:
        r5 = r6;
    L_0x0043:
        r0.annotation = r5;
        if (r5 == 0) goto L_0x006f;
    L_0x0047:
        r8 = r2.annotations;
        r9 = r8.length;
        r5 = r7;
    L_0x004b:
        if (r5 >= r9) goto L_0x006f;
    L_0x004d:
        r3 = r8[r5];
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = r0.javaName;
        r10 = r10.append(r11);
        r10 = r10.append(r3);
        r11 = " ";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r0.javaName = r10;
        r5 = r5 + 1;
        goto L_0x004b;
    L_0x006d:
        r5 = r7;
        goto L_0x0043;
    L_0x006f:
        r5 = r13.tokens;
        r5 = r5.next();
        r8 = new java.lang.Object[r6];
        r9 = java.lang.Character.valueOf(r12);
        r8[r7] = r9;
        r5 = r5.match(r8);
        if (r5 == 0) goto L_0x001a;
    L_0x0083:
        r1 = 1;
        r5 = r13.tokens;
        r5.raw = r6;
        r5 = r13.tokens;
        r4 = r5.next();
    L_0x008e:
        r5 = new java.lang.Object[r6];
        r8 = org.bytedeco.javacpp.tools.Token.EOF;
        r5[r7] = r8;
        r5 = r4.match(r5);
        if (r5 != 0) goto L_0x00e4;
    L_0x009a:
        if (r1 <= 0) goto L_0x00e4;
    L_0x009c:
        r5 = new java.lang.Object[r6];
        r8 = java.lang.Character.valueOf(r12);
        r5[r7] = r8;
        r5 = r4.match(r5);
        if (r5 == 0) goto L_0x00b3;
    L_0x00aa:
        r1 = r1 + 1;
    L_0x00ac:
        r5 = r13.tokens;
        r4 = r5.next();
        goto L_0x008e;
    L_0x00b3:
        r5 = new java.lang.Object[r6];
        r8 = 41;
        r8 = java.lang.Character.valueOf(r8);
        r5[r7] = r8;
        r5 = r4.match(r5);
        if (r5 == 0) goto L_0x00c6;
    L_0x00c3:
        r1 = r1 + -1;
        goto L_0x00ac;
    L_0x00c6:
        if (r2 == 0) goto L_0x00cc;
    L_0x00c8:
        r5 = r2.skip;
        if (r5 != 0) goto L_0x00ac;
    L_0x00cc:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r8 = r0.arguments;
        r5 = r5.append(r8);
        r8 = r4.value;
        r5 = r5.append(r8);
        r5 = r5.toString();
        r0.arguments = r5;
        goto L_0x00ac;
    L_0x00e4:
        r5 = r13.tokens;
        r5.raw = r7;
        goto L_0x001a;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Parser.attribute():org.bytedeco.javacpp.tools.Attribute");
    }

    String body() throws ParserException {
        if (!this.tokens.get().match(Character.valueOf('{'))) {
            return null;
        }
        int count = 1;
        this.tokens.raw = true;
        Token token = this.tokens.next();
        while (true) {
            if (token.match(Token.EOF) || count <= 0) {
                this.tokens.raw = false;
            } else {
                if (token.match(Character.valueOf('{'))) {
                    count++;
                } else {
                    if (token.match(Character.valueOf('}'))) {
                        count--;
                    }
                }
                token = this.tokens.next();
            }
        }
        this.tokens.raw = false;
        return "";
    }

    Parameters parameters(Context context, int infoNumber, boolean useDefaults) throws ParserException {
        int backIndex = this.tokens.index;
        if (!this.tokens.get().match(Character.valueOf('('))) {
            return null;
        }
        String spacing;
        int count = 0;
        Parameters params = new Parameters();
        List<Declarator> dcls = new ArrayList();
        params.list = "(";
        params.names = "(";
        int lastVarargs = -1;
        Token token = this.tokens.next();
        while (true) {
            if (token.match(Token.EOF)) {
                break;
            }
            spacing = token.spacing;
            if (token.match(Character.valueOf(')'))) {
                break;
            }
            int n;
            int count2 = count + 1;
            Declarator dcl = declarator(context, "arg" + count, infoNumber, useDefaults, 0, true, false);
            boolean hasDefault = !this.tokens.get().match(Character.valueOf(','), Character.valueOf(')'));
            Token defaultToken = null;
            String defaultValue = "";
            if (dcl != null && hasDefault) {
                defaultToken = this.tokens.get();
                int count22 = 0;
                token = this.tokens.next();
                token.spacing = "";
                while (true) {
                    if (token.match(Token.EOF)) {
                        break;
                    }
                    if (count22 == 0) {
                        if (token.match(Character.valueOf(','), Character.valueOf(')'))) {
                            break;
                        }
                    }
                    if (token.match(Character.valueOf('('))) {
                        count22++;
                    } else {
                        if (token.match(Character.valueOf(')'))) {
                            count22--;
                        }
                    }
                    String cppName = token.value;
                    for (String name : context.qualify(cppName)) {
                        if (this.infoMap.getFirst(name, false) != null) {
                            cppName = name;
                            break;
                        }
                        if (this.infoMap.getFirst(name) != null) {
                            cppName = name;
                        }
                    }
                    if (token.match(Integer.valueOf(5))) {
                        while (this.tokens.get(1).equals("::")) {
                            this.tokens.next();
                            Token t = this.tokens.next();
                            cppName = cppName + "::" + t.spacing + t;
                        }
                    }
                    StringBuilder append = new StringBuilder().append(defaultValue).append(token.spacing);
                    if (cppName == null || cppName.length() <= 0) {
                        Object cppName2 = token;
                    }
                    defaultValue = append.append(cppName).toString();
                    token = this.tokens.next();
                }
                for (String name2 : context.qualify(defaultValue)) {
                    if (this.infoMap.getFirst(name2, false) != null) {
                        defaultValue = name2;
                        break;
                    }
                    if (this.infoMap.getFirst(name2) != null) {
                        defaultValue = name2;
                    }
                }
                String s = dcl.type.annotations;
                n = s.indexOf("@ByVal ");
                if (n < 0) {
                    n = s.indexOf("@ByRef ");
                }
                if (n >= 0) {
                    if (!defaultValue.startsWith(dcl.type.cppName)) {
                        defaultValue = dcl.type.cppName + "(" + defaultValue + ")";
                    }
                    Info info = this.infoMap.getFirst(defaultValue);
                    if (info == null || !info.skip) {
                        defaultValue = defaultValue.replaceAll("\"", "\\\\\"").replaceAll("\n(\\s*)", "\"\n$1 + \"");
                        s = s.substring(0, n + 6) + "(nullValue = \"" + defaultValue + "\")" + s.substring(n + 6);
                    } else if (useDefaults) {
                        this.tokens.index = backIndex;
                        return parameters(context, infoNumber, false);
                    }
                }
                dcl.type.annotations = s;
            }
            if (!(dcl == null || dcl.type.javaName.equals("void") || (hasDefault && useDefaults))) {
                if (lastVarargs >= 0) {
                    params.list = params.list.substring(0, lastVarargs) + XMPConst.ARRAY_ITEM_NAME + params.list.substring(lastVarargs + 3);
                }
                n = params.list.length();
                params.infoNumber = Math.max(params.infoNumber, dcl.infoNumber);
                params.list += (count2 > 1 ? "," : "") + spacing + dcl.type.annotations + dcl.type.javaName + " " + dcl.javaName;
                lastVarargs = params.list.indexOf("...", n);
                if (hasDefault && !dcl.type.annotations.contains("(nullValue = ")) {
                    params.list += "/*" + defaultToken + defaultValue + "*/";
                }
                params.signature += '_';
                for (char c : dcl.type.javaName.substring(dcl.type.javaName.lastIndexOf(32) + 1).toCharArray()) {
                    char c2;
                    StringBuilder append2 = new StringBuilder().append(params.signature);
                    if (!Character.isJavaIdentifierPart(c2)) {
                        c2 = '_';
                    }
                    params.signature = append2.append(c2).toString();
                }
                params.names += (count2 > 1 ? ", " : "") + dcl.javaName;
                if (dcl.javaName.startsWith("arg")) {
                    try {
                        count = Integer.parseInt(dcl.javaName.substring(3)) + 1;
                    } catch (NumberFormatException e) {
                        count = count2;
                    }
                    if (!(hasDefault && useDefaults)) {
                        dcls.add(dcl);
                    }
                    if (this.tokens.get().expect(Character.valueOf(','), Character.valueOf(')')).match(Character.valueOf(','))) {
                        this.tokens.next();
                    }
                    token = this.tokens.get();
                }
            }
            count = count2;
            dcls.add(dcl);
            if (this.tokens.get().expect(Character.valueOf(','), Character.valueOf(')')).match(Character.valueOf(','))) {
                this.tokens.next();
            }
            token = this.tokens.get();
        }
        params.list += spacing + ")";
        params.names += ")";
        this.tokens.next();
        params.declarators = (Declarator[]) dcls.toArray(new Declarator[dcls.size()]);
        return params;
    }

    boolean function(Context context, DeclarationList declList) throws ParserException {
        int backIndex = this.tokens.index;
        String spacing = this.tokens.get().spacing;
        String modifiers = "public native ";
        int startIndex = this.tokens.index;
        Type type = type(context);
        Parameters params = parameters(context, 0, false);
        Declarator dcl = new Declarator();
        Declaration decl = new Declaration();
        if (type.javaName.length() == 0) {
            this.tokens.index = backIndex;
            return false;
        } else if (context.javaName != null || type.operator || params == null) {
            if ((type.constructor || type.destructor || type.operator) && params != null) {
                dcl.type = type;
                dcl.parameters = params;
                dcl.cppName = type.cppName;
                dcl.javaName = type.javaName.substring(type.javaName.lastIndexOf(32) + 1);
                if (type.operator) {
                    dcl.cppName = "operator " + dcl.cppName;
                    dcl.javaName = "as" + Character.toUpperCase(dcl.javaName.charAt(0)) + dcl.javaName.substring(1);
                }
                dcl.signature = dcl.javaName + params.signature;
            } else {
                this.tokens.index = startIndex;
                dcl = declarator(context, null, 0, false, 0, false, false);
                type = dcl.type;
            }
            if (dcl.cppName == null || type.javaName.length() == 0 || dcl.parameters == null) {
                this.tokens.index = backIndex;
                return false;
            }
            int i;
            int namespace = dcl.cppName.lastIndexOf("::");
            if (context.namespace != null && namespace < 0) {
                dcl.cppName = context.namespace + "::" + dcl.cppName;
            }
            Info info = null;
            String fullname = dcl.cppName;
            String fullname2 = dcl.cppName;
            if (dcl.parameters != null) {
                fullname = fullname + "(";
                fullname2 = fullname2 + "(";
                String separator = "";
                for (Declarator d : dcl.parameters.declarators) {
                    if (d != null) {
                        String s = d.type.cppName;
                        String s2 = d.type.cppName;
                        if (d.type.constValue) {
                            if (!s.startsWith("const ")) {
                                s = "const " + s;
                            }
                        }
                        if (d.type.constPointer) {
                            if (!s.endsWith(" const")) {
                                s = s + " const";
                            }
                        }
                        if (d.indirections > 0) {
                            for (i = 0; i < d.indirections; i++) {
                                s = s + "*";
                                s2 = s2 + "*";
                            }
                        }
                        if (d.reference) {
                            s = s + "&";
                            s2 = s2 + "&";
                        }
                        fullname = fullname + separator + s;
                        fullname2 = fullname2 + separator + s2;
                        separator = ", ";
                    }
                }
                fullname = fullname + ")";
                info = this.infoMap.getFirst(fullname);
                if (info == null) {
                    info = this.infoMap.getFirst(fullname2 + ")");
                }
            }
            if (info == null) {
                info = this.infoMap.getFirst(dcl.cppName);
                if (!(type.constructor || type.destructor || type.operator)) {
                    Info cppNames;
                    InfoMap infoMap = this.infoMap;
                    if (info != null) {
                        cppNames = new Info(info).cppNames(new String[]{fullname});
                    } else {
                        cppNames = new Info(new String[]{fullname});
                    }
                    infoMap.put(cppNames);
                }
            }
            String localName = dcl.cppName;
            if (localName.startsWith(context.namespace + "::")) {
                localName = dcl.cppName.substring(context.namespace.length() + 2);
            }
            int localNamespace = 0;
            int templateCount = 0;
            i = 0;
            while (i < localName.length()) {
                int c = localName.charAt(i);
                if (c != 60) {
                    if (c != 62) {
                        if (templateCount == 0 && localName.substring(i).startsWith("::")) {
                            localNamespace = i;
                            break;
                        }
                    }
                    templateCount--;
                } else {
                    templateCount++;
                }
                i++;
            }
            if (type.friend || ((context.javaName == null && localNamespace > 0) || (info != null && info.skip))) {
                token = this.tokens.get();
                while (true) {
                    if (!token.match(Token.EOF) && attribute() != null) {
                        token = this.tokens.get();
                    }
                }
                if (this.tokens.get().match(Character.valueOf(':'))) {
                    token = this.tokens.next();
                    while (true) {
                        if (token.match(Token.EOF)) {
                            break;
                        }
                        if (token.match(Character.valueOf('{'), Character.valueOf(';'))) {
                            break;
                        }
                        token = this.tokens.next();
                    }
                }
                if (this.tokens.get().match(Character.valueOf('{'))) {
                    body();
                } else {
                    this.tokens.next();
                }
                decl.text = spacing;
                decl.function = true;
                declList.add(decl);
                return true;
            }
            if (type.staticMember || context.javaName == null) {
                modifiers = "public static native ";
                if (this.tokens.isCFile) {
                    modifiers = "@NoException " + modifiers;
                }
            }
            List<Declarator> prevDcl = new ArrayList();
            boolean first = true;
            int n = -2;
            while (n < Integer.MAX_VALUE) {
                decl = new Declaration();
                this.tokens.index = startIndex;
                if ((type.constructor || type.destructor || type.operator) && params != null) {
                    type = type(context);
                    params = parameters(context, n / 2, n % 2 != 0);
                    dcl = new Declarator();
                    dcl.type = type;
                    dcl.parameters = params;
                    dcl.cppName = type.cppName;
                    dcl.javaName = type.javaName.substring(type.javaName.lastIndexOf(32) + 1);
                    if (type.operator) {
                        dcl.cppName = "operator " + dcl.cppName;
                        dcl.javaName = "as" + Character.toUpperCase(dcl.javaName.charAt(0)) + dcl.javaName.substring(1);
                    }
                    dcl.signature = dcl.javaName + params.signature;
                    if (this.tokens.get().match(Character.valueOf(':'))) {
                        token = this.tokens.next();
                        while (true) {
                            if (token.match(Token.EOF)) {
                                break;
                            }
                            if (token.match(Character.valueOf('{'), Character.valueOf(';'))) {
                                break;
                            }
                            token = this.tokens.next();
                        }
                    }
                } else {
                    dcl = declarator(context, null, n / 2, n % 2 != 0, 0, false, false);
                    type = dcl.type;
                    namespace = dcl.cppName.lastIndexOf("::");
                    if (context.namespace != null && namespace < 0) {
                        dcl.cppName = context.namespace + "::" + dcl.cppName;
                    }
                }
                token = this.tokens.get();
                while (true) {
                    if (token.match(Token.EOF)) {
                        break;
                    }
                    decl.constMember |= token.match(Token.CONST, Token.CONSTEXPR);
                    if (attribute() == null) {
                        break;
                    }
                    token = this.tokens.get();
                }
                if (this.tokens.get().match(Character.valueOf('{'))) {
                    body();
                } else {
                    if (this.tokens.get().match(Character.valueOf('='))) {
                        token = this.tokens.next().expect("0", Token.DELETE, Token.DEFAULT);
                        if (token.match("0")) {
                            decl.abstractMember = true;
                        } else {
                            if (token.match(Token.DELETE)) {
                                decl.text = spacing;
                                declList.add(decl);
                                return true;
                            }
                        }
                        this.tokens.next().expect(Character.valueOf(';'));
                    }
                    this.tokens.next();
                }
                if (type.virtual && context.virtualize) {
                    String str;
                    StringBuilder append = new StringBuilder().append("@Virtual").append(decl.abstractMember ? "(true) " : " ");
                    if (context.inaccessible) {
                        str = "protected native ";
                    } else {
                        str = "public native ";
                    }
                    modifiers = append.append(str).toString();
                }
                decl.declarator = dcl;
                if (context.namespace != null && context.javaName == null) {
                    decl.text += "@Namespace(\"" + context.namespace + "\") ";
                }
                if (!type.constructor || params == null) {
                    decl.text += modifiers + type.annotations + context.shorten(type.javaName) + " " + dcl.javaName + dcl.parameters.list + ";\n";
                } else {
                    decl.text += "public " + context.shorten(context.javaName) + dcl.parameters.list + " { super((Pointer)null); allocate" + params.names + "; }\nprivate native " + type.annotations + "void allocate" + dcl.parameters.list + ";\n";
                }
                decl.signature = dcl.signature;
                if (!(info == null || info.javaText == null)) {
                    if (!first) {
                        break;
                    }
                    decl.text = info.javaText;
                }
                String comment = commentAfter();
                if (first) {
                    declList.spacing = spacing;
                    decl.text = comment + decl.text;
                }
                decl.function = true;
                boolean found = false;
                for (Declarator d2 : prevDcl) {
                    found |= dcl.signature.equals(d2.signature);
                }
                if (dcl.javaName.length() <= 0 || found || (type.destructor && (info == null || info.javaText == null))) {
                    if (found && n / 2 > 0 && n % 2 == 0 && n / 2 > Math.max(dcl.infoNumber, dcl.parameters.infoNumber)) {
                        break;
                    }
                }
                if (declList.add(decl)) {
                    first = false;
                }
                if (type.virtual && context.virtualize) {
                    break;
                }
                prevDcl.add(dcl);
                n++;
            }
            declList.spacing = null;
            return true;
        } else {
            if (this.tokens.get().match(Character.valueOf(':'))) {
                token = this.tokens.next();
                while (true) {
                    if (token.match(Token.EOF)) {
                        break;
                    }
                    if (token.match(Character.valueOf('{'), Character.valueOf(';'))) {
                        break;
                    }
                    token = this.tokens.next();
                }
            }
            if (this.tokens.get().match(Character.valueOf('{'))) {
                body();
            } else {
                this.tokens.next();
            }
            decl.text = spacing;
            decl.function = true;
            declList.add(decl);
            return true;
        }
    }

    boolean variable(Context context, DeclarationList declList) throws ParserException {
        int backIndex = this.tokens.index;
        String spacing = this.tokens.get().spacing;
        String modifiers = "public static native ";
        String setterType = "void ";
        Declarator dcl = declarator(context, null, 0, false, 0, false, true);
        Declaration decl = new Declaration();
        String cppName = dcl.cppName;
        if (dcl.javaName != null) {
            if (this.tokens.get().match(Character.valueOf('['), Character.valueOf('='), Character.valueOf(','), Character.valueOf(':'), Character.valueOf(';'))) {
                if (!(dcl.type.staticMember || context.javaName == null)) {
                    modifiers = "public native ";
                    setterType = context.shorten(context.javaName) + " ";
                }
                int namespace = cppName.lastIndexOf("::");
                if (context.namespace != null && namespace < 0) {
                    cppName = context.namespace + "::" + cppName;
                }
                Info info = this.infoMap.getFirst(cppName);
                if (info == null || !info.skip) {
                    if (info == null) {
                        Info cppNames;
                        Info info2 = this.infoMap.getFirst(dcl.cppName);
                        InfoMap infoMap = this.infoMap;
                        if (info2 != null) {
                            cppNames = new Info(info2).cppNames(new String[]{cppName});
                        } else {
                            cppNames = new Info(new String[]{cppName});
                        }
                        infoMap.put(cppNames);
                    }
                    boolean first = true;
                    Declarator metadcl = context.variable;
                    for (int n = 0; n < Integer.MAX_VALUE; n++) {
                        decl = new Declaration();
                        this.tokens.index = backIndex;
                        dcl = declarator(context, null, -1, false, n, false, true);
                        if (dcl == null || dcl.cppName == null) {
                            break;
                        }
                        String indices;
                        int i;
                        StringBuilder append;
                        String str;
                        decl.declarator = dcl;
                        cppName = dcl.cppName;
                        namespace = cppName.lastIndexOf("::");
                        if (context.namespace != null && namespace < 0) {
                            cppName = context.namespace + "::" + cppName;
                        }
                        info = this.infoMap.getFirst(cppName);
                        namespace = cppName.lastIndexOf("::");
                        String shortName = cppName;
                        if (namespace >= 0) {
                            shortName = cppName.substring(namespace + 2);
                        }
                        String javaName = dcl.javaName;
                        if (metadcl == null || metadcl.indices == 0 || dcl.indices == 0) {
                            indices = "";
                            i = 0;
                            while (true) {
                                int i2 = (metadcl == null || metadcl.indices == 0) ? dcl.indices : metadcl.indices;
                                if (i >= i2) {
                                    break;
                                }
                                if (i > 0) {
                                    indices = indices + ", ";
                                }
                                indices = indices + "int " + ((char) (i + 105));
                                i++;
                            }
                            if (context.namespace != null && context.javaName == null) {
                                decl.text += "@Namespace(\"" + context.namespace + "\") ";
                            }
                            if (metadcl != null && metadcl.cppName.length() > 0) {
                                append = new StringBuilder().append(decl.text);
                                if (metadcl.indices == 0) {
                                    str = "@Name(\"" + metadcl.cppName + "." + shortName + "\") ";
                                } else {
                                    str = "@Name({\"" + metadcl.cppName + "\", \"." + shortName + "\"}) ";
                                }
                                decl.text = append.append(str).toString();
                                dcl.type.annotations = dcl.type.annotations.replaceAll("@Name\\(.*\\) ", "");
                                javaName = metadcl.javaName + "_" + shortName;
                            }
                            if (dcl.type.constValue) {
                                decl.text += "@MemberGetter ";
                            }
                            decl.text += modifiers + dcl.type.annotations.replace("@ByVal ", "@ByRef ") + dcl.type.javaName + " " + javaName + "(" + indices + ");";
                            if (!dcl.type.constValue) {
                                if (indices.length() > 0) {
                                    indices = indices + ", ";
                                }
                                decl.text += " " + modifiers + setterType + javaName + "(" + indices + dcl.type.javaName + " " + javaName + ");";
                            }
                            decl.text += "\n";
                            if (dcl.type.constValue && dcl.type.staticMember && indices.length() == 0) {
                                String rawType = dcl.type.javaName.substring(dcl.type.javaName.lastIndexOf(32) + 1);
                                if ("byte".equals(rawType) || "short".equals(rawType) || "int".equals(rawType) || "long".equals(rawType) || "float".equals(rawType) || "double".equals(rawType) || "char".equals(rawType) || "boolean".equals(rawType)) {
                                    decl.text += "public static final " + rawType + " " + javaName + " = " + javaName + "();\n";
                                }
                            }
                        }
                        if (dcl.indices > 0) {
                            this.tokens.index = backIndex;
                            dcl = declarator(context, null, -1, false, n, true, false);
                            indices = "";
                            i = 0;
                            while (true) {
                                if (i >= (metadcl == null ? 0 : metadcl.indices)) {
                                    break;
                                }
                                if (i > 0) {
                                    indices = indices + ", ";
                                }
                                indices = indices + "int " + ((char) (i + 105));
                                i++;
                            }
                            if (context.namespace != null && context.javaName == null) {
                                decl.text += "@Namespace(\"" + context.namespace + "\") ";
                            }
                            if (metadcl != null && metadcl.cppName.length() > 0) {
                                append = new StringBuilder().append(decl.text);
                                if (metadcl.indices == 0) {
                                    str = "@Name(\"" + metadcl.cppName + "." + shortName + "\") ";
                                } else {
                                    str = "@Name({\"" + metadcl.cppName + "\", \"." + shortName + "\"}) ";
                                }
                                decl.text = append.append(str).toString();
                                dcl.type.annotations = dcl.type.annotations.replaceAll("@Name\\(.*\\) ", "");
                                javaName = metadcl.javaName + "_" + shortName;
                            }
                            decl.text += "@MemberGetter " + modifiers + dcl.type.annotations.replace("@ByVal ", "@ByRef ") + dcl.type.javaName + " " + javaName + "(" + indices + ");\n";
                        }
                        decl.signature = dcl.signature;
                        if (!(info == null || info.javaText == null)) {
                            decl.text = info.javaText;
                            decl.declarator = null;
                        }
                        while (true) {
                            if (this.tokens.get().match(Token.EOF, Character.valueOf(';'))) {
                                break;
                            }
                            this.tokens.next();
                        }
                        this.tokens.next();
                        String comment = commentAfter();
                        if (first) {
                            first = false;
                            declList.spacing = spacing;
                            decl.text = comment + decl.text;
                        }
                        decl.variable = true;
                        declList.add(decl);
                    }
                    declList.spacing = null;
                    return true;
                }
                decl.text = spacing;
                declList.add(decl);
                return true;
            }
        }
        this.tokens.index = backIndex;
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean macro(org.bytedeco.javacpp.tools.Context r43, org.bytedeco.javacpp.tools.DeclarationList r44) throws org.bytedeco.javacpp.tools.ParserException {
        /*
        r42 = this;
        r0 = r42;
        r2 = r0.tokens;
        r14 = r2.index;
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.get();
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r6 = 35;
        r6 = java.lang.Character.valueOf(r6);
        r3[r4] = r6;
        r2 = r2.match(r3);
        if (r2 != 0) goto L_0x0022;
    L_0x0020:
        r2 = 0;
    L_0x0021:
        return r2;
    L_0x0022:
        r0 = r42;
        r2 = r0.tokens;
        r3 = 1;
        r2.raw = r3;
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.get();
        r0 = r2.spacing;
        r36 = r0;
        r0 = r42;
        r2 = r0.tokens;
        r29 = r2.next();
        r0 = r42;
        r2 = r0.tokens;
        r2.next();
        r0 = r42;
        r2 = r0.tokens;
        r15 = r2.index;
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.get();
    L_0x0052:
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = org.bytedeco.javacpp.tools.Token.EOF;
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 != 0) goto L_0x006e;
    L_0x0062:
        r0 = r37;
        r2 = r0.spacing;
        r3 = 10;
        r2 = r2.indexOf(r3);
        if (r2 < 0) goto L_0x009b;
    L_0x006e:
        r0 = r42;
        r2 = r0.tokens;
        r0 = r2.index;
        r22 = r0;
    L_0x0076:
        r0 = r42;
        r2 = r0.tokens;
        r3 = -1;
        r2 = r2.get(r3);
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r6 = 4;
        r6 = java.lang.Integer.valueOf(r6);
        r3[r4] = r6;
        r2 = r2.match(r3);
        if (r2 == 0) goto L_0x00a4;
    L_0x0090:
        r0 = r42;
        r2 = r0.tokens;
        r3 = r2.index;
        r3 = r3 + -1;
        r2.index = r3;
        goto L_0x0076;
    L_0x009b:
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.next();
        goto L_0x0052;
    L_0x00a4:
        r0 = r42;
        r2 = r0.tokens;
        r0 = r2.index;
        r30 = r0;
        r21 = new org.bytedeco.javacpp.tools.Declaration;
        r21.<init>();
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = org.bytedeco.javacpp.tools.Token.DEFINE;
        r2[r3] = r4;
        r0 = r29;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x0127;
    L_0x00c1:
        r0 = r22;
        if (r15 >= r0) goto L_0x0127;
    L_0x00c5:
        r0 = r42;
        r2 = r0.tokens;
        r2.index = r15;
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.get();
        r0 = r2.value;
        r31 = r0;
        r0 = r42;
        r2 = r0.tokens;
        r23 = r2.next();
        r0 = r23;
        r2 = r0.spacing;
        r2 = r2.length();
        if (r2 != 0) goto L_0x01c0;
    L_0x00e9:
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 40;
        r4 = java.lang.Character.valueOf(r4);
        r2[r3] = r4;
        r0 = r23;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x01c0;
    L_0x00fd:
        r25 = 1;
    L_0x00ff:
        r0 = r42;
        r2 = r0.infoMap;
        r0 = r31;
        r28 = r2.get(r0);
        r2 = r28.size();
        if (r2 <= 0) goto L_0x01c4;
    L_0x010f:
        r41 = r28.iterator();
    L_0x0113:
        r2 = r41.hasNext();
        if (r2 == 0) goto L_0x0127;
    L_0x0119:
        r27 = r41.next();
        r27 = (org.bytedeco.javacpp.tools.Info) r27;
        if (r27 == 0) goto L_0x01d1;
    L_0x0121:
        r0 = r27;
        r2 = r0.skip;
        if (r2 == 0) goto L_0x01d1;
    L_0x0127:
        r0 = r21;
        r2 = r0.text;
        r2 = r2.length();
        if (r2 != 0) goto L_0x07aa;
    L_0x0131:
        r0 = r42;
        r2 = r0.tokens;
        r2.index = r15;
        r2 = 10;
        r0 = r36;
        r2 = r0.lastIndexOf(r2);
        r5 = r2 + 1;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r21;
        r3 = r0.text;
        r2 = r2.append(r3);
        r3 = "// ";
        r2 = r2.append(r3);
        r0 = r36;
        r3 = r0.substring(r5);
        r2 = r2.append(r3);
        r3 = "#";
        r2 = r2.append(r3);
        r0 = r29;
        r3 = r0.spacing;
        r2 = r2.append(r3);
        r0 = r29;
        r2 = r2.append(r0);
        r2 = r2.toString();
        r0 = r21;
        r0.text = r2;
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.get();
    L_0x0182:
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.index;
        r0 = r30;
        if (r2 >= r0) goto L_0x07a3;
    L_0x018c:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r21;
        r3 = r0.text;
        r3 = r2.append(r3);
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r4 = 0;
        r6 = "\n";
        r2[r4] = r6;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x0780;
    L_0x01a9:
        r2 = "\n// ";
    L_0x01ab:
        r2 = r3.append(r2);
        r2 = r2.toString();
        r0 = r21;
        r0.text = r2;
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.next();
        goto L_0x0182;
    L_0x01c0:
        r25 = 0;
        goto L_0x00ff;
    L_0x01c4:
        r2 = 1;
        r2 = new org.bytedeco.javacpp.tools.Info[r2];
        r3 = 0;
        r4 = 0;
        r2[r3] = r4;
        r28 = java.util.Arrays.asList(r2);
        goto L_0x010f;
    L_0x01d1:
        if (r27 != 0) goto L_0x01db;
    L_0x01d3:
        if (r25 != 0) goto L_0x01f0;
    L_0x01d5:
        r2 = r15 + 1;
        r0 = r22;
        if (r2 == r0) goto L_0x01f0;
    L_0x01db:
        if (r27 == 0) goto L_0x0270;
    L_0x01dd:
        r0 = r27;
        r2 = r0.cppText;
        if (r2 != 0) goto L_0x0270;
    L_0x01e3:
        r0 = r27;
        r2 = r0.cppTypes;
        if (r2 == 0) goto L_0x0270;
    L_0x01e9:
        r0 = r27;
        r2 = r0.cppTypes;
        r2 = r2.length;
        if (r2 != 0) goto L_0x0270;
    L_0x01f0:
        r2 = new org.bytedeco.javacpp.tools.Info;
        r3 = 1;
        r3 = new java.lang.String[r3];
        r4 = 0;
        r3[r4] = r31;
        r2.<init>(r3);
        r3 = "";
        r27 = r2.cppText(r3);
        r0 = r42;
        r2 = r0.tokens;
        r2.index = r14;
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.get();
    L_0x020f:
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.index;
        r0 = r22;
        if (r2 >= r0) goto L_0x0265;
    L_0x0219:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r27;
        r3 = r0.cppText;
        r2 = r2.append(r3);
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r6 = "\n";
        r3[r4] = r6;
        r0 = r37;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x024d;
    L_0x0236:
        r0 = r37;
        r2 = r2.append(r0);
        r2 = r2.toString();
        r0 = r27;
        r0.cppText = r2;
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.next();
        goto L_0x020f;
    L_0x024d:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r37;
        r4 = r0.spacing;
        r3 = r3.append(r4);
        r0 = r37;
        r3 = r3.append(r0);
        r37 = r3.toString();
        goto L_0x0236;
    L_0x0265:
        r0 = r42;
        r2 = r0.infoMap;
        r0 = r27;
        r2.putFirst(r0);
        goto L_0x0127;
    L_0x0270:
        if (r27 == 0) goto L_0x04ea;
    L_0x0272:
        r0 = r27;
        r2 = r0.cppText;
        if (r2 != 0) goto L_0x04ea;
    L_0x0278:
        r0 = r27;
        r2 = r0.cppTypes;
        if (r2 == 0) goto L_0x04ea;
    L_0x027e:
        r0 = r27;
        r2 = r0.cppTypes;
        r3 = r2.length;
        if (r25 == 0) goto L_0x0345;
    L_0x0285:
        r2 = 0;
    L_0x0286:
        if (r3 <= r2) goto L_0x04ea;
    L_0x0288:
        r34 = new java.util.ArrayList;
        r34.<init>();
        r5 = -1;
    L_0x028e:
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r5 >= r2) goto L_0x04d8;
    L_0x0293:
        r18 = 1;
        r0 = r42;
        r2 = r0.tokens;
        r3 = r15 + 2;
        r2.index = r3;
        r33 = "(";
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.get();
    L_0x02a7:
        if (r25 == 0) goto L_0x035c;
    L_0x02a9:
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.index;
        r0 = r30;
        if (r2 >= r0) goto L_0x035c;
    L_0x02b3:
        r0 = r27;
        r2 = r0.cppTypes;
        r2 = r2.length;
        r0 = r18;
        if (r0 >= r2) goto L_0x035c;
    L_0x02bc:
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 5;
        r4 = java.lang.Integer.valueOf(r4);
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x0348;
    L_0x02cf:
        r0 = r27;
        r2 = r0.cppTypes;
        r39 = r2[r18];
        r0 = r37;
        r0 = r0.value;
        r32 = r0;
        r2 = "...";
        r0 = r32;
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x02fa;
    L_0x02e5:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "arg";
        r2 = r2.append(r3);
        r0 = r18;
        r2 = r2.append(r0);
        r32 = r2.toString();
    L_0x02fa:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r33;
        r2 = r2.append(r0);
        r0 = r39;
        r2 = r2.append(r0);
        r3 = " ";
        r2 = r2.append(r3);
        r0 = r32;
        r2 = r2.append(r0);
        r33 = r2.toString();
        r18 = r18 + 1;
        r0 = r27;
        r2 = r0.cppTypes;
        r2 = r2.length;
        r0 = r18;
        if (r0 >= r2) goto L_0x033b;
    L_0x0326:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r33;
        r2 = r2.append(r0);
        r3 = ", ";
        r2 = r2.append(r3);
        r33 = r2.toString();
    L_0x033b:
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.next();
        goto L_0x02a7;
    L_0x0345:
        r2 = 1;
        goto L_0x0286;
    L_0x0348:
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 41;
        r4 = java.lang.Character.valueOf(r4);
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x033b;
    L_0x035c:
        r0 = r27;
        r2 = r0.cppTypes;
        r2 = r2.length;
        r0 = r18;
        if (r0 >= r2) goto L_0x03c2;
    L_0x0365:
        r0 = r27;
        r2 = r0.cppTypes;
        r39 = r2[r18];
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "arg";
        r2 = r2.append(r3);
        r0 = r18;
        r2 = r2.append(r0);
        r32 = r2.toString();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r33;
        r2 = r2.append(r0);
        r0 = r39;
        r2 = r2.append(r0);
        r3 = " ";
        r2 = r2.append(r3);
        r0 = r32;
        r2 = r2.append(r0);
        r33 = r2.toString();
        r18 = r18 + 1;
        r0 = r27;
        r2 = r0.cppTypes;
        r2 = r2.length;
        r0 = r18;
        if (r0 >= r2) goto L_0x035c;
    L_0x03ac:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r33;
        r2 = r2.append(r0);
        r3 = ", ";
        r2 = r2.append(r3);
        r33 = r2.toString();
        goto L_0x035c;
    L_0x03c2:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r33;
        r2 = r2.append(r0);
        r3 = ")";
        r2 = r2.append(r3);
        r33 = r2.toString();
        r2 = new org.bytedeco.javacpp.tools.Parser;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r27;
        r4 = r0.cppTypes;
        r6 = 0;
        r4 = r4[r6];
        r3 = r3.append(r4);
        r4 = " ";
        r3 = r3.append(r4);
        r0 = r31;
        r3 = r3.append(r0);
        r0 = r33;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r0 = r42;
        r2.<init>(r0, r3);
        r4 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r3 = r43;
        r20 = r2.declarator(r3, r4, r5, r6, r7, r8, r9);
        r26 = 0;
    L_0x0411:
        r0 = r27;
        r2 = r0.cppNames;
        r2 = r2.length;
        r0 = r26;
        if (r0 >= r2) goto L_0x0458;
    L_0x041a:
        r0 = r27;
        r2 = r0.cppNames;
        r2 = r2[r26];
        r0 = r31;
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x0479;
    L_0x0428:
        r0 = r27;
        r2 = r0.javaNames;
        if (r2 == 0) goto L_0x0479;
    L_0x042e:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "@Name(\"";
        r2 = r2.append(r3);
        r0 = r27;
        r3 = r0.cppNames;
        r4 = 0;
        r3 = r3[r4];
        r2 = r2.append(r3);
        r3 = "\") ";
        r2 = r2.append(r3);
        r0 = r27;
        r3 = r0.javaNames;
        r3 = r3[r26];
        r2 = r2.append(r3);
        r31 = r2.toString();
    L_0x0458:
        r24 = 0;
        r2 = r34.iterator();
    L_0x045e:
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x047c;
    L_0x0464:
        r19 = r2.next();
        r19 = (org.bytedeco.javacpp.tools.Declarator) r19;
        r0 = r20;
        r3 = r0.signature;
        r0 = r19;
        r4 = r0.signature;
        r3 = r3.equals(r4);
        r24 = r24 | r3;
        goto L_0x045e;
    L_0x0479:
        r26 = r26 + 1;
        goto L_0x0411;
    L_0x047c:
        if (r24 != 0) goto L_0x04d4;
    L_0x047e:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r21;
        r3 = r0.text;
        r2 = r2.append(r3);
        r3 = "public static native ";
        r2 = r2.append(r3);
        r0 = r20;
        r3 = r0.type;
        r3 = r3.annotations;
        r2 = r2.append(r3);
        r0 = r20;
        r3 = r0.type;
        r3 = r3.javaName;
        r2 = r2.append(r3);
        r3 = " ";
        r2 = r2.append(r3);
        r0 = r31;
        r2 = r2.append(r0);
        r0 = r20;
        r3 = r0.parameters;
        r3 = r3.list;
        r2 = r2.append(r3);
        r3 = ";\n";
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0 = r21;
        r0.text = r2;
    L_0x04c9:
        r0 = r34;
        r1 = r20;
        r0.add(r1);
        r5 = r5 + 1;
        goto L_0x028e;
    L_0x04d4:
        if (r24 == 0) goto L_0x04c9;
    L_0x04d6:
        if (r5 <= 0) goto L_0x04c9;
    L_0x04d8:
        if (r27 == 0) goto L_0x0113;
    L_0x04da:
        r0 = r27;
        r2 = r0.javaText;
        if (r2 == 0) goto L_0x0113;
    L_0x04e0:
        r0 = r27;
        r2 = r0.javaText;
        r0 = r21;
        r0.text = r2;
        goto L_0x0127;
    L_0x04ea:
        if (r27 == 0) goto L_0x0500;
    L_0x04ec:
        r0 = r27;
        r2 = r0.cppText;
        if (r2 != 0) goto L_0x04d8;
    L_0x04f2:
        r0 = r27;
        r2 = r0.cppTypes;
        if (r2 == 0) goto L_0x0500;
    L_0x04f8:
        r0 = r27;
        r2 = r0.cppTypes;
        r2 = r2.length;
        r3 = 1;
        if (r2 != r3) goto L_0x04d8;
    L_0x0500:
        r40 = "";
        r39 = "int";
        r16 = "";
        r0 = r42;
        r2 = r0.tokens;
        r3 = r15 + 1;
        r2.index = r3;
        r35 = new org.bytedeco.javacpp.tools.Token;
        r35.<init>();
        r38 = 1;
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.get();
    L_0x051d:
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.index;
        r0 = r30;
        if (r2 >= r0) goto L_0x053e;
    L_0x0527:
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 3;
        r4 = java.lang.Integer.valueOf(r4);
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x0620;
    L_0x053a:
        r39 = "String";
        r16 = " + ";
    L_0x053e:
        if (r27 == 0) goto L_0x05cc;
    L_0x0540:
        r0 = r27;
        r2 = r0.cppTypes;
        if (r2 == 0) goto L_0x057d;
    L_0x0546:
        r6 = new org.bytedeco.javacpp.tools.Parser;
        r0 = r27;
        r2 = r0.cppTypes;
        r3 = 0;
        r2 = r2[r3];
        r0 = r42;
        r6.<init>(r0, r2);
        r8 = 0;
        r9 = -1;
        r10 = 0;
        r11 = 0;
        r12 = 0;
        r13 = 1;
        r7 = r43;
        r20 = r6.declarator(r7, r8, r9, r10, r11, r12, r13);
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r20;
        r3 = r0.type;
        r3 = r3.annotations;
        r2 = r2.append(r3);
        r0 = r20;
        r3 = r0.type;
        r3 = r3.javaName;
        r2 = r2.append(r3);
        r39 = r2.toString();
    L_0x057d:
        r26 = 0;
    L_0x057f:
        r0 = r27;
        r2 = r0.cppNames;
        r2 = r2.length;
        r0 = r26;
        if (r0 >= r2) goto L_0x05c6;
    L_0x0588:
        r0 = r27;
        r2 = r0.cppNames;
        r2 = r2[r26];
        r0 = r31;
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x06c1;
    L_0x0596:
        r0 = r27;
        r2 = r0.javaNames;
        if (r2 == 0) goto L_0x06c1;
    L_0x059c:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "@Name(\"";
        r2 = r2.append(r3);
        r0 = r27;
        r3 = r0.cppNames;
        r4 = 0;
        r3 = r3[r4];
        r2 = r2.append(r3);
        r3 = "\") ";
        r2 = r2.append(r3);
        r0 = r27;
        r3 = r0.javaNames;
        r3 = r3[r26];
        r2 = r2.append(r3);
        r31 = r2.toString();
    L_0x05c6:
        r0 = r27;
        r0 = r0.translate;
        r38 = r0;
    L_0x05cc:
        r0 = r42;
        r2 = r0.tokens;
        r3 = r15 + 1;
        r2.index = r3;
        if (r38 == 0) goto L_0x0730;
    L_0x05d6:
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.get();
    L_0x05de:
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.index;
        r0 = r30;
        if (r2 >= r0) goto L_0x06c9;
    L_0x05e8:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r40;
        r2 = r2.append(r0);
        r0 = r37;
        r3 = r0.spacing;
        r2 = r2.append(r3);
        r0 = r37;
        r3 = r2.append(r0);
        r0 = r42;
        r2 = r0.tokens;
        r2 = r2.index;
        r2 = r2 + 1;
        r0 = r30;
        if (r2 >= r0) goto L_0x06c5;
    L_0x060d:
        r2 = r16;
    L_0x060f:
        r2 = r3.append(r2);
        r40 = r2.toString();
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.next();
        goto L_0x05de;
    L_0x0620:
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 2;
        r4 = java.lang.Integer.valueOf(r4);
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x0639;
    L_0x0633:
        r39 = "double";
        r16 = "";
        goto L_0x053e;
    L_0x0639:
        r2 = 1;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 1;
        r4 = java.lang.Integer.valueOf(r4);
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x065e;
    L_0x064c:
        r0 = r37;
        r2 = r0.value;
        r3 = "L";
        r2 = r2.endsWith(r3);
        if (r2 == 0) goto L_0x065e;
    L_0x0658:
        r39 = "long";
        r16 = "";
        goto L_0x053e;
    L_0x065e:
        r2 = 2;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 5;
        r4 = java.lang.Integer.valueOf(r4);
        r2[r3] = r4;
        r3 = 1;
        r4 = 62;
        r4 = java.lang.Character.valueOf(r4);
        r2[r3] = r4;
        r0 = r35;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x0696;
    L_0x067a:
        r2 = 2;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 5;
        r4 = java.lang.Integer.valueOf(r4);
        r2[r3] = r4;
        r3 = 1;
        r4 = 40;
        r4 = java.lang.Character.valueOf(r4);
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 != 0) goto L_0x06b3;
    L_0x0696:
        r2 = 2;
        r2 = new java.lang.Object[r2];
        r3 = 0;
        r4 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r4 = java.lang.Character.valueOf(r4);
        r2[r3] = r4;
        r3 = 1;
        r4 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r4 = java.lang.Character.valueOf(r4);
        r2[r3] = r4;
        r0 = r37;
        r2 = r0.match(r2);
        if (r2 == 0) goto L_0x06b5;
    L_0x06b3:
        r38 = 0;
    L_0x06b5:
        r35 = r37;
        r0 = r42;
        r2 = r0.tokens;
        r37 = r2.next();
        goto L_0x051d;
    L_0x06c1:
        r26 = r26 + 1;
        goto L_0x057f;
    L_0x06c5:
        r2 = "";
        goto L_0x060f;
    L_0x06c9:
        r0 = r42;
        r1 = r40;
        r40 = r0.translate(r1);
    L_0x06d1:
        r2 = 32;
        r0 = r39;
        r26 = r0.lastIndexOf(r2);
        if (r26 < 0) goto L_0x06e3;
    L_0x06db:
        r2 = r26 + 1;
        r0 = r39;
        r39 = r0.substring(r2);
    L_0x06e3:
        r2 = r40.length();
        if (r2 <= 0) goto L_0x0728;
    L_0x06e9:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r21;
        r3 = r0.text;
        r2 = r2.append(r3);
        r3 = "public static final ";
        r2 = r2.append(r3);
        r0 = r39;
        r2 = r2.append(r0);
        r3 = " ";
        r2 = r2.append(r3);
        r0 = r31;
        r2 = r2.append(r0);
        r3 = " =";
        r2 = r2.append(r3);
        r0 = r40;
        r2 = r2.append(r0);
        r3 = ";\n";
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0 = r21;
        r0.text = r2;
    L_0x0728:
        r0 = r31;
        r1 = r21;
        r1.signature = r0;
        goto L_0x04d8;
    L_0x0730:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r21;
        r3 = r0.text;
        r2 = r2.append(r3);
        r3 = "public static native @MemberGetter ";
        r2 = r2.append(r3);
        r0 = r39;
        r2 = r2.append(r0);
        r3 = " ";
        r2 = r2.append(r3);
        r0 = r31;
        r2 = r2.append(r0);
        r3 = "();\n";
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0 = r21;
        r0.text = r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = " ";
        r2 = r2.append(r3);
        r0 = r31;
        r2 = r2.append(r0);
        r3 = "()";
        r2 = r2.append(r3);
        r40 = r2.toString();
        goto L_0x06d1;
    L_0x0780:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r37;
        r4 = r0.spacing;
        r2 = r2.append(r4);
        r4 = r37.toString();
        r6 = "\n";
        r7 = "\n//";
        r4 = r4.replace(r6, r7);
        r2 = r2.append(r4);
        r2 = r2.toString();
        goto L_0x01ab;
    L_0x07a3:
        r2 = 0;
        r0 = r36;
        r36 = r0.substring(r2, r5);
    L_0x07aa:
        r0 = r21;
        r2 = r0.text;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x07db;
    L_0x07b4:
        r0 = r42;
        r2 = r0.tokens;
        r0 = r30;
        r2.index = r0;
        r17 = r42.commentAfter();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r17;
        r2 = r2.append(r0);
        r0 = r21;
        r3 = r0.text;
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0 = r21;
        r0.text = r2;
    L_0x07db:
        r0 = r42;
        r2 = r0.tokens;
        r3 = 0;
        r2.raw = r3;
        r0 = r36;
        r1 = r44;
        r1.spacing = r0;
        r0 = r44;
        r1 = r21;
        r0.add(r1);
        r2 = 0;
        r0 = r44;
        r0.spacing = r2;
        r2 = 1;
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Parser.macro(org.bytedeco.javacpp.tools.Context, org.bytedeco.javacpp.tools.DeclarationList):boolean");
    }

    boolean typedef(Context context, DeclarationList declList) throws ParserException {
        String spacing = this.tokens.get().spacing;
        if (!this.tokens.get().match(Token.TYPEDEF)) {
            return false;
        }
        Declaration decl = new Declaration();
        Declarator dcl = declarator(context, null, 0, false, 0, true, false);
        this.tokens.next();
        String typeName = dcl.type.cppName;
        String defName = dcl.cppName;
        int namespace = defName.lastIndexOf("::");
        if (context.namespace != null && namespace < 0) {
            defName = context.namespace + "::" + defName;
        }
        Info info = this.infoMap.getFirst(defName);
        if (dcl.definition != null) {
            decl = dcl.definition;
            if (dcl.javaName.length() > 0 && context.javaName != null) {
                dcl.javaName = context.javaName + "." + dcl.javaName;
            }
            if (info == null || !info.skip) {
                info = info != null ? new Info(info).cppNames(new String[]{defName}) : new Info(new String[]{defName});
                InfoMap infoMap = this.infoMap;
                Info valueTypes = info.valueTypes(new String[]{dcl.javaName});
                String[] strArr = new String[1];
                strArr[0] = (dcl.indirections > 0 ? "@ByPtrPtr " : "") + dcl.javaName;
                infoMap.put(valueTypes.pointerTypes(strArr));
            }
        } else {
            if (!typeName.equals("void")) {
                info = this.infoMap.getFirst(typeName);
                if (info == null || !info.skip) {
                    if (info != null) {
                        info = new Info(info).cppNames(new String[]{defName});
                    } else {
                        info = new Info(new String[]{defName});
                    }
                    if (info.cppTypes == null && info.annotations != null) {
                        String s = typeName;
                        if (dcl.type.constValue) {
                            if (!s.startsWith("const ")) {
                                s = "const " + s;
                            }
                        }
                        if (dcl.type.constPointer) {
                            if (!s.endsWith(" const")) {
                                s = s + " const";
                            }
                        }
                        if (dcl.type.indirections > 0) {
                            for (int i = 0; i < dcl.type.indirections; i++) {
                                s = s + "*";
                            }
                        }
                        if (dcl.type.reference) {
                            s = s + "&";
                        }
                        info.cppNames(new String[]{defName, s}).cppTypes(new String[]{s});
                    }
                    if (info.valueTypes == null && dcl.indirections > 0) {
                        info.valueTypes(info.pointerTypes != null ? info.pointerTypes : new String[]{typeName});
                        info.pointerTypes(new String[]{"PointerPointer"});
                    } else if (info.pointerTypes == null) {
                        info.pointerTypes(new String[]{typeName});
                    }
                    if (info.annotations == null) {
                        info.cast(!dcl.cppName.equals(info.pointerTypes[0]));
                    }
                    this.infoMap.put(info);
                }
            } else if (info == null || !info.skip) {
                if (dcl.indirections > 0) {
                    decl.text += "@Namespace @Name(\"void\") ";
                    info = info != null ? new Info(info).cppNames(new String[]{defName}) : new Info(new String[]{defName});
                    this.infoMap.put(info.valueTypes(new String[]{dcl.javaName}).pointerTypes(new String[]{"@ByPtrPtr " + dcl.javaName}));
                } else if (context.namespace != null && context.javaName == null) {
                    decl.text += "@Namespace(\"" + context.namespace + "\") ";
                }
                decl.text += "@Opaque public static class " + dcl.javaName + " extends Pointer {\n    /** Empty constructor. Calls {@code super((Pointer)null)}. */\n    public " + dcl.javaName + "() { super((Pointer)null); }\n    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */\n    public " + dcl.javaName + "(Pointer p) { super(p); }\n}";
            }
        }
        if (!(info == null || info.javaText == null)) {
            decl.text = info.javaText;
        }
        decl.text = commentAfter() + decl.text;
        declList.spacing = spacing;
        declList.add(decl);
        declList.spacing = null;
        return true;
    }

    boolean using(Context context, DeclarationList declList) throws ParserException {
        if (!this.tokens.get().match(Token.USING)) {
            return false;
        }
        String spacing = this.tokens.get().spacing;
        boolean namespace = this.tokens.get(1).match(Token.NAMESPACE);
        Declarator dcl = declarator(context, null, 0, false, 0, true, false);
        this.tokens.next();
        context.usingList.add(dcl.type.cppName + (namespace ? "::" : ""));
        Declaration decl = new Declaration();
        if (dcl.definition != null) {
            decl = dcl.definition;
        }
        decl.text = commentAfter() + decl.text;
        declList.spacing = spacing;
        declList.add(decl);
        declList.spacing = null;
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean group(org.bytedeco.javacpp.tools.Context r61, org.bytedeco.javacpp.tools.DeclarationList r62) throws org.bytedeco.javacpp.tools.ParserException {
        /*
        r60 = this;
        r0 = r60;
        r3 = r0.tokens;
        r14 = r3.index;
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r0 = r3.spacing;
        r50 = r0;
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = org.bytedeco.javacpp.tools.Token.TYPEDEF;
        r4[r5] = r6;
        r57 = r3.match(r4);
        r30 = 0;
        r31 = 0;
        r22 = new org.bytedeco.javacpp.tools.Context;
        r0 = r22;
        r1 = r61;
        r0.<init>(r1);
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.get();
    L_0x003b:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.EOF;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 != 0) goto L_0x0083;
    L_0x004b:
        r3 = 5;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.CLASS;
        r3[r4] = r5;
        r4 = 1;
        r5 = org.bytedeco.javacpp.tools.Token.INTERFACE;
        r3[r4] = r5;
        r4 = 2;
        r5 = org.bytedeco.javacpp.tools.Token.__INTERFACE;
        r3[r4] = r5;
        r4 = 3;
        r5 = org.bytedeco.javacpp.tools.Token.STRUCT;
        r3[r4] = r5;
        r4 = 4;
        r5 = org.bytedeco.javacpp.tools.Token.UNION;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x008d;
    L_0x006f:
        r30 = 1;
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.CLASS;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        r0 = r22;
        r0.inaccessible = r3;
    L_0x0083:
        if (r30 != 0) goto L_0x00bc;
    L_0x0085:
        r0 = r60;
        r3 = r0.tokens;
        r3.index = r14;
        r3 = 0;
    L_0x008c:
        return r3;
    L_0x008d:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.FRIEND;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x00a8;
    L_0x009d:
        r31 = 1;
    L_0x009f:
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.next();
        goto L_0x003b;
    L_0x00a8:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = 5;
        r5 = java.lang.Integer.valueOf(r5);
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 != 0) goto L_0x009f;
    L_0x00bb:
        goto L_0x0083;
    L_0x00bc:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.next();
        r4 = 3;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 5;
        r6 = java.lang.Integer.valueOf(r6);
        r4[r5] = r6;
        r5 = 1;
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r5 = 2;
        r6 = "::";
        r4[r5] = r6;
        r3.expect(r4);
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 != 0) goto L_0x0138;
    L_0x00fa:
        r0 = r60;
        r3 = r0.tokens;
        r4 = 1;
        r3 = r3.get(r4);
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 5;
        r6 = java.lang.Integer.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 == 0) goto L_0x0138;
    L_0x0114:
        if (r57 != 0) goto L_0x0131;
    L_0x0116:
        r0 = r60;
        r3 = r0.tokens;
        r4 = 2;
        r3 = r3.get(r4);
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 59;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 != 0) goto L_0x0138;
    L_0x0131:
        r0 = r60;
        r3 = r0.tokens;
        r3.next();
    L_0x0138:
        r56 = r60.type(r61);
        r17 = new java.util.ArrayList;
        r17.<init>();
        r25 = new org.bytedeco.javacpp.tools.Declaration;
        r25.<init>();
        r0 = r56;
        r3 = r0.annotations;
        r0 = r25;
        r0.text = r3;
        r0 = r56;
        r0 = r0.javaName;
        r41 = r0;
        if (r57 != 0) goto L_0x01bd;
    L_0x0156:
        r0 = r56;
        r3 = r0.cppName;
        r3 = r3.length();
        if (r3 != 0) goto L_0x01bd;
    L_0x0160:
        r13 = 1;
    L_0x0161:
        r28 = 0;
        r49 = 0;
        r0 = r56;
        r3 = r0.cppName;
        r3 = r3.length();
        if (r3 <= 0) goto L_0x0244;
    L_0x016f:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 58;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 == 0) goto L_0x0244;
    L_0x0189:
        r28 = 1;
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.next();
    L_0x0193:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.EOF;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 != 0) goto L_0x0244;
    L_0x01a3:
        r12 = 0;
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.VIRTUAL;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x01bf;
    L_0x01b4:
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.next();
        goto L_0x0193;
    L_0x01bd:
        r13 = 0;
        goto L_0x0161;
    L_0x01bf:
        r3 = 3;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.PRIVATE;
        r3[r4] = r5;
        r4 = 1;
        r5 = org.bytedeco.javacpp.tools.Token.PROTECTED;
        r3[r4] = r5;
        r4 = 2;
        r5 = org.bytedeco.javacpp.tools.Token.PUBLIC;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x01ee;
    L_0x01d9:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.PUBLIC;
        r3[r4] = r5;
        r0 = r55;
        r12 = r0.match(r3);
        r0 = r60;
        r3 = r0.tokens;
        r3.next();
    L_0x01ee:
        r53 = r60.type(r61);
        r0 = r60;
        r3 = r0.infoMap;
        r0 = r53;
        r4 = r0.cppName;
        r37 = r3.getFirst(r4);
        if (r37 == 0) goto L_0x0208;
    L_0x0200:
        r0 = r37;
        r3 = r0.skip;
        if (r3 == 0) goto L_0x0208;
    L_0x0206:
        r49 = 1;
    L_0x0208:
        if (r12 == 0) goto L_0x0211;
    L_0x020a:
        r0 = r17;
        r1 = r53;
        r0.add(r1);
    L_0x0211:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 44;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r5 = 1;
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.expect(r4);
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 == 0) goto L_0x01b4;
    L_0x0244:
        if (r57 == 0) goto L_0x0273;
    L_0x0246:
        r0 = r56;
        r3 = r0.indirections;
        if (r3 <= 0) goto L_0x0273;
    L_0x024c:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 59;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r5 = 1;
        r6 = org.bytedeco.javacpp.tools.Token.EOF;
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 != 0) goto L_0x0273;
    L_0x026b:
        r0 = r60;
        r3 = r0.tokens;
        r3.next();
        goto L_0x024c;
    L_0x0273:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r5 = 1;
        r6 = 59;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 != 0) goto L_0x029f;
    L_0x0296:
        r0 = r60;
        r3 = r0.tokens;
        r3.index = r14;
        r3 = 0;
        goto L_0x008c;
    L_0x029f:
        r0 = r60;
        r3 = r0.tokens;
        r0 = r3.index;
        r52 = r0;
        r59 = new java.util.ArrayList;
        r59.<init>();
        r0 = r56;
        r0 = r0.cppName;
        r45 = r0;
        r3 = r60.body();
        if (r3 == 0) goto L_0x031f;
    L_0x02b8:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 59;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 != 0) goto L_0x031f;
    L_0x02d2:
        if (r57 == 0) goto L_0x03ef;
    L_0x02d4:
        r48 = 0;
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.get();
    L_0x02de:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.EOF;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 != 0) goto L_0x031f;
    L_0x02ee:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = 59;
        r5 = java.lang.Character.valueOf(r5);
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x039f;
    L_0x0302:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r0 = r55;
        r4 = r0.spacing;
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x031f:
        r0 = r56;
        r3 = r0.cppName;
        r4 = "::";
        r42 = r3.lastIndexOf(r4);
        r0 = r61;
        r3 = r0.namespace;
        if (r3 == 0) goto L_0x0371;
    L_0x032f:
        if (r42 >= 0) goto L_0x0371;
    L_0x0331:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r61;
        r4 = r0.namespace;
        r3 = r3.append(r4);
        r4 = "::";
        r3 = r3.append(r4);
        r0 = r56;
        r4 = r0.cppName;
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r56;
        r0.cppName = r3;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r61;
        r4 = r0.namespace;
        r3 = r3.append(r4);
        r4 = "::";
        r3 = r3.append(r4);
        r0 = r45;
        r3 = r3.append(r0);
        r45 = r3.toString();
    L_0x0371:
        r0 = r60;
        r3 = r0.infoMap;
        r0 = r56;
        r4 = r0.cppName;
        r37 = r3.getFirst(r4);
        if (r37 == 0) goto L_0x0385;
    L_0x037f:
        r0 = r37;
        r3 = r0.base;
        if (r3 != 0) goto L_0x0387;
    L_0x0385:
        if (r49 != 0) goto L_0x038f;
    L_0x0387:
        if (r37 == 0) goto L_0x044b;
    L_0x0389:
        r0 = r37;
        r3 = r0.skip;
        if (r3 == 0) goto L_0x044b;
    L_0x038f:
        r3 = "";
        r0 = r25;
        r0.text = r3;
        r0 = r62;
        r1 = r25;
        r0.add(r1);
        r3 = 1;
        goto L_0x008c;
    L_0x039f:
        if (r48 == 0) goto L_0x03be;
    L_0x03a1:
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r5 = java.lang.Character.valueOf(r5);
        r3[r4] = r5;
        r4 = 1;
        r5 = 44;
        r5 = java.lang.Character.valueOf(r5);
        r3[r4] = r5;
        r0 = r48;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x03e3;
    L_0x03be:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = 5;
        r5 = java.lang.Integer.valueOf(r5);
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x03e3;
    L_0x03d1:
        r0 = r55;
        r0 = r0.value;
        r41 = r0;
        r0 = r41;
        r1 = r56;
        r1.cppName = r0;
        r0 = r41;
        r1 = r56;
        r1.javaName = r0;
    L_0x03e3:
        r48 = r55;
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.next();
        goto L_0x02de;
    L_0x03ef:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.index;
        r36 = r3 + -1;
        r8 = 0;
    L_0x03f8:
        r3 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r8 >= r3) goto L_0x0414;
    L_0x03fd:
        r0 = r60;
        r3 = r0.tokens;
        r0 = r36;
        r3.index = r0;
        r5 = 0;
        r6 = -1;
        r7 = 0;
        r9 = 0;
        r10 = 1;
        r3 = r60;
        r4 = r61;
        r24 = r3.declarator(r4, r5, r6, r7, r8, r9, r10);
        if (r24 != 0) goto L_0x0440;
    L_0x0414:
        r3 = 10;
        r0 = r50;
        r8 = r0.lastIndexOf(r3);
        if (r8 < 0) goto L_0x031f;
    L_0x041e:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = 0;
        r0 = r50;
        r4 = r0.substring(r4, r8);
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
        goto L_0x031f;
    L_0x0440:
        r13 = 1;
        r0 = r59;
        r1 = r24;
        r0.add(r1);
        r8 = r8 + 1;
        goto L_0x03f8;
    L_0x044b:
        if (r37 == 0) goto L_0x0530;
    L_0x044d:
        r0 = r37;
        r3 = r0.pointerTypes;
        if (r3 == 0) goto L_0x0530;
    L_0x0453:
        r0 = r37;
        r3 = r0.pointerTypes;
        r3 = r3.length;
        if (r3 <= 0) goto L_0x0530;
    L_0x045a:
        r0 = r37;
        r3 = r0.pointerTypes;
        r4 = 0;
        r3 = r3[r4];
        r0 = r56;
        r0.javaName = r3;
        r0 = r56;
        r3 = r0.javaName;
        r0 = r61;
        r41 = r0.shorten(r3);
    L_0x046f:
        r15 = new org.bytedeco.javacpp.tools.Type;
        r3 = "Pointer";
        r15.<init>(r3);
        r38 = r17.iterator();
    L_0x047a:
        r3 = r38.hasNext();
        if (r3 == 0) goto L_0x049f;
    L_0x0480:
        r43 = r38.next();
        r43 = (org.bytedeco.javacpp.tools.Type) r43;
        r0 = r60;
        r3 = r0.infoMap;
        r0 = r43;
        r4 = r0.cppName;
        r44 = r3.getFirst(r4);
        if (r44 == 0) goto L_0x049a;
    L_0x0494:
        r0 = r44;
        r3 = r0.flatten;
        if (r3 != 0) goto L_0x047a;
    L_0x049a:
        r15 = r43;
        r38.remove();
    L_0x049f:
        r20 = "";
        r3 = r17.size();
        if (r3 <= 0) goto L_0x058d;
    L_0x04a7:
        r3 = r17.iterator();
    L_0x04ab:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x058d;
    L_0x04b1:
        r53 = r3.next();
        r53 = (org.bytedeco.javacpp.tools.Type) r53;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r20;
        r4 = r4.append(r0);
        r5 = "    public ";
        r4 = r4.append(r5);
        r0 = r53;
        r5 = r0.javaName;
        r4 = r4.append(r5);
        r5 = " as";
        r4 = r4.append(r5);
        r0 = r53;
        r5 = r0.javaName;
        r4 = r4.append(r5);
        r5 = "() { return as";
        r4 = r4.append(r5);
        r0 = r53;
        r5 = r0.javaName;
        r4 = r4.append(r5);
        r5 = "(this); }\n    @Namespace public static native @Name(\"static_cast<";
        r4 = r4.append(r5);
        r0 = r53;
        r5 = r0.cppName;
        r4 = r4.append(r5);
        r5 = "*>\") ";
        r4 = r4.append(r5);
        r0 = r53;
        r5 = r0.javaName;
        r4 = r4.append(r5);
        r5 = " as";
        r4 = r4.append(r5);
        r0 = r53;
        r5 = r0.javaName;
        r4 = r4.append(r5);
        r5 = "(";
        r4 = r4.append(r5);
        r0 = r56;
        r5 = r0.javaName;
        r4 = r4.append(r5);
        r5 = " pointer);\n";
        r4 = r4.append(r5);
        r20 = r4.toString();
        goto L_0x04ab;
    L_0x0530:
        if (r37 != 0) goto L_0x046f;
    L_0x0532:
        r0 = r56;
        r3 = r0.javaName;
        r3 = r3.length();
        if (r3 <= 0) goto L_0x0565;
    L_0x053c:
        r0 = r61;
        r3 = r0.javaName;
        if (r3 == 0) goto L_0x0565;
    L_0x0542:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r61;
        r4 = r0.javaName;
        r3 = r3.append(r4);
        r4 = ".";
        r3 = r3.append(r4);
        r0 = r56;
        r4 = r0.javaName;
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r56;
        r0.javaName = r3;
    L_0x0565:
        r0 = r60;
        r3 = r0.infoMap;
        r4 = new org.bytedeco.javacpp.tools.Info;
        r5 = 1;
        r5 = new java.lang.String[r5];
        r6 = 0;
        r0 = r56;
        r7 = r0.cppName;
        r5[r6] = r7;
        r4.<init>(r5);
        r5 = 1;
        r5 = new java.lang.String[r5];
        r6 = 0;
        r0 = r56;
        r7 = r0.javaName;
        r5[r6] = r7;
        r37 = r4.pointerTypes(r5);
        r0 = r37;
        r3.put(r0);
        goto L_0x046f;
    L_0x058d:
        r0 = r56;
        r3 = r0.javaName;
        r0 = r25;
        r0.signature = r3;
        r0 = r60;
        r3 = r0.tokens;
        r0 = r52;
        r3.index = r0;
        r3 = r41.length();
        if (r3 <= 0) goto L_0x0715;
    L_0x05a3:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 59;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 == 0) goto L_0x0715;
    L_0x05bd:
        r0 = r60;
        r3 = r0.tokens;
        r3.next();
        if (r31 == 0) goto L_0x05d6;
    L_0x05c6:
        r3 = "";
        r0 = r25;
        r0.text = r3;
        r0 = r62;
        r1 = r25;
        r0.add(r1);
        r3 = 1;
        goto L_0x008c;
    L_0x05d6:
        if (r37 == 0) goto L_0x05e4;
    L_0x05d8:
        r0 = r37;
        r3 = r0.base;
        if (r3 == 0) goto L_0x05e4;
    L_0x05de:
        r0 = r37;
        r3 = r0.base;
        r15.javaName = r3;
    L_0x05e4:
        r0 = r61;
        r3 = r0.namespace;
        if (r3 == 0) goto L_0x06ce;
    L_0x05ea:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r61;
        r4 = r0.namespace;
        r3 = r3.append(r4);
        r4 = "::";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r32 = r3.toString();
    L_0x0607:
        r0 = r56;
        r3 = r0.cppName;
        r0 = r32;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x06de;
    L_0x0613:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "@Name(\"";
        r4 = r3.append(r4);
        r0 = r61;
        r3 = r0.javaName;
        if (r3 == 0) goto L_0x062e;
    L_0x062c:
        if (r42 >= 0) goto L_0x06d2;
    L_0x062e:
        r0 = r56;
        r3 = r0.cppName;
    L_0x0632:
        r3 = r4.append(r3);
        r4 = "\") ";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x0644:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "@Opaque public static class ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = " extends ";
        r3 = r3.append(r4);
        r4 = r15.javaName;
        r3 = r3.append(r4);
        r4 = " {\n    /** Empty constructor. Calls {@code super((Pointer)null)}. */\n    public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = "() { super((Pointer)null); }\n    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */\n    public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = "(Pointer p) { super(p); }\n}";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
        r0 = r56;
        r1 = r25;
        r1.type = r0;
        r3 = 1;
        r0 = r25;
        r0.incomplete = r3;
        r21 = r60.commentAfter();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r21;
        r3 = r3.append(r0);
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
        r0 = r50;
        r1 = r62;
        r1.spacing = r0;
        r0 = r62;
        r1 = r25;
        r0.add(r1);
        r3 = 0;
        r0 = r62;
        r0.spacing = r3;
        r3 = 1;
        goto L_0x008c;
    L_0x06ce:
        r32 = r41;
        goto L_0x0607;
    L_0x06d2:
        r0 = r56;
        r3 = r0.cppName;
        r5 = r42 + 2;
        r3 = r3.substring(r5);
        goto L_0x0632;
    L_0x06de:
        r0 = r61;
        r3 = r0.namespace;
        if (r3 == 0) goto L_0x0644;
    L_0x06e4:
        r0 = r61;
        r3 = r0.javaName;
        if (r3 != 0) goto L_0x0644;
    L_0x06ea:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "@Namespace(\"";
        r3 = r3.append(r4);
        r0 = r61;
        r4 = r0.namespace;
        r3 = r3.append(r4);
        r4 = "\") ";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
        goto L_0x0644;
    L_0x0715:
        r0 = r60;
        r3 = r0.tokens;
        r3 = r3.get();
        r4 = 1;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r6 = java.lang.Character.valueOf(r6);
        r4[r5] = r6;
        r3 = r3.match(r4);
        if (r3 == 0) goto L_0x0736;
    L_0x072f:
        r0 = r60;
        r3 = r0.tokens;
        r3.next();
    L_0x0736:
        r0 = r56;
        r3 = r0.cppName;
        r3 = r3.length();
        if (r3 <= 0) goto L_0x074e;
    L_0x0740:
        r0 = r56;
        r3 = r0.cppName;
        r0 = r22;
        r0.namespace = r3;
        r0 = r45;
        r1 = r22;
        r1.cppName = r0;
    L_0x074e:
        if (r13 != 0) goto L_0x0758;
    L_0x0750:
        r0 = r56;
        r3 = r0.javaName;
        r0 = r22;
        r0.javaName = r3;
    L_0x0758:
        if (r37 == 0) goto L_0x0765;
    L_0x075a:
        r0 = r37;
        r3 = r0.virtualize;
        if (r3 == 0) goto L_0x0765;
    L_0x0760:
        r3 = 1;
        r0 = r22;
        r0.virtualize = r3;
    L_0x0765:
        r26 = new org.bytedeco.javacpp.tools.DeclarationList;
        r26.<init>();
        r3 = r59.size();
        if (r3 != 0) goto L_0x0845;
    L_0x0770:
        r0 = r60;
        r1 = r22;
        r2 = r26;
        r0.declarations(r1, r2);
    L_0x0779:
        r40 = "public static ";
        r35 = 1;
        r27 = 0;
        r39 = 0;
        r47 = 0;
        if (r37 == 0) goto L_0x08b5;
    L_0x0785:
        r0 = r37;
        r3 = r0.purify;
        if (r3 == 0) goto L_0x08b5;
    L_0x078b:
        r0 = r22;
        r3 = r0.virtualize;
        if (r3 != 0) goto L_0x08b5;
    L_0x0791:
        r11 = 1;
    L_0x0792:
        r33 = 0;
        r34 = 0;
        r4 = r26.iterator();
    L_0x079a:
        r3 = r4.hasNext();
        if (r3 == 0) goto L_0x08c4;
    L_0x07a0:
        r23 = r4.next();
        r23 = (org.bytedeco.javacpp.tools.Declaration) r23;
        r0 = r23;
        r3 = r0.declarator;
        if (r3 == 0) goto L_0x0829;
    L_0x07ac:
        r0 = r23;
        r3 = r0.declarator;
        r3 = r3.type;
        if (r3 == 0) goto L_0x0829;
    L_0x07b4:
        r0 = r23;
        r3 = r0.declarator;
        r3 = r3.type;
        r3 = r3.constructor;
        if (r3 == 0) goto L_0x0829;
    L_0x07be:
        r35 = 0;
        r0 = r23;
        r3 = r0.declarator;
        r3 = r3.parameters;
        r0 = r3.declarators;
        r46 = r0;
        r0 = r46;
        r3 = r0.length;
        if (r3 == 0) goto L_0x07e4;
    L_0x07cf:
        r0 = r46;
        r3 = r0.length;
        r5 = 1;
        if (r3 != r5) goto L_0x08b8;
    L_0x07d5:
        r3 = 0;
        r3 = r46[r3];
        r3 = r3.type;
        r3 = r3.javaName;
        r5 = "void";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x08b8;
    L_0x07e4:
        r0 = r23;
        r3 = r0.inaccessible;
        if (r3 != 0) goto L_0x08b8;
    L_0x07ea:
        r3 = 1;
    L_0x07eb:
        r27 = r27 | r3;
        r0 = r46;
        r3 = r0.length;
        r5 = 1;
        if (r3 != r5) goto L_0x08bb;
    L_0x07f3:
        r3 = 0;
        r3 = r46[r3];
        r3 = r3.type;
        r3 = r3.javaName;
        r5 = "long";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x08bb;
    L_0x0802:
        r0 = r23;
        r3 = r0.inaccessible;
        if (r3 != 0) goto L_0x08bb;
    L_0x0808:
        r3 = 1;
    L_0x0809:
        r39 = r39 | r3;
        r0 = r46;
        r3 = r0.length;
        r5 = 1;
        if (r3 != r5) goto L_0x08be;
    L_0x0811:
        r3 = 0;
        r3 = r46[r3];
        r3 = r3.type;
        r3 = r3.javaName;
        r5 = "Pointer";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x08be;
    L_0x0820:
        r0 = r23;
        r3 = r0.inaccessible;
        if (r3 != 0) goto L_0x08be;
    L_0x0826:
        r3 = 1;
    L_0x0827:
        r47 = r47 | r3;
    L_0x0829:
        r0 = r23;
        r3 = r0.abstractMember;
        r11 = r11 | r3;
        r0 = r23;
        r3 = r0.constMember;
        if (r3 == 0) goto L_0x08c1;
    L_0x0834:
        r0 = r23;
        r3 = r0.abstractMember;
        if (r3 == 0) goto L_0x08c1;
    L_0x083a:
        r3 = 1;
    L_0x083b:
        r33 = r33 | r3;
        r0 = r23;
        r3 = r0.variable;
        r34 = r34 | r3;
        goto L_0x079a;
    L_0x0845:
        r3 = r59.iterator();
    L_0x0849:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x0779;
    L_0x084f:
        r58 = r3.next();
        r58 = (org.bytedeco.javacpp.tools.Declarator) r58;
        r0 = r61;
        r4 = r0.variable;
        if (r4 == 0) goto L_0x08a5;
    L_0x085b:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r61;
        r5 = r0.variable;
        r5 = r5.cppName;
        r4 = r4.append(r5);
        r5 = ".";
        r4 = r4.append(r5);
        r0 = r58;
        r5 = r0.cppName;
        r4 = r4.append(r5);
        r4 = r4.toString();
        r0 = r58;
        r0.cppName = r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r61;
        r5 = r0.variable;
        r5 = r5.javaName;
        r4 = r4.append(r5);
        r5 = "_";
        r4 = r4.append(r5);
        r0 = r58;
        r5 = r0.javaName;
        r4 = r4.append(r5);
        r4 = r4.toString();
        r0 = r58;
        r0.javaName = r4;
    L_0x08a5:
        r0 = r58;
        r1 = r22;
        r1.variable = r0;
        r0 = r60;
        r1 = r22;
        r2 = r26;
        r0.declarations(r1, r2);
        goto L_0x0849;
    L_0x08b5:
        r11 = 0;
        goto L_0x0792;
    L_0x08b8:
        r3 = 0;
        goto L_0x07eb;
    L_0x08bb:
        r3 = 0;
        goto L_0x0809;
    L_0x08be:
        r3 = 0;
        goto L_0x0827;
    L_0x08c1:
        r3 = 0;
        goto L_0x083b;
    L_0x08c4:
        if (r33 == 0) goto L_0x08e1;
    L_0x08c6:
        r0 = r22;
        r3 = r0.virtualize;
        if (r3 == 0) goto L_0x08e1;
    L_0x08cc:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "@Const ";
        r3 = r3.append(r4);
        r0 = r40;
        r3 = r3.append(r0);
        r40 = r3.toString();
    L_0x08e1:
        if (r13 != 0) goto L_0x0a36;
    L_0x08e3:
        r0 = r61;
        r3 = r0.namespace;
        if (r3 == 0) goto L_0x0a85;
    L_0x08e9:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r61;
        r4 = r0.namespace;
        r3 = r3.append(r4);
        r4 = "::";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r32 = r3.toString();
    L_0x0906:
        r0 = r56;
        r3 = r0.cppName;
        r0 = r32;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x0a89;
    L_0x0912:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "@Name(\"";
        r3 = r3.append(r4);
        r0 = r56;
        r4 = r0.cppName;
        r3 = r3.append(r4);
        r4 = "\") ";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x093b:
        if (r35 == 0) goto L_0x093f;
    L_0x093d:
        if (r28 == 0) goto L_0x095c;
    L_0x093f:
        if (r34 == 0) goto L_0x095c;
    L_0x0941:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "@NoOffset ";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x095c:
        if (r37 == 0) goto L_0x096a;
    L_0x095e:
        r0 = r37;
        r3 = r0.base;
        if (r3 == 0) goto L_0x096a;
    L_0x0964:
        r0 = r37;
        r3 = r0.base;
        r15.javaName = r3;
    L_0x096a:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r0 = r40;
        r3 = r3.append(r0);
        r4 = "class ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = " extends ";
        r3 = r3.append(r4);
        r4 = r15.javaName;
        r3 = r3.append(r4);
        r4 = " {\n    static { Loader.load(); }\n";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
        if (r35 == 0) goto L_0x0ac0;
    L_0x09a5:
        if (r11 == 0) goto L_0x09ad;
    L_0x09a7:
        r0 = r22;
        r3 = r0.virtualize;
        if (r3 == 0) goto L_0x0ac0;
    L_0x09ad:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "    /** Default native constructor. */\n    public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = "() { super((Pointer)null); allocate(); }\n    /** Native array allocator. Access with {@link Pointer#position(long)}. */\n    public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = "(long size) { super((Pointer)null); allocateArray(size); }\n    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */\n    public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = "(Pointer p) { super(p); }\n    private native void allocate();\n    private native void allocateArray(long size);\n    @Override public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = " position(long position) {\n        return (";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = ")super.position(position);\n    }\n";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x0a04:
        r0 = r50;
        r1 = r62;
        r1.spacing = r0;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r0 = r20;
        r3 = r3.append(r0);
        r4 = "\n";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r62;
        r3 = r0.rescan(r3);
        r0 = r25;
        r0.text = r3;
        r3 = 0;
        r0 = r62;
        r0.spacing = r3;
    L_0x0a36:
        r3 = r17.iterator();
    L_0x0a3a:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x0baa;
    L_0x0a40:
        r16 = r3.next();
        r16 = (org.bytedeco.javacpp.tools.Type) r16;
        r0 = r60;
        r4 = r0.infoMap;
        r0 = r16;
        r5 = r0.cppName;
        r18 = r4.getFirst(r5);
        if (r18 == 0) goto L_0x0a3a;
    L_0x0a54:
        r0 = r18;
        r4 = r0.flatten;
        if (r4 == 0) goto L_0x0a3a;
    L_0x0a5a:
        r0 = r18;
        r4 = r0.javaText;
        if (r4 == 0) goto L_0x0a3a;
    L_0x0a60:
        r0 = r18;
        r0 = r0.javaText;
        r54 = r0;
        r4 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0 = r54;
        r51 = r0.indexOf(r4);
        r8 = 0;
    L_0x0a6f:
        r4 = 2;
        if (r8 >= r4) goto L_0x0b3f;
    L_0x0a72:
        r0 = r54;
        r1 = r51;
        r19 = r0.charAt(r1);
        r4 = 10;
        r0 = r19;
        if (r0 != r4) goto L_0x0b36;
    L_0x0a80:
        r8 = r8 + 1;
    L_0x0a82:
        r51 = r51 + 1;
        goto L_0x0a6f;
    L_0x0a85:
        r32 = r41;
        goto L_0x0906;
    L_0x0a89:
        r0 = r61;
        r3 = r0.namespace;
        if (r3 == 0) goto L_0x093b;
    L_0x0a8f:
        r0 = r61;
        r3 = r0.javaName;
        if (r3 != 0) goto L_0x093b;
    L_0x0a95:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "@Namespace(\"";
        r3 = r3.append(r4);
        r0 = r61;
        r4 = r0.namespace;
        r3 = r3.append(r4);
        r4 = "\") ";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
        goto L_0x093b;
    L_0x0ac0:
        if (r47 != 0) goto L_0x0ae9;
    L_0x0ac2:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */\n    public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = "(Pointer p) { super(p); }\n";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x0ae9:
        if (r27 == 0) goto L_0x0a04;
    L_0x0aeb:
        if (r11 == 0) goto L_0x0af3;
    L_0x0aed:
        r0 = r22;
        r3 = r0.virtualize;
        if (r3 == 0) goto L_0x0a04;
    L_0x0af3:
        if (r39 != 0) goto L_0x0a04;
    L_0x0af5:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r4 = "    /** Native array allocator. Access with {@link Pointer#position(long)}. */\n    public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = "(long size) { super((Pointer)null); allocateArray(size); }\n    private native void allocateArray(long size);\n    @Override public ";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = " position(long position) {\n        return (";
        r3 = r3.append(r4);
        r0 = r41;
        r3 = r3.append(r0);
        r4 = ")super.position(position);\n    }\n";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
        goto L_0x0a04;
    L_0x0b36:
        r4 = java.lang.Character.isWhitespace(r19);
        if (r4 != 0) goto L_0x0a82;
    L_0x0b3c:
        r8 = 0;
        goto L_0x0a82;
    L_0x0b3f:
        r4 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r0 = r54;
        r29 = r0.lastIndexOf(r4);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r25;
        r5 = r0.text;
        r4 = r4.append(r5);
        r0 = r54;
        r1 = r51;
        r2 = r29;
        r5 = r0.substring(r1, r2);
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "(\\s+)";
        r6 = r6.append(r7);
        r0 = r16;
        r7 = r0.javaName;
        r6 = r6.append(r7);
        r7 = "(\\s+)";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r9 = "$1";
        r7 = r7.append(r9);
        r0 = r56;
        r9 = r0.javaName;
        r7 = r7.append(r9);
        r9 = "$2";
        r7 = r7.append(r9);
        r7 = r7.toString();
        r5 = r5.replaceAll(r6, r7);
        r4 = r4.append(r5);
        r4 = r4.toString();
        r0 = r25;
        r0.text = r4;
        goto L_0x0a3a;
    L_0x0baa:
        r3 = r26.iterator();
    L_0x0bae:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x0bf8;
    L_0x0bb4:
        r23 = r3.next();
        r23 = (org.bytedeco.javacpp.tools.Declaration) r23;
        r0 = r23;
        r4 = r0.inaccessible;
        if (r4 != 0) goto L_0x0bae;
    L_0x0bc0:
        r0 = r23;
        r4 = r0.declarator;
        if (r4 == 0) goto L_0x0bda;
    L_0x0bc6:
        r0 = r23;
        r4 = r0.declarator;
        r4 = r4.type;
        if (r4 == 0) goto L_0x0bda;
    L_0x0bce:
        r0 = r23;
        r4 = r0.declarator;
        r4 = r4.type;
        r4 = r4.constructor;
        if (r4 == 0) goto L_0x0bda;
    L_0x0bd8:
        if (r11 != 0) goto L_0x0bae;
    L_0x0bda:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r25;
        r5 = r0.text;
        r4 = r4.append(r5);
        r0 = r23;
        r5 = r0.text;
        r4 = r4.append(r5);
        r4 = r4.toString();
        r0 = r25;
        r0.text = r4;
        goto L_0x0bae;
    L_0x0bf8:
        if (r13 != 0) goto L_0x0c23;
    L_0x0bfa:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r0 = r60;
        r4 = r0.tokens;
        r4 = r4.get();
        r4 = r4.spacing;
        r3 = r3.append(r4);
        r4 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x0c23:
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.next();
    L_0x0c2b:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = org.bytedeco.javacpp.tools.Token.EOF;
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 != 0) goto L_0x0c6c;
    L_0x0c3b:
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r4 = 0;
        r5 = 59;
        r5 = java.lang.Character.valueOf(r5);
        r3[r4] = r5;
        r0 = r55;
        r3 = r0.match(r3);
        if (r3 == 0) goto L_0x0c93;
    L_0x0c4f:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r25;
        r4 = r0.text;
        r3 = r3.append(r4);
        r0 = r55;
        r4 = r0.spacing;
        r3 = r3.append(r4);
        r3 = r3.toString();
        r0 = r25;
        r0.text = r3;
    L_0x0c6c:
        r0 = r60;
        r3 = r0.tokens;
        r3.next();
        r0 = r56;
        r1 = r25;
        r1.type = r0;
        if (r37 == 0) goto L_0x0c9c;
    L_0x0c7b:
        r0 = r37;
        r3 = r0.javaText;
        if (r3 == 0) goto L_0x0c9c;
    L_0x0c81:
        r0 = r37;
        r3 = r0.javaText;
        r0 = r25;
        r0.text = r3;
    L_0x0c89:
        r0 = r62;
        r1 = r25;
        r0.add(r1);
        r3 = 1;
        goto L_0x008c;
    L_0x0c93:
        r0 = r60;
        r3 = r0.tokens;
        r55 = r3.next();
        goto L_0x0c2b;
    L_0x0c9c:
        if (r37 == 0) goto L_0x0c89;
    L_0x0c9e:
        r0 = r37;
        r3 = r0.flatten;
        if (r3 == 0) goto L_0x0c89;
    L_0x0ca4:
        r0 = r25;
        r3 = r0.text;
        r0 = r37;
        r0.javaText = r3;
        goto L_0x0c89;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Parser.group(org.bytedeco.javacpp.tools.Context, org.bytedeco.javacpp.tools.DeclarationList):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean enumeration(org.bytedeco.javacpp.tools.Context r39, org.bytedeco.javacpp.tools.DeclarationList r40) throws org.bytedeco.javacpp.tools.ParserException {
        /*
        r38 = this;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r0 = r34;
        r2 = r0.index;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34 = r34.get();
        r0 = r34;
        r12 = r0.spacing;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34 = r34.get();
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = org.bytedeco.javacpp.tools.Token.TYPEDEF;
        r35[r36] = r37;
        r33 = r34.match(r35);
        r17 = 0;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.get();
    L_0x0040:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = org.bytedeco.javacpp.tools.Token.EOF;
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 != 0) goto L_0x0072;
    L_0x0058:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = org.bytedeco.javacpp.tools.Token.ENUM;
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x0081;
    L_0x0070:
        r17 = 1;
    L_0x0072:
        if (r17 != 0) goto L_0x00a8;
    L_0x0074:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r0 = r34;
        r0.index = r2;
        r34 = 0;
    L_0x0080:
        return r34;
    L_0x0081:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 5;
        r36 = java.lang.Integer.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x0072;
    L_0x009d:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
        goto L_0x0040;
    L_0x00a8:
        r13 = "enum";
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r35 = 1;
        r34 = r34.get(r35);
        r35 = 2;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = org.bytedeco.javacpp.tools.Token.CLASS;
        r35[r36] = r37;
        r36 = 1;
        r37 = org.bytedeco.javacpp.tools.Token.STRUCT;
        r35[r36] = r37;
        r34 = r34.match(r35);
        if (r34 == 0) goto L_0x00f3;
    L_0x00d0:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r34 = r0.append(r13);
        r35 = " ";
        r34 = r34.append(r35);
        r0 = r38;
        r0 = r0.tokens;
        r35 = r0;
        r35 = r35.next();
        r34 = r34.append(r35);
        r13 = r34.toString();
    L_0x00f3:
        if (r33 == 0) goto L_0x0146;
    L_0x00f5:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r35 = 1;
        r34 = r34.get(r35);
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r37 = java.lang.Character.valueOf(r37);
        r35[r36] = r37;
        r34 = r34.match(r35);
        if (r34 != 0) goto L_0x0146;
    L_0x0119:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r35 = 2;
        r34 = r34.get(r35);
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = 5;
        r37 = java.lang.Integer.valueOf(r37);
        r35[r36] = r37;
        r34 = r34.match(r35);
        if (r34 == 0) goto L_0x0146;
    L_0x013d:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34.next();
    L_0x0146:
        r4 = 0;
        r22 = 0;
        r8 = "int";
        r21 = "int";
        r27 = "";
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = "public static final ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r21;
        r34 = r0.append(r1);
        r11 = r34.toString();
        r6 = " ";
        r15 = "";
        r16 = "";
        r24 = "";
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34 = r34.next();
        r35 = 4;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = 5;
        r37 = java.lang.Integer.valueOf(r37);
        r35[r36] = r37;
        r36 = 1;
        r37 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r37 = java.lang.Character.valueOf(r37);
        r35[r36] = r37;
        r36 = 2;
        r37 = 58;
        r37 = java.lang.Character.valueOf(r37);
        r35[r36] = r37;
        r36 = 3;
        r37 = 59;
        r37 = java.lang.Character.valueOf(r37);
        r35[r36] = r37;
        r30 = r34.expect(r35);
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 5;
        r36 = java.lang.Integer.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x01d8;
    L_0x01c8:
        r0 = r30;
        r0 = r0.value;
        r24 = r0;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
    L_0x01d8:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 58;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x022d;
    L_0x01f4:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
        r32 = r38.type(r39);
        r0 = r32;
        r8 = r0.cppName;
        r0 = r32;
        r0 = r0.javaName;
        r21 = r0;
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = "public static final ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r21;
        r34 = r0.append(r1);
        r11 = r34.toString();
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.get();
    L_0x022d:
        if (r33 != 0) goto L_0x0305;
    L_0x022f:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 59;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x0305;
    L_0x024b:
        r3 = r38.commentBefore();
        r9 = new org.bytedeco.javacpp.tools.Declaration;
        r9.<init>();
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.get();
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 59;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 != 0) goto L_0x0284;
    L_0x027a:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
    L_0x0284:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 5;
        r36 = java.lang.Integer.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x02b0;
    L_0x02a0:
        r0 = r30;
        r0 = r0.value;
        r24 = r0;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
    L_0x02b0:
        r0 = r39;
        r0 = r0.namespace;
        r34 = r0;
        if (r34 == 0) goto L_0x02d9;
    L_0x02b8:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r39;
        r0 = r0.namespace;
        r35 = r0;
        r34 = r34.append(r35);
        r35 = "::";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r24;
        r34 = r0.append(r1);
        r24 = r34.toString();
    L_0x02d9:
        r0 = r38;
        r0 = r0.infoMap;
        r34 = r0;
        r0 = r34;
        r1 = r24;
        r18 = r0.getFirst(r1);
        if (r18 == 0) goto L_0x0783;
    L_0x02e9:
        r0 = r18;
        r0 = r0.skip;
        r34 = r0;
        if (r34 == 0) goto L_0x0783;
    L_0x02f1:
        r9.text = r12;
    L_0x02f3:
        r0 = r40;
        r0.add(r9);
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34.next();
        r34 = 1;
        goto L_0x0080;
    L_0x0305:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 != 0) goto L_0x032f;
    L_0x0321:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r0 = r34;
        r0.index = r2;
        r34 = 0;
        goto L_0x0080;
    L_0x032f:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
    L_0x0339:
        r34 = 2;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = org.bytedeco.javacpp.tools.Token.EOF;
        r34[r35] = r36;
        r35 = 1;
        r36 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 != 0) goto L_0x024b;
    L_0x035b:
        r3 = r38.commentBefore();
        r34 = r38.macro(r39, r40);
        if (r34 == 0) goto L_0x03d9;
    L_0x0365:
        r34 = r40.size();
        r34 = r34 + -1;
        r0 = r40;
        r1 = r34;
        r23 = r0.remove(r1);
        r23 = (org.bytedeco.javacpp.tools.Declaration) r23;
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r1 = r16;
        r34 = r0.append(r1);
        r0 = r34;
        r34 = r0.append(r3);
        r0 = r23;
        r0 = r0.text;
        r35 = r0;
        r34 = r34.append(r35);
        r16 = r34.toString();
        r34 = ",";
        r0 = r27;
        r1 = r34;
        r34 = r0.equals(r1);
        if (r34 == 0) goto L_0x03cd;
    L_0x03a2:
        r0 = r23;
        r0 = r0.text;
        r34 = r0;
        r34 = r34.trim();
        r35 = "//";
        r34 = r34.startsWith(r35);
        if (r34 != 0) goto L_0x03cd;
    L_0x03b4:
        r27 = ";";
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = "\npublic static final ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r21;
        r34 = r0.append(r1);
        r11 = r34.toString();
    L_0x03cd:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.get();
        goto L_0x0339;
    L_0x03d9:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r14 = r34.get();
        r7 = r14.value;
        r20 = r7;
        r0 = r39;
        r0 = r0.namespace;
        r34 = r0;
        if (r34 == 0) goto L_0x040e;
    L_0x03ef:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r39;
        r0 = r0.namespace;
        r35 = r0;
        r34 = r34.append(r35);
        r35 = "::";
        r34 = r34.append(r35);
        r0 = r34;
        r34 = r0.append(r7);
        r7 = r34.toString();
    L_0x040e:
        r0 = r38;
        r0 = r0.infoMap;
        r34 = r0;
        r0 = r34;
        r18 = r0.getFirst(r7);
        if (r18 == 0) goto L_0x058a;
    L_0x041c:
        r0 = r18;
        r0 = r0.javaNames;
        r34 = r0;
        if (r34 == 0) goto L_0x058a;
    L_0x0424:
        r0 = r18;
        r0 = r0.javaNames;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        if (r34 <= 0) goto L_0x058a;
    L_0x0431:
        r0 = r18;
        r0 = r0.javaNames;
        r34 = r0;
        r35 = 0;
        r20 = r34[r35];
    L_0x043b:
        r29 = " ";
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34 = r34.next();
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = 61;
        r37 = java.lang.Character.valueOf(r37);
        r35[r36] = r37;
        r34 = r34.match(r35);
        if (r34 == 0) goto L_0x05da;
    L_0x045f:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34 = r34.get();
        r0 = r34;
        r0 = r0.spacing;
        r29 = r0;
        r6 = " ";
        r5 = 0;
        r26 = new org.bytedeco.javacpp.tools.Token;
        r26.<init>();
        r31 = 1;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
    L_0x0483:
        r34 = 3;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = org.bytedeco.javacpp.tools.Token.EOF;
        r34[r35] = r36;
        r35 = 1;
        r36 = 44;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r35 = 2;
        r36 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x04b1;
    L_0x04af:
        if (r5 <= 0) goto L_0x05d0;
    L_0x04b1:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 1;
        r36 = java.lang.Integer.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x04dd;
    L_0x04cd:
        r0 = r30;
        r0 = r0.value;
        r34 = r0;
        r35 = "L";
        r34 = r34.endsWith(r35);
        if (r34 == 0) goto L_0x04dd;
    L_0x04db:
        r22 = 1;
    L_0x04dd:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r34 = r0.append(r6);
        r0 = r30;
        r0 = r0.spacing;
        r35 = r0;
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r30;
        r34 = r0.append(r1);
        r6 = r34.toString();
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 40;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x05b0;
    L_0x051a:
        r5 = r5 + 1;
    L_0x051c:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 5;
        r36 = java.lang.Integer.valueOf(r36);
        r34[r35] = r36;
        r0 = r26;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x0554;
    L_0x0538:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 40;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 != 0) goto L_0x057a;
    L_0x0554:
        r34 = 2;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r35 = 1;
        r36 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x057c;
    L_0x057a:
        r31 = 0;
    L_0x057c:
        r26 = r30;
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r30 = r34.next();
        goto L_0x0483;
    L_0x058a:
        if (r18 != 0) goto L_0x043b;
    L_0x058c:
        r0 = r38;
        r0 = r0.infoMap;
        r34 = r0;
        r18 = new org.bytedeco.javacpp.tools.Info;
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.String[r0];
        r35 = r0;
        r36 = 0;
        r35[r36] = r7;
        r0 = r18;
        r1 = r35;
        r0.<init>(r1);
        r0 = r34;
        r1 = r18;
        r0.put(r1);
        goto L_0x043b;
    L_0x05b0:
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r36 = 41;
        r36 = java.lang.Character.valueOf(r36);
        r34[r35] = r36;
        r0 = r30;
        r1 = r34;
        r34 = r0.match(r1);
        if (r34 == 0) goto L_0x051c;
    L_0x05cc:
        r5 = r5 + -1;
        goto L_0x051c;
    L_0x05d0:
        r34 = r6.trim();	 Catch:{ NumberFormatException -> 0x06f1 }
        r4 = java.lang.Integer.parseInt(r34);	 Catch:{ NumberFormatException -> 0x06f1 }
        r6 = " ";
    L_0x05da:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r34 = r0.append(r15);
        r0 = r34;
        r1 = r27;
        r34 = r0.append(r1);
        r0 = r34;
        r1 = r16;
        r34 = r0.append(r1);
        r0 = r34;
        r34 = r0.append(r11);
        r0 = r34;
        r34 = r0.append(r3);
        r15 = r34.toString();
        r27 = ",";
        r11 = "";
        r16 = "";
        r3 = r38.commentAfter();
        r34 = r3.length();
        if (r34 != 0) goto L_0x0644;
    L_0x0615:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34 = r34.get();
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = 44;
        r37 = java.lang.Character.valueOf(r37);
        r35[r36] = r37;
        r34 = r34.match(r35);
        if (r34 == 0) goto L_0x0644;
    L_0x0637:
        r0 = r38;
        r0 = r0.tokens;
        r34 = r0;
        r34.next();
        r3 = r38.commentAfter();
    L_0x0644:
        r0 = r14.spacing;
        r28 = r0;
        r34 = r3.length();
        if (r34 <= 0) goto L_0x0681;
    L_0x064e:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r34 = r0.append(r15);
        r0 = r34;
        r1 = r28;
        r34 = r0.append(r1);
        r0 = r34;
        r34 = r0.append(r3);
        r15 = r34.toString();
        r34 = 10;
        r0 = r28;
        r1 = r34;
        r25 = r0.lastIndexOf(r1);
        if (r25 < 0) goto L_0x0681;
    L_0x0677:
        r34 = r25 + 1;
        r0 = r28;
        r1 = r34;
        r28 = r0.substring(r1);
    L_0x0681:
        r34 = r28.length();
        if (r34 != 0) goto L_0x0693;
    L_0x0687:
        r34 = ",";
        r0 = r34;
        r34 = r15.endsWith(r0);
        if (r34 != 0) goto L_0x0693;
    L_0x0691:
        r28 = " ";
    L_0x0693:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r34 = r0.append(r15);
        r0 = r34;
        r1 = r28;
        r34 = r0.append(r1);
        r0 = r34;
        r1 = r20;
        r34 = r0.append(r1);
        r0 = r34;
        r1 = r29;
        r34 = r0.append(r1);
        r35 = "=";
        r34 = r34.append(r35);
        r0 = r34;
        r34 = r0.append(r6);
        r15 = r34.toString();
        r34 = r6.trim();
        r34 = r34.length();
        if (r34 <= 0) goto L_0x076c;
    L_0x06d0:
        if (r4 <= 0) goto L_0x06ed;
    L_0x06d2:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r34 = r0.append(r15);
        r35 = " + ";
        r34 = r34.append(r35);
        r0 = r34;
        r34 = r0.append(r4);
        r15 = r34.toString();
    L_0x06ed:
        r4 = r4 + 1;
        goto L_0x03cd;
    L_0x06f1:
        r10 = move-exception;
        r4 = 0;
        if (r31 == 0) goto L_0x06fd;
    L_0x06f5:
        r0 = r38;
        r6 = r0.translate(r6);
        goto L_0x05da;
    L_0x06fd:
        r34 = ",";
        r0 = r27;
        r1 = r34;
        r34 = r0.equals(r1);
        if (r34 == 0) goto L_0x070b;
    L_0x0709:
        r27 = ";";
    L_0x070b:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = "\npublic static native @MemberGetter ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r21;
        r34 = r0.append(r1);
        r35 = " ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r20;
        r34 = r0.append(r1);
        r35 = "();\n";
        r34 = r34.append(r35);
        r16 = r34.toString();
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = "public static final ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r21;
        r34 = r0.append(r1);
        r11 = r34.toString();
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = " ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r20;
        r34 = r0.append(r1);
        r35 = "()";
        r34 = r34.append(r35);
        r6 = r34.toString();
        goto L_0x05da;
    L_0x076c:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r34;
        r34 = r0.append(r15);
        r0 = r34;
        r34 = r0.append(r4);
        r15 = r34.toString();
        goto L_0x06ed;
    L_0x0783:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r9.text;
        r35 = r0;
        r34 = r34.append(r35);
        r0 = r34;
        r34 = r0.append(r12);
        r35 = "/** ";
        r34 = r34.append(r35);
        r0 = r34;
        r34 = r0.append(r13);
        r35 = " ";
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r24;
        r34 = r0.append(r1);
        r35 = " */\n";
        r34 = r34.append(r35);
        r34 = r34.toString();
        r0 = r34;
        r9.text = r0;
        r34 = 10;
        r0 = r34;
        r25 = r12.lastIndexOf(r0);
        if (r25 < 0) goto L_0x07d0;
    L_0x07c8:
        r34 = r25 + 1;
        r0 = r34;
        r12 = r12.substring(r0);
    L_0x07d0:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r9.text;
        r35 = r0;
        r34 = r34.append(r35);
        r0 = r34;
        r34 = r0.append(r12);
        r0 = r34;
        r34 = r0.append(r15);
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r37 = 59;
        r37 = java.lang.Character.valueOf(r37);
        r35[r36] = r37;
        r0 = r30;
        r1 = r35;
        r35 = r0.expect(r1);
        r0 = r35;
        r0 = r0.spacing;
        r35 = r0;
        r34 = r34.append(r35);
        r35 = ";";
        r34 = r34.append(r35);
        r34 = r34.toString();
        r0 = r34;
        r9.text = r0;
        if (r22 == 0) goto L_0x0846;
    L_0x081d:
        r0 = r9.text;
        r34 = r0;
        r35 = new java.lang.StringBuilder;
        r35.<init>();
        r36 = " ";
        r35 = r35.append(r36);
        r0 = r35;
        r1 = r21;
        r35 = r0.append(r1);
        r35 = r35.toString();
        r36 = " long";
        r34 = r34.replace(r35, r36);
        r0 = r34;
        r9.text = r0;
        r8 = "long long";
        r21 = "long";
    L_0x0846:
        r34 = r24.length();
        if (r34 <= 0) goto L_0x087e;
    L_0x084c:
        r0 = r38;
        r0 = r0.infoMap;
        r34 = r0;
        r0 = r34;
        r19 = r0.getFirst(r8);
        r0 = r38;
        r0 = r0.infoMap;
        r34 = r0;
        r35 = new org.bytedeco.javacpp.tools.Info;
        r0 = r35;
        r1 = r19;
        r0.<init>(r1);
        r35 = r35.cast();
        r36 = 1;
        r0 = r36;
        r0 = new java.lang.String[r0];
        r36 = r0;
        r37 = 0;
        r36[r37] = r24;
        r35 = r35.cppNames(r36);
        r34.put(r35);
    L_0x087e:
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r0 = r9.text;
        r35 = r0;
        r34 = r34.append(r35);
        r0 = r34;
        r1 = r16;
        r34 = r0.append(r1);
        r0 = r34;
        r34 = r0.append(r3);
        r34 = r34.toString();
        r0 = r34;
        r9.text = r0;
        goto L_0x02f3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Parser.enumeration(org.bytedeco.javacpp.tools.Context, org.bytedeco.javacpp.tools.DeclarationList):boolean");
    }

    boolean namespace(Context context, DeclarationList declList) throws ParserException {
        if (!this.tokens.get().match(Token.NAMESPACE)) {
            return false;
        }
        Declaration decl = new Declaration();
        String spacing = this.tokens.get().spacing;
        String name = null;
        this.tokens.next();
        if (this.tokens.get().match(Integer.valueOf(5))) {
            name = this.tokens.get().value;
            this.tokens.next();
        }
        if (this.tokens.get().match(Character.valueOf('='))) {
            this.tokens.next();
            context.namespaceMap.put(name, type(context).cppName);
            this.tokens.get().expect(Character.valueOf(';'));
            this.tokens.next();
            return true;
        }
        this.tokens.get().expect(Character.valueOf('{'));
        this.tokens.next();
        if (this.tokens.get().spacing.indexOf(10) < 0) {
            this.tokens.get().spacing = spacing;
        }
        Context context2 = new Context(context);
        if (name == null) {
            name = null;
        } else if (context2.namespace != null) {
            name = context2.namespace + "::" + name;
        }
        context2.namespace = name;
        declarations(context2, declList);
        decl.text += this.tokens.get().expect(Character.valueOf('}')).spacing;
        this.tokens.next();
        declList.add(decl);
        context = context2;
        return true;
    }

    boolean extern(Context context, DeclarationList declList) throws ParserException {
        if (this.tokens.get().match(Token.EXTERN)) {
            if (this.tokens.get(1).match(Integer.valueOf(3))) {
                String spacing = this.tokens.get().spacing;
                Declaration decl = new Declaration();
                this.tokens.next().expect("\"C\"");
                if (this.tokens.next().match(Character.valueOf('{'))) {
                    this.tokens.next();
                    declarations(context, declList);
                    this.tokens.get().expect(Character.valueOf('}'));
                    this.tokens.next();
                    declList.add(decl);
                    return true;
                }
                this.tokens.get().spacing = spacing;
                declList.add(decl);
                return true;
            }
        }
        return false;
    }

    void declarations(Context context, DeclarationList declList) throws ParserException {
        String comment;
        Declaration decl;
        Token token = this.tokens.get();
        while (true) {
            if (token.match(Token.EOF, Character.valueOf('}'))) {
                break;
            }
            if (token.match(Token.PRIVATE, Token.PROTECTED, Token.PUBLIC)) {
                if (this.tokens.get(1).match(Character.valueOf(':'))) {
                    context.inaccessible = !token.match(Token.PUBLIC);
                    this.tokens.next();
                    this.tokens.next();
                    token = this.tokens.get();
                }
            }
            Context ctx = context;
            comment = commentBefore();
            token = this.tokens.get();
            String spacing = token.spacing;
            TemplateMap map = template(ctx);
            if (map != null) {
                token = this.tokens.get();
                token.spacing = spacing;
                Context ctx2 = new Context(ctx);
                ctx2.templateMap = map;
                ctx = ctx2;
            }
            decl = new Declaration();
            if (comment != null && comment.length() > 0) {
                decl.inaccessible = ctx.inaccessible;
                decl.text = comment;
                declList.add(decl);
            }
            int startIndex = this.tokens.index;
            declList.infoMap = this.infoMap;
            declList.context = ctx;
            declList.templateMap = map;
            declList.infoIterator = null;
            declList.spacing = null;
            do {
                if (!(map == null || declList.infoIterator == null || !declList.infoIterator.hasNext())) {
                    Info info = (Info) declList.infoIterator.next();
                    if (info != null) {
                        Type type = new Parser(this, info.cppNames[0]).type(context);
                        if (type.arguments != null) {
                            int count = 0;
                            for (Entry<String, Type> e : map.entrySet()) {
                                if (count < type.arguments.length) {
                                    int count2 = count + 1;
                                    Type t = type.arguments[count];
                                    String s = t.cppName;
                                    if (t.constValue && !s.startsWith("const ")) {
                                        s = "const " + s;
                                    }
                                    if (t.constPointer && !s.endsWith(" const")) {
                                        s = s + " const";
                                    }
                                    if (t.indirections > 0) {
                                        for (int i = 0; i < t.indirections; i++) {
                                            s = s + "*";
                                        }
                                    }
                                    if (t.reference) {
                                        s = s + "&";
                                    }
                                    t.cppName = s;
                                    e.setValue(t);
                                    count = count2;
                                }
                            }
                            this.tokens.index = startIndex;
                        }
                    }
                    if (declList.infoIterator != null) {
                        break;
                    }
                }
                if (!(this.tokens.get().match(Character.valueOf(';')) || macro(ctx, declList) || extern(ctx, declList) || namespace(ctx, declList) || enumeration(ctx, declList) || group(ctx, declList) || typedef(ctx, declList) || using(ctx, declList) || function(ctx, declList) || variable(ctx, declList))) {
                    spacing = this.tokens.get().spacing;
                    if (attribute() != null) {
                        this.tokens.get().spacing = spacing;
                    } else {
                        throw new ParserException(token.file + ":" + token.lineNumber + ": Could not parse declaration at '" + token + "'");
                    }
                }
                while (true) {
                    if (!this.tokens.get().match(Character.valueOf(';'))) {
                        break;
                    }
                    if (this.tokens.get().match(Token.EOF)) {
                        break;
                    }
                    this.tokens.next();
                }
                if (declList.infoIterator != null) {
                    break;
                }
            } while (declList.infoIterator.hasNext());
            token = this.tokens.get();
        }
        comment = commentBefore();
        decl = new Declaration();
        if (comment != null && comment.length() > 0) {
            decl.text = comment;
            declList.add(decl);
        }
    }

    void parse(Context context, DeclarationList declList, String[] includePath, String include, boolean isCFile) throws IOException, ParserException {
        File f;
        List<Token> tokenList = new ArrayList();
        File file = null;
        String filename = include;
        if (filename.startsWith("<") && filename.endsWith(">")) {
            filename = filename.substring(1, filename.length() - 1);
        } else {
            f = new File(filename);
            if (f.exists()) {
                file = f;
            }
        }
        if (file == null && includePath != null) {
            for (String path : includePath) {
                f = new File(path, filename).getCanonicalFile();
                if (f.exists()) {
                    file = f;
                    break;
                }
            }
        }
        if (file == null) {
            file = new File(filename);
        }
        Info info = this.infoMap.getFirst(file.getName());
        if (info == null || !info.skip || info.linePatterns != null) {
            if (file.exists()) {
                this.logger.info("Parsing " + file);
                Token token = new Token();
                token.type = 4;
                token.value = "\n// Parsed from " + include + "\n\n";
                tokenList.add(token);
                Tokenizer tokenizer = new Tokenizer(file);
                if (!(info == null || info.linePatterns == null)) {
                    tokenizer.filterLines(info.linePatterns, info.skip);
                }
                while (true) {
                    token = tokenizer.nextToken();
                    if (token.isEmpty()) {
                        break;
                    }
                    if (token.type == -1) {
                        token.type = 4;
                    }
                    tokenList.add(token);
                }
                if (this.lineSeparator == null) {
                    this.lineSeparator = tokenizer.lineSeparator;
                }
                tokenizer.close();
                token = new Token();
                token.type = 4;
                token.spacing = "\n";
                tokenList.add(token);
                this.tokens = new TokenIndexer(this.infoMap, (Token[]) tokenList.toArray(new Token[tokenList.size()]), isCFile);
                declarations(context, declList);
                return;
            }
            throw new FileNotFoundException("Could not parse \"" + file + "\": File does not exist");
        }
    }

    public File parse(String outputDirectory, String[] classPath, Class cls) throws IOException, ParserException {
        return parse(new File(outputDirectory), classPath, cls);
    }

    public File parse(File outputDirectory, String[] classPath, Class cls) throws IOException, ParserException {
        Iterator it;
        Throwable th;
        ClassProperties allProperties = Loader.loadProperties(cls, this.properties, true);
        ClassProperties clsProperties = Loader.loadProperties(cls, this.properties, false);
        List<String> cIncludes = new ArrayList();
        cIncludes.addAll(clsProperties.get("platform.cinclude"));
        cIncludes.addAll(allProperties.get("platform.cinclude"));
        List<String> clsIncludes = new ArrayList();
        clsIncludes.addAll(clsProperties.get("platform.include"));
        clsIncludes.addAll(clsProperties.get("platform.cinclude"));
        List<String> allIncludes = new ArrayList();
        allIncludes.addAll(allProperties.get("platform.include"));
        allIncludes.addAll(allProperties.get("platform.cinclude"));
        List<String> allTargets = allProperties.get("target");
        List<String> clsTargets = clsProperties.get("target");
        List<String> clsHelpers = clsProperties.get("helper");
        String target = (String) clsTargets.get(0);
        List<Class> allInherited = allProperties.getInheritedClasses();
        this.infoMap = new InfoMap();
        for (Class c : allInherited) {
            try {
                ((InfoMapper) c.newInstance()).map(this.infoMap);
            } catch (ClassCastException e) {
            } catch (InstantiationException e2) {
            } catch (IllegalAccessException e3) {
            }
        }
        this.leafInfoMap = new InfoMap();
        try {
            ((InfoMapper) cls.newInstance()).map(this.leafInfoMap);
        } catch (ClassCastException e4) {
        } catch (InstantiationException e5) {
        } catch (IllegalAccessException e6) {
        }
        this.infoMap.putAll(this.leafInfoMap);
        String version = Generator.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "unknown";
        }
        String text = "// Targeted by JavaCPP version " + version + ": DO NOT EDIT THIS FILE\n\n";
        int n = target.lastIndexOf(46);
        if (n >= 0) {
            text = text + "package " + target.substring(0, n) + ";\n\n";
        }
        List<Info> infoList = this.leafInfoMap.get(null);
        for (Info info : infoList) {
            if (info.javaText != null && info.javaText.startsWith("import")) {
                text = text + info.javaText + "\n";
            }
        }
        text = text + "import java.nio.*;\nimport org.bytedeco.javacpp.*;\nimport org.bytedeco.javacpp.annotation.*;\n\n";
        for (String s : allTargets) {
            if (!target.equals(s)) {
                text = text + "import static " + s + ".*;\n";
            }
        }
        if (allTargets.size() > 1) {
            text = text + "\n";
        }
        StringBuilder append = new StringBuilder().append(text).append("public class ").append(target.substring(n + 1)).append(" extends ");
        String canonicalName = (clsHelpers.size() <= 0 || clsIncludes.size() <= 0) ? cls.getCanonicalName() : (String) clsHelpers.get(0);
        text = append.append(canonicalName).append(" {\n    static { Loader.load(); }\n").toString();
        String targetPath = target.replace('.', File.separatorChar);
        File file = new File(outputDirectory, targetPath + ".java");
        this.logger.info("Targeting " + file);
        Context context = new Context();
        context.infoMap = this.infoMap;
        Object includePath = classPath;
        n = targetPath.lastIndexOf(File.separatorChar);
        if (n >= 0) {
            includePath = (String[]) classPath.clone();
            for (int i = 0; i < includePath.length; i++) {
                includePath[i] = includePath[i] + File.separator + targetPath.substring(0, n);
            }
        }
        List<String> paths = allProperties.get("platform.includepath");
        String[] includePaths = (String[]) paths.toArray(new String[(paths.size() + includePath.length)]);
        System.arraycopy(includePath, 0, includePaths, paths.size(), includePath.length);
        DeclarationList declList = new DeclarationList();
        for (String include : allIncludes) {
            if (!clsIncludes.contains(include)) {
                parse(context, declList, includePaths, include, cIncludes.contains(include));
            }
        }
        DeclarationList declList2 = new DeclarationList(declList);
        if (clsIncludes.size() > 0) {
            containers(context, declList2);
            for (String include2 : clsIncludes) {
                if (allIncludes.contains(include2)) {
                    parse(context, declList2, includePaths, include2, cIncludes.contains(include2));
                }
            }
        }
        File targetDir = file.getParentFile();
        if (targetDir != null) {
            targetDir.mkdirs();
        }
        final String str = this.lineSeparator != null ? this.lineSeparator : "\n";
        Writer c12361 = new FileWriter(file) {
            public Writer append(CharSequence text) throws IOException {
                return super.append(((String) text).replace("\n", str).replace("\\u", "\\u005Cu"));
            }
        };
        Throwable th2 = null;
        try {
            c12361.append(text);
            for (Info info2 : infoList) {
                if (!(info2.javaText == null || info2.javaText.startsWith("import"))) {
                    c12361.append(info2.javaText + "\n");
                }
            }
            it = declList2.iterator();
            while (it.hasNext()) {
                c12361.append(((Declaration) it.next()).text);
            }
            c12361.append("\n}\n").close();
            if (c12361 != null) {
                if (th2 != null) {
                    try {
                        c12361.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                } else {
                    c12361.close();
                }
            }
            return file;
        } catch (Throwable th22) {
            Throwable th4 = th22;
            th22 = th3;
            th3 = th4;
        }
        throw th3;
        if (c12361 != null) {
            if (th22 != null) {
                try {
                    c12361.close();
                } catch (Throwable th5) {
                    th22.addSuppressed(th5);
                }
            } else {
                c12361.close();
            }
        }
        throw th3;
    }
}
