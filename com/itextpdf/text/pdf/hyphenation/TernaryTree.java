package com.itextpdf.text.pdf.hyphenation;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Stack;

public class TernaryTree implements Cloneable, Serializable {
    protected static final int BLOCK_SIZE = 2048;
    private static final long serialVersionUID = 5313366505322983510L;
    protected char[] eq;
    protected char freenode;
    protected char[] hi;
    protected CharVector kv;
    protected int length;
    protected char[] lo;
    protected char root;
    protected char[] sc;

    public class Iterator implements Enumeration<String> {
        int cur = -1;
        String curkey;
        StringBuffer ks = new StringBuffer();
        Stack<Item> ns = new Stack();

        private class Item implements Cloneable {
            char child;
            char parent;

            public Item() {
                this.parent = '\u0000';
                this.child = '\u0000';
            }

            public Item(char p, char c) {
                this.parent = p;
                this.child = c;
            }

            public Item clone() {
                return new Item(this.parent, this.child);
            }
        }

        public Iterator() {
            rewind();
        }

        public void rewind() {
            this.ns.removeAllElements();
            this.ks.setLength(0);
            this.cur = TernaryTree.this.root;
            run();
        }

        public String nextElement() {
            String res = this.curkey;
            this.cur = up();
            run();
            return res;
        }

        public char getValue() {
            if (this.cur >= 0) {
                return TernaryTree.this.eq[this.cur];
            }
            return '\u0000';
        }

        public boolean hasMoreElements() {
            return this.cur != -1;
        }

        private int up() {
            Item i = new Item();
            int res = 0;
            if (this.ns.empty()) {
                return -1;
            }
            if (this.cur != 0 && TernaryTree.this.sc[this.cur] == '\u0000') {
                return TernaryTree.this.lo[this.cur];
            }
            boolean climb = true;
            while (climb) {
                i = (Item) this.ns.pop();
                i.child = (char) (i.child + 1);
                switch (i.child) {
                    case '\u0001':
                        if (TernaryTree.this.sc[i.parent] != '\u0000') {
                            res = TernaryTree.this.eq[i.parent];
                            this.ns.push(i.clone());
                            this.ks.append(TernaryTree.this.sc[i.parent]);
                        } else {
                            i.child = (char) (i.child + 1);
                            this.ns.push(i.clone());
                            res = TernaryTree.this.hi[i.parent];
                        }
                        climb = false;
                        break;
                    case '\u0002':
                        res = TernaryTree.this.hi[i.parent];
                        this.ns.push(i.clone());
                        if (this.ks.length() > 0) {
                            this.ks.setLength(this.ks.length() - 1);
                        }
                        climb = false;
                        break;
                    default:
                        if (!this.ns.empty()) {
                            climb = true;
                            break;
                        }
                        return -1;
                }
            }
            return res;
        }

        private int run() {
            if (this.cur == -1) {
                return -1;
            }
            boolean leaf = false;
            while (true) {
                if (this.cur != 0) {
                    if (TernaryTree.this.sc[this.cur] == '￿') {
                        leaf = true;
                    } else {
                        this.ns.push(new Item((char) this.cur, '\u0000'));
                        if (TernaryTree.this.sc[this.cur] == '\u0000') {
                            leaf = true;
                        } else {
                            this.cur = TernaryTree.this.lo[this.cur];
                        }
                    }
                }
                if (leaf) {
                    break;
                }
                this.cur = up();
                if (this.cur == -1) {
                    return -1;
                }
            }
            StringBuffer buf = new StringBuffer(this.ks.toString());
            if (TernaryTree.this.sc[this.cur] == '￿') {
                int p = TernaryTree.this.lo[this.cur];
                while (TernaryTree.this.kv.get(p) != '\u0000') {
                    int p2 = p + 1;
                    buf.append(TernaryTree.this.kv.get(p));
                    p = p2;
                }
            }
            this.curkey = buf.toString();
            return 0;
        }
    }

    TernaryTree() {
        init();
    }

