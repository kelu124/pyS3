package org.bytedeco.javacpp.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

class DeclarationList extends ArrayList<Declaration> {
    Context context = null;
    ListIterator<Info> infoIterator = null;
    InfoMap infoMap = null;
    DeclarationList inherited = null;
    String spacing = null;
    TemplateMap templateMap = null;

    DeclarationList() {
    }

    DeclarationList(DeclarationList inherited) {
        this.inherited = inherited;
    }

    String rescan(String lines) {
        Throwable th;
        if (this.spacing == null) {
            return lines;
        }
        String text = "";
        Scanner scanner = new Scanner(lines);
        Throwable th2 = null;
        while (scanner.hasNextLine()) {
            try {
                text = text + this.spacing + scanner.nextLine();
                int newline = this.spacing.lastIndexOf(10);
                this.spacing = newline >= 0 ? this.spacing.substring(newline) : "\n";
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
        }
        if (scanner != null) {
            if (th22 != null) {
                try {
                    scanner.close();
                } catch (Throwable th4) {
                    th22.addSuppressed(th4);
                }
            } else {
                scanner.close();
            }
        }
        return text;
        if (scanner != null) {
            if (th22 != null) {
                try {
                    scanner.close();
                } catch (Throwable th5) {
                    th22.addSuppressed(th5);
                }
            } else {
                scanner.close();
            }
        }
        throw th4;
        throw th4;
    }

    public boolean add(Declaration decl) {
        int i;
        boolean add = true;
        Info info;
        if (this.templateMap != null && !this.templateMap.full() && (decl.type != null || decl.declarator != null)) {
            if (this.infoIterator == null) {
                TemplateMap templateMap = this.templateMap;
                Type type = decl.type;
                templateMap.type = type;
                templateMap = this.templateMap;
                Declarator dcl = decl.declarator;
                templateMap.declarator = dcl;
                List<Info> infoList = this.infoMap.get(dcl != null ? dcl.cppName : type.cppName);
                boolean hasJavaName = false;
                for (Info info2 : infoList) {
                    i = (info2.javaNames == null || info2.javaNames.length <= 0) ? 0 : 1;
                    hasJavaName |= i;
                }
                if (!decl.function || hasJavaName) {
                    this.infoIterator = infoList.size() > 0 ? infoList.listIterator() : null;
                }
            }
            add = false;
        } else if (decl.declarator != null && decl.declarator.type != null) {
            info2 = this.infoMap.getFirst(decl.declarator.type.cppName);
            if (info2 != null && info2.skip && info2.valueTypes == null && info2.pointerTypes == null) {
                add = false;
            } else if (decl.declarator.parameters != null) {
                for (Declarator d : decl.declarator.parameters.declarators) {
                    if (!(d == null || d.type == null)) {
                        info2 = this.infoMap.getFirst(d.type.cppName);
                        if (info2 != null && info2.skip && info2.valueTypes == null && info2.pointerTypes == null) {
                            add = false;
                            break;
                        }
                    }
                }
            }
        }
        if (!add) {
            return false;
        }
        List<Declaration> stack = new ArrayList();
        ListIterator<Declaration> it = stack.listIterator();
        it.add(decl);
        it.previous();
        while (it.hasNext()) {
            dcl = ((Declaration) it.next()).declarator;
            if (!(dcl == null || dcl.definition == null)) {
                it.add(dcl.definition);
                it.previous();
            }
            if (!(dcl == null || dcl.parameters == null || dcl.parameters.declarators == null)) {
                for (Declarator d2 : dcl.parameters.declarators) {
                    if (!(d2 == null || d2.definition == null)) {
                        it.add(d2.definition);
                        it.previous();
                    }
                }
            }
        }
        add = false;
        while (!stack.isEmpty()) {
            decl = (Declaration) stack.remove(stack.size() - 1);
            if (this.context != null) {
                boolean z = this.context.inaccessible && (!this.context.virtualize || decl.declarator == null || decl.declarator.type == null || !decl.declarator.type.virtual);
                decl.inaccessible = z;
            }
            if (decl.text.length() == 0) {
                decl.inaccessible = true;
            }
            it = listIterator();
            boolean found = false;
            while (it.hasNext()) {
                Declaration d3 = (Declaration) it.next();
                if (d3.signature.length() > 0 && d3.signature.equals(decl.signature)) {
                    if ((!d3.constMember || decl.constMember) && ((!d3.inaccessible || decl.inaccessible) && (!d3.incomplete || decl.incomplete))) {
                        found = true;
                    } else {
                        it.remove();
                    }
                }
            }
            if (this.inherited != null) {
                it = this.inherited.listIterator();
                while (it.hasNext()) {
                    d3 = (Declaration) it.next();
                    if (d3.signature.length() > 0 && d3.signature.equals(decl.signature) && !d3.incomplete && decl.incomplete) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                decl.text = rescan(decl.text);
                super.add(decl);
                add = true;
            }
        }
        return add;
    }
}
