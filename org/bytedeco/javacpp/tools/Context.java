package org.bytedeco.javacpp.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class Context {
    String cppName;
    boolean inaccessible;
    InfoMap infoMap;
    String javaName;
    String namespace;
    Map<String, String> namespaceMap;
    TemplateMap templateMap;
    List<String> usingList;
    Declarator variable;
    boolean virtualize;

    Context() {
        this.namespace = null;
        this.cppName = null;
        this.javaName = null;
        this.inaccessible = false;
        this.virtualize = false;
        this.variable = null;
        this.infoMap = null;
        this.templateMap = null;
        this.usingList = null;
        this.namespaceMap = null;
        this.usingList = new ArrayList();
        this.namespaceMap = new HashMap();
    }

    Context(Context c) {
        this.namespace = null;
        this.cppName = null;
        this.javaName = null;
        this.inaccessible = false;
        this.virtualize = false;
        this.variable = null;
        this.infoMap = null;
        this.templateMap = null;
        this.usingList = null;
        this.namespaceMap = null;
        this.namespace = c.namespace;
        this.cppName = c.cppName;
        this.javaName = c.javaName;
        this.inaccessible = c.inaccessible;
        this.variable = c.variable;
        this.infoMap = c.infoMap;
        this.templateMap = c.templateMap;
        this.usingList = c.usingList;
        this.namespaceMap = c.namespaceMap;
    }

    String[] qualify(String cppName) {
        if (cppName == null || cppName.length() == 0) {
            return new String[0];
        }
        for (Entry<String, String> e : this.namespaceMap.entrySet()) {
            cppName = cppName.replaceAll(((String) e.getKey()) + "::", ((String) e.getValue()) + "::");
        }
        if (cppName.startsWith("::")) {
            return new String[]{cppName.substring(2)};
        }
        List<String> names = new ArrayList();
        String ns = this.namespace != null ? this.namespace : "";
        while (ns != null) {
            String name;
            int i;
            if (ns.length() > 0) {
                name = ns + "::" + cppName;
            } else {
                name = cppName;
            }
            TemplateMap map = this.templateMap;
            while (map != null) {
                if (name.equals(map.getName())) {
                    String args = "<";
                    String separator = "";
                    for (Object t : map.values()) {
                        Object t2;
                        StringBuilder append = new StringBuilder().append(args).append(separator);
                        if (t2 != null) {
                            t2 = t2.cppName;
                        }
                        args = append.append(t2).toString();
                        separator = ",";
                    }
                    names.add(name + args + (args.endsWith(">") ? " >" : ">"));
                    names.add(name);
                    ns = this.infoMap.normalize(ns, false, true);
                    i = ns.lastIndexOf("::");
                    ns = i < 0 ? ns.substring(0, i) : ns.length() <= 0 ? "" : null;
                } else {
                    map = map.parent;
                }
            }
            names.add(name);
            ns = this.infoMap.normalize(ns, false, true);
            i = ns.lastIndexOf("::");
            if (i < 0) {
                if (ns.length() <= 0) {
                }
            }
        }
        for (String s : this.usingList) {
            String prefix = this.infoMap.normalize(cppName, false, true);
            i = s.lastIndexOf("::") + 2;
            ns = s.substring(0, i);
            String suffix = s.substring(i);
            if (suffix.length() == 0 || prefix.equals(suffix)) {
                names.add(ns + cppName);
            }
        }
        return (String[]) names.toArray(new String[names.size()]);
    }

    String shorten(String javaName) {
        if (this.javaName == null) {
            return javaName;
        }
        int lastDot = 0;
        String s1 = javaName;
        String s2 = this.javaName + '.';
        int i = 0;
        while (i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
            if (s1.charAt(i) == '.') {
                lastDot = i;
            }
            i++;
        }
        if (lastDot > 0) {
            return javaName.substring(lastDot + 1);
        }
        return javaName;
    }
}