    protected void init() {
        this.root = '\u0000';
        this.freenode = '\u0001';
        this.length = 0;
        this.lo = new char[2048];
        this.hi = new char[2048];
        this.eq = new char[2048];
        this.sc = new char[2048];
        this.kv = new CharVector();
    }

    public void insert(String key, char val) {
        int len = key.length() + 1;
        if (this.freenode + len > this.eq.length) {
            redimNodeArrays(this.eq.length + 2048);
        }
        int len2 = len - 1;
        char[] strkey = new char[len];
        key.getChars(0, len2, strkey, 0);
        strkey[len2] = '\u0000';
        this.root = insert(this.root, strkey, 0, val);
    }

    public void insert(char[] key, int start, char val) {
        if (this.freenode + (strlen(key) + 1) > this.eq.length) {
            redimNodeArrays(this.eq.length + 2048);
        }
        this.root = insert(this.root, key, start, val);
    }

    private char insert(char p, char[] key, int start, char val) {
        int len = strlen(key, start);
        if (p == '\u0000') {
            p = this.freenode;
            this.freenode = (char) (p + 1);
            this.eq[p] = val;
            this.length++;
            this.hi[p] = '\u0000';
            if (len > 0) {
                this.sc[p] = '￿';
                this.lo[p] = (char) this.kv.alloc(len + 1);
                strcpy(this.kv.getArray(), this.lo[p], key, start);
            } else {
                this.sc[p] = '\u0000';
                this.lo[p] = '\u0000';
            }
            return p;
        }
        if (this.sc[p] == '￿') {
            char pp = this.freenode;
            this.freenode = (char) (pp + 1);
            this.lo[pp] = this.lo[p];
            this.eq[pp] = this.eq[p];
            this.lo[p] = '\u0000';
            if (len > 0) {
                this.sc[p] = this.kv.get(this.lo[pp]);
                this.eq[p] = pp;
                char[] cArr = this.lo;
                cArr[pp] = (char) (cArr[pp] + 1);
                if (this.kv.get(this.lo[pp]) == '\u0000') {
                    this.lo[pp] = '\u0000';
                    this.sc[pp] = '\u0000';
                    this.hi[pp] = '\u0000';
                } else {
                    this.sc[pp] = '￿';
                }
            } else {
                this.sc[pp] = '￿';
                this.hi[p] = pp;
                this.sc[p] = '\u0000';
                this.eq[p] = val;
                this.length++;
                return p;
            }
        }
        char s = key[start];
        if (s < this.sc[p]) {
            this.lo[p] = insert(this.lo[p], key, start, val);
        } else if (s != this.sc[p]) {
            this.hi[p] = insert(this.hi[p], key, start, val);
        } else if (s != '\u0000') {
            this.eq[p] = insert(this.eq[p], key, start + 1, val);
        } else {
            this.eq[p] = val;
        }
        return p;
    }

    public static int strcmp(char[] a, int startA, char[] b, int startB) {
        while (a[startA] == b[startB]) {
            if (a[startA] == '\u0000') {
                return 0;
            }
            startA++;
            startB++;
        }
        return a[startA] - b[startB];
    }

    public static int strcmp(String str, char[] a, int start) {
        int len = str.length();
        int i = 0;
        while (i < len) {
            int d = str.charAt(i) - a[start + i];
            if (d != 0 || a[start + i] == '\u0000') {
                return d;
            }
            i++;
        }
        if (a[start + i] != '\u0000') {
            return -a[start + i];
        }
        return 0;
    }

    public static void strcpy(char[] dst, int di, char[] src, int si) {
        while (src[si] != '\u0000') {
            int di2 = di + 1;
            int si2 = si + 1;
            dst[di] = src[si];
            si = si2;
            di = di2;
        }
        dst[di] = '\u0000';
    }

    public static int strlen(char[] a, int start) {
        int len = 0;
        int i = start;
        while (i < a.length && a[i] != '\u0000') {
            len++;
            i++;
        }
        return len;
    }

    public static int strlen(char[] a) {
        return strlen(a, 0);
    }

    public int find(String key) {
        int len = key.length();
        char[] strkey = new char[(len + 1)];
        key.getChars(0, len, strkey, 0);
        strkey[len] = '\u0000';
        return find(strkey, 0);
    }

    public int find(char[] key, int start) {
        char p = this.root;
        int i = start;
        while (p != '\u0000') {
            if (this.sc[p] != '￿') {
                char c = key[i];
                int d = c - this.sc[p];
                if (d == 0) {
                    if (c == '\u0000') {
                        return this.eq[p];
                    }
                    i++;
                    p = this.eq[p];
                } else if (d < 0) {
                    p = this.lo[p];
                } else {
                    p = this.hi[p];
                }
            } else if (strcmp(key, i, this.kv.getArray(), this.lo[p]) == 0) {
                return this.eq[p];
            } else {
                return -1;
            }
        }
        return -1;
    }

    public boolean knows(String key) {
        return find(key) >= 0;
    }

    private void redimNodeArrays(int newsize) {
        int len = newsize < this.lo.length ? newsize : this.lo.length;
        char[] na = new char[newsize];
        System.arraycopy(this.lo, 0, na, 0, len);
        this.lo = na;
        na = new char[newsize];
        System.arraycopy(this.hi, 0, na, 0, len);
        this.hi = na;
        na = new char[newsize];
        System.arraycopy(this.eq, 0, na, 0, len);
        this.eq = na;
        na = new char[newsize];
        System.arraycopy(this.sc, 0, na, 0, len);
        this.sc = na;
    }

    public int size() {
        return this.length;
    }

    public Object clone() {
        TernaryTree t = new TernaryTree();
        t.lo = (char[]) this.lo.clone();
        t.hi = (char[]) this.hi.clone();
        t.eq = (char[]) this.eq.clone();
        t.sc = (char[]) this.sc.clone();
        t.kv = (CharVector) this.kv.clone();
        t.root = this.root;
        t.freenode = this.freenode;
        t.length = this.length;
        return t;
    }

    protected void insertBalanced(String[] k, char[] v, int offset, int n) {
        if (n >= 1) {
            int m = n >> 1;
            insert(k[m + offset], v[m + offset]);
            insertBalanced(k, v, offset, m);
            insertBalanced(k, v, (offset + m) + 1, (n - m) - 1);
        }
    }

    public void balance() {
        int i = 0;
        int n = this.length;
        String[] k = new String[n];
        char[] v = new char[n];
        Iterator iter = new Iterator();
        while (iter.hasMoreElements()) {
            v[i] = iter.getValue();
            int i2 = i + 1;
            k[i] = iter.nextElement();
            i = i2;
        }
        init();
        insertBalanced(k, v, 0, n);
    }

    public void trimToSize() {
        balance();
        redimNodeArrays(this.freenode);
        CharVector kx = new CharVector();
        kx.alloc(1);
        compact(kx, new TernaryTree(), this.root);
        this.kv = kx;
        this.kv.trimToSize();
    }

    private void compact(CharVector kx, TernaryTree map, char p) {
        if (p != '\u0000') {
            if (this.sc[p] == '￿') {
                int k = map.find(this.kv.getArray(), this.lo[p]);
                if (k < 0) {
                    k = kx.alloc(strlen(this.kv.getArray(), this.lo[p]) + 1);
                    strcpy(kx.getArray(), k, this.kv.getArray(), this.lo[p]);
                    map.insert(kx.getArray(), k, (char) k);
                }
                this.lo[p] = (char) k;
                return;
            }
            compact(kx, map, this.lo[p]);
            if (this.sc[p] != '\u0000') {
                compact(kx, map, this.eq[p]);
            }
            compact(kx, map, this.hi[p]);
        }
    }

    public Enumeration<String> keys() {
        return new Iterator();
    }

    public void printStats() {
        System.out.println("Number of keys = " + Integer.toString(this.length));
        System.out.println("Node count = " + Integer.toString(this.freenode));
        System.out.println("Key Array length = " + Integer.toString(this.kv.length()));
    }
}
